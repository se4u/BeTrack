package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by cevincent on 6/24/16.
 */
public class ReceiverStartTracking extends WakefulBroadcastReceiver {
    static final String TAG = "ReceiverStartTracking";

    public static boolean startTrackingRunning = false;

    private SettingsStudy ObjSettingsStudy;
    private SettingsBetrack ObjSettingsBetrack;

    @Override
    public void onReceive(Context context, Intent intent) { //

        Log.d(TAG, "onReceived");
        ObjSettingsStudy = SettingsStudy.getInstance(context);

        ReceiverScreen.ScreenState = ReceiverScreen.StateScreen.ON;


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
            if ( (true == ObjSettingsBetrack.GetStudyNotification()) && (ObjSettingsStudy.getTimeNextNotification() - System.currentTimeMillis() < 0) ) {
                Log.d(TAG, "We missed the notification so we trigger it manually");
                //We enable the daily survey
                ObjSettingsStudy.setDailySurveyDone(false);

                //Trigger a notification
                CreateNotification.Create(context);
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

            context.startService(new Intent(context, ServiceBetrack.class));

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
