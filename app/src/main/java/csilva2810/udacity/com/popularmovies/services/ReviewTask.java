package csilva2810.udacity.com.popularmovies.services;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import csilva2810.udacity.com.popularmovies.BuildConfig;
import csilva2810.udacity.com.popularmovies.constants.MoviesApi;
import csilva2810.udacity.com.popularmovies.models.Review;
import csilva2810.udacity.com.popularmovies.utils.AsyncTaskDelegate;
import csilva2810.udacity.com.popularmovies.utils.HttpRequest;

/**
 * Created by carlinhos on 1/13/17.
 */

public class ReviewTask extends AsyncTask<String, Void, List<Review>> {

    private static final String LOG_TAG = ReviewTask.class.getSimpleName();
    private AsyncTaskDelegate mDelegate;

    public ReviewTask(AsyncTaskDelegate delegate) {
        this.mDelegate = delegate;
    }

    @Override
    protected List<Review> doInBackground(String... params) {

        String movieID = params[0];

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(MoviesApi.SCHEME)
                .encodedAuthority(MoviesApi.AUTHORITY)
                .appendPath("movie")
                .appendPath(movieID)
                .appendPath(MoviesApi.REVIEWS_PATH)
                .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY);

        String myUrl = uriBuilder.build().toString();

        try {

            URL url = new URL(myUrl);
            String response = HttpRequest.getJson(url);
            Log.d(LOG_TAG, response);

            return Review.parseJson(response);

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Review> result) {
        if (result != null) {
            mDelegate.onProcessFinish(result, MoviesApi.REVIEWS_PATH);
        }
    }

}
