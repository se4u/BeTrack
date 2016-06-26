package com.app.uni.betrack;


import android.app.Activity;
import android.app.ActivityManager;
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

    private String STUDY_ID = "StudyId";
    private String STUDY_NAME = "StudyName";
    private String STUDY_VERSIONAPP = "VersionApp";
    private String STUDY_DURATION = "Duration";
    private String STUDY_PUBLICKEY = "PublicKey";
    private String STUDY_CONTACTEMAIL = "ContactEmail";
    private String STUDY_LINKENDSTUDY = "LinkEndStudy";
    private static int TIME_OUT = 1000;
    private Activity mActivity = null;

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) mActivity.getSystemService(mActivity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SettingsBetrack.SERVICE_TRACKING_NAME.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

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

                        //Broadcast an event to start the tracking service if not yet started
                        if (!isMyServiceRunning()) {
                            Intent intent = new Intent();
                            intent.setAction(SettingsBetrack.BROADCAST_START_TRACKING_NAME);
                            mActivity.sendBroadcast(intent);
                        }

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

                        //We save in the preference that a study has been started and is ongoing
                        editor.putBoolean(InfoStudy.STUDY_STARTED, true);
                        editor.commit();
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

        //final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        SharedPreferences.Editor editor = prefs.edit();

        //Read the data of the study from the local preference settings
        ReadAppToWatch(ContextInfoStudy, prefs);

    }

    public SetupStudy(Activity context, InfoStudy ContextInfoStudy) {
        mActivity = context;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity.getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();

        if (false == ContextInfoStudy.StudyStarted) {
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

    public void ReadAppToWatch(InfoStudy ContextInfoStudy, SharedPreferences prefs)
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

