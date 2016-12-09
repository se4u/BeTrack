package com.app.uni.betrack;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by cevincent on 11/11/2016.
 */

public class UtilsTimeManager {

    //Return the number remaining day(s) of the study + 1 day (Start day of the study)
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

        TimeRemaining += 1;

        return TimeRemaining;
    }
}
