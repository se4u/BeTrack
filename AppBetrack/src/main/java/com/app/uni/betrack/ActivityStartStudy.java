package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityStartStudy extends AppCompatActivity {

    private AbstractStep Step1;
    private Bundle bundle1;

    private AbstractStep Step2;
    private Bundle bundle2;

    private AbstractStep Step3;
    private Bundle bundle3;


    private SettingsStudy ObjSettingsStudy;
    private static UtilsLocalDataBase localdatabase =  null;

    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String ActivityStartDate = "";
        String ActivityStartTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

        ContentValues values = new ContentValues();

        if (null == localdatabase) {
            localdatabase =  new UtilsLocalDataBase(this);
        }

        if (null == ObjSettingsStudy) {
            ObjSettingsStudy = SettingsStudy.getInstance(this);
        }


        //We have all the information to start the study
        ObjSettingsStudy.setStudyStarted(true);
        if (ObjSettingsStudy.getSetupBetrackDone() == false) {
            i = new Intent(ActivityStartStudy.this, ActivitySetupBetrack.class);
        } else {
            i = new Intent(ActivityStartStudy.this, ActivitySurveyStart.class);
        }

        values.clear();
        //Save the date
        ActivityStartDate = sdf.format(new Date());
        //Save the time
        ActivityStartTime = shf.format(new Date());
        values.put(UtilsLocalDataBase.C_NOTIFICATION_TIME, "20:00");
        values.put(UtilsLocalDataBase.C_NOTIFICATION_TIME_DATE, ActivityStartDate);
        values.put(UtilsLocalDataBase.C_NOTIFICATION_TIME_TIME, ActivityStartTime);
        try {
            AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_NOTIFICATION_TIME);
        } catch (Exception f) {}

        startActivity(i);
        finish();
        super.onCreate(savedInstanceState);
    }
}
