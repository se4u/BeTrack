package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.ProgressStepper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cevincent on 14/09/2016.
 */
public class ActivityStudyEnd extends AppCompatActivity {
    private static final String TAG = "ActivityStudyEnd";

    private String DateStudyEnd = null;
    private String TimeStudyEnd = null;

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
        super.onCreate(savedInstanceState);

        if (null == localdatabase) {
            localdatabase =  new UtilsLocalDataBase(this);
        }

        if (null == ObjSettingsStudy) {
            ObjSettingsStudy = SettingsStudy.getInstance(this);
        }

        values.clear();

        values.put(UtilsLocalDataBase.C_ENDSTUDY_AVERAGE_PERIODICITY, ObjSettingsStudy.getAveragePeriodicity());
        values.put(UtilsLocalDataBase.C_ENDSTUDY_STD_DEVIATION, ObjSettingsStudy.getStandardDeviation());
        values.put(UtilsLocalDataBase.C_ENDSTUDY_BETRACK_KILLED, ObjSettingsStudy.getBetrackKilled());

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            values.put(UtilsLocalDataBase.C_ENDSTUDY_BETRACK_POLLING, 1);
        } else {
            values.put(UtilsLocalDataBase.C_ENDSTUDY_BETRACK_POLLING, 0);
        }

        DateStudyEnd = sdf.format(new Date());
        values.put(UtilsLocalDataBase.C_ENDSTUDY_DATE, DateStudyEnd);
        TimeStudyEnd = shf.format(new Date());
        values.put(UtilsLocalDataBase.C_ENDSTUDY_TIME, TimeStudyEnd);

        AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_END_STUDY);

        Log.d(TAG, "setEndSurveyTransferred = IN_PROGRESS");
        ObjSettingsStudy.setEndSurveyTransferred(SettingsStudy.EndStudyTranferState.IN_PROGRESS);
        ObjSettingsStudy.setEndSurveyDone(true);
        ObjSettingsStudy.setLastDayStudyState(SettingsStudy.LastDayStudyState.END_SURVEY_DONE);

        Intent i = new Intent(this, ActivityBeTrack.class);
        startActivity(i);
        finish();
        System.out.println("SurveyEnd completed");
    }
}
