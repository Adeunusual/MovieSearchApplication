package com.example.mymoviesearchapplication.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mymoviesearchapplication.R;
import com.example.mymoviesearchapplication.model.MovieDetailItem;
import com.example.mymoviesearchapplication.viewmodel.MovieViewModel;

import java.io.InputStream;
import java.net.URL;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.mymoviesearchapplication.model.FavoriteMovie;


/**
 * MovieDetailsActivity is responsible for displaying detailed information
 * about a selected movie. It fetches data from the ViewModel using the IMDb ID
 * passed from MainActivity and updates the UI accordingly.
 */
public class MovieDetailsActivity extends AppCompatActivity {

    //Firebase declarations
    private Button addToFavoritesButton;
    private FirebaseFirestore db;
    private FirebaseAuth auth;


    // UI components for displaying full movie details
    private ImageView moviePoster;
    private TextView movieTitle, movieYear, movieRated, movieReleased, movieRuntime,
            movieGenre, movieDirector, movieWriter, movieActors, moviePlot,
            movieLanguage, movieCountry, movieAwards, movieRating;

    private ImageButton  backButton;

    // ViewModel reference for fetching the detailed movie data
    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details); // Load the layout

        // Firebase & UI button (set BEFORE observers)
        addToFavoritesButton = findViewById(R.id.addToFavoritesButton);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Connect UI elements to Java variables
        moviePoster = findViewById(R.id.moviePoster);
        movieTitle = findViewById(R.id.movieTitle);
        movieYear = findViewById(R.id.movieYear);
        movieRated = findViewById(R.id.movieRated);
        movieReleased = findViewById(R.id.movieReleased);
        movieRuntime = findViewById(R.id.movieRuntime);
        movieGenre = findViewById(R.id.movieGenre);
        movieDirector = findViewById(R.id.movieDirector);
        movieWriter = findViewById(R.id.movieWriter);
        movieActors = findViewById(R.id.movieActors);
        moviePlot = findViewById(R.id.moviePlot);
        movieLanguage = findViewById(R.id.movieLanguage);
        movieCountry = findViewById(R.id.movieCountry);
        movieAwards = findViewById(R.id.movieAwards);
        movieRating = findViewById(R.id.movieRating);
        backButton = findViewById(R.id.backButton);

        // Initialize the ViewModel to fetch movie data
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Get IMDb ID from the Intent
        String imdbID = getIntent().getStringExtra("imdbID");

        if (imdbID != null) {
            // Call the ViewModel to fetch full movie details
            movieViewModel.fetchMovieDetails(imdbID);

            // Observe the LiveData and update UI when data is available
            movieViewModel.getMovieDetails().observe(this, movie -> {
                if (movie != null) {
                    // Set text fields with movie data
                    movieTitle.setText(movie.getTitle());
                    movieYear.setText("Year: " + movie.getYear());
                    movieRated.setText("Rated: " + movie.getRated());
                    movieReleased.setText("Released: " + movie.getReleased());
                    movieRuntime.setText("Runtime: " + movie.getRuntime());
                    movieGenre.setText("Genre: " + movie.getGenre());
                    movieDirector.setText("Director: " + movie.getDirector());
                    movieWriter.setText("Writer: " + movie.getWriter());
                    movieActors.setText("Actors: " + movie.getActors());
                    moviePlot.setText("Plot: " + movie.getPlot());
                    movieLanguage.setText("Language: " + movie.getLanguage());
                    movieCountry.setText("Country: " + movie.getCountry());
                    movieAwards.setText("Awards: " + movie.getAwards());
                    movieRating.setText("IMDb Rating: " + movie.getImdbRating());

                    // Load poster image from URL
                    String posterUrl = movie.getPoster();
                    if (posterUrl != null && !posterUrl.isEmpty() && !posterUrl.equals("N/A")) {
                        new LoadImageTask(moviePoster).execute(posterUrl);
                    } else {
                        moviePoster.setImageResource(android.R.drawable.ic_menu_report_image);
                    }

                    addToFavoritesButton.setOnClickListener(view -> {
                        String userId = auth.getCurrentUser().getUid();

                        FavoriteMovie fav = new FavoriteMovie(
                                userId,
                                imdbID,
                                movie.getTitle(),
                                movie.getPoster(),
                                movie.getImdbRating(),
                                "" // Empty description, user can edit later
                        );

                        db.collection("favorites")
                                .add(fav)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(this, "Added to Favorites!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to add: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    });

                }
            });
        } else {
            movieTitle.setText("No Movie Selected");
        }

        // Handle back button click - go back to the MainActivity
        backButton.setOnClickListener(v -> finish());

    }

    /**
     * AsyncTask to load movie poster image from a URL without blocking the UI thread.
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
