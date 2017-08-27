package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.text.SimpleDateFormat;

/**
 * Created by cedoctet on 21/08/2016.
 */
public class ReceiverAlarmNotification extends WakefulBroadcastReceiver {
    static final String TAG = "ReceiverAlarmNotif";
    private SettingsStudy ObjSettingsStudy;
    private SettingsBetrack ObjSettingsBetrack = null;
    private int PhoneUsage = 0;
    private ContentValues values = new ContentValues();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
    private SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");
    private String DateDaily = null;
    private String TimeDaily = null;
    private UtilsLocalDataBase localdatabase = null;
    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

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

        if (null == localdatabase) {
            localdatabase =  new UtilsLocalDataBase(context);
        }

        Log.d(TAG, "Received ");

        int NbrOfNotificationToDo = ObjSettingsStudy.getNbrOfNotificationToDo();
        NbrOfNotificationToDo--;
        Log.d(TAG, "NbrOfNotificationDone = " + NbrOfNotificationToDo);
        ObjSettingsStudy.setNbrOfNotificationToDo(NbrOfNotificationToDo);

        //Check it's the first of the study and te first notification
        if ( (NbrOfNotificationToDo + 1) != (ObjSettingsStudy.getNbrOfDaysToDo() * UtilsGetNotification.getNbrPerDay()) ) {
            //It's not so we check if "getNbrPerDay" notifications were done
            if ((NbrOfNotificationToDo % UtilsGetNotification.getNbrPerDay()) == 0) {
                //"getNbrPerDay" notifications were done, so one day of the study is over
                int NbrOfDaysToDo = ObjSettingsStudy.getNbrOfDaysToDo();
                NbrOfDaysToDo--;
                Log.d(TAG, "NbrOfDaysToDo = " + NbrOfDaysToDo);
                ObjSettingsStudy.setNbrOfDaysToDo(NbrOfDaysToDo);
            }
        }

        if (false == ObjSettingsStudy.getEndSurveyDone()) {
            if (ObjSettingsStudy.getNbrOfNotificationToDo() > 0) {
                //We enable the daily survey
                ObjSettingsStudy.setDailySurveyDone(false);
                //Restart for a new notification in 24 hours
                CreateNotification.ResetAlarm(context);
            }
        }

        //Trigger a notification
        CreateNotification.Create(context);

        //onResume Betrack activity
        if ((ActivityBeTrack.OnForeground) && (ReceiverScreen.StateScreen.ON == ReceiverScreen.ScreenState)) {
            Intent i=new Intent(context,ActivityBeTrack.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(i);
        }
    }
}
