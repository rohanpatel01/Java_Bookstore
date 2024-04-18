package Client;

import Shared.TestSend;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import Shared.Book;

public class Client {


//    ObjectOutputStream objectOutputStream;
    public Socket clientSocket;
    Object object = new Object();
    public Controller controller;
// = new PrintWriter(clientSocket.getOutputStream());
//   = new BufferedReader((new InputStreamReader(clientSocket.getInputStream())));
    PrintWriter writer;
    BufferedReader reader;


    public static void main(String[] args) {
        Thread clientNetworkingThread = new Thread(() -> {
            new Client().setupNetworking();
        });


        clientNetworkingThread.start();

    }

     public void setupNetworking() {
        try {
            System.out.println("attempting client socket creation");
            clientSocket = new Socket("10.145.34.2", 1024);
            System.out.println("client socket created");
            System.out.println("network established, clientSocket: " + clientSocket);

            writer = new PrintWriter(clientSocket.getOutputStream());
            reader = new BufferedReader((new InputStreamReader(clientSocket.getInputStream())));

            Thread readerThread = new Thread(() -> {
                String message;
                try {
                    if ((message = reader.readLine()) != null) {
                        System.out.println(message);
                    }

                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            });



            readerThread.start();




//            while (true ) {
//
//                if (object instanceof Book) {
//                    System.out.println("object: " + object);
//                    System.out.println("got book");
//                }
//
//            }

//            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
//            BufferedReader reader = new BufferedReader((new InputStreamReader(clientSocket.getInputStream())));

//            Scanner scanner = new Scanner(System.in);
//            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

//            while (true) {
//
//                String input = scanner.nextLine();
//                if (input.equals("send book")) {
//                    Book book = new Book("Glass Castle", "SUMMARY", "AUTHOR", 288);
////                    TestSend book = new TestSend();
////                    System.out.println("Book title: " + book.title);
//                    System.out.println("Book" + book);
//                    writer.println(input);
//                    writer.flush();
//                    sendObject(clientSocket, book);
//
//
//                } else {
//                    writer.println(input);
//                    writer.flush();
//                }
//
//                String received = reader.readLine();
//                System.out.println("I RECEIVED BACK: " + received);
//            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

//    public void sendObject(Socket socket, Object object) throws IOException {
//        System.out.println("attempt send book");
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//        objectOutputStream.writeObject(object); // dont hardcode the type
//        objectOutputStream.flush();
//        System.out.println("sent book");
//    }

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