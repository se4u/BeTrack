package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cedoctet on 21/08/2016.
 */
public class ReceiverAlarmNotification extends WakefulBroadcastReceiver {

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

        int NbrOfNotificationDone = ObjSettingsStudy.getNbrOfNotificationToDo();
        NbrOfNotificationDone--;
        ObjSettingsStudy.setNbrOfNotificationToDo(NbrOfNotificationDone);

        if (false == ObjSettingsStudy.getEndSurveyDone()) {
            if (ObjSettingsStudy.getNbrOfNotificationToDo() > 0) {
                //We enable the daily survey
                ObjSettingsStudy.setDailySurveyDone(false);
                //Restart for a new notification in 24 hours
                CreateNotification.ResetAlarm(context);
            }
        }

        values.clear();
        //Save the usage of the phone for the last 24 hours
        try {
            SettingsStudy.SemPhoneUsage.acquire();
            PhoneUsage = ObjSettingsStudy.getPhoneUsage();
            ObjSettingsStudy.setPhoneUsage(PhoneUsage + (int) ((System.currentTimeMillis() - ReceiverScreen.ScreenOnStartTime) / 1000));
            ReceiverScreen.ScreenOnStartTime = System.currentTimeMillis();
            PhoneUsage = ObjSettingsStudy.getPhoneUsage();
            values.put(UtilsLocalDataBase.C_PHONE_USAGE, PhoneUsage);
            ObjSettingsStudy.setPhoneUsage(0);
            SettingsStudy.SemPhoneUsage.release();
        } catch (Exception e) {}

        DateDaily = sdf.format(new Date());
        values.put(UtilsLocalDataBase.C_PHONE_USAGE_DATE, DateDaily);
        TimeDaily = shf.format(new Date());
        values.put(UtilsLocalDataBase.C_PHONE_USAGE_TIME, TimeDaily);

        AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_PHONE_USAGE);
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
