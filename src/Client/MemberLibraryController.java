package Client;

import Server.connection.Server;
import Shared.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

    public void initialize() {
//        System.out.println("initialize: booksHBox: " + booksHBox);
        client = new Client();
        client.setupNetworking();
        inventory = new Inventory();
        cart = new Cart();

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

                       int itemIndex = determineItemType(objectRecievedFromServer);
                       HBox itemHbox = determineItemHbox(objectRecievedFromServer);

                       inventory.inventoryLists.get(itemIndex).put(( objectRecievedFromServer).title, objectRecievedFromServer);
                       inventory.printInventory();
                       cart.printCart();
                       if ( inventory.inventoryLists.get(itemIndex).get(( objectRecievedFromServer).title).numCopies <= 0) { //((Book) objectRecievedFromServer).numCopies
                            for (Node node : itemHbox.getChildren()) {
                                if (node.getId() != null && node.getId().equals(( objectRecievedFromServer).title)) {
                                    Platform.runLater(() -> {
                                        itemHbox.getChildren().remove(node);
                                    });
                                }
                            }
                       } else {
//
                           boolean isCardPresent = false;

                            for (Node node : itemHbox.getChildren()) {
                                if (node.getId() != null && node.getId().equals( objectRecievedFromServer.title)) {
                                   isCardPresent = true;
                                    Platform.runLater(() -> {
                                        String newButtonName = (( objectRecievedFromServer).title) +(( objectRecievedFromServer).numCopies)  + "";
                                        ((Button) node.lookup(".button")).setText(newButtonName);
                                    });
                                }
                            }

                            if (!isCardPresent) {
                                HBox createdBookCard = new HBox();
                                createdBookCard.setId(( objectRecievedFromServer).title);
                                Button checkoutButton = new Button( ( objectRecievedFromServer).title + ( objectRecievedFromServer).numCopies );
                                checkoutButton.setOnAction(event -> bookSelected(event));  // , (Book) objectRecievedFromServer)
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
    public void bookSelected(ActionEvent event) {  // , Book inventoryBookSelected

        Button triggeredButton = (Button) event.getSource();
        // changed to cast into book
        Book inventoryBook = (Book) inventory.bookList.get(triggeredButton.getParent().getId());
        Book checkoutBook = new Book(inventoryBook.title, inventoryBook.summaryDescription, inventoryBook.author, inventoryBook.numPages, 1);

        try {
            checkoutBook.numCopies = -1; // so server can process as removing from inventory
            objectOutputStream.writeObject(checkoutBook);
            objectOutputStream.flush();

        } catch (IOException ioe) { ioe.printStackTrace(); }

        //TODO: Add item to cart
        checkoutBook.numCopies = 1;
        addItemToCart(checkoutBook);

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
            returnButton.setOnAction(event -> returnBook(event));  // , (Book) objectRecievedFromServer)
            cartCardHBox .getChildren().add(returnButton);
            cartVBox.getChildren().add(cartCardHBox);
        }
    }

    private int determineItemType(LibraryItem item) {
       if (item instanceof Book) {
           return 0;
       } else if (item instanceof Movie) {
          return 1;
       } else if (item instanceof Game) {
           return 2;
       } else if (item instanceof AudioBook){
           return 3;
       }

       return -999; // invalid object but should never happen
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


    @FXML
    public void returnBook(ActionEvent event){
       // send same book but with 1 as numCopies so server knows to add it to inventory

     // get whatever item that caused the event
        Button returnBookButton =  (Button) event.getSource();
        HBox cartBookCard = (HBox) returnBookButton.getParent();
        String bookItemName = cartBookCard.getId();
        Book itemBook = (Book) cart.cartItems.get(bookItemName);
        int count = itemBook.numCopies -= 1;

        Book bookCopy = new Book(itemBook.title, itemBook.summaryDescription, itemBook.author, itemBook.numPages, 1);

        // TODO: fix issue due to ^^^, wont send the last item
        if (count <= 0) {
            Platform.runLater(() -> {
                cart.cartItems.remove(bookItemName); // need to remove from cart as well
                cartVBox.getChildren().remove(cartBookCard);
                System.out.println("removing card");

            });
        } else {
            Platform.runLater(() -> {
                String newButtonName = bookItemName + " " + count;
                returnBookButton.setText(newButtonName);
            });

        }

        try {
            objectOutputStream.writeObject(bookCopy);
            objectOutputStream.flush();

        } catch (IOException ioe) { ioe.printStackTrace(); }

//        item.numCopies = 1;
//        try {
//            objectOutputStream.writeObject(item);
//            objectOutputStream.flush();
//
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
        // need to delete button (should be entire book/item card once made)

    }
}
