package Server.connection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import Shared.Book;
import Shared.TestSend;

public class Server {
    public static void main(String[] args) {
        new Server().setupNetworking();
    }

    private void setupNetworking() {
        try {
            ServerSocket server = new ServerSocket(1024);
            while (true) {
                Socket clientSocket = server.accept();
                System.out.println("client connected");

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    class ClientHandler implements Runnable {

        private Socket clientSocket;

        ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        public void run() {


            try {
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message;
                    while ((message = reader.readLine()) != null) {

                        if (message.equals("send book")) {
                            System.out.println("Server got object");
                            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                            Object recievedObject = objectInputStream.readObject();

                            if (recievedObject != null) {
                                System.out.println("recieved object");
                                if (recievedObject instanceof Book) { // was book
//                                    System.out.println("Test send: " + ((TestSend) recievedObject).hello);
                                    System.out.println("Item is book");
                                    Book book = (Book) recievedObject;
                                    System.out.println("Book title: " + book.title);
                                    System.out.println("Book: " + book);
                                }
                            } else {
                                System.out.println("IS NULL :(");
                            }

                        } else {

                            // maybe put the read object stuff in another thread?
//                            if (recievedObject != null) {
//                                System.out.println("Server got book");
//
//                            } else { // normal message

                            System.out.println("RECEIVED: " + message);
                            System.out.println(clientSocket.getInetAddress());
                            writer.println(message);
                            writer.flush();
//                            }

                        }
                    }
            } catch (IOException ioe) { ioe.printStackTrace(); }
            catch (ClassNotFoundException classNotFoundException) { classNotFoundException.printStackTrace(); }
        }
    }
}
