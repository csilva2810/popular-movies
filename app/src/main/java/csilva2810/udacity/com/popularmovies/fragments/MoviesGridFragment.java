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
import android.widget.ProgressBar;

import java.util.List;

import csilva2810.udacity.com.popularmovies.MainActivity;
import csilva2810.udacity.com.popularmovies.adapters.MoviesAdapter;
import csilva2810.udacity.com.popularmovies.constants.App;
import csilva2810.udacity.com.popularmovies.constants.MoviesApi;
import csilva2810.udacity.com.popularmovies.decorators.GridSpacingItemDecoration;
import csilva2810.udacity.com.popularmovies.services.RequestMoviesTask;
import csilva2810.udacity.com.popularmovies.utils.AsyncTaskDelegate;
import csilva2810.udacity.com.popularmovies.models.Movie;
import csilva2810.udacity.com.popularmovies.R;
import csilva2810.udacity.com.popularmovies.utils.ConversionUtils;

public class MoviesGridFragment extends Fragment implements AsyncTaskDelegate {

    private static final String LOG_TAG = MoviesGridFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private ProgressBar mSpinnerProgress;
    private MoviesAdapter mMoviesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        int gridColumns = 2;
        View view = inflater.inflate(R.layout.fragment_movies_grid, container, false);

        mSpinnerProgress = (ProgressBar) view.findViewById(R.id.spinner_progress);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.movies_grid_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        mGridLayoutManager = new GridLayoutManager(getActivity(), gridColumns);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.addItemDecoration(
                new GridSpacingItemDecoration(2, ConversionUtils.dpToPx(getActivity(), 5), true)
        );

        requestMovies();

        return view;

    }

    private void showSpinner() {
        mRecyclerView.setVisibility(View.GONE);
        mSpinnerProgress.setVisibility(View.VISIBLE);
    }

    private void hideSpinner() {
        mSpinnerProgress.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void requestMovies() {

        Context context = getActivity();

        SharedPreferences sp = App.getSharedPreferences(context);
        String movieFilter = sp.getString(App.SHARED_KEY_MOVIE_FILTER, MoviesApi.MOVIE_POPULAR);

        if (!App.isOnline(context)) {
            if (!movieFilter.equals(MainActivity.MOVIE_FAVORITES)) {
                ((MainActivity) context).showNoInternet();
                return;
            }
        }

        showSpinner();
        new RequestMoviesTask(context, MoviesGridFragment.this).execute(movieFilter);

    }

    public void bindMoviesToView(List<Movie> movies) {
        Log.d(LOG_TAG, movies.toString());
        mMoviesAdapter = new MoviesAdapter(getActivity(), movies);
        mRecyclerView.setAdapter(mMoviesAdapter);
        mMoviesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProcessFinish(Object output, String taskType) {
        hideSpinner();
        if (output != null) {
            List<Movie> movies = (List<Movie>) output;
            bindMoviesToView(movies);
            ((MainActivity) getActivity()).showMoviesGrid();
        } else {
            App.networkErrorMessage(getActivity());
        }
    }

}
