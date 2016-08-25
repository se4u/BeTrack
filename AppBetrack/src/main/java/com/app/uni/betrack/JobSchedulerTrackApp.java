package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

/**
 * Created by cedoctet on 24/08/2016.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)  public class JobSchedulerTrackApp extends JobService  {
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

            jobFinished( (JobParameters) msg.obj, false );
            return true;
        }

    } );
}
