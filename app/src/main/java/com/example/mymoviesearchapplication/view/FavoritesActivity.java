package com.example.mymoviesearchapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviesearchapplication.R;
import com.example.mymoviesearchapplication.model.FavoriteMovie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * FavoritesActivity displays the list of favorite movies saved by the logged-in user.
 * It fetches the data from Firebase Firestore and shows it in a RecyclerView.
 */
public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private List<FavoriteMovie> favoriteList;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.favoritesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        favoriteList = new ArrayList<>();
        adapter = new FavoritesAdapter(favoriteList, selectedMovie -> {
            // Navigate to FavoritesDetailsActivity with Firestore document ID
            Intent intent = new Intent(FavoritesActivity.this, FavoritesDetailsActivity.class);
            intent.putExtra("imdbID", selectedMovie.getImdbID());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        // Firebase setup
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadFavorites();

        ImageButton backToSearchButton = findViewById(R.id.backToSearchButton);
        backToSearchButton.setOnClickListener(v -> finish());


    }

    /**
     * Loads all favorite movies for the current user from Firestore.
     */
    private void loadFavorites() {
        String currentUserId = auth.getCurrentUser().getUid();

        db.collection("favorites")
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        favoriteList.clear();
                        QuerySnapshot snapshot = task.getResult();

                        for (QueryDocumentSnapshot doc : snapshot) {
                            FavoriteMovie movie = doc.toObject(FavoriteMovie.class);
                            favoriteList.add(movie);
                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to load favorites.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
