package com.app.uni.betrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.ArrayList;

/**
 * Created by cevincent on 17/01/2017.
 */

public class FragmentSurvey10Choices extends AbstractStep {

    //Output
    public  static final String SURVEY_STATUS = "SURVEY_STATUS";
    public int SurveyStatus = -1;

    //Input
    public static final String SURVEY_10_CHOICES_TITLE = "SURVEY_10_CHOICES_TITLE";
    public static final String SURVEY_10_CHOICES_DESC = "SURVEY_10_CHOICES_DESC";
    public static final String SURVEY_10_CHOICES_RB_TEXT = "SURVEY_10_CHOICES_RB_TEXT";
    public static final String SURVEY_10_CHOICES_ENABLE_NEXT_STEP = "SURVEY_10_CHOICES_ENABLE_NEXT_STEP";

    private static final int SURVEY_RB_MAX = 10;

    RadioButton[] radiobutton = new RadioButton[SURVEY_RB_MAX];

    private TextView Title;
    private TextView Description;
    private Bundle bundle;
    private ArrayList<Integer>  NextStep;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.survey_10choices, container, false);
        int NbrRbVisible = 0;

        Title = (TextView) v.findViewById(R.id.survey_title);
        Description = (TextView) v.findViewById(R.id.survey_desc);

        radiobutton[0] = (RadioButton) v.findViewById(R.id.radio_1);
        radiobutton[1] = (RadioButton) v.findViewById(R.id.radio_2);
        radiobutton[2] = (RadioButton) v.findViewById(R.id.radio_3);
        radiobutton[3] = (RadioButton) v.findViewById(R.id.radio_4);
        radiobutton[4] = (RadioButton) v.findViewById(R.id.radio_5);
        radiobutton[5] = (RadioButton) v.findViewById(R.id.radio_6);
        radiobutton[6] = (RadioButton) v.findViewById(R.id.radio_7);
        radiobutton[7] = (RadioButton) v.findViewById(R.id.radio_8);
        radiobutton[8] = (RadioButton) v.findViewById(R.id.radio_9);
        radiobutton[9] = (RadioButton) v.findViewById(R.id.radio_10);

        bundle = this.getArguments();
        String SurveyTitle = bundle.getString(SURVEY_10_CHOICES_TITLE, null);
        String SurveyDescription = bundle.getString(SURVEY_10_CHOICES_DESC, null);
        ArrayList<String> RbText = bundle.getStringArrayList(SURVEY_10_CHOICES_RB_TEXT);
        NextStep = bundle.getIntegerArrayList(SURVEY_10_CHOICES_ENABLE_NEXT_STEP);

        Title.setText(SurveyTitle);
        Description.setText(SurveyDescription);

        for (NbrRbVisible = 0; NbrRbVisible < RbText.size(); NbrRbVisible++)
        {
            radiobutton[NbrRbVisible].setText(RbText.get(NbrRbVisible));
        }

        while (NbrRbVisible < SURVEY_RB_MAX)
        {
            radiobutton[NbrRbVisible].setVisibility(View.INVISIBLE);
            NbrRbVisible++;
        }

        if (SurveyStatus != -1) {
            radiobutton[SurveyStatus].setChecked(true);
        }
        RadioGroup radioGroup;
        radioGroup = (RadioGroup) v.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId)
                {
                    case R.id.radio_1:
                        SurveyStatus = 0;
                        break;
                    case R.id.radio_2:
                        SurveyStatus = 1;
                        break;
                    case R.id.radio_3:
                        SurveyStatus = 2;
                        break;
                    case R.id.radio_4:
                        SurveyStatus = 3;
                        break;
                    case R.id.radio_5:
                        SurveyStatus = 4;
                        break;
                    case R.id.radio_6:
                        SurveyStatus = 5;
                        break;
                    case R.id.radio_7:
                        SurveyStatus = 6;
                        break;
                    case R.id.radio_8:
                        SurveyStatus = 7;
                        break;
                    case R.id.radio_9:
                        SurveyStatus = 8;
                        break;
                    case R.id.radio_10:
                        SurveyStatus = 9;
                        break;
                }

                bundle.putInt(SURVEY_STATUS, SurveyStatus);
            }
        });
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt(SURVEY_STATUS, SurveyStatus);
    }

    @Override
    public String name() {
        return "Tab " + getArguments().getInt(SURVEY_STATUS, 0);
    }

    @Override
    public boolean isOptional() {
        return false;
    }


    @Override
    public void onStepVisible() {
    }

    @Override
    public void onNext() {
        if (NextStep != null) {
            if (NextStep.get(SurveyStatus) >= 1) {
                //Check if the next step is invisible
                if (getVisibilityNextStep(NextStep.get(SurveyStatus)) == false) {
                    //Make it visible
                    setVisibilityNextStep(true, NextStep.get(SurveyStatus));
                }

            } else {
                //Check if the next step is visible
                if (getVisibilityNextStep(NextStep.get(SurveyStatus)) == true) {
                    //Make it visible
                    setVisibilityNextStep(false, 1);
                }
            }
        }
        System.out.println("onNext");
    }


    @Override
    public void onPrevious() {
        System.out.println("onPrevious");
    }

    @Override
    public String optional() {
        return "You can skip";
    }

    @Override
    public boolean nextIf() {
        return SurveyStatus > -1;
    }

    @Override
    public String error() {
        return getResources().getString(R.string.question_error);
    }


}
