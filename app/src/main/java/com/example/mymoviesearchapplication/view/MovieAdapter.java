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
 * It binds movie data (title, year, poster, rating) to the UI elements and handles item click events.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    // List that holds all the movies to be displayed
    private final List<MovieItem> movieList;

    // Listener interface to handle clicks on individual movie items
    private final OnMovieClickListener listener;

    /**
     * Interface to handle movie item clicks.
     * Implemented by MainActivity.
     */
    public interface OnMovieClickListener {
        void onMovieClick(MovieItem movie);
    }

    /**
     * Adapter constructor to initialize movie list and click listener.
     *
     * @param movieList - List of MovieItem objects
     * @param listener  - Callback when a movie is clicked
     */
    public MovieAdapter(List<MovieItem> movieList, OnMovieClickListener listener) {
        this.movieList = movieList;
        this.listener = listener;
    }

    /**
     * Inflates the layout for each item in the RecyclerView.
     *
     * @param parent   - RecyclerView parent
     * @param viewType - Not used here
     * @return ViewHolder containing the inflated layout
     */
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    /**
     * Binds movie data to each item in the RecyclerView.
     *
     * @param holder   - ViewHolder for the item
     * @param position - Position of the movie in the list
     */
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieItem movie = movieList.get(position);

        // Set title and year
        holder.titleTextView.setText(movie.getTitle());
        holder.yearTextView.setText("Year: " + movie.getYear());

        // Load the movie poster using AsyncTask
        new LoadImageTask(holder.posterImageView).execute(movie.getPoster());

        // Set click listener for each item
        holder.itemView.setOnClickListener(v -> listener.onMovieClick(movie));
    }

    /**
     * Returns the number of items in the movie list.
     *
     * @return total count of movies
     */
    @Override
    public int getItemCount() {
        return movieList.size();
    }

    /**
     * ViewHolder class that holds references to each item view's components.
     */
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, yearTextView, ratingTextView;
        ImageView posterImageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movieTitle);
            yearTextView = itemView.findViewById(R.id.movieYear);
            ratingTextView = itemView.findViewById(R.id.movieRating); // New TextView for rating
            posterImageView = itemView.findViewById(R.id.moviePoster);
        }
    }

    /**
     * AsyncTask to load poster images from a URL in the background.
     * Prevents UI freezing while loading images.
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

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            } else {
                imageView.setImageResource(android.R.drawable.ic_menu_report_image);
            }
        }
    }
}
