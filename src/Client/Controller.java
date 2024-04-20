package Client;

import Shared.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;


import javafx.scene.Node;


import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    private Stage stage;
    private Scene scene;
    private Parent root;

//    Parent libraryGUI;
//    Parent landingGUI;


    // start member fxml
    @FXML
    Button uniqueBook;
    @FXML
    Button uniqueMovie;
    @FXML
    Button uniqueAudiobook;
    @FXML
    Button uniqueGame;
    // end member fxml


    // start create/login fxml

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

    // end create/login fxml


    Client client;
    ObjectOutputStream objectOutputStream;
    Inventory clientSideInventory;
    Map<String, String> userCredentials;

    public Controller() {
        client = new Client();
        client.setupNetworking();
        clientSideInventory = new Inventory();
        userCredentials = new HashMap<>();

        // find landing and library GUI .fxml files
//        try {
//            libraryGUI = FXMLLoader.load(getClass().getResource("GUI.fxml"));
//            landingGUI = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
//        } catch (IOException ioe) {ioe.printStackTrace();}


        try {
            objectOutputStream = new ObjectOutputStream(client.clientSocket.getOutputStream());
        } catch (IOException ioException) { ioException.printStackTrace(); }
        System.out.println("Contoller setup networking");
    }

    @FXML
    public void loginButton(ActionEvent event) {

        String username = loginUser.getText();
        String password = loginPassword.getText();
        System.out.println("login button pressed ");
        if (!(username.isEmpty() || password.isEmpty())) {
            if ( userCredentials.containsKey(username) && password.equals(userCredentials.get(username)) ){
                System.out.println("login good");
                try {
                    root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
                    stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ioException) { ioException.printStackTrace(); }
            }
        }
    }

    @FXML
    public void signupButton() {
        String username = createUser.getText();
        String password = createPassword.getText();

        if (!(username.isEmpty() || password.isEmpty())) {
            userCredentials.put(username,password);
        }
    }


    @FXML
    public void bookSelected() {
        System.out.println("book selected");
        Book book = new Book("Glass Castle", "good book", "J. Walls", 288);
        try {
            objectOutputStream.writeObject(book);
            objectOutputStream.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    @FXML
    public void gameSelected() {
        System.out.println("Game selected");
        System.out.println("controller socket: " + client.clientSocket);

        Game game = new Game("League of Legends", "try to have fun", "Riot");

        try {
            objectOutputStream.writeObject(game);
            objectOutputStream.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    @FXML
    public void movieSelected() {
        System.out.println("Movie selected");

        Movie movie = new Movie("Your Name", "Great movie", "1:26:00", "Makoto Shinkai");

        System.out.println("controller socket: " + client.clientSocket);
        try {
            objectOutputStream.writeObject(movie);
            objectOutputStream.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    @FXML
    public void audiobookSelected() {
        System.out.println("Audiobook selected");

        AudioBook audioBook = new AudioBook("AtomicHabits_audiobook", "good self help", "narrator for atomic habits");

        System.out.println("controller socket: " + client.clientSocket);
        try {
            objectOutputStream.writeObject(audioBook);
            objectOutputStream.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }


}
