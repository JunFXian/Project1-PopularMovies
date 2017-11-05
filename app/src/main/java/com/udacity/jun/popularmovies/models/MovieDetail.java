/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: May 22, 2017
 * Reference:
 *      https://github.com/udacity/android-custom-arrayadapter/tree/gridview
 *      https://github.com/udacity/ud851-Sunshine
 *      https://github.com/udacity/ud851-Exercises
 */

package com.udacity.jun.popularmovies.models;

/**
 * This class is to encapsulate all required movie detail into one object
 */
public class MovieDetail {
    // define the model of each item in the adapter
    private String id = "";
    private String poster_path = "";
    private String original_title = "";
    private String overview = "";
    private String vote_average = "";
    private String release_date = "";

    // methods to retrieve the movie detail info
    public String getMovieID() {
        return this.id;
    }
    public String getPosterPath() {
        return this.poster_path;
    }
    public String getOriginalTitle() {
        return this.original_title;
    }
    public String getOverview() {
        return this.overview;
    }
    public String getVoteAverage() {
        return this.vote_average;
    }
    public String getDate() {
        return this.release_date;
    }

    // default constructor
//    private MovieDetail(){
//    }

    /**
     * This method is to convert the MovieDetail object onto a string array
     * @return string array contains the MovieDetail
     */
    public String[] convertToStringArray() {
        String[] movie = new String[6];
        movie[0] = getMovieID();
        movie[1] = getPosterPath();
        movie[2] = getOriginalTitle();
        movie[3] = getOverview();
        movie[4] = getVoteAverage();
        movie[5] = getDate();
        return movie;
    }

    // another constructor with string array parameter
    public MovieDetail(String[] movieItemStringArray) {
        id = movieItemStringArray[0];
        poster_path = movieItemStringArray[1];
        original_title = movieItemStringArray[2];
        overview = movieItemStringArray[3];
        vote_average = movieItemStringArray[4];
        release_date = movieItemStringArray[5];
    }
}
