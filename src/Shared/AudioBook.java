package Shared;

public class AudioBook extends LibraryItem {

    private String narrator;

    public AudioBook(String title, String summaryDescription, String narrator, int numCopies){
        super(3, title, summaryDescription, numCopies);
        this.narrator = narrator;
    }

    @Override
    public String toString() {
        return (  itemType + " : " + title + " : " + summaryDescription);
    }


}
