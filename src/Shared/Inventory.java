package Shared;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    public static List<Book> books;
    public static List<Book> movies;
    public static List<Book> games;
    public static List<Book> audiobooks;

    public Inventory() {
        books = new ArrayList<>();
        movies = new ArrayList<>();
        games = new ArrayList<>();
        audiobooks = new ArrayList<>();
    }



}
