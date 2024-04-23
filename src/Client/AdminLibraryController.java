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
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    Inventory inventory;
    Object objectRecievedFromServer;

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
        inventory = new Inventory();

        System.out.println("admin socket created");

        try {
            objectOutputStream = new ObjectOutputStream(client.clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(client.clientSocket.getInputStream());
        } catch (IOException ioException) { ioException.printStackTrace(); }

        // create and start thread to listen for objects coming in from server
        Thread objectListenerThread = new Thread(() -> {

            try {
                while (true) {
                    if ((objectRecievedFromServer = (objectInputStream.readObject())) != null ) {
                        inventory.bookList.put(((Book) objectRecievedFromServer).title, (Book) objectRecievedFromServer);
                        inventory.printInventory();

                    }

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
        Book book = new Book("Glass Castle", "good book", "J. Walls", 288, 1);
        try {
            objectOutputStream.writeObject(book);
            objectOutputStream.flush();
            System.out.println("sending: " + book);

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
