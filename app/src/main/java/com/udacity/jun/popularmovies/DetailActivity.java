/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: May 25, 2017
 * Reference:
 *      https://github.com/udacity/ud851-Sunshine
 *      https://github.com/udacity/ud851-Exercises
 *      https://github.com/udacity/android-content-provider
 *      https://stackoverflow.com/questions/33885561/does-picasso-library-for-android-handle-image-loading-while-network-connectivity
 *      https://stackoverflow.com/questions/10723770/whats-the-best-way-to-iterate-an-android-cursor
 */

package com.udacity.jun.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
//import android.widget.Toast;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.udacity.jun.popularmovies.adapters.ReviewRecyclerViewAdapter;
import com.udacity.jun.popularmovies.adapters.TrailerRecyclerViewAdapter;
import com.udacity.jun.popularmovies.async.ContentProviderFindIdAsyncTaskLoader;
import com.udacity.jun.popularmovies.async.MovieReviewAsyncTaskLoader;
import com.udacity.jun.popularmovies.async.MovieTrailerAsyncTaskLoader;
import com.udacity.jun.popularmovies.data.FavoriteMovieContract;
import com.udacity.jun.popularmovies.models.MovieDetail;
import com.udacity.jun.popularmovies.models.MovieReview;
import com.udacity.jun.popularmovies.models.MovieTrailer;
import com.udacity.jun.popularmovies.utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements
        TrailerRecyclerViewAdapter.RecyclerViewAdapterOnClickHandler{
    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewRecyclerView;
    private TextView trailerTitle;
    private TextView reviewTitle;
    private CollapsingToolbarLayout collapsingToolbar;
    private Button favouriteButton;
    private String url;

    private static final int TRAILER_ASYNCTASK_LOADER_ID = 10;
    private static final int REVIEW_ASYNCTASK_LOADER_ID = 20;
    private static final int CURSOR_FIND_ID_TO_DELETE_LOADER_ID = 30;

    private final LoaderManager.LoaderCallbacks<List<MovieTrailer>> trailerAsyncTaskLoaderListener
            = new LoaderManager.LoaderCallbacks<List<MovieTrailer>>() {
        @Override
        public Loader<List<MovieTrailer>> onCreateLoader(int id, final Bundle loaderArgs) {
            String movieId = "";
            if (loaderArgs != null) {
                movieId = loaderArgs.getString(getString(R.string.loader_bundle_key_movie_id));
            }
            return new MovieTrailerAsyncTaskLoader(DetailActivity.this, movieId);
        }

        @Override
        public void onLoadFinished(Loader<List<MovieTrailer>> loader, List<MovieTrailer> allMovieTrailer) {
            List<MovieTrailer> youTubeTrailer = new ArrayList<>();
            if (allMovieTrailer != null){
                int i = 0;
                for (MovieTrailer item : allMovieTrailer) {
                    if (item.getSite().equals(getString(R.string.youtube))) {
                        youTubeTrailer.add(i++, item);
                    }
                }
            }
            //check if the passing result has content or not
            if (youTubeTrailer.size() > 0) {
                TrailerRecyclerViewAdapter mTrailerAdapter = new TrailerRecyclerViewAdapter(DetailActivity.this, youTubeTrailer, DetailActivity.this);
                mTrailerRecyclerView.setAdapter(mTrailerAdapter);
            } else {
                mTrailerRecyclerView.setAlpha(0);
                trailerTitle.setText(R.string.trailer_no_content);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<MovieTrailer>> loader) {
        }
    };

    private final LoaderManager.LoaderCallbacks<List<MovieReview>> reviewAsyncTaskLoaderListener
            = new LoaderManager.LoaderCallbacks<List<MovieReview>>() {
        @Override
        public Loader<List<MovieReview>> onCreateLoader(int id, final Bundle loaderArgs) {
            String movieId = "";
            if (loaderArgs != null) {
                movieId = loaderArgs.getString(getString(R.string.loader_bundle_key_movie_id));
            }
            return new MovieReviewAsyncTaskLoader(DetailActivity.this, movieId);
        }

        @Override
        public void onLoadFinished(Loader<List<MovieReview>> loader, List<MovieReview> allMovieReview) {
            //check if the passing result has content or not
            if (allMovieReview != null && allMovieReview.size() > 0) {
                ReviewRecyclerViewAdapter mReviewAdapter = new ReviewRecyclerViewAdapter(DetailActivity.this, allMovieReview);
                mReviewRecyclerView.setAdapter(mReviewAdapter);
            } else {
                mReviewRecyclerView.setAlpha(0);
                reviewTitle.setText(R.string.review_no_content);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<MovieReview>> loader) {
        }
    };

    private final LoaderManager.LoaderCallbacks<Cursor> cursorFindIdToDeleteAsyncTaskLoaderListener
            = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {
            String movieId = "";
            if (loaderArgs != null) {
                movieId = loaderArgs.getString(getString(R.string.loader_bundle_key_movie_id));
            }
            return new ContentProviderFindIdAsyncTaskLoader(DetailActivity.this, movieId);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor resultCursor) {
            Uri uri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI;
            String movieRowId = "";
            try {
                if (resultCursor != null && resultCursor.moveToFirst()) {
                    movieRowId = resultCursor.getString(resultCursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry._ID));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            uri = uri.buildUpon().appendPath(movieRowId).build();
            getContentResolver().delete(uri, null, null);

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    };

    // Create a detail activity layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);

        //this will change the display title text color.
        collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.colorPrimaryDark));
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);

        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.detail_scroll);
        nestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);

        favouriteButton = (Button) findViewById(R.id.favourite_button);

        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.trailer_view);
        mReviewRecyclerView = (RecyclerView) findViewById(R.id.review_view);

        /*
         * A LinearLayoutManager is responsible for measuring and positioning item views within a
         * RecyclerView into a linear list. This means that it can produce either a horizontal or
         * vertical list depending on which parameter you pass in to the LinearLayoutManager
         * constructor. Here I pass in the constant from the LinearLayoutManager class for vertical lists,
         * LinearLayoutManager.VERTICAL.
         * The third parameter (shouldReverseLayout) should be true if you want to reverse your
         * layout. Generally, this is only true with horizontal lists that need to support a
         * right-to-left layout.
         */
        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false);
        //Configure Review RecyclerView
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false);
        //setLayoutManager associates the LayoutManager with the RecyclerView
        mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);
        mReviewRecyclerView.setLayoutManager(reviewLayoutManager);
        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mTrailerRecyclerView.setHasFixedSize(true);
        mReviewRecyclerView.setHasFixedSize(true);
        /*
         * Disable focusable property on the RecyclerView, so when you first enter the activity,
         * focus will default and not rely on RecyclerView.
         */
        mTrailerRecyclerView.setFocusable(false);
        mReviewRecyclerView.setFocusable(false);

//        getSupportLoaderManager().initLoader(TRAILER_ASYNCTASK_LOADER_ID, null, trailerAsyncTaskLoaderListener);
//        getSupportLoaderManager().initLoader(REVIEW_ASYNCTASK_LOADER_ID, null, reviewAsyncTaskLoaderListener);
//        getSupportLoaderManager().initLoader(CURSOR_ASYNCTASK_LOADER_ID, null, cursorAsyncTaskLoaderListener);

        bindDataToDetailActivity();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//    }


    // populate the movie details on the view
    private void bindDataToDetailActivity() {
        final String[] movie;
        final String isFavorite;

        ImageView image = (ImageView) findViewById(R.id.detail_image);
        TextView overview = (TextView) findViewById(R.id.detail_overview);
        TextView rate = (TextView) findViewById(R.id.detail_rate);
        TextView date = (TextView) findViewById(R.id.detail_date);
        trailerTitle = (TextView) findViewById(R.id.trailer_title);
        reviewTitle = (TextView) findViewById(R.id.review_title);

        trailerTitle.setText(R.string.trailer_title);
        reviewTitle.setText(R.string.review_title);

        Intent intentThatStartedThisActivity = getIntent();

        // check if the intent and its data can pass correctly
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                movie = intentThatStartedThisActivity.getStringArrayExtra(Intent.EXTRA_TEXT);
                isFavorite = intentThatStartedThisActivity.getStringExtra(getString(R.string.intent_key_is_favorite));
                final MovieDetail movieDetailObject = new MovieDetail(movie);
                //Poster size can be one of the following: "w92", "w154", "w185", "w342", "w500", "w780", or "original"
                url = NetworkUtils.posterURL(getString(R.string.image_base_url),
                        getString(R.string.poster_size), movieDetailObject.getPosterPath());
                //Populate the detail info to the view
                Picasso.with(DetailActivity.this).load(url).placeholder(R.drawable.no_image).into(image);
                collapsingToolbar.setTitle(movieDetailObject.getOriginalTitle());
                overview.setText(movieDetailObject.getOverview());
                String rateString = getString(R.string.user_rating) + movieDetailObject.getVoteAverage();
                rate.setText(rateString );
                String dateString = getString(R.string.release_date)+ movieDetailObject.getDate();
                date.setText(dateString);

                //fetch the selected movie trailers and reviews from the database
                fetchMoviesTrailersAndReviews(movieDetailObject.getMovieID());

                //check local database to see if this movie is favored or not
                if (isFavorite.equals(getString(R.string.is_true))) {
                    favouriteButton.setText(getString(R.string.remove_from_favorite_button));
                }

                favouriteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(DetailActivity.this, "button is clicked", Toast.LENGTH_SHORT).show();
                        if (isFavorite.equals(getString(R.string.is_true))) {
                            removeFromFavoriteMovie(movieDetailObject.getMovieID());
                            favouriteButton.setText(getString(R.string.add_to_favorite_button));
                        } else {
                            addToFavoriteMovie(movieDetailObject);
                            favouriteButton.setText(getString(R.string.remove_from_favorite_button));
                        }

                        finish();
                        Intent intentToStayDetailActivity = new Intent(DetailActivity.this, DetailActivity.class);
                        intentToStayDetailActivity.putExtra(Intent.EXTRA_TEXT, movie);
                        String updatedIsFavorite;
                        if (isFavorite.equals(getString(R.string.is_true))) {
                            updatedIsFavorite = getString(R.string.is_false);
                        } else {
                            updatedIsFavorite = getString(R.string.is_true);
                        }
                        intentToStayDetailActivity.putExtra(getString(R.string.intent_key_is_favorite), updatedIsFavorite);
                        startActivity(intentToStayDetailActivity);
                    }
                });
            }
        }
    }

    /**
     * This method is to use AsyncTask to execute the request of movie trailers and reviews
     * @param movieId the sorting method of the data request
     */
    private void fetchMoviesTrailersAndReviews(String movieId) {
        // check the device connectivity
        if (isOnline()){
            Bundle asyncBundle = new Bundle();
            asyncBundle.putString(getString(R.string.loader_bundle_key_movie_id), movieId);
            //create a AsyncTask loader thread for fetching movies data
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<List<MovieTrailer>> trailerAsyncTaskLoader = loaderManager.getLoader(TRAILER_ASYNCTASK_LOADER_ID);
            if (trailerAsyncTaskLoader == null) {
                loaderManager.initLoader(TRAILER_ASYNCTASK_LOADER_ID, asyncBundle, trailerAsyncTaskLoaderListener);
            } else {
                loaderManager.restartLoader(TRAILER_ASYNCTASK_LOADER_ID, asyncBundle, trailerAsyncTaskLoaderListener);
            }
            Loader<List<MovieReview>> reviewAsyncTaskLoader = loaderManager.getLoader(REVIEW_ASYNCTASK_LOADER_ID);
            if (reviewAsyncTaskLoader == null) {
                loaderManager.initLoader(REVIEW_ASYNCTASK_LOADER_ID, asyncBundle, reviewAsyncTaskLoaderListener);
            } else {
                loaderManager.restartLoader(REVIEW_ASYNCTASK_LOADER_ID, asyncBundle, reviewAsyncTaskLoaderListener);
            }
        } else {
            mTrailerRecyclerView.setAlpha(0);
            trailerTitle.setText(R.string.trailer_no_internet);
            mReviewRecyclerView.setAlpha(0);
            reviewTitle.setText(R.string.review_no_internet);
        }
    }

    /**
     * This method is to check the device is currently online or not
     * @return a boolean to check if the device is connected
     */
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    /**
     * This method is for responding to clicks from trailer list.
     * @param trailerKey the YouTube trailer key of the movie
     */
    @Override
    public void onClick(String trailerKey) {
        //build a browser url to open Youtube trailer
        String youtubeUrl = NetworkUtils.trailerURL(getString(R.string.youtube_base_url),
                getString(R.string.trailer_param_key), trailerKey);

        Uri yuotubeWebpage = Uri.parse(youtubeUrl);
        // start an intent to open a browser
        Intent intent = new Intent(Intent.ACTION_VIEW, yuotubeWebpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method is to add a single movie details into the database.
     * @param movie the movie detail information
     */
    private void addToFavoriteMovie(MovieDetail movie) {
        // create a ContentValue to pass the values onto the insert query
        ContentValues cv = new ContentValues();
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, movie.getMovieID());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE, movie.getOriginalTitle());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RATE, movie.getVoteAverage());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_DATE, movie.getDate());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POSTERURI, movie.getPosterPath());
        // use Picasso to fetch the movie poster and store in cache
        Picasso.with(DetailActivity.this).load(url)
                .placeholder(R.drawable.no_image)
                .error(android.R.drawable.stat_notify_error)
                .networkPolicy(NetworkPolicy.OFFLINE)//use this for offline support
                .fetch();
        Uri uri = getContentResolver().insert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, cv);
        if (uri != null) {
//            Toast.makeText(DetailActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            finish();
        }
    }

    /**
     * This method is to remove a single movie details from the database.
     * @param movieId the movie ID information
     */
    private void removeFromFavoriteMovie(String movieId) {
        Bundle asyncFindIdToDeleteBundle = new Bundle();
        asyncFindIdToDeleteBundle.putString(getString(R.string.loader_bundle_key_movie_id), movieId);
        //create a AsyncTask loader thread for fetching movies data
        LoaderManager cursorloaderFindIdToDeleteManager = getSupportLoaderManager();
        Loader<List<MovieTrailer>> cursorFindIdToDeleteLoader = cursorloaderFindIdToDeleteManager.getLoader(CURSOR_FIND_ID_TO_DELETE_LOADER_ID);
        if (cursorFindIdToDeleteLoader == null) {
            cursorloaderFindIdToDeleteManager.initLoader(CURSOR_FIND_ID_TO_DELETE_LOADER_ID, asyncFindIdToDeleteBundle, cursorFindIdToDeleteAsyncTaskLoaderListener);
        } else {
            cursorloaderFindIdToDeleteManager.restartLoader(CURSOR_FIND_ID_TO_DELETE_LOADER_ID, asyncFindIdToDeleteBundle, cursorFindIdToDeleteAsyncTaskLoaderListener);
        }
    }
}
