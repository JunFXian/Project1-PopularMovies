/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: Jun 21, 2017
 * Reference:
 */

package com.udacity.jun.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.jun.popularmovies.R;
import com.udacity.jun.popularmovies.models.MovieReview;

import java.util.List;

//import android.database.Cursor;

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ReviewViewHolder> {
    // The context is used to utility methods, app resources and layout inflaters
    private final Context mContext;
//    // cursor to locate the position
//    private Cursor mCursor;
    private final List<MovieReview> mReviewList;

    /**
     * Constructor to creates a ReviewRecyclerViewAdapter
     * @param context      Used to talk to the UI and app resources
     */
    public ReviewRecyclerViewAdapter(@NonNull Context context, List<MovieReview> list) {
        mContext = context;
        mReviewList = list;
    }

    // A ViewHolder is a required part of the pattern for RecyclerViews.
    class ReviewViewHolder extends RecyclerView.ViewHolder {
        final TextView authorView;
        final TextView contentView;
        final TextView urlView;

        ReviewViewHolder(View view) {
            super(view);
            authorView = (TextView) view.findViewById(R.id.review_author);
            contentView = (TextView) view.findViewById(R.id.review_content);
            urlView = (TextView) view.findViewById(R.id.review_url);
        }
    }

    /**
     * This gets called when each new ViewHolder is created.
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item you can use this viewType
     *                  integer to provide a different layout
     * @return A new ViewHolder that holds the View for each list item
     */
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_review_list, viewGroup, false);
        view.setFocusable(true);
        return new ReviewViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position.
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ReviewViewHolder viewHolder, int position) {
//        mCursor.moveToPosition(position);
        //review author
        String author = mReviewList.get(position).getAuthor() + " :";
        viewHolder.authorView.setText(author);
        //review content
        viewHolder.contentView.setText(mReviewList.get(position).getContent());
        //review url
        String urlLink = "<URL: " + mReviewList.get(position).getUrl() + ">";
        viewHolder.urlView.setText(urlLink);
    }

    /**
     * This method simply returns the number of items to display
     * @return The number of items available in trailer list
     */
    @Override
    public int getItemCount() {
//        if (null == mCursor) return 0;
//        return mCursor.getCount();
        return mReviewList.size();
    }
}