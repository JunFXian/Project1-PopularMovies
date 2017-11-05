/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: Jun 26, 2017
 * Reference:
 *      https://github.com/udacity/android-content-provider
 */

package com.udacity.jun.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class FavoriteMovieDBHelper extends SQLiteOpenHelper{
    private static final String LOG_TAG = FavoriteMovieDBHelper.class.getSimpleName();

    //database name & version
    private static final String DATABASE_NAME = "favoriteMovie.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteMovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " + FavoriteMovieContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIE +
                "(" + FavoriteMovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RATE + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POSTERURI + " TEXT NOT NULL" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }

    // Upgrade database when version is changed.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ". OLD DATA WILL BE DESTROYED");

        String NEW_COLUMN_NAME = "movie_no";
        String TYPE = " INTEGER DEFAULT 0";

        /**
         * SUGGESTION:
         * Dropping table is not a good idea when you are creating corporate application it may create some performance issue and data loose if you drop table,
         * instead you should alter table it will save your data and update table.
         * Read More Handling onUpgrade(): https://thebhwgroup.com/blog/how-android-sqlite-onupgrade
         *                                 http://www.vogella.com/tutorials/AndroidSQLite/article.html
         */
//        // Drop the table
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIE);
//        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + FavoriteMovieContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIE + "'");
        if (newVersion > oldVersion) {
            sqLiteDatabase.execSQL("ALTER TABLE " + FavoriteMovieContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIE + " ADD COLUMN " + NEW_COLUMN_NAME + TYPE);
        }

        // re-create database
        onCreate(sqLiteDatabase);
    }
}
