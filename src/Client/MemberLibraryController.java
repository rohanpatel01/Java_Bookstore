package Client;

import Server.connection.Server;
import Shared.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
//import java.awt.event.ActionEvent;
import java.io.IOException;
//import java.io.ObjectInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MemberLibraryController {

    Client client;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    Inventory inventory;
    LibraryItem objectRecievedFromServer;
    Cart cart;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    Button uniqueBook;
    @FXML
    Button uniqueMovie;
    @FXML
    Button uniqueAudiobook;
    @FXML
    Button uniqueGame;
    @FXML
    VBox cartVBox;
    @FXML
    HBox booksHBox;
    @FXML
    HBox moviesHBox;
    @FXML
    HBox gamesHBox;
    @FXML
    HBox audiobooksHBox;
    @FXML
    Button exitButton;

    public void initialize() {
//        System.out.println("initialize: booksHBox: " + booksHBox);
        client = new Client();
        client.setupNetworking();
        inventory = new Inventory();
        cart = new Cart();

        // populate item with cart


        try {
            objectOutputStream = new ObjectOutputStream(client.clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(client.clientSocket.getInputStream());

//            System.out.println("member");
        } catch (IOException ioException) { ioException.printStackTrace(); }


        Thread t = new Thread(new ObjectReader()); //, objectOutputStream
        t.start();
    }


    class ObjectReader implements Runnable{

       @Override
       public void run() {
           while (true) {
               try {
                   // allow for all types of items
                   // based on what type of item - have method to determine type then make index correct one for inventory big
                    // need to change what hbox we target based on what type of item it is

                   if ((objectRecievedFromServer = (LibraryItem) objectInputStream.readObject()) != null) {

                       int itemIndex = inventory.determineItemType(objectRecievedFromServer);
                       HBox itemHbox = determineItemHbox(objectRecievedFromServer);

                       inventory.inventoryLists.get(itemIndex).put(( objectRecievedFromServer).title, objectRecievedFromServer);
                       inventory.printInventory();
                       cart.printCart();

                       // handle card creation and modification
                       int currentItemCount = inventory.inventoryLists.get(itemIndex).get(( objectRecievedFromServer).title).numCopies;
                       if (currentItemCount == 0) {
                           boolean foundCard = false;
                           for (Node node : itemHbox.getChildren()) {
                               if (node.getId() != null && node.getId().equals(( objectRecievedFromServer).title)) {
                                   foundCard = true;
                                   // found card - means card was checked out and thus needs to decrease in count and disable button
                                   Platform.runLater(() -> {
                                       String newButtonName = (( objectRecievedFromServer).title) + ((objectRecievedFromServer).numCopies)  + "";
                                       ((Button) node.lookup(".button")).setText(newButtonName);
                                      node.setDisable(true);
                                   });
                               }
                           }
                           // card does not exist - means loaded into from inventory and need to create card for it
                           if (!foundCard) {
                               HBox createdBookCard = new HBox();
                               createdBookCard.setId(( objectRecievedFromServer).title);
                               Button checkoutButton = new Button( ( objectRecievedFromServer).title + ( objectRecievedFromServer).numCopies );
                               checkoutButton.setUserData(itemIndex);
                               // give the button some user data so we can tell later what type of object the button corresponds to
                               checkoutButton.setOnAction(event -> itemSelected(event));  // , (Book) objectRecievedFromServer)

                               if (objectRecievedFromServer.numCopies == 0) {
                                   checkoutButton.setDisable(true);
                               }

                               Platform.runLater(() -> {
                                   createdBookCard.getChildren().add(checkoutButton);
                                   itemHbox.getChildren().add(createdBookCard);
                               });
                           }

                       } else { // item is greater or less than 0 - just update the count and update


                           // if already there update, else create
                           boolean foundCard = false;
                           for (Node node : itemHbox.getChildren()) {
                               if (node.getId() != null && node.getId().equals(( objectRecievedFromServer).title)) {
                                   foundCard = true;
                                   Platform.runLater(() -> {
                                       String newButtonName = (( objectRecievedFromServer).title) +(( objectRecievedFromServer).numCopies)  + "";
                                       ((Button) node.lookup(".button")).setText(newButtonName);
                                       node.setDisable(false); // shouldn't need this
                                   });
                               }
                           }
                           // adding new item that was not in inventory before - create card
                           if (!foundCard) {
                               HBox createdBookCard = new HBox();
                               createdBookCard.setId(( objectRecievedFromServer).title);
                               Button checkoutButton = new Button( ( objectRecievedFromServer).title + ( objectRecievedFromServer).numCopies );
                               checkoutButton.setUserData(itemIndex);
                               // give the button some user data so we can tell later what type of object the button corresponds to
                               checkoutButton.setOnAction(event -> itemSelected(event));  // , (Book) objectRecievedFromServer)

                               // shouldn't be the case
//                               if (objectRecievedFromServer.numCopies == 0) {
//                                   checkoutButton.setDisable(false);
//                               }

                               Platform.runLater(() -> {
                                   createdBookCard.getChildren().add(checkoutButton);
                                   itemHbox.getChildren().add(createdBookCard);
                               });
                           }


                       }

                   }

               }  catch (IOException| ClassNotFoundException  exception) {exception.printStackTrace(); }
           }
       }
   }


    // TODO: make something general that just sends over to server and adds to card without worrying about it
    @FXML
    public void itemSelected(ActionEvent event) {

        Button triggeredButton = (Button) event.getSource();
        // TODO: just to make pretty see if can use item type by creating variable and using below
        LibraryItem copy = null;

        if (triggeredButton.getUserData().equals(0)) {
            Book item = (Book) inventory.bookList.get(triggeredButton.getParent().getId());
            copy = new Book(item.title, item.summaryDescription, item.author, item.numPages, 1);
        } else if (triggeredButton.getUserData().equals(1)) {
            // TODO: issue cannot have
            Movie item = (Movie) inventory.movieList.get(triggeredButton.getParent().getId());
            copy = new Movie(item.title, item.summaryDescription, item.movieRunTime, item.director, 1);
        } else if (triggeredButton.getUserData().equals(2)) {
            Game item = (Game) inventory.gameList.get(triggeredButton.getParent().getId());
            copy = new Game(item.title, item.summaryDescription, item.developerStudio, 1);
        } else if (triggeredButton.getUserData().equals(3)) {
            AudioBook item = (AudioBook) inventory.audiobookList.get(triggeredButton.getParent().getId());
            copy = new AudioBook(item.title, item.summaryDescription, item.narrator, 1);
        }

        try {
            copy.numCopies = -1; // so server can process as removing from inventory
            objectOutputStream.writeObject(copy);
            objectOutputStream.flush();

        } catch (IOException ioe) { ioe.printStackTrace(); }

        copy.numCopies = 1;
        addItemToCart(copy);
    }

    // TODO: need to generalize this to be able to handle all types of objects
    @FXML
    public void returnBook(ActionEvent event){
        // todo: this is goofy look at this again
        Button returnBookButton =  (Button) event.getSource();
        int itemType = (int) returnBookButton.getUserData();
        HBox cartBookCard = (HBox) returnBookButton.getParent();
        String itemName = cartBookCard.getId();
        LibraryItem copy = null;
        int count; // should never be default

        if (itemType == 0) {
            Book item = (Book) cart.cartItems.get(itemName);
            count = item.numCopies -= 1;
            copy = new Book(item.title, item.summaryDescription, item.author, item.numPages, 1);
        } else if (itemType == 1) {
            Movie item = (Movie) cart.cartItems.get(itemName);
            count = item.numCopies -= 1;
            copy = new Movie(item.title, item.summaryDescription, item.movieRunTime, item.director, 1);
        } else if (itemType == 2) {
            Game item = (Game) cart.cartItems.get(itemName);
            count = item.numCopies -= 1;
            copy = new Game(item.title, item.summaryDescription, item.developerStudio, 1);
        } else if (itemType == 3) {
            AudioBook item = (AudioBook) cart.cartItems.get(itemName);
            count = item.numCopies -= 1;
            copy = new AudioBook(item.title, item.summaryDescription, item.narrator, 1);
        } else {
            System.out.println("badddddd");
            count = -99; // should never happen
        }

        if (count <= 0) {
            Platform.runLater(() -> {
                cart.cartItems.remove(itemName); // need to remove from cart as well
                cartVBox.getChildren().remove(cartBookCard);
                System.out.println("removing card");

            });
        } else {
            Platform.runLater(() -> {
                String newButtonName = itemName  + " " + count;
                returnBookButton.setText(newButtonName);
            });

        }

        try {
            System.out.println("sending: " + copy);
            objectOutputStream.writeObject(copy);
            objectOutputStream.flush();

        } catch (IOException ioe) { ioe.printStackTrace(); }

    }

    @FXML
    public void exit(ActionEvent event) {
        System.out.println("exit");
        try {
            root = FXMLLoader.load(getClass().getResource("ExitPage.fxml"));
            stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ioException) { ioException.printStackTrace(); }
    }

    private void addItemToCart(LibraryItem item) {

        if (cart.cartItems.get(item.title) != null) {
            cart.cartItems.get(item.title).numCopies += item.numCopies;

           for (Node node : cartVBox.getChildren()) {
            if (node.getId() != null && node.getId().equals(item.title)) {
                Platform.runLater(() -> {
                    String newButtonName = (item.title + " " + cart.cartItems.get(item.title).numCopies);
                    ((Button) node.lookup(".button")).setText(newButtonName);
                });
            }
        }


            System.out.println("update cart item");
        } else {
            cart.add(item);
            System.out.println("create cart item");
            HBox cartCardHBox = new HBox();
            cartCardHBox.setId(item.title);
            Button returnButton = new Button( item.title + " " + item.numCopies );
            returnButton.setUserData(item.itemType); // set the item type for the returnButton so we know what item we're dealing with
            returnButton.setOnAction(event -> returnBook(event));  // , (Book) objectRecievedFromServer)
            cartCardHBox .getChildren().add(returnButton);
            cartVBox.getChildren().add(cartCardHBox);
        }
    }


    private HBox determineItemHbox(LibraryItem item) {

        if (item instanceof Book) {
            return booksHBox;
        } else if (item instanceof Movie) {
            return moviesHBox;
        } else if (item instanceof Game) {
            return gamesHBox;
        } else if (item instanceof AudioBook){
            return audiobooksHBox;
        }

        return null; // will never happen
    }

}
