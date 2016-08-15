package com.app.uni.betrack;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cevincent on 6/24/16.
 */
public class ScreenReceiver extends BroadcastReceiver {

    public enum StateScreen {
        UNKNOWN, OFF, ON
    }
    public static StateScreen ScreenState = StateScreen.UNKNOWN;

    public static LocalDataBase localdatabase;

    public LocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    static final String TAG = "ScreenReceiver";

    private boolean isMyServiceRunning(Context mActivity) {
        ActivityManager manager = (ActivityManager) mActivity.getSystemService(mActivity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SettingsBetrack.SERVICE_TRACKING_NAME.equals(service.service.getClassName())) {
                Log.d(TAG, "Betrack service is runnning");
                return true;
            }
        }
        Log.d(TAG, "Betrack sevirce is not runnning");
        return false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String ActivityStopDate = "";
        String ActivityStopTime = "";
        ContentValues values = new ContentValues();

        Log.d(TAG, "Check screen state");
        if (intent.getAction().equals(SettingsBetrack.BROADCAST_CHECK_SCREEN_STATUS)) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
                for (Display display : dm.getDisplays()) {
                    if (display.getState() != Display.STATE_OFF) {
                        Log.d(TAG, "MANUAL CHECK SCREEN IS ON");
                        ScreenState = StateScreen.ON;
                        break;
                    }
                    else
                    {
                        Log.d(TAG, "MANUAL CHECK SCREEN IS OFF");
                        ScreenState = StateScreen.OFF;
                        break;
                    }
                }
            } else {
                PowerManager powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
                if  (powerManager.isScreenOn()) {
                    Log.d(TAG, "MANUAL CHECK SCREEN IS ON");
                    ScreenState = StateScreen.ON;
                }
                else
                {
                    Log.d(TAG, "MANUAL CHECK SCREEN IS OFF");
                    ScreenState = StateScreen.OFF;

                }
            }
        }
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.d(TAG, "ACTION_SCREEN_ON");
            ScreenState = StateScreen.ON;

        }
        if ((intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) ||
                (intent.getAction().equals(Intent.ACTION_SHUTDOWN))){

            Log.d(TAG, "ACTION_SCREEN_OFF or ACTION_SHUTDOWN");
            ScreenState = StateScreen.OFF;

        }

        if (ScreenState == StateScreen.OFF) {

            Log.d(TAG, "Screen is off we save the data to the local database");
            //Save the end time
            SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
            SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

            values.clear();
            values = AccesLocalDB().getOldestElementDb(LocalDataBase.TABLE_APPWATCH);
            try {
                ActivityStopDate = values.get(LocalDataBase.C_APPWATCH_DATESTOP).toString();
                Log.d(TAG, "End monitoring date: nothing to save");
            } catch (Exception e) {
                if (null != values) {
                    //Save the stop date
                    ActivityStopDate = sdf.format(new Date());
                    //Save the stop time
                    ActivityStopTime = shf.format(new Date());

                    values.put(LocalDataBase.C_APPWATCH_DATESTOP, ActivityStopDate);
                    values.put(LocalDataBase.C_APPWATCH_TIMESTOP, ActivityStopTime);
                    try {
                    this.AccesLocalDB().Update(values, values.getAsLong(LocalDataBase.C_APPWATCH_ID), LocalDataBase.TABLE_APPWATCH);
                    } catch (Exception f) {
                        Log.d(TAG, "Nothing to update in the database");
                    }
                    TrackIntentService.ActivityOnGoing = null;
                    TrackIntentService.ActivityStartDate = null;
                    TrackIntentService.ActivityStartTime = null;

                    Log.d(TAG, "End monitoring date:" + ActivityStopDate + " time:" + ActivityStopTime);
                }
            }
        }
        //Here we should rather kill the service when screen is off and restart it when screen is on
        if (!isMyServiceRunning(context)) context.startService(new Intent(context, TrackService.class));

    }

}
