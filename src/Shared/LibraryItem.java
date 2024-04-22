package Shared;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public abstract class LibraryItem implements Serializable {

    public static int LIBRARY_ID = 0;
//    public int itemID;
    public String title;
    public String summaryDescription;
    public boolean isAvailable;
    public String itemType;

    public int numberCopiesRemaining;
    public List<String> pastMembers;
    public String memberCurrentlyCheckingOut;
    public String lastTimeCheckedOut; // see what type this has to be and how we can get time
    public int numCopies;
    public LibraryItem() {
//        itemID = -999;
        title = "";
        summaryDescription = "";
        isAvailable = true;
        itemType = "DEFAULT";
        numberCopiesRemaining = 1;
        pastMembers = new ArrayList<>();
        memberCurrentlyCheckingOut = "";
        lastTimeCheckedOut = "";
        numCopies = 0;
    }

    public LibraryItem(String title, String summaryDescription) {
        this();
        this.title = title;
        this.summaryDescription = summaryDescription;
    }

}
