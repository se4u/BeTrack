package com.app.uni.betrack;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.commonsware.cwac.locpoll.LocationPoller;
import com.commonsware.cwac.locpoll.LocationPollerParameter;

/**
 * Created by cedoctet on 27/08/2016.
 */
public class CreateTrackGPS {

    private static AlarmManager alarmMgr;
    public static PendingIntent alarmIntent;
    private static final String TAG = "AlarmNotificationGPS";
    private static SettingsStudy ObjSettingsStudy = null;
    public static long TimeToSet;

    static public void CreateAlarm(Context context)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (null == ObjSettingsStudy)  {
                    ObjSettingsStudy = SettingsStudy.getInstance(context);
                }
                ObjSettingsStudy.setSetupBetrackDone(false);
                return;
            }
        }

        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }

        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i=new Intent(context, LocationPoller.class);

        Bundle bundle = new Bundle();
        LocationPollerParameter parameter = new LocationPollerParameter(bundle);
        parameter.setIntentToBroadcastOnCompletion(new Intent(context, ReceiverAlarmTrackGPS.class));
        // try GPS and fall back to NETWORK_PROVIDER
        parameter.setProviders(new String[] {LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER});
        parameter.setTimeout(3*60000);
        i.putExtras(bundle);

        alarmIntent = PendingIntent.getBroadcast(context, 0, i, 0);

        try {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                TimeToSet = System.currentTimeMillis() + SettingsBetrack.TRACKGPS_DELTA;
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

    static public void StopAlarm(Context context)
    {
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
    }
}
