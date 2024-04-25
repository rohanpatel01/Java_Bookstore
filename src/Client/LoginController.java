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
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private boolean isAdmin = false;

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


//    Client client;
    Map<String, String> memberCredentials;
    Map<String, String> adminCredentials;
    User user = new User();

    public LoginController() {
        memberCredentials = new HashMap<>();
        adminCredentials = new HashMap<>();

    }

    @FXML
    public void loginButton(ActionEvent event) {

        String username = loginUser.getText();
        String password = loginPassword.getText();
        System.out.println("login button pressed ");

        // TODO: make this better by having helper methods
        if (isAdmin) {
            if (!(username.isEmpty() || password.isEmpty())) {
                if ( adminCredentials.containsKey(username) && password.equals(adminCredentials.get(username)) ){
                    System.out.println("login good");
                    try {
                        root = FXMLLoader.load(getClass().getResource("AdminLibrary.fxml"));
                        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ioException) { ioException.printStackTrace(); }
                }
            }
        } else {
            if (!(username.isEmpty() || password.isEmpty())) {
                if ( memberCredentials.containsKey(username) && password.equals(memberCredentials.get(username)) ){
                    System.out.println("login good");
                    try {
                        root = FXMLLoader.load(getClass().getResource("MemberLibrary.fxml"));
                        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        //TODO: update userData for scene so can be passed into respective scene
                        stage.show();
                    } catch (IOException ioException) { ioException.printStackTrace(); }
                }
            }

        }

    }

    @FXML
    public void signupButton() {
        String username = createUser.getText();
        String password = createPassword.getText();

        try (MongoCursor<User> cursor = MongoDBManager.userCollection.find().iterator()) {
            while (cursor.hasNext()) {
                User currentUser = cursor.next();
                if (currentUser.username.equals(username)) {
                    System.out.println("ERROR: User already signed up");
                    return;
                }
            }
        }


        if (!(username.isEmpty()) || password.isEmpty()) {
            if (isAdmin) {
                adminCredentials.put(username, password);
                user = new User(username, password, true);
            } else {
                memberCredentials.put(username, password);
                user = new User(username, password, false);
            }
            MongoDBManager.userCollection.insertOne(user);
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
