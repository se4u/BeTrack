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
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;



public class IntentServiceTrackApp extends IntentService {


    static final String TAG = "IntentServiceTrackApp";
    static final private int NBR_SAMPLE_REACHED = 500;
    public static String ActivityOnGoing = null;
    public static String ActivityStartDate = null;
    public static String ActivityStartTime = null;
    public static String ActivityStopDate = null;
    public static String ActivityStopTime=null;
    static public long baseTime = 0;
    static private double averagePeriodicity = 0;
    static private double stdDevPeriodicity = 0;
    Handler mHandler;
    private SettingsBetrack ObjSettingsBetrack = null;
    private SettingsStudy ObjSettingsStudy = null;
    private UtilsLocalDataBase localdatabase = null;
    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    private UtilsScreenState screenstate = null;
    private UtilsScreenState AccessScreenState() {return screenstate; }

    public IntentServiceTrackApp() {

        super("IntentServiceTrackApp");
        mHandler = new Handler();
    }

    private static BigDecimal truncateDecimal(double x,int numberofDecimals)
    {
        if ( x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
        }
    }



    protected void onHandleIntent(Intent intent) {
        String topActivity;
        boolean StudyOnGoing;

        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

        if (null == localdatabase) {
            localdatabase =  new UtilsLocalDataBase(this);
        }

        if (null == screenstate) {
            screenstate =  new UtilsScreenState(this);
        }

        if (null == ObjSettingsBetrack) {
            //Read the preferences
            ObjSettingsBetrack = SettingsBetrack.getInstance();
            ObjSettingsBetrack.Update(this);
        }

        if (null == ObjSettingsStudy)  {
            ObjSettingsStudy = SettingsStudy.getInstance(this);
        }

        if (intent != null) {

            //Compute what's the average refresh frequency of the service and the standard deviation
            if ((!ObjSettingsStudy.getAccuracyComputed())
                    && ((UtilsScreenState.StateScreen.ON == AccessScreenState().UtilsGetSavedScreenState())
                        || (UtilsScreenState.StateScreen.UNLOCKED == AccessScreenState().UtilsGetSavedScreenState()))
                    )
            {

                Log.d(TAG, "Compute accuracy periodicity basetime: " + baseTime);
                if (baseTime == 0) {
                    baseTime = System.currentTimeMillis();
                }

                if (((System.currentTimeMillis() - baseTime) / 1000) > 0 ) {
                    Long periodicity = System.currentTimeMillis() - baseTime;
                    Log.d(TAG, "arrayperiodicity[" + ObjSettingsStudy.getArrayPeriodicity().size() +"] = " + periodicity);
                    ObjSettingsStudy.setArrayPeriodicity(periodicity);
                    baseTime = System.currentTimeMillis();
                }

                if (ObjSettingsStudy.getArrayPeriodicity() != null) {
                    if (ObjSettingsStudy.getArrayPeriodicity().size() >= NBR_SAMPLE_REACHED) {
                        //Compute the average
                        for (int i = 0; i < NBR_SAMPLE_REACHED; i++) {
                            averagePeriodicity += ObjSettingsStudy.getArrayPeriodicity().get(i);
                        }

                        averagePeriodicity /= NBR_SAMPLE_REACHED;

                        //Compute the standard deviation
                        for (int i = 0; i < NBR_SAMPLE_REACHED; i++) {
                            stdDevPeriodicity += Math.pow((ObjSettingsStudy.getArrayPeriodicity().get(i) - averagePeriodicity), 2);
                        }
                        stdDevPeriodicity /= (NBR_SAMPLE_REACHED - 1);
                        stdDevPeriodicity = Math.sqrt(stdDevPeriodicity);

                        Log.d(TAG, "averageperiodicity:" + truncateDecimal(averagePeriodicity, 2));
                        Log.d(TAG, "stdDevperiodicity:" + truncateDecimal(stdDevPeriodicity, 2));

                        ObjSettingsStudy.setAveragePeriodicity(String.valueOf(truncateDecimal(averagePeriodicity, 2)));
                        ObjSettingsStudy.setStandardDeviation(String.valueOf(truncateDecimal(stdDevPeriodicity, 2)));

                        ObjSettingsStudy.setAccuracyComputed(true);
                    }
                }
            }

            //Check if a study is going on
            StudyOnGoing = ObjSettingsBetrack.GetStudyEnable();
            if (true == StudyOnGoing) {

                Intent intentCheckScreenStatus = new Intent();

                if (ObjSettingsBetrack.GetStudyEnable())
                {

                    // We get usage stats for the last minute
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        topActivity = handleCheckActivity_FromLollipop(intent);
                    } else {
                        topActivity = handleCheckActivity(intent);
                    }

                    try {
                        ActivityStopDate = values.get(UtilsLocalDataBase.C_APPWATCH_DATESTOP).toString();
                    } catch (Exception e) {
                        try {
                            ActivityOnGoing = values.get(UtilsLocalDataBase.C_APPWATCH_APPLICATION).toString();
                        }
                        catch (Exception f) {

                        }
                    }

                    Log.d(TAG, "Foreground App " + topActivity + " ActivityOnGoing " + ActivityOnGoing);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        AccessScreenState().UtilsUpdateScreenStateFromHardware(this);

                        //Save the date
                        ActivityStartDate = sdf.format(new Date());
                        //Save the start time
                        ActivityStartTime = shf.format(new Date());


                        if (ObjSettingsStudy.getBetrackScreenState() != AccessScreenState().UtilsGetSavedScreenState()) {
                            ObjSettingsStudy.setBetrackScreenState(AccessScreenState().UtilsGetSavedScreenState());
                            values.clear();
                            if ((AccessScreenState().UtilsGetSavedScreenState() == UtilsScreenState.StateScreen.ON)
                                    || (AccessScreenState().UtilsGetSavedScreenState() == UtilsScreenState.StateScreen.UNLOCKED)) {

                                try {
                                    SettingsStudy.SemScreenOn.acquire();
                                    if (SettingsStudy.getStartScreenOn() == 0) {
                                        SettingsStudy.setStartScreenOn(System.currentTimeMillis());
                                        Log.d(TAG, "Screen ON saved " + System.currentTimeMillis());
                                    }

                                } catch (Exception eScreenOn) {
                                }
                                finally {
                                    SettingsStudy.SemScreenOn.release();
                                }

                                if (ObjSettingsStudy.getBetrackScreenState() == UtilsScreenState.StateScreen.UNLOCKED) {
                                    Log.d(TAG, "SCREEN_USER_PRESENT saved in database " + ActivityStartDate + " " + ActivityStartTime);
                                    values.put(UtilsLocalDataBase.C_PHONE_USAGE_STATE, SettingsBetrack.SCREEN_USER_PRESENT);
                                } else {
                                    Log.d(TAG, "SCREEN_SWITCHED_ON saved in database " + ActivityStartDate + " " + ActivityStartTime);
                                    values.put(UtilsLocalDataBase.C_PHONE_USAGE_STATE, SettingsBetrack.SCREEN_SWITCHED_ON);
                                }



                                long DeltaLastTransfer = System.currentTimeMillis() - ObjSettingsStudy.getTimeLastTransfer();

                                if (DeltaLastTransfer >= SettingsBetrack.POSTDATA_SENDING_DELTA)  {
                                    Log.d(TAG, "More than one hour without transfering any data");
                                    Intent msgIntent = new Intent(this, IntentServicePostData.class);
                                    //Start the service for sending the data to the remote server
                                    this.startService(msgIntent);
                                }

                                values.put(UtilsLocalDataBase.C_PHONE_USAGE_DATE, ActivityStartDate);
                                values.put(UtilsLocalDataBase.C_PHONE_USAGE_TIME, ActivityStartTime);
                                try {
                                    AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_PHONE_USAGE);

                                } catch (Exception f) {
                                    Log.d(TAG, "Nothing to update in the database");
                                }
                            }
                            else
                            {
                                baseTime = 0;
                                values.put(UtilsLocalDataBase.C_PHONE_USAGE_STATE, SettingsBetrack.SCREEN_SWITCHED_OFF);
                                Log.d(TAG, "SCREEN_SWITCHED_OFF saved in database " + ActivityStartDate + " " + ActivityStartTime);

                                values.put(UtilsLocalDataBase.C_PHONE_USAGE_DATE, ActivityStartDate);
                                values.put(UtilsLocalDataBase.C_PHONE_USAGE_TIME, ActivityStartTime);


                                try {
                                    SettingsStudy.SemScreenOn.acquire();
                                    long ScreenOn = SettingsStudy.getDurationScreenOn();
                                    Log.d(TAG, "Duration Screen On:" + ScreenOn);
                                    Log.d(TAG, "Duration last screen On:" + System.currentTimeMillis() + "-" +      SettingsStudy.getStartScreenOn() + "/1000= " + (System.currentTimeMillis() - SettingsStudy.getStartScreenOn())/1000);
                                    ScreenOn += (System.currentTimeMillis() - SettingsStudy.getStartScreenOn())/1000;
                                    Log.d(TAG, "New duration screen on:" + ScreenOn);
                                    SettingsStudy.setDurationScreenOn(ScreenOn);
                                    SettingsStudy.setStartScreenOn(0);
                                } catch (Exception eScreenOn) {
                                }
                                finally {
                                    SettingsStudy.SemScreenOn.release();
                                }


                                try {
                                    AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_PHONE_USAGE);

                                } catch (Exception f) {
                                    Log.d(TAG, "Nothing to update in the database");
                                }

                                values.clear();
                                values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_APPWATCH, false);
                                try {
                                    ActivityStopDate = values.get(UtilsLocalDataBase.C_APPWATCH_DATESTOP).toString();
                                    Log.d(TAG, "End monitoring date: nothing to save");
                                } catch (Exception e) {
                                    if (null != values) {
                                        //Save the stop date
                                        ActivityStopDate = sdf.format(new Date());
                                        //Save the stop time
                                        ActivityStopTime = shf.format(new Date());

                                        try {
                                            Log.d(TAG, "Sem1 try acquire");
                                            SettingsStudy.SemAppWatchMonitor.acquire();
                                            Log.d(TAG, "Sem1 acquired");

                                            if (SettingsStudy.getAppWatchId() != -1) {
                                                int TimeWatched = (int) ((System.currentTimeMillis() - SettingsStudy.getAppWatchStartTime()) / 1000);
                                                Log.d(TAG, "TimeWatched: " + TimeWatched + " AppWatchId: " + ObjSettingsStudy.getApplicationsToWatch().get(SettingsStudy.getAppWatchId()));
                                                ObjSettingsStudy.setAppTimeWatched(SettingsStudy.getAppWatchId(), ObjSettingsStudy.getApplicationsToWatch().size(), TimeWatched);
                                                SettingsStudy.setAppWatchId(-1);
                                            }

                                        } catch (Exception eWatchId) {
                                        }
                                        finally {
                                            SettingsStudy.SemAppWatchMonitor.release();
                                            Log.d(TAG, "Sem1 try released");
                                        }

                                        values.put(UtilsLocalDataBase.C_APPWATCH_DATESTOP, ActivityStopDate);
                                        values.put(UtilsLocalDataBase.C_APPWATCH_TIMESTOP, ActivityStopTime);
                                        try {
                                            this.AccesLocalDB().Update(values, values.getAsLong(UtilsLocalDataBase.C_APPWATCH_ID), UtilsLocalDataBase.TABLE_APPWATCH);
                                        } catch (Exception f) {
                                            Log.d(TAG, "Nothing to update in the database");
                                        }
                                        ActivityOnGoing = null;
                                        ActivityStartDate = null;
                                        ActivityStartTime = null;
                                        ActivityStopDate = null;
                                        ActivityStopTime = null;

                                        Log.d(TAG, "End monitoring date:" + ActivityStopDate + " time:" + ActivityStopTime);
                                    }
                                }

                                if ((SettingsBetrack.STUDY_ENABLE_CONTINUOUS_TRACKING == false) || (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N)) {
                                    CreateTrackApp.StopAlarm(getBaseContext());
                                }
                            }
                        }
                    } else {
                        if (UtilsScreenState.StateScreen.UNKNOWN == AccessScreenState().UtilsGetSavedScreenState()) {
                            AccessScreenState().UtilsUpdateScreenStateFromHardware(this);
                            ObjSettingsStudy.setBetrackScreenState(AccessScreenState().UtilsGetSavedScreenState());
                        }
                    }
                    //Check the status of the screen
                    //Check if that activity should be monitored
                    for(int i =0;i<ObjSettingsStudy.getApplicationsToWatch().size();i++) {

                        //Log.d(TAG, "Application to watch " + ObjSettingsStudy.getApplicationsToWatch().get(i));
                        //Log.d(TAG, "ActivityOnGoing " + ActivityOnGoing);
                        //Log.d(TAG, "topActivity " + topActivity);

                        if (null != ActivityOnGoing) {
                            //An activity was watched and should not be watched anymore
                            if ((null != topActivity) && (!topActivity.equals("android")) && (!topActivity.equals("com.android.systemui")) && (!ActivityOnGoing.equals(topActivity))) {
                                if ((AccessScreenState().UtilsGetSavedScreenState() == UtilsScreenState.StateScreen.ON)
                                        || (AccessScreenState().UtilsGetSavedScreenState() == UtilsScreenState.StateScreen.UNLOCKED)) {

                                    //We save in the local database the information about the study
                                    values.clear();
                                    values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_APPWATCH, false);
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
                                        try {
                                            Log.d(TAG, "Sem2 try acquire");
                                            SettingsStudy.SemAppWatchMonitor.acquire();
                                            Log.d(TAG, "Sem2 acquired");

                                            if (SettingsStudy.getAppWatchId() != -1) {
                                                int TimeWatched = (int) ((System.currentTimeMillis() - SettingsStudy.getAppWatchStartTime()) / 1000);
                                                Log.d(TAG, "TimeWatched: " + TimeWatched + " AppWatchId: " + ObjSettingsStudy.getApplicationsToWatch().get(SettingsStudy.getAppWatchId()));
                                                ObjSettingsStudy.setAppTimeWatched(SettingsStudy.getAppWatchId(), ObjSettingsStudy.getApplicationsToWatch().size(), TimeWatched);
                                                SettingsStudy.setAppWatchId(-1);
                                            }

                                        } catch (Exception eWatchId) {}
                                        finally {
                                            SettingsStudy.SemAppWatchMonitor.release();
                                            Log.d(TAG, "Sem2 try released");
                                        }


                                        this.AccesLocalDB().Update(values, values.getAsLong(UtilsLocalDataBase.C_APPWATCH_ID), UtilsLocalDataBase.TABLE_APPWATCH);
                                        //mHandler.post(new UtilsDisplayToast(this, "Betrack: Stop 1 monitoring date: " + ActivityStopDate + " time: " + ActivityStopTime));

                                        Log.d(TAG, "End monitoring date:" + ActivityStopDate + " time:" + ActivityStopTime);

                                        //Reinitialize activity watched infos
                                        ActivityOnGoing = null;
                                        ActivityStartDate = null;
                                        ActivityStartTime = null;
                                        ActivityStopDate = null;
                                        ActivityStopTime = null;
                                        break;

                                    }
                                }
                            }
                        }
                        else {
                            values.clear();
                            values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_APPWATCH, false);
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

                                    try {
                                        Log.d(TAG, "Sem3 try acquire");
                                        SettingsStudy.SemAppWatchMonitor.acquire();
                                        Log.d(TAG, "Sem3 acquired");

                                        if (SettingsStudy.getAppWatchId() != -1) {
                                            int TimeWatched = (int) ((System.currentTimeMillis() - SettingsStudy.getAppWatchStartTime()) / 1000);
                                            Log.d(TAG, "TimeWatched: " + TimeWatched + " AppWatchId: " + ObjSettingsStudy.getApplicationsToWatch().get(SettingsStudy.getAppWatchId()));
                                            ObjSettingsStudy.setAppTimeWatched(SettingsStudy.getAppWatchId(), ObjSettingsStudy.getApplicationsToWatch().size(), TimeWatched);
                                            SettingsStudy.setAppWatchId(-1);
                                        }

                                    } catch (Exception eWatchId) {}
                                    finally {
                                        SettingsStudy.SemAppWatchMonitor.release();
                                        Log.d(TAG, "Sem3 try released");
                                    }

                                    this.AccesLocalDB().Update(values, values.getAsLong(UtilsLocalDataBase.C_APPWATCH_ID), UtilsLocalDataBase.TABLE_APPWATCH);
                                    //mHandler.post(new UtilsDisplayToast(this, "Betrack: Stop 2 monitoring date: " + ActivityStopDate + " time: " + ActivityStopTime));

                                    Log.d(TAG, "Finish last entry end monitoring date:" + ActivityStopDate + " time:" + ActivityStopTime);
                                    //Reinitialize activity watched infos
                                    ActivityOnGoing = null;
                                    ActivityStartDate = null;
                                    ActivityStartTime = null;
                                    ActivityStopDate = null;
                                    ActivityStopTime = null;
                                    break;
                                }
                            }
                        }

                        //Log.d(TAG, "Check app foreground: " + topActivity + " app to monitor: " + ObjSettingsStudy.getApplicationsToWatch().get(i) + " Screen state: " + ReceiverScreen.ScreenState);

                        if ((null != topActivity) && (null != ObjSettingsStudy.getApplicationsToWatch().get(i))) {
                            if (topActivity.toLowerCase().contains(ObjSettingsStudy.getApplicationsToWatch().get(i).toLowerCase())) {

                                //Log.d(TAG, "Status ActivityOnGoing: " + ActivityOnGoing);

                                //A new activity to be watch
                                if (null == ActivityOnGoing) {
                                    //Check the status of the screen
                                    if (ObjSettingsStudy.getBetrackScreenState() == UtilsScreenState.StateScreen.UNLOCKED) {
                                        values.clear();
                                        values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_APPWATCH, false);
                                        try {
                                            if (0 != values.size()) {
                                                ActivityStopDate = values.get(UtilsLocalDataBase.C_APPWATCH_DATESTOP).toString();
                                                Log.d(TAG, "Last entry is not null we can start a new monitoring");
                                            }
                                            else {
                                                Log.d(TAG, "New monitoring started");
                                            }


                                            ActivityOnGoing = topActivity;
                                            Log.d(TAG, "Sem4 try acquire");
                                            SettingsStudy.SemAppWatchMonitor.acquire();
                                            Log.d(TAG, "Sem4 acquired");
                                            SettingsStudy.setAppWatchStartTime(System.currentTimeMillis());
                                            SettingsStudy.setAppWatchId(i);
                                            SettingsStudy.SemAppWatchMonitor.release();
                                            Log.d(TAG, "Sem4 try released");
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
                                            //mHandler.post(new UtilsDisplayToast(this, "Betrack: Start monitoring: " + topActivity + " date:" + ActivityStartDate + " time:" + ActivityStartTime));

                                        } catch (Exception e) {

                                            ActivityOnGoing = values.get(UtilsLocalDataBase.C_APPWATCH_APPLICATION).toString();
                                            ActivityStartDate = values.get(UtilsLocalDataBase.C_APPWATCH_DATESTART).toString();
                                            ActivityStartTime = values.get(UtilsLocalDataBase.C_APPWATCH_TIMESTART).toString();
                                            Log.d(TAG, "Last entry is null we should not start a new monitoring");
                                        }
                                        finally {
                                            break;
                                        }
                                    }
                                    else
                                    {
                                        if (ObjSettingsStudy.getBetrackScreenState() == UtilsScreenState.StateScreen.ON) {
                                            Log.d(TAG, "Screen is still locked we don't start a new monitoring yet");
                                        } else if (ObjSettingsStudy.getBetrackScreenState() == UtilsScreenState.StateScreen.OFF) {
                                            Log.d(TAG, "Screen is off we don't start a new monitoring yet");
                                        } else {
                                            Log.d(TAG, "Screen is in an unknown state, that should never happen");
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) private String handleCheckActivity_FromLollipop(Intent intent)
    {
        // result
        String topActivity = null;
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - SettingsBetrack.SAMPLING_RATE * 60;
        List<UsageStats> stats;

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        try {
            stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime);
        } catch (Exception f) {
            stats = null;
        }


        // Sort the stats by the last time used
        if (stats != null) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : stats) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                //Log.d(TAG, "App " + usageStats.getPackageName() + " Time " + usageStats.getLastTimeUsed() );
                //Log.d(TAG, "App" + usageStats.getPackageName() + "Time in foreground " + usageStats.getTotalTimeInForeground());
            }
            if (mySortedMap != null && !mySortedMap.isEmpty()) {
                topActivity = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            }
        }

        return topActivity;
    }

}
