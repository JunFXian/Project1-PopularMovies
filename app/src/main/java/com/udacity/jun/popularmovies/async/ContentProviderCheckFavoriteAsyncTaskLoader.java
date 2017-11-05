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

public class ContentProviderCheckFavoriteAsyncTaskLoader extends AsyncTaskLoader<Cursor> {
    // Initialize a Cursor, this will hold all the task data
    private Cursor mCheckTaskData = null;
    private final Context mContext;
    private final String mMovieId;

    // constructor for the AsyncTask class
    public ContentProviderCheckFavoriteAsyncTaskLoader(Context contextInfo, String movie)
    {
        super(contextInfo);
        this.mContext = contextInfo;
        this.mMovieId = movie;
    }

    @Override
    protected void onStartLoading() {
        if (mCheckTaskData != null) {
            // Delivers any previously loaded data immediately
            deliverResult(mCheckTaskData);
        } else {
            // Force a new load
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {
        try {
//            String[] mProjection = new String[]{FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID};
            String mSelection = FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + "=?";
            String[] mSelectionArgs = new String[]{mMovieId};
            return mContext.getContentResolver().query(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                    null, mSelection, mSelectionArgs, FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RATE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    // deliverResult sends the result of the load, a Cursor, to the registered listener
    public void deliverResult(Cursor data) {
        mCheckTaskData = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
}
