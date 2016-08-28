package com.app.uni.betrack;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by cedoctet on 25/08/2016.
 */
public class CreateNotification {

    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;
    private static final String TAG = "AlarmNotification";

    public final static void Create(Context context){
        final NotificationManager mNotification = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final Intent launchNotificationIntent = new Intent(context, ActivityBeTrack.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context,
                1, launchNotificationIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(context)
                .setWhen(System.currentTimeMillis())
                .setTicker(context.getString(R.string.notification_title))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_desc))
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNotification.notify(SettingsBetrack.NOTIFICATION_ID, builder.build());
        } else {
            mNotification.notify(SettingsBetrack.NOTIFICATION_ID, builder.getNotification());
        }

    }

    static public void CreateAlarm(Context context, boolean StudyNotification, String StudyNotificationTime)
    {
        long TimeToSet;
        Date time = null;


        //Read the time from the preference
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

        try {
            //Parse the time from preference
            time = shf.parse(StudyNotificationTime);
        }catch (Exception e) {
            e.printStackTrace();
        }

        //Prepare a new calendar
        Calendar calendarpref = new GregorianCalendar();
        calendarpref.setTime(time);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.SECOND, calendarpref.get(calendarpref.SECOND));
        cal.set(Calendar.HOUR_OF_DAY, calendarpref.get(calendarpref.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, calendarpref.get(calendarpref.MINUTE));

        Log.d(TAG, "Time to set " + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR)
                + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));

        if ((cal.getTimeInMillis() - System.currentTimeMillis()) < 0) {
            cal.add(Calendar.DATE, 1);
        }

        TimeToSet = cal.getTimeInMillis();


        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }

        if (true == StudyNotification)  {
            alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ReceiverAlarmNotification.class);
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            try {
                Log.d(TAG, "Time to set in ms: " + TimeToSet + " Time today in ms: " + System.currentTimeMillis() + " Result: " + (cal.getTimeInMillis() - System.currentTimeMillis()));

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, TimeToSet, alarmIntent);
                }
                else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
                {
                    alarmMgr.setExact(AlarmManager.RTC_WAKEUP, TimeToSet, alarmIntent);
                }
                else
                {
                    alarmMgr.set(AlarmManager.RTC_WAKEUP, TimeToSet, alarmIntent);
                }

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static public void ResetAlarm(Context context)
    {
        long TimeToSet = 24 * 60 * 60 * 1000;
        try {
            Log.d(TAG, "Reset alarm in 24 hours");

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TimeToSet, alarmIntent);
            }
            else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
            {
                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TimeToSet, alarmIntent);
            }
            else
            {
                alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TimeToSet, alarmIntent);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
