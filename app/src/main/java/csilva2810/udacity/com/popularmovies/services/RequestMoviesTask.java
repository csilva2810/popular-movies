package csilva2810.udacity.com.popularmovies.services;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import csilva2810.udacity.com.popularmovies.BuildConfig;
import csilva2810.udacity.com.popularmovies.constants.MoviesApi;
import csilva2810.udacity.com.popularmovies.fragments.MoviesGridFragment;
import csilva2810.udacity.com.popularmovies.models.Movie;
import csilva2810.udacity.com.popularmovies.utils.AsyncTaskDelegate;
import csilva2810.udacity.com.popularmovies.utils.HttpRequest;

/**
 * Created by carlinhos on 1/12/17.
 */
public class RequestMoviesTask extends AsyncTask<String, Integer, ArrayList<Movie>> {

    public static final String LOG_TAG = MoviesGridFragment.class.getSimpleName();
    private AsyncTaskDelegate mDelegate;
    private Context mContext;

    public RequestMoviesTask(Context context, AsyncTaskDelegate delegate) {
        this.mContext = context;
        this.mDelegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        ArrayList<Movie> movies;
        String movieType = params[0];

        if (movieType.equals(Movie.MOVIE_FAVORITES)) {
            movies = Movie.getFavorites(mContext);
            return movies;
        }

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(MoviesApi.SCHEME)
                .encodedAuthority(MoviesApi.AUTHORITY)
                .appendPath("movie")
                .appendPath(movieType)
                .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY);

        String myUrl = uriBuilder.build().toString();

        try {

            URL url = new URL(myUrl);
            String response = HttpRequest.getJson(url);

            movies = Movie.parseJson(mContext, response);
            Log.d(LOG_TAG, "Movies: " + movies);
            return movies;

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return null;

    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);
        if (movies != null) {
            mDelegate.onProcessFinish(movies, null);
        }
    }

}
