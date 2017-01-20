package csilva2810.udacity.com.popularmovies;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import csilva2810.udacity.com.popularmovies.constants.App;
import csilva2810.udacity.com.popularmovies.constants.MoviesApi;
import csilva2810.udacity.com.popularmovies.fragments.MoviesGridFragment;
import csilva2810.udacity.com.popularmovies.fragments.NoInternetFragment;
import csilva2810.udacity.com.popularmovies.listeners.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements
        OnFragmentInteractionListener {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String MOVIE_FAVORITES = "favorites";
    public static final String MOVIE_FRAGMENT_KEY = "movie_fragment";

    private MoviesGridFragment mMoviesGridFragment;
    private Toolbar mToolbar;
    private LinearLayout mNoInternetLayout;
    private LinearLayout mMoviesGridLayout;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.PACKAGE_NAME = getApplicationContext().getPackageName();

        mFragmentManager = getSupportFragmentManager();
        mMoviesGridFragment = new MoviesGridFragment();

        mToolbar = (Toolbar) findViewById(R.id.default_toolbar);
        setSupportActionBar(mToolbar);

        mMoviesGridLayout = (LinearLayout) findViewById(R.id.main_fragment_container);
        mNoInternetLayout = (LinearLayout) findViewById(R.id.no_internet_fragment);


        if (!App.isOnline(MainActivity.this)) {
            showNoInternet();
        } else {
            showMoviesGrid();
        }

        if (savedInstanceState != null) {
            mMoviesGridFragment =
                    (MoviesGridFragment) mFragmentManager
                            .getFragment(savedInstanceState, MOVIE_FRAGMENT_KEY);

            return;
        }

        mFragmentManager
                .beginTransaction()
                .add(R.id.main_fragment_container, mMoviesGridFragment)
                .commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if (mMoviesGridFragment != null) {
            mFragmentManager.putFragment(outState, MOVIE_FRAGMENT_KEY, mMoviesGridFragment);
        }

        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_filter_popular:
                setMovieFilter(MoviesApi.MOVIE_POPULAR);
                break;
            case R.id.action_filter_top_rated:
                setMovieFilter(MoviesApi.MOVIE_TOP_RATED);
                break;
            case R.id.action_filter_favorites:
                setMovieFilter(MOVIE_FAVORITES);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setMovieFilter(String movieFilter) {

        SharedPreferences sp = App.getSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(App.SHARED_KEY_MOVIE_FILTER, movieFilter);
        editor.apply();

        if (mMoviesGridFragment != null) {
            mMoviesGridFragment.requestMovies();
        }

    }

    public void showMoviesGrid() {
        mMoviesGridLayout.setVisibility(View.VISIBLE);
        mNoInternetLayout.setVisibility(View.GONE);
    }

    public void showNoInternet() {
        mMoviesGridLayout.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        mMoviesGridFragment.requestMovies();
    }
}
