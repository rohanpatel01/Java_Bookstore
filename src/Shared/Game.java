package Shared;

import java.io.Serializable;

public class Game extends LibraryItem implements Serializable {

    public String developerStudio;

    public Game() {}

    public Game (String title, String summaryDescription, String developerStudio, int numCopies) {
        super(2, title, summaryDescription, numCopies);
        this.developerStudio = developerStudio;
    }

    @Override
    public String toString() {
        return (  itemType + " : " + title + " : " + summaryDescription);
    }

}
