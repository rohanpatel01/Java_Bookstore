package Shared;

import java.io.Serializable;

public class Movie extends LibraryItem implements Serializable {

    public String movieRunTime;
    public String director;

    public Movie() {}

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
