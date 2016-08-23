package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by cevincent on 6/24/16.
 */
public class ReceiverStartTracking extends BroadcastReceiver {
    static final String TAG = "ReceiverStartTracking";

    private ConfigSettingsBetrack ObjSettingsBetrack;
    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) { //

        Log.d(TAG, "onReceived");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();

        ReceiverScreen.ScreenState = ReceiverScreen.StateScreen.ON;

        //Read the preferences
        ObjSettingsBetrack = ConfigSettingsBetrack.getInstance();
        ObjSettingsBetrack.UpdateSettingsBetrack(prefs, context);


        ReceiverAlarmNotification.CreateAlarm(context,
                                                ObjSettingsBetrack.StudyNotification,
                                                    ObjSettingsBetrack.StudyNotificationTime);


        context.startService(new Intent(context, ServiceTrack.class)); //

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            if(!hasPermission(context)){
                Log.d(TAG, "No permission !");
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
