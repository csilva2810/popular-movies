package csilva2810.udacity.com.popularmovies.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by carlinhos on 1/17/17.
 */

public class DateUtils {

    private static final String LOG_TAG = DateUtils.class.getSimpleName();

    public static final String API_DATE_FORMAT = "yyyy-MM-dd";

    public static long dateInMillis(String releaseDate) {
        String[] date = releaseDate.split("-");
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);

        Log.d(LOG_TAG, "DateInMillis: " + calendar.getTimeInMillis());

        return calendar.getTimeInMillis();
    }

    public static String getDisplayDate(Long millis) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            Date date = calendar.getTime();
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
            return df.format(date);
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());
            return "";
        }
    }

}
