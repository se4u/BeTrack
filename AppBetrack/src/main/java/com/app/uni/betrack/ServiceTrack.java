package com.app.uni.betrack;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class ServiceTrack extends Service {
    public ServiceTrack() {
    }
    static final String TAG = "UpdaterService";
    public static ConfigInfoStudy mInfoStudy;
    public static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1001;

    static ServiceTrack instance;

    private NotificationManager mNotificationManager;

    public static UtilsLocalDataBase localdatabase;

    public UtilsLocalDataBase AccesLocalDB()
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

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SHUTDOWN);
        filter.addAction(ConfigSettingsBetrack.BROADCAST_CHECK_SCREEN_STATUS);

        BroadcastReceiver mReceiver = new ReceiverScreen();
        registerReceiver(mReceiver, filter);
        instance = this;
        if (startService(new Intent(this, UtilsForegroundEnablingService.class)) == null)
            throw new RuntimeException("Couldn't find " + UtilsForegroundEnablingService.class.getSimpleName());


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) { //
        super.onStartCommand(intent, flags, startId);
        Intent msgIntent = new Intent(this, ServiceTrackIntent.class);
        Log.d(TAG, "onStarted");

        //Start the service for monitoring app
        startService(msgIntent);

        return START_STICKY;
    }
    @Override
    public void onDestroy() { //
        super.onDestroy();
        instance = null;
        stopForeground(true);
        Log.d(TAG, "onDestroyed");

    }

}
