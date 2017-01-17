package csilva2810.udacity.com.popularmovies.utils;

import java.util.Calendar;

/**
 * Created by carlinhos on 1/17/17.
 */

public class DateUtils {

    public static long dateInMillis(String releaseDate) {
        String[] date = releaseDate.split("-");
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTimeInMillis();
    }

}
