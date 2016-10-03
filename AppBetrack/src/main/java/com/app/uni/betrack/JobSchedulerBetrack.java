package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

/**
 * Created by cedoctet on 24/08/2016.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)  public class JobSchedulerBetrack extends JobService  {
    @Override
    public boolean onStartJob(JobParameters params) {
        mJobHandler.sendMessage( Message.obtain( mJobHandler, 1, params ) );
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages( 1 );
        return false;
    }

    private Handler mJobHandler = new Handler( new Handler.Callback() {

        @Override
        public boolean handleMessage( Message msg ) {

            if (CreateTrackApp.SemTrackApp.tryAcquire()) {
                Intent msgIntent = new Intent(getApplicationContext(), IntentServiceTrackApp.class);
                //Start the service for monitoring app
                startService(msgIntent);

            }

            if (System.currentTimeMillis() >= CreateTrackGPS.TimeTrigger) {
                try {
                    if (null != CreateTrackGPS.alarmIntent) {
                        CreateTrackGPS.alarmIntent.send();
                    }

                    CreateTrackGPS.TimeTrigger = System.currentTimeMillis() + SettingsBetrack.TRACKGPS_DELTA;
                } catch (PendingIntent.CanceledException e) {
                    // the stack trace isn't very helpful here.  Just log the exception message.
                    System.out.println( "Sending contentIntent failed: " );
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                CreateTrackApp.CreateAlarm(getApplicationContext(), SettingsBetrack.SAMPLING_RATE);
            }

            jobFinished((JobParameters) msg.obj, false);

            return true;
        }

    } );
}
