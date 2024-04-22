package Shared;

import com.sun.xml.internal.ws.api.message.AddressingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {

//    public enum ItemType {
//        BookIndex,
//        MovieIndex,
//        GameIndex,
//        AudiobookIndex
//    }

//    public static Map<ItemType, Map<Integer, ?>> inventoryList; // want this to be static but cannot access
    public static Map<String, Book > bookList;
    public static Map<String, Movie > movieList;
    public static Map<String, Game > gameList;
    public static Map<String, AudioBook > audiobookList;

    public Inventory() {
        bookList = new HashMap<>();
        movieList = new HashMap<>();
        gameList = new HashMap<>();
        audiobookList = new HashMap<>();
    }

    public void addToInventory(Object objectRecieved) {


        if (objectRecieved instanceof Book) {
            addBook((Book) objectRecieved);

        } else if (objectRecieved instanceof Movie) {
            addMovie((Movie) objectRecieved);

        } else if (objectRecieved instanceof Game) {
            addGame((Game) objectRecieved);

        } else if (objectRecieved instanceof AudioBook) {
            addAudiobook((AudioBook) objectRecieved);
        }

    }


    public void printInventory() {
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

    public void addBook(Book item) {
        if (bookList.get(item.title) != null) { // if book already in inventory
//            bookList.put(item.title, new ArrayList<>());

            // just update numCopies to be sum of current and new item numCopies
            // the client and server will handle if we are able to or not
            // ^^ they will only do so if the book in inventory will only be 0 or above, will never let go negative
            bookList.get(item.title).numCopies += item.numCopies;

        } else { // new book
            bookList.put(item.title, item);
//            bookList.get(item.title).add(item);
        }

    }

    public void addMovie(Movie item) {
//        if (movieList.get(item.title) == null) {
//            movieList.put(item.title, new ArrayList<>());
//        }
//
//        movieList.get(item.title).add(item);
    }

    public void addGame(Game item) {
//        if (gameList.get(item.title) == null) {
//            gameList.put(item.title, new ArrayList<>());
//        }
//
//        gameList.get(item.title).add(item);
    }

    public void addAudiobook(AudioBook item) {
//        if (audiobookList.get(item.title) == null) {
//            audiobookList.put(item.title, new ArrayList<>());
//        }
//
//        audiobookList.get(item.title).add(item);
    }

    // have more methods to checkout book, game, audiobook, movie?
    // when user checks out book remove it from this list or maybe even have a map from itemID to the object? but then can't
}
