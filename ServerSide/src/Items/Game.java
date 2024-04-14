package Items;

public class Game extends LibraryItem {

    private String developerStudio;

    public Game (String title, String summaryDescription, String developerStudio) {

//        this.itemID = ???;
        this.title = title;
        this.summaryDescription = summaryDescription;
        this.itemType = "game";
        this.isAvailable = true;

        this.developerStudio = developerStudio;
    }
}
