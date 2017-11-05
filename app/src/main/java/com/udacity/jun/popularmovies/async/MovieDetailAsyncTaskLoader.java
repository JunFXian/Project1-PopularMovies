/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: Jun 4, 2017
 * Reference:
 *      https://github.com/udacity/ud851-Sunshine
 *      http://www.jameselsey.co.uk/blogs/techblog/extracting-out-your-asynctasks-into-separate-classes-makes-your-code-cleaner/
 *      https://stackoverflow.com/questions/9787729/loadercallbacks-as-a-static-inner-class-to-handle-multiple-loaders-with-differe
 *      https://stackoverflow.com/questions/10045543/global-loader-loadermanager-for-reuse-in-multiple-activities-fragments
 */

package com.udacity.jun.popularmovies.async;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import com.udacity.jun.popularmovies.BuildConfig;
import com.udacity.jun.popularmovies.R;
import com.udacity.jun.popularmovies.models.MovieDetail;
import com.udacity.jun.popularmovies.utilities.NetworkUtils;

/**
 * The following code is from James Elsey posted on July 23, 2012
 * This class is to create a AsyncTask thread for fetching movies data
 */
public class MovieDetailAsyncTaskLoader extends AsyncTaskLoader<List<MovieDetail>> {
    // Initialize a movie detail list, this will hold all the task data
    private List<MovieDetail> mMovieList = null;
    private final String sortMethod;

    // constructor for the AsyncTask class
    public MovieDetailAsyncTaskLoader(Context contextInfo, String sorting)
    {
        super(contextInfo);
        this.sortMethod = sorting;
    }

    // onStartLoading() is called when a loader first starts loading data
    @Override
    protected void onStartLoading() {
        if (mMovieList != null) {
            // Delivers any previously loaded data immediately
            deliverResult(mMovieList);
        } else {
            // Force a new load
            forceLoad();
        }
    }

    // loadInBackground() performs asynchronous loading of data, here it requests movies data
    @Override
    public List<MovieDetail> loadInBackground() {
        int num = getContext().getResources().getInteger(R.integer.number_of_page);
        String token = BuildConfig.api_key;
        List<MovieDetail> subList;
        List<MovieDetail> mergeList = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            // use a try/catch block to catch any errors in loading data
            try {
                subList = NetworkUtils.getSortingResponseUsingRetrofit(sortMethod, token, i + 1);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            if (subList != null) {
                for (MovieDetail singleMovie : subList) {
                    mergeList.add(singleMovie);
                }
            }
        }
        return mergeList;
    }

    // deliverResult sends the result of the load to the registered listener
    @Override
    public void deliverResult(List<MovieDetail> data) {
        mMovieList = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
}
