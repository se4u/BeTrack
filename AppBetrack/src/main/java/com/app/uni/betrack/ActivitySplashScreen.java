package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by cevincent on 5/27/16.
 */
public class ActivitySplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int TIME_OUT = 2000;

    private static SettingsStudy ObjSettingsStudy;

    Activity mActivity = this;

    NetworkGetStudiesAvailable gsa = new NetworkGetStudiesAvailable(new NetworkGetStudiesAvailable.AsyncResponse(){

        @Override
        public void processFinish(final String output) {

            new Handler().postDelayed(new Runnable() {


                @Override
                public void run() {

                    if (null != output) {
                        Intent i = new Intent(ActivitySplashScreen.this, ActivityBeTrack.class);
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

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_splash);

        ObjSettingsStudy = SettingsStudy.getInstance();
        ObjSettingsStudy.Update(this);

        try
        {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                while(true)
                {
                    if(!hasPermission()) {
                        //Explain what's going on to the user of the study before to display the setting menu
                        Thread.sleep(100);
                        new UtilsEnableUsageStat(this).show();
                    }
                    else
                    {
                        break;
                    }

                }
            }

            //Check if a study is already going on
            if (false == ObjSettingsStudy.getStudyStarted()) {

                //Try to read studies available from the distant server
                gsa.execute();

            }
            else
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            Intent i = new Intent(ActivitySplashScreen.this, ActivitySurvey.class);
                            startActivityForResult(i, 1);
                            // close this activity
                            finish();
                    }
                }, TIME_OUT);
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
