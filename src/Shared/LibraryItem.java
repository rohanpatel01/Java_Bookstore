package Shared;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public abstract class LibraryItem {
    // item type, title, author, pages, summary description

    // constructor info
    public int itemID = 0;
    public String title;
    public String summaryDescription;
    public boolean isAvailable;
    public String itemType;

    public int numberCopiesRemaining;
    public List<String> pastMembers;
    public String memberCurrentlyCheckingOut;
    public String lastTimeCheckedOut; // see what type this has to be and how we can get time

//    public LibraryItem() {
//        itemID = 0; // need to make unique after
//        numberCopiesRemaining = 1;
//        pastMembers = new ArrayList<>();
//        memberCurrentlyCheckingOut = "";
//        lastTimeCheckedOut = "";
//    }
    public void checkOut() { this.isAvailable = false; }

    public void checkIn() { this.isAvailable = true; }


}
