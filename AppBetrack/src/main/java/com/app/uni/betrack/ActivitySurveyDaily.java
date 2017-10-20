package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.ProgressStepper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * Created by cevincent on 16/09/2016.
 */

public class ActivitySurveyDaily   extends ProgressStepper {
    private static final String TAG = "ActivitySurveyDaily";

    private int Mood = 0;
    private int Social = 0;
    private int Bored = 0;
    private int Relaxed = 0;
    private int Interact = 0;
    private int UsedSocial = 0;
    private int SocialComputer = 0;
    private int WhichWebsite = 0;
    private int Actively = 0;

    private String DateDaily = null;
    private String TimeDaily = null;

    private AbstractStep Step1;
    private Bundle bundle1;

    private AbstractStep Step3;
    private Bundle bundle3;


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

        Mood = Step1.getArguments().getInt(FragmentSurvey5ChoicesRadio.SURVEY_STATUS1, 0);
        Bored = Step1.getArguments().getInt(FragmentSurvey5ChoicesRadio.SURVEY_STATUS2, 0);
        Social = Step1.getArguments().getInt(FragmentSurvey5ChoicesRadio.SURVEY_STATUS3, 0);
        Relaxed = Step1.getArguments().getInt(FragmentSurvey5ChoicesRadio.SURVEY_STATUS4, 0);

        UsedSocial = Step6.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS1, 0);
        SocialComputer = Step7.getArguments().getInt(FragmentSurvey2Choices.SURVEY_STATUS, 0);
        WhichWebsite = Step8.getArguments().getInt(FragmentSurvey10ChoicesCheckBox.SURVEY_STATUS, 0);
        Actively = Step9.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS1, 0);
        Interact = Step3.getArguments().getInt(FragmentSurveySeekBar.SURVEY_STATUS1, 0);

        values.clear();
        values.put(UtilsLocalDataBase.C_DAILYSTATUS_MOOD, Mood);
        values.put(UtilsLocalDataBase.C_DAILYSTATUS_SOCIAL, Social);
        values.put(UtilsLocalDataBase.C_DAILYSTATUS_BORED, Bored);
        values.put(UtilsLocalDataBase.C_DAILYSTATUS_RELAXED, Relaxed);
        values.put(UtilsLocalDataBase.C_DAILYSTATUS_INTERACT, Interact);
        values.put(UtilsLocalDataBase.C_DAILYSTATUS_USED_SOCIAL, UsedSocial);
        values.put(UtilsLocalDataBase.C_DAILYSTATUS_SOCIAL_COMPUTER, SocialComputer);
        values.put(UtilsLocalDataBase.C_DAILYSTATUS_WHICH_WEBSITES, WhichWebsite);
        values.put(UtilsLocalDataBase.C_DAILYSTATUS_ACTIVELY, Actively);
        DateDaily = sdf.format(new Date());
        values.put(UtilsLocalDataBase.C_DAILYSTATUS_DATE, DateDaily);
        TimeDaily = shf.format(new Date());
        values.put(UtilsLocalDataBase.C_DAILYSTATUS_TIME, TimeDaily);

        AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_DAILYSTATUS);
        Log.d(TAG, "idUser: " + ObjSettingsStudy.getIdUser()
                + " Mood: " + Mood
                + " Social: " + Social
                + " Bored: " + Bored
                + " Relaxed: " + Relaxed
                + " Interact: " + Interact
                + " UsedSocial: " + UsedSocial
                + " SocialComputer: " + SocialComputer
                + " WhichWebsite: " + WhichWebsite
                + " Actively: " + Actively
                + " Date: " + DateDaily
                + " Time: " + TimeDaily);

        ObjSettingsStudy.setDailySurveyState(ObjSettingsStudy.getDailySurveyState() - 1);

        Intent msgIntent = new Intent(getApplicationContext(), IntentServicePostData.class);
        //Start the service for sending the data to the remote server
        startService(msgIntent);

        if (ObjSettingsStudy.getNbrOfNotificationToDo() == 0) {
            ObjSettingsStudy.setLastDayStudyState(SettingsStudy.LastDayStudyState.START_SURVEY_DONE);
        }


        Intent i = new Intent(this, ActivityBeTrack.class);
        startActivity(i);
        finish();
        System.out.println("SurveyDaily completed");
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

        //Step 1 Daily Surveys: How do you feel right now?
        bundle1 = new Bundle();
        bundle1.putString(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_TITLE, getResources().getString(R.string.title_sd_screen1));
        bundle1.putString(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_DESC, getResources().getString(R.string.question_sd_screen1));

        ArrayList<String> ChoiceTextRightStep1 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option2_sd_screen1));
            add(getResources().getString(R.string.option4_sd_screen1));
            add(getResources().getString(R.string.option6_sd_screen1));
            add(getResources().getString(R.string.option8_sd_screen1));
        }};

        ArrayList<String> ChoiceTextLeftStep1 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_sd_screen1));
            add(getResources().getString(R.string.option3_sd_screen1));
            add(getResources().getString(R.string.option5_sd_screen1));
            add(getResources().getString(R.string.option7_sd_screen1));
        }};

        bundle1.putStringArrayList(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_RB_ANSWERS_RIGHT, ChoiceTextRightStep1);
        bundle1.putStringArrayList(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_RB_ANSWERS_LEFT, ChoiceTextLeftStep1);

        Step1 = new FragmentSurvey5ChoicesRadio();
        Step1.setArguments(bundle1);
        addStep(Step1, true, 0, false);

        //Step 6 Daily Surveys: How much have you used social networking sites/apps since the last time we asked?
        bundle6 = new Bundle();
        bundle6.putString(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_TITLE, getResources().getString(R.string.title_sd_screen6));
        bundle6.putString(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_DESC, getResources().getString(R.string.question_sd_screen6));

        ArrayList<String> ChoiceTextRightStep6 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option2_sd_screen6));
        }};
        ArrayList<String> ChoiceTextLeftStep6 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_sd_screen6));
        }};

        bundle6.putStringArrayList(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_RB_ANSWERS_RIGHT, ChoiceTextRightStep6);
        bundle6.putStringArrayList(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_RB_ANSWERS_LEFT, ChoiceTextLeftStep6);

        ArrayList<Integer> NextStep6 = new ArrayList<Integer>() {{
            add(1); //Optional step visible
            add(1); //Not used
            add(1); //Not used
            add(1); //Not used
        }};

        ArrayList<Integer> NextStepTrigger6 = new ArrayList<Integer>() {{
            add(1); //Optional step visible
            add(-2); //Not used
            add(-2); //Not used
            add(-2); //Not used
        }};

        bundle6.putIntegerArrayList(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_ENABLE_NEXT_STEP, NextStep6);
        bundle6.putIntegerArrayList(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_ENABLE_NEXT_STEP_TRIGGER, NextStepTrigger6);

        Step6 = new FragmentSurvey5ChoicesRadio();
        Step6.setArguments(bundle6);
        addStep(Step6, true, 0, true);

        //Step 7 Daily Surveys: Have you used social networking sites on a device other than your smartphone since the last time we asked?
        bundle7 = new Bundle();
        bundle7.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_sd_screen7));
        bundle7.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.question_sd_screen7));
        Step7 = new FragmentSurvey2Choices();
        ArrayList<Integer> NextStep7 = new ArrayList<Integer>() {{
            add(1); //Optional step are shown  (yes)
            add(2); //Optional step are shown (no)
        }};
        bundle7.putIntegerArrayList(FragmentSurvey2Choices.SURVEY_2_CHOICES_ENABLE_NEXT_STEP, NextStep7);
        Step7.setArguments(bundle7);
        addStep(Step7, false, 1, true);

        //Step 8 Daily Surveys: Which social networking sites did you use?
        bundle8 = new Bundle();
        bundle8.putString(FragmentSurvey10ChoicesCheckBox.SURVEY_10_CHOICES_TITLE, getResources().getString(R.string.title_sd_screen8));
        bundle8.putString(FragmentSurvey10ChoicesCheckBox.SURVEY_10_CHOICES_DESC, getResources().getString(R.string.question_sd_screen8));

        ArrayList<String> CbText8 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_sd_screen8));
            add(getResources().getString(R.string.option2_sd_screen8));
            add(getResources().getString(R.string.option3_sd_screen8));
        }};

        ArrayList<Integer> NextStep8 = new ArrayList<Integer>() {{
            add(1); //Optional step visible
            add(0); //Not used
            add(0); //Not used
        }};

        bundle8.putStringArrayList(FragmentSurvey10ChoicesCheckBox.SURVEY_10_CHOICES_CB_TEXT, CbText8);
        bundle8.putIntegerArrayList(FragmentSurvey10ChoicesCheckBox.SURVEY_10_CHOICES_ENABLE_NEXT_STEP, NextStep8);

        Step8 = new FragmentSurvey10ChoicesCheckBox();
        Step8.setArguments(bundle8);
        addStep(Step8, false, 2, true);

        //Step 9 Daily Surveys: Which social networking sites did you use?
        bundle9 = new Bundle();
        bundle9.putString(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_TITLE, getResources().getString(R.string.title_sd_screen9));
        bundle9.putString(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_DESC, getResources().getString(R.string.question_sd_screen9));

        ArrayList<String> ChoiceTextRightStep9 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option2_sd_screen9));
        }};
        ArrayList<String> ChoiceTextLeftStep9 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_sd_screen9));
        }};

        bundle9.putStringArrayList(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_RB_ANSWERS_RIGHT, ChoiceTextRightStep9);
        bundle9.putStringArrayList(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_RB_ANSWERS_LEFT, ChoiceTextLeftStep9);

        Step9 = new FragmentSurvey5ChoicesRadio();
        Step9.setArguments(bundle9);
        addStep(Step9, false, 3, false);


        //Step 3 Daily Surveys: How much have you interacted with other people “directly” (face-to- face or via phone call) since the last time we asked?
        bundle3 = new Bundle();
        bundle3.putString(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_TITLE, getResources().getString(R.string.title_sd_screen5));
        bundle3.putString(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_DESC, getResources().getString(R.string.question_sd_screen5));

        ArrayList<String> ChoiceTextRightStep3 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option2_sd_screen5));
        }};

        ArrayList<String> ChoiceTextLeftStep3 = new ArrayList<String>() {{
            add(getResources().getString(R.string.option1_sd_screen5));
        }};

        bundle3.putStringArrayList(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_RB_ANSWERS_RIGHT, ChoiceTextRightStep3);
        bundle3.putStringArrayList(FragmentSurvey5ChoicesRadio.SURVEY_5_CHOICES_RB_ANSWERS_LEFT, ChoiceTextLeftStep3);

        Step3 = new FragmentSurvey5ChoicesRadio();
        Step3.setArguments(bundle3);
        addStep(Step3, true, 0, false);

        super.onCreate(savedInstanceState);
    }
}
