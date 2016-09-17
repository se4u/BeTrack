package com.app.uni.betrack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by cedoctet on 21/08/2016.
 */
public class ReceiverAlarmNotification extends BroadcastReceiver {

    private SettingsStudy ObjSettingsStudy;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        ObjSettingsStudy = SettingsStudy.getInstance(context);

        //We enable the daily survey
        ObjSettingsStudy.setDailySurveyDone(false);

        //Trigger a notification
        CreateNotification.Create(context);

        //Restart for a new notification in 24 hours
        CreateNotification.ResetAlarm(context);

        //onResume Betrack activity
        Intent i=new Intent(context,ActivityBeTrack.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(i);
    }

}
