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

    private int SurveyRelationShip = 0;
    private int SurveyContraception = 0;
    private int SurveyTinder = 0;
    private int SurveyPhoneUsage = 0;
    private int SurveyStudy1 = 0;
    private int SurveyStudy2 = 0;
    private int SurveyStudy3 = 0;
    private int SurveyResearchApp1 = 0;
    private String SurveyResearchApp2 = null;
    private String DateStudyEnd = null;
    private String TimeStudyEnd = null;

    private static final int SURVEY_PHONE_USAGE_MINUTES_MIN = 0;
    private static final int SURVEY_PHONE_USAGE_MINUTES_MAX = 59;
    private static final int SURVEY_PHONE_USAGE_MINUTES_VAL_INC = 5;
    private static final int SURVEY_PHONE_USAGE_HOURS_MIN = 0;
    private static final int SURVEY_PHONE_USAGE_HOURS_MAX = 23;

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
        int resultInt =0;
        String resultString = null;

        //Step 1 RELATIONSHIP
        resultInt = Step1.getArguments().getInt(FragmentSurvey10Choices.SURVEY_STATUS, 0);
        if (resultInt != 0) {
            SurveyRelationShip = resultInt;
        }

        //Step 2 CONTRACEPTION
        resultInt = Step2.getArguments().getInt(FragmentSurvey2Choices.SURVEY_STATUS, 0);
        if (resultInt != 0) {
            SurveyContraception = resultInt;
        }

        //Step 3 TINDER
        resultInt = Step3.getArguments().getInt(FragmentSurvey10Choices.SURVEY_STATUS, 0);
        if (resultInt != 0) {
            SurveyTinder = resultInt;
        }

        //Step 4 PHONE USAGE
        resultInt = Step4.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS2, 0);

        SurveyPhoneUsage = resultInt * SURVEY_PHONE_USAGE_MINUTES_VAL_INC;
        resultInt = Step4.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            SurveyPhoneUsage += (resultInt * 60);
        }

        //Step 5 STUDY 1
        resultInt = Step5.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            SurveyStudy1 = resultInt;
        }

        //Step 6 STUDY 2
        resultInt = Step6.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            SurveyStudy2 = resultInt;
        }

        //Step 7 STUDY 3
        resultInt = Step7.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            SurveyStudy3 = resultInt;
        }

        //Step 8 RESEARCH APP 1
        resultInt = Step8.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            SurveyResearchApp1 = resultInt;
        }

        //Step 9 RESEARCH APP 2
        resultString = Step9.getArguments().getString(FragmentSurveyText.SURVEY_STATUS, null);
        if (resultString != null) {
            SurveyResearchApp2 = resultString;
        }

        values.clear();
        values.put(UtilsLocalDataBase.C_ENDSTUDY_RELATIONSHIP, SurveyRelationShip);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_CONTRACEPTION, SurveyContraception);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_TINDER, SurveyTinder);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_PHONE_USAGE, SurveyPhoneUsage);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_STUDY1, SurveyStudy1);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_STUDY2, SurveyStudy2);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_STUDY3, SurveyStudy3);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_RESEARCHAPP1, SurveyResearchApp1);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_RESEARCHAPP2, SurveyResearchApp2);

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

        //Step 1 RELATIONSHIP
        bundle1 = new Bundle();
        bundle1.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_TITLE, getResources().getString(R.string.title_se_screen1));
        bundle1.putString(FragmentSurvey10Choices.SURVEY_10_CHOICES_DESC, getResources().getString(R.string.question_se_screen1));
        ArrayList<String> RbText1 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_se_screen1));
            add(getResources().getString(R.string.option2_se_screen1));
            add(getResources().getString(R.string.option3_se_screen1));
            add(getResources().getString(R.string.option4_se_screen1));
        }};

        ArrayList<Integer> NextStep1 = new ArrayList<Integer>() {{
            add(1); //Optional step visible
            add(1); //Optional step visible
            add(1); //Optional step visible
            add(1); //Optional step visible
        }};

        bundle1.putStringArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_RB_TEXT, RbText1);
        bundle1.putIntegerArrayList(FragmentSurvey10Choices.SURVEY_10_CHOICES_ENABLE_NEXT_STEP, NextStep1);

        Step1 = new FragmentSurvey10Choices();
        Step1.setArguments(bundle1);
        addStep(Step1, true, 0, false);

        //Step 2 CONTRACEPTION
        bundle2 = new Bundle();
        bundle2.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_se_screen2));
        bundle2.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.question_se_screen2));
        Step2 = new FragmentSurvey2Choices();
        Step2.setArguments(bundle2);
        addStep(Step2, true, 0, false);

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
        addStep(Step3, true, 0, false);

        //Step 4 PHONE USAGE
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

        //Step 5 STUDY 1
        bundle5 = new Bundle();
        bundle5.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_TITLE, getResources().getString(R.string.title_se_screen5));
        bundle5.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_DESC, getResources().getString(R.string.question_se_screen5));

        ArrayList<String> ChoiceTextRightStep5 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option2_se_screen5));
        }};
        ArrayList<String> ChoiceTextLeftStep5 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_se_screen5));
        }};

        bundle5.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_RIGHT, ChoiceTextRightStep5);
        bundle5.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_LEFT, ChoiceTextLeftStep5);

        Step5 = new FragmentSurveySeekBar();
        Step5.setArguments(bundle5);
        addStep(Step5, true, 0, false);

        //Step 6 STUDY 2
        bundle6 = new Bundle();
        bundle6.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_TITLE, getResources().getString(R.string.title_se_screen6));
        bundle6.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_DESC, getResources().getString(R.string.question_se_screen6));

        ArrayList<String> ChoiceTextRightStep6 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option2_se_screen6));
        }};
        ArrayList<String> ChoiceTextLeftStep6 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_se_screen6));
        }};

        bundle6.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_RIGHT, ChoiceTextRightStep6);
        bundle6.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_LEFT, ChoiceTextLeftStep6);

        Step6 = new FragmentSurveySeekBar();
        Step6.setArguments(bundle6);
        addStep(Step6, true, 0, false);

        //Step 7 STUDY 3
        bundle7 = new Bundle();
        bundle7.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_TITLE, getResources().getString(R.string.title_se_screen7));
        bundle7.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_DESC, getResources().getString(R.string.question_se_screen7));

        ArrayList<String> ChoiceTextRightStep7 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option2_se_screen7));
        }};
        ArrayList<String> ChoiceTextLeftStep7 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_se_screen7));
        }};

        bundle7.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_RIGHT, ChoiceTextRightStep7);
        bundle7.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_LEFT, ChoiceTextLeftStep7);

        Step7 = new FragmentSurveySeekBar();
        Step7.setArguments(bundle7);
        addStep(Step7, true, 0, false);

        //Step 8 RESEARCH APP 1
        bundle8 = new Bundle();
        bundle8.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_TITLE, getResources().getString(R.string.title_se_screen8));
        bundle8.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_DESC, getResources().getString(R.string.question_se_screen8));

        ArrayList<String> ChoiceTextRightStep8 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option2_se_screen8));
        }};
        ArrayList<String> ChoiceTextLeftStep8 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_se_screen8));
        }};

        bundle8.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_RIGHT, ChoiceTextRightStep8);
        bundle8.putStringArrayList(FragmentSurveySeekBar.SURVEY_SEEKBAR_ANSWERS_LEFT, ChoiceTextLeftStep8);

        Step8 = new FragmentSurveySeekBar();
        Step8.setArguments(bundle8);
        addStep(Step8, true, 0, false);

        //Step 9 RESEARCH APP 2
        bundle9 = new Bundle();
        bundle9.putString(FragmentSurveyText.SURVEY_TEXT_TITLE, getResources().getString(R.string.title_se_screen9));
        bundle9.putString(FragmentSurveyText.SURVEY_TEXT_DESC, getResources().getString(R.string.question_se_screen9));
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
