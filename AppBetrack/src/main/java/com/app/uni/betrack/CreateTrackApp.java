package com.app.uni.betrack;

import android.annotation.TargetApi;
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
public class CreateTrackApp {


    private static JobScheduler mJobScheduler = null;
    public static AlarmManager alarmMgr;
    public static PendingIntent alarmIntent;
    public static final Semaphore SemTrackApp = new Semaphore(1, true);


    static public void CreateAlarm(Context context, int SamplingRate)
    {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            CreateJob(context, SamplingRate);

        }
        else {

            if (alarmMgr!= null) {
                alarmMgr.cancel(alarmIntent);
            }

            alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ReceiverAlarmTrackApp.class);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            }
            alarmIntent = PendingIntent.getBroadcast(context, SettingsBetrack.ID_TRACKAPP, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            try {
                ReceiverAlarmTrackApp.internalSamplingRate = SamplingRate;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
                {
                    SetAlarmFromKitKat(System.currentTimeMillis() + SamplingRate);
                }
                else
                {
                    alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                            SamplingRate, alarmIntent);
                }


            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static public void StopAlarm(Context context)
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CancelJob(SettingsBetrack.ID_TRACKAPP);
        } else {
            if (alarmMgr != null) {
                alarmMgr.cancel(alarmIntent);
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)  private static void SetAlarmFromKitKat(long TimeToSet) {
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, TimeToSet, alarmIntent);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)  private static void CancelJob(int jobId) {
        if (null != mJobScheduler) {
            mJobScheduler.cancel(jobId);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)  private static void CreateJob(Context context, int SamplingRate) {
        if (null != mJobScheduler) {
            mJobScheduler.cancel(SettingsBetrack.ID_TRACKAPP);
        }

        mJobScheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo.Builder builder = new JobInfo.Builder( SettingsBetrack.ID_TRACKAPP,
                new ComponentName( context.getPackageName(),
                        JobSchedulerBetrack.class.getName() ) );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(SamplingRate);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setPeriodic(SamplingRate);
            }
        }


        if( mJobScheduler.schedule( builder.build() ) <= 0 ) {
            //If something goes wrong
        }
    }
}
