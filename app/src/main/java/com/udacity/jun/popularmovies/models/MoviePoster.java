/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: Jun 26, 2017
 * Reference:
 */

package com.udacity.jun.popularmovies.models;

public class MoviePoster {
    // define the model of each item in the adapter
    private String poster_url = "";
    private String poster_title = "";

    // methods to retrieve the movie detail info
    public String getPosterUrl() {
        return this.poster_url;
    }
    public String getPosterTitle() {
        return this.poster_title;
    }

    public void setPosterUrl(String url) {
        this.poster_url = url;
    }
    public void setPosterTitle(String title) {
        this.poster_title = title;
    }
}
