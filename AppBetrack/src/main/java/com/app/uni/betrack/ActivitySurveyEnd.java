package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.ProgressStepper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cevincent on 14/09/2016.
 */
public class ActivitySurveyEnd  extends ProgressStepper {
    private static final String TAG = "ActivitySurveyEnd";

    private int SurveyPhoneUsage = 0;
    private int SurveyFacebook = 0;
    private int SurveyMessenger = 0;
    private int SurveyInstagram = 0;
    private int SurveyHangouts = 0;
    private int Surveygoogleplus = 0;
    private int Surveytwitter = 0;
    private int Surveypinterest = 0;
    private int Surveysnapchat = 0;
    private int Surveywhatsapp = 0;
    private int Surveyskype = 0;
    private int SurveyStudy1 = 0;
    private int SurveyStudy2 = 0;
    private int SurveyStudy3 = 0;
    private int SurveyResearchApp1 = 0;
    private int SurveyResearchApp2 = 0;
    private String SurveyResearchApp3 = null;
    private String DateStudyEnd = null;
    private String TimeStudyEnd = null;

    private static final int SURVEY_PHONE_USAGE_MINUTES_MIN = 0;
    private static final int SURVEY_PHONE_USAGE_MINUTES_MAX = 59;
    private static final int SURVEY_PHONE_USAGE_MINUTES_VAL_INC = 5;
    private static final int SURVEY_PHONE_USAGE_HOURS_MIN = 0;
    private static final int SURVEY_PHONE_USAGE_HOURS_MAX = 24;

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
        int resultInt =0;
        String resultString = null;

        //Step 1 PHONE USAGE
        resultInt = Step1.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS2, 0);

        SurveyPhoneUsage = resultInt * SURVEY_PHONE_USAGE_MINUTES_VAL_INC;
        resultInt = Step1.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            SurveyPhoneUsage += (resultInt * 60);
        }

        //Step 2 Facebook
        resultInt = Step2.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS2, 0);

        SurveyFacebook = resultInt * SURVEY_PHONE_USAGE_MINUTES_VAL_INC;
        resultInt = Step2.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            SurveyFacebook += (resultInt * 60);
        }

        //Step 3 messenger
        resultInt = Step3.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS2, 0);

        SurveyMessenger = resultInt * SURVEY_PHONE_USAGE_MINUTES_VAL_INC;
        resultInt = Step3.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            SurveyMessenger += (resultInt * 60);
        }

        //Step 4 instagram
        resultInt = Step4.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS2, 0);

        SurveyInstagram = resultInt * SURVEY_PHONE_USAGE_MINUTES_VAL_INC;
        resultInt = Step4.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            SurveyInstagram += (resultInt * 60);
        }

        //Step 5 hangouts
        resultInt = Step5.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS2, 0);

        SurveyHangouts = resultInt * SURVEY_PHONE_USAGE_MINUTES_VAL_INC;
        resultInt = Step5.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            SurveyHangouts += (resultInt * 60);
        }

        //Step 6 googleplus
        resultInt = Step6.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS2, 0);

        Surveygoogleplus = resultInt * SURVEY_PHONE_USAGE_MINUTES_VAL_INC;
        resultInt = Step6.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            Surveygoogleplus += (resultInt * 60);
        }

        //Step 7 twitter
        resultInt = Step7.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS2, 0);

        Surveytwitter = resultInt * SURVEY_PHONE_USAGE_MINUTES_VAL_INC;
        resultInt = Step7.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            Surveytwitter += (resultInt * 60);
        }

        //Step 8 pinterest
        resultInt = Step8.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS2, 0);

        Surveypinterest = resultInt * SURVEY_PHONE_USAGE_MINUTES_VAL_INC;
        resultInt = Step8.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            Surveypinterest += (resultInt * 60);
        }

        //Step 9 snapchat
        resultInt = Step9.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS2, 0);

        Surveysnapchat = resultInt * SURVEY_PHONE_USAGE_MINUTES_VAL_INC;
        resultInt = Step9.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            Surveysnapchat += (resultInt * 60);
        }

        //Step 10 whatsapp
        resultInt = Step10.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS2, 0);

        Surveywhatsapp = resultInt * SURVEY_PHONE_USAGE_MINUTES_VAL_INC;
        resultInt = Step10.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            Surveywhatsapp += (resultInt * 60);
        }

        //Step 11 skype
        resultInt = Step11.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS2, 0);

        Surveyskype = resultInt * SURVEY_PHONE_USAGE_MINUTES_VAL_INC;
        resultInt = Step11.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            Surveyskype += (resultInt * 60);
        }

        //Step 12 STUDY 1
        resultInt = Step12.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            SurveyStudy1 = resultInt;
        }

        //Step 13 STUDY 2
        resultInt = Step13.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            SurveyStudy2 = resultInt;
        }

        //Step 14 STUDY 3
        resultInt = Step14.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            SurveyStudy3 = resultInt;
        }

        //Step 15 RESEARCH APP 1
        resultInt = Step15.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            SurveyResearchApp1 = resultInt;
        }

        //Step 16 RESEARCH APP 2
        resultInt = Step16.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            SurveyResearchApp2 = resultInt;
        }

        //Step 17 RESEARCH APP 3
        resultString = Step17.getArguments().getString(FragmentSurveyText.SURVEY_STATUS, null);
        if (resultString != null) {
            SurveyResearchApp3 = resultString;
        }


        values.clear();
        values.put(UtilsLocalDataBase.C_ENDSTUDY_PHONE_USAGE, SurveyPhoneUsage);

        values.put(UtilsLocalDataBase.C_ENDSTUDY_FACEBOOK, SurveyFacebook);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_MESSENGER, SurveyMessenger);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_INSTAGRAM, SurveyInstagram);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_HANGOUTS, SurveyHangouts);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_GOOGLEPLUS, Surveygoogleplus);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_TWITTER, Surveytwitter);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_PINTEREST, Surveypinterest);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_SNAPCHAT, Surveysnapchat);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_WHATSAPP, Surveywhatsapp);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_SKYPE, Surveyskype);

        values.put(UtilsLocalDataBase.C_ENDSTUDY_STUDY1, SurveyStudy1);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_STUDY2, SurveyStudy2);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_STUDY3, SurveyStudy3);

        values.put(UtilsLocalDataBase.C_ENDSTUDY_RESEARCHAPP1, SurveyResearchApp1);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_RESEARCHAPP2, SurveyResearchApp2);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_RESEARCHAPP3, SurveyResearchApp3);

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

        //Step 1 PHONE USAGE
        bundle1 = new Bundle();
        bundle1.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_se_screen1));
        bundle1.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_se_screen1));

        bundle1.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_1, getResources().getString(R.string.survey_hours));
        bundle1.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_2, getResources().getString(R.string.survey_minutes));

        bundle1.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MIN);
        bundle1.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MAX);

        bundle1.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MIN);
        bundle1.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MAX);
        bundle1.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_VAL_INC_2, SURVEY_PHONE_USAGE_MINUTES_VAL_INC);

        bundle1.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE_1, 0);
        Step1 = new FragmentSurveyScrolling();
        Step1.setArguments(bundle1);
        addStep(Step1, true, 0, false);

        //Step 2 Facebook
        bundle2 = new Bundle();
        bundle2.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_se_screen2));
        bundle2.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_se_screen2));

        bundle2.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_1, getResources().getString(R.string.survey_hours));
        bundle2.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_2, getResources().getString(R.string.survey_minutes));

        bundle2.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MIN);
        bundle2.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MAX);

        bundle2.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MIN);
        bundle2.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MAX);
        bundle2.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_VAL_INC_2, SURVEY_PHONE_USAGE_MINUTES_VAL_INC);

        bundle2.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE_1, 0);
        Step2 = new FragmentSurveyScrolling();
        Step2.setArguments(bundle2);
        addStep(Step2, true, 0, false);

        //Step 3 Facebook Messenger
        bundle3 = new Bundle();
        bundle3.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_se_screen3));
        bundle3.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_se_screen3));

        bundle3.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_1, getResources().getString(R.string.survey_hours));
        bundle3.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_2, getResources().getString(R.string.survey_minutes));

        bundle3.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MIN);
        bundle3.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MAX);

        bundle3.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MIN);
        bundle3.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MAX);
        bundle3.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_VAL_INC_2, SURVEY_PHONE_USAGE_MINUTES_VAL_INC);

        bundle3.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE_1, 0);
        Step3 = new FragmentSurveyScrolling();
        Step3.setArguments(bundle3);
        addStep(Step3, true, 0, false);

        //Step 4 Instagram
        bundle4 = new Bundle();
        bundle4.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_se_screen4));
        bundle4.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_se_screen4));

        bundle4.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_1, getResources().getString(R.string.survey_hours));
        bundle4.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_2, getResources().getString(R.string.survey_minutes));

        bundle4.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MIN);
        bundle4.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MAX);

        bundle4.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MIN);
        bundle4.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MAX);
        bundle4.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_VAL_INC_2, SURVEY_PHONE_USAGE_MINUTES_VAL_INC);

        bundle4.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE_1, 0);
        Step4 = new FragmentSurveyScrolling();
        Step4.setArguments(bundle4);
        addStep(Step4, true, 0, false);

        //Step 5 Google Hangouts
        bundle5 = new Bundle();
        bundle5.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_se_screen5));
        bundle5.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_se_screen5));

        bundle5.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_1, getResources().getString(R.string.survey_hours));
        bundle5.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_2, getResources().getString(R.string.survey_minutes));

        bundle5.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MIN);
        bundle5.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MAX);

        bundle5.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MIN);
        bundle5.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MAX);
        bundle5.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_VAL_INC_2, SURVEY_PHONE_USAGE_MINUTES_VAL_INC);

        bundle5.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE_1, 0);
        Step5 = new FragmentSurveyScrolling();
        Step5.setArguments(bundle5);
        addStep(Step5, true, 0, false);

        //Step 6 Google+
        bundle6 = new Bundle();
        bundle6.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_se_screen6));
        bundle6.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_se_screen6));

        bundle6.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_1, getResources().getString(R.string.survey_hours));
        bundle6.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_2, getResources().getString(R.string.survey_minutes));

        bundle6.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MIN);
        bundle6.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MAX);

        bundle6.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MIN);
        bundle6.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MAX);
        bundle6.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_VAL_INC_2, SURVEY_PHONE_USAGE_MINUTES_VAL_INC);

        bundle6.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE_1, 0);
        Step6 = new FragmentSurveyScrolling();
        Step6.setArguments(bundle6);
        addStep(Step6, true, 0, false);

        //Step 7 Twitter
        bundle7 = new Bundle();
        bundle7.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_se_screen7));
        bundle7.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_se_screen7));

        bundle7.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_1, getResources().getString(R.string.survey_hours));
        bundle7.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_2, getResources().getString(R.string.survey_minutes));

        bundle7.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MIN);
        bundle7.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MAX);

        bundle7.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MIN);
        bundle7.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MAX);
        bundle7.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_VAL_INC_2, SURVEY_PHONE_USAGE_MINUTES_VAL_INC);

        bundle7.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE_1, 0);
        Step7 = new FragmentSurveyScrolling();
        Step7.setArguments(bundle7);
        addStep(Step7, true, 0, false);

        //Step 8 Pinterest
        bundle8 = new Bundle();
        bundle8.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_se_screen8));
        bundle8.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_se_screen8));

        bundle8.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_1, getResources().getString(R.string.survey_hours));
        bundle8.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_2, getResources().getString(R.string.survey_minutes));

        bundle8.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MIN);
        bundle8.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MAX);

        bundle8.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MIN);
        bundle8.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MAX);
        bundle8.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_VAL_INC_2, SURVEY_PHONE_USAGE_MINUTES_VAL_INC);

        bundle8.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE_1, 0);
        Step8 = new FragmentSurveyScrolling();
        Step8.setArguments(bundle8);
        addStep(Step8, true, 0, false);

        //Step 9 Snapchat
        bundle9 = new Bundle();
        bundle9.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_se_screen9));
        bundle9.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_se_screen9));

        bundle9.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_1, getResources().getString(R.string.survey_hours));
        bundle9.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_2, getResources().getString(R.string.survey_minutes));

        bundle9.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MIN);
        bundle9.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MAX);

        bundle9.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MIN);
        bundle9.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MAX);
        bundle9.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_VAL_INC_2, SURVEY_PHONE_USAGE_MINUTES_VAL_INC);

        bundle9.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE_1, 0);
        Step9 = new FragmentSurveyScrolling();
        Step9.setArguments(bundle9);
        addStep(Step9, true, 0, false);

        //Step 10 WhatsApp
        bundle10 = new Bundle();
        bundle10.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_se_screen10));
        bundle10.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_se_screen10));

        bundle10.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_1, getResources().getString(R.string.survey_hours));
        bundle10.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_2, getResources().getString(R.string.survey_minutes));

        bundle10.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MIN);
        bundle10.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MAX);

        bundle10.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MIN);
        bundle10.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MAX);
        bundle10.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_VAL_INC_2, SURVEY_PHONE_USAGE_MINUTES_VAL_INC);

        bundle10.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE_1, 0);
        Step10 = new FragmentSurveyScrolling();
        Step10.setArguments(bundle10);
        addStep(Step10, true, 0, false);

        //Step 11 Skype
        bundle11 = new Bundle();
        bundle11.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_TITLE, getResources().getString(R.string.title_se_screen11));
        bundle11.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_DESC, getResources().getString(R.string.question_se_screen11));

        bundle11.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_1, getResources().getString(R.string.survey_hours));
        bundle11.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_2, getResources().getString(R.string.survey_minutes));

        bundle11.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MIN);
        bundle11.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_1, SURVEY_PHONE_USAGE_HOURS_MAX);

        bundle11.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MIN);
        bundle11.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_2, SURVEY_PHONE_USAGE_MINUTES_MAX);
        bundle11.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_VAL_INC_2, SURVEY_PHONE_USAGE_MINUTES_VAL_INC);

        bundle11.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE_1, 0);
        Step11 = new FragmentSurveyScrolling();
        Step11.setArguments(bundle11);
        addStep(Step11, true, 0, false);

        //Step 12 STUDY 1
        bundle12 = new Bundle();
        bundle12.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_TITLE, getResources().getString(R.string.title_se_screen12));
        bundle12.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_DESC, getResources().getString(R.string.question_se_screen12));

        ArrayList<String> ChoiceTextRightStep12 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option2_se_screen12));
        }};
        ArrayList<String> ChoiceTextLeftStep12 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_se_screen12));
        }};

        bundle12.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_RIGHT, ChoiceTextRightStep12);
        bundle12.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_LEFT, ChoiceTextLeftStep12);

        Step12 = new FragmentSurveySeekBar();
        Step12.setArguments(bundle12);
        addStep(Step12, true, 0, false);

        //Step 13 STUDY 2
        bundle13 = new Bundle();
        bundle13.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_TITLE, getResources().getString(R.string.title_se_screen13));
        bundle13.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_DESC, getResources().getString(R.string.question_se_screen13));

        ArrayList<String> ChoiceTextRightStep13 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option2_se_screen13));
        }};
        ArrayList<String> ChoiceTextLeftStep13 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_se_screen13));
        }};

        bundle13.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_RIGHT, ChoiceTextRightStep13);
        bundle13.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_LEFT, ChoiceTextLeftStep13);

        Step13 = new FragmentSurveySeekBar();
        Step13.setArguments(bundle13);
        addStep(Step13, true, 0, false);

        //Step 14 STUDY 3
        bundle14 = new Bundle();
        bundle14.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_TITLE, getResources().getString(R.string.title_se_screen14));
        bundle14.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_DESC, getResources().getString(R.string.question_se_screen14));

        ArrayList<String> ChoiceTextRightStep14 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option2_se_screen14));
        }};
        ArrayList<String> ChoiceTextLeftStep14 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_se_screen14));
        }};

        bundle14.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_RIGHT, ChoiceTextRightStep14);
        bundle14.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_LEFT, ChoiceTextLeftStep14);

        Step14 = new FragmentSurveySeekBar();
        Step14.setArguments(bundle14);
        addStep(Step14, true, 0, false);

        //Step 15 STUDY 3
        bundle15 = new Bundle();
        bundle15.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_TITLE, getResources().getString(R.string.title_se_screen15));
        bundle15.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_DESC, getResources().getString(R.string.question_se_screen15));

        ArrayList<String> ChoiceTextRightStep15 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option2_se_screen15));
        }};
        ArrayList<String> ChoiceTextLeftStep15 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_se_screen15));
        }};

        bundle15.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_RIGHT, ChoiceTextRightStep15);
        bundle15.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_LEFT, ChoiceTextLeftStep15);

        Step15 = new FragmentSurveySeekBar();
        Step15.setArguments(bundle15);
        addStep(Step15, true, 0, false);

        //Step 16 RESEARCH APP 1
        bundle16 = new Bundle();
        bundle16.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_TITLE, getResources().getString(R.string.title_se_screen16));
        bundle16.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_DESC, getResources().getString(R.string.question_se_screen16));

        ArrayList<String> ChoiceTextRightStep16 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option2_se_screen16));
        }};
        ArrayList<String> ChoiceTextLeftStep16 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_se_screen16));
        }};

        bundle16.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_RIGHT, ChoiceTextRightStep16);
        bundle16.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_LEFT, ChoiceTextLeftStep16);

        Step16 = new FragmentSurveySeekBar();
        Step16.setArguments(bundle16);
        addStep(Step16, true, 0, false);

        //Step 17 RESEARCH APP 2
        bundle17 = new Bundle();
        bundle17.putString(FragmentSurveyText.SURVEY_TEXT_TITLE, getResources().getString(R.string.title_se_screen17));
        bundle17.putString(FragmentSurveyText.SURVEY_TEXT_DESC, getResources().getString(R.string.question_se_screen17));
        bundle17.putString(FragmentSurveyText.SURVEY_TEXT_COMMENT, null);
        bundle17.putBoolean(FragmentSurveyText.SURVEY_TEXT_IS_OPTIONAL, true);
        bundle17.putInt(FragmentSurveyText.SURVEY_TEXT_MAX_NBR_LINE, 10);
        Step17 = new FragmentSurveyText();
        Step17.setArguments(bundle17);
        addStep(Step17, true, 0, false);

        super.onCreate(savedInstanceState);
    }
}