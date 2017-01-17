package csilva2810.udacity.com.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import csilva2810.udacity.com.popularmovies.adapters.ReviewsAdapter;
import csilva2810.udacity.com.popularmovies.adapters.VideosAdapter;
import csilva2810.udacity.com.popularmovies.constants.MoviesApi;
import csilva2810.udacity.com.popularmovies.models.Movie;
import csilva2810.udacity.com.popularmovies.models.Review;
import csilva2810.udacity.com.popularmovies.models.Video;
import csilva2810.udacity.com.popularmovies.services.ReviewTask;
import csilva2810.udacity.com.popularmovies.services.VideoTask;
import csilva2810.udacity.com.popularmovies.utils.AsyncTaskDelegate;
import csilva2810.udacity.com.popularmovies.utils.ColorUtils;

public class MovieDetailsActivity extends AppCompatActivity implements AsyncTaskDelegate {

    private static final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();
    private CollapsingToolbarLayout mCollapsingToolbar;
    private ImageView mMovieCover;
    private Bitmap mBitmap;
    private RecyclerView mVideosRecyclerView, mReviewsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.movie_details_toolbar);

        setSupportActionBar(toolbar);

        mVideosRecyclerView = (RecyclerView) findViewById(R.id.videos_recyclerview);
        mVideosRecyclerView.setHasFixedSize(true);
        mVideosRecyclerView.setLayoutManager(
                new LinearLayoutManager(MovieDetailsActivity.this,
                        LinearLayoutManager.HORIZONTAL, false)
        );

        mReviewsRecyclerView = (RecyclerView) findViewById(R.id.reviews_recyclerview);
        mReviewsRecyclerView.setHasFixedSize(true);
        mReviewsRecyclerView.setLayoutManager(
                new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.VERTICAL,
                        false)
        );

        Intent intent = getIntent();
        if (intent.hasExtra(Movie.EXTRA_MOVIE)) {

            Movie movie = intent.getParcelableExtra(Movie.EXTRA_MOVIE);

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    mBitmap = bitmap;
                    mMovieCover.setImageBitmap(bitmap);
                    setToolbarColor(mBitmap);
                }
                @Override
                public void onBitmapFailed(Drawable errorDrawable) {}
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {}
            };

            mMovieCover = (ImageView) findViewById(R.id.movie_cover_imageview);
            TextView voteAverageTextView = (TextView) findViewById(R.id.movie_vote_average);
            TextView releaseDateTextView = (TextView) findViewById(R.id.movie_release_date);
            TextView movieOverviewTextView = (TextView) findViewById(R.id.movie_overview);

            Picasso.with(MovieDetailsActivity.this).load(movie.getBackdropImage()).into(target);

            String voteAverage = getString(R.string.average_placeholder, String.valueOf(movie.getVoteAverage()));
            voteAverageTextView.setText(voteAverage);

            releaseDateTextView.setText(getDisplayDate(movie.getReleaseDate()));
            movieOverviewTextView.setText(movie.getOverview());

            mCollapsingToolbar.setTitle(movie.getTitle());

            new VideoTask(MovieDetailsActivity.this).execute(String.valueOf(movie.getId()));
            new ReviewTask(MovieDetailsActivity.this).execute(String.valueOf(movie.getId()));

        }

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

    private String getDisplayDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
            return df.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void onProcessFinish(Object output, String taskType) {
        switch (taskType) {
            case MoviesApi.VIDEOS_PATH:
                List<Video> videos = (List<Video>) output;
                mVideosRecyclerView.setAdapter(new VideosAdapter(MovieDetailsActivity.this, videos));
                break;
            case MoviesApi.REVIEWS_PATH:
                List<Review> reviews = (List<Review>) output;
                mReviewsRecyclerView.setAdapter(new ReviewsAdapter(MovieDetailsActivity.this, reviews));
                break;
        }
    }

}
