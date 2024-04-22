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

        Book starterBook = new Book("Glass Castle", "good book", "J. Walls", 288, 5);
        inventory.bookList.put(starterBook.title, starterBook);

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

                            handleObject(objectRecieved, objectOutputStream);
                            inventory.printInventory();

                        }
                    }

                } catch (IOException ioe) { ioe.printStackTrace(); }
                catch (ClassNotFoundException classNotFoundException) { classNotFoundException.printStackTrace(); }
            });

            objectReaderThread.start();
        }
    }

    private void handleObject(Object objectReceived, ObjectOutputStream objectOutputStream) {

        if (objectReceived instanceof Book) {

            // if book does not exist in server inventory - add the item
            if (inventory.bookList.get(((Book) objectReceived).title) == null) {

                inventory.updateBook((Book) objectReceived);

            } else { // item is in inventory, see how to update depending on if adding or removing

                if (((Book) objectReceived).numCopies > 0) { // adding item to inventory

                    inventory.updateBook((Book) objectReceived);
                    sendToAllClients( inventory.bookList.get(((Book) objectReceived).title) , objectOutputStream);

                } else { // attempt to borrow that many copies of book
                    System.out.println("decrease item");
                    int currentBooksInInventory =(inventory.bookList.get(((Book) objectReceived).title).numCopies);

                    // NOTE: summing them because now objectRecieved.numCopies is negative so (positive + (-number))
                    if (  (currentBooksInInventory - Math.abs(((Book) objectReceived).numCopies)) >= 0  ) { // ((Book) objectReceived).numCopies) >= 0

                        inventory.updateBook((Book) objectReceived);
                        sendToAllClients( inventory.bookList.get(((Book) objectReceived).title) , objectOutputStream);

                    } else {
                        System.out.println("cannot borrow item");
                    }

                }

            }
        }

        // TODO: handle different types of library items later

    }


    private void sendToAllClients(Object object, ObjectOutputStream objectOutputStream) {
        Thread sendObjectToAllClientsAsync = new Thread(() -> {
            for (Socket client : clientList) {
                System.out.println(client.toString());

                try {
                    System.out.println("sending object:" + object);
                    objectOutputStream.reset(); // need to reset or objects are not updated properly
                    objectOutputStream.writeObject(object);
                    objectOutputStream.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        sendObjectToAllClientsAsync.start();
    }


}
