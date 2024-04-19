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
    public static Map<String, List<Book> > bookList;
    public static Map<String, List<Movie> > movieList;
    public static Map<String, List<Game> > gameList;
    public static Map<String, List<AudioBook> > audiobookList;

    public Inventory() {
        bookList = new HashMap<>();
        movieList = new HashMap<>();
        gameList = new HashMap<>();
        audiobookList = new HashMap<>();
    }

    public void addBook(Book item) {
        if (bookList.get(item.title) == null) {
            bookList.put(item.title, new ArrayList<>());
        }
        bookList.get(item.title).add(item);

    }

    public void addMovie(Movie item) {
        if (movieList.get(item.title) == null) {
            movieList.put(item.title, new ArrayList<>());
        }

        movieList.get(item.title).add(item);
    }

    public void addGame(Game item) {
        if (gameList.get(item.title) == null) {
            gameList.put(item.title, new ArrayList<>());
        }

        gameList.get(item.title).add(item);
    }

    public void addAudiobook(AudioBook item) {
        if (audiobookList.get(item.title) == null) {
            audiobookList.put(item.title, new ArrayList<>());
        }

        audiobookList.get(item.title).add(item);
    }

    // have more methods to checkout book, game, audiobook, movie?
    // when user checks out book remove it from this list or maybe even have a map from itemID to the object? but then can't
}
