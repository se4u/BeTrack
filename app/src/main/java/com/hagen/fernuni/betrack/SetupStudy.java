package com.hagen.fernuni.betrack;


import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by cevincent on 4/22/16.
 */
public class SetupStudy {
    private String STUDY_STARTED = "study_started";
    private Activity mActivity;

    public SetupStudy(Activity context, String StudyID) {
        mActivity = context;
        final String StudyStatusKey = STUDY_STARTED + StudyID;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        SharedPreferences.Editor editor = prefs.edit();
        boolean StudyStatus = prefs.getBoolean(StudyStatusKey, false);

        //Check if a study has been already setup
        if (false == StudyStatus) {
            //Read the data of the study from the remote SQL database

            editor.putBoolean(StudyStatusKey, true);

            //No internet connection, return an error

        }
        else
        {
            //Read the data of the study from the local preference settings

        }
    }


}
