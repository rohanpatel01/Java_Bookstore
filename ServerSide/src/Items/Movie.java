package Items;

public class Movie extends LibraryItem {

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
