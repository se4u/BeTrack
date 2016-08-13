package com.app.uni.betrack;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class TrackService extends Service {
    public TrackService() {
    }
    static final String TAG = "UpdaterService";
    public static InfoStudy mInfoStudy;
    public static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1001;

    static TrackService instance;

    private NotificationManager mNotificationManager;

    public static LocalDataBase localdatabase;

    public LocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() { //
        super.onCreate();

        Log.d(TAG, "onCreated");

        IntentFilter filter = new IntentFilter(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SHUTDOWN);
        filter.addAction(SettingsBetrack.BROADCAST_CHECK_SCREEN_STATUS);

        BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        instance = this;
        if (startService(new Intent(this, ForegroundEnablingService.class)) == null)
            throw new RuntimeException("Couldn't find " + ForegroundEnablingService.class.getSimpleName());


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) { //
        super.onStartCommand(intent, flags, startId);
        Intent msgIntent = new Intent(this, TrackIntentService.class);
        Log.d(TAG, "onStarted");

        //Start the service for monitoring app
        startService(msgIntent);

        return START_STICKY;
    }
    @Override
    public void onDestroy() { //
        super.onDestroy();
        instance = null;
        //stopForeground(true);
        Log.d(TAG, "onDestroyed");

    }

}
