/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: May 22, 2017
 * Reference:
 *      https://github.com/udacity/ud851-Sunshine
 *      https://github.com/udacity/ud851-Exercises
 *      http://vickychijwani.me/retrofit-vs-volley/
 *      http://www.vogella.com/tutorials/Retrofit/article.html
 *      https://stackoverflow.com/questions/36177629/retrofit2-android-expected-begin-array-but-was-begin-object-at-line-1-column-2
 */

package com.udacity.jun.popularmovies.utilities;

import android.net.Uri;

import com.udacity.jun.popularmovies.models.ListWrapper;
import com.udacity.jun.popularmovies.models.MovieDetail;
import com.udacity.jun.popularmovies.models.MovieReview;
import com.udacity.jun.popularmovies.models.MovieTrailer;

import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
import java.util.List;

import retrofit2.Call;

/**
 * Utilities to communicate with the network
 */
public class NetworkUtils {
    private final static String SLASH = "/";

    /**
     * This method returns the movie list result from the HTTP response.
     * @param sorting The sorting methods to fetch the HTTP response.
     * @param token The api key to access the database.
     * @param page The number of the page on the current request.
     * @return The MovieDetail list.
     */
    public static List<MovieDetail> getSortingResponseUsingRetrofit(String sorting, String token, int page){
        MovieDbApi list = MovieDbApi.retrofit.create(MovieDbApi.class);
        Call<ListWrapper<MovieDetail>> call = list.getSortedMovieList(sorting, token, page);
        ListWrapper<MovieDetail> result = null;
        try {
            result = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result != null) {
            return result.items;
        } else {
            return null;
        }
    }

    /**
     * This method returns the movie trailer list result from the HTTP response.
     * @param movieId The selected movie ID in the database.
     * @param token The api key to access the database.
     * @return The MovieTrailer list.
     */
    public static List<MovieTrailer> getTrailerResponseUsingRetrofit(String movieId, String token){
        MovieDbApi list = MovieDbApi.retrofit.create(MovieDbApi.class);
        Call<ListWrapper<MovieTrailer>> call = list.getMovieTrailersList(movieId, token);
        ListWrapper<MovieTrailer> result = null;
        try {
            result = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result != null) {
            return result.items;
        } else {
            return null;
        }
    }

    /**
     * This method returns the movie review list result from the HTTP response.
     * @param movieId The selected movie ID in the database.
     * @param token The api key to access the database.
     * @return The MovieReview list.
     */
    public static List<MovieReview> getReviewResponseUsingRetrofit(String movieId, String token){
        MovieDbApi list = MovieDbApi.retrofit.create(MovieDbApi.class);
        Call<ListWrapper<MovieReview>> call = list.getMovieReviewsList(movieId, token);
        ListWrapper<MovieReview> result = null;
        try {
            result = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result != null) {
            return result.items;
        } else {
            return null;
        }
    }

    /**
     * Builds the URL used to request a movie poster image
     * @param baseUrl the base of the url
     * @param posterSize size of the poster ("w92", "w154", "w185", "w342", "w500", "w780", or "original")
     * @param posterPath the path of the poster returned by the query
     * @return The URL string to use to fetch the poster from themoviedb.org
     */
    public static String posterURL(String baseUrl, String posterSize, String posterPath) {
        Uri posterURI;
        posterURI = Uri.parse(baseUrl).buildUpon().appendPath(posterSize).build();
        String newUrl = posterURI.toString();
        newUrl = newUrl + SLASH + posterPath;
        return newUrl;
    }

    /**
     * Builds the URL used to request a movie trailer from Youtube
     * @param baseYoutubeUrl the base of the Youtube url
     * @param key the trailer key
     * @return The URL string to use to fetch the trailer from Youtube
     */
    public static String trailerURL(String baseYoutubeUrl, String paramKey, String key) {
        Uri trailerURI;
        trailerURI = Uri.parse(baseYoutubeUrl).buildUpon().appendQueryParameter(paramKey, key).build();
        return trailerURI.toString();
    }
}


