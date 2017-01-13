package csilva2810.udacity.com.popularmovies.services;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import csilva2810.udacity.com.popularmovies.BuildConfig;
import csilva2810.udacity.com.popularmovies.constants.MoviesApi;
import csilva2810.udacity.com.popularmovies.fragments.MoviesGridFragment;
import csilva2810.udacity.com.popularmovies.models.Movie;
import csilva2810.udacity.com.popularmovies.utils.AsynkTaskDelegate;
import csilva2810.udacity.com.popularmovies.utils.HttpRequest;

/**
 * Created by carlinhos on 1/12/17.
 */
public class RequestMoviesTask extends AsyncTask<String, Integer, List<Movie>> {

    public static final String LOG_TAG = MoviesGridFragment.class.getSimpleName();
    private AsynkTaskDelegate mDelegate;

    public RequestMoviesTask(AsynkTaskDelegate delegate) {
        this.mDelegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected List<Movie> doInBackground(String... params) {

        String movieType = params[0];

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(MoviesApi.SCHEME)
                .encodedAuthority(MoviesApi.URL)
                .appendPath("movie")
                .appendPath(movieType)
                .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY);

        String myUrl = uriBuilder.build().toString();

        try {

            URL url = new URL(myUrl);
            String response = HttpRequest.getJson(url);

            return Movie.parseJson(response);

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return null;

    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        if (movies != null) {
            mDelegate.onProcessFinish(movies);
        }
    }

}
