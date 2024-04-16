package Items;

import java.io.Serializable;

public class Game extends LibraryItem implements Serializable {

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
