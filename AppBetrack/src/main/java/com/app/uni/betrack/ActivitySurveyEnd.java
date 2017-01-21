package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cevincent on 14/09/2016.
 */
public class ActivitySurveyEnd  extends DotStepper {
    private static final String TAG = "ActivitySurveyEnd";

    private int SurveyPeriod = -1;
    private int SurveySocial1 = 0;
    private int SurveySocial2 = 0;
    private int SurveyMood1 = 0;
    private int SurveyMood2 = 0;
    private int SurveyMood3 = 0;
    private int SurveyInRelation = -1;
    private String SurveyContraception = null;
    private int PhoneUsage = 0;
    private String DateStudyEnd = null;
    private String TimeStudyEnd = null;

    private AbstractStep Step1;
    private Bundle bundle1;
    private AbstractStep Step2;
    private Bundle bundle2;
    private AbstractStep Step3;
    private Bundle bundle3;
    private AbstractStep Step4;
    private Bundle bundle4;
    private AbstractStep Step5;
    private Bundle bundle5;
    private AbstractStep Step6;
    private Bundle bundle6;
    private AbstractStep Step7;
    private Bundle bundle7;
    private AbstractStep Step8;
    private Bundle bundle8;
    private AbstractStep Step9;
    private Bundle bundle9;

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
    public void onComplete() {
        super.onComplete();

        SurveyPeriod = Step1.getArguments().getInt(FragmentSurvey2Choices.SURVEY_STATUS, 0);
        SurveySocial1 = Step2.getArguments().getInt(FragmentSurvey6Choices.SURVEY_STATUS, 0);
        SurveySocial2 = Step3.getArguments().getInt(FragmentSurvey6Choices.SURVEY_STATUS, 0);
        SurveyMood1 = Step4.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS1, 0);
        SurveyMood2 = Step4.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS2, 0);
        SurveyMood3 = Step4.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS3, 0);
        SurveyInRelation = Step5.getArguments().getInt(FragmentSurvey2Choices.SURVEY_STATUS, 0);
        SurveyContraception  = Step6.getArguments().getString(FragmentSurveyText.SURVEY_STATUS, null);

        values.clear();
        values.put(UtilsLocalDataBase.C_ENDSTUDY_PERIOD, SurveyPeriod);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_SOCIAL1_LIFE, SurveySocial1);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_SOCIAL2_LIFE, SurveySocial2);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_MOOD1, SurveyMood1);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_MOOD2, SurveyMood2);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_MOOD3, SurveyMood3);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_RELATIONSHIP, SurveyInRelation);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_CONTRACEPTION, SurveyContraception);

        DateStudyEnd = sdf.format(new Date());
        values.put(UtilsLocalDataBase.C_ENDSTUDY_DATE, DateStudyEnd);
        TimeStudyEnd = shf.format(new Date());
        values.put(UtilsLocalDataBase.C_ENDSTUDY_TIME, TimeStudyEnd);

        AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_END_STUDY);

        Log.d(TAG, "setEndSurveyTransferred = IN_PROGRESS");
        ObjSettingsStudy.setEndSurveyTransferred(SettingsStudy.EndStudyTranferState.IN_PROGRESS);

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

        //Step 1 RELATIONSHIP
        bundle1 = new Bundle();
        bundle1.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_TITLE, getResources().getString(R.string.title_se_screen1));
        bundle1.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_DESC, getResources().getString(R.string.question_se_screen1));
        ArrayList<String> RbText1 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_se_screen1));
            add(getResources().getString(R.string.option2_se_screen1));
            add(getResources().getString(R.string.option3_se_screen1));
        }};

        ArrayList<Integer> NextStep1 = new ArrayList<Integer>() {{
            add(1); //Optional step visible
            add(1); //Optional step visible
            add(1); //Optional step visible
        }};

        bundle1.putStringArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_RB_TEXT, RbText1);
        bundle1.putIntegerArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_ENABLE_NEXT_STEP, NextStep1);

        Step1 = new FragmentSurvey10Choices();
        Step1.setArguments(bundle1);
        addStep(Step1, true);

        //Step 2 CONTRACEPTION
        bundle2 = new Bundle();
        bundle2.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_se_screen2));
        bundle2.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.question_se_screen2));
        Step2 = new FragmentSurvey2Choices();
        Step2.setArguments(bundle2);
        addStep(Step2, true);

        //Step 3 TINDER
        bundle3 = new Bundle();
        bundle3.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_TITLE, getResources().getString(R.string.title_se_screen3));
        bundle3.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_DESC, getResources().getString(R.string.question_se_screen3));
        ArrayList<String> RbText3 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_se_screen3));
            add(getResources().getString(R.string.option2_se_screen3));
            add(getResources().getString(R.string.option3_se_screen3));
            add(getResources().getString(R.string.option4_se_screen3));
            add(getResources().getString(R.string.option5_se_screen3));
        }};

        ArrayList<Integer> NextStep3 = new ArrayList<Integer>() {{
            add(1); //Optional step visible
            add(1); //Optional step visible
            add(1); //Optional step visible
            add(1); //Optional step visible
            add(1); //Optional step visible
        }};

        bundle3.putStringArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_RB_TEXT, RbText3);
        bundle3.putIntegerArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_ENABLE_NEXT_STEP, NextStep3);

        Step3 = new FragmentSurvey10Choices();
        Step3.setArguments(bundle3);
        addStep(Step3, true);

        //Step 4 PHONE USAGE
        bundle4 = new Bundle();
        bundle4.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_TITLE, getResources().getString(R.string.title_se_screen4));
        bundle4.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_DESC, getResources().getString(R.string.question_se_screen4));
        ArrayList<String> RbText4 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_se_screen4));
            add(getResources().getString(R.string.option2_se_screen4));
            add(getResources().getString(R.string.option3_se_screen4));
        }};

        ArrayList<Integer> NextStep4 = new ArrayList<Integer>() {{
            add(1); //Optional step visible
            add(1); //Optional step visible
            add(1); //Optional step visible
        }};

        bundle4.putStringArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_RB_TEXT, RbText4);
        bundle4.putIntegerArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_ENABLE_NEXT_STEP, NextStep4);

        Step4 = new FragmentSurvey10Choices();
        Step4.setArguments(bundle4);
        addStep(Step4, true);

        //Step 5 STUDY 1
        bundle5 = new Bundle();
        bundle5.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_TITLE, getResources().getString(R.string.title_se_screen5));
        bundle5.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_DESC, getResources().getString(R.string.question_se_screen5));

        ArrayList<String> ChoiceText1 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_se_screen5));
        }};

        bundle5.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS, ChoiceText1);

        Step5 = new FragmentSurveySeekBar();
        Step5.setArguments(bundle5);
        addStep(Step5, true);

        //Step 6 STUDY 2
        bundle6 = new Bundle();
        bundle6.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_TITLE, getResources().getString(R.string.title_se_screen6));
        bundle6.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_DESC, getResources().getString(R.string.question_se_screen6));

        ArrayList<String> ChoiceText2 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_se_screen6));
        }};

        bundle6.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS, ChoiceText2);

        Step6 = new FragmentSurveySeekBar();
        Step6.setArguments(bundle6);
        addStep(Step6, true);

        //Step 7 STUDY 3
        bundle7 = new Bundle();
        bundle7.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_TITLE, getResources().getString(R.string.title_se_screen7));
        bundle7.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_DESC, getResources().getString(R.string.question_se_screen7));

        ArrayList<String> ChoiceText3 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_se_screen7));
        }};

        bundle7.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS, ChoiceText3);

        Step7 = new FragmentSurveySeekBar();
        Step7.setArguments(bundle7);
        addStep(Step7, true);

        //Step 8 RESEARCH APP 1
        bundle8 = new Bundle();
        bundle8.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_TITLE, getResources().getString(R.string.title_se_screen8));
        bundle8.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_DESC, getResources().getString(R.string.question_se_screen8));

        ArrayList<String> ChoiceText4 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_se_screen8));
        }};

        bundle8.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS, ChoiceText4);

        Step8 = new FragmentSurveySeekBar();
        Step8.setArguments(bundle8);
        addStep(Step8, true);

        //Step 9 RESEARCH APP 2
        bundle9 = new Bundle();
        bundle9.putString(FragmentSurveyText.SURVEY_TEXT_TITLE, getResources().getString(R.string.title_ss_screen9));
        bundle9.putString(FragmentSurveyText.SURVEY_TEXT_DESC, getResources().getString(R.string.question_ss_screen9));
        bundle9.putString(FragmentSurveyText.SURVEY_TEXT_COMMENT, null);
        bundle9.putBoolean(FragmentSurveyText.SURVEY_TEXT_IS_OPTIONAL, true);
        Step9 = new FragmentSurveyText();
        Step9.setArguments(bundle9);
        addStep(Step9, true);

        super.onCreate(savedInstanceState);
    }
}
