package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;
import com.github.fcannizzaro.materialstepper.style.ProgressStepper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cevincent on 13/07/2016.
 */
public class ActivitySurveyStart extends AppCompatActivity {

    private static final String TAG = "ActivitySurveyStart";

    private String DateStudyStart = null;

    private ContentValues values = new ContentValues();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
    private SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

    private UtilsLocalDataBase localdatabase = null;
    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }
    private SettingsStudy ObjSettingsStudy = null;
    private SettingsBetrack ObjSettingsBetrack = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle(getResources().getString(R.string.app_name));

        if (null == localdatabase) {
            localdatabase =  new UtilsLocalDataBase(this);
        }

        if (null == ObjSettingsStudy) {
            ObjSettingsStudy = SettingsStudy.getInstance(this);
        }

        if (null == ObjSettingsBetrack) {
            //Read the preferences
            ObjSettingsBetrack = SettingsBetrack.getInstance();
            ObjSettingsBetrack.Update(this);
        }


        ObjSettingsStudy.setStartDateSurvey(DateStudyStart);
        ObjSettingsStudy.setStartSurveyDone(true);

        //Save the number of notification for the study (match the number of day of the study)
        ObjSettingsStudy.setNbrOfNotificationToDo(ObjSettingsStudy.getStudyDuration());

        Intent msgIntent = new Intent(getApplicationContext(), IntentServicePostData.class);
        //Start the service for sending the data to the remote server
        startService(msgIntent);

        Intent i = new Intent(ActivitySurveyStart.this, ActivityBeTrack.class);
        startActivity(i);
        finish();

        //Save that the study was just started
        SettingsBetrack.STUDY_JUST_STARTED = true;

        System.out.println("completed");

        super.onCreate(savedInstanceState);
    }
}

