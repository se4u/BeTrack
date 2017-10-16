package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    private UtilsScreenState screenstate = null;
    private UtilsScreenState AccessScreenState() {return screenstate; }

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

        if (null == screenstate) {
            screenstate =  new UtilsScreenState(context);
        }

        Log.d(TAG, "Received ");

        int NbrOfNotificationDone = ObjSettingsStudy.getNbrOfNotificationToDo();
        NbrOfNotificationDone--;
        Log.d(TAG, "NbrOfNotificationDone = " + NbrOfNotificationDone);
        ObjSettingsStudy.setNbrOfNotificationToDo(NbrOfNotificationDone);


        if (false == ObjSettingsStudy.getEndSurveyDone()) {
            if (ObjSettingsStudy.getNbrOfNotificationToDo() > 0) {
                //We enable the daily survey
                ObjSettingsStudy.setDailySurveyDone(false);
                //Restart for a new notification in 24 hours
                CreateNotification.ResetAlarm(context);
            }

            //Save when the notification was received in the database
            String ActivityStartDate = "";
            String ActivityStartTime = "";
            SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
            SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");
            values.clear();
            //Save the date
            ActivityStartDate = sdf.format(new Date());
            //Save the time
            ActivityStartTime = shf.format(new Date());
            values.put(UtilsLocalDataBase.C_NOTIFICATION_RCV_DATE, ActivityStartDate);
            values.put(UtilsLocalDataBase.C_NOTIFICATION_RCV_TIME, ActivityStartTime);
            try {
                AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_NOTIFICATION_RCV);
            } catch (Exception f) {}
        }

        //Try to transfer the data
        Intent msgIntent = new Intent(context, IntentServicePostData.class);
        //Start the service for sending the data to the remote server
        context.startService(msgIntent);

        //onResume Betrack activity
        if ((ActivityBeTrack.OnForeground) &&
                ((UtilsScreenState.StateScreen.ON == ObjSettingsStudy.getBetrackScreenState() )
                || (UtilsScreenState.StateScreen.UNLOCKED == ObjSettingsStudy.getBetrackScreenState()))) {
            Intent i=new Intent(context,ActivityBeTrack.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(i);
        }
    }
}
