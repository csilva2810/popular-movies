package csilva2810.udacity.com.popularmovies.constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import csilva2810.udacity.com.popularmovies.MainActivity;
import csilva2810.udacity.com.popularmovies.R;
import csilva2810.udacity.com.popularmovies.fragments.NoInternetFragment;

/**
 * Created by carlinhos on 12/11/16.
 */

public abstract class App {

    public static final String LOG_TAG = App.class.getSimpleName();
    public static String PACKAGE_NAME;

    public static final String SHARED_PREFERENCES = PACKAGE_NAME;
    public static final String SHARED_KEY_MOVIE_FILTER = PACKAGE_NAME + ".MOVIE_FILTER";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isOnline = networkInfo != null && networkInfo.isConnectedOrConnecting();
        Log.d(LOG_TAG, "Is OnLine: " + String.valueOf(isOnline));

        return isOnline;
    }

    public static void networkErrorMessage(Context context) {
        Toast.makeText(
                context,
                context.getString(R.string.connection_error),
                Toast.LENGTH_SHORT
        ).show();
    }

}
