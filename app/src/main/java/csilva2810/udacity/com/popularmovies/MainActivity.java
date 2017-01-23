package csilva2810.udacity.com.popularmovies;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import csilva2810.udacity.com.popularmovies.constants.App;
import csilva2810.udacity.com.popularmovies.fragments.MoviesGridFragment;
import csilva2810.udacity.com.popularmovies.models.Movie;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String MOVIE_FRAGMENT_KEY = "movie_fragment";

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());

        String[] fragmentTypes = new String[] {
                Movie.MOVIE_POPULAR,
                Movie.MOVIE_TOP_RATED,
                Movie.MOVIE_FAVORITES
        };
        String[] fragmentTitles = new String[] {
                getString(R.string.action_filter_popular),
                getString(R.string.action_filter_top_rated),
                getString(R.string.action_filter_favorites)
        };

        for (int i = 0; i < fragmentTypes.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putString(App.SHARED_KEY_MOVIE_FILTER, fragmentTypes[i]);

            MoviesGridFragment fragment = new MoviesGridFragment();
            fragment.setArguments(bundle);

            Log.d(LOG_TAG, "Fragment " + fragment);
            Log.d(LOG_TAG, "Bundle " + bundle);
            adapter.addFragment(fragment, fragmentTitles[i]);
        }

        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
