package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cevincent on 14/09/2016.
 */
public class ActivitySurveyEnd  extends DotStepper {
    private static final String TAG = "ActivitySurveyEnd";

    private int SurveyPeriod = -1;
    private int SurveyInRelation = -1;
    private String SurveyContraception = null;
    private String DateStudyEnd = null;

    private AbstractStep Step1;
    private Bundle bundle1;
    private AbstractStep Step2;
    private Bundle bundle2;
    private AbstractStep Step3;
    private Bundle bundle3;

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
        String resultString;

        SurveyPeriod = Step1.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS, 0);

        SurveyInRelation = Step2.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS, 0);

        resultString  = Step3.getArguments().getString(FragmentSurveyText.SURVEY_STATUS, null);
        if (resultString != null) {
            SurveyContraception = resultString;
        }

        values.clear();
        values.put(UtilsLocalDataBase.C_ENDSTUDY_RELATIONSHIP, SurveyInRelation);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_CONTRACEPTION, SurveyContraception);
        DateStudyEnd = sdf.format(new Date());
        values.put(UtilsLocalDataBase.C_ENDSTUDY_DATE, DateStudyEnd);

        AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_END_STUDY);
        Log.d(TAG, "idUser: " + ObjSettingsStudy.getIdUser()
                + " In a relationship: " + SurveyInRelation
                + " Contraception used: " + SurveyContraception
                + " Date end of the study: " + DateStudyEnd);

        Intent msgIntent = new Intent(getApplicationContext(), IntentServicePostData.class);
        //Start the service for sending the data to the remote server
        startService(msgIntent);

        Intent i = new Intent(ActivitySurveyEnd.this, ActivityBeTrack.class);
        startActivity(i);
        finish();
        ObjSettingsStudy.setEndSurveyDone(true);
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
        bundle1.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_se_screen1));
        bundle1.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.question_se_screen1));
        Step1 = new FragmentSurvey2Choices();
        Step1.setArguments(bundle1);
        addStep(Step1);

        //Step 2 RELATIONSHIP
        bundle2 = new Bundle();
        bundle2.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_se_screen2));
        bundle2.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.question_se_screen2));
        Step2 = new FragmentSurvey2Choices();
        Step2.setArguments(bundle2);
        addStep(Step2);

        //Step 3 CONTRACEPTION
        bundle3 = new Bundle();
        bundle3.putString(FragmentSurveyText.SURVEY_TEXT_TITLE, getResources().getString(R.string.title_se_screen3));
        bundle3.putString(FragmentSurveyText.SURVEY_TEXT_DESC, getResources().getString(R.string.question_se_screen3));
        bundle3.putString(FragmentSurveyText.SURVEY_TEXT_COMMENT, getResources().getString(R.string.yourtext_se_screen3));
        Step3 = new FragmentSurveyText();
        Step3.setArguments(bundle3);
        addStep(Step3);

        super.onCreate(savedInstanceState);
    }
}
