package com.app.uni.betrack;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by cedoctet on 21/08/2016.
 */
public class ReceiverAlarmNotification extends WakefulBroadcastReceiver {

    private SettingsStudy ObjSettingsStudy;
    private SettingsBetrack ObjSettingsBetrack = null;

    @Override
    public void onReceive(Context context, Intent intent)
    {
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
        if ((ActivityBeTrack.OnForeground) && (ReceiverScreen.StateScreen.ON == ReceiverScreen.ScreenState)) {
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
