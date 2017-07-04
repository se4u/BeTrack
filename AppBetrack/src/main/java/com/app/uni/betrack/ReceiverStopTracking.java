package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.app.uni.betrack.IntentServiceTrackApp.ActivityStopDate;
import static com.app.uni.betrack.IntentServiceTrackApp.ActivityStopTime;

/**
 * Created by cvincent on 30.06.17.
 */

public class ReceiverStopTracking  extends WakefulBroadcastReceiver {
    static final String TAG = "ReceiverStopTracking";
    private static UtilsLocalDataBase localdatabase =  null;
    private SettingsStudy ObjSettingsStudy;

    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    @Override
    public void onReceive(Context context, Intent intent) { //

        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

        Log.d(TAG, "onReceived");
        ObjSettingsStudy = SettingsStudy.getInstance(context);

        if (null == localdatabase) {
            localdatabase =  new UtilsLocalDataBase(context);
        }

        Log.d(TAG, "Phone is turning off we save the data to the local database");

        try {
            CreateTrackApp.SemTrackApp.acquire();
        } catch (Exception e) {}
        //We stop any new monitoring
        CreateTrackApp.StopAlarm(context);


        //Save when the screen was switched off
        values.clear();

        //Save the date
        ActivityStopDate = sdf.format(new Date());
        //Save the time
        ActivityStopTime = shf.format(new Date());

        values.put(UtilsLocalDataBase.C_PHONE_USAGE_STATE, 0);
        values.put(UtilsLocalDataBase.C_PHONE_USAGE_DATE, ActivityStopDate);
        values.put(UtilsLocalDataBase.C_PHONE_USAGE_TIME, ActivityStopTime);
        try {
            AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_PHONE_USAGE);
            Log.d(TAG, "Screen Off saved in database " + ActivityStopDate + " " + ActivityStopTime);
        } catch (Exception f)
        {
            Log.d(TAG, "Nothing to update in the database");
        }

        //Save in the database if needed when we stop monitoring an application
        values.clear();
        values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_APPWATCH, false);
        try {
            ActivityStopDate = values.get(UtilsLocalDataBase.C_APPWATCH_DATESTOP).toString();
            Log.d(TAG, "End monitoring date: nothing to save");
        } catch (Exception e) {
            if (null != values) {
                //Save the stop date
                ActivityStopDate = sdf.format(new Date());
                //Save the stop time
                ActivityStopTime = shf.format(new Date());

                try {
                    Log.d(TAG, "RecStopSem1 try acquire");
                    SettingsStudy.SemAppWatchMonitor.acquire();
                    Log.d(TAG, "RecStopSem1 acquired");
                    //SettingsStudy.AppWatchId = ReturnAppWatchId();

                    if (SettingsStudy.getAppWatchId() != -1) {
                        int TimeWatched = (int) ((System.currentTimeMillis() - SettingsStudy.getAppWatchStartTime()) / 1000);
                        ObjSettingsStudy.setAppTimeWatched(SettingsStudy.getAppWatchId(), ObjSettingsStudy.getApplicationsToWatch().size(), TimeWatched);
                        SettingsStudy.setAppWatchId(-1);
                    }
                } catch (Exception eWatchId) {
                }
                finally {
                    SettingsStudy.SemAppWatchMonitor.release();
                    Log.d(TAG, "RecStopSem1 try released");
                }

                values.put(UtilsLocalDataBase.C_APPWATCH_DATESTOP, ActivityStopDate);
                values.put(UtilsLocalDataBase.C_APPWATCH_TIMESTOP, ActivityStopTime);
                try {
                    this.AccesLocalDB().Update(values, values.getAsLong(UtilsLocalDataBase.C_APPWATCH_ID), UtilsLocalDataBase.TABLE_APPWATCH);
                } catch (Exception f) {
                    Log.d(TAG, "Nothing to update in the database");
                }
                IntentServiceTrackApp.ActivityOnGoing = null;
                IntentServiceTrackApp.ActivityStartDate = null;
                IntentServiceTrackApp.ActivityStartTime = null;
                ActivityStopDate = null;
                ActivityStopTime = null;

                Log.d(TAG, "End monitoring date:" + ActivityStopDate + " time:" + ActivityStopTime);
            }
        }
        SettingsStudy.setAppWatchStartTime(0);
        SettingsStudy.setAppWatchId(-1);
        SettingsStudy.setStartScreenOn(0);
    }
}
