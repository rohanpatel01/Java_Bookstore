package Client;

import Shared.*;
import com.mongodb.client.MongoCursor;
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

public class LoginController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private boolean isAdmin = false;

    Client client;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    Object objectRecieved;

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
                        if (objectRecieved instanceof Boolean) {
                            System.out.println("woohoo");
                            System.out.println(((Boolean) objectRecieved).booleanValue());
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

            try (MongoCursor<User> cursor = MongoDBManager.userCollection.find().iterator()) {
                while (cursor.hasNext()) {
                    User currentUser = cursor.next();
                    if (currentUser.username.equals(username) && currentUser.password.equals(password)) {

                       if (currentUser.isAdmin) { // admin login

                           try {
                               root = FXMLLoader.load(getClass().getResource("AdminLibrary.fxml"));
                           } catch (IOException ioException) { ioException.printStackTrace(); }

                       } else { // member login

                           try {
                               root = FXMLLoader.load(getClass().getResource("MemberLibrary.fxml"));
                           } catch (IOException ioException) { ioException.printStackTrace(); }
                       }

                        // load user
                        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        scene.setUserData(user);
                        stage.show();


                    } else {
                        System.out.println("ERROR: wrong username or password. please try again");
                    }
                }
            }
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

            System.out.println("send user");



//            try (MongoCursor<User> cursor = MongoDBManager.userCollection.find().iterator()) {
//                while (cursor.hasNext()) {
//                    User currentUser = cursor.next();
//                    if (currentUser.username.equals(username)) {
//                        System.out.println("ERROR: User already signed up");
//                        return;
//                    }
//                }
//            }

//            if (isAdmin) {
////                adminCredentials.put(username, password);
//                user = new User(username, password, true);
//            } else {
////                memberCredentials.put(username, password);
//                user = new User(username, password, false);
//            }
//            MongoDBManager.userCollection.insertOne(user);
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
