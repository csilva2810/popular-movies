package csilva2810.udacity.com.popularmovies.models;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import csilva2810.udacity.com.popularmovies.constants.MoviesApi;
import csilva2810.udacity.com.popularmovies.database.MovieContract;
import csilva2810.udacity.com.popularmovies.utils.DateUtils;

/**
 * Created by carlinhos on 12/8/16.
 */

public class Movie implements Parcelable {

    private static final String LOG_TAG = Movie.class.getSimpleName();
    public static final String MOVIE_FAVORITES = "favorites";

    public static final String MOVIE_POPULAR = "popular";
    public static final String MOVIE_TOP_RATED = "top_rated";

    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";
    public static final String EXTRA_MOVIE_OPERATION = "EXTRA_OPERATION";

    public static final int FLAG_REMOVED = 1;
    public static final int FLAG_ADDED = 2;

    private long id;
    private String title;
    private String posterImage;
    private String backdropImage;
    private String overview;
    private double voteAverage;
    private long releaseDate;
    private boolean isFavorite;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Movie(long id, String title, String posterImage, String backdropImage, String overview, double voteAverage, long releaseDate) {
        this.id = id;
        this.title = title;
        this.posterImage = posterImage;
        this.backdropImage = backdropImage;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public Movie(Parcel movie) {
        this.id = movie.readLong();
        this.title = movie.readString();
        this.posterImage = movie.readString();
        this.backdropImage = movie.readString();
        this.overview = movie.readString();
        this.voteAverage = movie.readDouble();
        this.releaseDate = movie.readLong();
        this.isFavorite = movie.readByte() != 0;
    }

    public String getBackdropImage() {
        return backdropImage;
    }

    public void setBackdropImage(String backdropImage) {
        this.backdropImage = backdropImage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public long getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(long releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return  this.getId() + " - " +
                this.getTitle() + " - " +
                this.getReleaseDate() + " - " +
                this.getBackdropImage() + " - " +
                this.getPosterImage();
    }

    public static List<Movie> parseJson(String json) {
        List<Movie> movies = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray moviesArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movieJson = moviesArray.getJSONObject(i);

                int id = movieJson.getInt("id");
                String title = movieJson.getString("title");
                String posterImage =
                        MoviesApi.IMAGES_URL + MoviesApi.IMAGE_POSTER_MEDIUM + movieJson.getString("poster_path");
                String backdropImage =
                        MoviesApi.IMAGES_URL + MoviesApi.IMAGE_BACKDROP_MEDIUM + movieJson.getString("backdrop_path");
                String overview = movieJson.getString("overview");
                Double voteAverage = movieJson.getDouble("vote_average");
                long releaseDate = DateUtils.dateInMillis(movieJson.getString("release_date"));

                Movie m = new Movie(id, title, posterImage, backdropImage, overview, voteAverage, releaseDate);
                movies.add(m);

            }

        } catch (final JSONException e) {
            Log.e(LOG_TAG, "Parsing error:" + e.getMessage());
        }
        return movies;
    }

    public static List<Movie> getFavorites(Context context) {
        String columns[] = new String[] {
                MovieContract.MovieEntry.COLUMN_API_ID,
                MovieContract.MovieEntry.COLUMN_TITLE,
                MovieContract.MovieEntry.COLUMN_POSTER,
                MovieContract.MovieEntry.COLUMN_BACKDROP,
                MovieContract.MovieEntry.COLUMN_OVERVIEW,
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE
        };
        final int INDEX_API_ID = 0;
        final int INDEX_TITLE = 1;
        final int INDEX_POSTER = 2;
        final int INDEX_BACKDROP = 3;
        final int INDEX_OVERVIEW = 4;
        final int INDEX_VOTE_AVERAGE = 5;
        final int INDEX_RELEASE_DATE = 6;

        List<Movie> movies = new ArrayList<>();
        Cursor c = context.getContentResolver().query(
                MovieContract.MovieEntry.getMovieUri(),
                columns, null, null, null
        );

        if (c == null) {
            return null;
        }

        while (c.moveToNext()) {
            final Movie movie = new Movie(
                    c.getLong(INDEX_API_ID),
                    c.getString(INDEX_TITLE),
                    c.getString(INDEX_POSTER),
                    c.getString(INDEX_BACKDROP),
                    c.getString(INDEX_OVERVIEW),
                    c.getDouble(INDEX_VOTE_AVERAGE),
                    c.getLong(INDEX_RELEASE_DATE)
            );
            Log.d(LOG_TAG, "Favorite Movies: " + movie.toString());
            movies.add(movie);
        }

        c.close();
        return movies;
    }

    @Nullable
    public static Set<Long> getFavoritesId(Context context) {
        Set<Long> favorites = new HashSet<>();
        try {

            int INDEX_APP_ID = 0;
            Cursor c = context.getContentResolver().query(
                    MovieContract.MovieEntry.getMovieUri(),
                    new String[] {MovieContract.MovieEntry.COLUMN_API_ID},
                    null, null, null
            );

            if (c != null) {
                while (c.moveToNext()) {
                    favorites.add(c.getLong(INDEX_APP_ID));
                }
                c.close();
            }

            Log.d(LOG_TAG, "Favorites Movies: " + favorites);

            return favorites;

        } catch (IllegalArgumentException | SQLException e) {
            Log.d(LOG_TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(posterImage);
        dest.writeString(backdropImage);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeLong(releaseDate);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }

    static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel movie) {
            return new Movie(movie);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
