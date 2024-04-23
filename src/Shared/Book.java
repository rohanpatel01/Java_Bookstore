package Shared;
import java.io.Serializable;

public class Book extends LibraryItem implements Serializable {

    public int numPages;
    public String author;

     public Book(String title, String summaryDescription, String author, int numPages, int numCopies) {

         super(title, summaryDescription, numCopies);

         this.itemType = "BOOK";
         this.author = author;
         this.numPages = numPages;
    }
    @Override
    public String toString() {
        return (" (" + numCopies + ") " +  itemType + " : " + title + " : " + summaryDescription);
    }
}
