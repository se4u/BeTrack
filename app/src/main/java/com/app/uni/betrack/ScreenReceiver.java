package com.app.uni.betrack;

import android.content.BroadcastReceiver;
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
    public static String ActivityStopDate = "";
    public static String ActivityStopTime = "";
    public static final Semaphore SemUpdateStopDateTime = new Semaphore(1, true);
    static final String TAG = "ScreenReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
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
                wasScreenOff = true;
                SemUpdateStopDateTime.release();
            } catch (InterruptedException e) {
                Log.d(TAG, "Broadcast action error!");
            }

        }
    }

}
