package Client;

import Shared.Book;
import Shared.Inventory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class AdminLibraryController {

    Client client;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    Inventory clientSideInventory;
    Object objectRecieved;

    @FXML
    Button bookButton;
    @FXML
    Button movieButton;
    @FXML
    Button gameButton;
    @FXML
    Button audiobookButton;


    public AdminLibraryController() {
        client = new Client();
        client.setupNetworking();
        clientSideInventory = new Inventory();

        System.out.println("admin socket created");


        try {
            // maybe put the input stream in another thread and do that

            objectOutputStream = new ObjectOutputStream(client.clientSocket.getOutputStream());
//            objectOutputStream.flush();

            objectInputStream = new ObjectInputStream(client.clientSocket.getInputStream());

        } catch (IOException ioException) { ioException.printStackTrace(); }
        System.out.println("Contoller setup networking");


        // create and start thread to listen for objects coming in from server
        Thread objectListenerThread = new Thread(() -> {

            try {
                objectInputStream = new ObjectInputStream(client.clientSocket.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // need while true?
            try {
                if ((objectRecieved = (objectInputStream.readObject())) != null ) {
                    System.out.println("admin recieved object from server");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


        });
        objectListenerThread.start();
    }

    public void AddBook() {
        System.out.println("book selected");
        Book book = new Book("Glass Castle", "good book", "J. Walls", 288);
        try {
            objectOutputStream.writeObject(book);
            objectOutputStream.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }        // send server the book and have it store in inventory
    }

    public void AddMovie() {

    }

    public void AddGame() {

    }

    public void AddAudiobook() {

    }
}
