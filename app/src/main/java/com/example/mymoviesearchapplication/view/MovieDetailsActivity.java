package com.example.mymoviesearchapplication.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymoviesearchapplication.R;

import java.io.InputStream;
import java.net.URL;

/**
 * MovieDetailsActivity is responsible for displaying detailed information
 * about a selected movie. It retrieves movie data passed from MainActivity
 * and updates the UI accordingly.
 */
public class MovieDetailsActivity extends AppCompatActivity {

    // UI components for displaying movie details
    private ImageView moviePoster;
    private TextView movieTitle, movieYear, movieType;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Initialize UI elements
        moviePoster = findViewById(R.id.moviePoster);
        movieTitle = findViewById(R.id.movieTitle);
        movieYear = findViewById(R.id.movieYear);
        movieType = findViewById(R.id.movieType);
        backButton = findViewById(R.id.backButton);

        // Get movie data from Intent (sent from MainActivity)
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("title") && intent.hasExtra("year") && intent.hasExtra("type") && intent.hasExtra("poster")) {
            // Set movie details from the received data
            movieTitle.setText(intent.getStringExtra("title"));
            movieYear.setText("Year: " + intent.getStringExtra("year"));
            movieType.setText("Type: " + intent.getStringExtra("type"));

            // Load movie poster asynchronously from the URL
            String posterUrl = intent.getStringExtra("poster");
            if (posterUrl != null && !posterUrl.isEmpty()) {
                new LoadImageTask(moviePoster).execute(posterUrl);
            } else {
                // Display a default placeholder if no image is available
                moviePoster.setImageResource(android.R.drawable.ic_menu_report_image);
            }
        } else {
            // If no data was received, display default values
            movieTitle.setText("No Data");
            movieYear.setText("Unknown");
            movieType.setText("Unknown");
            moviePoster.setImageResource(android.R.drawable.ic_menu_report_image);
        }

        // Handle back button click event - closes the activity and returns to MainActivity
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * AsyncTask for downloading and setting the movie poster image.
     * This ensures image loading is done in the background without freezing the UI.
     */
    private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0]; // Get the image URL
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
         * After the image is downloaded, set it to the ImageView.
         * If image loading fails, display a default placeholder.
         */
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
