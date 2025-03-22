package com.example.mymoviesearchapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymoviesearchapplication.model.MovieItem;
import com.example.mymoviesearchapplication.MovieRepository;

import java.util.List;

/**
 * MovieViewModel acts as the bridge between the UI (MainActivity)
 * and the data source (MovieRepository).
 * It follows the MVVM architecture and holds the movie data using LiveData.
 */
public class MovieViewModel extends ViewModel {

    // MutableLiveData stores the list of movies and allows updates internally
    private final MutableLiveData<List<MovieItem>> moviesLiveData = new MutableLiveData<>();

    // Reference to the repository that handles data fetching logic
    private final MovieRepository movieRepository;

    /**
     * Constructor initializes the repository.
     */
    public MovieViewModel() {
        movieRepository = new MovieRepository();
    }

    /**
     * Expose LiveData to the UI so it can observe changes,
     * but not modify the list directly (ensures encapsulation).
     */
    public LiveData<List<MovieItem>> getMovies() {
        return moviesLiveData;
    }

    /**
     * Fetch movies from the API using the repository
     * and update the LiveData to notify the UI (MainActivity).
     *
     * @param query - search term entered by the user
     */
    public void fetchMovies(String query) {
        movieRepository.getMovies(query, moviesLiveData);
    }
}
