package com.app.uni.betrack;

/**
 * Created by cevincent on 5/26/16.
 */
public class SettingsBetrack {

    static public String SERVICE_TRACKING_NAME = "com.app.uni.betrack.TrackIntentService";
    static public String BROADCAST_START_TRACKING_NAME = "com.app.uni.betrack.START_TRACKING";
    static public String BROADCAST_CHECK_SCREEN_STATUS = "com.app.uni.betrack.CHECK_SCREEN_STATUS";

    static public String STUDY_WEBSITE = "http://www.ricphoto.fr/";

    static public String STUDY_GETSTUDIESAVAILABLE = "BeTrackGetStudiesAvailable.php";
    static public String STUDY_GETAPPTOWATCH = "BeTrackGetAppToWatch.php?table_name=TestPeriod_applications";
    static public String STUDY_POSTAPPWATCHED = "BeTrackPostAppWatch.php";
    static public String STUDY_POSTDAILYSTATUS = "BeTrackPostDailyStatus.php";

    static public String STUDY_ENABLE = "study_enable";
    static public String STUDY_NOTIFICATION = "study_notification";
    static public String STUDY_NOTIFICATION_TIME = "study_notification_time";
    static public String ENABLE_DATA_USAGE = "enable_data_usage";
    static public String FREQ_UPDATE_SERVER = "frequency_update_server";

    static public int SERVER_TIMEOUT = 20000;
    static public int DELTA_BTW_RECHECK_STUDY_STARTED = 120000;
    static public int SAMPLING_RATE = 1000;

    static public int NOTIFICATION_ID = 1;

    public Boolean StudyEnable;
    public Boolean StudyNotification;
    public String StudyNotificationTime;
    public Boolean EnableDataUsage;
    public int FrequencyUpdateServer;
}
