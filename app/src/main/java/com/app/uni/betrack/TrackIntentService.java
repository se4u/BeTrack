package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.IntentService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

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

    public TrackIntentService() {
        super("TrackIntentService");
    }

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
        String StudyStatusKey = SetupStudy.STUDY_STARTED;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        boolean StudyStatus;
        boolean StartNewStudy = false;

        if (intent != null) {
            do {
                //Check if a study is going on
                StudyStatus = prefs.getBoolean(StudyStatusKey, false);
                if (true == StudyStatus) {
                    //Check if it's a new study
                    if (false == StartNewStudy) {
                        //Since it's a study already started, we setup the study using the local preference
                        SetupStudy lSetupStudy = new SetupStudy(prefs);
                    }
                    else
                    {
                        //InfoStudy was already completed when the new study was setup by the application
                    }
                    do {
                        try {
                            // We get usage stats for the last minute
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                topActivity = handleCheckActivity_FromKitkat(intent);
                            } else {
                                topActivity = handleCheckActivity(intent);
                            }

                            Log.d(TAG, "Foreground App " + topActivity);

                            //Check if that activity should be monitored
                            for(int i =0;i<InfoStudy.ApplicationsToWatch.size();i++){

                                Log.d(TAG, "Application to watch " + InfoStudy.ApplicationsToWatch.get(i));

                                if (InfoStudy.ApplicationsToWatch.get(i).equals(topActivity))
                                {
                                    //This has activity is monitored
                                    Log.d(TAG, "Foreground App is monitored " + topActivity);
                                }


                            }

                            //Save the information in the local database

                            //If authorise every day let's try to transfer the data over internet


                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            Log.d(TAG, "Intent action error!");
                        }
                    } while (true);
                }
                else
                {
                    //We wait 10s and check if a study has been started
                    try {
                        Log.d(TAG, "Wait for a study to be started");
                        StartNewStudy = true;
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        Log.d(TAG, "Intent action error!");
                    }
                }
            } while (true);
            /*
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }*/
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
        long beginTime = endTime - 1000 * 60;

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
