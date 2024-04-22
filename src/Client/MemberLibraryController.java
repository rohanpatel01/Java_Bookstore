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
            try {
                if ((objectRecievedFromServer = objectInputStream.readObject()) != null) {
                   inventory.addToInventory(objectRecievedFromServer);
                    System.out.println("member library");
                   inventory.printInventory();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
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
