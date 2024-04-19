package Shared;
import java.io.Serializable;

public class Book extends LibraryItem implements Serializable {

    public int numPages;
    public String author;

     public Book(String title, String summaryDescription, String author, int numPages) {

         super(title, summaryDescription);
         this.itemID = LibraryItem.LIBRARY_ID;
         LibraryItem.LIBRARY_ID += 1;

         this.itemType = "BOOK";
         this.author = author;
         this.numPages = numPages;
    }
    @Override
    public String toString() {
        return (itemID + " - " +  itemType + " : " + title + " : " + summaryDescription + " : is available:" + isAvailable);
    }
}
