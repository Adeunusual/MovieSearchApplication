package com.example.mymoviesearchapplication.model;

/**
 * MovieDetailItem is a simple model class used to represent
 * the full detailed information of a movie retrieved from the OMDb API.
 *
 */
public class MovieDetailItem {

    // These fields map directly to the JSON keys returned from the OMDb API
    private String Title;
    private String Year;
    private String Rated;
    private String Released;
    private String Runtime;
    private String Genre;
    private String Director;
    private String Writer;
    private String Actors;
    private String Plot;
    private String Language;
    private String Country;
    private String Awards;
    private String Poster;
    private String imdbRating;

    /**
     * Default constructor required for manual object creation.
     */
    public MovieDetailItem() {
        // Empty constructor
    }

    /**
     * Constructor to initialize all the properties of a movie detail item.
     *
     * @param title        - The movie title
     * @param year         - Year of release
     * @param rated        - Age rating
     * @param released     - Full release date
     * @param runtime      - Duration of the movie
     * @param genre        - Genre(s)
     * @param director     - Director(s)
     * @param writer       - Writer(s)
     * @param actors       - Main cast
     * @param plot         - Short plot summary
     * @param language     - Languages used
     * @param country      - Country of origin
     * @param awards       - Awards won
     * @param poster       - Poster image URL
     * @param imdbRating   - IMDb rating value
     */
    public MovieDetailItem(String title, String year, String rated, String released,
                           String runtime, String genre, String director, String writer,
                           String actors, String plot, String language, String country,
                           String awards, String poster, String imdbRating) {

        this.Title = title;
        this.Year = year;
        this.Rated = rated;
        this.Released = released;
        this.Runtime = runtime;
        this.Genre = genre;
        this.Director = director;
        this.Writer = writer;
        this.Actors = actors;
        this.Plot = plot;
        this.Language = language;
        this.Country = country;
        this.Awards = awards;
        this.Poster = poster;
        this.imdbRating = imdbRating;
    }

    // Getter methods for each movie property

    public String getTitle() {
        return Title;
    }

    public String getYear() {
        return Year;
    }

    public String getRated() {
        return Rated;
    }

    public String getReleased() {
        return Released;
    }

    public String getRuntime() {
        return Runtime;
    }

    public String getGenre() {
        return Genre;
    }

    public String getDirector() {
        return Director;
    }

    public String getWriter() {
        return Writer;
    }

    public String getActors() {
        return Actors;
    }

    public String getPlot() {
        return Plot;
    }

    public String getLanguage() {
        return Language;
    }

    public String getCountry() {
        return Country;
    }

    public String getAwards() {
        return Awards;
    }

    public String getPoster() {
        return Poster;
    }

    public String getImdbRating() {
        return imdbRating;
    }
}
