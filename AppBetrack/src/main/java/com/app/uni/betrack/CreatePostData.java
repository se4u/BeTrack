package com.app.uni.betrack;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.concurrent.Semaphore;

/**
 * Created by cedoctet on 25/08/2016.
 */
public class CreatePostData {

    private static JobScheduler mJobScheduler;
    public static AlarmManager alarmMgr;
    public static PendingIntent alarmIntent;
    public static final Semaphore SemUpdateServer = new Semaphore(1, true);
    public static boolean InternalFastCheck;

    static public void CreateAlarm(Context context, boolean FastCheck)
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (null != mJobScheduler) {
                mJobScheduler.cancel(ConfigSettingsBetrack.JOBID_POSTDATA);
            }

            mJobScheduler = (JobScheduler)
                    context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

            JobInfo.Builder builder = new JobInfo.Builder( ConfigSettingsBetrack.JOBID_POSTDATA,
                    new ComponentName( context.getPackageName(),
                            JobSchedulerPostData.class.getName() ) );

            if (false == FastCheck) {
                builder.setPeriodic(ConfigSettingsBetrack.POSTDATA_SENDING_DELTA);
            }
            else {
                builder.setPeriodic(ConfigSettingsBetrack.POSTDATA_SENDING_DELTA_FASTCHECK);
            }

            InternalFastCheck = FastCheck;

            if( mJobScheduler.schedule( builder.build() ) <= 0 ) {
                //If something goes wrong
            }

        }
        else {
            if (alarmMgr!= null) {
                alarmMgr.cancel(alarmIntent);
            }

            alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ReceiverAlarmPostData.class);
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            try {
                if (false == FastCheck) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
                    {
                        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                                ConfigSettingsBetrack.POSTDATA_SENDING_DELTA, alarmIntent);
                    }
                    else
                    {
                        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                                ConfigSettingsBetrack.POSTDATA_SENDING_DELTA, alarmIntent);
                    }
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
                    {
                        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                                ConfigSettingsBetrack.POSTDATA_SENDING_DELTA_FASTCHECK, alarmIntent);
                    }
                    else
                    {
                        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                                ConfigSettingsBetrack.POSTDATA_SENDING_DELTA_FASTCHECK, alarmIntent);
                    }
                }

                InternalFastCheck = FastCheck;

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
