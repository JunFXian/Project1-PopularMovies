/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: Jun 21, 2017
 * Reference:
 *      https://github.com/chrisbanes/cheesesquare (chrisbanes, Jun 21, 2017)
 *      https://github.com/udacity/ud851-Sunshine
 */

package com.udacity.jun.popularmovies.adapters;

import android.content.Context;
//import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.jun.popularmovies.R;
import com.udacity.jun.popularmovies.models.MovieTrailer;

import java.util.List;

public class TrailerRecyclerViewAdapter extends RecyclerView.Adapter<TrailerRecyclerViewAdapter.TrailerViewHolder> {
    // The context is used to utility methods, app resources and layout inflaters
    private final Context mContext;
//    // cursor to locate the position
//    private Cursor mCursor;
    private final List<MovieTrailer> mTrailerList;

    /**
     * define an interface to handle clicks on items within this Adapter. In the
     * constructor of TrailerRecyclerViewAdapter, we receive an instance of a class that has implemented
     * said interface. We store that instance in this variable to call the onClick method whenever
     * an item is clicked in the list.
     */
    final private RecyclerViewAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface RecyclerViewAdapterOnClickHandler {
        void onClick(String key);
    }

    /**
     * Constructor to creates a TrailerRecyclerViewAdapter
     * @param context      Used to talk to the UI and app resources
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public TrailerRecyclerViewAdapter(@NonNull Context context, List<MovieTrailer> list,
                                      RecyclerViewAdapterOnClickHandler clickHandler) {
        mContext = context;
        mTrailerList = list;
        mClickHandler = clickHandler;
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a RecyclerView item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView buttonView;
        final TextView nameView;

        TrailerViewHolder(View view) {
            super(view);
            buttonView = (ImageView) view.findViewById(R.id.play_button_image);
            nameView = (TextView) view.findViewById(R.id.trailer_name);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click. We fetch the trailer that has been
         * selected, and then call the onClick handler registered with this adapter, passing that
         * trailer url.
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            //Read date from the cursor
//            mCursor.moveToPosition(adapterPosition);
            String trailerKey = mTrailerList.get(adapterPosition).getKey();
            mClickHandler.onClick(trailerKey);
        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item you can use this viewType
     *                  integer to provide a different layout
     * @return A new ViewHolder that holds the View for each list item
     */
    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_trailer_list, viewGroup, false);
        view.setFocusable(true);
        return new TrailerViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the trailer
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(TrailerViewHolder viewHolder, int position) {
//        mCursor.moveToPosition(position);

        //button image
        viewHolder.buttonView.setImageResource(R.drawable.icon_play_button);

        //trailer name
        String trailerName = mTrailerList.get(position).getName();
        viewHolder.nameView.setText(trailerName);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     * @return The number of items available in trailer list
     */
    @Override
    public int getItemCount() {
//        if (null == mCursor) return 0;
//        return mCursor.getCount();
        return mTrailerList.size();
    }
}