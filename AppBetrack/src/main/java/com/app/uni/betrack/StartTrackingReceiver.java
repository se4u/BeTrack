package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by cevincent on 6/24/16.
 */
public class StartTrackingReceiver extends BroadcastReceiver {
    static final String TAG = "StartTrackingReceiver";
    @Override
    public void onReceive(Context context, Intent intent) { //

        Log.d(TAG, "onReceived");
        context.startService(new Intent(context, TrackService.class)); //

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
