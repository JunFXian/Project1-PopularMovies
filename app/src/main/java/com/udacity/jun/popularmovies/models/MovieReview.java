/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: Jun 19, 2017
 * Reference:
 */

package com.udacity.jun.popularmovies.models;

/**
 * This class is to encapsulate all required movie reviews into one object
 */
public class MovieReview {
    // define the model of each item in the adapter
    private String id;
    private String author;
    private String content;
    private String url;

    // methods to retrieve the movie review info
    private String getID() {
        return this.id;
    }
    public String getAuthor() {
        return this.author;
    }
    public String getContent() {
        return this.content;
    }
    public String getUrl() {
        return this.url;
    }

    private void setID(String mId) {
        id = mId;
    }

    public void setAuthor(String mAuthor) {
        author = mAuthor;
    }

    public void setContent(String mContent) {
        content = mContent;
    }

    public void setUrl(String mUrl) {
        url = mUrl;
    }

    /**
     * This method is to convert the MovieReview object onto a string array
     * @return string array contains the MovieReview
     */
    public String[] convertToStringArray() {
        String[] movieReview = new String[4];
        movieReview[0] = getID();
        movieReview[1] = getAuthor();
        movieReview[2] = getContent();
        movieReview[3] = getUrl();
        return movieReview;
    }
}