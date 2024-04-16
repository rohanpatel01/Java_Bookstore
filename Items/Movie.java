package Items;

import java.io.Serializable;

public class Movie extends LibraryItem implements Serializable {

    private String movieRunTime;
    private String director;

    public Movie (String title, String summaryDescription, String movieRunTime, String director) {

//        this.itemID = ???;
        this.title = title;
        this.summaryDescription = summaryDescription;
        this.itemType = "game";
        this.isAvailable = true;

        this.movieRunTime = movieRunTime;
        this.director = director;
    }


}
