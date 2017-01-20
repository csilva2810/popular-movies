package csilva2810.udacity.com.popularmovies.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by carlinhos on 1/17/17.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "csilva2810.udacity.com.popularmovies";
    public static final String PATH_MOVIE = "movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class MovieEntry implements BaseColumns {

        static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" +
                PATH_MOVIE;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" +
                PATH_MOVIE;

        public static Uri getMovieUri() {
            return CONTENT_URI;
        }

        public static Uri getMovieWithIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /**
         * Colunas para tabela de filmes
         * api_id, title, poster, synopsis, user_rating, release_date
         */

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_API_ID = "api_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_BACKDROP = "backdrop";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "user_rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";

    }

}
