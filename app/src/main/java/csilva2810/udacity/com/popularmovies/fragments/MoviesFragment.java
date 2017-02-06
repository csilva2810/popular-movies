package csilva2810.udacity.com.popularmovies.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;

import csilva2810.udacity.com.popularmovies.MovieDetailsActivity;
import csilva2810.udacity.com.popularmovies.R;
import csilva2810.udacity.com.popularmovies.adapters.MoviesAdapter;
import csilva2810.udacity.com.popularmovies.constants.App;
import csilva2810.udacity.com.popularmovies.decorators.GridSpacingItemDecoration;
import csilva2810.udacity.com.popularmovies.models.Movie;
import csilva2810.udacity.com.popularmovies.services.RequestMoviesTask;
import csilva2810.udacity.com.popularmovies.utils.AsyncTaskDelegate;
import csilva2810.udacity.com.popularmovies.utils.ConversionUtils;

public class MoviesFragment extends Fragment implements AsyncTaskDelegate,
        MoviesAdapter.OnMovieClickListener {

    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();

    private static final String KEY_CLICKED_POSITION = "KEY_CLICKED_POSITION";
    private static final String KEY_MOVIES = "KEY_MOVIES";
    private static final String KEY_MOVIES_FILTER = "KEY_MOVIES_FILTER";

    public static final int REQUEST_CODE_DETAILS = 1;

    private RecyclerView mRecyclerView;
    private ProgressBar mSpinnerProgress;
    private LinearLayout mNoInternetLayout, mNoFavoritesLayout;
    private MoviesAdapter mAdapter;
    private RequestMoviesTask mTask;

    private String mMoviesFilter;
    private int mMovieClickedPosition;
    private ArrayList<Movie> mMovies;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_CLICKED_POSITION, mMovieClickedPosition);
        outState.putString(KEY_MOVIES_FILTER, mMoviesFilter);
        outState.putParcelableArrayList(KEY_MOVIES, mMovies);
        if (mTask != null) {
            mTask.cancel(true);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            Bundle args = getArguments();
            mMoviesFilter = args.getString(App.SHARED_KEY_MOVIE_FILTER);
        } else {
            mMovieClickedPosition = savedInstanceState.getInt(KEY_CLICKED_POSITION);
            mMoviesFilter = savedInstanceState.getString(KEY_MOVIES_FILTER);
            mMovies = savedInstanceState.getParcelableArrayList(KEY_MOVIES);
        }

        int gridColumns = getActivity().getResources().getInteger(R.integer.movies_grid_columns);
        int gridGutter = getActivity().getResources().getInteger(R.integer.movies_grid_gutter);

        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        mNoInternetLayout = (LinearLayout) view.findViewById(R.id.no_internet_layout);
        mNoFavoritesLayout = (LinearLayout) view.findViewById(R.id.no_favorites_layout);

        mSpinnerProgress = (ProgressBar) view.findViewById(R.id.spinner_progress);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.movies_grid_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumns));
        mRecyclerView.addItemDecoration(
                new GridSpacingItemDecoration(gridColumns, ConversionUtils.dpToPx(getActivity(), gridGutter), true)
        );

        Button tryAgainButton = (Button) mNoInternetLayout.findViewById(R.id.button_try_again);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMovies();
            }
        });


        if (!mMoviesFilter.equals(Movie.MOVIE_FAVORITES)) {
            if (mMovies == null) {
                requestMovies();
            } else {
                bindMoviesToView(mMovies);
            }
        } else {
            requestMovies();
        }

        return view;

    }

    private void requestMovies() {
        if (!mMoviesFilter.equals(Movie.MOVIE_FAVORITES)) {
            if (!App.isOnline(getActivity())) {
                showNoInternet();
                return;
            }
        }

        showSpinner();
        mTask = new RequestMoviesTask(getActivity(), MoviesFragment.this);
        mTask.execute(mMoviesFilter);
    }

    private void bindMoviesToView(ArrayList<Movie> movies) {
        mMovies = movies;

        if (mMoviesFilter.equals(Movie.MOVIE_FAVORITES) && mMovies.size() == 0) {
            showNoFavorites();
            return;
        }

        mAdapter = new MoviesAdapter(getContext(), mMovies, this);
        mRecyclerView.setAdapter(mAdapter);
        showMoviesGrid();
    }

    private void showSpinner() {
        mRecyclerView.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);
        mNoFavoritesLayout.setVisibility(View.GONE);
        mSpinnerProgress.setVisibility(View.VISIBLE);
    }

    private void hideSpinner() {
        mSpinnerProgress.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);
        mNoFavoritesLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showMoviesGrid() {
        mNoInternetLayout.setVisibility(View.GONE);
        mNoFavoritesLayout.setVisibility(View.GONE);
        mSpinnerProgress.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showNoInternet() {
        mRecyclerView.setVisibility(View.GONE);
        mSpinnerProgress.setVisibility(View.GONE);
        mNoFavoritesLayout.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.VISIBLE);
    }

    private void showNoFavorites() {
        mRecyclerView.setVisibility(View.GONE);
        mSpinnerProgress.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);
        mNoFavoritesLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProcessFinish(Object output, String taskType) {
        hideSpinner();
        bindMoviesToView((ArrayList<Movie>) output);
    }

    @Override
    public void onMovieClick(Object object, int position, View coverImageView) {
        Movie movie = (Movie) object;
        mMovieClickedPosition = position;

        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra(Movie.EXTRA_MOVIE, movie);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String transition = getActivity().getString(R.string.transition_movie_cover);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), coverImageView, transition);

            startActivityForResult(intent, REQUEST_CODE_DETAILS, options.toBundle());
        } else {
            startActivityForResult(intent, REQUEST_CODE_DETAILS);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_DETAILS) {
            switch (resultCode) {
                case Activity.RESULT_OK:

                    int operation = data.getIntExtra(Movie.EXTRA_MOVIE_OPERATION, 0);
                    Movie movie = data.getParcelableExtra(Movie.EXTRA_MOVIE);

                    switch (operation) {
                        case Movie.FLAG_ADDED:
                            mMovies.set(mMovieClickedPosition, movie);
                            break;
                        case Movie.FLAG_REMOVED:
                            if (mMoviesFilter.equals(Movie.MOVIE_FAVORITES)) {
                                mMovies.remove(mMovieClickedPosition);
                            } else {
                                mMovies.set(mMovieClickedPosition, movie);
                            }
                            break;
                    }

                    mAdapter.notifyItemChanged(mMovieClickedPosition);

                    break;
            }
        }
    }
    
}
