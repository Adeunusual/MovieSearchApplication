package com.example.mymoviesearchapplication.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviesearchapplication.R;
import com.example.mymoviesearchapplication.model.MovieItem;
import com.example.mymoviesearchapplication.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity handles the movie search screen.
 * It connects the UI to the ViewModel and displays search results in a RecyclerView.
 */
public class MainActivity extends AppCompatActivity {

    // ViewModel that holds and fetches movie data
    private MovieViewModel movieViewModel;

    // Adapter to bind the list of movies to the RecyclerView
    private MovieAdapter movieAdapter;

    // List that stores the movie search results
    private List<MovieItem> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Inflate the layout file for this activity

        // Connect UI elements to Java variables
        EditText searchEditText = findViewById(R.id.searchEditText);
        Button searchButton = findViewById(R.id.searchButton);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // Initialize ViewModel using ViewModelProvider
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Initialize Adapter and define click behavior on each movie item
        movieAdapter = new MovieAdapter(movieList, movie -> {
            if (movie != null) {
                // When a movie is clicked, open the MovieDetailsActivity and pass only the IMDb ID
                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                intent.putExtra("imdbID", movie.getImdbID()); // Pass only the imdbID
                startActivity(intent);
            } else {
                // Show error toast if movie data is missing
                Toast.makeText(this, getString(R.string.toast_movie_missing), Toast.LENGTH_SHORT).show();
            }
        });

        // Setup RecyclerView with a vertical list layout and assign the adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdapter);

        // Handle Search button click
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                // Trigger the ViewModel to fetch movies using the search keyword
                movieViewModel.fetchMovies(query);
            } else {
                // Show toast if search input is empty
                Toast.makeText(this, getString(R.string.toast_enter_movie), Toast.LENGTH_SHORT).show();
            }
        });

        // Observe LiveData from the ViewModel to update the UI automatically
        movieViewModel.getMovies().observe(this, movies -> {
            // Clear old results and add new ones
            movieList.clear();
            movieList.addAll(movies);
            movieAdapter.notifyDataSetChanged(); // Refresh the RecyclerView
        });
    }
}
