package com.app.uni.betrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.concurrent.Semaphore;

/**
 * Created by cevincent on 5/26/16.
 */
public class SettingsBetrack {

    static private final String TAG = "ConfigSettingsBetrack";

    //Colors should match the colors.xml file !!!
    /*<color name="colorPrimary">#26A69A</color>
    <color name="colorPrimaryDark">#303F9F</color>
    <color name="colorAccent">#FF4081</color>
    <color name="colorLightGrey">#ECEFF1</color>
    <color name="colorDarkGrey">#455A64</color>*/
    static final public String colorPrimary = "#1565C0";
    static final public String colorDarkGrey = "#90A4AE";

    //Server access
    static public String BROADCAST_CHECK_INTERNET = "com.app.uni.betrack.CHECK_INTERNET";
    static public String BROADCAST_TRIGGER_NOTIFICATION = "com.app.uni.betrack.TRIGGER_NOTIFICATION";
    static public String BROADCAST_START_TRACKING_NAME = "com.app.uni.betrack.START_TRACKING";
    static public String BROADCAST_ARG_MANUAL_START = "com.app.uni.betrack.BROADCAST_ARG_MANUAL_START";

    static public String BROADCAST_CHECK_SCREEN_STATUS = "com.app.uni.betrack.CHECK_SCREEN_STATUS";

    static public String STUDY_WEBSITE = "http://www.ricphoto.fr/betrack/android/";

    static public String STUDY_GETSTUDIESAVAILABLE = "BeTrackGetStudiesAvailable.php";
    static public String STUDY_GETAPPTOWATCH = "BeTrackGetAppToWatch.php?table_name=BetrackApp";


    static public String STUDY_POSTAPPWATCHED = "BeTrackPostAppWatch";
    static public String STUDY_POSTDAILYSTATUS = "BeTrackPostDailyStatus";
    static public String STUDY_POSTSTARTSTUDY = "BeTrackPostStartStudy";
    static public String STUDY_POSTENDSTUDY = "BeTrackPostEndStudy";
    static public String STUDY_POSTGPSDATA = "BeTrackPostGPSData";
    static public String STUDY_POSTPHONEUSAGEDATA = "BeTrackPostPhoneUsageData";
    static public String STUDY_POSTBLOBKEY = "BeTrackPostSessionKeys";

    static final String C_STARTSTUDY_PID = "ParticipantID";

    static public int SERVER_TIMEOUT = 20000;
    static public int DELTA_BTW_RECHECK_STUDY_STARTED = 10000;

    static public int SAMPLING_RATE = 1000; //In ms
    static public int SAMPLING_RATE_SCREEN_OFF = 2 * 60 * 1000;

    static public int POSTDATA_SENDING_DELTA = 1000 * 60 * 60 * 2; //In ms (every 2 hours)

    static public int TRACKGPS_DELTA = 1000  * 60 * 60 * 1; //In ms (every 1 hour)

    static public int UPDATE_STATUS_STUDY_TIME = 60000;

    static public int ID_NOTIFICATION_BETRACK = 1335;
    static public int ID_TRACKAPP = 1336;
    static public int ID_NOTIFICATION_SERVICE = 1337;

    static public int GPSMAXTIMEOUT = 30000;
    static public int GPSDECTIMEOUT = 5000;
    static public int GPSMINTIMEOUT = 5000;

    static public String STUDY_PUBLIC_KEY = "public.pem";

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
            //Log.d(TAG, "StudyEnable: " + StudyEnable + " EnableDataUsage: " + EnableDataUsage + " StudyNotification: " + StudyNotification + " StudyNotificationTime: " + StudyNotificationTime);
            SemSettingsBetrack.release();
        } catch (Exception e) {

        }
    }

}
