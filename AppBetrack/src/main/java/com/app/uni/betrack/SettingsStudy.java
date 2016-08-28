package com.app.uni.betrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * Created by cevincent on 4/24/16.
 */
public class SettingsStudy {

    public static final Semaphore SemSettingsStudy = new Semaphore(1, true);

    public enum returnCode {
        OK,
        NETWORKERROR,
        ERROR,
    };

    static private final String STUDY_STARTED = "study_started";
    static private final String APP_NAME_TO_WATCH = "AppNameToWatch";
    static private final String ID_USER = "IdUser";
    static private final String STUDY_DESCRIPTION = "StudyDescription";

    static private String IdUser; //Unique ID for this user
    static private String StudyDescription; //Description of the study going on

    static private Boolean StudyStarted; //A study is started

    static private String StudyId;
    static private String StudyName;
    static private String StudyVersionApp;
    static private String StudyDuration;
    static private String StudyPublicKey;
    static private String StudyContactEmail;
    static private String StudyLinkEndStudy;
    static private ArrayList<String> ApplicationsToWatch = new ArrayList<String>();
    static private int TIME_OUT = 1000;
    static private Context mContext = null;
    static private returnCode mReturnCode = returnCode.ERROR;

    private SettingsStudy()
    {}

    private static class ConfigInfoStudyHolder
    {
        private final static SettingsStudy instance = new SettingsStudy();
    }

    public static SettingsStudy getInstance()
    {
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

    public ArrayList<String>  getApplicationsToWatch()
    {
        ArrayList<String>  ReturnApplicationsToWatch = null;
        try {
            SemSettingsStudy.acquire();
            ReturnApplicationsToWatch = ApplicationsToWatch;
            SemSettingsStudy.release();
        } catch (Exception e) {
            ReturnApplicationsToWatch = null;
        } finally {
            return ReturnApplicationsToWatch;
        }
    }


    public static returnCode Update(Context context)
    {
        returnCode rc = returnCode.ERROR;
        mContext = context;
        try {
            SemSettingsStudy.acquire();
            if (false == StudyStarted) {
                try
                {
                    //Read what to watch from the distant server
                    gwtw.execute();

                    //Resynchronise with the network process
                    SemSettingsStudy.acquire();
                    SemSettingsStudy.release();

                    //TODO In case of timeout I have to check what's happening
                    rc = mReturnCode;
                    mReturnCode = returnCode.NETWORKERROR;


                }
                catch (Exception e) {
                    rc = returnCode.ERROR;
                }

            }
            else
            {
                ReadSettingsStudy();
                rc = returnCode.OK;
                SemSettingsStudy.release();
            }
        } catch (Exception e) {
            rc = returnCode.ERROR;
        }

        return rc;
    }

    private static NetworkGetWhatToWatch gwtw = new NetworkGetWhatToWatch(new NetworkGetWhatToWatch.AsyncResponse(){

        @Override
        public void processFinish(final String output) {

            new Handler().postDelayed(new Runnable() {


                @Override
                public void run() {

                    if (null != output) {
                        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
                        SharedPreferences.Editor editor = prefs.edit();
                        Set<String> hs = prefs.getStringSet(SettingsStudy.APP_NAME_TO_WATCH, new HashSet<String>());
                        Set<String> in = new HashSet<String>(hs);

                        //Save the applications to watch in the preference file
                        for (int i=0; i< NetworkGetWhatToWatch.ContextInfoStudy.ApplicationsToWatch.size(); i++) {
                            in.add(NetworkGetWhatToWatch.ContextInfoStudy.ApplicationsToWatch.get(i));
                        }
                        editor.putStringSet(SettingsStudy.APP_NAME_TO_WATCH, in);

                        //We save in the preference that a study has been started and is ongoing
                        editor.putBoolean(SettingsStudy.STUDY_STARTED, true);
                        editor.commit();

                        //Everything ok
                        mReturnCode = returnCode.OK;

                    } else {
                        //Network error
                        mReturnCode = returnCode.NETWORKERROR;

                    }
                    SemSettingsStudy.release();

                }
            }, TIME_OUT);
        }}
    );

    private static void ReadSettingsStudy()
    {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> hs = prefs.getStringSet(SettingsStudy.APP_NAME_TO_WATCH, new HashSet<String>());
        Set<String> in = new HashSet<String>(hs);
        Iterator<String> iterator = hs.iterator();

        //Read if a study is started

        //Read app to watch
        while(iterator.hasNext()){

            String AppToWatch = iterator.next();

            ApplicationsToWatch.add(AppToWatch);
        }
    }
}
