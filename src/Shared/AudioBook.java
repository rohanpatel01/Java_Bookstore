package Shared;

public class AudioBook extends LibraryItem {

    private String narrator;

    public AudioBook(String title, String summaryDescription, String narrator ){
        // want to just get that value and increment it here
//        this.itemID = ???;
        this.title = title;
        this.summaryDescription = summaryDescription;
        this.itemType = "audiobook";
        this.isAvailable = true;

        this.narrator = narrator;
    }
}
