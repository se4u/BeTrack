package com.app.uni.betrack;

/**
 * Created by cevincent on 5/26/16.
 */
public class SettingsBetrack {

    static public String STUDY_WEBSITE = "http://www.ricphoto.fr/";

    static public String STUDY_ENABLE = "study_enable";
    static public String STUDY_NOTIFICATION = "study_notification";
    static public String STUDY_NOTIFICATION_TIME = "study_notification_time";
    static public String ENABLE_DATA_USAGE = "enable_data_usage";
    static public String FREQ_UPDATE_SERVER = "frequency_update_server";

    static public int SERVER_TIMEOUT = 10000;
    static public int DELTA_BTW_RECHECK_STUDY_STARTED = 120000;
    static public int SAMPLING_RATE = 1000;

    public Boolean StudyEnable;
    public Boolean StudyNotification;
    public String StudyNotificationTime;
    public Boolean EnableDataUsage;
    public int FrequencyUpdateServer;
}
