package Server.connection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import Shared.Book;
import Shared.Inventory;

public class Server {

    Inventory inventory;

    public static void main(String[] args) {
        new Server().setupNetworking();
    }

    private void setupNetworking() {
        Object objectRecieved;
        ObjectInputStream objectInputStream;
        Socket clientSocket;

        PrintWriter writer = null;
        BufferedReader reader = null;

        inventory = new Inventory();

        try {
            ServerSocket server = new ServerSocket(1024);
            while (true) {
                clientSocket = server.accept();
                System.out.println("client connected");

                objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

                Thread t = new Thread(new ClientHandler(clientSocket, objectInputStream));
                t.start();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    class ClientHandler implements Runnable {

        private Socket clientSocket;
        private ObjectInputStream objectInputStream;
        private Object objectRecieved;


        ClientHandler(Socket clientSocket, ObjectInputStream objectInputStream) {
            this.clientSocket = clientSocket;
            this.objectInputStream = objectInputStream;


            BufferedReader reader;
            PrintWriter writer;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        public void run() {


            Thread objectReaderThread = new Thread(() -> {
                try {
                    while (true) { // should have a while true to recieve objects?
                        if ((objectRecieved = objectInputStream.readObject()) != null) {
                            System.out.println("Server got object");

                            // have another method parse which object and add it to inventory
                            if (objectRecieved instanceof Book) {
                                System.out.println("BOOOKKK");
                                System.out.println( (Book) objectRecieved);
                            }
                        }
                    }

                } catch (IOException ioe) { ioe.printStackTrace(); }
                catch (ClassNotFoundException classNotFoundException) { classNotFoundException.printStackTrace(); }
            });

            objectReaderThread.start();
        }
    }
}
