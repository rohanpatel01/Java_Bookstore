package Items;

import java.io.Serializable;

public class Book extends LibraryItem implements Serializable {

    private int numPages;
    private String author;

     public Book(String title, String summaryDescription, String author, int numPages) {

//        this.itemID = ???;
         this.title = title;
         this.summaryDescription = summaryDescription;
         this.itemType = "game";
         this.isAvailable = true;

        this.author = author;
        this.numPages = numPages;
    }
}
