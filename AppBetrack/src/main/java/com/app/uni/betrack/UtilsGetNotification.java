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

    private static int[][] Notification_Table_Lookup = new int[][] {
            {0,0,2,0}, // "09:30:00" -> "13:30:00" //Morning
            {1,0,0,0}, // "17:30:00" -> "09:30:00" //Evening/Night
            {2,0,1,0}, // "13:30:00" -> "17:30:00" //Afternoon
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

    public static String next10minutes(long currentTime) {
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

        //Compute the time range when we should trigger a notification
        long timeRange = 9 * 60 * 1000;

        //Pickup a random number between 0 and timeRange
        Random r = new Random();
        long timeOffset = nextLong(r, timeRange);

        Calendar calStart = Calendar.getInstance();
        calStart.setTimeInMillis(currentTime);

        //Add this time to calStart
        calStart.setTimeInMillis(calStart.getTimeInMillis() + timeOffset + 60000); //We never trigger the notification during the first minute

        //Return the time for the next notification
        Date timeNextNotification = calStart.getTime();

        Log.d(TAG, "Next notification " + shf.format(timeNextNotification));

        return shf.format(timeNextNotification);
    }

    public static boolean CheckTimeWindow(long time, Context context) {

        boolean rc = false;

        SettingsStudy ObjSettingsStudy = SettingsStudy.getInstance(context);

        //Check if the time is on the day of the notification
        //Prepare a new calendar for the time we wan to check
        Calendar calTimeToCheck = Calendar.getInstance();
        calTimeToCheck.setTimeInMillis(time);

        Calendar calTimeOfNotification = Calendar.getInstance();
        calTimeOfNotification.setTimeInMillis(ObjSettingsStudy.getTimeNextNotification());
        if (calTimeToCheck.get(Calendar.DAY_OF_WEEK) == calTimeOfNotification.get(Calendar.DAY_OF_WEEK)) {
            //Check if the time is within the window of the notification
            if (returnIndex(time) == ObjSettingsStudy.getNbrOfNotificationToDo() % UtilsGetNotification.getNbrPerDay())
            {
                rc = true;
            }
        }

        return rc;
    }

    public static boolean CheckInBtwNotifications(long time) {
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");
        Date timeStart = null;
        Date timeStop = null;
        boolean timeInBtwNotification = true;

        int counter = 0;

        while ( (counter <= UtilsGetNotification.ListSurveys[0].length) && (timeInBtwNotification == true)) {

            try {
                //Parse the time
                timeStart = shf.parse(Notification_Table[counter][0]);
                timeStop = shf.parse(Notification_Table[counter][1]);
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

            //Check if the input time is in between this range
            if ((time >= calStart.getTimeInMillis()) && (time <= calStop.getTimeInMillis())) {
                timeInBtwNotification = false;
            }

            counter++;
        }

        Log.d(TAG, "timeInBtwNotification: " + timeInBtwNotification);

        return timeInBtwNotification;
    }

    public static int returnIndex(long time) {
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");
        Date timeStart = null;
        Date timeStop = null;
        boolean timeFound = false;

        int counter = 0;
        int i = Notification_Table_Lookup[0][0];
        while ( (counter < Notification_Table.length) && (timeFound == false)) {

            try {
                //Parse the time
                timeStart = shf.parse(Notification_Table[Notification_Table_Lookup[counter][0]][Notification_Table_Lookup[counter][1]]);
                timeStop = shf.parse(Notification_Table[Notification_Table_Lookup[counter][2]][Notification_Table_Lookup[counter][3]]);
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

            if (calStart.getTimeInMillis() > calStop.getTimeInMillis()) {
                if (time < calStart.getTimeInMillis()) {
                    calStart.add(Calendar.DATE, -1);
                } else {
                    calStop.add(Calendar.DATE, 1);
                }
            }

            //Check if the input time is in between this range
            if ((time >= calStart.getTimeInMillis()) && (time <= calStop.getTimeInMillis())) {
                timeFound = true;
            } else {
                counter++;
            }
        }

        if (!timeFound) {counter = -1;}

        Log.d(TAG, "timeFound: " + timeFound);

        return counter;
    }

    public static int adjustTimeWindow(int TimeWindowWanted, int TimeWindowSaved) {
        int AdjustTimeWindow = 0;

        //0 Morning
        //1 Evening
        //2 Afternoon

        //We are in the morning (TimeWindowWanted = 0) but we should be in the afternoon (TimeWindowSaved = 2) we adjust the window location
        if ((TimeWindowWanted == 0) && (TimeWindowSaved == 2)) {
            AdjustTimeWindow = 2;
        }
        //We are in the evening (TimeWindowWanted = 1) but we should be in the morning (TimeWindowSaved = 0) we adjust the window location
        else if ((TimeWindowWanted == 1) && (TimeWindowSaved == 0)) {
            AdjustTimeWindow = 2;
        }
        //We are in the afternoon (TimeWindowWanted = 2) but we should be in the evening (TimeWindowSaved = 1) we adjust the window location
        else if ((TimeWindowWanted == 2) && (TimeWindowSaved == 1)) {
            AdjustTimeWindow = 2;
        }
        //We are in the morning (TimeWindowWanted = 0) but we should be in the evening (TimeWindowSaved = 1) we adjust the window location
        else if ((TimeWindowWanted == 0) && (TimeWindowSaved == 1)) {
            AdjustTimeWindow = 1;
        }
        //We are in the evening (TimeWindowWanted = 1) but we should be in the afternoon (TimeWindowSaved = 2) we adjust the window location
        else if ((TimeWindowWanted == 1) && (TimeWindowSaved == 2)) {
            AdjustTimeWindow = 1;
        }
        //We are in the afternoon (TimeWindowWanted = 2) but we should be in the morning (TimeWindowSaved = 0) we adjust the window location
        else if ((TimeWindowWanted == 2) && (TimeWindowSaved == 0)) {
            AdjustTimeWindow = 1;
        }
        return AdjustTimeWindow;
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
