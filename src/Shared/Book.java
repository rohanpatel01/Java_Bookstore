package Shared;
import java.io.Serializable;

public class Book extends LibraryItem implements Serializable {

    public int numPages;
    public String author;

     public Book(String title, String summaryDescription, String author, int numPages) {

         super(title, summaryDescription);

         // only increment the library ID if one with same title does not already exist
//         if (!Inventory.bookList.containsKey(title)) {
//             this.itemID = LibraryItem.LIBRARY_ID;
//             LibraryItem.LIBRARY_ID += 1;
//         }

         // make this only happen for books that are not already in list like ^^
//         this.itemID = LibraryItem.LIBRARY_ID;
//         LibraryItem.LIBRARY_ID += 1;

         this.itemType = "BOOK";
         this.author = author;
         this.numPages = numPages;
    }
    @Override
    public String toString() {
        return (" (" + numCopies + ") " +  itemType + " : " + title + " : " + summaryDescription);
    }
}
