package com.app.uni.betrack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

/**
 * Created by cedoctet on 21/08/2016.
 */
public class ReceiverAlarmNotification extends BroadcastReceiver {

    private SettingsStudy ObjSettingsStudy;
    private SettingsBetrack ObjSettingsBetrack = null;

    private static final String LOCK_NAME_STATIC = "com.app.uni.betrack.wakelock.notification";

    private static volatile PowerManager.WakeLock lockStatic;

    synchronized private static PowerManager.WakeLock getLock(Context context) {
        if (lockStatic == null) {
            PowerManager mgr = (PowerManager) context.getApplicationContext()
                    .getSystemService(Context.POWER_SERVICE);

            lockStatic = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    LOCK_NAME_STATIC);
            lockStatic.setReferenceCounted(true);
        }
        return (lockStatic);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        getLock(context).acquire();
        if (null == ObjSettingsStudy) {
            ObjSettingsStudy = SettingsStudy.getInstance(context);
        }

        if (null == ObjSettingsBetrack) {
            //Read the preferences
            ObjSettingsBetrack = SettingsBetrack.getInstance();
            ObjSettingsBetrack.Update(context);
        }

        //We enable the daily survey
        ObjSettingsStudy.setDailySurveyDone(false);

        //Restart for a new notification in 24 hours
        CreateNotification.ResetAlarm(context);

        //onResume Betrack activity
        if (ActivityBeTrack.OnForeground) {
            Intent i=new Intent(context,ActivityBeTrack.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(i);
        } else {
            //Trigger a notification
            CreateNotification.Create(context);
        }
        getLock(context).release();
    }
}
