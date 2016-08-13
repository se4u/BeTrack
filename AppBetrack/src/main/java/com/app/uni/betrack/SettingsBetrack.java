package com.app.uni.betrack;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.concurrent.Semaphore;

/**
 * Created by cevincent on 5/26/16.
 */
public class SettingsBetrack {

    static private final String TAG = "SettingsBetrack";

    static public String SERVICE_TRACKING_NAME = "com.app.uni.betrack.TrackIntentService";
    static public String BROADCAST_START_TRACKING_NAME = "com.app.uni.betrack.START_TRACKING";
    static public String BROADCAST_CHECK_SCREEN_STATUS = "com.app.uni.betrack.CHECK_SCREEN_STATUS";

    static public String STUDY_WEBSITE = "http://www.ricphoto.fr/";

    static public String STUDY_GETSTUDIESAVAILABLE = "BeTrackGetStudiesAvailable.php";
    static public String STUDY_GETAPPTOWATCH = "BeTrackGetAppToWatch.php?table_name=TestPeriod_applications";
    static public String STUDY_POSTAPPWATCHED = "BeTrackPostAppWatch.php";
    static public String STUDY_POSTDAILYSTATUS = "BeTrackPostDailyStatus.php";

    static public int SERVER_TIMEOUT = 20000;
    static public int DELTA_BTW_RECHECK_STUDY_STARTED = 10000;
    static public int SAMPLING_RATE = 1000;
    static public int UPDATE_STATUS_STUDY_TIME = 60000;

    static public int NOTIFICATION_ID = 1;

    public static final Semaphore SemPreferenceUpdated = new Semaphore(1, true);
    public static boolean PreferenceUpdated = false;


    public Boolean StudyEnable;
    public Boolean StudyNotification;
    public String StudyNotificationTime;
    public Boolean EnableDataUsage;
    public SettingsBetrack(SharedPreferences prefs, Context mActivity) {
        BuildPref(prefs, mActivity);
    }

    public void UpdateSettingsBetrack(SharedPreferences prefs, Context mActivity) {
        BuildPref(prefs, mActivity);
    }

    private void BuildPref(SharedPreferences prefs, Context mActivity) {

        //Update settings with value of preferences from the shared preference editor or default values
        StudyEnable = prefs.getBoolean(mActivity.getString(R.string.pref_key_study_enable), true);
        EnableDataUsage = prefs.getBoolean(mActivity.getString(R.string.pref_key_data_sync_enable_usage_3g), true);
        StudyNotification = prefs.getBoolean(mActivity.getString(R.string.pref_key_study_notification), true);
        StudyNotificationTime = prefs.getString(mActivity.getString(R.string.pref_key_study_notification_time), "20:00") + ":00";
        Log.d(TAG, "StudyEnable: " + StudyEnable + " EnableDataUsage: " + EnableDataUsage + " StudyNotification: " + StudyNotification + " StudyNotificationTime: " + StudyNotificationTime);
        PreferenceUpdated = false;
    }

}
