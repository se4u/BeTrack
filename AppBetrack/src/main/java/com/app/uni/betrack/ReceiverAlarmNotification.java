package com.app.uni.betrack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

        //Check that the alarm was not triggered too early
        //Prepare a new calendar
        Date time = null;


        //Read the time from the preference
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

        try {
            //Parse the time from preference
            time = shf.parse(ObjSettingsBetrack.GetStudyNotificationTime());
        }catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendarpref = new GregorianCalendar();
        calendarpref.setTime(time);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.SECOND, calendarpref.get(calendarpref.SECOND));
        cal.set(Calendar.HOUR_OF_DAY, calendarpref.get(calendarpref.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, calendarpref.get(calendarpref.MINUTE));
        long TimeToSleep = cal.getTimeInMillis() - System.currentTimeMillis();
        if (TimeToSleep > 0) {
            try {
                //Sleep until we reach the exact time + 5s
                Thread.sleep(TimeToSleep + 5000);
            }catch (Exception e) {
                e.printStackTrace();
            }
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
