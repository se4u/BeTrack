package com.app.uni.betrack;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class ServiceBetrack extends Service {
    public ServiceBetrack() {
    }
    static final String TAG = "ServiceBetrack";
    static ServiceBetrack instance;

    private SettingsStudy ObjSettingsStudy = null;
    private BroadcastReceiver mReceiverScreen;
    private BroadcastReceiver mNetworkChange;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() { //
        super.onCreate();

        Log.d(TAG, "onCreated");

        IntentFilter filterScreen = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filterScreen.addAction(Intent.ACTION_SCREEN_ON);

        mReceiverScreen = new ReceiverScreen();
        registerReceiver(mReceiverScreen, filterScreen);

        IntentFilter filterNetworkChange = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        filterNetworkChange.addAction("android.net.wifi.WIFI_STATE_CHANGED");

        mNetworkChange = new ReceiverNetworkChange();
        registerReceiver(mNetworkChange, filterNetworkChange);

        if (null == ObjSettingsStudy) {
            //Read the setting of the study
            ObjSettingsStudy = SettingsStudy.getInstance(this);
        }

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
        unregisterReceiver(mReceiverScreen);
        unregisterReceiver(mNetworkChange);
        stopForeground(true);
        //Check if we get killed
        SettingsStudy.EndStudyTranferState endStudyTranferState = ObjSettingsStudy.getEndSurveyTransferred();
        if (endStudyTranferState != SettingsStudy.EndStudyTranferState.DONE) {
            //We got killed but the study is not finished so we restart the service
            Intent intent = new Intent();
            intent.setAction(SettingsBetrack.BROADCAST_START_TRACKING_NAME);
            intent.putExtra(SettingsBetrack.BROADCAST_ARG_MANUAL_START, "1");
            sendBroadcast(intent);
        }
        Log.d(TAG, "onDestroyed");

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // start blank activity to prevent kill
        // @see https://code.google.com/p/android/issues/detail?id=53313
        Intent intent = new Intent(this, ActivityDummy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onTaskRemoved(rootIntent);
    }

}
