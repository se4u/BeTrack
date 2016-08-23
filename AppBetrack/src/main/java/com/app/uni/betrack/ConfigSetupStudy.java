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
public class ConfigSetupStudy {

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
            if (ConfigSettingsBetrack.SERVICE_TRACKING_NAME.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    NetworkGetWhatToWatch gwtw = new NetworkGetWhatToWatch(new NetworkGetWhatToWatch.AsyncResponse(){

        @Override
        public void processFinish(final String output) {

            new Handler().postDelayed(new Runnable() {


                @Override
                public void run() {

                    ActivityBeTrack.dialog.dismiss();
                    ActivityBeTrack.actionBar.show();

                    if (null != output) {
                        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity.getApplicationContext());
                        SharedPreferences.Editor editor = prefs.edit();
                        Set<String> hs = prefs.getStringSet(ConfigInfoStudy.APP_NAME_TO_WATCH, new HashSet<String>());
                        Set<String> in = new HashSet<String>(hs);

                        //Broadcast an event to start the tracking service if not yet started
                        if (!isMyServiceRunning()) {
                            Intent intent = new Intent();
                            intent.setAction(ConfigSettingsBetrack.BROADCAST_START_TRACKING_NAME);
                            mActivity.sendBroadcast(intent);
                        }

                        //Show the study screen
                        mActivity.findViewById(R.id.Layout_Welcome).setVisibility(View.INVISIBLE);
                        mActivity.findViewById(R.id.Layout_NetworkError).setVisibility(View.INVISIBLE);
                        mActivity.findViewById(R.id.Layout_Study).setVisibility(View.VISIBLE);

                        //Save the applications to watch in the preference file
                        for (int i=0; i< NetworkGetWhatToWatch.ContextInfoStudy.ApplicationsToWatch.size(); i++) {
                            in.add(NetworkGetWhatToWatch.ContextInfoStudy.ApplicationsToWatch.get(i));
                        }
                        editor.putStringSet(ConfigInfoStudy.APP_NAME_TO_WATCH, in);

                        //We save in the preference that a study has been started and is ongoing
                        editor.putBoolean(ConfigInfoStudy.STUDY_STARTED, true);
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

    public ConfigSetupStudy(SharedPreferences prefs, ConfigInfoStudy ContextInfoStudy) {

        //final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        SharedPreferences.Editor editor = prefs.edit();

        //Read the data of the study from the local preference settings
        ReadAppToWatch(ContextInfoStudy, prefs);

    }

    public ConfigSetupStudy(Activity context, ConfigInfoStudy ContextInfoStudy) {
        mActivity = context;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity.getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();

        if (false == ContextInfoStudy.StudyStarted) {
            try
            {
                NetworkGetWhatToWatch.ContextInfoStudy = ContextInfoStudy;

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

    public void ReadAppToWatch(ConfigInfoStudy ContextInfoStudy, SharedPreferences prefs)
    {
        Set<String> hs = prefs.getStringSet(ConfigInfoStudy.APP_NAME_TO_WATCH, new HashSet<String>());
        Set<String> in = new HashSet<String>(hs);
        Iterator<String> iterator = hs.iterator();

        while(iterator.hasNext()){

            String AppToWatch = iterator.next();

            ContextInfoStudy.ApplicationsToWatch.add(AppToWatch);
        }
    }
}

