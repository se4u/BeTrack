package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
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

        if (ObjSettingsStudy.getStartSurveyDone() == true) {
            //Read the preferences
            ObjSettingsBetrack = SettingsBetrack.getInstance();
            ObjSettingsBetrack.Update(context);

            context.startService(new Intent(context, ServiceBetrack.class));

            CreateNotification.CreateAlarm(context,
                    ObjSettingsBetrack.GetStudyNotification(),
                    ObjSettingsBetrack.GetStudyNotificationTime());

            CreateTrackApp.CreateAlarm(context, SettingsBetrack.SAMPLING_RATE);

            CreateTrackGPS.CreateAlarm(context);

            startTrackingRunning = true;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                if(!hasPermission(context)){
                    Log.d(TAG, "No permission !");
                }
            }
        }
    }

    @TargetApi(19) private boolean hasPermission(Context context) {

        AppOpsManager appOps = (AppOpsManager)
                context.getSystemService(context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }
}
