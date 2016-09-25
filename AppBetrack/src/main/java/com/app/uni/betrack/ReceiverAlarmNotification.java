package com.app.uni.betrack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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

    }

}
