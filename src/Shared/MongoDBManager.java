package Shared;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDBManager {


    public static MongoClient mongo;
    public static MongoDatabase database;
    public static MongoCollection<Book> bookCollection;
    public static MongoCollection<Movie> movieCollection;
    public static MongoCollection<Game> gameCollection;
    public static MongoCollection<AudioBook> audiobookCollection;
    public static ArrayList<MongoCollection> allCollections;



    // change password
    public static final String URI = "mongodb+srv://rohanppatel01:mongoPassword@422-final-project.6ysknqg.mongodb.net/";
    public static final String DB = "mongoInventory";
    public static final String bookCollectionName = "books"; // the name of the collection defined in mongoDB
    public static final String movieCollectionName = "movies"; // the name of the collection defined in mongoDB
    public static final String gameCollectionName = "games"; // the name of the collection defined in mongoDB
    public static final String audiobookCollectionName = "audiobooks"; // the name of the collection defined in mongoDB

    static {
        allCollections = new ArrayList<>();

        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        mongo = MongoClients.create(URI);
        database = mongo.getDatabase(DB).withCodecRegistry(pojoCodecRegistry);

        bookCollection = database.getCollection(bookCollectionName, Book.class);
        movieCollection = database.getCollection(movieCollectionName, Movie.class);
        gameCollection = database.getCollection(gameCollectionName, Game.class);
        audiobookCollection = database.getCollection(audiobookCollectionName, AudioBook.class);

        allCollections.add(bookCollection);
        allCollections.add(movieCollection);
        allCollections.add(gameCollection);
        allCollections.add(audiobookCollection);
    }



}
