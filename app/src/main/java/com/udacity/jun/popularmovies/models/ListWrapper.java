/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: Jun 19, 2017
 * Reference:
 *      http://www.vogella.com/tutorials/Retrofit/article.html
 *      http://www.truiton.com/2015/04/android-retrofit-tutorial/
 */

package com.udacity.jun.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListWrapper<T> {
    @SerializedName("results")
    public List<T> items;
}
