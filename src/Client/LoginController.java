package Client;

import Shared.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;


import javafx.scene.Node;


import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Shared.User;
import javafx.util.Duration;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class LoginController {

    public static User currentUser;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private boolean isAdmin = false;

    Client client;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    Object objectRecieved;
    User user;
    User recievedUser; // default value for user that is invalid
    private boolean mongoCommFlag = false;
    private final Object lock = new Object();

    private String salt = "salty:D";

    String pastelRedHex = "#ff7675";
    String pastelGreenHex = "#55efc4";
    double warningNotificationTime = 1.5;


    @FXML
    TitledPane titledPane;
    @FXML
    Button signupButton;
    @FXML
    Button loginButton;
    @FXML
    TextField createUser;
    @FXML
    TextField createPassword;
    @FXML
    TextField loginUser;
    @FXML
    TextField loginPassword;
    @FXML
    Button changeUserButton;
    @FXML
    Button exitButton;
    @FXML
    Label notificationLabel;
    @FXML
    Hyperlink forgotPasswordHyperLink;
    @FXML
    Label newPasswordLabel;
    @FXML
    TextField newPasswordField;


    SecretKey encryptionKey;
    boolean useNewPassword = false;


    public void initialize() {



        try {
            encryptionKey = generateSecretKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public LoginController() {
        client = new Client();
        client.setupNetworking();

        try {
            objectOutputStream = new ObjectOutputStream(client.clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(client.clientSocket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Thread t = new Thread(new ObjectReader()); //, objectOutputStream
        t.start();

    }

   class ObjectReader implements Runnable {

        public void run() {
            while (true) {
                try {
                    if ((objectRecieved = objectInputStream.readObject()) != null) {
                        if (objectRecieved instanceof User) {
                            synchronized (lock) {
                                recievedUser = (User) objectRecieved;
                                System.out.println("object recieved: " + (User) objectRecieved);
                                System.out.println("recieved user value: " + recievedUser);
                                mongoCommFlag  = true;
                                lock.notifyAll();

                            }
                        }
                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
   }


    @FXML
    public void loginButton(ActionEvent event) {


        String username = loginUser.getText();
        String password;

        if (useNewPassword) {
            password = newPasswordField.getText();
        } else {
            password = loginPassword.getText();
        }


        if (!(username.isEmpty() || password.isEmpty())) {


            User sendUser = new User(username, password, false);

            try {
                objectOutputStream.writeObject(sendUser);
                objectOutputStream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Thread t = new Thread(() -> {
                synchronized (lock) {
                    while (!mongoCommFlag ) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                    mongoCommFlag = false; // reset flag

                    if (recievedUser.username.equals(username)) {

                        if (recievedUser.isAdmin) { // admin login

                            try {
                                root = FXMLLoader.load(getClass().getResource("AdminLibrary.fxml"));
                            } catch (IOException ioException) { ioException.printStackTrace(); }

                        } else { // member login

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("MemberLibrary.fxml"));
                                root = loader.load();

                                // get the user for the member
                                MemberLibraryController memberLibraryController = loader.getController();
//                                memberLibraryController.setUser(user);
                                memberLibraryController.setUser(recievedUser);
                                System.out.println("login controller found user cart items: " + recievedUser);


                            } catch (IOException ioException) { ioException.printStackTrace(); }
                        }

                        // load user
                        Platform.runLater(() -> {
                                stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                                scene = new Scene(root);
                                stage.setScene(scene);
                                stage.show();
                            System.out.println("show new stage");

                        });

                    } else  {

                        Platform.runLater(() -> {
                            notificationLabel.setText("WARNING: Invalid username or password. Please try again.");
                            notificationLabel.setBackground(new Background(new BackgroundFill(getColorFromHex(pastelRedHex), new CornerRadii(5), null))); // red

                            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(warningNotificationTime), action -> {
                                // reset warning label to disappear
                                notificationLabel.setText("");
                                notificationLabel.setBackground(Background.EMPTY);
                            }));
                            timeline.setCycleCount(1); // Run only once
                            timeline.play();


                        });

                    }

                }
            });
            t.start();

        }
    }

    @FXML
    public void signupButton(ActionEvent event) {

        String username = createUser.getText();
        String password = createPassword.getText();

        if (!(username.isEmpty()) || password.isEmpty()) {


            if (!isPasswordStrong(password)) {
                Platform.runLater(() -> {

                    notificationLabel.setBackground(new Background(new BackgroundFill(getColorFromHex(pastelRedHex), new CornerRadii(5), null)));
                    notificationLabel.setText("Password is too weak. Try again");

                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(warningNotificationTime), action -> {
                        // reset warning label to disappear
                        notificationLabel.setText("");
                        notificationLabel.setBackground(Background.EMPTY);
                    }));
                    timeline.setCycleCount(1); // Run only once
                    timeline.play();
                });
                return;
            }

            Platform.runLater(() -> {
                notificationLabel.setBackground(new Background(new BackgroundFill(getColorFromHex(pastelGreenHex), new CornerRadii(5), null)));
                notificationLabel.setText("successful sign up");

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(warningNotificationTime), action -> {
                    // reset warning label to disappear
                    notificationLabel.setText("");
                    notificationLabel.setBackground(Background.EMPTY);
                }));
                timeline.setCycleCount(1); // Run only once
                timeline.play();
            });

            String saltedPassword = password + salt;
            User sendUser = new User(username, saltedPassword, false);

            try {
                sendUser.password = encrypt(sendUser.password, encryptionKey);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            sendUser.isSignup = true;


            try {
                objectOutputStream.writeObject(sendUser);
                objectOutputStream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            Thread t = new Thread(() -> {
                synchronized (lock) {
                    while(!mongoCommFlag ) {
                        try {
                            // Wait until mongoBoolean is set
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    mongoCommFlag  = false; // reset back to false


                    // invalid here meaning that the user has not already created account and we can do so
                    if (recievedUser.username.equals("invalid")) {
                        // TODO: should not be adding to mongo here, rather just make the user so when we login we have it
                        if (isAdmin) {
                            user = sendUser;
                            user.isAdmin = true;
                        } else {
                            user = sendUser;
                            user.isAdmin = false;
                        }

                    } else {
                        Platform.runLater(() -> {

                            notificationLabel.setBackground(new Background(new BackgroundFill(getColorFromHex("#ff7675"), new CornerRadii(5), null))); // red
                            notificationLabel.setText("WARNING: This username is already taken. Please select another or login.");

                            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(warningNotificationTime), action -> {
                                // reset warning label to disappear
                                notificationLabel.setText("");
                                notificationLabel.setBackground(Background.EMPTY);
                            }));
                            timeline.setCycleCount(1); // Run only once
                            timeline.play();
                        });
                    }
                }
            });
            t.start();

        }
    }

    @FXML
    public void changeUser(ActionEvent event) {

        isAdmin = !isAdmin;
        String titledPaneText = isAdmin ? "Admin Login":"Member Login";
        String changeUserButtonText = isAdmin ? "Change to Member":"Change to Admin";
        titledPane.setText(titledPaneText);
        changeUserButton.setText(changeUserButtonText);

    }

    @FXML
    public void exit(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("ExitPage.fxml"));
            stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ioException) { ioException.printStackTrace(); }
    }


    private static SecretKey generateSecretKey() throws Exception {
        // Generate a secret key using AES algorithm
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // You can use 128, 192, or 256
        return keyGenerator.generateKey();
    }


    public static String encrypt(String input, Key key) throws Exception {
        // Create cipher object
        Cipher cipher = Cipher.getInstance("AES");

        // Initialize cipher to encryption mode
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // Encrypt the input string
        byte[] encryptedBytes = cipher.doFinal(input.getBytes());

        // Encode the encrypted bytes to base64 for easy storage or transmission
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }


    public static String decrypt(String encryptedInput, Key key) throws Exception {
        // Create cipher object
        Cipher cipher = Cipher.getInstance("AES");

        // Initialize cipher to decryption mode
        cipher.init(Cipher.DECRYPT_MODE, key);

        // Decode the base64 string to get the encrypted bytes
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedInput);

        // Decrypt the bytes
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Convert the decrypted bytes back to string
        return new String(decryptedBytes);
    }

    public void forgotPassword(ActionEvent event) {
        useNewPassword = true;

        ((Hyperlink) event.getSource()).setVisible(false);
        ((TextField) loginPassword).setDisable(true);

        ((Label) newPasswordLabel).setVisible(true);
        ((TextField) newPasswordField).setVisible(true);


    }

    private boolean isPasswordStrong(String password) {

        if (password.length() < 8) { return false; }
        if (!password.matches("[a-zA-Z0-9]")) { return true; }
        return true;
    }

    private Color getColorFromHex(String hexColor) {

        int red = Integer.valueOf(hexColor.substring(1, 3), 16);
        int green = Integer.valueOf(hexColor.substring(3, 5), 16);
        int blue = Integer.valueOf(hexColor.substring(5, 7), 16);
        return Color.rgb(red, green, blue);
    }



}
