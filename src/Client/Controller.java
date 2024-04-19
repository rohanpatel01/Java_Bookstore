package Client;

import Shared.Inventory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import Shared.Book;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class Controller {

    @FXML
    Button uniqueBook;

    Client client;

    ObjectOutputStream objectOutputStream;

    public Controller() {
        client = new Client();
        client.setupNetworking();

        try {
            objectOutputStream = new ObjectOutputStream(client.clientSocket.getOutputStream());
        } catch (IOException ioException) { ioException.printStackTrace(); }
        System.out.println("Contoller setup networking");
    }

    @FXML
    public void bookSelected() {
        System.out.println("book selected");
        Book book = new Book("Glass Castle", "good book", "J. Walls", 288);
        System.out.println("controller socket: " + client.clientSocket);
        try {
            objectOutputStream.writeObject(book);
            objectOutputStream.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }


}
