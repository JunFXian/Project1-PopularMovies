/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: Jun 21, 2017
 * Reference:
 *      https://stackoverflow.com/questions/9787729/loadercallbacks-as-a-static-inner-class-to-handle-multiple-loaders-with-differe
 *      https://stackoverflow.com/questions/10045543/global-loader-loadermanager-for-reuse-in-multiple-activities-fragments
 */

package com.udacity.jun.popularmovies.async;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.udacity.jun.popularmovies.BuildConfig;
import com.udacity.jun.popularmovies.models.MovieReview;
import com.udacity.jun.popularmovies.utilities.NetworkUtils;

import java.util.List;

/**
 * This class is to create a AsyncTask thread for fetching movies reviews
 */
public class MovieReviewAsyncTaskLoader extends AsyncTaskLoader<List<MovieReview>> {
    private List<MovieReview> mMovieReviewList = null;
    private final String movieId;

    // constructor for the AsyncTask class
    public MovieReviewAsyncTaskLoader(Context contextInfo, String movie)
    {
        super(contextInfo);
        this.movieId = movie;
    }

    @Override
    protected void onStartLoading() {
        if (mMovieReviewList != null) {
            // Delivers any previously loaded data immediately
            deliverResult(mMovieReviewList);
        } else {
            // Force a new load
            forceLoad();
        }
    }

    @Override
    public List<MovieReview> loadInBackground() {
        String token = BuildConfig.api_key;
        List<MovieReview> result;
        try {
            result = NetworkUtils.getReviewResponseUsingRetrofit(movieId, token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    @Override
    public void deliverResult(List<MovieReview> data) {
        mMovieReviewList = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
}
