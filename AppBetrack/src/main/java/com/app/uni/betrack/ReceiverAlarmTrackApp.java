package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.WakefulBroadcastReceiver;

import static com.app.uni.betrack.CreateTrackApp.alarmIntent;
import static com.app.uni.betrack.CreateTrackApp.alarmMgr;
import static java.lang.System.currentTimeMillis;

/**
 * Created by cedoctet on 21/08/2016.
 */
public class ReceiverAlarmTrackApp extends WakefulBroadcastReceiver {
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
                SetAlarmFromKitKat(System.currentTimeMillis() + internalSamplingRate);
            }
            else
            {
                alarmMgr.set(AlarmManager.RTC_WAKEUP, currentTimeMillis() +
                        internalSamplingRate, alarmIntent);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)  private static void SetAlarmFromKitKat(long TimeToSet) {
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, TimeToSet, alarmIntent);
    }
}
