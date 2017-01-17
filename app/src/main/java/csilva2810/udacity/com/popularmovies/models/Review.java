package csilva2810.udacity.com.popularmovies.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static csilva2810.udacity.com.popularmovies.MainActivity.LOG_TAG;

/**
 * Created by carlinhos on 1/16/17.
 */

public class Review {

    private String id;
    private String authorName;
    private String content;
    private String url;

    public Review(String id, String authorName, String content, String url) {
        this.id = id;
        this.authorName = authorName;
        this.content = content;
        this.url = url;
    }

    public Review(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getContent() {
        return content;
    }

    public String getResumedContent() {
        int contentLength = content.length() > 255 ? 255 : content.length();
        return content.substring(0, contentLength) + "...";
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static List<Review> parseJson(String json) {
        List<Review> reviews = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray reviewsArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < reviewsArray.length(); i++) {
                JSONObject reviewJson = reviewsArray.getJSONObject(i);

                String id = reviewJson.getString("id");
                String authorName = reviewJson.getString("author");
                String content = reviewJson.getString("content");
                String url = reviewJson.getString("url");

                Review review = new Review(id, authorName, content, url);
                reviews.add(review);

            }

        } catch (final JSONException e) {
            Log.e(LOG_TAG, "Parsing error:" + e.getMessage());
        }
        return reviews;

    }

}
