/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: Jun 19, 2017
 * Reference:
 */

package com.udacity.jun.popularmovies.models;

/**
 * This class is to encapsulate all required movie trailer info into one object
 */
public class MovieTrailer {
    // define the model of each item in the adapter
    private String id = "";
    private String key = "";
    private String name = "";
    private String site = "";

    // methods to retrieve the movie trailer info
    private String getID() {
        return this.id;
    }
    public String getKey() {
        return this.key;
    }
    public String getName() {
        return this.name;
    }
    public String getSite() {
        return this.site;
    }

    private void setId(String mId) {
        id = mId;
    }

    public void setKey(String mKey) {
        key = mKey;
    }

    public void setName(String mName) {
        name = mName;
    }

    public void setSite(String mSite) {
        site = mSite;
    }

    /**
     * This method is to convert the MovieTrailer object onto a string array
     * @return string array contains the MovieTrailer
     */
    public String[] convertToStringArray() {
        String[] movieTrailer = new String[4];
        movieTrailer[0] = getID();
        movieTrailer[1] = getKey();
        movieTrailer[2] = getName();
        movieTrailer[3] = getSite();
        return movieTrailer;
    }
}
