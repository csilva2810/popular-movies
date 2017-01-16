package csilva2810.udacity.com.popularmovies.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import csilva2810.udacity.com.popularmovies.constants.MoviesApi;

import static csilva2810.udacity.com.popularmovies.MainActivity.LOG_TAG;

/**
 * Created by carlinhos on 1/15/17.
 */

public class Video {

    private String id;
    private String key;
    private String name;
    private String site;
    private String type;

    public Video(String id, String key, String name, String site, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static List<Video> parseJson(String json) {
        List<Video> videos = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray moviesArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject videoJson = moviesArray.getJSONObject(i);

                String id = videoJson.getString("id");
                String key = videoJson.getString("key");
                String name = videoJson.getString("name");
                String site = videoJson.getString("site");
                String type = videoJson.getString("type");

                if (!site.toUpperCase().equals("YOUTUBE")) {
                    continue;
                }

                Video video = new Video(id, key, name, site, type);
                videos.add(video);

            }

        } catch (final JSONException e) {
            Log.e(LOG_TAG, "Parsing error:" + e.getMessage());
        }
        return videos;
    }

}
