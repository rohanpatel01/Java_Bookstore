package Shared;

public class Movie extends LibraryItem {

    private String movieRunTime;
    private String director;

    public Movie (String title, String summaryDescription, String movieRunTime, String director) {

        super(title, summaryDescription);
//        this.itemID = LibraryItem.LIBRARY_ID;
//        LibraryItem.LIBRARY_ID += 1;

        this.itemType = "MOVIE";
        this.movieRunTime = movieRunTime;
        this.director = director;
    }


    @Override
    public String toString() {
        return (  itemType + " : " + title + " : " + summaryDescription);
    }


}
