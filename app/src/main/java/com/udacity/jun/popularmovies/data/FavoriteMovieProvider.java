/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: Jun 26, 2017
 * Reference:
 *      https://github.com/udacity/android-content-provider
 *      https://github.com/udacity/ud851-Sunshine
 *      https://github.com/udacity/ud851-Exercises
 */

package com.udacity.jun.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class FavoriteMovieProvider extends ContentProvider{
    // --Commented out by Inspection (2017-07-01, 8:56 PM):
    // private static final String LOG_TAG = FavoriteMovieProvider.class.getSimpleName();
    //declare DB helper as global variable
    private FavoriteMovieDBHelper mFavoriteMovieDbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    // Codes for the UriMatcher
    private static final int FAVORITE_WITH_DIR = 100;
    private static final int FAVORITE_WITH_ID = 200;
    private static UriMatcher buildUriMatcher(){
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher movieUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteMovieContract.CONTENT_AUTHORITY;
        final String path = FavoriteMovieContract.PATH_TASKS;

        // add a code for each type of URI you want
        // for directory type
        movieUriMatcher.addURI(authority, path, FAVORITE_WITH_DIR);
        // for single item type
        movieUriMatcher.addURI(authority, path + "/#", FAVORITE_WITH_ID);

        return movieUriMatcher;
    }

    //initialize provider to setup underlying SQLite database
    @Override
    public boolean onCreate(){
        //create DB helper to access the database
        Context context = getContext();
        mFavoriteMovieDbHelper = new FavoriteMovieDBHelper(context);

        return true;
    }

    //CRUD methods
    // insert: contentProvider CRUD method #1
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values){
        // create a database
        final SQLiteDatabase mFavoriteDB = mFavoriteMovieDbHelper.getWritableDatabase();
        Uri returnUri;
        int match = sUriMatcher.match(uri);
        switch (match) {
            //insert a new row only on a directory, cannot be a single item, so no FAVORITE_WITH_ID needed
            case FAVORITE_WITH_DIR: {
                long _id = mFavoriteDB.insert(FavoriteMovieContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIE,
                        null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    //success
                    returnUri = FavoriteMovieContract.FavoriteMovieEntry.buildFavouriteMovieUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

//    @Override
//    public int bulkInsert(Uri uri, ContentValues[] values){
//        return 0;
//    }

    // query: contentProvider CRUD method #2
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder){
        final SQLiteDatabase mFavoriteDB = mFavoriteMovieDbHelper.getReadableDatabase();
        Cursor returnCursor;
        int match = sUriMatcher.match(uri);
        switch(match){
            // query for a directory
            case FAVORITE_WITH_DIR:{
                returnCursor = mFavoriteDB.query(FavoriteMovieContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIE,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            // Individual flavor based on Id selected
            case FAVORITE_WITH_ID:{
                // using selection and selectionArgs
                String rowId = uri.getPathSegments().get(1);
                String mSelection = FavoriteMovieContract.FavoriteMovieEntry._ID+ "=?";
                String[] mSelectionArgs = new String[]{rowId};
                returnCursor = mFavoriteDB.query(FavoriteMovieContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIE,
                        projection, mSelection, mSelectionArgs, null, null, sortOrder);
                break;
            }
            default:{
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        if (getContext() != null) {
            returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return returnCursor;
    }


    // update: contentProvider CRUD method #3
    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
        final SQLiteDatabase mFavoriteDB = mFavoriteMovieDbHelper.getWritableDatabase();
        int numUpdated = 0;
        if (contentValues == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }
        int match = sUriMatcher.match(uri);
        switch(match){
            case FAVORITE_WITH_DIR:{
                numUpdated = mFavoriteDB.update(FavoriteMovieContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIE,
                        contentValues, selection, selectionArgs);
                break;
            }
            case FAVORITE_WITH_ID: {
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        if (numUpdated > 0){
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        return numUpdated;
    }

    // delete: contentProvider CRUD method #4
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs){
        final SQLiteDatabase mFavoriteDB = mFavoriteMovieDbHelper.getWritableDatabase();
        int numDeleted = 0;
        int match = sUriMatcher.match(uri);
        switch(match){
            case FAVORITE_WITH_DIR:
                numDeleted = mFavoriteDB.delete(FavoriteMovieContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIE,
                        selection, selectionArgs);
                // reset _ID
                mFavoriteDB.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        FavoriteMovieContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIE + "'");
                break;
            case FAVORITE_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                String mSelection = FavoriteMovieContract.FavoriteMovieEntry._ID + "=?";
                String[] mSelectionArgs = new String[]{id};
                numDeleted = mFavoriteDB.delete(FavoriteMovieContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIE,
                        mSelection, mSelectionArgs);
                // reset _ID
                mFavoriteDB.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        FavoriteMovieContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIE + "'");
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //Notify the resolver of a change and return the number of items deleted
        if (numDeleted != 0) {
            // A row was deleted, set notification
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        return numDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri){
        final int match = sUriMatcher.match(uri);
        switch (match){
            case FAVORITE_WITH_DIR:{
                return FavoriteMovieContract.FavoriteMovieEntry.CONTENT_DIR_TYPE;
            }
            case FAVORITE_WITH_ID:{
                return FavoriteMovieContract.FavoriteMovieEntry.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }







}
