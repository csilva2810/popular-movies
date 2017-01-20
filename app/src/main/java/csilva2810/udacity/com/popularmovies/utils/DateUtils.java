package csilva2810.udacity.com.popularmovies.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public static String getDisplayDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
            return df.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

}
