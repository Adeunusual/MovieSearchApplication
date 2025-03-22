package com.example.mymoviesearchapplication;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.mymoviesearchapplication.model.MovieDetailItem;
import com.example.mymoviesearchapplication.model.MovieItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * MovieRepository handles the actual data-fetching logic from the OMDb API.
 * It supports both search and detail endpoints and updates LiveData objects
 * so that ViewModels or Activities can observe and react to the data.
 */
public class MovieRepository {

    // This is your personal OMDb API Key
    private static final String API_KEY = "c57645f8";

    // Base URL for searching movies by title
    private static final String BASE_SEARCH_URL = "https://www.omdbapi.com/?apikey=" + API_KEY + "&s=";

    // Base URL for fetching full movie details using IMDb ID
    private static final String BASE_DETAIL_URL = "https://www.omdbapi.com/?apikey=" + API_KEY + "&i=";

    /**
     * Triggers the AsyncTask to fetch a list of movies that match the user's search query.
     *
     * @param query           - The user's search text (e.g., "Batman")
     * @param moviesLiveData - LiveData that will be updated with the list of MovieItem objects
     */
    public void getMovies(String query, MutableLiveData<List<MovieItem>> moviesLiveData) {
        new FetchMoviesTask(moviesLiveData).execute(BASE_SEARCH_URL + query);
    }

    /**
     * Triggers the AsyncTask to fetch detailed information about a specific movie.
     *
     * @param imdbID               - Unique IMDb ID for the selected movie
     * @param movieDetailLiveData - LiveData that will be updated with the MovieDetailItem
     */
    public void getMovieDetails(String imdbID, MutableLiveData<MovieDetailItem> movieDetailLiveData) {
        new FetchMovieDetailsTask(movieDetailLiveData).execute(BASE_DETAIL_URL + imdbID);
    }

    /**
     * FetchMoviesTask handles background network requests to search for movies.
     * It parses the JSON result and creates a list of MovieItem objects.
     */
    private static class FetchMoviesTask extends AsyncTask<String, Void, List<MovieItem>> {
        private final MutableLiveData<List<MovieItem>> liveData;

        /**
         * Constructor to pass the MutableLiveData reference.
         *
         * @param liveData - LiveData to update after fetching data
         */
        public FetchMoviesTask(MutableLiveData<List<MovieItem>> liveData) {
            this.liveData = liveData;
        }

        /**
         * Runs in the background to connect to the OMDb API and fetch search results.
         *
         * @param urls - API URL for the search query
         * @return List of MovieItem objects parsed from the JSON response
         */
        @Override
        protected List<MovieItem> doInBackground(String... urls) {
            List<MovieItem> movieList = new ArrayList<>();

            try {
                // Step 1: Open connection to the OMDb search endpoint
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Step 2: Read the response stream
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                // Step 3: Store the full JSON response in a string
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }

                // Step 4: Convert the JSON string to a JSONObject
                JSONObject jsonObject = new JSONObject(json.toString());

                // Step 5: Get the "Search" array which holds all movie results
                JSONArray jsonArray = jsonObject.getJSONArray("Search");

                // Step 6: Loop through each movie object in the array
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject movie = jsonArray.getJSONObject(i);

                    // Step 7: Create a new MovieItem and add it to the list
                    movieList.add(new MovieItem(
                            movie.getString("Title"),
                            movie.getString("Year"),
                            movie.getString("imdbID"),
                            movie.getString("Type"),
                            movie.getString("Poster")
                    ));
                }

            } catch (Exception e) {
                // Catch and log any errors (e.g., network issues, JSON errors)
                Log.e("MovieRepository", "Error fetching movie list", e);
            }

            // Return the list of movies
            return movieList;
        }

        /**
         * Once the background task finishes, update the LiveData.
         *
         * @param movieItems - The list of movies to be shown in the UI
         */
        @Override
        protected void onPostExecute(List<MovieItem> movieItems) {
            liveData.setValue(movieItems);
        }
    }

    /**
     * FetchMovieDetailsTask handles background network requests to fetch full movie details.
     * It creates a MovieDetailItem object using the JSON data.
     */
    private static class FetchMovieDetailsTask extends AsyncTask<String, Void, MovieDetailItem> {
        private final MutableLiveData<MovieDetailItem> liveData;

        /**
         * Constructor to pass the MutableLiveData reference.
         *
         * @param liveData - LiveData to update after fetching movie detail
         */
        public FetchMovieDetailsTask(MutableLiveData<MovieDetailItem> liveData) {
            this.liveData = liveData;
        }

        /**
         * Runs in the background to fetch detailed movie information.
         *
         * @param urls - API URL for the detail request
         * @return MovieDetailItem created from the API JSON response
         */
        @Override
        protected MovieDetailItem doInBackground(String... urls) {
            MovieDetailItem detailItem = null;

            try {
                // Step 1: Open connection to the OMDb detail endpoint
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Step 2: Read the response
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                // Step 3: Convert the response to a JSON string
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }

                // Step 4: Convert string to JSONObject
                JSONObject movie = new JSONObject(json.toString());

                // Step 5: Create a MovieDetailItem using the JSON values
                detailItem = new MovieDetailItem(
                        movie.getString("Title"),
                        movie.getString("Year"),
                        movie.getString("Rated"),
                        movie.getString("Released"),
                        movie.getString("Runtime"),
                        movie.getString("Genre"),
                        movie.getString("Director"),
                        movie.getString("Writer"),
                        movie.getString("Actors"),
                        movie.getString("Plot"),
                        movie.getString("Language"),
                        movie.getString("Country"),
                        movie.getString("Awards"),
                        movie.getString("Poster"),
                        movie.getString("imdbRating")
                );

            } catch (Exception e) {
                // If something goes wrong, log the error
                Log.e("MovieRepository", "Error fetching movie details", e);
            }

            // Return the fully constructed detail item
            return detailItem;
        }

        /**
         * After the detail is fetched, update the LiveData so UI can display it.
         *
         * @param detailItem - The complete MovieDetailItem for the selected movie
         */
        @Override
        protected void onPostExecute(MovieDetailItem detailItem) {
            liveData.setValue(detailItem);
        }
    }
}
