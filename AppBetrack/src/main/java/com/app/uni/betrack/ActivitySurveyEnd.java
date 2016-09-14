package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cevincent on 14/09/2016.
 */
public class ActivitySurveyEnd  extends DotStepper {
    private static final String TAG = "ActivitySurveyEnd";

    private int i = 1;

    private int SurveyInRelation = -1;
    private String SurveyContraception = null;
    private String DateStudyEnd = null;

    private ContentValues values = new ContentValues();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
    private UtilsLocalDataBase localdatabase = new UtilsLocalDataBase(this);
    public UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }
    private SettingsStudy ObjSettingsStudy;

    @Override
    public void onComplete() {
        super.onComplete();

        //TODO still need to find the proper to get the values from fragments...
        values.clear();
        values.put(UtilsLocalDataBase.C_ENDSTUDY_PID, ObjSettingsStudy.getIdUser());
        values.put(UtilsLocalDataBase.C_ENDSTUDY_RELATIONSHIP, SurveyInRelation);
        values.put(UtilsLocalDataBase.C_ENDSTUDY_CONTRACEPTION, SurveyContraception);
        DateStudyEnd = sdf.format(new Date());
        values.put(UtilsLocalDataBase.C_ENDSTUDY_DATE, DateStudyEnd);

        AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_END_STUDY);
        Log.d(TAG, "idUser: " + ObjSettingsStudy.getIdUser()
                + "In a relationship: " + SurveyInRelation
                + "Contraception used: " + SurveyContraception
                + "Date end of the study: " + DateStudyEnd);

        ObjSettingsStudy.setStartSurveyDone(true);

        //Intent i = new Intent(ActivitySurveyStart.this, ActivityBeTrack.class);
        //startActivity(i);
        //finish();
        System.out.println("completed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setErrorTimeout(1500);
        setTitle(getResources().getString(R.string.app_name));

        ObjSettingsStudy = SettingsStudy.getInstance(this);

        //Step 1 RELATIONSHIP
        Bundle bundle1 = new Bundle();
        bundle1.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_se_screen1));
        bundle1.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.question_se_screen1));
        AbstractStep Step1 = new FragmentSurvey2Choices();
        Step1.setArguments(bundle1);
        addStep(Step1);

        //Step 2 CONTRACEPTION

        super.onCreate(savedInstanceState);
    }
}
