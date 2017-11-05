/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: Jun 26, 2017
 * Reference:
 *      https://github.com/udacity/android-content-provider
 */

package com.udacity.jun.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMovieContract {
    //the authority which is how the code knows which content provider to access
    public static final String CONTENT_AUTHORITY = "com.udacity.jun.popularmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //Define the possible paths for accessing data in the contract
    //path #1 for "FavoriteMovie" table directory
    public static final String PATH_TASKS = "FavoriteMovie";

    public static final class FavoriteMovieEntry implements BaseColumns {
        // table name
        public static final String TABLE_FAVORITE_MOVIE = "FavoriteMovie";
        // columns
        public static final String _ID = "_id";
        public static final String COLUMN_MOVIE_ID= "movie_id";
        public static final String COLUMN_TITLE= "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATE = "rate";
        public static final String COLUMN_DATE = "release_date";
        public static final String COLUMN_POSTERURI = "poster_uri";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITE_MOVIE;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITE_MOVIE;

        // for building URIs on insertion
        public static Uri buildFavouriteMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
