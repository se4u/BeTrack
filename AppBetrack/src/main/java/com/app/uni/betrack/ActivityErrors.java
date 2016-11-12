package com.app.uni.betrack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by cevincent on 12/11/2016.
 */

public class ActivityErrors  extends AppCompatActivity {
    public static String STATUS_START_ACTIVITY = "STATUS_START_ACTIVITY";
    public static String START_STUDY = "START_STUDY";
    public static String END_STUDY = "END_STUDY";
    private boolean isOnline = false;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ActivityErrors.this.receivedBroadcast(intent);
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter iff = new IntentFilter();
        iff.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        iff.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        iff.addAction(SettingsBetrack.BROADCAST_CHECK_INTERNET);

        // Put whatever message you want to receive as the action
        this.registerReceiver(this.mBroadcastReceiver,iff);
    }
    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mBroadcastReceiver);
    }

    private void receivedBroadcast(Intent i) {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            if (isOnline) {
                isOnline = false;
                Intent iError = new Intent(ActivityErrors.this, ActivitySplashScreen.class);
                startActivity(iError);
                finish();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gsa.execute();
                    }
                }, ReceiverNetworkChange.TIME_OUT);
            }
        }
    }

    NetworkGetStudiesAvailable gsa = new NetworkGetStudiesAvailable(this, new NetworkGetStudiesAvailable.AsyncResponse(){

        @Override
        public void processFinish(final String output) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != output) {
                        isOnline = true;
                    } else {
                        isOnline = false;
                    }
                    Intent intent = new Intent();
                    intent.setAction(SettingsBetrack.BROADCAST_CHECK_INTERNET);
                    sendBroadcast(intent);
                }
            }, ActivitySplashScreen.TIME_OUT);
        }});

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView Description;
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_errors);
        Bundle extras = getIntent().getExtras();
        String data = extras.getString(STATUS_START_ACTIVITY, null);
        Description = (TextView) findViewById(R.id.network_error_text);
        if(data.equals(START_STUDY)) {
            //Start of the study
            Description.setText(getResources().getString(R.string.network_error_start_text));
        } else if(data.equals(END_STUDY)) {
            //End study case
            Description.setText(getResources().getString(R.string.network_error_end_text));
        }
    }
}
