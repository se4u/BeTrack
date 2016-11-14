package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;

/**
 * Created by cevincent on 4/24/16.
 */
public class SettingsStudy {

    static final String TAG = "SettingsStudy";

    private SettingsBetrack ObjSettingsBetrack = null;
    private UtilsLocalDataBase localdatabase = null;
    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    public static final Semaphore SemSettingsStudy = new Semaphore(1, true);
    public static final Semaphore SemPhoneUsage = new Semaphore(1, true);

    static private final String STUDY_STARTED = "study_started";
    static private final String START_SURVEY_DONE = "start_survey_done";
    static private final String SETUP_BETRACK_DONE = "setup_betrack_done";
    static private final String END_SURVEY_DONE = "end_survey_done";
    static private final String END_SURVEY_TRANSFERRED = "end_survey_transferred";

    static private Boolean StudyStarted; //A study is started
    static private Boolean SetupBetrackDone; //The phone has be set up to be used by Betrack
    static private Boolean StartSurveyDone; //Survey for starting the study has been filled up
    static private Boolean EndSurveyDone; //Survey for ending the study has been filled up
    static private Boolean EndSurveyTransferred; //Set to true when trhe end survey has been successfully transferred


    static private final String APP_NAME_TO_WATCH = "AppNameToWatch";
    Set<String> ApplicationsToWatchHs;
    Set<String> ApplicationsToWatchIn;
    static private final String APP_TIME_WATCHED = "AppTimeWatched";
    static private String ApplicationsTimeWatched;
    static public long AppWatchStartTime = 0;
    static public int AppWatchId = 0;

    static private String StudyId;
    static private String StudyName;
    static private String StudyDescription;
    static private String StudyVersionApp;
    static private int StudyDuration;
    static private String StudyPublicKey;
    static private String StudyContactEmail;
    static private final String STUDY_ID = "StudyId";
    static private final String STUDY_NAME = "StudyName";
    static private final String STUDY_DESCRIPTION = "StudyDescription";
    static private final String STUDY_VERSIONAPP = "StudyVersionApp";
    static private final String STUDY_DURATION = "StudyDuration";
    static private final String STUDY_PUBLICKEY = "StudyPublicKey";
    static private final String STUDY_CONTACTEMAIL = "StudyContactEmail";

    static private String IdUser;
    static private final String USER_ID = "UserId";
    static private String IdUserCypher;
    static private final String USER_ID_CYPHER = "UserIdCypher";

    static private Boolean DailySurveyDone;
    static private String StartDateSurvey;
    static private int PhoneUsage;
    static private final String STUDY_DAILY_SURVEY_DONE = "DailySurveyDone";
    static private final String STUDY_STARTDATE_SURVEY = "StartDateSurvey";
    static private final String STUDY_PHONEUSAGE_SURVEY = "PhoneUsageSurvey";

    static private long TimeLastTransfer;
    static private long TimeLastGPS;

    static private final String STUDY_TIME_NEXT_NOTIFICATION = "TimeNextNotification";
    static private long TimeNextNotification;

    static public UtilsCryptoAES.SecretKeys SessionKey;
    static public boolean SessionKeyHasBeenTransferred = false;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    static private Context mContext = null;

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

        //Read app to watch
        ApplicationsToWatchHs = prefs.getStringSet(APP_NAME_TO_WATCH, new HashSet<String>());
        ApplicationsToWatchIn = new HashSet<>(ApplicationsToWatchHs);

        //Read time spend per application
        ApplicationsTimeWatched = prefs.getString(APP_TIME_WATCHED, null);

        //Read status flags
        StudyStarted  = prefs.getBoolean(STUDY_STARTED, false);
        StartSurveyDone = prefs.getBoolean(START_SURVEY_DONE, false);
        SetupBetrackDone = prefs.getBoolean(SETUP_BETRACK_DONE, false);
        EndSurveyDone  = prefs.getBoolean(END_SURVEY_DONE, false);
        EndSurveyTransferred = prefs.getBoolean(END_SURVEY_TRANSFERRED, false);

        //Read informations about the study
        StudyId = prefs.getString(STUDY_ID, null);
        StudyName = prefs.getString(STUDY_NAME, null);
        StudyDescription = prefs.getString(STUDY_DESCRIPTION, null);
        StudyVersionApp = prefs.getString(STUDY_VERSIONAPP, null);
        StudyDuration = prefs.getInt(STUDY_DURATION, 0);
        StudyPublicKey = prefs.getString(STUDY_PUBLICKEY, null);
        StudyContactEmail = prefs.getString(STUDY_CONTACTEMAIL, null);

        //Read information used to trigger the daily survey
        DailySurveyDone = prefs.getBoolean(STUDY_DAILY_SURVEY_DONE, false);
        StartDateSurvey = prefs.getString(STUDY_STARTDATE_SURVEY, null);
        PhoneUsage = prefs.getInt(STUDY_PHONEUSAGE_SURVEY, 0);

        TimeLastTransfer = System.currentTimeMillis();
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

    private static class ConfigInfoStudyHolder
    {
        private final static SettingsStudy instance = new SettingsStudy();
    }

    public static SettingsStudy getInstance(Context context)
    {
        mContext = context;
        return ConfigInfoStudyHolder.instance;
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

    public void setEndSurveyTransferred(boolean status)
    {
        try {
            SemSettingsStudy.acquire();
            EndSurveyTransferred = status;
            editor.putBoolean(END_SURVEY_TRANSFERRED, EndSurveyTransferred);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public boolean getEndSurveyTransferred()
    {
        boolean ReturnEndSurveyTransferred = false;
        try {
            SemSettingsStudy.acquire();
            ReturnEndSurveyTransferred = EndSurveyTransferred;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnEndSurveyTransferred = false;
        } finally {
            return ReturnEndSurveyTransferred;
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
                listTimeAppWatched[appWatchedId] += timeWatched;
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

    public void setApplicationsToWatch(String appToWatch)
    {
        try {
            SemSettingsStudy.acquire();
            ApplicationsToWatchIn.add(appToWatch);
            editor.putStringSet(APP_NAME_TO_WATCH, ApplicationsToWatchIn);
            editor.commit();
            SemSettingsStudy.release();
        } catch (Exception e) {
            Log.d(TAG, "Error during acquiring SemSettingsStudy");
        }
    }

    public ArrayList<String>  getApplicationsToWatch()
    {
        ArrayList<String>  ReturnApplicationsToWatch = new ArrayList<String>();

        if (null == ApplicationsToWatchIn) {
            ApplicationsToWatchHs = prefs.getStringSet(APP_NAME_TO_WATCH, new HashSet<String>());
            ApplicationsToWatchIn = new HashSet<>(ApplicationsToWatchHs);
        }

        Iterator<String> iterator = ApplicationsToWatchIn.iterator();
        try {
            SemSettingsStudy.acquire();
            while(iterator.hasNext()) {
                ReturnApplicationsToWatch.add(iterator.next());
            }
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnApplicationsToWatch = null;
        } finally {
            return ReturnApplicationsToWatch;
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

    public int getPhoneUsage()
    {
        int ReturnPhoneUsage = 0;
        try {
            SemSettingsStudy.acquire();
            ReturnPhoneUsage = PhoneUsage;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnPhoneUsage = 0;
        } finally {
            return ReturnPhoneUsage;
        }
    }

    public void setPhoneUsage(int phoneusage)
    {
        try {
            SemSettingsStudy.acquire();
            PhoneUsage = phoneusage;
            editor.putInt(STUDY_PHONEUSAGE_SURVEY, PhoneUsage);
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

}
