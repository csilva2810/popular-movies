package csilva2810.udacity.com.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import csilva2810.udacity.com.popularmovies.database.MovieContract;
import csilva2810.udacity.com.popularmovies.fragments.ReviewsFragment;
import csilva2810.udacity.com.popularmovies.fragments.VideosFragment;
import csilva2810.udacity.com.popularmovies.listeners.OnFragmentInteractionListener;
import csilva2810.udacity.com.popularmovies.models.Movie;
import csilva2810.udacity.com.popularmovies.utils.ColorUtils;
import csilva2810.udacity.com.popularmovies.utils.DateUtils;

public class MovieDetailsActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private static final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();
    private static final String KEY_VIDEOS_FRAGMENT = "videos_fragment_key";
    private static final String KEY_REVIEWS_FRAGMENT = "reviews_fragment_key";
    private static final String KEY_MOVIE = "movie_object_key";
    public static final String ARG_MOVIEID = "arg_movieid";

    private CollapsingToolbarLayout mCollapsingToolbar;
    private ImageView mMovieCover;
    private Bitmap mBitmap;

    private Movie mMovie;
    private Toolbar mToolbar;
    private boolean mIsFavorite;
    private VideosFragment mVideosFragment;
    private ReviewsFragment mReviewsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mToolbar = (Toolbar) findViewById(R.id.movie_details_toolbar);
        setSupportActionBar(mToolbar);

        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(KEY_MOVIE);

            mVideosFragment = (VideosFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, KEY_VIDEOS_FRAGMENT);

            mReviewsFragment = (ReviewsFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, KEY_REVIEWS_FRAGMENT);
        } else {
            mMovie = getIntent().getParcelableExtra(Movie.EXTRA_MOVIE);

            Bundle arguments = new Bundle();
            arguments.putLong(ARG_MOVIEID, mMovie.getId());

            mVideosFragment = new VideosFragment();
            mVideosFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.videos_fragment, mVideosFragment)
                    .commit();

            mReviewsFragment = new ReviewsFragment();
            mReviewsFragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.revies_fragment, mReviewsFragment)
                    .commit();
        }

        mIsFavorite = isFavorite(mMovie);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                mMovieCover.setImageBitmap(bitmap);
                setToolbarColor(bitmap);
            }
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d(LOG_TAG, "Load Failed: " + errorDrawable);
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                mMovieCover.setImageDrawable(placeHolderDrawable);
                Log.d(LOG_TAG, "Prepare Load: " + placeHolderDrawable);
            }
        };

        mMovieCover = (ImageView) findViewById(R.id.movie_cover_imageview);
        mMovieCover.setTag(target);
        Picasso.with(MovieDetailsActivity.this)
                .load(mMovie.getBackdropImage())
                .placeholder(R.drawable.placeholder_image)
                .into(target);

        TextView voteAverageTextView = (TextView) findViewById(R.id.movie_vote_average);
        String voteAverage = getString(R.string.average_placeholder, String.valueOf(mMovie.getVoteAverage()));
        voteAverageTextView.setText(voteAverage);

        TextView releaseDateTextView = (TextView) findViewById(R.id.movie_release_date);
        releaseDateTextView.setText(DateUtils.getDisplayDate(mMovie.getReleaseDate()));

        TextView movieOverviewTextView = (TextView) findViewById(R.id.movie_overview);
        movieOverviewTextView.setText(mMovie.getOverview());

        mCollapsingToolbar.setTitle(mMovie.getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_as_favorite);
        toggleFabIcon(fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsFavorite) {
                    removeFromFavorites(mMovie);
                } else {
                    addToFavorites(mMovie);
                }
                mIsFavorite = !mIsFavorite;
                toggleFabIcon((FloatingActionButton) view);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mVideosFragment != null) {
            getSupportFragmentManager().putFragment(outState, KEY_VIDEOS_FRAGMENT, mVideosFragment);
        }
        if (mReviewsFragment != null) {
            getSupportFragmentManager().putFragment(outState, KEY_REVIEWS_FRAGMENT, mReviewsFragment);
        }

        outState.putParcelable(KEY_MOVIE, mMovie);
        Log.d(LOG_TAG, "Save Instance " + outState);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mMovie = savedInstanceState.getParcelable(KEY_MOVIE);
        Log.d(LOG_TAG, "Restore Instance " + savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void toggleFabIcon(FloatingActionButton fab) {
        if (mIsFavorite) {
            fab.setImageDrawable(
                    ContextCompat.getDrawable(fab.getContext(), R.drawable.ic_star_24dp)
            );
        } else {
            fab.setImageDrawable(
                    ContextCompat.getDrawable(fab.getContext(), R.drawable.ic_star_border_24dp)
            );
        }
    }

    private boolean isFavorite(Movie movie) {

        try {

            Cursor cursor = getContentResolver().query(
                    MovieContract.MovieEntry.getMovieWithIdUri(movie.getId()),
                    new String[] {MovieContract.MovieEntry._ID},
                    null,
                    null,
                    null
            );

            if (cursor == null) {
                return false;
            }

            return cursor.getCount() > 0;

        } catch (IllegalArgumentException e) {
            Log.e(LOG_TAG, e.getMessage());
            return false;
        }

    }

    private boolean addToFavorites(final Movie movie) {

        try {

            ContentValues cv = new ContentValues();
            cv.put(MovieContract.MovieEntry.COLUMN_API_ID, movie.getId());
            cv.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
            cv.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.getPosterImage());
            cv.put(MovieContract.MovieEntry.COLUMN_BACKDROP, movie.getBackdropImage());
            cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
            cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
            cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                    DateUtils.dateInMillis(movie.getReleaseDate())
            );

            getContentResolver().insert(
                    MovieContract.MovieEntry.getMovieUri(),
                    cv
            );

            Log.d(LOG_TAG, "Inserted Movie: " + cv.toString());

            return true;

        } catch (IllegalArgumentException|android.database.SQLException ex) {
            Log.d(LOG_TAG, ex.getMessage());
        }

        return false;

    }

    private boolean removeFromFavorites(final Movie movie) {

        try {
            int deletedRows = getContentResolver().delete(
                    MovieContract.MovieEntry.getMovieUri(),
                    MovieContract.MovieEntry.COLUMN_API_ID + " = ? ",
                    new String[] { String.valueOf(movie.getId()) }
            );
            if (deletedRows > -1) {
                return true;
            }
        } catch (IllegalArgumentException e) {
            Log.d(LOG_TAG, e.getMessage());
        }

        return false;

    }

    private void setToolbarColor(Bitmap bitmap) {

        Palette.PaletteAsyncListener paletteAsyncListener = new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {

            int toolbarBackgroundColor;
            int toolbarTitleColor;
            int statusBarColor;

            Palette.Swatch swatch = p.getVibrantSwatch();

            if (swatch == null)
                swatch = p.getDarkVibrantSwatch();
            if (swatch == null)
                swatch = p.getLightVibrantSwatch();

            if (swatch != null) {
                toolbarBackgroundColor = swatch.getRgb();
                toolbarTitleColor = swatch.getTitleTextColor();
                statusBarColor = ColorUtils.darken(toolbarBackgroundColor, 0.15);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    (getWindow()).setStatusBarColor(statusBarColor);
                }
                mCollapsingToolbar.setContentScrimColor(toolbarBackgroundColor);
                mCollapsingToolbar.setCollapsedTitleTextColor(toolbarTitleColor);
            }

            }
        };

        Palette.from(bitmap).generate(paletteAsyncListener);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
