package Shared;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public abstract class LibraryItem implements Serializable {
    // item type, title, author, pages, summary description

    // constructor info
    public int itemID = -9999;
    public String title;
    public String summaryDescription;
    public boolean isAvailable;
    public String itemType;

    public int numberCopiesRemaining;
    public List<String> pastMembers;
    public String memberCurrentlyCheckingOut;
    public String lastTimeCheckedOut; // see what type this has to be and how we can get time

}
