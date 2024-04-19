package Shared;

public class AudioBook extends LibraryItem {

    private String narrator;

    public AudioBook(String title, String summaryDescription, String narrator ){
        super(title, summaryDescription);
        this.itemID = LibraryItem.LIBRARY_ID;
        LibraryItem.LIBRARY_ID += 1;

        this.itemType = "AUDIOBOOK";
        this.narrator = narrator;
    }
}
