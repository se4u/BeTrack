package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;

import com.github.fcannizzaro.materialstepper.AbstractStep;
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

    private static int SurveyAge = 24;
    private static int SurveySex = 0;
    private int SurveyEthnicity1 = 0;
    private String SurveyEthnicity2 = null;
    private int SurveyStudent = 0;
    private int SurveyEnglishLevel1 = 0;
    private int SurveyEnglishLevel2 = 0;
    private int SurveyEnglishLevel3 = 0;
    private int SurveyEnglishLevel4 = 0;
    private String SurveyEnglishLevel5 = null;
    private int SurveyEnglishLevel6 = 0;
    private int SurveyUniversity1 = 0;
    private String SurveyUniversity2 = null;
    private int SurveyUniversity3 = 0;

    private static final int SURVEY_AGE_START = 18;
    private static final int SURVEY_AGE_END = 40;
    private static final int SURVEY_DEFAULT_AGE = SurveyAge - SURVEY_AGE_START;

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

    @Override
    public void onComplete() {
        super.onComplete();
        int resultInt =0;
        String resultString = null;

        //Get the values from the survey

        //Step 1 AGE
        resultInt = Step1.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS1, 0);
        if (resultInt != 0) {
            SurveyAge = resultInt;
        }

        //Step 2 SEX
        resultInt = Step14.getArguments().getInt(FragmentSurvey10ChoicesRadio.SURVEY_STATUS, 0);
        if (resultInt != 0) {
            SurveySex = resultInt;
        }

        //Step 3 ETHNICITY 1
        resultInt = Step2.getArguments().getInt(FragmentSurvey10ChoicesRadio.SURVEY_STATUS, 0);
        if (resultInt != 0) {
            SurveyEthnicity1 = resultInt;
        }

        //Step 4 ETHNICITY 2
        resultString = Step3.getArguments().getString(FragmentSurveyText.SURVEY_STATUS, null);
        if (resultString != null) {
            SurveyEthnicity2 = resultString;
        }

        //Step 5 STUDENT
        resultInt = Step4.getArguments().getInt(FragmentSurvey2Choices.SURVEY_STATUS, 0);
        if (resultInt != 0) {
            SurveyStudent = resultInt;
        }

        //Step 6 ENGLISH LEVEL 1
        resultInt = Step5.getArguments().getInt(FragmentSurvey10ChoicesRadio.SURVEY_STATUS, 0);
        if (resultInt != 0) {
            SurveyEnglishLevel1 = resultInt;
        }

        //Step 7 ENGLISH LEVEL 2
        resultString = Step6.getArguments().getString(FragmentSurveyText.SURVEY_STATUS, null);
        if (resultString != null) {
            SurveyEnglishLevel2 = Integer.parseInt(resultString);
        } else {
            SurveyEnglishLevel2 = 0;
        }

        //Step 8 ENGLISH LEVEL 3
        resultInt = Step7.getArguments().getInt(FragmentSurvey2Choices.SURVEY_STATUS, 0);
        if (resultInt != 0) {
            SurveyEnglishLevel3 = resultInt;
        }

        //Step 9 ENGLISH LEVEL 4
        resultInt = Step8.getArguments().getInt(FragmentSurvey10ChoicesRadio.SURVEY_STATUS, 0);
        if (resultInt != 0) {
            SurveyEnglishLevel4 = resultInt;
        }

        //Step 10 ENGLISH LEVEL 5
        resultString = Step9.getArguments().getString(FragmentSurveyText.SURVEY_STATUS, null);
        if (resultString != null) {
            SurveyEnglishLevel5 = resultString;
        }

        //Step 11 ENGLISH LEVEL 6
        resultString = Step10.getArguments().getString(FragmentSurveyText.SURVEY_STATUS, null);
        if (resultString != null) {
            SurveyEnglishLevel6 = Integer.parseInt(resultString);
        } else {
            SurveyEnglishLevel6 = 0;
        }

        //Step 12 UNIVERSITY 1
        resultInt = Step11.getArguments().getInt(FragmentSurvey10ChoicesRadio.SURVEY_STATUS, 0);
        if (resultInt != 0) {
            SurveyUniversity1 = resultInt;
        }

        //Step 13 UNIVERSITY 2
        resultString = Step13.getArguments().getString(FragmentSurveyText.SURVEY_STATUS, null);
        if (resultString != null) {
            SurveyUniversity2 = resultString;
        }

        //Step 14 UNIVERSITY 3
        resultInt = Step12.getArguments().getInt(FragmentSurvey10ChoicesRadio.SURVEY_STATUS, 0);
        if (resultInt != 0) {
            SurveyUniversity3 = resultInt;
        }


        //Save those data in the local database
        values.clear();
        values.put(UtilsLocalDataBase.C_STARTSTUDY_AGE, SurveyAge);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_SEX, SurveySex);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_ETHNICITY1, SurveyEthnicity1);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_ETHNICITY2, SurveyEthnicity2);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_STUDENT, SurveyStudent);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_ENGLISH_LEVEL1, SurveyEnglishLevel1);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_ENGLISH_LEVEL2, SurveyEnglishLevel2);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_ENGLISH_LEVEL3, SurveyEnglishLevel3);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_ENGLISH_LEVEL4, SurveyEnglishLevel4);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_ENGLISH_LEVEL5, SurveyEnglishLevel5);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_ENGLISH_LEVEL6, SurveyEnglishLevel6);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_UNIVERSITY1, SurveyUniversity1);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_UNIVERSITY2, SurveyUniversity2);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_UNIVERSITY3, SurveyUniversity3);

        DateStudyStart = sdf.format(new Date());
        values.put(UtilsLocalDataBase.C_STARTSTUDY_DATE, DateStudyStart);
        TimeStudyStart = shf.format(new Date());
        values.put(UtilsLocalDataBase.C_STARTSTUDY_TIME, TimeStudyStart);

        AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_START_STUDY);

        ObjSettingsStudy.setStartDateSurvey(DateStudyStart);
        ObjSettingsStudy.setStartSurveyDone(true);

        //Save the number of notification per day for the study
        ObjSettingsStudy.setNbrOfNotificationToDo(ObjSettingsStudy.getStudyDuration() * UtilsGetNotification.getNbrPerDay());
        //Save the number of days for the study
        ObjSettingsStudy.setNbrOfDaysToDo(ObjSettingsStudy.getStudyDuration());

        Intent msgIntent = new Intent(getApplicationContext(), IntentServicePostData.class);
        //Start the service for sending the data to the remote server
        startService(msgIntent);

        Intent i = new Intent(ActivitySurveyStart.this, ActivityBeTrack.class);
        startActivity(i);
        finish();

        //Save that the study was just started
        SettingsBetrack.STUDY_JUST_STARTED = true;

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
        bundle1.putString(FragmentSurveyScrolling.SURVEY_SCROLLING_POST_UNIT_1, getResources().getString(R.string.survey_age));
        bundle1.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_START_RANGE_1, SURVEY_AGE_START);
        bundle1.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_END_RANGE_1, SURVEY_AGE_END);
        bundle1.putInt(FragmentSurveyScrolling.SURVEY_SCROLLING_DEFAULT_VALUE_1, SURVEY_DEFAULT_AGE);
        Step1 = new FragmentSurveyScrolling();
        Step1.setArguments(bundle1);
        addStep(Step1, true, 0, false);

        //Step 2 SEX
        bundle14 = new Bundle();
        bundle14.putString(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen14));
        bundle14.putString(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_DESC, getResources().getString(R.string.question_ss_screen14));
        ArrayList<String> RbText14 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_ss_screen14));
            add(getResources().getString(R.string.option2_ss_screen14));
            add(getResources().getString(R.string.option3_ss_screen14));
        }};

        bundle14.putStringArrayList(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_RB_TEXT, RbText14);

        Step14 = new FragmentSurvey10ChoicesRadio();
        Step14.setArguments(bundle14);
        addStep(Step14, true, 0, true);


        //Step 3 ETHNICITY 1
        bundle2 = new Bundle();
        bundle2.putString(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen2));
        bundle2.putString(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_DESC, getResources().getString(R.string.question_ss_screen2));
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

        bundle2.putStringArrayList(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_RB_TEXT, RbText2);
        bundle2.putIntegerArrayList(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_ENABLE_NEXT_STEP, NextStep2);

        Step2 = new FragmentSurvey10ChoicesRadio();
        Step2.setArguments(bundle2);
        addStep(Step2, true, 0, true);

        //Step 4 ETHNICITY 2
        bundle3 = new Bundle();
        bundle3.putString(FragmentSurveyText.SURVEY_TEXT_TITLE, getResources().getString(R.string.title_ss_screen3));
        bundle3.putString(FragmentSurveyText.SURVEY_TEXT_DESC, getResources().getString(R.string.question_ss_screen3));
        bundle3.putString(FragmentSurveyText.SURVEY_TEXT_COMMENT, null);
        bundle3.putBoolean(FragmentSurveyText.SURVEY_TEXT_IS_OPTIONAL, false);
        Step3 = new FragmentSurveyText();
        Step3.setArguments(bundle3);
        addStep(Step3, false, 1, false);

        //Step 5 STUDENT
        bundle4 = new Bundle();
        bundle4.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen4));
        bundle4.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.question_ss_screen4));
        ArrayList<Integer> NextStep4 = new ArrayList<Integer>() {{
            add(1); //Optional step are shown  (yes)
            add(1); //Optional step are shown (no)
        }};
        bundle4.putIntegerArrayList(FragmentSurvey2Choices.SURVEY_2_CHOICES_ENABLE_NEXT_STEP, NextStep4);
        Step4 = new FragmentSurvey2Choices();
        Step4.setArguments(bundle4);
        addStep(Step4, true, 0, false);

        //Step 6 ENGLISH LEVEL 1
        bundle5 = new Bundle();
        bundle5.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen5));
        bundle5.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.question_ss_screen5));
        ArrayList<Integer> NextStep5 = new ArrayList<Integer>() {{
            add(0); //Optional step are hidden  (yes)
            add(1); //Optional step are shown (no)
        }};
        bundle5.putIntegerArrayList(FragmentSurvey2Choices.SURVEY_2_CHOICES_ENABLE_NEXT_STEP, NextStep5);
        Step5 = new FragmentSurvey2Choices();
        Step5.setArguments(bundle5);
        addStep(Step5, true, 0, true);

        //Step 7 ENGLISH LEVEL 2
        bundle6 = new Bundle();
        bundle6.putString(FragmentSurveyText.SURVEY_TEXT_TITLE, getResources().getString(R.string.title_ss_screen6));
        bundle6.putString(FragmentSurveyText.SURVEY_TEXT_DESC, getResources().getString(R.string.question_ss_screen6));
        bundle6.putString(FragmentSurveyText.SURVEY_TEXT_COMMENT, null);
        bundle6.putBoolean(FragmentSurveyText.SURVEY_TEXT_IS_OPTIONAL, false);
        bundle6.putBoolean(FragmentSurveyText.SURVEY_TEXT_IS_NUMBER_INPUT, true);
        Step6 = new FragmentSurveyText();
        Step6.setArguments(bundle6);
        addStep(Step6, false, 1, false);

        //Step 8 ENGLISH LEVEL 3
        bundle7 = new Bundle();
        bundle7.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen7));
        bundle7.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.question_ss_screen7));
        ArrayList<Integer> NextStep7 = new ArrayList<Integer>() {{
            add(1); //Optional step are shown  (yes)
            add(1); //Optional step are shown (no)
        }};
        bundle7.putIntegerArrayList(FragmentSurvey2Choices.SURVEY_2_CHOICES_ENABLE_NEXT_STEP, NextStep7);
        Step7 = new FragmentSurvey2Choices();
        Step7.setArguments(bundle7);
        addStep(Step7, false, 1, false);

        //Step 9 ENGLISH LEVEL 4
        bundle8 = new Bundle();
        bundle8.putString(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen8));
        bundle8.putString(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_DESC, getResources().getString(R.string.question_ss_screen8));
        ArrayList<String> RbText8 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_ss_screen8));
            add(getResources().getString(R.string.option2_ss_screen8));
            add(getResources().getString(R.string.option3_ss_screen8));
        }};

        ArrayList<Integer> NextStep8 = new ArrayList<Integer>() {{
            add(2); //Optional step visible
            add(2); //Optional step visible
            add(1); //Optional step visible
        }};

        bundle8.putStringArrayList(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_RB_TEXT, RbText8);
        bundle8.putIntegerArrayList(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_ENABLE_NEXT_STEP, NextStep8);

        Step8 = new FragmentSurvey10ChoicesRadio();
        Step8.setArguments(bundle8);
        addStep(Step8, false, 1, true);

        //Step 10 ENGLISH LEVEL 5
        bundle9 = new Bundle();
        bundle9.putString(FragmentSurveyText.SURVEY_TEXT_TITLE, getResources().getString(R.string.title_ss_screen9));
        bundle9.putString(FragmentSurveyText.SURVEY_TEXT_DESC, getResources().getString(R.string.question_ss_screen9));
        bundle9.putString(FragmentSurveyText.SURVEY_TEXT_COMMENT, null);
        bundle9.putBoolean(FragmentSurveyText.SURVEY_TEXT_IS_OPTIONAL, false);
        Step9 = new FragmentSurveyText();
        Step9.setArguments(bundle9);
        addStep(Step9, false, 2, false);

        //Step 11 ENGLISH LEVEL 6
        bundle10 = new Bundle();
        bundle10.putString(FragmentSurveyText.SURVEY_TEXT_TITLE, getResources().getString(R.string.title_ss_screen10));
        bundle10.putString(FragmentSurveyText.SURVEY_TEXT_DESC, getResources().getString(R.string.question_ss_screen10));
        bundle10.putString(FragmentSurveyText.SURVEY_TEXT_COMMENT, null);
        bundle10.putBoolean(FragmentSurveyText.SURVEY_TEXT_IS_OPTIONAL, false);
        bundle10.putBoolean(FragmentSurveyText.SURVEY_TEXT_IS_NUMBER_INPUT, true);
        Step10 = new FragmentSurveyText();
        Step10.setArguments(bundle10);
        addStep(Step10, false, 3, false);

        //Step 12 UNIVERSITY 1
        bundle11 = new Bundle();
        bundle11.putString(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen11));
        bundle11.putString(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_DESC, getResources().getString(R.string.question_ss_screen11));
        ArrayList<String> RbText11 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_ss_screen11));
            add(getResources().getString(R.string.option2_ss_screen11));
            add(getResources().getString(R.string.option3_ss_screen11));
            add(getResources().getString(R.string.option4_ss_screen11));
            add(getResources().getString(R.string.option5_ss_screen11));
        }};

        ArrayList<Integer> NextStep11 = new ArrayList<Integer>() {{
            add(0); //Optional step visible
            add(0); //Optional step visible
            add(0); //Optional step visible
            add(0); //Optional step visible
            add(1); //Optional step visible
        }};

        bundle11.putStringArrayList(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_RB_TEXT, RbText11);
        bundle11.putIntegerArrayList(FragmentSurvey10ChoicesRadio.SURVEY_10_CHOICES_ENABLE_NEXT_STEP, NextStep11);

        Step11 = new FragmentSurvey10ChoicesRadio();
        Step11.setArguments(bundle11);
        addStep(Step11, true, 0, true);

        //Step 13 UNIVERSITY 2
        bundle13 = new Bundle();
        bundle13.putString(FragmentSurveyText.SURVEY_TEXT_TITLE, getResources().getString(R.string.title_ss_screen13));
        bundle13.putString(FragmentSurveyText.SURVEY_TEXT_DESC, getResources().getString(R.string.question_ss_screen13));
        bundle13.putString(FragmentSurveyText.SURVEY_TEXT_COMMENT, null);
        bundle13.putBoolean(FragmentSurveyText.SURVEY_TEXT_IS_OPTIONAL, false);
        Step13 = new FragmentSurveyText();
        Step13.setArguments(bundle13);
        addStep(Step13, false, 1, false);

        //Step 14 UNIVERSITY 3
        bundle12 = new Bundle();
        bundle12.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen12));
        bundle12.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.question_ss_screen12));
        bundle12.putBoolean(FragmentSurvey2Choices.SURVEY_2_CHOICES_ENABLE_AUTOMOVE, false);

        Step12 = new FragmentSurvey2Choices();
        Step12.setArguments(bundle12);
        addStep(Step12, true, 0, false);

        super.onCreate(savedInstanceState);
    }
}

