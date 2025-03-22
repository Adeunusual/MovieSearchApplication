package com.example.mymoviesearchapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymoviesearchapplication.MovieRepository;
import com.example.mymoviesearchapplication.model.MovieItem;
import com.example.mymoviesearchapplication.model.MovieDetailItem;

import java.util.List;

/**
 * MovieViewModel acts as the bridge between the UI (MainActivity or DetailActivity)
 * and the data layer (MovieRepository). It follows the MVVM architecture pattern,
 * and stores all movie-related data using LiveData so the UI can observe changes.
 */
public class MovieViewModel extends ViewModel {

    // LiveData to hold a list of movies for the search screen
    private final MutableLiveData<List<MovieItem>> moviesLiveData = new MutableLiveData<>();

    // LiveData to hold detailed info for a single movie (for details screen)
    private final MutableLiveData<MovieDetailItem> movieDetailLiveData = new MutableLiveData<>();

    // Repository to handle API calls
    private final MovieRepository movieRepository;

    /**
     * Constructor that initializes the repository object.
     */
    public MovieViewModel() {
        movieRepository = new MovieRepository();
    }

    /**
     * Expose the list of MovieItem objects to the UI.
     * This list is observed by the MainActivity (search screen).
     *
     * @return LiveData holding the list of search results
     */
    public LiveData<List<MovieItem>> getMovies() {
        return moviesLiveData;
    }

    /**
     * Expose the MovieDetailItem to the UI.
     * This is used by the DetailActivity or DetailFragment to show full movie info.
     *
     * @return LiveData holding the selected movie's details
     */
    public LiveData<MovieDetailItem> getMovieDetails() {
        return movieDetailLiveData;
    }

    /**
     * Fetches movies based on the search query entered by the user.
     * This triggers the repository and updates the moviesLiveData object.
     *
     * @param query - The search keyword (e.g., "Batman, superman")
     */
    public void fetchMovies(String query) {
        movieRepository.getMovies(query, moviesLiveData);
    }

    /**
     * Fetches full details for a single movie using its IMDb ID.
     * This updates the movieDetailLiveData object so the UI can observe and display it.
     *
     * @param imdbID - The unique ID of the selected movie (e.g., "tt1234567")
     */
    public void fetchMovieDetails(String imdbID) {
        movieRepository.getMovieDetails(imdbID, movieDetailLiveData);
    }
}
