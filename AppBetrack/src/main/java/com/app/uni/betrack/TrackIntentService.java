package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.IntentService;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
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

    private enum ConnectionState {
        NONE,
        WIFI,
        LTE,
        };

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.app.uni.betrack.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.app.uni.betrack.extra.PARAM2";
    static final String TAG = "UpdaterIntentService";

    public static String ActivityOnGoing = null;
    public static String ActivityStartDate = null;
    public static String ActivityStartTime = null;


    private SettingsBetrack ObjSettingsBetrack;

    private LocalDataBase localdatabase = new LocalDataBase(this);

    public LocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    public TrackIntentService() {
        super("TrackIntentService");
    }

    private         InfoStudy ContextInfoStudy = new InfoStudy();

    private ExecutorService es = Executors.newFixedThreadPool(1);
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

    private final void createNotification(){
        final NotificationManager mNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final Intent launchNotificationIntent = new Intent(this, BeTrackActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this,
                1, launchNotificationIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(this)
                .setWhen(System.currentTimeMillis())
                .setTicker(getResources().getString(R.string.notification_title))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(getResources().getString(R.string.notification_title))
                .setContentText(getResources().getString(R.string.notification_desc))
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNotification.notify(SettingsBetrack.NOTIFICATION_ID, builder.build());
        } else {
            mNotification.notify(SettingsBetrack.NOTIFICATION_ID, builder.getNotification());
        }

    }

    protected void onHandleIntent(Intent intent) {
        String topActivity = null;
        String ActivityStopDate = null;
        String ActualTime = null;
        String ActivityStopTime=null;
        String StudyOnGoingKey = InfoStudy.STUDY_STARTED;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        boolean StudyOnGoing;
        ConnectionState NetworkState;

        PostDataAvailable.localdatabase = this.AccesLocalDB();
        ScreenReceiver.localdatabase = this.AccesLocalDB();

        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

        //Read the preferences
        ObjSettingsBetrack = new SettingsBetrack(prefs, this);

        //Read the user ID
        String UserId = prefs.getString(InfoStudy.ID_USER, "NOID ?????");

        if (intent != null) {
            do {
                //Check if a study is going on
                StudyOnGoing = prefs.getBoolean(StudyOnGoingKey, false);
                if (true == StudyOnGoing) {

                    new SetupStudy(prefs, ContextInfoStudy);
                    Intent intentCheckScreenStatus = new Intent();

                    do {

                        try {

                            //Dummy read to make sure that we stay alive
                            //Log.d(TAG, "Dummy read to stay alive ");
                            //values.clear();
                            //values = AccesLocalDB().getOldestElementDb(LocalDataBase.TABLE_APPWATCH);

                            //Check if preferences have been updated
                            SettingsBetrack.SemPreferenceUpdated.acquire();
                            if (SettingsBetrack.PreferenceUpdated)
                            {
                                ObjSettingsBetrack.UpdateSettingsBetrack(prefs, this);
                            }
                            SettingsBetrack.SemPreferenceUpdated.release();

                            //Check if the participant still want to be a part of the study
                            if (!ObjSettingsBetrack.StudyEnable)
                            {
                                //We are out of the study
                                Thread.sleep(SettingsBetrack.UPDATE_STATUS_STUDY_TIME);
                                continue;
                            }

                            // We get usage stats for the last minute
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                topActivity = handleCheckActivity_FromKitkat(intent);
                            } else {
                                topActivity = handleCheckActivity(intent);
                            }

                            Log.d(TAG, "Foreground App " + topActivity);
                            if (ScreenReceiver.StateScreen.UNKNOWN == ScreenReceiver.ScreenState) {
                                intentCheckScreenStatus.setAction(SettingsBetrack.BROADCAST_CHECK_SCREEN_STATUS);
                                this.sendBroadcast(intentCheckScreenStatus);
                            }
                            //Check if we should fire a notification
                            ActualTime = shf.format(new Date());
                            //Log.d(TAG, "Actual time:" + ActualTime + " time when to trigger the notification:" + ObjSettingsBetrack.StudyNotificationTime);
                            if (ObjSettingsBetrack.StudyNotificationTime.equals(ActualTime) &&
                                    ObjSettingsBetrack.StudyNotification)
                            {
                                //Log.d(TAG, "Notification triggered");
                                createNotification();
                            }

                            //Check the status of the screen
                            //Check if that activity should be monitored
                            for(int i =0;i<ContextInfoStudy.ApplicationsToWatch.size();i++) {

                                //Log.d(TAG, "Application to watch " + ContextInfoStudy.ApplicationsToWatch.get(i));
                                //Log.d(TAG, "ActivityOnGoing " + ActivityOnGoing);
                                //Log.d(TAG, "topActivity " + topActivity);

                                if (null != ActivityOnGoing) {
                                    //An activity was watched and should not be watched anymore
                                    if ((null != topActivity) && (!ActivityOnGoing.equals(topActivity))) {

                                        if (ScreenReceiver.StateScreen.ON == ScreenReceiver.ScreenState) {

                                            //We save in the local database the informations about the study
                                            values.clear();
                                            values = AccesLocalDB().getOldestElementDb(LocalDataBase.TABLE_APPWATCH);
                                            try {
                                                ActivityStopDate = values.get(LocalDataBase.C_APPWATCH_DATESTOP).toString();
                                                Log.d(TAG, "End monitoring date: Should never happen the last entry is already filled up ???");
                                            } catch (Exception e) {
                                                //Save the stop date
                                                ActivityStopDate = sdf.format(new Date());
                                                //Save the stop time
                                                ActivityStopTime = shf.format(new Date());

                                                values.put(LocalDataBase.C_APPWATCH_DATESTOP, ActivityStopDate);
                                                values.put(LocalDataBase.C_APPWATCH_TIMESTOP, ActivityStopTime);

                                                this.AccesLocalDB().Update(values, values.getAsLong(LocalDataBase.C_APPWATCH_ID), LocalDataBase.TABLE_APPWATCH);

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
                                    values = AccesLocalDB().getOldestElementDb(LocalDataBase.TABLE_APPWATCH);
                                    if (0 != values.size()) {
                                        try {
                                            ActivityStopDate = values.get(LocalDataBase.C_APPWATCH_DATESTOP).toString();
                                            Log.d(TAG, "Check in case some monitoring was started but never stopped");
                                        } catch (Exception e) {
                                            //Save the stop date
                                            ActivityStopDate = sdf.format(new Date());
                                            //Save the stop time
                                            ActivityStopTime = shf.format(new Date());

                                            values.put(LocalDataBase.C_APPWATCH_DATESTOP, ActivityStopDate);
                                            values.put(LocalDataBase.C_APPWATCH_TIMESTOP, ActivityStopTime);

                                            this.AccesLocalDB().Update(values, values.getAsLong(LocalDataBase.C_APPWATCH_ID), LocalDataBase.TABLE_APPWATCH);

                                            //Reinitialize activity watched infos
                                            ActivityOnGoing = null;
                                            ActivityStartDate = null;
                                            ActivityStartTime = null;
                                            ActivityStopDate = null;
                                            ActivityStopTime = null;

                                            Log.d(TAG, "Finish last entry end monitoring date:" + ActivityStopDate + " time:" + ActivityStopTime);
                                        }
                                    }
                                }

                                //Log.d(TAG, "Check app foreground: " + topActivity + "app to monitor: " + ContextInfoStudy.ApplicationsToWatch.get(i) + " Screen state: " + ScreenReceiver.ScreenState);

                                if ((null != topActivity) && (null != ContextInfoStudy.ApplicationsToWatch.get(i))) {
                                    if (topActivity.toLowerCase().contains(ContextInfoStudy.ApplicationsToWatch.get(i).toLowerCase())) {

                                        //Log.d(TAG, "Status ActivityOnGoing: " + ActivityOnGoing);

                                        //A new activity to be watch
                                        if (null == ActivityOnGoing) {

                                            //Check the status of the screen
                                            if (ScreenReceiver.StateScreen.ON == ScreenReceiver.ScreenState) {
                                                KeyguardManager km = (KeyguardManager) getBaseContext().getSystemService(Context.KEYGUARD_SERVICE);
                                                boolean locked = km.inKeyguardRestrictedInputMode();

                                                if (!locked) {
                                                    values.clear();
                                                    values = AccesLocalDB().getOldestElementDb(LocalDataBase.TABLE_APPWATCH);
                                                    try {
                                                        if (0 != values.size()) {
                                                            ActivityStopDate = values.get(LocalDataBase.C_APPWATCH_DATESTOP).toString();
                                                            Log.d(TAG, "Last entry is not null we can start a new monitoring");
                                                        }
                                                        else {
                                                            Log.d(TAG, "New monitoring started");
                                                        }
                                                        ActivityOnGoing = topActivity;

                                                        //Save the date
                                                        ActivityStartDate = sdf.format(new Date());
                                                        //Save the start time
                                                        ActivityStartTime = shf.format(new Date());

                                                        ActivityStopDate = null;
                                                        ActivityStopTime = null;

                                                        values.clear();
                                                        values.put(LocalDataBase.C_APPWATCH_APPLICATION, ActivityOnGoing);
                                                        values.put(LocalDataBase.C_APPWATCH_DATESTART, ActivityStartDate);
                                                        values.put(LocalDataBase.C_APPWATCH_DATESTOP, ActivityStopDate);
                                                        values.put(LocalDataBase.C_APPWATCH_TIMESTART, ActivityStartTime);
                                                        values.put(LocalDataBase.C_APPWATCH_TIMESTOP, ActivityStopTime);
                                                        this.AccesLocalDB().insertOrIgnore(values, LocalDataBase.TABLE_APPWATCH);

                                                        Log.d(TAG, "IdUser: " + UserId + "Start monitoring: " + topActivity + " date:" + ActivityStartDate + " time:" + ActivityStartTime);

                                                    } catch (Exception e) {

                                                        ActivityOnGoing = values.get(LocalDataBase.C_APPWATCH_APPLICATION).toString();
                                                        ActivityStartDate = values.get(LocalDataBase.C_APPWATCH_DATESTART).toString();
                                                        ActivityStartTime = values.get(LocalDataBase.C_APPWATCH_TIMESTART).toString();
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
                            //Check if the delta between 2 updates of the server was long enough that we can start a new

                            //Check if there is a data connection
                            NetworkState = haveNetworkConnection();

                            // To transfer the data either we have access to a WIFI network or
                            // we have are allowed to use the 3G/LTE
                            if ((ConnectionState.WIFI == NetworkState) ||
                                    ((ConnectionState.LTE == NetworkState) && (ObjSettingsBetrack.EnableDataUsage)))
                            {
                                if (null == InfoStudy.IdUser ) {
                                    InfoStudy.IdUser = prefs.getString(InfoStudy.ID_USER, "No user ID !");
                                    //Log.d(TAG, "IdUser not available we read it from the database: " + InfoStudy.IdUser);
                                }
                                else
                                {
                                    //Log.d(TAG, "IdUser available: " + InfoStudy.IdUser);
                                }
                                //Check if we are not already transferring the data
                                if (PostDataAvailable.SemUpdateServer.tryAcquire()) {
                                    es.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Transfer all the data available to the distant server
                                            PostDataAvailable.Start();
                                        }
                                    });
                                }
                                else
                                {
                                    Log.d(TAG, "tryacquire SemUpdateServer failed");
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

    private ConnectionState haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectionState NetworkState = ConnectionState.NONE;

        try {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    Log.d(TAG, "haveNetworkConnection: WIFI");
                    NetworkState = ConnectionState.WIFI;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    Log.d(TAG, "haveNetworkConnection: LTE/3G");
                    NetworkState = ConnectionState.LTE;
                }
            }else {
                NetworkInfo[] netInfo = cm.getAllNetworkInfo();
                for (NetworkInfo ni : netInfo) {
                    if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                        if (ni.isConnected()){
                            Log.d(TAG, "haveNetworkConnection: WIFI");
                            NetworkState = ConnectionState.WIFI;
                        }

                    if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                        if (ni.isConnected()) {
                            Log.d(TAG, "haveNetworkConnection: LTE/3G");
                            NetworkState = ConnectionState.LTE;
                        }
                }
            }
        }
        finally {
            if (ConnectionState.NONE == NetworkState) {
                Log.d(TAG, "haveNetworkConnection: nope");
            }
            return NetworkState;
        }
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
