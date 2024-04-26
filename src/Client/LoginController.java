package Client;

import Shared.*;
import com.mongodb.client.MongoCursor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;


import javafx.scene.Node;


import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import Shared.User;


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
//    Boolean mongoBoolean = new Boolean(false);
    User foundUser; // default value for user that is invalid
    private boolean mongoCommFlag = false;
    private final Object lock = new Object();


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

    User user = new User();

    public void initialize() {
        System.out.println("stage: " + stage);
        System.out.println("scene: " + scene);
        System.out.println("root: " + root);
//        stage = (Stage) exitButton.getScene().getWindow();
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
//                            System.out.println("woohoo");
                            synchronized (lock) {
//                                System.out.println(((Boolean) objectRecieved).booleanValue());
//                                mongoBoolean = new Boolean(((Boolean) objectRecieved).booleanValue());
                                foundUser = (User) objectRecieved;
                                System.out.println("object recieved: " + (User) objectRecieved);
                                System.out.println("recieved user value: " + foundUser);
                                mongoCommFlag  = true;
                                lock.notifyAll();
//                                System.out.println("notify all");

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
        String password = loginPassword.getText();
        System.out.println("login button pressed ");

        // TODO: make this better by having helper methods

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

                    // true here meaning user is in the database and thus can login
                    System.out.println("Found user: " + foundUser);
                    System.out.println("expected login username: " + username);
                    if (foundUser.username.equals(username)) {
                        if (foundUser.isAdmin) { // admin login

                            try {
                                root = FXMLLoader.load(getClass().getResource("AdminLibrary.fxml"));
                            } catch (IOException ioException) { ioException.printStackTrace(); }

                        } else { // member login

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("MemberLibrary.fxml"));
                                root = loader.load();

                                // get the user for the member
                                MemberLibraryController memberLibraryController = loader.getController();
                                memberLibraryController.setUser(foundUser);


                            } catch (IOException ioException) { ioException.printStackTrace(); }
                        }
                        // to pass user into member library controller
//                        stage.setUserData(new User(username, password, foundUser.isAdmin));

                        // load user
                        Platform.runLater(() -> {
                                stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                                scene = new Scene(root);
                                stage.setScene(scene);
//                                stage.setUserData(user);
//                                System.out.println("stage user data: " + stage.getUserData());
                            root.setUserData(5);
                                currentUser = foundUser;
                                stage.show();

                        });

                    } else  {
                        System.out.println("ERROR: Invalid username or password");
                    }

                }
            });
            t.start();









//            try (MongoCursor<User> cursor = MongoDBManager.userCollection.find().iterator()) {
//                while (cursor.hasNext()) {
//                    User currentUser = cursor.next();
//                    if (currentUser.username.equals(username) && currentUser.password.equals(password)) {
//
//                    } else {
//                        System.out.println("ERROR: wrong username or password. please try again");
//                    }
//                }
//            }
        }
    }

    @FXML
    public void signupButton() {

        String username = createUser.getText();
        String password = createPassword.getText();


        if (!(username.isEmpty()) || password.isEmpty()) {

            User sendUser = new User(username, password, false);

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

                    // worked - only add to mongo if not in there
                    System.out.println("got to sign in");

                    // invalid here meaning that the user has not already created account and we can do so
                    if (foundUser.username.equals("invalid")) {
                        if (isAdmin) {
                            user = new User(username, password, true);
                        } else {
                            user = new User(username, password, false);
                        }
                        MongoDBManager.userCollection.insertOne(user);

                    } else {
                        System.out.println("client already created account");
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


}
