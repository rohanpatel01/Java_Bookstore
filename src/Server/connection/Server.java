package Server.connection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import Shared.*;

public class Server {

    Inventory inventory;
    ArrayList<Socket> clientList;

    public static void main(String[] args) {
        new Server().setupNetworking();
    }

    private void setupNetworking() {
        Object objectRecieved;
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        Socket clientSocket;

        clientList = new ArrayList<>();
        inventory = new Inventory();

        try {
            ServerSocket server = new ServerSocket(1024);
            while (true) {
                clientSocket = server.accept();
                clientList.add(clientSocket);
                System.out.println("client connected");

                objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

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





        }
        public void run() {

            Thread objectReaderThread = new Thread(() -> {
                try {
                    while (true) { // should have a while true to recieve objects?
                        if ((objectRecieved = objectInputStream.readObject()) != null) {

                            inventory.addToInventory(objectRecieved);
//                            addToInventory(objectRecieved);
                            inventory.printInventory();
                            sendToAllClients(objectRecieved, objectOutputStream);

                        }
                    }

                } catch (IOException ioe) { ioe.printStackTrace(); }
                catch (ClassNotFoundException classNotFoundException) { classNotFoundException.printStackTrace(); }
            });

            objectReaderThread.start();
        }
    }

    private void sendToAllClients(Object object, ObjectOutputStream objectOutputStream) {
        Thread sendObjectToAllClientsAsync = new Thread(() -> {
            for (Socket client : clientList) {
                System.out.println(client.toString());

                try {
                    objectOutputStream.writeObject(object);
                    objectOutputStream.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        sendObjectToAllClientsAsync.start();
    }


//    private void addToInventory(Object objectRecieved) {
//
//
//        if (objectRecieved instanceof Book) {
//            inventory.addBook((Book) objectRecieved);
//
//        } else if (objectRecieved instanceof Movie) {
//            inventory.addMovie((Movie) objectRecieved);
//
//        } else if (objectRecieved instanceof Game) {
//            inventory.addGame((Game) objectRecieved);
//
//        } else if (objectRecieved instanceof AudioBook) {
//           inventory.addAudiobook((AudioBook) objectRecieved);
//        }
//
//    }

}
