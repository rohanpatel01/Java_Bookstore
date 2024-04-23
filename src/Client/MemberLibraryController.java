package Client;

import Shared.*;
import javafx.application.Platform;
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
    ArrayList<Object> cart;
//    FXMLLoader loader;
//    Parent root;


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

    HBox something;

    public MemberLibraryController() {

        client = new Client();
        client.setupNetworking();
        inventory = new Inventory();
        cart = new ArrayList<>();

//        try {
//            loader = new FXMLLoader(getClass().getResource("your_fxml_file.fxml"));
//            root = loader.load();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        try {
            objectOutputStream = new ObjectOutputStream(client.clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(client.clientSocket.getInputStream());

            System.out.println("member");
        } catch (IOException ioException) { ioException.printStackTrace(); }

        Thread readObjectFromServerThread = new Thread(() -> {

            // to see what type of item it is and dont have to repeat just have an array that holds the various item lists in indecies
            // then based on what type of item it is we will use that corresponding list
            // same with the hbox that holds each of the items

            while (true) {
                try {
                    if ((objectRecievedFromServer = objectInputStream.readObject()) != null) {

                        // TODO:  find better way to do this for all books, see if can have general thing in inventory,
                        // TODO:  hard tho since server requires to sendAllClients where client does not need to do that
                        if (objectRecievedFromServer instanceof Book) {

                            inventory.bookList.put(((Book) objectRecievedFromServer).title, (Book) objectRecievedFromServer);

//                            System.out.println(something.getChildren());
//                            if ( inventory.bookList.get(((Book) objectRecievedFromServer).title).numCopies <= 0) { //((Book) objectRecievedFromServer).numCopies
//                                for (Node node : something.getChildren()) {
//                                    if (node.getId() != null && node.getId().equals(((Book) objectRecievedFromServer).title)) {
//                                        System.out.println("delete node");
//                                        something.getChildren().remove(node);
//                                    }
//                                }
//                            } else {
//
                                // to check if need to create we look through and see if card exists with fxid of the book name
                                boolean isCardPresent = false;

//                                System.out.println("something get children: " + something.getChildren());
//                                for (Node node : something.getChildren()){
//                                    System.out.println("hi");
//                                }


//                                for (Node node : something.getChildren()) {
//                                    // if item exists update the number on card to whatever the number of copies was given from server
//                                    if (node.getId() != null && node.getId().equals(((Book) objectRecievedFromServer).title)) {
//                                       isCardPresent = true;
//                                        System.out.println("updating number on card");
//                                        // get that node's button and change the text
////                                        System.out.println("updating to: " + inventory.bookList.get(((Book) objectRecievedFromServer).title).toString());
////                                        Platform.runLater(() -> {
////                                            ((Button) ((HBox) node).lookup(((Book) objectRecievedFromServer).title)).setText(inventory.bookList.get(((Book) objectRecievedFromServer).numCopies).toString());
////                                        });
//                                    }
//                                }
//
//                                // item is not created - create a card for it
//                                if (!isCardPresent) {
//                                    System.out.println("creating card");
//                                    // create temporary hbox for this
//                                    HBox createdBookCard = new HBox();
//                                    Button checkoutButton = new Button( ((Book) objectRecievedFromServer).title + ((Book) objectRecievedFromServer).numCopies );
//                                    createdBookCard.setId(((Book) objectRecievedFromServer).title);
//                                    something.getChildren().add(createdBookCard);
//                                    something.getChildren().add(checkoutButton);
//                                }
//
//                            }
                        }
                        inventory.printInventory();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        readObjectFromServerThread.start();
    }

    @FXML
    public void bookSelected() {
        System.out.println("book selected");
        Book book = new Book("Glass_Castle", "good book", "J. Walls", 288, -1);
        cart.add(book);

        if (inventory.bookList.get(book.title).numCopies > 0) {
            // actually create the card or whatever in the cart based on copies in inventory
            // if inventory has no copies and we hit a book then create a card with count of 1

            // create book card in cart
            HBox bookHbox = new HBox();
            Button button = new Button("some boook");
            cartVBox.getChildren().add(button);


            try {
                objectOutputStream.writeObject(book);
                objectOutputStream.flush();

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
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
