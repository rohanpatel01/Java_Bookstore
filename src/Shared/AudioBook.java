package Shared;

import java.io.Serializable;

public class AudioBook extends LibraryItem implements Serializable {

    public String narrator;

    public AudioBook() {}

    public AudioBook(String title, String summaryDescription, String narrator, int numCopies){
        super(3, title, summaryDescription, numCopies);
        this.narrator = narrator;
    }

    @Override
    public String toString() {
        return (  itemType + " : " + title + " : " + summaryDescription);
    }


}
