package com.example.mymoviesearchapplication.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymoviesearchapplication.R;
import com.example.mymoviesearchapplication.model.FavoriteMovie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.InputStream;
import java.net.URL;

/**
 * FavoritesDetailsActivity displays and allows editing of a favorite movie's description,
 * and supports deleting the movie from Firestore.
 */
public class FavoritesDetailsActivity extends AppCompatActivity {

    private ImageView posterImageView;
    private TextView titleTextView, ratingTextView;
    private EditText descriptionEditText;
    private Button updateButton, deleteButton;
    private ImageButton backButton;


    private FirebaseFirestore db;
    private String imdbID;
    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_details);

        // UI elements
        posterImageView = findViewById(R.id.detailsPoster);
        titleTextView = findViewById(R.id.detailsTitle);
        ratingTextView = findViewById(R.id.detailsRating);
        descriptionEditText = findViewById(R.id.detailsDescription);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);

        db = FirebaseFirestore.getInstance();
        imdbID = getIntent().getStringExtra("imdbID");

        if (imdbID != null) {
            loadFavoriteDetails(imdbID);
        }

        updateButton.setOnClickListener(v -> {
            String updatedDesc = descriptionEditText.getText().toString().trim();
            if (!updatedDesc.isEmpty() && documentId != null) {
                db.collection("favorites").document(documentId)
                        .update("description", updatedDesc)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        deleteButton.setOnClickListener(v -> {
            if (documentId != null) {
                db.collection("favorites").document(documentId)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Delete failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Fetch the movie details for the given IMDb ID and display them.
     */
    private void loadFavoriteDetails(String imdbID) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("favorites")
                .whereEqualTo("userId", currentUserId)
                .whereEqualTo("imdbID", imdbID)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        FavoriteMovie movie = doc.toObject(FavoriteMovie.class);
                        documentId = doc.getId();

                        titleTextView.setText(movie.getTitle());
                        ratingTextView.setText("IMDb: " + movie.getRating());
                        descriptionEditText.setText(movie.getDescription());

                        if (movie.getPosterUrl() != null && !movie.getPosterUrl().equals("N/A")) {
                            new LoadImageTask(posterImageView).execute(movie.getPosterUrl());
                        } else {
                            posterImageView.setImageResource(android.R.drawable.ic_menu_report_image);
                        }

                        break; // Only one document per user + movie expected
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load movie: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // AsyncTask for loading image
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
