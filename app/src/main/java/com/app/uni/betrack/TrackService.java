package com.app.uni.betrack;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class TrackService extends Service {
    public TrackService() {
    }
    static final String TAG = "UpdaterService";
    public static InfoStudy mInfoStudy;
    public static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1001;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() { //
        super.onCreate();
        Intent msgIntent = new Intent(this, TrackIntentService.class);
        Log.d(TAG, "onCreated");

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);

        //Start the service for monitoring app
        startService(msgIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) { //
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStarted");

        return START_STICKY;
    }
    @Override
    public void onDestroy() { //
        super.onDestroy();

        Log.d(TAG, "onDestroyed");
    }
}
