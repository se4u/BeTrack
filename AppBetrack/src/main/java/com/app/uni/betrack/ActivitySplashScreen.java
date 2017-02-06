package com.app.uni.betrack;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

    NetworkGetStudiesAvailable gsa = new NetworkGetStudiesAvailable(this, new NetworkGetStudiesAvailable.AsyncResponse(){

        @Override
        public void processFinish(final String output) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != output) {
                        gww.execute();
                    } else {
                        Intent iError = new Intent(ActivitySplashScreen.this, ActivityErrors.class);
                        iError.putExtra(ActivityErrors.STATUS_START_ACTIVITY, ActivityErrors.START_STUDY);
                        startActivity(iError);
                        finish();
                    }
                }
            }, TIME_OUT);
        }});

    NetworkGetWhatToWatch gww = new NetworkGetWhatToWatch(this, new NetworkGetWhatToWatch.AsyncResponse(){

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
                        Intent iError = new Intent(ActivitySplashScreen.this, ActivityErrors.class);
                        iError.putExtra(ActivityErrors.STATUS_START_ACTIVITY, ActivityErrors.START_STUDY);
                        startActivity(iError);
                        finish();
                    }

                }
            }, TIME_OUT);
    }});


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
        splashVersion.setText("V" + version);

        ObjSettingsStudy = SettingsStudy.getInstance(this);

        //Check if a study is already going on
        if (false == ObjSettingsStudy.getStudyStarted()) {

            //Try to read studies available from the distant server
            gsa.execute();
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
