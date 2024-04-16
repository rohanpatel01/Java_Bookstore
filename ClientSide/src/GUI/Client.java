package GUI;

import Items.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import Items.Book;
public class Client {
    Socket socket;

    // Controller
    @FXML
    Button uniqueBook;

    @FXML
    GridPane itemSelectionGridPane;


    public void initialize() {
        // put any code to run before grid here
        // maybe about retrieving items from database or something idk
    }

    public void bookSelected(){
        System.out.println("book selected");
//        Book newBook = new Book("Glass Castle", );
//        String title, String summaryDescription, String author, int numPages
        Book book = new Book("Glass Castle", "summary", "Jeannette Walls", 288);

        try {
            // need to use reflection or something to see what type of object was clicked
            // or needs to be serialized
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(book);
            oos.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }



    // Client
    public static void main(String[] args) {
        new Client().setupNetworking();
    }

    private void setupNetworking() {
        try {
            socket = new Socket("10.155.175.228", 1024);
            System.out.println("network established");

            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader reader = new BufferedReader((new InputStreamReader(socket.getInputStream())));

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine();
                writer.println(input);
                writer.flush();

                String received = reader.readLine();
                System.out.println("I RECEIVED BACK: " + received);
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
