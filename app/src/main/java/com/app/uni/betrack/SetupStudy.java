package com.app.uni.betrack;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;

import java.util.HashSet;
import java.util.Iterator;
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
    private static int TIME_OUT = 1000;
    private Activity mActivity = null;

    GetWhatToWatch gwtw = new GetWhatToWatch(new GetWhatToWatch.AsyncResponse(){

        @Override
        public void processFinish(final String output) {

            new Handler().postDelayed(new Runnable() {


                @Override
                public void run() {

                    if (null != output) {
                        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity.getApplicationContext());
                        SharedPreferences.Editor editor = prefs.edit();
                        Set<String> hs = prefs.getStringSet("AppNameToWatch", new HashSet<String>());
                        Set<String> in = new HashSet<String>(hs);
                        String StudyStatusKey = STUDY_STARTED;

                        //Broadcast an event to start the tracking service if not yet started

                        //Show the study screen
                        mActivity.findViewById(R.id.Layout_Welcome).setVisibility(View.INVISIBLE);
                        mActivity.findViewById(R.id.Layout_NetworkError).setVisibility(View.INVISIBLE);
                        mActivity.findViewById(R.id.Layout_Study).setVisibility(View.VISIBLE);

                        //Display the settignthe menu
                        mActivity.invalidateOptionsMenu();

                        //Save the applications to watch in the preference file
                        for (int i=0; i< GetWhatToWatch.ContextInfoStudy.ApplicationsToWatch.size(); i++) {
                            in.add(GetWhatToWatch.ContextInfoStudy.ApplicationsToWatch.get(i));
                        }
                        editor.putStringSet("AppNameToWatch", in);
                        //Read the data of the study from InfoStudy and update the local preference settings
                        //editor.putBoolean(StudyStatusKey, true);
                        //editor.commit();

                        //We save in the preference that a study has been started
                        //editor.putBoolean(InfoStudy.STUDY_ONGOING, true);
                        //editor.commit();
                    } else {
                        mActivity.findViewById(R.id.Layout_Welcome).setVisibility(View.INVISIBLE);
                        mActivity.findViewById(R.id.Layout_Study).setVisibility(View.INVISIBLE);
                        mActivity.findViewById(R.id.Layout_NetworkError).setVisibility(View.VISIBLE);
                    }

                }
            }, TIME_OUT);
        }}
    );

    public SetupStudy(SharedPreferences prefs, InfoStudy ContextInfoStudy) {
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
            ReadAppToWatch(ContextInfoStudy, prefs);
        }
    }

    public SetupStudy(Activity context, InfoStudy ContextInfoStudy) {
        mActivity = context;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity.getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();

        String StudyStatusKey = STUDY_STARTED;

        boolean StudyStatus = prefs.getBoolean(StudyStatusKey, false);
        if (false == StudyStatus) {
            try
            {
                GetWhatToWatch.ContextInfoStudy = ContextInfoStudy;

                //Read what to watch from the distant server
                gwtw.execute();

            }
            catch (Exception e) {

            }

        }
        else
        {
            ReadAppToWatch(ContextInfoStudy, prefs);
        }
    }

    private void ReadAppToWatch(InfoStudy ContextInfoStudy, SharedPreferences prefs)
    {
        Set<String> hs = prefs.getStringSet("AppNameToWatch", new HashSet<String>());
        Set<String> in = new HashSet<String>(hs);
        Iterator<String> iterator = hs.iterator();

        while(iterator.hasNext()){

            String AppToWatch = iterator.next();

            ContextInfoStudy.ApplicationsToWatch.add(AppToWatch);
        }
    }
}

