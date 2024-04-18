package Client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import Shared.Book;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class Controller {

    @FXML
    Button uniqueBook;

    Client client;



    public Controller() {
            client = new Client();
            client.setupNetworking();


        System.out.println("Contoller setup networking");

    }

    @FXML
    public void bookSelected() {
        System.out.println("book selected");
        Book book = new Book("title", "summary", "author", 99);
        System.out.println("controller socket: " + client.clientSocket);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.clientSocket.getOutputStream());
            objectOutputStream.writeObject(book);
            objectOutputStream.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }


}
