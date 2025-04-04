package com.example.mymoviesearchapplication.model;

/**
 * FavoriteMovie is a model class used to store a user's saved movie
 * inside Firebase Firestore. Each entry is tied to a user and includes
 * basic info + editable description.
 */
public class FavoriteMovie {

    private String userId;       // Firebase Auth UID
    private String imdbID;       // Unique movie ID from OMDb
    private String title;        // Movie title
    private String posterUrl;    // URL to poster image
    private String rating;       // IMDb rating
    private String description;  // Editable user description

    // Required empty constructor for Firestore deserialization
    public FavoriteMovie() {
    }

    // Constructor used when adding to Firestore
    public FavoriteMovie(String userId, String imdbID, String title, String posterUrl, String rating, String description) {
        this.userId = userId;
        this.imdbID = imdbID;
        this.title = title;
        this.posterUrl = posterUrl;
        this.rating = rating;
        this.description = description;
    }

    // Getters and Setters

    public String getUserId() {
        return userId;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
