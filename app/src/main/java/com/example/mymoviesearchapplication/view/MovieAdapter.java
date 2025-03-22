package com.example.mymoviesearchapplication.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviesearchapplication.R;
import com.example.mymoviesearchapplication.model.MovieItem;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * MovieAdapter is a RecyclerView Adapter responsible for displaying movie items in a list.
 * It binds movie data to the UI elements and handles click events.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final List<MovieItem> movieList; // List to store movie data
    private final OnMovieClickListener listener; // Click listener for movie selection

    /**
     * Interface for handling click events on movie items.
     */
    public interface OnMovieClickListener {
        void onMovieClick(MovieItem movie);
    }

    /**
     * Constructor for the adapter.
     * @param movieList - List of movies to display.
     * @param listener - Callback for handling movie click events.
     */
    public MovieAdapter(List<MovieItem> movieList, OnMovieClickListener listener) {
        this.movieList = movieList;
        this.listener = listener;
    }

    /**
     * Creates a new ViewHolder when needed.
     * Inflates the `item_movie.xml` layout to be used in the RecyclerView.
     */
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    /**
     * Binds data to the ViewHolder.
     * This method is called for each item in the list to update UI elements.
     */
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieItem movie = movieList.get(position);

        // Set movie title and release year
        holder.titleTextView.setText(movie.getTitle());
        holder.yearTextView.setText(movie.getYear());

        // Load image using AsyncTask
        new LoadImageTask(holder.posterImageView).execute(movie.getPoster());

        // Handle click event for the movie item
        holder.itemView.setOnClickListener(v -> listener.onMovieClick(movie));
    }

    /**
     * Returns the total number of items in the movie list.
     */
    @Override
    public int getItemCount() {
        return movieList.size();
    }

    /**
     * ViewHolder class that represents a single item in the RecyclerView.
     * It holds references to the UI components of `item_movie.xml`.
     */
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, yearTextView;
        ImageView posterImageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movieTitle);
            yearTextView = itemView.findViewById(R.id.movieYear);
            posterImageView = itemView.findViewById(R.id.moviePoster);
        }
    }

    /**
     * AsyncTask to load images from the internet in the background.
     * This prevents blocking the main UI thread while fetching images.
     */
    private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        /**
         * After the image is downloaded, set it in the ImageView.
         * If the image fails to load, display a default error icon.
         */
        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            } else {
                imageView.setImageResource(android.R.drawable.ic_menu_report_image);// Default image if loading fails
            }
        }
    }
}
