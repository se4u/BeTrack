package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;

/**
 * Created by cevincent on 4/24/16.
 */
public class SettingsStudy {

    public static final Semaphore SemSettingsStudy = new Semaphore(1, true);
    public static final Semaphore SemAppWatchMonitor = new Semaphore(1, true);
    public static final Semaphore SemScreenOn = new Semaphore(1, true);

    static final String TAG = "SettingsStudy";
    static private final String STUDY_BETRACK_SCREENSTATE = "BetrackScreenState";
    static private final String STUDY_STARTED = "study_started";
    static private final String START_SURVEY_DONE = "start_survey_done";
    static private final String SETUP_BETRACK_DONE = "setup_betrack_done";
    static private final String END_SURVEY_DONE = "end_survey_done";
    static private final String END_SURVEY_TRANSFERRED = "end_survey_transferred";
    static private final String ARRAYPERIODICITY = "arrayPeriodicity";
    static private final String APP_NAME_TO_WATCH = "AppNameToWatch";
    static private final String APP_TIME_WATCHED = "AppTimeWatched";
    static private final String STUDY_ID = "StudyId";
    static private final String STUDY_NAME = "StudyName";
    static private final String STUDY_DESCRIPTION = "StudyDescription";
    static private final String STUDY_VERSIONAPP = "StudyVersionApp";
    static private final String STUDY_DURATION = "StudyDuration";
    static private final String STUDY_PUBLICKEY = "StudyPublicKey";
    static private final String STUDY_CONTACTEMAIL = "StudyContactEmail";
    static private final String USER_ID = "UserId";
    static private final String USER_ID_CYPHER = "UserIdCypher";
    static private final String STUDY_DAILY_SURVEY_DONE = "DailySurveyDone";
    static private final String STUDY_STARTDATE_SURVEY = "StartDateSurvey";
    static private final String STUDY_NBR_OF_NOTIFICATION_TO_DO = "NumberOfNotificationDone";
    static private final String STUDY_TIME_LAST_TRANSFER = "TimeLastTransfer";
    static private final String APP_WATCH_START_TIME = "AppWatchStartTime";
    static private final String APP_WATCH_ID = "AppWatchId";
    static private final String STUDY_TIME_NEXT_NOTIFICATION = "TimeNextNotification";
    static private final String STUDY_ACCURACY_COMPUTED = "AccuracyComputed";
    static private final String STUDY_AVERAGE_PERIODICITY = "AveragePeriodicity";
    static private final String STUDY_STD_DEVIATION = "StandardDeviation";
    static private final String STUDY_BETRACK_KILLED = "BetrackKilled";
    static private final String DURATION_SCREEN_ON = "DurationScreenOn";
    static private final String START_SCREEN_ON = "StartScreenOn";
    static private final String LAST_DAY_STUDY = "LastDayStudy";

    static public UtilsCryptoAES.SecretKeys SessionKey;
    static SharedPreferences.Editor editor;
    static private int AppWatchId = -1;
    static private UtilsScreenState.StateScreen ScreenState = UtilsScreenState.StateScreen.UNKNOWN;
    static private Boolean StudyStarted; //A study is started
    static private Boolean SetupBetrackDone; //The phone has be set up to be used by Betrack
    static private Boolean StartSurveyDone; //Survey for starting the study has been filled up
    static private Boolean EndSurveyDone; //Survey for ending the study has been filled up
    static private EndStudyTranferState EndSurveyTransferred; //Set to true when trhe end survey has been successfully transferred
    static private String ApplicationsTimeWatched;
    static private String StudyId;
    static private String StudyName;
    static private String StudyDescription;
    static private String StudyVersionApp;
    static private int StudyDuration;
    static private String StudyPublicKey;
    static private String StudyContactEmail;
    static private String IdUser;
    static private String IdUserCypher;
    static private Boolean DailySurveyDone;
    static private String StartDateSurvey;
    static private int NbrOfNotificationToDo;
    static private long TimeLastTransfer;
    static private long TimeLastGPS;
    static private long AppWatchStartTime;
    static private long TimeNextNotification;
    static private Boolean AccuracyComputed;
    static private String AveragePeriodicity;
    static private String StandardDeviation;
    static private Boolean BetrackKilled;
    static private long DurationScreenOn;
    static private long StartScreenOn;
    static private LastDayStudyState LastDayStudy;

    static private Context mContext = null;
    Set<String> ArrayPeriodicityHs;
    Set<String> ApplicationsToWatchHs;
    SharedPreferences prefs;
    private SettingsBetrack ObjSettingsBetrack = null;
    private UtilsLocalDataBase localdatabase = null;
    private SettingsStudy()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = prefs.edit();

        if (null == localdatabase) {
            localdatabase =  new UtilsLocalDataBase(mContext);
        }

        if (null == ObjSettingsBetrack) {
            //Read the preferences
            ObjSettingsBetrack = SettingsBetrack.getInstance();
            ObjSettingsBetrack.Update(mContext);
        }

        //Read user id from pref
        IdUser = prefs.getString(USER_ID, null);
        if (null == IdUser)
        {
            //User id doesn't exist yet we create one
            IdUser = UtilsDeviceIdGenerator.readDeviceId(mContext);
            //We save it
            editor.putString(USER_ID, IdUser);
            editor.commit();
            EncryptUserID(IdUser);

        } else {
            IdUserCypher = prefs.getString(USER_ID_CYPHER, null);
            if (IdUserCypher == null) {
                EncryptUserID(IdUser);
            }
        }

        //Read the state of the screen
         if (prefs.getInt(STUDY_BETRACK_SCREENSTATE, SettingsBetrack.SCREEN_SWITCHED_OFF) == SettingsBetrack.SCREEN_SWITCHED_OFF) {
             ScreenState = UtilsScreenState.StateScreen.OFF;
         } else if (prefs.getInt(STUDY_BETRACK_SCREENSTATE, SettingsBetrack.SCREEN_SWITCHED_OFF) == SettingsBetrack.SCREEN_SWITCHED_ON) {
             ScreenState = UtilsScreenState.StateScreen.ON;
         }  else if (prefs.getInt(STUDY_BETRACK_SCREENSTATE, SettingsBetrack.SCREEN_SWITCHED_OFF) == SettingsBetrack.SCREEN_USER_PRESENT) {
             ScreenState = UtilsScreenState.StateScreen.UNLOCKED;
         } else {
             ScreenState = UtilsScreenState.StateScreen.UNKNOWN;
         }

        DurationScreenOn = prefs.getLong(DURATION_SCREEN_ON, 0);
        StartScreenOn = prefs.getLong(START_SCREEN_ON, 0);


        AppWatchId = prefs.getInt(APP_WATCH_ID, -1);
         //Read when we started the monitoring of the application
        AppWatchStartTime = prefs.getLong(APP_WATCH_START_TIME, 0);

        //Read status if betrack has been killed
        BetrackKilled = prefs.getBoolean(STUDY_BETRACK_KILLED, false);

        //Read periodicity information
        AccuracyComputed = prefs.getBoolean(STUDY_ACCURACY_COMPUTED, false);
        AveragePeriodicity = prefs.getString(STUDY_AVERAGE_PERIODICITY, null);
        StandardDeviation = prefs.getString(STUDY_STD_DEVIATION, null);

        //Read array periodicity
        //ArrayPeriodicityHs = prefs.getStringSet(ARRAYPERIODICITY, new LinkedHashSet<String>());

        //Read app to watch
        ApplicationsToWatchHs = prefs.getStringSet(APP_NAME_TO_WATCH, new LinkedHashSet<String>());

        //Read time spend per application
        ApplicationsTimeWatched = prefs.getString(APP_TIME_WATCHED, null);

        //Read status flags
        StudyStarted  = prefs.getBoolean(STUDY_STARTED, false);
        StartSurveyDone = prefs.getBoolean(START_SURVEY_DONE, false);
        SetupBetrackDone = prefs.getBoolean(SETUP_BETRACK_DONE, false);
        EndSurveyDone  = prefs.getBoolean(END_SURVEY_DONE, false);
        int ToBeConvertedTransferState = prefs.getInt(END_SURVEY_TRANSFERRED, EndStudyTranferState.NOT_YET.ordinal());

        if (ToBeConvertedTransferState == EndStudyTranferState.NOT_YET.ordinal()) {
            EndSurveyTransferred = EndStudyTranferState.NOT_YET;
        } else if (ToBeConvertedTransferState == EndStudyTranferState.DONE.ordinal()) {
            EndSurveyTransferred = EndStudyTranferState.DONE;
        } else if (ToBeConvertedTransferState == EndStudyTranferState.IN_PROGRESS.ordinal()) {
            EndSurveyTransferred = EndStudyTranferState.IN_PROGRESS;
        } else {
            EndSurveyTransferred = EndStudyTranferState.ERROR;
        }

        int ToBeConvertedLastDayStudy = prefs.getInt(LAST_DAY_STUDY, LastDayStudyState.ALL_SURVEYS_PENDING.ordinal());

        if (ToBeConvertedLastDayStudy == LastDayStudyState.ALL_SURVEYS_PENDING.ordinal()) {
            LastDayStudy = LastDayStudyState.ALL_SURVEYS_PENDING;
        } else if (ToBeConvertedLastDayStudy == LastDayStudyState.START_SURVEY_DONE.ordinal()) {
            LastDayStudy = LastDayStudyState.START_SURVEY_DONE;
        } else if (ToBeConvertedLastDayStudy == LastDayStudyState.END_SURVEY_DONE.ordinal()) {
            LastDayStudy = LastDayStudyState.END_SURVEY_DONE;
        } else {
            LastDayStudy = LastDayStudyState.END_SURVEY_ERROR;
        }

        //Read informations about the study
        StudyId = prefs.getString(STUDY_ID, null);
        StudyName = prefs.getString(STUDY_NAME, null);
        StudyDescription = prefs.getString(STUDY_DESCRIPTION, null);
        StudyVersionApp = prefs.getString(STUDY_VERSIONAPP, null);
        StudyDuration = prefs.getInt(STUDY_DURATION, 0);
        StudyPublicKey = prefs.getString(STUDY_PUBLICKEY, null);
        StudyContactEmail = prefs.getString(STUDY_CONTACTEMAIL, null);

        //Read information used to trigger the daily survey
        DailySurveyDone = prefs.getBoolean(STUDY_DAILY_SURVEY_DONE, true);
        StartDateSurvey = prefs.getString(STUDY_STARTDATE_SURVEY, null);
        NbrOfNotificationToDo = prefs.getInt(STUDY_NBR_OF_NOTIFICATION_TO_DO, 0);


        TimeLastTransfer = prefs.getLong(STUDY_TIME_LAST_TRANSFER, System.currentTimeMillis());



        TimeLastGPS = System.currentTimeMillis() + SettingsBetrack.TRACKGPS_DELTA;

        TimeNextNotification = prefs.getLong(STUDY_TIME_NEXT_NOTIFICATION, 0);

        //Delete previous session key that would not have been used
        Long IdSql;
        ContentValues values = new ContentValues();
        while(true) {
            values.clear();
            values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_SESSION_KEY, false);
            if (0 != values.size()) {
                IdSql = values.getAsLong(UtilsLocalDataBase.C_SESSION_KEY_ID);
                AccesLocalDB().deleteELement(UtilsLocalDataBase.TABLE_SESSION_KEY, IdSql);
            } else {
                break;
            }
        }
        //Generate a new session key
        try {
            String Result;
            SessionKey = UtilsCryptoAES.generateKey();
            //Encrypt the key
            byte[] dataBytes = SessionKey.toString().getBytes("utf-8");
            Result = UtilsCryptoRSA.encryptWithPublicKey(ObjSettingsBetrack.STUDY_PUBLIC_KEY, dataBytes, mContext);
            //Save it in the local database with the date and time
            values.clear();
            values.put(UtilsLocalDataBase.C_SESSION_KEY_BLOB, Result);
            values.put(UtilsLocalDataBase.C_SESSION_KEY_DATE, sdf.format(new Date()));
            values.put(UtilsLocalDataBase.C_SESSION_KEY_TIME, shf.format(new Date()));

            AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_SESSION_KEY);

        } catch (Exception e) {
            SessionKey = null;
        }
    }

    public static SettingsStudy getInstance(Context context)
    {
        mContext = context;
        return ConfigInfoStudyHolder.instance;
    }

    static public int getAppWatchId()
    {
        int ReturnAppWatchId = -1;
        try {
            SemSettingsStudy.acquire();
            ReturnAppWatchId = AppWatchId;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnAppWatchId = -1;
        } finally {
            return ReturnAppWatchId;
        }
    }

    static public void setAppWatchId(int appwatchid)
    {
        try {
            SemSettingsStudy.acquire();
            AppWatchId = appwatchid;
            editor.putInt(APP_WATCH_ID, AppWatchId);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    static public long getStartScreenOn()
    {
        long ReturnStartScreenOn = 0;
        try {
            SemSettingsStudy.acquire();
            ReturnStartScreenOn = StartScreenOn;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnStartScreenOn = 0;
        } finally {
            return ReturnStartScreenOn;
        }
    }

    static public void setStartScreenOn(long startscreenon)
    {
        try {
            SemSettingsStudy.acquire();
            StartScreenOn = startscreenon;
            editor.putLong(START_SCREEN_ON, StartScreenOn);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    static public long getDurationScreenOn()
    {
        long ReturnDurationScreenOn = 0;
        try {
            SemSettingsStudy.acquire();
            ReturnDurationScreenOn = DurationScreenOn;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnDurationScreenOn = 0;
        } finally {
            return ReturnDurationScreenOn;
        }
    }

    static public void setDurationScreenOn(long durationscreenon)
    {
        try {
            SemSettingsStudy.acquire();
            DurationScreenOn = durationscreenon;
            editor.putLong(DURATION_SCREEN_ON, DurationScreenOn);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }


    static public long getAppWatchStartTime()
    {
        long ReturnAppWatchStartTime = 0;
        try {
            SemSettingsStudy.acquire();
            ReturnAppWatchStartTime = AppWatchStartTime;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnAppWatchStartTime = 0;
        } finally {
            return ReturnAppWatchStartTime;
        }
    }

    static public void setAppWatchStartTime(long appwatchstarttime)
    {
        try {
            SemSettingsStudy.acquire();
            AppWatchStartTime = appwatchstarttime;
            editor.putLong(APP_WATCH_START_TIME, AppWatchStartTime);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    private void EncryptUserID(String IdUser)
    {
        //Encrypt the user ID
        try {
            byte[] dataBytes = IdUser.toString().getBytes("utf-8");
            IdUserCypher = UtilsCryptoRSA.encryptWithPublicKey(ObjSettingsBetrack.STUDY_PUBLIC_KEY, dataBytes, mContext);
            //We save it
            editor.putString(USER_ID_CYPHER, IdUserCypher);
            editor.commit();
        } catch (Exception e) {
            IdUserCypher = null;
        }
    }

    public String getStandardDeviation()
    {
        String ReturnStandardDeviation = null;
        try {
            SemSettingsStudy.acquire();
            ReturnStandardDeviation = StandardDeviation;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnStandardDeviation = null;
        } finally {
            return ReturnStandardDeviation;
        }
    }

    public void setStandardDeviation(String standarddeviation)
    {
        try {
            SemSettingsStudy.acquire();
            StandardDeviation = standarddeviation;
            editor.putString(STUDY_STD_DEVIATION, StandardDeviation);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public String getAveragePeriodicity()
    {
        String ReturnAveragePeriodicity = null;
        try {
            SemSettingsStudy.acquire();
            ReturnAveragePeriodicity = AveragePeriodicity;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnAveragePeriodicity = null;
        } finally {
            return ReturnAveragePeriodicity;
        }
    }

    public void setAveragePeriodicity(String averageperiodicity)
    {
        try {
            SemSettingsStudy.acquire();
            AveragePeriodicity = averageperiodicity;
            editor.putString(STUDY_AVERAGE_PERIODICITY, AveragePeriodicity);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public Boolean getAccuracyComputed()
    {
        boolean ReturnAccuracyComputed = false;
        try {
            SemSettingsStudy.acquire();
            ReturnAccuracyComputed = AccuracyComputed;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnAccuracyComputed = false;
        } finally {
            return ReturnAccuracyComputed;
        }
    }

    public void setAccuracyComputed(boolean status)
    {
        try {
            SemSettingsStudy.acquire();
            AccuracyComputed = status;
            editor.putBoolean(STUDY_ACCURACY_COMPUTED, AccuracyComputed);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public UtilsScreenState.StateScreen getBetrackScreenState()
    {
        UtilsScreenState.StateScreen ReturnBetrackScreenState = UtilsScreenState.StateScreen.UNKNOWN;
        try {
            SemSettingsStudy.acquire();
            ReturnBetrackScreenState = ScreenState;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnBetrackScreenState = UtilsScreenState.StateScreen.UNKNOWN;
        } finally {
            return ReturnBetrackScreenState;
        }
    }

    public void setBetrackScreenState(UtilsScreenState.StateScreen BetrackScreenState)
    {
        try {
            SemSettingsStudy.acquire();
            ScreenState = BetrackScreenState;
            if (ScreenState == UtilsScreenState.StateScreen.OFF) {
                editor.putInt(STUDY_BETRACK_SCREENSTATE, SettingsBetrack.SCREEN_SWITCHED_OFF);
            } else if (ScreenState == UtilsScreenState.StateScreen.ON) {
                editor.putInt(STUDY_BETRACK_SCREENSTATE, SettingsBetrack.SCREEN_SWITCHED_ON);
            }  else if (ScreenState == UtilsScreenState.StateScreen.UNLOCKED) {
                editor.putInt(STUDY_BETRACK_SCREENSTATE, SettingsBetrack.SCREEN_USER_PRESENT);
            } else {
                editor.putInt(STUDY_BETRACK_SCREENSTATE, SettingsBetrack.SCREEN_BETRACK_ERROR);
            }
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public Boolean getBetrackKilled()
    {
        boolean ReturnBetrackKilled = false;
        try {
            SemSettingsStudy.acquire();
            ReturnBetrackKilled = BetrackKilled;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnBetrackKilled = false;
        } finally {
            return ReturnBetrackKilled;
        }
    }

    public void setBetrackKilled(boolean status)
    {
        try {
            SemSettingsStudy.acquire();
            BetrackKilled = status;
            editor.putBoolean(STUDY_BETRACK_KILLED, BetrackKilled);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public String getStartDateSurvey()
    {
        String ReturnStartDateSurvey = null;
        try {
            SemSettingsStudy.acquire();
            ReturnStartDateSurvey = StartDateSurvey;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnStartDateSurvey = null;
        } finally {
            return ReturnStartDateSurvey;
        }
    }

    public void setStartDateSurvey(String startdatesurvey)
    {
        try {
            SemSettingsStudy.acquire();
            StartDateSurvey = startdatesurvey;
            editor.putString(STUDY_STARTDATE_SURVEY, StartDateSurvey);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public long getTimeLastGPS()
    {

        long ReturnTimeLastGPS = 0;
        try {
            SemSettingsStudy.acquire();
            ReturnTimeLastGPS = TimeLastGPS;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnTimeLastGPS = 0;
        } finally {
            return ReturnTimeLastGPS;
        }
    }

    public void setTimeLastGPS(long timelastgps)
    {
        try {
            SemSettingsStudy.acquire();
            TimeLastGPS = timelastgps;
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public long getTimeNextNotification()
    {

        long ReturnTimeNextNotification = 0;
        try {
            SemSettingsStudy.acquire();
            ReturnTimeNextNotification = TimeNextNotification;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnTimeNextNotification = 0;
        } finally {
            return ReturnTimeNextNotification;
        }
    }

    public void setTimeNextNotification(long timenextnotification)
    {
        try {
            SemSettingsStudy.acquire();
            TimeNextNotification = timenextnotification;
            editor.putLong(STUDY_TIME_NEXT_NOTIFICATION, TimeNextNotification);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public long getTimeLastTransfer()
    {
        long ReturnTimeLastTransfer = 0;
        try {
            SemSettingsStudy.acquire();
            ReturnTimeLastTransfer = TimeLastTransfer;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnTimeLastTransfer = 0;
        } finally {
            return ReturnTimeLastTransfer;
        }
    }

    public void setTimeLastTransfer(long timelasttransfer)
    {
        try {
            SemSettingsStudy.acquire();
            TimeLastTransfer = timelasttransfer;
            editor.putLong(STUDY_TIME_LAST_TRANSFER, TimeLastTransfer);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public Boolean getDailySurveyDone()
    {
        boolean ReturnDailySurveyDone = false;
        try {
            SemSettingsStudy.acquire();
            ReturnDailySurveyDone = DailySurveyDone;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnDailySurveyDone = false;
        } finally {
            return ReturnDailySurveyDone;
        }
    }

    public void setDailySurveyDone(boolean status)
    {
        try {
            SemSettingsStudy.acquire();
            DailySurveyDone = status;
            editor.putBoolean(STUDY_DAILY_SURVEY_DONE, DailySurveyDone);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public String getIdUser()
    {
        String ReturnIdUser = null;
        try {
            SemSettingsStudy.acquire();
            ReturnIdUser = IdUser;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnIdUser = null;
        } finally {
            return ReturnIdUser;
        }
    }

    public String getIdUserCypher()
    {
        String ReturnIdUserCypher = null;
        try {
            SemSettingsStudy.acquire();
            ReturnIdUserCypher = IdUserCypher;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnIdUserCypher = null;
        } finally {
            return ReturnIdUserCypher;
        }
    }

    public boolean getStudyStarted()
    {
        boolean ReturnStudyStarted = false;
        try {
            SemSettingsStudy.acquire();
            ReturnStudyStarted = StudyStarted;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnStudyStarted = false;
        } finally {
            return ReturnStudyStarted;
        }
    }

    public void setStudyStarted(boolean status)
    {
        try {
            SemSettingsStudy.acquire();
            StudyStarted = status;
            editor.putBoolean(STUDY_STARTED, StudyStarted);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public boolean getStartSurveyDone()
    {
        boolean ReturnStartSurveyDone = false;
        try {
            SemSettingsStudy.acquire();
            ReturnStartSurveyDone = StartSurveyDone;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnStartSurveyDone = false;
        } finally {
            return ReturnStartSurveyDone;
        }
    }

    public void setStartSurveyDone(boolean status)
    {
        try {
            SemSettingsStudy.acquire();
            StartSurveyDone = status;
            editor.putBoolean(START_SURVEY_DONE, StartSurveyDone);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public boolean getSetupBetrackDone()
    {
        boolean ReturnSetupBetrackDone = false;
        try {
            SemSettingsStudy.acquire();
            ReturnSetupBetrackDone = SetupBetrackDone;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnSetupBetrackDone = false;
        } finally {
            return ReturnSetupBetrackDone;
        }
    }

    public void setSetupBetrackDone(boolean status)
    {
        try {
            SemSettingsStudy.acquire();
            SetupBetrackDone = status;
            editor.putBoolean(SETUP_BETRACK_DONE, SetupBetrackDone);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public boolean getEndSurveyDone()
    {
        boolean ReturnEndSurveyDone = false;
        try {
            SemSettingsStudy.acquire();
            ReturnEndSurveyDone = EndSurveyDone;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnEndSurveyDone = false;
        } finally {
            return ReturnEndSurveyDone;
        }
    }

    public void setEndSurveyDone(boolean status)
    {
        try {
            SemSettingsStudy.acquire();
            EndSurveyDone = status;
            editor.putBoolean(END_SURVEY_DONE, EndSurveyDone);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public LastDayStudyState getLastDayStudyState()
    {
        LastDayStudyState ReturnEndLastDayStudyState = LastDayStudyState.END_SURVEY_ERROR;
        try {
            SemSettingsStudy.acquire();
            ReturnEndLastDayStudyState = LastDayStudy;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnEndLastDayStudyState = LastDayStudyState.END_SURVEY_ERROR;
        } finally {
            return ReturnEndLastDayStudyState;
        }
    }

    public void setLastDayStudyState(LastDayStudyState status)
    {
        try {
            SemSettingsStudy.acquire();
            LastDayStudy = status;
            editor.putInt(LAST_DAY_STUDY, LastDayStudy.ordinal());
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }


    public EndStudyTranferState getEndSurveyTransferred()
    {
        EndStudyTranferState ReturnEndSurveyTransferred = EndStudyTranferState.ERROR;
        try {
            SemSettingsStudy.acquire();
            ReturnEndSurveyTransferred = EndSurveyTransferred;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnEndSurveyTransferred = EndStudyTranferState.ERROR;
        } finally {
            return ReturnEndSurveyTransferred;
        }
    }

    public void setEndSurveyTransferred(EndStudyTranferState status)
    {
        try {
            SemSettingsStudy.acquire();
            EndSurveyTransferred = status;
            editor.putInt(END_SURVEY_TRANSFERRED, EndSurveyTransferred.ordinal());
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public void  setAppTimeWatched(int appWatchedId, int numAppWatched, int timeWatched)
    {
        int[] listTimeAppWatched = new int[numAppWatched];
        StringBuilder str = new StringBuilder();

        Arrays.fill(listTimeAppWatched, 0);

        try {
            SemSettingsStudy.acquire();
            if (ApplicationsTimeWatched != null) {
                StringTokenizer st = new StringTokenizer(ApplicationsTimeWatched, ",");

                for (int i = 0; i < listTimeAppWatched.length; i++) {
                    listTimeAppWatched[i] = Integer.parseInt(st.nextToken());
                    if (i == appWatchedId) {
                        listTimeAppWatched[i] += timeWatched;
                    }
                }
            } else {
                for (int i = 0; i < listTimeAppWatched.length; i++) {
                    listTimeAppWatched[i] = 0;
                    if (i == appWatchedId) {
                        listTimeAppWatched[i] = timeWatched;
                    }
                }
            }

            for (int i = 0; i < listTimeAppWatched.length; i++) {
                str.append(listTimeAppWatched[i]).append(",");
            }

            editor.putString(APP_TIME_WATCHED, str.toString());
            ApplicationsTimeWatched = str.toString();

            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public int getAppTimeWatched(int appWatchedId, int numAppWatched)
    {
        int ReturnTimeWatched = 0;
        int[] listTimeAppWatched = new int[numAppWatched];
        StringBuilder str = new StringBuilder();

        Arrays.fill(listTimeAppWatched, 0);

        try {
            SemSettingsStudy.acquire();
            if (ApplicationsTimeWatched != null) {
                StringTokenizer st = new StringTokenizer(ApplicationsTimeWatched, ",");

                for (int i = 0; i < listTimeAppWatched.length; i++) {
                    listTimeAppWatched[i] = Integer.parseInt(st.nextToken());
                    if (i == appWatchedId) {
                        ReturnTimeWatched = listTimeAppWatched[i];
                        break;
                    }
                }
            } else {
                ReturnTimeWatched = 0;
            }

            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnTimeWatched = 0;
        } finally {
            return ReturnTimeWatched;
        }
    }

    public ArrayList<Long>  getArrayPeriodicity()
    {
        ArrayList<Long>  ReturnArrayPeriodicity = new ArrayList<>();
        ArrayPeriodicityHs = prefs.getStringSet(ARRAYPERIODICITY, new LinkedHashSet<String>());
        Iterator<String> iterator = ArrayPeriodicityHs.iterator();
        try {
            SemSettingsStudy.acquire();
            while(iterator.hasNext()) {
                ReturnArrayPeriodicity.add(Long.parseLong(iterator.next().trim()));
            }
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnArrayPeriodicity = null;
        } finally {
            return ReturnArrayPeriodicity;
        }
    }

    public void setArrayPeriodicity(long Periodicity)
    {
        try {
            SemSettingsStudy.acquire();
            ArrayPeriodicityHs = prefs.getStringSet(ARRAYPERIODICITY, new LinkedHashSet<String>());
            int size = ArrayPeriodicityHs.size() + Long.toString(Periodicity).length();

            //make a copy, update it and save it
            Set<String> newStrSet = new HashSet<String>();
            newStrSet.add(String.format("%-"+size+"s", Long.toString(Periodicity)));
            newStrSet.addAll(ArrayPeriodicityHs);
            editor.remove(ARRAYPERIODICITY);
            editor.putStringSet(ARRAYPERIODICITY, newStrSet);
            editor.commit();

            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public ArrayList<String>  getApplicationsToWatch()
    {
        ArrayList<String>  ReturnApplicationsToWatch = new ArrayList<String>();

        Iterator<String> iterator = ApplicationsToWatchHs.iterator();
        try {
            SemSettingsStudy.acquire();
            while(iterator.hasNext()) {
                ReturnApplicationsToWatch.add(iterator.next());
            }
            Collections.sort(ReturnApplicationsToWatch);
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnApplicationsToWatch = null;
        } finally {
            return ReturnApplicationsToWatch;
        }
    }

    public void setApplicationsToWatch(String appToWatch)
    {
        try {
            SemSettingsStudy.acquire();
            ApplicationsToWatchHs.add(appToWatch);
            editor.putStringSet(APP_NAME_TO_WATCH, ApplicationsToWatchHs);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public String getStudyId()
    {
        String ReturnStudyId = null;
        try {
            SemSettingsStudy.acquire();
            ReturnStudyId = StudyId;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnStudyId = null;
        } finally {
            return ReturnStudyId;
        }
    }

    public void setStudyId(String studyid)
    {
        try {
            SemSettingsStudy.acquire();
            StudyId = studyid;
            editor.putString(STUDY_ID, StudyId);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public String getStudyName()
    {
        String ReturnStudyName = null;
        try {
            SemSettingsStudy.acquire();
            ReturnStudyName = StudyName;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnStudyName = null;
        } finally {
            return ReturnStudyName;
        }
    }

    public void setStudyName(String studyname)
    {
        try {
            SemSettingsStudy.acquire();
            StudyName = studyname;
            editor.putString(STUDY_NAME, StudyName);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public String getStudyDescription()
    {
        String ReturnStudyDescription = null;
        try {
            SemSettingsStudy.acquire();
            ReturnStudyDescription = StudyDescription;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnStudyDescription = null;
        } finally {
            return ReturnStudyDescription;
        }
    }

    public void setStudyDescription(String studydescription)
    {
        try {
            SemSettingsStudy.acquire();
            StudyDescription = studydescription;
            editor.putString(STUDY_DESCRIPTION, StudyDescription);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public String getStudyVersionApp()
    {
        String ReturnStudyVersionApp = null;
        try {
            SemSettingsStudy.acquire();
            ReturnStudyVersionApp = StudyVersionApp;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnStudyVersionApp = null;
        } finally {
            return ReturnStudyVersionApp;
        }
    }

    public void setStudyVersionApp(String studyversionapp)
    {
        try {
            SemSettingsStudy.acquire();
            StudyVersionApp = studyversionapp;
            editor.putString(STUDY_VERSIONAPP, StudyVersionApp);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public int getNbrOfNotificationToDo()
    {
        int ReturnNbrOfNotificationToDo = 0;
        try {
            SemSettingsStudy.acquire();
            ReturnNbrOfNotificationToDo = NbrOfNotificationToDo;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnNbrOfNotificationToDo = 0;
        } finally {
            return ReturnNbrOfNotificationToDo;
        }
    }

    public void setNbrOfNotificationToDo(int nbrofnotification)
    {
        try {
            SemSettingsStudy.acquire();
            NbrOfNotificationToDo = nbrofnotification;
            editor.putInt(STUDY_NBR_OF_NOTIFICATION_TO_DO, NbrOfNotificationToDo);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public int getStudyDuration()
    {
        int ReturnStudyDuration = 0;
        try {
            SemSettingsStudy.acquire();
            ReturnStudyDuration = StudyDuration;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnStudyDuration = 0;
        } finally {
            return ReturnStudyDuration;
        }
    }

    public void setStudyDuration(int studyduration)
    {
        try {
            SemSettingsStudy.acquire();
            StudyDuration = studyduration;
            editor.putInt(STUDY_DURATION, StudyDuration);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public String getStudyPublicKey()
    {
        String ReturnStudyPublicKey = null;
        try {
            SemSettingsStudy.acquire();
            ReturnStudyPublicKey = StudyPublicKey;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnStudyPublicKey = null;
        } finally {
            return ReturnStudyPublicKey;
        }
    }

    public void setStudyPublicKey(String studypublickey)
    {
        try {
            SemSettingsStudy.acquire();
            StudyPublicKey = studypublickey;
            editor.putString(STUDY_PUBLICKEY, StudyPublicKey);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public String getStudyContactEmail()
    {
        String ReturnStudyContactEmail = null;
        try {
            SemSettingsStudy.acquire();
            ReturnStudyContactEmail = StudyContactEmail;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnStudyContactEmail = null;
        } finally {
            return ReturnStudyContactEmail;
        }
    }

    public void setStudyContactEmail(String studycontactemail)
    {
        try {
            SemSettingsStudy.acquire();
            StudyContactEmail = studycontactemail;
            editor.putString(STUDY_CONTACTEMAIL, StudyContactEmail);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public static enum EndStudyTranferState {
        NOT_YET, IN_PROGRESS, DONE, ERROR
    }

    public static enum LastDayStudyState {
        ALL_SURVEYS_PENDING, START_SURVEY_DONE, END_SURVEY_DONE, END_SURVEY_ERROR
    }

    private static class ConfigInfoStudyHolder
    {
        private final static SettingsStudy instance = new SettingsStudy();
    }

}
