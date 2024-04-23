package Client;

import Server.connection.Server;
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
    @FXML
    HBox booksHBox;


    public MemberLibraryController() {

        client = new Client();
        client.setupNetworking();
        inventory = new Inventory();
        cart = new ArrayList<>();

        try {
            objectOutputStream = new ObjectOutputStream(client.clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(client.clientSocket.getInputStream());

            System.out.println("member");
        } catch (IOException ioException) { ioException.printStackTrace(); }


        Thread t = new Thread(new MemberLibraryController.ObjectReader()); //, objectOutputStream
        t.start();

//        Thread readObjectFromServerThread = new Thread(() -> {
//            new ObjectReader();
//        });
//        readObjectFromServerThread.start();
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


    class ObjectReader implements Runnable{

       @Override
       public void run() {
           while (true) {
               try {
                   if ((objectRecievedFromServer = objectInputStream.readObject()) != null) {
                       System.out.println("got object from server");
                       System.out.println(cartVBox);
                   }
               } catch (IOException | ClassNotFoundException ioe) {ioe.printStackTrace(); }
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
