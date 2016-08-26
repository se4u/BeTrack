package com.app.uni.betrack;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by cedoctet on 21/08/2016.
 */
public class ReceiverAlarmTrackApp extends BroadcastReceiver {
    public static int internalSamplingRate;

    @Override
    public void onReceive(Context context, Intent intent)
    {

        try {
            //Make sure that the service is just running one time
            CreateTrackApp.SemTrackApp.acquire();
        } catch (Exception e) {

        } finally {
            Intent msgIntent = new Intent(context, IntentServiceTrackApp.class);
            //Start the service for monitoring app
            context.startService(msgIntent);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
            {
                CreateTrackApp.alarmMgr.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                        internalSamplingRate, CreateTrackApp.alarmIntent);
            }
            else
            {
                CreateTrackApp.alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                        internalSamplingRate, CreateTrackApp.alarmIntent);
            }
        }
    }
}
