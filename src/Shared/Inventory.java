package Shared;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.sun.xml.internal.ws.api.message.AddressingUtils;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Inventory {


    public Map<String, LibraryItem> bookList;
    public Map<String, LibraryItem> movieList;
    public Map<String, LibraryItem > gameList;
    public Map<String, LibraryItem > audiobookList;

    public List<Map<String, LibraryItem>> inventoryLists;



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


    public Inventory() {
        bookList = new HashMap<>();
        movieList = new HashMap<>();
        gameList = new HashMap<>();
        audiobookList = new HashMap<>();

        inventoryLists = new ArrayList<>();
        inventoryLists.add(bookList);
        inventoryLists.add(movieList);
        inventoryLists.add(gameList);
        inventoryLists.add(audiobookList);

        // create mongoDB stuff

        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        mongo = MongoClients.create(URI);
        database = mongo.getDatabase(DB).withCodecRegistry(pojoCodecRegistry);

        bookCollection = database.getCollection(bookCollectionName, Book.class);
        movieCollection = database.getCollection(movieCollectionName, Movie.class);
        gameCollection = database.getCollection(gameCollectionName, Game.class);
        audiobookCollection = database.getCollection(audiobookCollectionName, AudioBook.class);



    }



    public void printInventory() {
        for (String keys : bookList.keySet() )
        {
            System.out.println(keys + ":"+ bookList.get(keys));
        }
        // TODO: uncomment below when ready to test / look at rest of objects
//        System.out.println("============================================");
//
//        for (String keys : movieList.keySet() )
//        {
//            System.out.println(keys + ":"+ movieList.get(keys));
//        }
//        System.out.println("============================================");
//
//
//        for (String keys : gameList.keySet() )
//        {
//            System.out.println(keys + ":"+ gameList.get(keys));
//        }
//        System.out.println("============================================");
//
//        for (String keys : audiobookList.keySet() )
//        {
//            System.out.println(keys + ":"+ audiobookList.get(keys));
//        }
//        System.out.println("============================================");

    }

    public void updateItem(LibraryItem item) {

        // add item to mongodb and respective collection if not added
        // otherwise update it

        if (inventoryLists.get(item.itemType).get(item.title) != null) {
            System.out.println("increasing item");
            inventoryLists.get(item.itemType).get(item.title).numCopies += item.numCopies;

            if (item instanceof Book) {
                bookCollection.findOneAndReplace(Filters.eq("title", item.title), (Book) inventoryLists.get(item.itemType).get(item.title));
            } else if (item instanceof Movie) {
                movieCollection.findOneAndReplace(Filters.eq("title", item.title), (Movie) inventoryLists.get(item.itemType).get(item.title));
            } else if (item instanceof Game) {
                gameCollection.findOneAndReplace(Filters.eq("title", item.title), (Game) inventoryLists.get(item.itemType).get(item.title));
            } else if (item instanceof AudioBook) {
                audiobookCollection.findOneAndReplace(Filters.eq("title", item.title), (AudioBook) inventoryLists.get(item.itemType).get(item.title));
            }

        } else {
            System.out.println("creating item");
            inventoryLists.get(item.itemType).put(item.title, item);

            if (item instanceof Book) {
               bookCollection.insertOne((Book) item);
            } else if (item instanceof Movie) {
                movieCollection.insertOne((Movie) item);
            } else if (item instanceof Game){
                gameCollection.insertOne((Game) item);
            } else if (item instanceof AudioBook) {
                audiobookCollection.insertOne((AudioBook) item);
            }

        }
    }



    public int determineItemType(LibraryItem item) {

       if (item instanceof Book) {
           return 0;
       } else if (item instanceof Movie) {
          return 1;
       } else if (item instanceof Game) {
           return 2;
       } else if (item instanceof AudioBook){
           return 3;
       }

       return -999; // invalid object but should never happen
    }
}
