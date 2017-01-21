package com.app.uni.betrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;

import java.util.Date;

public class ActivityStartStudy extends DotStepper {

    private AbstractStep Step1;
    private Bundle bundle1;

    private AbstractStep Step2;
    private Bundle bundle2;

    private SettingsStudy ObjSettingsStudy;

    @Override
    public void onComplete() {
        super.onComplete();
        Intent i;
        //We have all the information to start the study
        ObjSettingsStudy.setStudyStarted(true);
        if (ObjSettingsStudy.getSetupBetrackDone() == false) {
            i = new Intent(ActivityStartStudy.this, ActivitySetupBetrack.class);
        } else {
            i = new Intent(ActivityStartStudy.this, ActivitySurveyStart.class);
        }
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ObjSettingsStudy = SettingsStudy.getInstance(this);

        //We manage to read the study available we display the disclaimer
        //Step 1 Disclaimer
        bundle1 = new Bundle();
        bundle1.putString(FragmentDisclaimers.ACTIVITY_DISCLAIMER_TITLE, getResources().getString(R.string.disclaimer_title));
        bundle1.putString(FragmentDisclaimers.ACTIVITY_DISCLAIMER_DESC, getResources().getString(R.string.disclaimer));
        bundle1.putString(FragmentDisclaimers.ACTIVITY_DISCLAIMER_AGREE, getResources().getString(R.string.disclaimer_agree));
        Step1 = new FragmentDisclaimers();
        Step1.setArguments(bundle1);
        addStep(Step1, true);
        //Step 2 Study details
        bundle2 = new Bundle();
        bundle2.putString(FragmentDisclaimers.ACTIVITY_DISCLAIMER_TITLE, ObjSettingsStudy.getStudyName());
        bundle2.putString(FragmentDisclaimers.ACTIVITY_DISCLAIMER_DESC, ObjSettingsStudy.getStudyDescription());
        bundle2.putString(FragmentDisclaimers.ACTIVITY_DISCLAIMER_AGREE, getResources().getString(R.string.study_agree));
        Step2 = new FragmentDisclaimers();
        Step2.setArguments(bundle2);
        addStep(Step2, true);

        super.onCreate(savedInstanceState);
    }
}
