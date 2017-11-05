/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: Jun 28, 2017
 * Reference:
 *      https://github.com/udacity/ud851-Sunshine
 */

package com.udacity.jun.popularmovies.async;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.udacity.jun.popularmovies.data.FavoriteMovieContract;

public class ContentProviderAsyncTaskLoader extends AsyncTaskLoader<Cursor> {
    // Initialize a Cursor, this will hold all the task data
    private Cursor mTaskData = null;
    private final Context context;

    // constructor for the AsyncTask class
    public ContentProviderAsyncTaskLoader(Context contextInfo)
    {
        super(contextInfo);
        this.context = contextInfo;
    }

    // onStartLoading() is called when a loader first starts loading data
    @Override
    protected void onStartLoading() {
        if (mTaskData != null) {
            // Delivers any previously loaded data immediately
            deliverResult(mTaskData);
        } else {
            // Force a new load
            forceLoad();
        }
    }

    // loadInBackground() performs asynchronous loading of data
    @Override
    public Cursor loadInBackground() {
        // Will implement to load data so can query all favorite movies from the movie database
        try {
            return context.getContentResolver().query(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                    null, null, null, FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RATE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    // deliverResult sends the result of the load, a Cursor, to the registered listener
    public void deliverResult(Cursor data) {
        mTaskData = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
}
