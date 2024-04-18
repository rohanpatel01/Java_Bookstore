package Client;

import Shared.TestSend;
import javafx.application.Platform;
import javafx.fxml.FXML;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import Shared.Book;

public class Client {


    Socket clientSocket;

    @FXML
    Button uniqueBook;

    public void initialize() {
        // put any code to run before grid here
        // maybe about retrieving items from database or something idk
    }

    public void bookSelected(){
        Object book = new Book("Glass Castle", "Description", "Author", 288);
        try {
            System.out.println("Socket: " + clientSocket);
            System.out.println("Book: " + book);
            sendObject(clientSocket, book);
        } catch (IOException ioException) { ioException.printStackTrace(); }

    }

    public static void main(String[] args) {
        Thread clientNetworkingThread = new Thread(() -> {
            new Client().setupNetworking();
        });
        clientNetworkingThread.start();
    }

    private void setupNetworking() {
        try {
            System.out.println("attempting client socket creation");
            clientSocket = new Socket("10.154.144.2", 1024);
            System.out.println("client socket created");
            System.out.println("network established, clientSocket: " + clientSocket);

//            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
//            BufferedReader reader = new BufferedReader((new InputStreamReader(clientSocket.getInputStream())));

//            Scanner scanner = new Scanner(System.in); // is blocking until new content is read

            while (true) {
//                synchronized (currentObject) {
//                    if (currentObject instanceof Book) {
//                        System.out.println("Sending book");
//                        sendObject(clientSocket, currentObject);
//                        currentObject = new Object(); // set it back to null after processed bc dont want keep sending
//
//                    }
//                }
//
            }
        } catch (IOException ioe) { ioe.printStackTrace(); }
    }

    public void sendObject(Socket socket, Object object) throws IOException {
        System.out.println("attempt send book");
        System.out.println("sendObject socket: " + socket);
        System.out.println("send object object: " + object);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(object); // dont hardcode the type
        objectOutputStream.flush();
        System.out.println("sent book");
    }

}





//class SendObject implements Runnable {
//    private Socket socket;
//    private Object object;
//    public SendObject(Socket socket, Object object) {
//       this.socket = socket;
//       this.object = object;
//    }
//
//    // should have list or something and pull from there so we can click multiple things
//   @Override
//   public void run() {
//        try {
//
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//            objectOutputStream.writeObject(object);
//            objectOutputStream.flush();
//
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        }
//
//   }
//}