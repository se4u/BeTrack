package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Date;

/**
 * Created by cevincent on 6/24/16.
 */
public class ReceiverStartTracking extends WakefulBroadcastReceiver {
    static final String TAG = "ReceiverStartTracking";

    public static boolean startTrackingRunning = false;
    public static boolean screenJustStarted = true;

    private SettingsStudy ObjSettingsStudy;
    private SettingsBetrack ObjSettingsBetrack;

    @Override
    public void onReceive(Context context, Intent intent) { //

        Log.d(TAG, "onReceived");
        ObjSettingsStudy = SettingsStudy.getInstance(context);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            ReceiverScreen.ScreenState = ReceiverScreen.StateScreen.ON;

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
        } else {

        }

        if (null == ObjSettingsStudy)  {
            ObjSettingsStudy = SettingsStudy.getInstance(context);
        }

        if (ObjSettingsBetrack == null) {
            ObjSettingsBetrack = SettingsBetrack.getInstance();
            ObjSettingsBetrack.Update(context);
        }

        Bundle results = getResultExtras(true);

        String id = intent.getStringExtra(SettingsBetrack.BROADCAST_ARG_MANUAL_START);
        //The system is just started
        if(id == null) {
            Log.d(TAG, "The system is just started");

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
