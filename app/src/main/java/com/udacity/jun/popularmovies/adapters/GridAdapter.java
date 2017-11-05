/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 1
 * Date: May 25, 2017
 * Reference:
 *      https://github.com/udacity/android-custom-arrayadapter/tree/gridview
 */

package com.udacity.jun.popularmovies.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.squareup.picasso.Picasso;
import com.udacity.jun.popularmovies.R;
import com.udacity.jun.popularmovies.models.MoviePoster;

public class GridAdapter extends ArrayAdapter<MoviePoster> {
    // Create ViewHolder subclass
    private static class ViewHolder {
        ImageView poster;
        TextView title;
    }

    /**
     * Constructor: the context is used to inflate the layout file,
     * and the List is the data to populate into the grid
     * @param context The current context. Used to inflate the layout file.
     * @param posterList A List of posters objects to display in a grid
     */
    public GridAdapter(Activity context, List<MoviePoster> posterList) {
        // initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Set to 0 since it is not used
        super(context, 0, posterList);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     * @param position The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        // Gets the poster object from the ArrayAdapter at the appropriate position
        String posterURL = "";
        String posterTitle = "";
        if (getItem(position) != null) {
            posterURL = getItem(position).getPosterUrl();
            posterTitle = getItem(position).getPosterTitle();
        }

//        // Adapters recycle views to AdapterViews.
//        // If this is a new View object we're getting, then inflate the layout.
//        // If not, this view already has the layout inflated from a previous call to getView,
//        //and modify the View widgets as usual.
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_poster, parent, false);
//        }

        ViewHolder viewHolder; // view lookup cache stored in tag
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.movie_poster, parent, false);
            // handle image, lookup view for image
            viewHolder.poster = (ImageView) convertView.findViewById(R.id.poster_image);
            viewHolder.title = (TextView) convertView.findViewById(R.id.poster_title);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the image into the view using data object, with Picasso image tool
        Picasso.with(getContext()).load(posterURL).placeholder(R.drawable.no_image).into(viewHolder.poster);
        viewHolder.title.setText(posterTitle);

        return convertView;
    }
}
