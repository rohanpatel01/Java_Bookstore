package Server.connection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Shared.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Server {

    Inventory inventory;
    ArrayList<Socket> clientList;

    private static MongoClient mongo;
    private static MongoDatabase database;
    private static MongoCollection<Book> bookCollection;
    private static MongoCollection<Movie> movieCollection;
    private static MongoCollection<Game> gameCollection;
    private static MongoCollection<AudioBook> audiobookCollection;

    // change password
    private static final String URI = "mongodb+srv://rohanppatel01:mongoPassword@422-final-project.6ysknqg.mongodb.net/";
    private static final String DB = "mongoInventory";
    private static final String bookCollectionName = "books"; // the name of the collection defined in mongoDB
    private static final String movieCollectionName = "movies"; // the name of the collection defined in mongoDB
    private static final String gameCollectionName = "games"; // the name of the collection defined in mongoDB
    private static final String audiobookCollectionName = "audiobooks"; // the name of the collection defined in mongoDB

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

        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        mongo = MongoClients.create(URI);
        database = mongo.getDatabase(DB).withCodecRegistry(pojoCodecRegistry);

        bookCollection = database.getCollection(bookCollectionName, Book.class);
        movieCollection = database.getCollection(movieCollectionName, Movie.class);
        gameCollection = database.getCollection(gameCollectionName, Game.class);
        audiobookCollection = database.getCollection(audiobookCollectionName, AudioBook.class);

//        Book starterBook = new Book("Glass_Castle", "good book", "J. Walls", 288, 5); // making _ we will parse this out later
//        bookCollection.insertOne(starterBook);


        System.out.println("mongo server created mongo: " + mongo);

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

                // have thread to send them all the items in inventory so they are updated - make synchronized so no other program can interrupt
                Thread updateClient = new Thread(new ClientInventoryUpdater(clientSocket, objectOutputStream, objectInputStream));
                updateClient.start();

            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    class ClientInventoryUpdater implements Runnable {

        private Socket clientSocket;
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;
        private Object objectRecieved;



        ClientInventoryUpdater(Socket clientSocket, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) {
            this.clientSocket = clientSocket;
            this.objectInputStream = objectInputStream;
            this.objectOutputStream = objectOutputStream;
        }

        @Override
       public void run() {

            // TODO: look in the mongoDB and all elements here so can send to all clients

//            Book starterBook = new Book("Glass_Castle", "good book", "J. Walls", 288, 5); // making _ we will parse this out later
//            Movie starterMovie = new Movie("Your Name", "great movie","1:00", "some japanese dude", 5); // making _ we will parse this out later
////            Book otherBook = new Book("Atomic Habits Book", "be better", "Author Atomic Habits", 19, 5); // making _ we will parse this out later
//            inventory.bookList.put(starterBook.title, starterBook);
//            inventory.movieList.put(starterMovie.title, starterMovie);
//            inventory.bookList.put(otherBook .title, otherBook );

            for (int i = 0; i < inventory.inventoryLists.size(); i++) {
                for (String s : inventory.inventoryLists.get(i).keySet()){
                    try {
                        objectOutputStream.writeObject(inventory.inventoryLists.get(i).get(s));
                        objectOutputStream.flush();
                    } catch (IOException e) { throw new RuntimeException(e); }
                }
            }
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
                        if ((objectRecieved = (LibraryItem) objectInputStream.readObject()) != null) {
                            System.out.println("server recieved object: " + objectRecieved);
                            handleObject( (LibraryItem) objectRecieved, objectOutputStream);
                            inventory.printInventory();

                        }
                    }

                } catch (IOException ioe) { ioe.printStackTrace(); }
                catch (ClassNotFoundException classNotFoundException) { classNotFoundException.printStackTrace(); }
            });

            objectReaderThread.start();
        }
    }

    private void handleObject(LibraryItem objectReceived, ObjectOutputStream objectOutputStream) {
      // changing this to be more general to support different types of items
        int itemType = inventory.determineItemType( (LibraryItem) objectReceived);

        if ((inventory.inventoryLists.get(itemType).get((objectReceived).title) == null)) {
            inventory.updateItem( objectReceived);
        } else {
            if ((objectReceived).numCopies > 0) {

                inventory.updateItem(objectReceived);
                sendToAllClients( inventory.inventoryLists.get(itemType).get(( objectReceived).title) , objectOutputStream);
            } else {
                int currentBooksInInventory =(inventory.inventoryLists.get(itemType).get(( objectReceived).title).numCopies);
                if (  (currentBooksInInventory - Math.abs((objectReceived).numCopies)) >= 0  ) { // ((Book) objectReceived).numCopies) >= 0
                    // TODO: May need to synchronize this so multiple clients cannot get same item
                    inventory.updateItem( objectReceived);
                    sendToAllClients( inventory.inventoryLists.get(itemType).get(( objectReceived).title) , objectOutputStream);
                    System.out.println("sending to all clients: " + objectReceived);
                } else {
                    System.out.println("cannot borrow item");
                }
            }
        }
    }


    private void sendToAllClients(Object object, ObjectOutputStream objectOutputStream) {
        System.out.println("sending this item to all client: " + object);
        Thread sendObjectToAllClientsAsync = new Thread(() -> {
            for (Socket client : clientList) {
                System.out.println(client.toString());

                try {
                    System.out.println("sending object:" + object);
                    objectOutputStream.reset(); // need to reset or objects are not updated properly
                    objectOutputStream.writeObject(object);
                    objectOutputStream.flush();
                    objectOutputStream.reset(); // resetting since client is not recieving or something
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        sendObjectToAllClientsAsync.start();
    }


}
