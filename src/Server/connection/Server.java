package Server.connection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import Shared.Book;
import Shared.TestSend;

public class Server {

    Object objectRecieved;
    ObjectInputStream objectInputStream;
    Socket clientSocket;


    public static void main(String[] args) {
        new Server().setupNetworking();
    }

    private void setupNetworking() {
        try {
            ServerSocket server = new ServerSocket(1024);
            while (true) {
                clientSocket = server.accept();
                System.out.println("client connected");

                ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                System.out.println("object input stream: " + objectInputStream);
                Thread t = new Thread(new ClientHandler(clientSocket, objectInputStream));
                t.start();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

        Thread objectReaderThread = new Thread(() -> {
            String message;
            try {
                if ((message = clientSocke.readLine()) != null) {
                    System.out.println(message);
                }

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        });

    }

    class ClientHandler implements Runnable {

        private Socket clientSocket;
        private ObjectInputStream objectInputStream;

        ClientHandler(Socket clientSocket, ObjectInputStream objectInputStream) {
            this.clientSocket = clientSocket;
            this.objectInputStream = objectInputStream;

        }
        public void run() {

              try {
                  if ((objectRecieved = objectInputStream.readObject()) != null) {
                     if (objectRecieved instanceof Book) {
                         System.out.println("got book");
                     }
                  }
              } catch (IOException e) {
                  throw new RuntimeException(e);
              } catch (ClassNotFoundException e) {
                  throw new RuntimeException(e);
              }



//            try {
//                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
//                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                String message;
//                    while ((message = reader.readLine()) != null) {
//
//                        if (message.equals("send book")) {
//                            System.out.println("Server got object");
//                            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
//                            Object recievedObject = objectInputStream.readObject();
//
//                            if (recievedObject != null) {
//                                System.out.println("recieved object");
//                                if (recievedObject instanceof Book) { // was book
////                                    System.out.println("Test send: " + ((TestSend) recievedObject).hello);
//                                    System.out.println("Item is book");
//                                    Book book = (Book) recievedObject;
//                                    System.out.println("Book title: " + book.title);
//                                    System.out.println("Book: " + book);
//                                }
//                            } else {
//                                System.out.println("IS NULL :(");
//                            }
//
//                        } else {
//
//                            // maybe put the read object stuff in another thread?
////                            if (recievedObject != null) {
////                                System.out.println("Server got book");
////
////                            } else { // normal message
//
//                            System.out.println("RECEIVED: " + message);
//                            System.out.println(clientSocket.getInetAddress());
//                            writer.println(message);
//                            writer.flush();
////                            }
//
//                        }
//                    }
//            } catch (IOException ioe) { ioe.printStackTrace(); }
//            catch (ClassNotFoundException classNotFoundException) { classNotFoundException.printStackTrace(); }
        }
    }
}
