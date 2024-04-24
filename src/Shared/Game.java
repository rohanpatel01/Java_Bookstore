package Shared;

public class Game extends LibraryItem {

    public String developerStudio;
    public Game (String title, String summaryDescription, String developerStudio, int numCopies) {
        super(2, title, summaryDescription, numCopies);
        this.developerStudio = developerStudio;
    }

    @Override
    public String toString() {
        return (  itemType + " : " + title + " : " + summaryDescription);
    }

}
