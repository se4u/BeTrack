package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.IntentService;
import android.app.KeyguardManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;



public class IntentServiceTrackApp extends IntentService {


    static final String TAG = "IntentServiceTrackApp";

    public static String ActivityOnGoing = null;
    public static String ActivityStartDate = null;
    public static String ActivityStartTime = null;


    private static SettingsBetrack ObjSettingsBetrack = null;
    private static SettingsStudy ObjSettingsStudy = null;

    private static UtilsLocalDataBase localdatabase = null;

    public UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    Handler mHandler;

    public IntentServiceTrackApp() {

        super("IntentServiceTrackApp");
        mHandler = new Handler();
    }

    protected void onHandleIntent(Intent intent) {
        String topActivity = null;
        String ActivityStopDate = null;
        String ActualTime = null;
        String ActivityStopTime=null;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        boolean StudyOnGoing;

        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

        if (null == ObjSettingsBetrack) {
            //Read the preferences
            ObjSettingsBetrack = SettingsBetrack.getInstance();
            ObjSettingsBetrack.UpdateSettingsBetrack(prefs, this);
        }

        if (null == ObjSettingsStudy)  {
            ObjSettingsStudy = SettingsStudy.getInstance();
            ObjSettingsStudy.Update(this);
        }

        if (null == localdatabase) {
            localdatabase = new UtilsLocalDataBase(this);
        }

        if (intent != null) {

            //Check if a study is going on
            StudyOnGoing = ObjSettingsBetrack.GetStudyEnable();
            if (true == StudyOnGoing) {

                Intent intentCheckScreenStatus = new Intent();

                if (ObjSettingsBetrack.GetStudyEnable())
                {

                    // We get usage stats for the last minute
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        topActivity = handleCheckActivity_FromKitkat(intent);
                    } else {
                        topActivity = handleCheckActivity(intent);
                    }

                    Log.d(TAG, "Foreground App " + topActivity);
                    if (ReceiverScreen.StateScreen.UNKNOWN == ReceiverScreen.ScreenState) {
                        intentCheckScreenStatus.setAction(SettingsBetrack.BROADCAST_CHECK_SCREEN_STATUS);
                        this.sendBroadcast(intentCheckScreenStatus);
                    }

                    //Check the status of the screen
                    //Check if that activity should be monitored
                    for(int i =0;i<ObjSettingsStudy.getApplicationsToWatch().size();i++) {

                        //Log.d(TAG, "Application to watch " + ContextInfoStudy.ApplicationsToWatch.get(i));
                        //Log.d(TAG, "ActivityOnGoing " + ActivityOnGoing);
                        //Log.d(TAG, "topActivity " + topActivity);

                        if (null != ActivityOnGoing) {
                            //An activity was watched and should not be watched anymore
                            if ((null != topActivity) && (!ActivityOnGoing.equals(topActivity))) {

                                if (ReceiverScreen.StateScreen.ON == ReceiverScreen.ScreenState) {

                                    //We save in the local database the informations about the study
                                    values.clear();
                                    values = AccesLocalDB().getOldestElementDb(UtilsLocalDataBase.TABLE_APPWATCH);
                                    try {
                                        ActivityStopDate = values.get(UtilsLocalDataBase.C_APPWATCH_DATESTOP).toString();
                                        //Log.d(TAG, "Data ready to be transfer to the remote database ");
                                    } catch (Exception e) {
                                        //Save the stop date
                                        ActivityStopDate = sdf.format(new Date());
                                        //Save the stop time
                                        ActivityStopTime = shf.format(new Date());

                                        values.put(UtilsLocalDataBase.C_APPWATCH_DATESTOP, ActivityStopDate);
                                        values.put(UtilsLocalDataBase.C_APPWATCH_TIMESTOP, ActivityStopTime);

                                        this.AccesLocalDB().Update(values, values.getAsLong(UtilsLocalDataBase.C_APPWATCH_ID), UtilsLocalDataBase.TABLE_APPWATCH);

                                        //Reinitialize activity watched infos
                                        ActivityOnGoing = null;
                                        ActivityStartDate = null;
                                        ActivityStartTime = null;
                                        ActivityStopDate = null;
                                        ActivityStopTime = null;

                                        Log.d(TAG, "End monitoring date:" + ActivityStopDate + " time:" + ActivityStopTime);
                                    }
                                }
                            }
                        }
                        else {
                            values.clear();
                            values = AccesLocalDB().getOldestElementDb(UtilsLocalDataBase.TABLE_APPWATCH);
                            if (0 != values.size()) {
                                try {
                                    ActivityStopDate = values.get(UtilsLocalDataBase.C_APPWATCH_DATESTOP).toString();
                                    //Log.d(TAG, "Data ready to be transfer to the remote database ");
                                } catch (Exception e) {
                                    Log.d(TAG, "Check in case some monitoring was started but never stopped");
                                    //Save the stop date
                                    ActivityStopDate = sdf.format(new Date());
                                    //Save the stop time
                                    ActivityStopTime = shf.format(new Date());

                                    values.put(UtilsLocalDataBase.C_APPWATCH_DATESTOP, ActivityStopDate);
                                    values.put(UtilsLocalDataBase.C_APPWATCH_TIMESTOP, ActivityStopTime);

                                    this.AccesLocalDB().Update(values, values.getAsLong(UtilsLocalDataBase.C_APPWATCH_ID), UtilsLocalDataBase.TABLE_APPWATCH);

                                    Log.d(TAG, "Finish last entry end monitoring date:" + ActivityStopDate + " time:" + ActivityStopTime);
                                }
                                finally {
                                    //Reinitialize activity watched infos
                                    ActivityOnGoing = null;
                                    ActivityStartDate = null;
                                    ActivityStartTime = null;
                                    ActivityStopDate = null;
                                    ActivityStopTime = null;
                                }
                            }
                        }

                        //Log.d(TAG, "Check app foreground: " + topActivity + "app to monitor: " + ContextInfoStudy.ApplicationsToWatch.get(i) + " Screen state: " + ReceiverScreen.ScreenState);

                        if ((null != topActivity) && (null != ObjSettingsStudy.getApplicationsToWatch().get(i))) {
                            if (topActivity.toLowerCase().contains(ObjSettingsStudy.getApplicationsToWatch().get(i).toLowerCase())) {

                                //Log.d(TAG, "Status ActivityOnGoing: " + ActivityOnGoing);

                                //A new activity to be watch
                                if (null == ActivityOnGoing) {

                                    //Check the status of the screen
                                    if (ReceiverScreen.StateScreen.ON == ReceiverScreen.ScreenState) {
                                        KeyguardManager km = (KeyguardManager) getBaseContext().getSystemService(Context.KEYGUARD_SERVICE);
                                        boolean locked = km.inKeyguardRestrictedInputMode();

                                        if (!locked) {
                                            values.clear();
                                            values = AccesLocalDB().getOldestElementDb(UtilsLocalDataBase.TABLE_APPWATCH);
                                            try {
                                                if (0 != values.size()) {
                                                    ActivityStopDate = values.get(UtilsLocalDataBase.C_APPWATCH_DATESTOP).toString();
                                                    Log.d(TAG, "Last entry is not null we can start a new monitoring");
                                                }
                                                else {
                                                    Log.d(TAG, "New monitoring started");
                                                }

                                                mHandler.post(new UtilsDisplayToast(this, "Betrack: Start monitoring: " + topActivity));

                                                ActivityOnGoing = topActivity;

                                                //Save the date
                                                ActivityStartDate = sdf.format(new Date());
                                                //Save the start time
                                                ActivityStartTime = shf.format(new Date());

                                                ActivityStopDate = null;
                                                ActivityStopTime = null;

                                                values.clear();
                                                values.put(UtilsLocalDataBase.C_APPWATCH_APPLICATION, ActivityOnGoing);
                                                values.put(UtilsLocalDataBase.C_APPWATCH_DATESTART, ActivityStartDate);
                                                values.put(UtilsLocalDataBase.C_APPWATCH_DATESTOP, ActivityStopDate);
                                                values.put(UtilsLocalDataBase.C_APPWATCH_TIMESTART, ActivityStartTime);
                                                values.put(UtilsLocalDataBase.C_APPWATCH_TIMESTOP, ActivityStopTime);
                                                this.AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_APPWATCH);

                                                Log.d(TAG, "IdUser: " + ObjSettingsStudy.getIdUser() + "Start monitoring: " + topActivity + " date:" + ActivityStartDate + " time:" + ActivityStartTime);

                                            } catch (Exception e) {

                                                ActivityOnGoing = values.get(UtilsLocalDataBase.C_APPWATCH_APPLICATION).toString();
                                                ActivityStartDate = values.get(UtilsLocalDataBase.C_APPWATCH_DATESTART).toString();
                                                ActivityStartTime = values.get(UtilsLocalDataBase.C_APPWATCH_TIMESTART).toString();
                                                Log.d(TAG, "Last entry is null we should not start a new monitoring");
                                            }
                                        }
                                        else
                                        {
                                            Log.d(TAG, "Screen is still locked we don't start a new monitoring yet");
                                        }

                                    }

                                }

                            }

                        }
                    }
                }
            }
            else
            {
                CreateTrackApp.CreateAlarm(this, SettingsBetrack.DELTA_BTW_RECHECK_STUDY_STARTED);
            }
        }
        CreateTrackApp.SemTrackApp.release();
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

}
