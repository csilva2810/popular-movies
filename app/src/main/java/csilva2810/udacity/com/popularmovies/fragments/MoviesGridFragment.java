package csilva2810.udacity.com.popularmovies.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.List;

import csilva2810.udacity.com.popularmovies.R;
import csilva2810.udacity.com.popularmovies.adapters.MoviesAdapter;
import csilva2810.udacity.com.popularmovies.constants.App;
import csilva2810.udacity.com.popularmovies.decorators.GridSpacingItemDecoration;
import csilva2810.udacity.com.popularmovies.models.Movie;
import csilva2810.udacity.com.popularmovies.services.RequestMoviesTask;
import csilva2810.udacity.com.popularmovies.utils.AsyncTaskDelegate;
import csilva2810.udacity.com.popularmovies.utils.ConversionUtils;

public class MoviesGridFragment extends Fragment implements AsyncTaskDelegate {

    private static final String LOG_TAG = MoviesGridFragment.class.getSimpleName();

    private String mMoviesFilter;
    private RecyclerView mRecyclerView;
    private ProgressBar mSpinnerProgress;
    private LinearLayout mNoInternetLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();
        mMoviesFilter = args.getString(App.SHARED_KEY_MOVIE_FILTER);

        int gridColumns = 2;
        int gridSpacingSize = 5;

        Log.d(LOG_TAG, "savedInstanceState: " + savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_movies_grid, container, false);

        mNoInternetLayout = (LinearLayout) view.findViewById(R.id.no_internet_layout);
        mSpinnerProgress = (ProgressBar) view.findViewById(R.id.spinner_progress);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.movies_grid_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumns));
        mRecyclerView.addItemDecoration(
                new GridSpacingItemDecoration(gridColumns, ConversionUtils.dpToPx(getActivity(), gridSpacingSize), true)
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

        new RequestMoviesTask(getActivity(), MoviesGridFragment.this).execute(mMoviesFilter);

    }

    private void bindMoviesToView(List<Movie> movies) {
        mRecyclerView.setAdapter(new MoviesAdapter(getActivity(), movies));
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
    public void onProcessPrepare() {
        if (!mMoviesFilter.equals(Movie.MOVIE_FAVORITES)) {
            if (!App.isOnline(getActivity())) {
                showNoInternet();
                return;
            }
        }
        showSpinner();
    }

    @Override
    public void onProcessFinish(Object output, String taskType) {
        hideSpinner();
        if (output != null) {
            List<Movie> movies = (List<Movie>) output;
            bindMoviesToView(movies);
            showMoviesGrid();
        } else {
            App.networkErrorMessage(getActivity());
        }
    }

}
