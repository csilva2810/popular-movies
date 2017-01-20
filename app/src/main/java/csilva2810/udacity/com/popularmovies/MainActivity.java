package csilva2810.udacity.com.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import csilva2810.udacity.com.popularmovies.constants.App;
import csilva2810.udacity.com.popularmovies.constants.MoviesApi;
import csilva2810.udacity.com.popularmovies.fragments.MoviesGridFragment;
import csilva2810.udacity.com.popularmovies.fragments.NoInternetFragment;

public class MainActivity extends AppCompatActivity implements
    NoInternetFragment.OnTryAgainClickListener {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String MOVIE_FRAGMENT_KEY = "movie_fragment";
    public static final String NO_INTERNET_FRAGMENT_KEY = "no_internet_fragment";
    public static final String ACTIVE_FRAGMENT_KEY = "active_fragment";
    public static final String MOVIE_FAVORITES = "favorites";

    private MoviesGridFragment mMoviesGridFragment;
    private NoInternetFragment mNoInternetFragment;
    private String mActiveFragment;
    private Toolbar mToolbar;

    private NoInternetFragment.OnTryAgainClickListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.PACKAGE_NAME = getApplicationContext().getPackageName();

        mToolbar = (Toolbar) findViewById(R.id.default_toolbar);
        setSupportActionBar(mToolbar);

        if (!App.isOnline(MainActivity.this)) {
            addFragment(NO_INTERNET_FRAGMENT_KEY);
            return;
        }

        if (savedInstanceState != null) {
            mActiveFragment = savedInstanceState.getString(ACTIVE_FRAGMENT_KEY);
            switch (mActiveFragment) {
                case MOVIE_FRAGMENT_KEY:
                    mMoviesGridFragment = (MoviesGridFragment)
                            getSupportFragmentManager()
                                    .getFragment(savedInstanceState, MOVIE_FRAGMENT_KEY);
                    break;
                case NO_INTERNET_FRAGMENT_KEY:
                    mNoInternetFragment = (NoInternetFragment)
                            getSupportFragmentManager()
                            .getFragment(savedInstanceState, NO_INTERNET_FRAGMENT_KEY);
                    break;
            }
            return;
        }

        addFragment(MOVIE_FRAGMENT_KEY);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        switch (mActiveFragment) {
            case MOVIE_FRAGMENT_KEY:
                if (mMoviesGridFragment != null) {
                    getSupportFragmentManager()
                            .putFragment(outState, MOVIE_FRAGMENT_KEY, mMoviesGridFragment);
                }
                break;
            case NO_INTERNET_FRAGMENT_KEY:
                if (mNoInternetFragment != null) {
                    getSupportFragmentManager()
                            .putFragment(outState, NO_INTERNET_FRAGMENT_KEY, mNoInternetFragment);
                }
                break;
        }
        outState.putString(ACTIVE_FRAGMENT_KEY, mActiveFragment);
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

    @Nullable
    private Fragment getFragmentByKey(String fragmentKey) {
        switch (fragmentKey) {
            case MOVIE_FRAGMENT_KEY:
                if (mMoviesGridFragment == null) {
                    mMoviesGridFragment = new MoviesGridFragment();
                }
                return mMoviesGridFragment;
            case NO_INTERNET_FRAGMENT_KEY:
                if (mNoInternetFragment == null) {
                    mNoInternetFragment = new NoInternetFragment();
                }
                return mNoInternetFragment;
        }
        return null;
    }

    public void addFragment(String fragmentKey) {

        mActiveFragment = fragmentKey;
        Fragment fragment = getFragmentByKey(fragmentKey);

        if (getSupportFragmentManager().findFragmentById(R.id.main_fragment_container) != null) {
            replaceFragment(fragment);
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_fragment_container, fragment)
                .commit();

    }

    private void replaceFragment(Fragment f) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, f)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void onTryAgainClickListener() {
        if (!App.isOnline(MainActivity.this)) {
            return;
        }
        addFragment(MOVIE_FRAGMENT_KEY);
    }
}
