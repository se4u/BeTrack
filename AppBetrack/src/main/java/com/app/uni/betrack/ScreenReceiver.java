package com.app.uni.betrack;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;

/**
 * Created by cevincent on 6/24/16.
 */
public class ScreenReceiver extends BroadcastReceiver {

    public static boolean ScreenOff = false;

    public static LocalDataBase localdatabase;

    public LocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    static final String TAG = "ScreenReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String ActivityStopDate = "";
        String ActivityStopTime = "";
        ContentValues values = new ContentValues();

        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            Log.d(TAG, "ACTION_USER_PRESENT");
            ScreenOff = false;

        }
        if ((intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) ||
                (intent.getAction().equals(Intent.ACTION_SHUTDOWN))){

            Log.d(TAG, "ACTION_SCREEN_OFF or ACTION_SHUTDOWN");
            ScreenOff = true;
            //Save the end time
            SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
            SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

            //Save the stop date
            ActivityStopDate = sdf.format(new Date());
            //Save the stop time
            ActivityStopTime = shf.format(new Date());

            if ((null != this.AccesLocalDB()) && (null != TrackIntentService.ActivityOnGoing)) {
                //We save in the local database the informations about the study
                values.clear();
                values.put(LocalDataBase.C_APPWATCH_USERID, InfoStudy.IdUser);
                values.put(LocalDataBase.C_APPWATCH_APPLICATION, TrackIntentService.ActivityOnGoing);
                values.put(LocalDataBase.C_APPWATCH_DATESTART, TrackIntentService.ActivityStartDate);
                values.put(LocalDataBase.C_APPWATCH_DATESTOP, ActivityStopDate);
                values.put(LocalDataBase.C_APPWATCH_TIMESTART, TrackIntentService.ActivityStartTime);
                values.put(LocalDataBase.C_APPWATCH_TIMESTOP, ActivityStopTime);
                this.AccesLocalDB().insertOrIgnore(values, LocalDataBase.TABLE_APPWATCH);
                Log.d(TAG, "End monitoring date:" + ActivityStopDate + " time:" + ActivityStopTime);
            }
        }
    }

}
