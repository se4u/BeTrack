package com.app.uni.betrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.concurrent.Semaphore;

/**
 * Created by cevincent on 5/26/16.
 */
public class SettingsBetrack {


    //Start Configuration of optional modules in Betrack

    //If set to yes enable the support for GPS tracking
    static public boolean STUDY_ENABLE_GPS = false;

    //The flag below is only valid only from Android Nougat
    //If set to yes then the tracking is running even when the screen is off. If used drain the battery! But that's as well the only way to track screen on and screen off...
    static public boolean STUDY_ENABLE_CONTINUOUS_TRACKING = false;

    //End Configuration of Betrack


    //Colors should match the colors.xml file !!!
    /*<color name="colorPrimary">#26A69A</color>
    <color name="colorPrimaryDark">#303F9F</color>
    <color name="colorAccent">#FF4081</color>
    <color name="colorLightGrey">#ECEFF1</color>
    <color name="colorDarkGrey">#455A64</color>*/
    static final public String colorPrimary = "#1565C0";
    static final public String colorDarkGrey = "#90A4AE";
    static final public String colorLightGrey = "#ECEFF1";
    public static final Semaphore SemSettingsBetrack = new Semaphore(1, true);
    static final String C_STARTSTUDY_PID = "ParticipantID";
    static private final String TAG = "ConfigSettingsBetrack";
    //Server access
    static public String BROADCAST_CHECK_INTERNET = "com.app.uni.betrack.CHECK_INTERNET";
    static public String BROADCAST_TRIGGER_NOTIFICATION = "com.app.uni.betrack.TRIGGER_NOTIFICATION";
    static public String BROADCAST_START_TRACKING_NAME = "com.app.uni.betrack.START_TRACKING";
    static public String BROADCAST_ARG_MANUAL_START = "com.app.uni.betrack.BROADCAST_ARG_MANUAL_START";
    static public String STUDY_WEBSITE = "http://smartphonestudy.chenlab.psych.ubc.ca/TEST/android/";
    static public String STUDY_GETSTUDIESAVAILABLE = "BeTrackGetStudiesAvailable.php";
    static public String STUDY_GETAPPTOWATCH = "BeTrackGetAppToWatch.php?table_name=BetrackApp";
    static public String STUDY_POSTAPPWATCHED = "BeTrackPostAppWatch";
    static public String STUDY_POSTDAILYSTATUS = "BeTrackPostDailyStatus";
    static public String STUDY_POSTSTARTSTUDY = "BeTrackPostStartStudy";
    static public String STUDY_POSTENDSTUDY = "BeTrackPostEndStudy";
    static public String STUDY_POSTSLEEPSTATUS = "BeTrackPostSleepStatus";
    static public String STUDY_POSTPHONEUSAGEDATA = "BeTrackPostPhoneUsageData";
    static public String STUDY_POSTNOTIFICATIONRCVTIME = "BeTrackPostNotifRcv";
    static public String STUDY_POSTBLOBKEY = "BeTrackPostSessionKeys";
    static public int SERVER_TIMEOUT = 20000;
    static public int DELTA_BTW_RECHECK_STUDY_STARTED = 10000;
    static public int SAMPLING_RATE = 1000; //In ms
    static public int POSTDATA_SENDING_DELTA = 1000 * 60 * 60 * 1; //In ms (every 1 hour)
    static public int ID_NOTIFICATION_BETRACK = 1335;
    static public int ID_TRACKAPP = 1336;
    static public int ID_NOTIFICATION_SERVICE = 1337;
    static public String STUDY_PUBLIC_KEY = "public.pem";
    static public boolean STUDY_JUST_STARTED = false;

    static public int SCREEN_SWITCHED_OFF = 0;
    static public int SCREEN_SWITCHED_ON = 1;
    static public int SCREEN_USER_PRESENT = 2;
    static public int SCREEN_PHONE_SWITCHED_ON = 3;
    static public int SCREEN_PHONE_SWITCHED_OFF = 4;
    static public int SCREEN_BETRACK_STARTED_MANUALY = 5;
    static public int SCREEN_BETRACK_ERROR = 6;

    private Boolean StudyEnable;
    private Boolean StudyNotification;
    private Boolean EnableDataUsage;


    private SettingsBetrack()
    {}

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
            EnableDataUsage = prefs.getBoolean(mActivity.getString(R.string.pref_key_data_sync_enable_usage_3g), false);
            StudyNotification = prefs.getBoolean(mActivity.getString(R.string.pref_key_study_notification), true);
	    //Log.d(TAG, "StudyEnable: " + StudyEnable + " EnableDataUsage: " + EnableDataUsage + " StudyNotification: " + StudyNotification + " StudyNotificationTime: " + StudyNotificationTime);
            SemSettingsBetrack.release();
        } catch (Exception e) {

        }
    }

    private static class ConfigSettingsBetrackHolder
    {
        private final static SettingsBetrack instance = new SettingsBetrack();
    }

}
