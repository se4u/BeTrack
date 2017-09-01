package com.app.uni.betrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.ArrayList;

/**
 * Created by cvincent on 01/09/17.
 */

public class FragmentSurvey10ChoicesCheckBox extends AbstractStep {

    //Output
    public  static final String SURVEY_STATUS = "SURVEY_STATUS";
    public int SurveyStatus = -1;

    //Input
    public static final String SURVEY_10_CHOICES_TITLE = "SURVEY_10_CHOICES_TITLE";
    public static final String SURVEY_10_CHOICES_DESC = "SURVEY_10_CHOICES_DESC";
    public static final String SURVEY_10_CHOICES_CB_TEXT = "SURVEY_10_CHOICES_CB_TEXT";
    public static final String SURVEY_10_CHOICES_ENABLE_NEXT_STEP = "SURVEY_10_CHOICES_ENABLE_NEXT_STEP";

    private static final int SURVEY_CB_MAX = 10;

    CheckBox[] checkbox = new CheckBox[SURVEY_CB_MAX];

    private TextView Title;
    private TextView Description;
    private Bundle bundle;
    private ArrayList<Integer> NextStep = null;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.survey_10choicescheckbox, container, false);
        int NbrCbVisible = 0;

        Title = (TextView) v.findViewById(R.id.survey_title);
        Description = (TextView) v.findViewById(R.id.survey_desc);

        checkbox[0] = (CheckBox) v.findViewById(R.id.checkbox_1);
        checkbox[1] = (CheckBox) v.findViewById(R.id.checkbox_2);
        checkbox[2] = (CheckBox) v.findViewById(R.id.checkbox_3);
        checkbox[3] = (CheckBox) v.findViewById(R.id.checkbox_4);
        checkbox[4] = (CheckBox) v.findViewById(R.id.checkbox_5);
        checkbox[5] = (CheckBox) v.findViewById(R.id.checkbox_6);
        checkbox[6] = (CheckBox) v.findViewById(R.id.checkbox_7);
        checkbox[7] = (CheckBox) v.findViewById(R.id.checkbox_8);
        checkbox[8] = (CheckBox) v.findViewById(R.id.checkbox_9);
        checkbox[9] = (CheckBox) v.findViewById(R.id.checkbox_10);

        bundle = this.getArguments();
        String SurveyTitle = bundle.getString(SURVEY_10_CHOICES_TITLE, null);
        String SurveyDescription = bundle.getString(SURVEY_10_CHOICES_DESC, null);
        ArrayList<String> CbText = bundle.getStringArrayList(SURVEY_10_CHOICES_CB_TEXT);
        NextStep = bundle.getIntegerArrayList(SURVEY_10_CHOICES_ENABLE_NEXT_STEP);

        Title.setText(SurveyTitle);
        Description.setText(SurveyDescription);

        for (NbrCbVisible = 0; NbrCbVisible < CbText.size(); NbrCbVisible++)
        {
            checkbox[NbrCbVisible].setText(CbText.get(NbrCbVisible));
            checkbox[NbrCbVisible].setId(NbrCbVisible);

            checkbox[NbrCbVisible].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = ((CheckBox) view).isChecked();

                    if (SurveyStatus == -1) {
                        SurveyStatus = 0;
                    }

                    if (checked) {
                        SurveyStatus |= 1 << view.getId();
                    }
                    else {
                        SurveyStatus &= ~(1 << view.getId());
                    }

                    if (SurveyStatus == 0) {
                        SurveyStatus = -1;
                    }

                    bundle.putInt(SURVEY_STATUS, SurveyStatus);
                }
            });
        }

        while (NbrCbVisible < SURVEY_CB_MAX)
        {
            checkbox[NbrCbVisible].setVisibility(View.GONE);
            NbrCbVisible++;
        }

        if (SurveyStatus != -1) {
            checkbox[SurveyStatus].setChecked(true);
        }

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
        int NextStepValue = 0;
        if (SurveyStatus != -1) NextStepValue = NextStep.get(0);
        if ((NextStep != null) && (SurveyStatus != -1)) {
            if (NextStepValue >= 1) {
                //Check if the next step is invisible
                if (getVisibilityNextStep(NextStepValue) == false) {
                    //Make it visible
                    setVisibilityNextStep(true, NextStepValue);
                }

            } else {
                //Check if the next step is visible
                if (getVisibilityNextStep(NextStepValue) == true) {
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

