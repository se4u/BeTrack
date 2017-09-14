package com.app.uni.betrack;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by cevincent on 5/27/16.
 */
public class ActivitySplashScreen extends AppCompatActivity {

    // Splash screen timer
    public static int TIME_OUT = 2000;
    private SettingsStudy ObjSettingsStudy;
    private SettingsBetrack ObjSettingsBetrack = null;

    @Override
    public void onResume() {
        super.onResume();
        StartBetrack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView splashVersion;
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        String version = null;

        try {
            info = manager.getPackageInfo(
                    this.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            version = info.versionName;
        }


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_splash);

        splashVersion = (TextView) findViewById(R.id.splash_version);
        splashVersion.setText("MH001 V" + version);

        ObjSettingsStudy = SettingsStudy.getInstance(this);

        if (null == ObjSettingsBetrack) {
            //Read the preferences
            ObjSettingsBetrack = SettingsBetrack.getInstance();
            ObjSettingsBetrack.Update(this);
        }

    }

    private void StartBetrack()
    {
        //Check if there is a data connection
        final UtilsNetworkStatus.ConnectionState NetworkState = UtilsNetworkStatus.hasNetworkConnection((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));

        final NetworkGetWhatToWatch gww = new NetworkGetWhatToWatch(this, new NetworkGetWhatToWatch.AsyncResponse(){

            @Override
            public void processFinish(final String output) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent iStartStudy = new Intent(ActivitySplashScreen.this, ActivityStartStudy.class);
                        if (null != output) {
                            startActivity(iStartStudy);
                            finish();
                        } else {
                            Intent iInternetConnectivity = new Intent(ActivitySplashScreen.this, ActivityInternetConnectivity.class);
                            iInternetConnectivity.putExtra(ActivityInternetConnectivity.STATUS_START_ACTIVITY, ActivityInternetConnectivity.START_STUDY);
                            startActivity(iInternetConnectivity);
                        }

                    }
                }, TIME_OUT);
            }
        });

        //Check if a study is already going on
        if (false == ObjSettingsStudy.getStudyStarted()) {

            if ((UtilsNetworkStatus.ConnectionState.WIFI == NetworkState) ||
                    ((UtilsNetworkStatus.ConnectionState.LTE == NetworkState) && (ObjSettingsBetrack.GetEnableDataUsage()))) {
                //Try to read studies available from the distant server
                new NetworkGetStudiesAvailable(this, new NetworkGetStudiesAvailable.AsyncResponse(){

                    @Override
                    public void processFinish(final String output) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (null != output) {
                                    gww.execute();
                                } else {
                                    Intent iInternetConnectivity = new Intent(ActivitySplashScreen.this, ActivityInternetConnectivity.class);
                                    iInternetConnectivity.putExtra(ActivityInternetConnectivity.STATUS_START_ACTIVITY, ActivityInternetConnectivity.START_STUDY);
                                    startActivity(iInternetConnectivity);
                                }
                            }
                        }, TIME_OUT);
                    }
                }).execute();
            } else {
                Intent iInternetConnectivity = new Intent(ActivitySplashScreen.this, ActivityInternetConnectivity.class);
                iInternetConnectivity.putExtra(ActivityInternetConnectivity.STATUS_START_ACTIVITY, ActivityInternetConnectivity.START_STUDY);
                startActivity(iInternetConnectivity);
            }
        }
        else
        {
            if (true == ObjSettingsStudy.getSetupBetrackDone()) {

                if (false == ObjSettingsStudy.getStartSurveyDone()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(ActivitySplashScreen.this, ActivitySurveyStart.class);
                            startActivity(i);
                            finish();
                        }
                    }, TIME_OUT);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(ActivitySplashScreen.this, ActivityBeTrack.class);
                            startActivity(i);
                            finish();
                        }
                    }, TIME_OUT);
                }

            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(ActivitySplashScreen.this, ActivitySetupBetrack.class);
                        startActivity(i);
                        finish();
                    }
                }, TIME_OUT);
            }
        }
    }
}
