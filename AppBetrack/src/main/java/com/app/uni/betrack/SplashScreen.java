package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

/**
 * Created by cevincent on 5/27/16.
 */
public class SplashScreen extends AppCompatActivity implements ViewSwitcher.ViewFactory{
    // Splash screen timer
    private static int TIME_OUT = 2000;
    private static boolean StudyOnGoing = false;
    private ImageSwitcher mSwitcher;
    // The Handler used for manage the Runnable that switch the images
    private Handler m_Handler = new Handler();

    private static final int[] imgs = {R.mipmap.ic_launcher,
            R.drawable.ic_girl_2, R.drawable.ic_boy_1, R.drawable.ic_girl_1};

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
        final String StudyOnGoingKey = InfoStudy.STUDY_STARTED;


        StudyOnGoing = prefs.getBoolean(StudyOnGoingKey, false);


        SharedPreferences.Editor editor = prefs.edit();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_splash);


        mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
        mSwitcher.setFactory(this);

        mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out));

        //mSwitcher.setImageResource(imgs[imgs.length-1]);
        mSwitcher.setImageResource(imgs[0]);
        //m_Handler.post(m_AnimatedSplashScreen);

        try
        {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                while(true)
                {
                    if(!hasPermission()) {
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            Intent i = new Intent(SplashScreen.this, BeTrackActivity.class);
                            startActivity(i);
                            //Intent i = new Intent(SplashScreen.this, SurveyActivity.class);
                            //startActivityForResult(i, 1);
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

    public View makeView() {
        ImageView i = new ImageView(this);
        i.setBackgroundColor(Color.TRANSPARENT);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return i;
    }

    Runnable m_AnimatedSplashScreen = new Runnable() {
        private int CounterImg = 0;
        public void run() {
            int NumImage  = imgs.length - 1;

            mSwitcher.setImageResource(imgs[CounterImg]);

            if (CounterImg < NumImage) {
                CounterImg++;

            }
            else {
                CounterImg=0;
            }

            m_Handler.postDelayed(this, (1000));
        }
    };

}
