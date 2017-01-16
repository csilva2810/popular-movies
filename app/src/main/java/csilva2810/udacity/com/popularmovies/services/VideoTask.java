package csilva2810.udacity.com.popularmovies.services;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import csilva2810.udacity.com.popularmovies.BuildConfig;
import csilva2810.udacity.com.popularmovies.constants.MoviesApi;
import csilva2810.udacity.com.popularmovies.models.Video;
import csilva2810.udacity.com.popularmovies.utils.AsyncTaskDelegate;
import csilva2810.udacity.com.popularmovies.utils.HttpRequest;

/**
 * Created by carlinhos on 1/13/17.
 */

public class VideoTask extends AsyncTask<String, Void, List<Video>> {

    private static final String LOG_TAG = VideoTask.class.getSimpleName();
    private AsyncTaskDelegate mDelegate;

    public VideoTask(AsyncTaskDelegate delegate) {
        this.mDelegate = delegate;
    }

    @Override
    protected List<Video> doInBackground(String... params) {

        String movieID = params[0];

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(MoviesApi.SCHEME)
                .encodedAuthority(MoviesApi.AUTHORITY)
                .appendPath("movie")
                .appendPath(movieID)
                .appendPath(MoviesApi.VIDEOS_PATH)
                .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY);

        String myUrl = uriBuilder.build().toString();

        try {

            URL url = new URL(myUrl);
            String response = HttpRequest.getJson(url);
            Log.d(LOG_TAG, response);

            return Video.parseJson(response);

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Video> result) {
        if (result != null) {
            mDelegate.onProcessFinish(result, MoviesApi.VIDEOS_PATH);
        }
    }

}
