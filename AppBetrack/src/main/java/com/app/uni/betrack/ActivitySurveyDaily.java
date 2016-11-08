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
 * Created by cevincent on 16/09/2016.
 */
public class ActivitySurveyDaily   extends DotStepper {
    private static final String TAG = "ActivitySurveyDaily";

    private int SurveyPeriod = -1;
    private int SurveySocial1 = 0;
    private int SurveySocial2 = 0;
    private int SurveyMood = 0;
    private int PhoneUsage = 0;

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

        SurveyPeriod = Step1.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS, 0);
        SurveySocial1 = Step2.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS, 0);
        SurveySocial2 = Step3.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS, 0);
        SurveyMood = Step4.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS, 0);

        values.clear();
        values.put(UtilsLocalDataBase.C_USER_PERIOD, SurveyPeriod);
        values.put(UtilsLocalDataBase.C_USER_SOCIAL1_LIFE, SurveySocial1);
        values.put(UtilsLocalDataBase.C_USER_SOCIAL2_LIFE, SurveySocial2);

        try {
            SettingsStudy.SemPhoneUsage.acquire();
            PhoneUsage = ObjSettingsStudy.getPhoneUsage();
            ObjSettingsStudy.setPhoneUsage(PhoneUsage + (int) ((System.currentTimeMillis() - IntentServiceTrackApp.ScreenOnStartTime) / 1000));
            IntentServiceTrackApp.ScreenOnStartTime = System.currentTimeMillis();
            PhoneUsage = ObjSettingsStudy.getPhoneUsage();
            values.put(UtilsLocalDataBase.C_USER_PHONE_USAGE, PhoneUsage);
            ObjSettingsStudy.setPhoneUsage(0);
            SettingsStudy.SemPhoneUsage.release();
        } catch (Exception e) {}

        values.put(UtilsLocalDataBase.C_USER_MOOD, SurveyMood);
        DateDaily = sdf.format(new Date());
        values.put(UtilsLocalDataBase.C_USER_DATE, DateDaily);
        TimeDaily = shf.format(new Date());
        values.put(UtilsLocalDataBase.C_USER_TIME, TimeDaily);

        AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_USER);
        Log.d(TAG, "idUser: " + ObjSettingsStudy.getIdUser()
                + " Period: " + SurveyPeriod
                + " SurveySocial1: " + SurveySocial1
                + " SurveySocial2: " + SurveySocial2
                + " PhoneUsage: " + PhoneUsage
                + " SurveyMood: " + SurveyMood
                + " Date: " + DateDaily
                + " Time: " + TimeDaily);


        ObjSettingsStudy.setDailySurveyDone(true);

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

        //Step 2 (social 1/2)
        bundle2 = new Bundle();
        bundle2.putString(FragmentSurvey6Choices.SURVEY_6_CHOICES_TITLE, getResources().getString(R.string.title_sd_screen3));
        bundle2.putString(FragmentSurvey6Choices.SURVEY_6_CHOICES_DESC, getResources().getString(R.string.question_sd_screen3));

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

        bundle2.putIntegerArrayList(FragmentSurvey6Choices.SURVEY_6_CHOICES_ICON, Icons1);
        bundle2.putStringArrayList(FragmentSurvey6Choices.SURVEY_6_CHOICES_ICON_TEXT, IconsText1);

        Step2 = new FragmentSurvey6Choices();
        Step2.setArguments(bundle2);
        addStep(Step2);

        //Step 3 (social 2/2)
        bundle3 = new Bundle();
        bundle3.putString(FragmentSurvey6Choices.SURVEY_6_CHOICES_TITLE, getResources().getString(R.string.title_sd_screen4));
        bundle3.putString(FragmentSurvey6Choices.SURVEY_6_CHOICES_DESC, getResources().getString(R.string.question_sd_screen4));

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

        bundle3.putIntegerArrayList(FragmentSurvey6Choices.SURVEY_6_CHOICES_ICON, Icons2);
        bundle3.putStringArrayList(FragmentSurvey6Choices.SURVEY_6_CHOICES_ICON_TEXT, IconsText2);

        Step3 = new FragmentSurvey6Choices();
        Step3.setArguments(bundle3);
        addStep(Step3);

        //Step 4 (Mood)

        bundle4 = new Bundle();
        bundle4.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_TITLE, getResources().getString(R.string.title_sd_screen5));
        bundle4.putString(FragmentSurveySeekBar.SURVEY_SEEKBAR_CHOICES_DESC, getResources().getString(R.string.question_sd_screen5));
        Step4 = new FragmentSurveySeekBar();
        Step4.setArguments(bundle4);
        addStep(Step4);

        super.onCreate(savedInstanceState);
    }
}
