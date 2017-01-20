package csilva2810.udacity.com.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by carlinhos on 1/17/17.
 */

public class MoviesProvider extends ContentProvider {

    public static final String UNKNOWN_URI = "Unknown URI: ";
    private MovieDbHelper mMovieDbHelper;

    private static final int MOVIE = 100;
    private static final int SINGLE_MOVIE = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        String authority = MovieContract.CONTENT_AUTHORITY;
        sUriMatcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        sUriMatcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", SINGLE_MOVIE);
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case SINGLE_MOVIE:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] columns, String s, String[] sArgs, String groupBy) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE:
                return mMovieDbHelper.getReadableDatabase().query(
                    MovieContract.MovieEntry.TABLE_NAME,
                    columns, s, sArgs, groupBy, null, null
                );
            case SINGLE_MOVIE:
                String selection = MovieContract.MovieEntry.COLUMN_API_ID + " = ?";
                String selectionArgs[] = { uri.getLastPathSegment() };
                return mMovieDbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        columns, selection, selectionArgs, groupBy, null, null
                );
            default:
                throw new IllegalArgumentException(UNKNOWN_URI + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE:
                long _id = mMovieDbHelper.getWritableDatabase().insert(
                        MovieContract.MovieEntry.TABLE_NAME, null, contentValues
                );
                if (_id != -1) {
                    return ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, _id);
                } else {
                    throw new android.database.SQLException("Error to insert row into " + uri);
                }
            default:
                throw new IllegalArgumentException(UNKNOWN_URI + uri);
        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case MOVIE:
                rowsDeleted = mMovieDbHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection, selectionArgs
                );
                break;
            default:
                throw new IllegalArgumentException(UNKNOWN_URI + uri);
        }

        return rowsDeleted;

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] sArgs) {

        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE:
                rowsUpdated = mMovieDbHelper.getWritableDatabase().update(
                        MovieContract.MovieEntry.TABLE_NAME,
                        contentValues, s, sArgs
                );
                break;
            default:
                throw new IllegalArgumentException(UNKNOWN_URI + uri);
        }

        return rowsUpdated;

    }

}
