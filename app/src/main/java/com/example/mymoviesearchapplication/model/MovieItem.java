package com.example.mymoviesearchapplication.model;

/**
 * MovieItem is a simple model class used to represent
 * the data for a single movie retrieved from the OMDb API.
 */
public class MovieItem {
    // These fields map directly to the movie properties returned from the API
    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster;

    /**
     * Constructor to initialize a MovieItem object with all required movie details.
     *
     * @param title   - The title of the movie
     * @param year    - The release year of the movie
     * @param imdbID  - The unique IMDb ID for the movie
     * @param type    - The type of result (e.g., movie, series)
     * @param poster  - URL string to the movie poster image
     */
    public MovieItem(String title, String year, String imdbID, String type, String poster) {
        this.Title = title;
        this.Year = year;
        this.imdbID = imdbID;
        this.Type = type;
        this.Poster = poster;
    }

    // Getter methods to access each property of the movie
    public String getTitle() {
        return Title;
    }

    public String getYear() {
        return Year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getType() {
        return Type;
    }

    public String getPoster() {
        return Poster;
    }
}
