package com.app.uni.betrack;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class ServiceBetrack extends Service {
    public ServiceBetrack() {
    }
    static final String TAG = "ServiceBetrack";
    static ServiceBetrack instance;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() { //
        super.onCreate();

        Log.d(TAG, "onCreated");

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SHUTDOWN);
        filter.addAction(SettingsBetrack.BROADCAST_CHECK_SCREEN_STATUS);

        BroadcastReceiver mReceiver = new ReceiverScreen();
        registerReceiver(mReceiver, filter);
        instance = this;
        if (startService(new Intent(this, UtilsForegroundEnablingService.class)) == null)
            throw new RuntimeException("Couldn't find " + UtilsForegroundEnablingService.class.getSimpleName());


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
        instance = null;
        stopForeground(true);
        Log.d(TAG, "onDestroyed");

    }

}
