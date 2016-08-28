package com.app.uni.betrack;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.concurrent.Semaphore;

/**
 * Created by cedoctet on 27/08/2016.
 */
public class CreateTrackGPS {

    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;
    private static final String TAG = "AlarmNotification";
    public static final Semaphore SemTrackGPS = new Semaphore(1, true);

    static public void CreateAlarm(Context context)
    {

        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }

        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReceiverAlarmTrackGPS.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        try {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                        SettingsBetrack.TRACKGPS_DELTA, alarmIntent);
            }
            else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
            {
                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                        SettingsBetrack.TRACKGPS_DELTA, alarmIntent);
            }
            else
            {
                alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                        SettingsBetrack.TRACKGPS_DELTA, alarmIntent);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
