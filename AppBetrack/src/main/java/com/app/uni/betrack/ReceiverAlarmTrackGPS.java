package com.app.uni.betrack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by cedoctet on 24/08/2016.
 */
public class ReceiverAlarmTrackGPS extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        try {
            //Make sure that the service is just running one time
            CreateTrackGPS.SemTrackGPS.acquire();
        } catch (Exception e) {

        } finally {
            Intent msgIntent = new Intent(context, IntentServiceTrackGPS.class);
            //Start the service for monitoring app
            context.startService(msgIntent);
            //Set next alarm
            CreateTrackGPS.CreateAlarm(context);
        }
    }
}
