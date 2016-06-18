package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by cevincent on 5/27/16.
 */
public class SplashScreen extends AppCompatActivity {
    // Splash screen timer
    private static int TIME_OUT = 1000;
    private static boolean StudyOnGoing = false;
    private String STUDY_ONGOING = "study_ongoing";
    Activity mActivity = this;

    GetStudiesAvailable gsa = new GetStudiesAvailable(new GetStudiesAvailable.AsyncResponse(){

        @Override
        public void processFinish(final String output) {

            new Handler().postDelayed(new Runnable() {


                @Override
                public void run() {

                    if (null != output) {
                        Intent i = new Intent(SplashScreen.this, BeTrackActivity.class);
                        startActivity(i);

                        // close this activity
                        finish();
                    } else {
                        new NetworkError(mActivity).show();
                    }

                }
            }, TIME_OUT);
        }});



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String StudyOnGoingKey = STUDY_ONGOING;
        StudyOnGoing = prefs.getBoolean(StudyOnGoingKey, false);


        SharedPreferences.Editor editor = prefs.edit();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_splash);

        try
        {

           // StudyOnGoing = true; // TODO to be remove for debug without internet

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                while(true)
                {
                    if(!hasPermission()) {
                    //if(true) {
                        //Explain what's going on to the user of the study before to display the setting menu
                        Thread.sleep(100);
                        new EnableUsageStat(this).show();
                    }
                    else
                    {
                        break;
                    }

                }
            }

            //Check if a study is already going on
            if (false == StudyOnGoing) {

                //Try to read studies available from the distant server
                gsa.execute();

            }
            else
            {
                Intent i = new Intent(SplashScreen.this, BeTrackActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }

        }
        catch (Exception e) {

        }

    }
    @TargetApi(19) private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }
}
