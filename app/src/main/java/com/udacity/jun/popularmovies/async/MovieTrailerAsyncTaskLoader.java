/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: Jun 21, 2017
 * Reference:
 *      https://stackoverflow.com/questions/9787729/loadercallbacks-as-a-static-inner-class-to-handle-multiple-loaders-with-differe
 *      https://stackoverflow.com/questions/10045543/global-loader-loadermanager-for-reuse-in-multiple-activities-fragments
 */

package com.udacity.jun.popularmovies.async;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.udacity.jun.popularmovies.BuildConfig;
import com.udacity.jun.popularmovies.models.MovieTrailer;
import com.udacity.jun.popularmovies.utilities.NetworkUtils;

import java.util.List;

/**
 * This class is to create a AsyncTask thread for fetching movies trailers
 */
public class MovieTrailerAsyncTaskLoader extends AsyncTaskLoader<List<MovieTrailer>> {
    private List<MovieTrailer> mMovieTrailerList = null;
    private final String movieId;

    // constructor for the AsyncTask class
    public MovieTrailerAsyncTaskLoader(Context contextInfo, String movie)
    {
        super(contextInfo);
        this.movieId = movie;
    }

    @Override
    protected void onStartLoading() {
        if (mMovieTrailerList != null) {
            // Delivers any previously loaded data immediately
            deliverResult(mMovieTrailerList);
        } else {
            // Force a new load
            forceLoad();
        }
    }

    @Override
    public List<MovieTrailer> loadInBackground() {
        String token = BuildConfig.api_key;
        List<MovieTrailer> result;
        try {
            result = NetworkUtils.getTrailerResponseUsingRetrofit(movieId, token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    @Override
    public void deliverResult(List<MovieTrailer> data) {
        mMovieTrailerList = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
}
