package Items;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class LibraryItem {
    // item type, title, author, pages, summary description

    // constructor info
    public int itemID = 0;
    public String title;
    public String summaryDescription;
    public boolean isAvailable;
    public String itemType;

    public int numberCopiesRemaining;
    public String[] pastMembers;
    public String memberCurrentlyCheckingOut;
    public String lastTimeCheckedOut; // see what type this has to be and how we can get time

    public void checkOut() { this.isAvailable = false; }

    public void checkIn() { this.isAvailable = true; }


}
