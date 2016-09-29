package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;

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
    private static final String LOCK_NAME_STATIC = "com.app.uni.betrack.wakelock.jobtrackapp";

    private static volatile PowerManager.WakeLock lockStatic;

    synchronized private static PowerManager.WakeLock getLock(Context context) {
        if (lockStatic == null) {
            PowerManager mgr = (PowerManager) context.getApplicationContext()
                    .getSystemService(Context.POWER_SERVICE);

            lockStatic = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    LOCK_NAME_STATIC);
            lockStatic.setReferenceCounted(true);
        }
        return (lockStatic);
    }
    private Handler mJobHandler = new Handler( new Handler.Callback() {

        @Override
        public boolean handleMessage( Message msg ) {
            getLock(getApplicationContext()).acquire();
            if (CreateTrackApp.SemTrackApp.tryAcquire()) {
                Intent msgIntent = new Intent(getApplicationContext(), IntentServiceTrackApp.class);
                //Start the service for monitoring app
                startService(msgIntent);

            }

            jobFinished( (JobParameters) msg.obj, false );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                CreateTrackApp.CreateAlarm(getApplicationContext(), SettingsBetrack.SAMPLING_RATE);
            }
            getLock(getApplicationContext()).release();
            return true;
        }

    } );
}
