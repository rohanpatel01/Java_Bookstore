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
    Object objectRecievedFromServer;
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
//    @FXML
//    HBox booksHBox;
//    @FXML
//    HBox booksListHBox;
    @FXML
    HBox booksHBox;

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
                   if ((objectRecievedFromServer = objectInputStream.readObject()) != null) {
                       inventory.bookList.put(((Book) objectRecievedFromServer).title, (Book) objectRecievedFromServer);
                       System.out.println("recieved object: ");
                       inventory.printInventory();
                       if ( inventory.bookList.get(((Book) objectRecievedFromServer).title).numCopies <= 0) { //((Book) objectRecievedFromServer).numCopies
                            for (Node node : booksHBox.getChildren()) {
                                if (node.getId() != null && node.getId().equals(((Book) objectRecievedFromServer).title)) {
//                                    System.out.println("delete node");
                                    Platform.runLater(() -> {
//                                        System.out.println("remove: " + node);
                                        booksHBox.getChildren().remove(node);
                                    });
                                }
                            }
                       } else {
//
                           // to check if need to create we look through and see if card exists with fxid of the book name
                           boolean isCardPresent = false;
//                           System.out.println(booksHBox);

                            for (Node node : booksHBox.getChildren()) {
                                // if item exists update the number on card to whatever the number of copies was given from server
                                if (node.getId() != null && node.getId().equals(((Book) objectRecievedFromServer).title)) {
                                   isCardPresent = true;
//                                    System.out.println("updating number on card");
                                    Platform.runLater(() -> {
                                        String newButtonName = (((Book) objectRecievedFromServer).title) +(((Book) objectRecievedFromServer).numCopies)  + "";
                                        ((Button) node.lookup(".button")).setText(newButtonName);
                                    });
                                }
                            }

                            if (!isCardPresent) {
                                System.out.println("create card");
                                Platform.runLater(() -> {
                                    HBox createdBookCard = new HBox();
                                    createdBookCard.setId(((Book) objectRecievedFromServer).title);
                                    Button checkoutButton = new Button( ((Book) objectRecievedFromServer).title + ((Book) objectRecievedFromServer).numCopies );
                                    System.out.println("create card object: " + objectRecievedFromServer);
                                    checkoutButton.setOnAction(event -> bookSelected(event));  // , (Book) objectRecievedFromServer)
                                    createdBookCard.getChildren().add(checkoutButton);
                                    booksHBox.getChildren().add(createdBookCard);
                                });
                            }

                        }
                   }
//                       inventory.printInventory();

               }  catch (IOException| ClassNotFoundException  exception) {exception.printStackTrace(); }
           }
       }
   }


    // TODO: make something general that just sends over to server and adds to card without worrying about it
    @FXML
    public void bookSelected(ActionEvent event) {  // , Book inventoryBookSelected
//        System.out.println("book selected");
//        Book book = new Book("Glass_Castle", "good book", "J. Walls", 288, -1);
        // get the button that created the item
        // gets its id
        // find the book in inventory
        // create a book and change num, copies,
        // add it to card and send it over

        // cart should have numCopies as 1
        // object sent should have numCopies as -1

        Button triggeredButton = (Button) event.getSource();
        Book inventoryBook = inventory.bookList.get(triggeredButton.getParent().getId());
        Book checkoutBook = new Book(inventoryBook.title, inventoryBook.summaryDescription, inventoryBook.author, inventoryBook.numPages, 1);

        try {
            checkoutBook.numCopies = -1; // so server can process as removing from inventory
            objectOutputStream.writeObject(checkoutBook);
            objectOutputStream.flush();

        } catch (IOException ioe) { ioe.printStackTrace(); }

        if (cart.cartItems.get(checkoutBook.title) != null) {
            cart.cartItems.get(checkoutBook.title).numCopies += 1;
            System.out.println("update cart item");
        } else {
            cart.addItemToCart(checkoutBook);
            System.out.println("create cart item");
        }


//        cart.add(checkoutBook); // TODO: setting to -1 might change things idk
        cart.addItemToCart(checkoutBook);
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






    @FXML
    public void returnBook(Book item){
       // send same book but with 1 as numCopies so server knows to add it to inventory
        item.numCopies = 1;
        try {
            objectOutputStream.writeObject(item);
            objectOutputStream.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        // need to delete button (should be entire book/item card once made)

    }
}
