package com.app.uni.betrack;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * Created by cvincent on 25/08/17.
 */

public class UtilsGetNotification {
    static final String TAG = "UtilsGetNotification";

    public static Class<?>[][]  ListSurveys = new Class<?>[][] {
            //"09:30:00","13:00:00"
            {ActivitySurveySleep.class, ActivitySurveyDaily.class}, //First time period: 3 mod 3 =0
            //"17:30:00","21:00:00"
            {null, ActivitySurveyDaily.class}, //Third time period: 1 mod 3 = 1
            //"13:30:00","17:00:00"
            {null, ActivitySurveyDaily.class},  //Second time priod! 2 mod 3 = 2
    };

    //The minimum time difference btw 2 notifications shall be 30 minutes!!!
    //If not it will not work due to some limitations of Android
    private static String[][] Notification_Table = new String[][] {
            {"09:30:00","13:00:00"}, //First time period: 3 mod 3 =0
            {"17:30:00","21:00:00"}, //Third time period: 1 mod 3 = 1
            {"13:30:00","17:00:00"}, //Second time priod! 2 mod 3 = 2
};

    public static String next(int index) {
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");
        Date timeStart = null;
        Date timeStop = null;

        try {
            //Parse the time
            timeStart = shf.parse(Notification_Table[index][0]);
            timeStop = shf.parse(Notification_Table[index][1]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Prepare a new calendar for the start time
        Calendar calendarpref = new GregorianCalendar();
        calendarpref.setTime(timeStart);

        Calendar calStart = Calendar.getInstance();
        calStart.setTimeInMillis(System.currentTimeMillis());
        calStart.set(Calendar.SECOND, calendarpref.get(calendarpref.SECOND));
        calStart.set(Calendar.HOUR_OF_DAY, calendarpref.get(calendarpref.HOUR_OF_DAY));
        calStart.set(Calendar.MINUTE, calendarpref.get(calendarpref.MINUTE));

        //Prepare a new calendar for the stop time
        calendarpref.setTime(timeStop);

        Calendar calStop = Calendar.getInstance();
        calStop.setTimeInMillis(System.currentTimeMillis());
        calStop.set(Calendar.SECOND, calendarpref.get(calendarpref.SECOND));
        calStop.set(Calendar.HOUR_OF_DAY, calendarpref.get(calendarpref.HOUR_OF_DAY));
        calStop.set(Calendar.MINUTE, calendarpref.get(calendarpref.MINUTE));

        //Compute the time range when we should trigger a notification
        long timeRange = calStop.getTimeInMillis() - calStart.getTimeInMillis();

        //Pickup a random number between 0 and timeRange
        Random r = new Random();
        long timeOffset = nextLong(r, timeRange);

        //Add this time to calStart
        calStart.setTimeInMillis(calStart.getTimeInMillis() + timeOffset);

        //Return the time for the next notification
        Date timeNextNotification = calStart.getTime();

        Log.d(TAG, "Next notification " + shf.format(timeNextNotification));

        return shf.format(timeNextNotification);
    }

    public static int getNbrPerDay() {
        return Notification_Table.length;
    }

    public static int getNbrSurveysPerDay() {
        return UtilsGetNotification.ListSurveys[0].length - 1;
    }

    private static long nextLong(Random rng, long n) {
        long bits, val;
        do {
            bits = (rng.nextLong() << 1) >>> 1;
            val = bits % n;
        } while (bits-val+(n-1) < 0L);
        return val;
    }
}
