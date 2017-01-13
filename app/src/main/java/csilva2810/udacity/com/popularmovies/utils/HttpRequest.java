package csilva2810.udacity.com.popularmovies.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {

    private static final String LOG_TAG = HttpRequest.class.getSimpleName();

    public static String getJson(URL url) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String responseJson = null;

        try {

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder stringBuilder = new StringBuilder();

            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            if (stringBuilder.length() == 0) {
                return null;
            }

            responseJson = stringBuilder.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error:" + e.getMessage());
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return responseJson;

    }

}
