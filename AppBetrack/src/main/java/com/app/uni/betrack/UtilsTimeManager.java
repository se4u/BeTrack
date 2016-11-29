package com.app.uni.betrack;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by cevincent on 11/11/2016.
 */

public class UtilsTimeManager {

    public static int ComputeTimeRemaing(Context context) {
        SettingsStudy ObjSettingsStudy = null;
        int TimeRemaining = 0;
        //Read the setting of the study
        ObjSettingsStudy = SettingsStudy.getInstance(context);

        //Compute the time remaining of the study
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        int StudyDuration = ObjSettingsStudy.getStudyDuration();

        try {
            //Parse the date from preference
            sdf.parse(ObjSettingsStudy.getStartDateSurvey());
        }catch (Exception e) {
            e.printStackTrace();
        }

        Calendar calendarStartDate = sdf.getCalendar();

        long millisStartDate = calendarStartDate.getTimeInMillis();
        long millisActualDate = System.currentTimeMillis();

        TimeRemaining =  (StudyDuration  - (int)((millisActualDate - millisStartDate) / (24*60*60*1000)));
        TimeRemaining += 1; // Add one day for the set up of the study

        return TimeRemaining;
    }

    public static long TimeToNotification(Context context) {


        SettingsBetrack ObjSettingsBetrack = null;
        //Read the preferences
        ObjSettingsBetrack = SettingsBetrack.getInstance();
        ObjSettingsBetrack.Update(context);

        SimpleDateFormat shfNotification = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);

        try {
            shfNotification.parse(ObjSettingsBetrack.GetStudyNotificationTime());
        }catch (Exception e) {
            e.printStackTrace();
        }

        Calendar calendarNotification = shfNotification.getCalendar();
        long millisNotification = (int)calendarNotification.getTimeInMillis() + 1000 * 60 * 60;
        long millisActualDate = System.currentTimeMillis() - cal.getTimeInMillis();
        if (millisActualDate < 0) millisActualDate = 0;

        if ((millisNotification - millisActualDate) > 0) {
            return (millisNotification - millisActualDate);
        } else {
            return 0;
        }

    }
}
