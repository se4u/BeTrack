package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cevincent on 6/24/16.
 */
public class ReceiverStartTracking extends WakefulBroadcastReceiver {
    static final String TAG = "ReceiverStartTracking";

    public static boolean startTrackingRunning = false;

    private SettingsStudy ObjSettingsStudy;
    private SettingsBetrack ObjSettingsBetrack;
    private static UtilsLocalDataBase localdatabase =  null;

    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    private UtilsScreenState screenstate = null;
    private UtilsScreenState AccessScreenState() {return screenstate; }


    @Override
    public void onReceive(Context context, Intent intent) {

        String ActivityStartDate = "";
        String ActivityStartTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");


        if (null == ObjSettingsStudy)  {
            ObjSettingsStudy = SettingsStudy.getInstance(context);
        }

        if (ObjSettingsBetrack == null) {
            ObjSettingsBetrack = SettingsBetrack.getInstance();
            ObjSettingsBetrack.Update(context);
        }

        if (null == localdatabase) {
            localdatabase =  new UtilsLocalDataBase(context);
        }

        if (null == screenstate) {
            screenstate =  new UtilsScreenState(context);
        }

        ContentValues values = new ContentValues();
        Log.d(TAG, "onReceived");
        ObjSettingsStudy = SettingsStudy.getInstance(context);

        AccessScreenState().UtilsSetSavedScreenState(UtilsScreenState.StateScreen.ON);

        try {
            SettingsStudy.SemScreenOn.acquire();
            if (SettingsStudy.getStartScreenOn() == 0) {
                SettingsStudy.setStartScreenOn(System.currentTimeMillis());
                Log.d(TAG, "Screen ON saved " + System.currentTimeMillis());
            }

        } catch (Exception eScreenOn) {
        }
        finally {
            SettingsStudy.SemScreenOn.release();
        }

        Bundle results = getResultExtras(true);

        String id = intent.getStringExtra(SettingsBetrack.BROADCAST_ARG_MANUAL_START);


        //Screen is on we save this status in the databse
        //Save when the screen was switched off
        values.clear();
        //Save the date
        ActivityStartDate = sdf.format(new Date());
        //Save the time
        ActivityStartTime = shf.format(new Date());

        //The system is just started
        if(id == null) {
            Log.d(TAG, "The system is just started");
            Log.d(TAG, "SCREEN_PHONE_SWITCHED_ON saved in database " + ActivityStartDate + " " + ActivityStartTime);
            values.put(UtilsLocalDataBase.C_PHONE_USAGE_STATE, SettingsBetrack.SCREEN_PHONE_SWITCHED_ON);
            //Since we were off we check if we didn't miss the last notification
            if ( (true == ObjSettingsBetrack.GetStudyNotification())
                    && (ObjSettingsStudy.getTimeNextNotification() - System.currentTimeMillis() < 0)
                    && (ObjSettingsStudy.getEndSurveyTransferred() == SettingsStudy.EndStudyTranferState.NOT_YET)
                    && (ObjSettingsStudy.getStartSurveyDone() == true)) {
                Log.d(TAG, "We missed the notification so we trigger it manually");
                Intent intentNotif = new Intent();
                intentNotif.setAction(SettingsBetrack.BROADCAST_TRIGGER_NOTIFICATION);
                context.sendBroadcast(intentNotif);
            } else {
                Log.d(TAG, "notification was not missed we go on with the startup");
            }
        } else {
            values.put(UtilsLocalDataBase.C_PHONE_USAGE_STATE, SettingsBetrack.SCREEN_BETRACK_STARTED_MANUALY);
        }

        values.put(UtilsLocalDataBase.C_PHONE_USAGE_DATE, ActivityStartDate);
        values.put(UtilsLocalDataBase.C_PHONE_USAGE_TIME, ActivityStartTime);
        try {
            if (((id != null) && (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N)) || (id == null)) {
                Log.d(TAG, "SCREEN_BETRACK_STARTED_MANUALY saved in database " + ActivityStartDate + " " + ActivityStartTime);
                AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_PHONE_USAGE);
            } else {
                values.clear();
            }
        } catch (Exception f) {
            Log.d(TAG, "Nothing to update in the database");
        }

        if (ObjSettingsStudy.getStartSurveyDone() == true) {
            //Read the preferences
            if (ObjSettingsBetrack == null) {
                ObjSettingsBetrack = SettingsBetrack.getInstance();
                ObjSettingsBetrack.Update(context);
            }

            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                context.startService(new Intent(context, ServiceBetrack.class));
            }
            CreateNotification.CreateAlarm(context,
                    ObjSettingsBetrack.GetStudyNotification(),
                    ObjSettingsBetrack.GetStudyNotificationTime(),
                    false);

            CreateTrackApp.CreateAlarm(context, SettingsBetrack.SAMPLING_RATE);

            startTrackingRunning = true;

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if(!hasPermission(context)){
                    Log.d(TAG, "No permission !");
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) private boolean hasPermission(Context context) {

        AppOpsManager appOps = (AppOpsManager)
                context.getSystemService(context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }
}
