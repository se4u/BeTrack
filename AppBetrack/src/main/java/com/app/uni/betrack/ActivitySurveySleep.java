package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.ProgressStepper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * Created by cvincent on 27/08/17.
 */

public class ActivitySurveySleep extends ProgressStepper {
    private static final String TAG = "ActivitySurveySleep";

    private String TimeToBed = null;
    private String TimeToSleep = null;
    private int FallAsleep = 0;
    private int HowManyWakeUp = 0;
    private int HowLongWakeUp = 0;
    private String WhatTimeLastAwaking = null;
    private String WhatTimeOutBed = null;
    private int QualitySleep = 0;
    private String Comments = null;

    private String DateDaily = null;
    private String TimeDaily = null;

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

    private static final String SURVEY_HOW_LONG_FALL_ASLEEP_OUT_MIN = "0";
    private static final String SURVEY_HOW_LONG_FALL_ASLEEP_OUT_MAX = "> 180";
    private static final int SURVEY_HOW_LONG_FALL_ASLEEP_MIN = 5;
    private static final int SURVEY_HOW_LONG_FALL_ASLEEP_INCREMENT = 5;
    private static final int SURVEY_HOW_LONG_FALL_ASLEEP_MAX = 180;
    private static int SurveyHowLongDefault = 0;
    private static final int SURVEY_HOW_LONG_FALL_ASLEEP_PERIOD1 = SurveyHowLongDefault;

    private static final String SURVEY_WAKE_UP_OUT_MIN = "0";
    private static final String SURVEY_WAKE_UP_OUT_MAX = "> 10";
    private static final int SURVEY_WAKE_UP_MIN = 1;
    private static final int SURVEY_WAKE_UP_MAX = 10;
    private static int SurveyWakeUpDefault = 0;
    private static final int SURVEY_WAKE_UP_PERIOD1 = SurveyWakeUpDefault - SURVEY_WAKE_UP_MIN;

    private static final String SURVEY_TIME_AWAKING_OUT_MIN = "0";
    private static final String SURVEY_TIME_AWAKING_OUT_MAX = "> 180";
    private static final int SURVEY_TIME_AWAKING_MIN = 5;
    private static final int SURVEY_TIME_AWAKING_ASLEEP_INCREMENT = 5;
    private static final int SURVEY_TIME_AWAKING_MAX = 180;
    private static int SurveyTimeAwakingDefault = 0;
    private static final int SURVEY_TIME_AWAKING_PERIOD1 = SurveyTimeAwakingDefault - SURVEY_TIME_AWAKING_MIN;

    //Table for daily sleep status
    static final String TABLE_SLEEPSTATUS = "SleepStatus";
    static final String C_SLEEPSTATUS_ID = BaseColumns._ID;
    static final String C_SLEEPSTATUS_PID = "ParticipantID";
    static final String C_SLEEPSTATUS_TIME_TO_BED = "TimeToBed";
    static final String C_SLEEPSTATUS_TIME_TO_SLEEP = "TimeToSleep";
    static final String C_SLEEPSTATUS_FALL_ASLEEP = "FallAsleep";
    static final String C_SLEEPSTATUS_HOWM_WAKEUP = "HowManyWakeUp";
    static final String C_SLEEPSTATUS_HOWL_WAKEUP = "HowLongWakeUp";
    static final String C_SLEEPSTATUS_WHAT_TIME_AWAKE = "WhatTimeLastAwaking";
    static final String C_SLEEPSTATUS_WHAT_TIME_OUT = "WhatTimeOutBed";
    static final String C_SLEEPSTATUS_QUALITY_SLEEP = "QualitySleep";
    static final String C_SLEEPSTATUS_COMMENTS = "Comments";
    static final String C_SLEEPSTATUS_DATE = "Date";
    static final String C_SLEEPSTATUS_TIME = "Time";


    @Override
    public void onComplete() {
        super.onComplete();


        TimeToBed = Step1.getArguments().getString(FragmentSurveyTimePicker.SURVEY_STATUS, null);
        TimeToSleep = Step2.getArguments().getString(FragmentSurveyTimePicker.SURVEY_STATUS, null);
        FallAsleep = Step3.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS1, 0);
        HowManyWakeUp = Step4.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS1, 0);
        HowLongWakeUp = Step5.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS1, 0);
        WhatTimeLastAwaking = Step6.getArguments().getString(FragmentSurveyTimePicker.SURVEY_STATUS, null);
        WhatTimeOutBed = Step7.getArguments().getString(FragmentSurveyTimePicker.SURVEY_STATUS, null);
        QualitySleep = Step8.getArguments().getInt(FragmentSurvey10ChoicesRadio.SURVEY_STATUS, 0);
        Comments = Step9.getArguments().getString(FragmentSurveyText.SURVEY_STATUS, null);

        values.clear();
        values.put(UtilsLocalDataBase.C_SLEEPSTATUS_TIME_TO_BED, TimeToBed);
        values.put(UtilsLocalDataBase.C_SLEEPSTATUS_TIME_TO_SLEEP, TimeToSleep);
        values.put(UtilsLocalDataBase.C_SLEEPSTATUS_FALL_ASLEEP, FallAsleep);
        values.put(UtilsLocalDataBase.C_SLEEPSTATUS_HOWM_WAKEUP, HowManyWakeUp);
        values.put(UtilsLocalDataBase.C_SLEEPSTATUS_HOWL_WAKEUP, HowLongWakeUp);
        values.put(UtilsLocalDataBase.C_SLEEPSTATUS_WHAT_TIME_AWAKE, WhatTimeLastAwaking);
        values.put(UtilsLocalDataBase.C_SLEEPSTATUS_WHAT_TIME_OUT, WhatTimeOutBed);
        values.put(UtilsLocalDataBase.C_SLEEPSTATUS_QUALITY_SLEEP, QualitySleep);
        values.put(UtilsLocalDataBase.C_SLEEPSTATUS_COMMENTS, Comments);
        DateDaily = sdf.format(new Date());
        values.put(UtilsLocalDataBase.C_SLEEPSTATUS_DATE, DateDaily);
        TimeDaily = shf.format(new Date());
        values.put(UtilsLocalDataBase.C_SLEEPSTATUS_TIME, TimeDaily);

        AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_SLEEPSTATUS);
        Log.d(TAG, "idUser: " + ObjSettingsStudy.getIdUser()
                + " TimeToBed: " + TimeToBed
                + " TimeToSleep: " + TimeToSleep
                + " FallAsleep: " + FallAsleep
                + " HowManyWakeUp: " + HowManyWakeUp
                + " HowLongWakeUp: " + HowLongWakeUp
                + " WhatTimeLastAwaking: " + WhatTimeLastAwaking
                + " WhatTimeOutBed: " + WhatTimeOutBed
                + " QualitySleep: " + QualitySleep
                + " Comments: " + Comments
                + " Date: " + DateDaily
                + " Time: " + TimeDaily);

        ObjSettingsStudy.setDailySurveyState(ObjSettingsStudy.getDailySurveyState() - 1);

        Intent msgIntent = new Intent(getApplicationContext(), IntentServicePostData.class);
        //Start the service for sending the data to the remote server
        startService(msgIntent);

        Intent i = new Intent(this, ActivityBeTrack.class);
        startActivity(i);
        finish();
        System.out.println("SurveySleep completed");
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

        //Step 1 Sleep Survey: What time did you get into bed?
        bundle1 = new Bundle();
        bundle1.putString(FragmentSurveyTimePicker.SURVEY_TIMEPICKER_CHOICES_TITLE, getResources().getString(R.string.title_sds_screen1));
        bundle1.putString(FragmentSurveyTimePicker.SURVEY_TIMEPICKER_CHOICES_DESC, getResources().getString(R.string.question_sds_screen1));
        bundle1.putString(FragmentSurveyTimePicker.SURVEY_TIMEPICKER_DEFAULT_VALUE, "21:00");

        Step1 = new FragmentSurveyTimePicker();
        Step1.setArguments(bundle1);
        addStep(Step1, true, 0, false);

        //Step 2 Sleep Survey: What time did you try to go to sleep?
        bundle2 = new Bundle();
        bundle2.putString(FragmentSurveyTimePicker.SURVEY_TIMEPICKER_CHOICES_TITLE, getResources().getString(R.string.title_sds_screen2));
        bundle2.putString(FragmentSurveyTimePicker.SURVEY_TIMEPICKER_CHOICES_DESC, getResources().getString(R.string.question_sds_screen2));
        bundle2.putString(FragmentSurveyTimePicker.SURVEY_TIMEPICKER_DEFAULT_VALUE, "21:00");

        Step2 = new FragmentSurveyTimePicker();
        Step2.setArguments(bundle2);
        addStep(Step2, true, 0, false);

        //Step 3 Sleep Survey: How long did it take you to fall asleep?
        bundle3 = new Bundle();
        bundle3.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_sds_screen3));
        bundle3.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_sds_screen3));
        bundle3.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_PRE_UNIT_1, "~");
        bundle3.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_1, getResources().getString(R.string.survey_minutes));
        bundle3.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_MIN_TXT_VALUE_1, SURVEY_HOW_LONG_FALL_ASLEEP_OUT_MIN + getResources().getString(R.string.survey_minutes));
        bundle3.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_MAX_TXT_VALUE_1, SURVEY_HOW_LONG_FALL_ASLEEP_OUT_MAX + getResources().getString(R.string.survey_minutes));
        bundle3.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_1, SURVEY_HOW_LONG_FALL_ASLEEP_MIN);
        bundle3.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_1, SURVEY_HOW_LONG_FALL_ASLEEP_MAX);
        bundle3.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_VAL_INC_1, SURVEY_HOW_LONG_FALL_ASLEEP_INCREMENT);
        bundle3.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE_1, SURVEY_HOW_LONG_FALL_ASLEEP_PERIOD1);
        Step3 = new FragmentSurveyScrolling();
        Step3.setArguments(bundle3);
        addStep(Step3, true, 0, false);

        //Step 4 Sleep Survey: How many times did you wake up, not counting your final awakening?
        bundle4 = new Bundle();
        bundle4.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_sds_screen4));
        bundle4.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_sds_screen4));
        bundle4.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_PRE_UNIT_1, "");
        bundle4.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_1, getResources().getString(R.string.survey_times));
        bundle4.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_MIN_TXT_VALUE_1, SURVEY_WAKE_UP_OUT_MIN + getResources().getString(R.string.survey_times));
        bundle4.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_MAX_TXT_VALUE_1, SURVEY_WAKE_UP_OUT_MAX + getResources().getString(R.string.survey_times));
        bundle4.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_1, SURVEY_WAKE_UP_MIN);
        bundle4.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_1, SURVEY_WAKE_UP_MAX);
        bundle4.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE_1, SURVEY_WAKE_UP_PERIOD1);

        ArrayList<Integer> NextStep4 = new ArrayList<Integer>() {{
            add(1); //Optional step visible
            add(1); //Not used
        }};

        ArrayList<Integer> NextStepTrigger4 = new ArrayList<Integer>() {{
            add(0); //Optional step visible
            add(-2); //Not used
        }};

        bundle4.putIntegerArrayList(FragmentSurveyScrolling.SURVEY_SCROLLING_ENABLE_NEXT_STEP, NextStep4);
        bundle4.putIntegerArrayList(FragmentSurveyScrolling.SURVEY_SCROLLING_ENABLE_NEXT_STEP_TRIGGER, NextStepTrigger4);

        Step4 = new FragmentSurveyScrolling();
        Step4.setArguments(bundle4);
        addStep(Step4, true, 0, true);

        //Step 5 Sleep Survey: In total, how long did these awakenings last?
        bundle5 = new Bundle();
        bundle5.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_sds_screen5));
        bundle5.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_sds_screen5));
        bundle5.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_PRE_UNIT_1, "~");
        bundle5.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_1, getResources().getString(R.string.survey_minutes));
        bundle5.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_MIN_TXT_VALUE_1, SURVEY_TIME_AWAKING_OUT_MIN + getResources().getString(R.string.survey_minutes));
        bundle5.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_MAX_TXT_VALUE_1, SURVEY_TIME_AWAKING_OUT_MAX + getResources().getString(R.string.survey_minutes));
        bundle5.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_1, SURVEY_TIME_AWAKING_MIN);
        bundle5.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_1, SURVEY_TIME_AWAKING_MAX);
        bundle5.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_VAL_INC_1, SURVEY_TIME_AWAKING_ASLEEP_INCREMENT);
        bundle5.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE_1, SURVEY_TIME_AWAKING_PERIOD1);
        Step5 = new FragmentSurveyScrolling();
        Step5.setArguments(bundle5);
        addStep(Step5, false, 1, false);

        //Step 6 Sleep Survey: What time was your final awakening?
        bundle6 = new Bundle();
        bundle6.putString(FragmentSurveyTimePicker.SURVEY_TIMEPICKER_CHOICES_TITLE, getResources().getString(R.string.title_sds_screen6));
        bundle6.putString(FragmentSurveyTimePicker.SURVEY_TIMEPICKER_CHOICES_DESC, getResources().getString(R.string.question_sds_screen6));
        bundle6.putString(FragmentSurveyTimePicker.SURVEY_TIMEPICKER_DEFAULT_VALUE, "07:00");

        Step6 = new FragmentSurveyTimePicker();
        Step6.setArguments(bundle6);
        addStep(Step6, true, 0, false);

        //Step 7 Sleep Survey: What time did you get out of bed for the day?
        bundle7 = new Bundle();
        bundle7.putString(FragmentSurveyTimePicker.SURVEY_TIMEPICKER_CHOICES_TITLE, getResources().getString(R.string.title_sds_screen7));
        bundle7.putString(FragmentSurveyTimePicker.SURVEY_TIMEPICKER_CHOICES_DESC, getResources().getString(R.string.question_sds_screen7));
        bundle7.putString(FragmentSurveyTimePicker.SURVEY_TIMEPICKER_DEFAULT_VALUE, "07:00");

        Step7 = new FragmentSurveyTimePicker();
        Step7.setArguments(bundle7);
        addStep(Step7, true, 0, false);


        //Step 8 Daily Surveys: Which social networking sites did you use?
        bundle8 = new Bundle();
        bundle8.putString(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_TITLE, getResources().getString(R.string.title_sds_screen8));
        bundle8.putString(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_DESC, getResources().getString(R.string.question_sds_screen8));

        ArrayList<String> RbText8 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_sds_screen8));
            add(getResources().getString(R.string.option2_sds_screen8));
            add(getResources().getString(R.string.option3_sds_screen8));
            add(getResources().getString(R.string.option4_sds_screen8));
            add(getResources().getString(R.string.option5_sds_screen8));
        }};

        ArrayList<Integer> NextStep8 = new ArrayList<Integer>() {{
            add(1); //Optional step hidden
            add(1); //Optional step hidden
            add(1); //Optional step hidden
            add(1); //Optional step hidden
            add(1); //Optional step hidden
        }};

        bundle8.putStringArrayList(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_RB_TEXT, RbText8);
        bundle8.putIntegerArrayList(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_ENABLE_NEXT_STEP, NextStep8);

        Step8 = new FragmentSurvey10ChoicesRadio();
        Step8.setArguments(bundle8);
        addStep(Step8, true, 0, false);

        //Step 9 Comments (if applicable):
        bundle9 = new Bundle();
        bundle9.putString(FragmentSurveyText.SURVEY_TEXT_TITLE, getResources().getString(R.string.title_sds_screen9));
        bundle9.putString(FragmentSurveyText.SURVEY_TEXT_DESC, getResources().getString(R.string.question_sds_screen9));
        bundle9.putString(FragmentSurveyText.SURVEY_TEXT_COMMENT, null);
        bundle9.putBoolean(FragmentSurveyText.SURVEY_TEXT_IS_OPTIONAL, true);
        bundle9.putInt(FragmentSurveyText.SURVEY_TEXT_MAX_NBR_LINE, 10);
        bundle9.putInt(FragmentSurveyText.SURVEY_TEXT_MAX_NBR_CHAR, 1024); //Should be inferior or equal to the max size value in the database!
        Step9 = new FragmentSurveyText();
        Step9.setArguments(bundle9);
        addStep(Step9, true, 0, false);


        super.onCreate(savedInstanceState);
    }
}
