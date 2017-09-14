package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cevincent on 16/09/2016.
 */
public class ActivitySurveyDaily extends AppCompatActivity {
    private static final String TAG = "ActivitySurveyDaily";


    private ContentValues values = new ContentValues();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
    private SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

    private UtilsLocalDataBase localdatabase = null;
    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }
    private SettingsStudy ObjSettingsStudy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (null == localdatabase) {
            localdatabase =  new UtilsLocalDataBase(this);
        }

        if (null == ObjSettingsStudy) {
            ObjSettingsStudy = SettingsStudy.getInstance(this);
        }

        ObjSettingsStudy.setDailySurveyDone(true);

        Intent msgIntent = new Intent(getApplicationContext(), IntentServicePostData.class);
        //Start the service for sending the data to the remote server
        startService(msgIntent);


        if (ObjSettingsStudy.getNbrOfNotificationToDo() == 0) {
            ObjSettingsStudy.setLastDayStudyState(SettingsStudy.LastDayStudyState.START_SURVEY_DONE);
        }

        Intent i = new Intent(this, ActivityBeTrack.class);
        startActivity(i);
        finish();
        System.out.println("SurveyDaily completed");

        super.onCreate(savedInstanceState);
    }
}
