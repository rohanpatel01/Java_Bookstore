package Client;

import Shared.TestSend;
import javafx.fxml.FXML;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import Shared.Book;

public class Client {

    Object selectedObject;

    String testSendString = "";
    Socket clientSocket;

    // Controller
    @FXML
    Button uniqueBook;

    public void initialize() {
        // put any code to run before grid here
        // maybe about retrieving items from database or something idk
    }

    public void bookSelected(){
        System.out.println("book selected");
//        Book book = new Book("Glass Castle", "summary", "Jeannette Walls", 288);
//        selectedObject = book;
        testSendString = "book selected";
        System.out.println("Client socket: " + clientSocket);
    }
    public static void main(String[] args) {
        Thread clientNetworkingThread = new Thread(() -> {
            new Client().setupNetworking();
        });
        clientNetworkingThread.start();
        // need this because want to read elements that are created from onAction events and read them at same time
    }

    private void setupNetworking() {
        try {
            System.out.println("attempting client socket creation");
            clientSocket = new Socket("11.20.16.195", 1024);
            System.out.println("client socket created");
            System.out.println("network established, clientSocket: " + clientSocket);

            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader reader = new BufferedReader((new InputStreamReader(clientSocket.getInputStream())));

//            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            boolean x = true;
            while (true) {
                String input = scanner.nextLine();
                writer.println(input);
                writer.flush();

//                System.out.println("test send string: " + testSendString);
//                if (x) {
//
//                    oos.writeObject(testSendString);
//                    oos.flush();
//                    x = false;
//                }
//                if (selectedObject != null) {
//                    System.out.println("selected object not null");
////                    oos.writeObject(selectedObject);
//                    oos.writeObject(testSendString);
//                    oos.flush();
//                    selectedObject = null;
//                } else {
//                    System.out.println("SELECTED OBJECT NULL");
//                }

                String received = reader.readLine();
                System.out.println("I RECEIVED BACK: " + received);
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
