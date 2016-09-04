package com.app.uni.betrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.concurrent.Semaphore;

/**
 * Created by cevincent on 5/26/16.
 */
public class SettingsBetrack {

    static private final String TAG = "ConfigSettingsBetrack";

    static public String SERVICE_TRACKING_NAME = "com.app.uni.betrack.IntentServiceTrackApp";
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

    static public int POSTDATA_SENDING_DELTA = 10000; //In ms
    static public int POSTDATA_SENDING_DELTA_FASTCHECK = 1000; //In ms

    static public int TRACKGPS_DELTA = 10000; //In ms

    static public int UPDATE_STATUS_STUDY_TIME = 60000;

    static public int NOTIFICATION_ID = 1;

    static public int JOBID_TRACKAPP = 1;
    static public int JOBID_POSTDATA = 2;
    static public int JOBID_TRACKGPS = 3;
    static public int JOBID_NOTIFICATION = 4;

    public static final Semaphore SemSettingsBetrack = new Semaphore(1, true);

    private Boolean StudyEnable;
    private Boolean StudyNotification;
    private String StudyNotificationTime;
    private Boolean EnableDataUsage;

    private SettingsBetrack()
    {}

    private static class ConfigSettingsBetrackHolder
    {
        private final static SettingsBetrack instance = new SettingsBetrack();
    }

    public static SettingsBetrack getInstance()
    {
        return ConfigSettingsBetrackHolder.instance;
    }

    public void Update(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        BuildPref(prefs, context);
    }

    public Boolean GetStudyEnable()
    {
        Boolean ReturnStudyEnable = false;
        try {
            SemSettingsBetrack.acquire();
            ReturnStudyEnable = StudyEnable;
            SemSettingsBetrack.release();
        } catch (Exception e) {
            ReturnStudyEnable = false;
        } finally {
            return ReturnStudyEnable;
        }
    }

    public Boolean GetStudyNotification()
    {
        Boolean ReturnStudyNotification = false;
        try {
            SemSettingsBetrack.acquire();
            ReturnStudyNotification = StudyNotification;
            SemSettingsBetrack.release();
        } catch (Exception e) {
            ReturnStudyNotification = false;
        } finally {
            return ReturnStudyNotification;
        }
    }

    public String GetStudyNotificationTime()
    {
        String ReturnStudyNotificationTime = null;
        try {
            SemSettingsBetrack.acquire();
            ReturnStudyNotificationTime = StudyNotificationTime;
            SemSettingsBetrack.release();
        } catch (Exception e) {
            ReturnStudyNotificationTime = null;
        } finally {
            return ReturnStudyNotificationTime;
        }
    }

    public Boolean GetEnableDataUsage()
    {
        Boolean ReturnEnableDataUsage = false;
        try {
            SemSettingsBetrack.acquire();
            ReturnEnableDataUsage = EnableDataUsage;
            SemSettingsBetrack.release();
        } catch (Exception e) {
            ReturnEnableDataUsage = false;
        } finally {
            return ReturnEnableDataUsage;
        }
    }

    private void BuildPref(SharedPreferences prefs, Context mActivity) {
        try {
            SemSettingsBetrack.acquire();
            //Update settings with value of preferences from the shared preference editor or default values
            StudyEnable = prefs.getBoolean(mActivity.getString(R.string.pref_key_study_enable), true);
            EnableDataUsage = prefs.getBoolean(mActivity.getString(R.string.pref_key_data_sync_enable_usage_3g), true);
            StudyNotification = prefs.getBoolean(mActivity.getString(R.string.pref_key_study_notification), true);
            StudyNotificationTime = prefs.getString(mActivity.getString(R.string.pref_key_study_notification_time), "20:00") + ":00";
            Log.d(TAG, "StudyEnable: " + StudyEnable + " EnableDataUsage: " + EnableDataUsage + " StudyNotification: " + StudyNotification + " StudyNotificationTime: " + StudyNotificationTime);
            SemSettingsBetrack.release();
        } catch (Exception e) {

        }
    }

}
