package csilva2810.udacity.com.popularmovies.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import csilva2810.udacity.com.popularmovies.MovieDetailsActivity;
import csilva2810.udacity.com.popularmovies.R;
import csilva2810.udacity.com.popularmovies.adapters.MoviesAdapter;
import csilva2810.udacity.com.popularmovies.constants.App;
import csilva2810.udacity.com.popularmovies.decorators.GridSpacingItemDecoration;
import csilva2810.udacity.com.popularmovies.models.Movie;
import csilva2810.udacity.com.popularmovies.services.RequestMoviesTask;
import csilva2810.udacity.com.popularmovies.utils.AsyncTaskDelegate;
import csilva2810.udacity.com.popularmovies.utils.ConversionUtils;

public class MoviesGridFragment extends Fragment implements AsyncTaskDelegate,
        MoviesAdapter.OnMovieClickListener {

    private static final String LOG_TAG = MoviesGridFragment.class.getSimpleName();

    public static final int REQUEST_CODE_DETAILS = 1;

    private String mMoviesFilter;
    private RecyclerView mRecyclerView;
    private ProgressBar mSpinnerProgress;
    private LinearLayout mNoInternetLayout;
    private MoviesAdapter mAdapter;
    private int mMovieClickedPosition;
    private List<Movie> mMovies;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();
        mMoviesFilter = args.getString(App.SHARED_KEY_MOVIE_FILTER);

        int gridColumns = getActivity().getResources().getInteger(R.integer.movies_grid_columns);
        int gridGutter = getActivity().getResources().getInteger(R.integer.movies_grid_gutter);

        Log.d(LOG_TAG, "savedInstanceState: " + savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_movies_grid, container, false);

        mNoInternetLayout = (LinearLayout) view.findViewById(R.id.no_internet_layout);
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

        requestMovies();

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
        new RequestMoviesTask(getActivity(), MoviesGridFragment.this).execute(mMoviesFilter);
    }

    private void bindMoviesToView(List<Movie> movies) {
        mMovies = movies;
        mAdapter = new MoviesAdapter(getContext(), mMovies, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showSpinner() {
        mRecyclerView.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);
        mSpinnerProgress.setVisibility(View.VISIBLE);
    }

    private void hideSpinner() {
        mSpinnerProgress.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showMoviesGrid() {
        mNoInternetLayout.setVisibility(View.GONE);
        mSpinnerProgress.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showNoInternet() {
        mRecyclerView.setVisibility(View.GONE);
        mSpinnerProgress.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProcessFinish(Object output, String taskType) {
        hideSpinner();
        if (output != null) {
            List<Movie> movies = (List<Movie>) output;
            bindMoviesToView(movies);
            showMoviesGrid();
        }
    }

    @Override
    public void onMovieClick(Object object, int position) {
        Movie movie = (Movie) object;
        mMovieClickedPosition = position;

        Log.d(LOG_TAG, "Movie Clicked: " + position + " " + movie);

        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra(Movie.EXTRA_MOVIE, movie);
        startActivityForResult(intent, REQUEST_CODE_DETAILS);
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
