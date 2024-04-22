package Client;

import Shared.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
//import java.io.ObjectInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class MemberLibraryController {

    Client client;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    Inventory inventory;
    Object objectRecievedFromServer;

    @FXML
    Button uniqueBook;
    @FXML
    Button uniqueMovie;
    @FXML
    Button uniqueAudiobook;
    @FXML
    Button uniqueGame;

    public MemberLibraryController() {

        // have message reader thread, server will send message to specific client if
        // action cannot be fufilled and client should update GUI accordingly to tell user

        client = new Client();
        client.setupNetworking();
        inventory = new Inventory();

        System.out.println("member socket created");

        try {
            objectOutputStream = new ObjectOutputStream(client.clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(client.clientSocket.getInputStream());

            System.out.println("member");
        } catch (IOException ioException) { ioException.printStackTrace(); }

        Thread readObjectFromServerThread = new Thread(() -> {
            while (true) {
                try {
                    if ((objectRecievedFromServer = objectInputStream.readObject()) != null) {

                    // just update number to whatever it is
//                   inventory.addToInventory(objectRecievedFromServer);
                    // all we have to do is update the numCopies of the object
                    // but didn't add the object to our inventory yet
                    // need to update our cart and send object to server
                    // then on server getback we update our own inventory
                    // dont need to distinguish, just update whatever to our thing,
                        // if not in client inventory add it
                        // if is then just change the numCopies number
                        // if numCopies != 0 and same title as we wanted then we add one to our cart?
//                    clientHandleObject(objectRecievedFromServer);
//                    inventory.updateBook((Book) objectRecievedFromServer);
                        System.out.println("Object recieved: " + objectRecievedFromServer);
//                    System.out.println("object recieved from server");
//                    System.out.println("member library");
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



//    private void addToInventory(Object objectRecieved) {
//
//
//        if (objectRecieved instanceof Book) {
//            inventory.addBook((Book) objectRecieved);
//
//        } else if (objectRecieved instanceof Movie) {
//            inventory.addMovie((Movie) objectRecieved);
//
//        } else if (objectRecieved instanceof Game) {
//            inventory.addGame((Game) objectRecieved);
//
//        } else if (objectRecieved instanceof AudioBook) {
//            inventory.addAudiobook((AudioBook) objectRecieved);
//        }
//
//    }



    @FXML
    public void bookSelected() {
        System.out.println("book selected");
        Book book = new Book("Glass Castle", "good book", "J. Walls", 288, -1);
//        book.numCopies = -1; // testing to borrow items
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
