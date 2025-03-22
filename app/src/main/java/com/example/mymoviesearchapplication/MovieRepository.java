package com.example.mymoviesearchapplication;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.mymoviesearchapplication.model.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
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
 * It uses an AsyncTask to fetch and parse movie data, and then updates LiveData.
 */
public class MovieRepository {

    // OMDb API Key
    private static final String API_KEY = "c57645f8";

    // Base URL for OMDb API search queries
    private static final String BASE_URL = "https://www.omdbapi.com/?apikey=" + API_KEY + "&s=";

    /**
     * Public method to trigger the AsyncTask that fetches movies from the API.
     *
     * @param query           The user's search input
     * @param moviesLiveData  LiveData object that will hold the movie list
     */
    public void getMovies(String query, MutableLiveData<List<MovieItem>> moviesLiveData) {
        new FetchMoviesTask(moviesLiveData).execute(BASE_URL + query); // Build the URL and execute
    }

    /**
     * AsyncTask to perform the network operation on a background thread.
     * It fetches JSON from OMDb, parses it, and returns a list of MovieItem objects.
     */
    private static class FetchMoviesTask extends AsyncTask<String, Void, List<MovieItem>> {
        private final MutableLiveData<List<MovieItem>> liveData;

        public FetchMoviesTask(MutableLiveData<List<MovieItem>> liveData) {
            this.liveData = liveData;
        }

        @Override
        protected List<MovieItem> doInBackground(String... strings) {
            List<MovieItem> movieList = new ArrayList<>();
            try {
                // Step 1: Open connection to the API
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Step 2: Read response from input stream
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }

                // Step 3: Parse the JSON response
                JSONObject jsonObject = new JSONObject(json.toString());

                // Extract the "Search" array which contains movie results
                JSONArray jsonArray = jsonObject.getJSONArray("Search");

                // Step 4: Loop through each movie object in the array
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject movie = jsonArray.getJSONObject(i);

                    // Create a MovieItem from JSON and add to the list
                    movieList.add(new MovieItem(
                            movie.getString("Title"),
                            movie.getString("Year"),
                            movie.getString("imdbID"),
                            movie.getString("Type"),
                            movie.getString("Poster")
                    ));
                }

            } catch (Exception e) {
                // If an error occurs, log it (helps with debugging)
                Log.e("MovieRepository", "Error fetching movies", e);
            }

            return movieList;
        }

        /**
         * Once the background task is done, update the LiveData
         * so the UI (MainActivity) can react to the new list.
         */
        @Override
        protected void onPostExecute(List<MovieItem> movieItems) {
            liveData.setValue(movieItems); // Notifies observers in MainActivity
        }
    }
}
