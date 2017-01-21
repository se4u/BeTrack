package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
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
public class ActivitySurveyStart extends ProgressStepper {

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
    private SettingsStudy ObjSettingsStudy = null;
    private SettingsBetrack ObjSettingsBetrack = null;

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
    private AbstractStep Step10;
    private Bundle bundle10;
    private AbstractStep Step11;
    private Bundle bundle11;
    private AbstractStep Step12;
    private Bundle bundle12;
    private AbstractStep Step13;
    private Bundle bundle13;
    private AbstractStep Step14;
    private Bundle bundle14;
    private AbstractStep Step15;
    private Bundle bundle15;
    private AbstractStep Step16;
    private Bundle bundle16;
    private AbstractStep Step17;
    private Bundle bundle17;
    private AbstractStep Step18;
    private Bundle bundle18;
    private AbstractStep Step19;
    private Bundle bundle19;
    private AbstractStep Step20;
    private Bundle bundle20;
    private AbstractStep Step21;
    private Bundle bundle21;

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
        resultInt = Step4.getArguments().getInt(FragmentSurvey10Choices.SURVEY_STATUS, 0);
        if (resultInt != 0) {
            SurveyLenghCycle = resultInt;
        }
        resultString  = Step5.getArguments().getString(FragmentSurveyDatePicker.SURVEY_STATUS, null);
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

        //Save the number of notification for the study (match the number of day of the study)
        ObjSettingsStudy.setNbrOfNotificationToDo(ObjSettingsStudy.getStudyDuration());

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

        if (null == ObjSettingsBetrack) {
            //Read the preferences
            ObjSettingsBetrack = SettingsBetrack.getInstance();
            ObjSettingsBetrack.Update(this);
        }

        //Step 1 AGE
        bundle1 = new Bundle();
        bundle1.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_ss_screen1));
        bundle1.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_ss_screen1));
        bundle1.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_UNIT, getResources().getString(R.string.survey_age));
        bundle1.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE, SURVEY_AGE_START);
        bundle1.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE, SURVEY_AGE_END);
        bundle1.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE, SURVEY_DEFAULT_AGE);
        Step1 = new FragmentSurveyScrolling();
        Step1.setArguments(bundle1);
        addStep(Step1, true);

        //Step 2 ETHNICITY 1
        bundle2 = new Bundle();
        bundle2.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen2));
        bundle2.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_DESC, getResources().getString(R.string.question_ss_screen2));
        ArrayList<String> RbText2 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_ss_screen2));
            add(getResources().getString(R.string.option2_ss_screen2));
            add(getResources().getString(R.string.option3_ss_screen2));
            add(getResources().getString(R.string.option4_ss_screen2));
            add(getResources().getString(R.string.option5_ss_screen2));
            add(getResources().getString(R.string.option6_ss_screen2));
            add(getResources().getString(R.string.option7_ss_screen2));
            add(getResources().getString(R.string.option8_ss_screen2));
            add(getResources().getString(R.string.option9_ss_screen2));
        }};

        ArrayList<Integer> NextStep2 = new ArrayList<Integer>() {{
            add(0); //Optional step hidden
            add(0); //Optional step hidden
            add(0); //Optional step hidden
            add(0); //Optional step hidden
            add(0); //Optional step hidden
            add(0); //Optional step hidden
            add(0); //Optional step hidden
            add(0); //Optional step hidden
            add(1); //Optional step visible
        }};

        bundle2.putStringArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_RB_TEXT, RbText2);
        bundle2.putIntegerArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_ENABLE_NEXT_STEP, NextStep2);

        Step2 = new FragmentSurvey10Choices();
        Step2.setArguments(bundle2);
        addStep(Step2, true);

        //Step 3 ETHNICITY 2
        bundle3 = new Bundle();
        bundle3.putString(FragmentSurveyText.SURVEY_TEXT_TITLE, getResources().getString(R.string.title_ss_screen3));
        bundle3.putString(FragmentSurveyText.SURVEY_TEXT_DESC, getResources().getString(R.string.question_ss_screen3));
        bundle3.putString(FragmentSurveyText.SURVEY_TEXT_COMMENT, null);
        bundle3.putBoolean(FragmentSurveyText.SURVEY_TEXT_IS_OPTIONAL, false);
        Step3 = new FragmentSurveyText();
        Step3.setArguments(bundle3);
        addStep(Step3, false);

        //Step 4 STUDENT
        bundle4 = new Bundle();
        bundle4.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen4));
        bundle4.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.title_ss_screen4));
        Step4 = new FragmentSurvey2Choices();
        Step4.setArguments(bundle4);
        addStep(Step4, true);

        //Step 5 ENGLISH LEVEL 1
        bundle5 = new Bundle();
        bundle5.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen5));
        bundle5.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.title_ss_screen5));
        Step5 = new FragmentSurvey2Choices();
        Step5.setArguments(bundle5);
        addStep(Step5, true);

        //Step 6 ENGLISH LEVEL 2
        bundle6 = new Bundle();
        bundle6.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen6));
        bundle6.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_DESC, getResources().getString(R.string.question_ss_screen6));
        ArrayList<String> RbText6 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_ss_screen6));
            add(getResources().getString(R.string.option2_ss_screen6));
            add(getResources().getString(R.string.option3_ss_screen6));
            add(getResources().getString(R.string.option4_ss_screen6));
        }};

        ArrayList<Integer> NextStep6 = new ArrayList<Integer>() {{
            add(1); //Optional step visible
            add(1); //Optional step visible
            add(1); //Optional step visible
            add(1); //Optional step visible
        }};

        bundle6.putStringArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_RB_TEXT, RbText6);
        bundle6.putIntegerArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_ENABLE_NEXT_STEP, NextStep6);

        Step6 = new FragmentSurvey10Choices();
        Step6.setArguments(bundle6);
        addStep(Step6, true);

        //Step 7 ENGLISH LEVEL 3
        bundle7 = new Bundle();
        bundle7.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen7));
        bundle7.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.title_ss_screen7));
        Step7 = new FragmentSurvey2Choices();
        Step7.setArguments(bundle7);
        addStep(Step7, true);

        //Step 8 ENGLISH LEVEL 4
        bundle8 = new Bundle();
        bundle8.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen8));
        bundle8.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_DESC, getResources().getString(R.string.question_ss_screen8));
        ArrayList<String> RbText8 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_ss_screen8));
            add(getResources().getString(R.string.option2_ss_screen8));
        }};

        ArrayList<Integer> NextStep8 = new ArrayList<Integer>() {{
            add(1); //Optional step visible
            add(1); //Optional step visible
        }};

        bundle8.putStringArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_RB_TEXT, RbText8);
        bundle8.putIntegerArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_ENABLE_NEXT_STEP, NextStep8);

        Step8 = new FragmentSurvey10Choices();
        Step8.setArguments(bundle8);
        addStep(Step8, true);

        //Step 9 ENGLISH LEVEL 5
        bundle9 = new Bundle();
        bundle9.putString(FragmentSurveyText.SURVEY_TEXT_TITLE, getResources().getString(R.string.title_ss_screen9));
        bundle9.putString(FragmentSurveyText.SURVEY_TEXT_DESC, getResources().getString(R.string.question_ss_screen9));
        bundle9.putString(FragmentSurveyText.SURVEY_TEXT_COMMENT, null);
        bundle9.putBoolean(FragmentSurveyText.SURVEY_TEXT_IS_OPTIONAL, false);
        Step9 = new FragmentSurveyText();
        Step9.setArguments(bundle9);
        addStep(Step9, false);

        //Step 10 ENGLISH LEVEL 6
        bundle10 = new Bundle();
        bundle10.putString(FragmentSurveyText.SURVEY_TEXT_TITLE, getResources().getString(R.string.title_ss_screen10));
        bundle10.putString(FragmentSurveyText.SURVEY_TEXT_DESC, getResources().getString(R.string.question_ss_screen10));
        bundle10.putString(FragmentSurveyText.SURVEY_TEXT_COMMENT, null);
        bundle10.putBoolean(FragmentSurveyText.SURVEY_TEXT_IS_OPTIONAL, false);
        Step10 = new FragmentSurveyText();
        Step10.setArguments(bundle10);
        addStep(Step10, false);

        //Step 11 UNIVERSITY 1
        bundle11 = new Bundle();
        bundle11.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen11));
        bundle11.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_DESC, getResources().getString(R.string.question_ss_screen11));
        ArrayList<String> RbText11 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_ss_screen11));
            add(getResources().getString(R.string.option2_ss_screen11));
            add(getResources().getString(R.string.option3_ss_screen11));
            add(getResources().getString(R.string.option4_ss_screen11));
        }};

        ArrayList<Integer> NextStep11 = new ArrayList<Integer>() {{
            add(1); //Optional step visible
            add(1); //Optional step visible
            add(1); //Optional step visible
            add(1); //Optional step visible
        }};

        bundle11.putStringArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_RB_TEXT, RbText11);
        bundle11.putIntegerArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_ENABLE_NEXT_STEP, NextStep11);

        Step11 = new FragmentSurvey10Choices();
        Step11.setArguments(bundle11);
        addStep(Step11, true);

        //Step 12 UNIVERSITY 2
        bundle12 = new Bundle();
        bundle12.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen12));
        bundle12.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.title_ss_screen12));
        Step12 = new FragmentSurvey2Choices();
        Step12.setArguments(bundle12);
        addStep(Step12, true);

        //Step 13 UNIVERSITY 3
        bundle13 = new Bundle();
        bundle13.putString(FragmentSurveyText.SURVEY_TEXT_TITLE, getResources().getString(R.string.title_ss_screen13));
        bundle13.putString(FragmentSurveyText.SURVEY_TEXT_DESC, getResources().getString(R.string.question_ss_screen13));
        bundle13.putString(FragmentSurveyText.SURVEY_TEXT_COMMENT, null);
        bundle13.putBoolean(FragmentSurveyText.SURVEY_TEXT_IS_OPTIONAL, false);
        Step13 = new FragmentSurveyText();
        Step13.setArguments(bundle13);
        addStep(Step13, false);

        //Step 14 RELATIONSHIP
        bundle14 = new Bundle();
        bundle14.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen14));
        bundle14.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_DESC, getResources().getString(R.string.question_ss_screen14));
        ArrayList<String> RbText14 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_ss_screen14));
            add(getResources().getString(R.string.option2_ss_screen14));
            add(getResources().getString(R.string.option3_ss_screen14));
        }};

        ArrayList<Integer> NextStep14 = new ArrayList<Integer>() {{
            add(1); //Optional step visible
            add(1); //Optional step visible
            add(1); //Optional step visible
        }};

        bundle14.putStringArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_RB_TEXT, RbText14);
        bundle14.putIntegerArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_ENABLE_NEXT_STEP, NextStep14);

        Step14 = new FragmentSurvey10Choices();
        Step14.setArguments(bundle14);
        addStep(Step14, true);

        //Step 15 CONTRACEPTION
        bundle15 = new Bundle();
        bundle15.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen15));
        bundle15.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.title_ss_screen15));
        Step15 = new FragmentSurvey2Choices();
        Step15.setArguments(bundle15);
        addStep(Step15, true);

        //Step 16 BABY
        bundle16 = new Bundle();
        bundle16.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen16));
        bundle16.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.title_ss_screen16));
        Step16 = new FragmentSurvey2Choices();
        Step16.setArguments(bundle16);
        addStep(Step16, true);

        //Step 17 PERIOD 1
        bundle17 = new Bundle();
        bundle17.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_ss_screen17));
        bundle17.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_ss_screen17));
        bundle17.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_UNIT, getResources().getString(R.string.survey_days));
        bundle17.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE, SURVEY_PERIOD_MIN);
        bundle17.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE, SURVEY_PEIORD_MAX);
        bundle17.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE, SURVEY_DEFAULT_PERIOD);
        Step17 = new FragmentSurveyScrolling();
        Step17.setArguments(bundle17);
        addStep(Step17, true);

        //Step 18 PERIOD 2
        bundle18 = new Bundle();
        bundle18.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_ss_screen18));
        bundle18.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_ss_screen18));
        bundle18.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_UNIT, getResources().getString(R.string.survey_days));
        bundle18.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE, SURVEY_PERIOD_MIN);
        bundle18.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE, SURVEY_PEIORD_MAX);
        bundle18.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE, SURVEY_DEFAULT_PERIOD);
        Step18 = new FragmentSurveyScrolling();
        Step18.setArguments(bundle18);
        addStep(Step18, true);

        //Step 19 PERIOD 3
        bundle19 = new Bundle();
        bundle19.putString(FragmentSurveyDatePicker.SURVEY_DATEPICKER_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen19));
        bundle19.putString(FragmentSurveyDatePicker.SURVEY_DATEPICKER_CHOICES_DESC, getResources().getString(R.string.title_ss_screen19));
        Step19 = new FragmentSurveyDatePicker();
        Step19.setArguments(bundle19);
        addStep(Step19, true);

        //Step 20 PERIOD 4
        bundle20 = new Bundle();
        bundle20.putString(FragmentSurveyDatePicker.SURVEY_DATEPICKER_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen20));
        bundle20.putString(FragmentSurveyDatePicker.SURVEY_DATEPICKER_CHOICES_DESC, getResources().getString(R.string.title_ss_screen20));
        Step20 = new FragmentSurveyDatePicker();
        Step20.setArguments(bundle20);
        addStep(Step20, true);

        //Step 21 NOTIFICATION TIME
        bundle21 = new Bundle();
        bundle21.putString(FragmentSurveyTimePicker.SURVEY_TIMEPICKER_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen21));
        bundle21.putString(FragmentSurveyTimePicker.SURVEY_TIMEPICKER_CHOICES_DESC, getResources().getString(R.string.question_ss_screen21));
        Step21 = new FragmentSurveyTimePicker();
        Step21.setArguments(bundle21);
        addStep(Step21, true);


        super.onCreate(savedInstanceState);
    }
}

