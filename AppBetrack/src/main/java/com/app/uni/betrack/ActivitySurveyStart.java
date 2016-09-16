package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private ContentValues values = new ContentValues();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
    private UtilsLocalDataBase localdatabase = new UtilsLocalDataBase(this);
    public UtilsLocalDataBase AccesLocalDB()
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


    @Override
    public void onComplete() {
        super.onComplete();

        //get data from the different steps
        SurveyAge = Step1.getArguments().getInt(FragmentSurvey2Choices.SURVEY_STATUS, 0);
        SurveyInRelation = Step2.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS, 0);
        SurveyLengthPeriod = Step3.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS, 0);
        SurveyLenghCycle = Step4.getArguments().getInt(FragmentSurveyScrolling.SURVEY_STATUS, 0);
        SurveyContraception  = Step5.getArguments().getString(FragmentSurveyText.SURVEY_STATUS, null);

        //Save those data in the local database
        values.clear();
        values.put(UtilsLocalDataBase.C_STARTSTUDY_PID, ObjSettingsStudy.getIdUser());
        values.put(UtilsLocalDataBase.C_STARTSTUDY_AGE, SurveyAge);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_RELATIONSHIP, SurveyInRelation);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_AVGPERIODLENGHT, SurveyLengthPeriod);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_AVGMENSTRUALCYCLE, SurveyLenghCycle);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_CONTRACEPTION, SurveyContraception);
        DateStudyStart = sdf.format(new Date());
        values.put(UtilsLocalDataBase.C_STARTSTUDY_DATE, DateStudyStart);

        AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_START_STUDY);
        Log.d(TAG, "idUser: " + ObjSettingsStudy.getIdUser() + "Participant age: " + SurveyAge + " In a relationship: " + SurveyInRelation
        + "period lenght" + SurveyLengthPeriod + "cycle lenght" + SurveyLenghCycle + "Contraception used" + SurveyContraception
        + "date start" + DateStudyStart);

        ObjSettingsStudy.setStartSurveyDone(true);

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

        ObjSettingsStudy = SettingsStudy.getInstance(this);

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
        super.onCreate(savedInstanceState);
    }
}

