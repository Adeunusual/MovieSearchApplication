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
import com.example.mymoviesearchapplication.model.FavoriteMovie;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * FavoritesAdapter binds favorite movie data to a RecyclerView row.
 * It displays the poster, title, and rating.
 */
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    private final List<FavoriteMovie> favorites;
    private final OnFavoriteClickListener listener;

    // Listener interface for row click
    public interface OnFavoriteClickListener {
        void onFavoriteClick(FavoriteMovie movie);
    }

    public FavoritesAdapter(List<FavoriteMovie> favorites, OnFavoriteClickListener listener) {
        this.favorites = favorites;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        FavoriteMovie movie = favorites.get(position);
        holder.title.setText(movie.getTitle());
        holder.rating.setText("IMDb: " + movie.getRating());

        new LoadImageTask(holder.poster).execute(movie.getPosterUrl());

        holder.itemView.setOnClickListener(v -> listener.onFavoriteClick(movie));
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    static class FavoritesViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title, rating;

        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.favoritePoster);
            title = itemView.findViewById(R.id.favoriteTitle);
            rating = itemView.findViewById(R.id.favoriteRating);
        }
    }

    // AsyncTask to load poster from URL
    private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                InputStream in = new URL(urls[0]).openStream();
                return BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null)
                imageView.setImageBitmap(result);
            else
                imageView.setImageResource(android.R.drawable.ic_menu_report_image);
        }
    }
}
