package Server.connection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import Shared.*;

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

                            addToInventory(objectRecieved);

                            for (String keys : Inventory.bookList.keySet() )
                            {
                                System.out.println(keys + ":"+ Inventory.bookList.get(keys));
                            }
                            System.out.println("============================================");

                            for (String keys : Inventory.movieList.keySet() )
                            {
                                System.out.println(keys + ":"+ Inventory.movieList.get(keys));
                            }
                            System.out.println("============================================");


                            for (String keys : Inventory.gameList.keySet() )
                            {
                                System.out.println(keys + ":"+ Inventory.gameList.get(keys));
                            }
                            System.out.println("============================================");

                            for (String keys : Inventory.audiobookList.keySet() )
                            {
                                System.out.println(keys + ":"+ Inventory.audiobookList.get(keys));
                            }
                            System.out.println("============================================");

                        }
                    }

                } catch (IOException ioe) { ioe.printStackTrace(); }
                catch (ClassNotFoundException classNotFoundException) { classNotFoundException.printStackTrace(); }
            });

            objectReaderThread.start();
        }
    }


    private void addToInventory(Object objectRecieved) {


        if (objectRecieved instanceof Book) {
            inventory.addBook((Book) objectRecieved);

        } else if (objectRecieved instanceof Movie) {
            inventory.addMovie((Movie) objectRecieved);

        } else if (objectRecieved instanceof Game) {
            inventory.addGame((Game) objectRecieved);

        } else if (objectRecieved instanceof AudioBook) {
           inventory.addAudiobook((AudioBook) objectRecieved);
        }

    }





}
