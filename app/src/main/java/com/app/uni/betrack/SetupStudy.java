package com.app.uni.betrack;


import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RadioButton;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cevincent on 4/22/16.
 */
public class SetupStudy {
    static public String STUDY_STARTED = "study_started";
    private String STUDY_ID = "StudyId";
    private String STUDY_NAME = "StudyName";
    private String STUDY_VERSIONAPP = "VersionApp";
    private String STUDY_DURATION = "Duration";
    private String STUDY_PUBLICKEY = "PublicKey";
    private String STUDY_CONTACTEMAIL = "ContactEmail";
    private String STUDY_LINKENDSTUDY = "LinkEndStudy";
    private Activity mActivity;

    public SetupStudy(SharedPreferences prefs) {
        //mActivity = context;
        String StudyStatusKey = STUDY_STARTED;
        final String StudyId = STUDY_ID;
        final String StudyName = STUDY_NAME;
        final String StudyVersionApp = STUDY_VERSIONAPP;
        final String StudyDuration = STUDY_DURATION;
        final String StudyPublicKey = STUDY_PUBLICKEY;
        final String StudyContactEmail = STUDY_CONTACTEMAIL;
        final String StudyLinkEndStudy = STUDY_LINKENDSTUDY;

        //final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        SharedPreferences.Editor editor = prefs.edit();
        boolean StudyStatus = prefs.getBoolean(StudyStatusKey, false);

        //Check if a study has been already setup
        if (false == StudyStatus) {
            //Read the data of the study from InfoStudy and update the local preference settings


            editor.putBoolean(StudyStatusKey, true);

        }
        else
        {
            //Read the data of the study from the local preference settings


        }
    }
    public SetupStudy(Activity context) {
        mActivity = context;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity.getApplicationContext());
        Set<String> hs = prefs.getStringSet("AppNameToWatch", new HashSet<String>());
        Set<String> in = new HashSet<String>(hs);
        String StudyStatusKey = STUDY_STARTED;
        SharedPreferences.Editor editor = prefs.edit();
        boolean StudyStatus = prefs.getBoolean(StudyStatusKey, false);
        if (false == StudyStatus) {
            try
            {
                //Check if connection to the distant server worked
                if (null != new GetAppToWatch().execute().get()) {
                    //Save the applications to watch in the preference file
                    for (int i=0; i< GetAppToWatch.ApplicationName.size(); i++) {
                        in.add(GetAppToWatch.ApplicationName.get(i));
                    }
                    editor.putStringSet("AppNameToWatch", in);
                    //Read the data of the study from InfoStudy and update the local preference settings
                    editor.putBoolean(StudyStatusKey, true);
                    editor.commit();

                }
                else
                {
                    //Connection error
                }
            }
            catch (Exception e) {

            }

        }
        else
        {
            //Read the data of the study from the local preference settings


        }
    }

}
