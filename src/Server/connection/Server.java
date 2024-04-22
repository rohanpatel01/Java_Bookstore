package Server.connection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import Shared.*;

public class Server {

    Inventory inventory;

    public static void main(String[] args) {
        new Server().setupNetworking();
    }

    private void setupNetworking() {
        Object objectRecieved;
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        Socket clientSocket;
        ArrayList<Socket> clientList = new ArrayList<>();

        PrintWriter writer = null;
        BufferedReader reader = null;

        inventory = new Inventory();

        try {
            ServerSocket server = new ServerSocket(1024);
            while (true) {
                clientSocket = server.accept();
                clientList.add(clientSocket);
                System.out.println("client connected");

                objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
//                objectOutputStream.flush(); // so stops blocking

//                objectInputStream = new ObjectInputStream(client.clientSocket.getInputStream());
                Thread t = new Thread(new ClientHandler(clientSocket, objectOutputStream, objectInputStream)); //, objectOutputStream
                t.start();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    class ClientHandler implements Runnable {

        private Socket clientSocket;
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;
        private Object objectRecieved;


        ClientHandler(Socket clientSocket, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream ) { // , ObjectOutputStream objectOutputStream
            this.clientSocket = clientSocket;

            this.objectInputStream = objectInputStream;
            this.objectOutputStream = objectOutputStream;




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
                            printInventory();

                        }
                    }

                } catch (IOException ioe) { ioe.printStackTrace(); }
                catch (ClassNotFoundException classNotFoundException) { classNotFoundException.printStackTrace(); }
            });

            objectReaderThread.start();
        }
    }

    private void sendToAllClients(Object object) {

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

    private void printInventory() {
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
