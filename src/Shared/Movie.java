package Shared;

public class Movie extends LibraryItem {

    private String movieRunTime;
    private String director;

    public Movie (String title, String summaryDescription, String movieRunTime, String director, int numCopies) {

        super(1, title, summaryDescription, numCopies);

        this.movieRunTime = movieRunTime;
        this.director = director;
    }


    @Override
    public String toString() {
        return (  itemType + " : " + title + " : " + summaryDescription);
    }


}
