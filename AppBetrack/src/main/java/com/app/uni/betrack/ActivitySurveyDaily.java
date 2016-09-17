package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cevincent on 16/09/2016.
 */
public class ActivitySurveyDaily   extends DotStepper {
    private static final String TAG = "ActivitySurveyEnd";

    private int SurveyPeriod = -1;
    private String DateDaily = null;

    private AbstractStep Step1;
    private Bundle bundle1;

    private ContentValues values = new ContentValues();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
    private UtilsLocalDataBase localdatabase = null;
    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }
    private SettingsStudy ObjSettingsStudy;

    @Override
    public void onComplete() {
        super.onComplete();

        SurveyPeriod = Step1.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS, 0);
        values.clear();
        values.put(UtilsLocalDataBase.C_USER_PERIOD, SurveyPeriod);
        DateDaily = sdf.format(new Date());
        values.put(UtilsLocalDataBase.C_USER_DATE, DateDaily);

        AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_USER);
        Log.d(TAG, "idUser: " + ObjSettingsStudy.getIdUser()
                + " Period: " + SurveyPeriod
                + " Date: " + DateDaily);


        ObjSettingsStudy.setDailySurveyDone(true);
        ObjSettingsStudy.setLastDateDailySurvey(DateDaily);

        Intent msgIntent = new Intent(getApplicationContext(), IntentServicePostData.class);
        //Start the service for sending the data to the remote server
        startService(msgIntent);

        Intent i = new Intent(ActivitySurveyDaily.this, ActivityBeTrack.class);
        startActivity(i);
        finish();
        System.out.println("completed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setErrorTimeout(1500);
        setTitle(getResources().getString(R.string.app_name));

        if (null == localdatabase) {
            localdatabase =  new UtilsLocalDataBase(this);
        }

        if (null == ObjSettingsStudy) {
            ObjSettingsStudy = SettingsStudy.getInstance(this);
        }

        //Step 1 PERIOD
        bundle1 = new Bundle();
        bundle1.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_sd_screen1));
        bundle1.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.question_sd_screen1));
        Step1 = new FragmentSurvey2Choices();
        Step1.setArguments(bundle1);
        addStep(Step1);

        super.onCreate(savedInstanceState);
    }
}
