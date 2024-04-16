package Shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book extends LibraryItem implements Serializable {

    private int numPages;
    private String author;

//    public int itemID = 0;
//    public String title;
//    public String summaryDescription;
//    public boolean isAvailable;
//    public String itemType;
//
//    public int numberCopiesRemaining;
//    public String[] pastMembers;
//    public String memberCurrentlyCheckingOut;
//    public String lastTimeCheckedOut; // see what type this has to be and how we can get time
     public Book(String title, String summaryDescription, String author, int numPages) {

         // other stuff from LibraryItem

//         public int itemID = 0;
//         public String title;
//         public String summaryDescription;
//         public boolean isAvailable;
//         public String itemType;
//
//         public int numberCopiesRemaining;
//         public List<String> pastMembers;
//         public String memberCurrentlyCheckingOut;
//         public String lastTimeCheckedOut; // see what type this has to be and how we can get time



         itemID = 0; // need to make unique after
         numberCopiesRemaining = 1;
         pastMembers = new ArrayList<>();
         memberCurrentlyCheckingOut = "";
         lastTimeCheckedOut = "";

         this.title = title;
         this.summaryDescription = summaryDescription;
         this.itemType = "book";
         this.isAvailable = true;

        this.author = author;
        this.numPages = numPages;
    }
    @Override
    public String toString() {
        return itemType + title + summaryDescription + isAvailable;
    }
}
