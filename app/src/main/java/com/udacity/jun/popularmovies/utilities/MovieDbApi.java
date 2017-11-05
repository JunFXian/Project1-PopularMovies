/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: Jun 19, 2017
 * Reference:
 *      http://www.vogella.com/tutorials/Retrofit/article.html
 *      https://medium.com/@shelajev/how-to-make-http-calls-on-android-with-retrofit-2-cfc4a67c6254
 */

package com.udacity.jun.popularmovies.utilities;

import com.udacity.jun.popularmovies.models.ListWrapper;
import com.udacity.jun.popularmovies.models.MovieDetail;
import com.udacity.jun.popularmovies.models.MovieReview;
import com.udacity.jun.popularmovies.models.MovieTrailer;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDbApi {
    String BASE_URL = "http://api.themoviedb.org";
    String SORT_URL = "/3/movie/{sort}";
    String APIKEY_QUERY = "api_key";
    String PAGE_QUERY = "page";
    String TRAILER_URL = "/3/movie/{id}/videos";
    String REVIEW_URL = "/3/movie/{id}/reviews";

    @GET(SORT_URL)
    Call<ListWrapper<MovieDetail>> getSortedMovieList(@Path("sort") String sortMethod,
                                                      @Query(APIKEY_QUERY) String apiKey,
                                                      @Query(PAGE_QUERY) Integer pageNumber);

    @GET(TRAILER_URL)
    Call<ListWrapper<MovieTrailer>> getMovieTrailersList(@Path("id") String movieId,
                                                         @Query(APIKEY_QUERY) String apiKey);

    @GET(REVIEW_URL)
    Call<ListWrapper<MovieReview>> getMovieReviewsList(@Path("id") String movieId,
                                                       @Query(APIKEY_QUERY) String apiKey);

    //To use the interface during runtime, need to build a Retrofit object
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
