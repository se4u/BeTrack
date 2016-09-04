package com.app.uni.betrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class ActivityStartStudy extends AppCompatActivity {

    public static String STATUS_START_ACTIVITY = "STATUS_START_ACTIVITY";
    public static String NETWORK_ERROR = "NETWORK_ERROR";
    public static String NETWORK_OK = "NETWORK_OK";

    private enum startStudyState {
        NETWORK_ERROR_FROM_GET_STUDIES,
        NETWORK_ERROR_FROM_GET_INFO_STUDY,
        DISCLAIMER,
        STUDY
    };

    private startStudyState studyStateMachine;
    private SettingsStudy ObjSettingsStudy;

    private NetworkGetStudiesAvailable gsa = new NetworkGetStudiesAvailable(this, new NetworkGetStudiesAvailable.AsyncResponse(){

        @Override
        public void processFinish(final String output) {
            if (null != output) {
                //This time we manage to read the studies from the distant server
                studyStateMachine = startStudyState.DISCLAIMER;
                findViewById(R.id.Layout_Disclaimer).setVisibility(View.VISIBLE);
                findViewById(R.id.Layout_StudyDescription).setVisibility(View.INVISIBLE);
                findViewById(R.id.Layout_NetworkError).setVisibility(View.INVISIBLE);
                findViewById(R.id.Layout_NetworkLoading).setVisibility(View.INVISIBLE);

            } else {
                //Still not access to the server...
                studyStateMachine = startStudyState.NETWORK_ERROR_FROM_GET_STUDIES;
                findViewById(R.id.Layout_NetworkError).setVisibility(View.VISIBLE);
                findViewById(R.id.Layout_Disclaimer).setVisibility(View.INVISIBLE);
                findViewById(R.id.Layout_StudyDescription).setVisibility(View.INVISIBLE);
                findViewById(R.id.Layout_NetworkLoading).setVisibility(View.INVISIBLE);
            }
        }

    });

    private NetworkGetWhatToWatch gwtw = new NetworkGetWhatToWatch(this, new NetworkGetWhatToWatch.AsyncResponse(){

        @Override
        public void processFinish(final String output) {
            if (null != output) {
                //We have all the information to start the study, we wan display the start survey
                ObjSettingsStudy.setStudyStarted(true);
                Intent i = new Intent(ActivityStartStudy.this, ActivitySetupBetrack.class);
                startActivity(i);
                finish();
            } else {
                //We didn't not manage to read the information from the distant server
                studyStateMachine = startStudyState.NETWORK_ERROR_FROM_GET_INFO_STUDY;
                findViewById(R.id.Layout_NetworkError).setVisibility(View.VISIBLE);
                findViewById(R.id.Layout_Disclaimer).setVisibility(View.INVISIBLE);
                findViewById(R.id.Layout_StudyDescription).setVisibility(View.INVISIBLE);
                findViewById(R.id.Layout_NetworkLoading).setVisibility(View.INVISIBLE);
            }
        }}
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String networkStatus;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_study);

        Bundle extras = getIntent().getExtras();

        ObjSettingsStudy = SettingsStudy.getInstance(this);

        if(extras == null) {
            networkStatus= NETWORK_ERROR;
        } else {
            networkStatus= extras.getString(STATUS_START_ACTIVITY);
        }

        //Check if we manage to read the available study
        if (networkStatus.equals(NETWORK_OK)) {
            //We manage to read the study available we display the disclaimer
            studyStateMachine = startStudyState.DISCLAIMER;
            findViewById(R.id.Layout_Disclaimer).setVisibility(View.VISIBLE);
            findViewById(R.id.Layout_StudyDescription).setVisibility(View.INVISIBLE);
            findViewById(R.id.Layout_NetworkError).setVisibility(View.INVISIBLE);
            findViewById(R.id.Layout_NetworkLoading).setVisibility(View.INVISIBLE);
        }
        else {
            //We didn't manage to read available study, we display an error message
            studyStateMachine = startStudyState.NETWORK_ERROR_FROM_GET_STUDIES;
            findViewById(R.id.Layout_NetworkError).setVisibility(View.VISIBLE);
            findViewById(R.id.Layout_Disclaimer).setVisibility(View.INVISIBLE);
            findViewById(R.id.Layout_StudyDescription).setVisibility(View.INVISIBLE);
            findViewById(R.id.Layout_NetworkLoading).setVisibility(View.INVISIBLE);
        }

    }

    public void onButtonClicked(View view) {
        switch (view.getId()) {
            case  R.id.DisclaimerAccept:
                studyStateMachine = startStudyState.STUDY;
                //Update the study screen before we display it
                TextView StudyTitle = new TextView(this);
                StudyTitle = (TextView) findViewById(R.id.study_title);
                StudyTitle.setText(ObjSettingsStudy.getStudyName());

                TextView StudyDescription = new TextView(this);
                StudyDescription = (TextView) findViewById(R.id.study_description);
                StudyDescription.setText(ObjSettingsStudy.getStudyDescription());

                findViewById(R.id.Layout_Disclaimer).setVisibility(View.INVISIBLE);
                findViewById(R.id.Layout_StudyDescription).setVisibility(View.VISIBLE);
                findViewById(R.id.Layout_NetworkError).setVisibility(View.INVISIBLE);
                findViewById(R.id.Layout_NetworkLoading).setVisibility(View.INVISIBLE);
                break;
            case  R.id.DisclaimerRefuse:
                // Disclaimer refused we close this activity
                finish();
                break;
            case  R.id.StudyAccept:
                //Read the data from the distant server
                gwtw.execute();

                findViewById(R.id.Layout_Disclaimer).setVisibility(View.INVISIBLE);
                findViewById(R.id.Layout_StudyDescription).setVisibility(View.INVISIBLE);
                findViewById(R.id.Layout_NetworkError).setVisibility(View.INVISIBLE);
                findViewById(R.id.Layout_NetworkLoading).setVisibility(View.VISIBLE);
                break;
            case  R.id.StudyRefuse:
                // Study refused we close this activity
                finish();
                break;
            case R.id.ButtonNetworkRetry:
                if (studyStateMachine == startStudyState.NETWORK_ERROR_FROM_GET_STUDIES) {
                    //Read the data from the distant server
                    gsa.execute();
                } else if (studyStateMachine == startStudyState.NETWORK_ERROR_FROM_GET_INFO_STUDY) {
                    //Read the data from the distant server
                    gwtw.execute();
                } else {
                    //Should never happen...
                }
                findViewById(R.id.Layout_Disclaimer).setVisibility(View.INVISIBLE);
                findViewById(R.id.Layout_StudyDescription).setVisibility(View.INVISIBLE);
                findViewById(R.id.Layout_NetworkError).setVisibility(View.INVISIBLE);
                findViewById(R.id.Layout_NetworkLoading).setVisibility(View.VISIBLE);
                break;
        }
    }
}
