package Items;

public class Book extends LibraryItem {

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
