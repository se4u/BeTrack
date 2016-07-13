package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.IntentService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class TrackIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.app.uni.betrack.action.FOO";
    private static final String ACTION_BAZ = "com.app.uni.betrack.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.app.uni.betrack.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.app.uni.betrack.extra.PARAM2";
    static final String TAG = "UpdaterIntentService";

    static public String ActivityOnGoing = null;
    static public String ActivityStartDate = null;
    static public String ActivityStartTime = null;

    private LocalDataBase localdatabase = new LocalDataBase(this);;

    public LocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    public TrackIntentService() {
        super("TrackIntentService");
    }

    private         InfoStudy ContextInfoStudy = new InfoStudy();

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, TrackIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, TrackIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    protected void onHandleIntent(Intent intent) {
        String topActivity = null;
        String ActivityStopDate = null;
        String ActivityStopTime=null;
        String StudyOnGoingKey = InfoStudy.STUDY_STARTED;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        boolean StudyOnGoing;

        PostDataAvailable.localdatabase = this.AccesLocalDB();
        ScreenReceiver.localdatabase = this.AccesLocalDB();

        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

        if (intent != null) {
            do {
                //Check if a study is going on
                StudyOnGoing = prefs.getBoolean(StudyOnGoingKey, false);
                if (true == StudyOnGoing) {

                    new SetupStudy(prefs, ContextInfoStudy);

                    do {
                        try {
                            // We get usage stats for the last minute
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                topActivity = handleCheckActivity_FromKitkat(intent);
                            } else {
                                topActivity = handleCheckActivity(intent);
                            }

                            //Log.d(TAG, "Foreground App " + topActivity);


                            //Check if that activity should be monitored
                            for(int i =0;i<ContextInfoStudy.ApplicationsToWatch.size();i++) {

                                //Log.d(TAG, "Application to watch " + ContextInfoStudy.ApplicationsToWatch.get(i));

                                if (null != ActivityOnGoing) {
                                    //An activity was watched and should not be watched anymore
                                    ScreenReceiver.SemUpdateStopDateTime.acquire();
                                    if ((null != topActivity) && (!ActivityOnGoing.equals(topActivity)) || (ScreenReceiver.wasScreenOff)) {

                                        if (false == ScreenReceiver.wasScreenOff) {
                                            //Save the stop date
                                            ActivityStopDate = sdf.format(new Date());
                                            //Save the stop time
                                            ActivityStopTime = shf.format(new Date());

                                            //Encrypt the data before to save them in the database

                                            //We save in the local database the informations about the study
                                            values.clear();

                                            values.put(LocalDataBase.C_APPWATCH_APPLICATION, ActivityOnGoing);
                                            values.put(LocalDataBase.C_APPWATCH_DATESTART, ActivityStartDate);
                                            values.put(LocalDataBase.C_APPWATCH_DATESTOP, ActivityStopDate);
                                            values.put(LocalDataBase.C_APPWATCH_TIMESTART, ActivityStartTime);
                                            values.put(LocalDataBase.C_APPWATCH_TIMESTOP, ActivityStopTime);
                                            this.AccesLocalDB().insertOrIgnore(values, LocalDataBase.TABLE_APPWATCH);
                                            Log.d(TAG, "End monitoring date:" + ActivityStopDate + " time:" + ActivityStopTime);


                                        } else {
                                            //Reset the flag screen off
                                            ScreenReceiver.wasScreenOff = false;
                                        }

                                        //Reinitialize activity watched infos
                                        ActivityOnGoing = null;
                                        ActivityStartDate = null;
                                        ActivityStartTime = null;
                                        ActivityStopDate = null;
                                        ActivityStopTime = null;

                                    }
                                    ScreenReceiver.SemUpdateStopDateTime.release();
                                } else {
                                    ScreenReceiver.SemUpdateStopDateTime.acquire();
                                    ScreenReceiver.wasScreenOff = false;
                                    ScreenReceiver.SemUpdateStopDateTime.release();
                                }

                                if ((null != topActivity) && (null != ContextInfoStudy.ApplicationsToWatch.get(i))) {
                                    if (topActivity.toLowerCase().contains(ContextInfoStudy.ApplicationsToWatch.get(i).toLowerCase())) {

                                        //A new activity to be watch
                                        if (null == ActivityOnGoing) {
                                            ActivityOnGoing = topActivity;

                                            //Save the date
                                            ActivityStartDate = sdf.format(new Date());
                                            //Save the start time
                                            ActivityStartTime = shf.format(new Date());

                                            Log.d(TAG, "Start monitoring: " + topActivity + " date:" + ActivityStartDate + " time:" + ActivityStartTime);

                                        }

                                    }

                                }
                            }
                            //Check if the delta between 2 updates of the server was long enough that we can start a new

                            //Check if there is a WIFI connection

                            //No WIFI can we use other ways to transfer data
                            //if ()
                            {

                                //Check if we are not already transferring the data

                                if (PostDataAvailable.SemUpdateServer.tryAcquire()) {
                                    ExecutorService es = Executors.newFixedThreadPool(1);
                                    es.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Transfer all the data available to the distant server
                                            PostDataAvailable.Start();
                                        }
                                    });
                                }
                            }

                            Thread.sleep(SettingsBetrack.SAMPLING_RATE);
                        } catch (InterruptedException e) {
                            Log.d(TAG, "Intent action error!");
                        }
                    } while (true);
                }
                else
                {
                    //We wait and check if a study has been started
                    try {
                        Log.d(TAG, "Wait for a study to be started");
                        Thread.sleep(SettingsBetrack.DELTA_BTW_RECHECK_STUDY_STARTED);
                    } catch (InterruptedException e) {
                        Log.d(TAG, "Intent action error!");
                    }
                }
            } while (true);
        }
    }

    private String handleCheckActivity(Intent intent) {
        String topActivity = null;

        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> taskInfo = mActivityManager.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        topActivity = componentInfo.getPackageName();

        return topActivity;

    }

    @TargetApi(21) private String handleCheckActivity_FromKitkat(Intent intent)
    {
        // result
        String topActivity = null;
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - SettingsBetrack.SAMPLING_RATE * 60;

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime);

        // Sort the stats by the last time used
        if (stats != null) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : stats) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (mySortedMap != null && !mySortedMap.isEmpty()) {
                topActivity = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            }
        }

        return topActivity;
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
