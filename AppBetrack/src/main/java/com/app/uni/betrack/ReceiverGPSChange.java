package com.app.uni.betrack;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.commonsware.cwac.locpoll.LocationPoller;
import com.commonsware.cwac.locpoll.LocationPollerParameter;

import java.util.concurrent.Semaphore;

/**
 * Created by cedoctet on 04/10/2016.
 */

public class ReceiverGPSChange  extends WakefulBroadcastReceiver {
    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;
    private SettingsStudy ObjSettingsStudy;
    public static final Semaphore SemGPS = new Semaphore(1, true);

    @Override
    public void onReceive(final Context context, final Intent intent) {
        ObjSettingsStudy = SettingsStudy.getInstance(context);

        long DeltaLastTransfer = System.currentTimeMillis() - ObjSettingsStudy.getTimeLastGPS();
        if (DeltaLastTransfer >= SettingsBetrack.TRACKGPS_DELTA)  {
            StartGPS(context);
        }
    }

    public static void StartGPS(Context context) {

        if (!SemGPS.tryAcquire()) {
            return;
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
            alarmIntent.send();
        } catch (PendingIntent.CanceledException e) {
            // the stack trace isn't very helpful here.  Just log the exception message.
            System.out.println( "Sending contentIntent failed: " );
        }

    }
}

