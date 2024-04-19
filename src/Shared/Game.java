package Shared;

public class Game extends LibraryItem {

    private String developerStudio;
    public Game (String title, String summaryDescription, String developerStudio) {
        super(title, summaryDescription);
        this.itemID = LibraryItem.LIBRARY_ID;
        LibraryItem.LIBRARY_ID += 1;

        this.itemType = "GAME";
        this.developerStudio = developerStudio;
    }

    @Override
    public String toString() {
        return (itemID + " - " +  itemType + " : " + title + " : " + summaryDescription);
    }

}
