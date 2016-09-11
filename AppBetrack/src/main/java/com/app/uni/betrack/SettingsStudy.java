package com.app.uni.betrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * Created by cevincent on 4/24/16.
 */
public class SettingsStudy {

    static final String TAG = "SettingsStudy";

    public static final Semaphore SemSettingsStudy = new Semaphore(1, true);

    static private final String STUDY_STARTED = "study_started";
    static private final String START_SURVEY_DONE = "start_survey_done";
    static private final String SETUP_BETRACK_DONE = "setup_betrack_done";
    static private final String END_SURVEY_DONE = "end_survey_done";
    static private Boolean StudyStarted; //A study is started
    static private Boolean StartSurveyDone; //Survey for starting the study has been filled up
    static private Boolean SetupBetrackDone; //Set uo of teh phone has been done to allow betrack to start recording informations
    static private Boolean EndSurveyDone; //Survey for ending the study has been filled up


    static private final String APP_NAME_TO_WATCH = "AppNameToWatch";
    Set<String> ApplicationsToWatchHs;
    Set<String> ApplicationsToWatchIn;

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

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    static private Context mContext = null;

    private SettingsStudy()
    {
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = prefs.edit();

        //Read user id from pref
        IdUser = prefs.getString(USER_ID, null);
        if (null == IdUser)
        {
            //User id doesn't exist yet we create one
            IdUser = UtilsDeviceIdGenerator.readDeviceId(mContext);
            //We save it
            editor.putString(USER_ID, IdUser);
            editor.commit();
        }
        //Read app to watch
        ApplicationsToWatchHs = prefs.getStringSet(APP_NAME_TO_WATCH, new HashSet<String>());
        ApplicationsToWatchIn = new HashSet<>(ApplicationsToWatchHs);

        //Read status flags
        StudyStarted  = prefs.getBoolean(STUDY_STARTED, false);
        StartSurveyDone = prefs.getBoolean(START_SURVEY_DONE, false);
        SetupBetrackDone = prefs.getBoolean(SETUP_BETRACK_DONE, false);
        EndSurveyDone  = prefs.getBoolean(END_SURVEY_DONE, false);

        //Read informations about the study
        StudyId = prefs.getString(STUDY_ID, null);
        StudyName = prefs.getString(STUDY_NAME, null);
        StudyDescription = prefs.getString(STUDY_DESCRIPTION, null);
        StudyVersionApp = prefs.getString(STUDY_VERSIONAPP, null);
        StudyDuration = prefs.getInt(STUDY_DURATION, 0);
        StudyPublicKey = prefs.getString(STUDY_PUBLICKEY, null);
        StudyContactEmail = prefs.getString(STUDY_CONTACTEMAIL, null);
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
            editor.putBoolean(END_SURVEY_DONE, StudyStarted);
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
        Iterator<String> iterator = ApplicationsToWatchHs.iterator();
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
