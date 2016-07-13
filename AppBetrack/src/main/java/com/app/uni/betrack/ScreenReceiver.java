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

    public static boolean wasScreenOff = false;
    public static LocalDataBase localdatabase;

    public LocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }
    public static final Semaphore SemUpdateStopDateTime = new Semaphore(1, true);
    static final String TAG = "ScreenReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String ActivityStopDate = "";
        String ActivityStopTime = "";
        ContentValues values = new ContentValues();

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            //Save the end time

            SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
            SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

            try {
                SemUpdateStopDateTime.acquire();
                //Save the stop date
                ActivityStopDate = sdf.format(new Date());
                //Save the stop time
                ActivityStopTime = shf.format(new Date());

                if (null != this.AccesLocalDB()) {
                    //We save in the local database the informations about the study
                    values.clear();
                    values.put(LocalDataBase.C_APPWATCH_APPLICATION, TrackIntentService.ActivityOnGoing);
                    values.put(LocalDataBase.C_APPWATCH_DATESTART, TrackIntentService.ActivityStartDate);
                    values.put(LocalDataBase.C_APPWATCH_DATESTOP, ActivityStopDate);
                    values.put(LocalDataBase.C_APPWATCH_TIMESTART, TrackIntentService.ActivityStartTime);
                    values.put(LocalDataBase.C_APPWATCH_TIMESTOP, ActivityStopTime);

                    wasScreenOff = true;


                    this.AccesLocalDB().insertOrIgnore(values, LocalDataBase.TABLE_APPWATCH);
                    Log.d(TAG, "End monitoring date:" + ActivityStopDate + " time:" + ActivityStopTime);
                }

                SemUpdateStopDateTime.release();
            } catch (InterruptedException e) {
                Log.d(TAG, "Broadcast action error!");
            }

        }
    }

}
