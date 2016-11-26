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
 * Created by cevincent on 13/07/2016.
 */
public class ActivitySurveyStart extends DotStepper {

    private static final String TAG = "ActivitySurveyStart";

    private int i = 1;

    private int SurveyInRelation = -1;
    private static int SurveyAge = 24;
    private static int SurveyLengthPeriod = 4;
    private static int SurveyLenghCycle = 29;
    private String SurveyContraception;
    private int SurveySocial1 = 0;
    private int SurveySocial2 = 0;
    private int SurveyMood1 = 0;
    private int SurveyMood2 = 0;
    private int SurveyMood3 = 0;

    private static final int SURVEY_AGE_START = 18;
    private static final int SURVEY_AGE_END = 40;
    private static final int SURVEY_DEFAULT_AGE = SurveyAge - SURVEY_AGE_START + 1;

    private static final int SURVEY_PERIOD_MIN = 1;
    private static final int SURVEY_PEIORD_MAX = 10;
    private static final int SURVEY_DEFAULT_PERIOD = SurveyLengthPeriod - SURVEY_PERIOD_MIN + 1;

    private static final int SURVEY_CYCLE_MIN = 10;
    private static final int SURVEY_CYCLE_MAX = 90;
    private static final int SURVEY_DEFAULT_CYCLE = SurveyLenghCycle - SURVEY_CYCLE_MIN + 1;

    private String DateStudyStart = null;
    private String TimeStudyStart = null;


    private ContentValues values = new ContentValues();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
    private SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

    private UtilsLocalDataBase localdatabase = null;
    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }
    private SettingsStudy ObjSettingsStudy;

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


    @Override
    public void onComplete() {
        super.onComplete();
        int resultInt =0;
        String resultString = null;

        //get data from the different steps
        SurveyInRelation = Step1.getArguments().getInt(FragmentSurvey2Choices.SURVEY_STATUS, 0);

        resultInt = Step2.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS, 0);
        if (resultInt != 0) {
            SurveyAge = resultInt;
        }
        resultInt = Step3.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS, 0);
        if (resultInt != 0) {
            SurveyLengthPeriod = resultInt;
        }
        resultInt = Step4.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS, 0);
        if (resultInt != 0) {
            SurveyLenghCycle = resultInt;
        }
        resultString  = Step5.getArguments().getString(FragmentSurveyText.SURVEY_STATUS, null);
        if (resultString != null) {
            SurveyContraception = resultString;
        }

        SurveySocial1 = Step6.getArguments().getInt(FragmentSurvey6Choices.SURVEY_STATUS, 0);
        SurveySocial2 = Step7.getArguments().getInt(FragmentSurvey6Choices.SURVEY_STATUS, 0);
        SurveyMood1 = Step8.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS1, 0);
        SurveyMood2 = Step8.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS2, 0);
        SurveyMood3 = Step8.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS3, 0);

        //Save those data in the local database
        values.clear();
        values.put(UtilsLocalDataBase.C_STARTSTUDY_AGE, SurveyAge);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_RELATIONSHIP, SurveyInRelation);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_AVGPERIODLENGHT, SurveyLengthPeriod);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_AVGMENSTRUALCYCLE, SurveyLenghCycle);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_CONTRACEPTION, SurveyContraception);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_SOCIAL1_LIFE, SurveySocial1);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_SOCIAL2_LIFE, SurveySocial2);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_MOOD1, SurveyMood1);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_MOOD2, SurveyMood2);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_MOOD3, SurveyMood3);

        DateStudyStart = sdf.format(new Date());
        values.put(UtilsLocalDataBase.C_STARTSTUDY_DATE, DateStudyStart);
        TimeStudyStart = shf.format(new Date());
        values.put(UtilsLocalDataBase.C_STARTSTUDY_TIME, TimeStudyStart);

        AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_START_STUDY);
        Log.d(TAG, "idUser: " + ObjSettingsStudy.getIdUser() + "Participant age: " + SurveyAge + " In a relationship: " + SurveyInRelation
        + " period lenght: " + SurveyLengthPeriod + " cycle lenght: " + SurveyLenghCycle + " Contraception used: " + SurveyContraception
        + " date start: " + DateStudyStart + " time start: " + TimeStudyStart);

        ObjSettingsStudy.setStartDateSurvey(DateStudyStart);
        ObjSettingsStudy.setStartSurveyDone(true);

        //We don't to trigger directly the daily survey so we fake it
        ObjSettingsStudy.setDailySurveyDone(true);

        Intent msgIntent = new Intent(getApplicationContext(), IntentServicePostData.class);
        //Start the service for sending the data to the remote server
        startService(msgIntent);

        Intent i = new Intent(ActivitySurveyStart.this, ActivityBeTrack.class);
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

        //Step 1 RELATIONSHIP
        bundle1 = new Bundle();
        bundle1.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen1));
        bundle1.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.question_ss_screen1));
        Step1 = new FragmentSurvey2Choices();
        Step1.setArguments(bundle1);
        addStep(Step1);

        //Step 2 AGE
        bundle2 = new Bundle();
        bundle2.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_ss_screen2));
        bundle2.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_ss_screen2));
        bundle2.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_UNIT, getResources().getString(R.string.survey_age));
        bundle2.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE, SURVEY_AGE_START);
        bundle2.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE, SURVEY_AGE_END);
        bundle2.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE, SURVEY_DEFAULT_AGE);
        Step2 = new FragmentSurveyScrolling();
        Step2.setArguments(bundle2);
        addStep(Step2);

        //Step 3 PERIOD
        bundle3 = new Bundle();
        bundle3.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_ss_screen3));
        bundle3.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_ss_screen3));
        bundle3.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_UNIT, getResources().getString(R.string.survey_days));
        bundle3.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE, SURVEY_PERIOD_MIN);
        bundle3.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE, SURVEY_PEIORD_MAX);
        bundle3.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE, SURVEY_DEFAULT_PERIOD);
        Step3 = new FragmentSurveyScrolling();
        Step3.setArguments(bundle3);
        addStep(Step3);

        //Step 4 CYCLE
        bundle4 = new Bundle();
        bundle4.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_ss_screen4));
        bundle4.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_ss_screen4));
        bundle4.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_UNIT, getResources().getString(R.string.survey_days));
        bundle4.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE, SURVEY_CYCLE_MIN);
        bundle4.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE, SURVEY_CYCLE_MAX);
        bundle4.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE, SURVEY_DEFAULT_CYCLE);
        Step4 = new FragmentSurveyScrolling();
        Step4.setArguments(bundle4);
        addStep(Step4);

        //Step 5 CONTRACEPTION
        bundle5 = new Bundle();
        bundle5.putString(FragmentSurveyText.SURVEY_TEXT_TITLE, getResources().getString(R.string.title_ss_screen5));
        bundle5.putString(FragmentSurveyText.SURVEY_TEXT_DESC, getResources().getString(R.string.question_ss_screen5));
        bundle5.putString(FragmentSurveyText.SURVEY_TEXT_COMMENT, getResources().getString(R.string.yourtext_ss_screen5));
        Step5 = new FragmentSurveyText();
        Step5.setArguments(bundle5);
        addStep(Step5);

        //Step 6 (social 1/2)
        bundle6 = new Bundle();
        bundle6.putString(FragmentSurvey6Choices.SURVEY_6_CHOICES_TITLE, getResources().getString(R.string.title_sd_screen3));
        bundle6.putString(FragmentSurvey6Choices.SURVEY_6_CHOICES_DESC, getResources().getString(R.string.question_sd_screen3));

        ArrayList<Integer> Icons1 = new ArrayList<Integer>() {{
            add(R.drawable.ic_work);
            add(R.drawable.ic_study);
            add(R.drawable.ic_read);
            add(R.drawable.ic_watchtv);
            add(R.drawable.ic_sports);
            add(R.drawable.ic_shopping);
        }};

        ArrayList<String> IconsText1 = new ArrayList<String>() {{
            add(getResources().getString(R.string.answer1_sd_screen3));
            add(getResources().getString(R.string.answer2_sd_screen3));
            add(getResources().getString(R.string.answer3_sd_screen3));
            add(getResources().getString(R.string.answer4_sd_screen3));
            add(getResources().getString(R.string.answer5_sd_screen3));
            add(getResources().getString(R.string.answer6_sd_screen3));
        }};

        bundle6.putIntegerArrayList(FragmentSurvey6Choices.SURVEY_6_CHOICES_ICON, Icons1);
        bundle6.putStringArrayList(FragmentSurvey6Choices.SURVEY_6_CHOICES_ICON_TEXT, IconsText1);

        Step6 = new FragmentSurvey6Choices();
        Step6.setArguments(bundle6);
        addStep(Step6);

        //Step 7 (social 2/2)
        bundle7 = new Bundle();
        bundle7.putString(FragmentSurvey6Choices.SURVEY_6_CHOICES_TITLE, getResources().getString(R.string.title_sd_screen4));
        bundle7.putString(FragmentSurvey6Choices.SURVEY_6_CHOICES_DESC, getResources().getString(R.string.question_sd_screen4));

        ArrayList<Integer> Icons2 = new ArrayList<Integer>() {{
            add(R.drawable.ic_meetfriends);
            add(R.drawable.ic_goout);
            add(R.drawable.ic_clubbing);
            add(R.drawable.ic_date);
            add(R.drawable.ic_sex);
            add(R.drawable.ic_kiss);
        }};

        ArrayList<String> IconsText2 = new ArrayList<String>() {{
            add(getResources().getString(R.string.answer1_sd_screen4));
            add(getResources().getString(R.string.answer2_sd_screen4));
            add(getResources().getString(R.string.answer3_sd_screen4));
            add(getResources().getString(R.string.answer4_sd_screen4));
            add(getResources().getString(R.string.answer5_sd_screen4));
            add(getResources().getString(R.string.answer6_sd_screen4));
        }};

        bundle7.putIntegerArrayList(FragmentSurvey6Choices.SURVEY_6_CHOICES_ICON, Icons2);
        bundle7.putStringArrayList(FragmentSurvey6Choices.SURVEY_6_CHOICES_ICON_TEXT, IconsText2);

        Step7 = new FragmentSurvey6Choices();
        Step7.setArguments(bundle7);
        addStep(Step7);

        //Step 8 (Mood)

        bundle8 = new Bundle();
        bundle8.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_TITLE, getResources().getString(R.string.title_sd_screen5));
        bundle8.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_DESC, getResources().getString(R.string.question_sd_screen5));

        ArrayList<String> MoodText1 = new ArrayList<String>() {{
            add(getResources().getString(R.string.answer1_sd_screen5));
            add(getResources().getString(R.string.answer2_sd_screen5));
            add(getResources().getString(R.string.answer3_sd_screen5));
        }};

        bundle8.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS, MoodText1);

        Step8 = new FragmentSurveySeekBar();
        Step8.setArguments(bundle8);
        addStep(Step8);

        //Step 9 NOTIFICATION TIME
        bundle9 = new Bundle();
        bundle9.putString(FragmentSurveyTimePicker.SURVEY_TIMEPICKER_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen6));
        bundle9.putString(FragmentSurveyTimePicker.SURVEY_TIMEPICKER_CHOICES_DESC, getResources().getString(R.string.question_ss_screen6));
        Step9 = new FragmentSurveyTimePicker();
        Step9.setArguments(bundle9);
        addStep(Step9);


        super.onCreate(savedInstanceState);
    }
}

