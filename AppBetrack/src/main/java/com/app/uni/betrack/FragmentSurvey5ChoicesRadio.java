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
 * Created by cvincent on 18/10/17.
 */

public class FragmentSurvey5ChoicesRadio extends AbstractStep {
    //Output
    public  static final String SURVEY_STATUS1 = "SURVEY_STATUS1";
    public int SurveyStatus1 = -1;
    public  static final String SURVEY_STATUS2 = "SURVEY_STATUS2";
    public int SurveyStatus2 = -1;
    public  static final String SURVEY_STATUS3 = "SURVEY_STATUS3";
    public int SurveyStatus3 = -1;
    public  static final String SURVEY_STATUS4 = "SURVEY_STATUS4";
    public int SurveyStatus4 = -1;

    //Input
    public static final String SURVEY_5_CHOICES_TITLE = "SURVEY_5_CHOICES_TITLE";
    public static final String SURVEY_5_CHOICES_DESC = "SURVEY_5_CHOICES_DESC";

    public static final String SURVEY_5_CHOICES_RB_ANSWERS_RIGHT = "SURVEY_5_CHOICES_RB_ANSWERS_RIGHT";
    public static final String SURVEY_5_CHOICES_RB_ANSWERS_LEFT = "SURVEY_5_CHOICES_RB_ANSWERS_LEFT";

    public static final String SURVEY_5_CHOICES_ENABLE_NEXT_STEP = "SURVEY_5_CHOICES_ENABLE_NEXT_STEP";
    public static final String SURVEY_5_CHOICES_ENABLE_NEXT_STEP_TRIGGER = "SURVEY_5_CHOICES_ENABLE_NEXT_STEP_TRIGGER";

    private static final int SURVEY_5_CHOICES_MAX = 4;
    private static final int SURVEY_RB_MAX = 5;


    private ArrayList<Integer> NextStepTrigger = null;
    private ArrayList<Integer> NextStep = null;

    private TextView Title;
    private TextView Description;
    private RadioButton[] radioButton1 = new RadioButton[SURVEY_RB_MAX];
    private RadioButton[] radioButton2 = new RadioButton[SURVEY_RB_MAX];
    private RadioButton[] radioButton3 = new RadioButton[SURVEY_RB_MAX];
    private RadioButton[] radioButton4 = new RadioButton[SURVEY_RB_MAX];

    private Bundle bundle;
    TextView[] radioButtonTextRight = new TextView[SURVEY_5_CHOICES_MAX];
    TextView[] radioButtonTextLeft = new TextView[SURVEY_5_CHOICES_MAX];
    View[] radioButtonView = new View[SURVEY_5_CHOICES_MAX];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.survey_5choicesradio, container, false);
        int NbrTextVisible = 0;

        Title = (TextView) v.findViewById(R.id.survey_title);
        Description = (TextView) v.findViewById(R.id.survey_desc);
        radioButton1[0] = (RadioButton) v.findViewById(R.id.radio1_1);
        radioButton1[1] = (RadioButton) v.findViewById(R.id.radio1_2);
        radioButton1[2] = (RadioButton) v.findViewById(R.id.radio1_3);
        radioButton1[3] = (RadioButton) v.findViewById(R.id.radio1_4);
        radioButton1[4] = (RadioButton) v.findViewById(R.id.radio1_5);

        radioButton2[0] = (RadioButton) v.findViewById(R.id.radio2_1);
        radioButton2[1] = (RadioButton) v.findViewById(R.id.radio2_2);
        radioButton2[2] = (RadioButton) v.findViewById(R.id.radio2_3);
        radioButton2[3] = (RadioButton) v.findViewById(R.id.radio2_4);
        radioButton2[4] = (RadioButton) v.findViewById(R.id.radio2_5);

        radioButton3[0] = (RadioButton) v.findViewById(R.id.radio3_1);
        radioButton3[1] = (RadioButton) v.findViewById(R.id.radio3_2);
        radioButton3[2] = (RadioButton) v.findViewById(R.id.radio3_3);
        radioButton3[3] = (RadioButton) v.findViewById(R.id.radio3_4);
        radioButton3[4] = (RadioButton) v.findViewById(R.id.radio3_5);

        radioButton4[0] = (RadioButton) v.findViewById(R.id.radio4_1);
        radioButton4[1] = (RadioButton) v.findViewById(R.id.radio4_2);
        radioButton4[2] = (RadioButton) v.findViewById(R.id.radio4_3);
        radioButton4[3] = (RadioButton) v.findViewById(R.id.radio4_4);
        radioButton4[4] = (RadioButton) v.findViewById(R.id.radio4_5);

        radioButtonTextRight[0] = (TextView) v.findViewById(R.id.TextInfoRight1);
        radioButtonTextRight[1] = (TextView) v.findViewById(R.id.TextInfoRight2);
        radioButtonTextRight[2] = (TextView) v.findViewById(R.id.TextInfoRight3);
        radioButtonTextRight[3] = (TextView) v.findViewById(R.id.TextInfoRight4);

        radioButtonTextLeft[0] = (TextView) v.findViewById(R.id.TextInfoLeft1);
        radioButtonTextLeft[1] = (TextView) v.findViewById(R.id.TextInfoLeft2);
        radioButtonTextLeft[2] = (TextView) v.findViewById(R.id.TextInfoLeft3);
        radioButtonTextLeft[3] = (TextView) v.findViewById(R.id.TextInfoLeft4);

        radioButtonView[0] = (View) v.findViewById(R.id.ChoicesRadio1);
        radioButtonView[1] = (View) v.findViewById(R.id.ChoicesRadio2);
        radioButtonView[2] = (View) v.findViewById(R.id.ChoicesRadio3);
        radioButtonView[3] = (View) v.findViewById(R.id.ChoicesRadio4);

        bundle = this.getArguments();
        String SurveyTitle = bundle.getString(SURVEY_5_CHOICES_TITLE, null);
        String SurveyDescription = bundle.getString(SURVEY_5_CHOICES_DESC, null);
        ArrayList<String> RadioBtn7TextRight = bundle.getStringArrayList(SURVEY_5_CHOICES_RB_ANSWERS_RIGHT);
        ArrayList<String> RadioBtn7TextLeft = bundle.getStringArrayList(SURVEY_5_CHOICES_RB_ANSWERS_LEFT);
        NextStep = bundle.getIntegerArrayList(SURVEY_5_CHOICES_ENABLE_NEXT_STEP);
        NextStepTrigger = bundle.getIntegerArrayList(SURVEY_5_CHOICES_ENABLE_NEXT_STEP_TRIGGER);

        Title.setText(SurveyTitle);
        Description.setText(SurveyDescription);

        for (NbrTextVisible = 0; NbrTextVisible < RadioBtn7TextRight.size(); NbrTextVisible++)
        {
            radioButtonTextRight[NbrTextVisible].setText(RadioBtn7TextRight.get(NbrTextVisible));
            if (RadioBtn7TextLeft != null) {
                if (NbrTextVisible < RadioBtn7TextLeft.size()) {
                    radioButtonTextLeft[NbrTextVisible].setText(RadioBtn7TextLeft.get(NbrTextVisible));
                }
            }

        }

        while (NbrTextVisible < SURVEY_5_CHOICES_MAX)
        {
            radioButtonView[NbrTextVisible].setVisibility(View.INVISIBLE);
            NbrTextVisible++;
        }


        if (SurveyStatus1 != -1) {
            radioButton1[SurveyStatus1 - 1].setChecked(true);
        }

        if (SurveyStatus2 != -1) {
            radioButton2[SurveyStatus2 - 1].setChecked(true);
        }

        if (SurveyStatus3 != -1) {
            radioButton3[SurveyStatus3 - 1].setChecked(true);
        }

        if (SurveyStatus4 != -1) {
            radioButton4[SurveyStatus4 - 1].setChecked(true);
        }

        RadioGroup radioGroup1;
        radioGroup1 = (RadioGroup) v.findViewById(R.id.radio_group1);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId)
                {
                    case R.id.radio1_1:
                        SurveyStatus1 = 1;
                        break;
                    case R.id.radio1_2:
                        SurveyStatus1 = 2;
                        break;
                    case R.id.radio1_3:
                        SurveyStatus1 = 3;
                        break;
                    case R.id.radio1_4:
                        SurveyStatus1 = 4;
                        break;
                    case R.id.radio1_5:
                        SurveyStatus1 = 5;
                        break;
                }

                bundle.putInt(SURVEY_STATUS1, SurveyStatus1);
            }
        });

        RadioGroup radioGroup2;
        radioGroup2 = (RadioGroup) v.findViewById(R.id.radio_group2);
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId)
                {
                    case R.id.radio2_1:
                        SurveyStatus2 = 1;
                        break;
                    case R.id.radio2_2:
                        SurveyStatus2 = 2;
                        break;
                    case R.id.radio2_3:
                        SurveyStatus2 = 3;
                        break;
                    case R.id.radio2_4:
                        SurveyStatus2 = 4;
                        break;
                    case R.id.radio2_5:
                        SurveyStatus2 = 5;
                        break;
                }

                bundle.putInt(SURVEY_STATUS2, SurveyStatus2);
            }
        });

        RadioGroup radioGroup3;
        radioGroup3 = (RadioGroup) v.findViewById(R.id.radio_group3);
        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId)
                {
                    case R.id.radio3_1:
                        SurveyStatus3 = 1;
                        break;
                    case R.id.radio3_2:
                        SurveyStatus3 = 2;
                        break;
                    case R.id.radio3_3:
                        SurveyStatus3 = 3;
                        break;
                    case R.id.radio3_4:
                        SurveyStatus3 = 4;
                        break;
                    case R.id.radio3_5:
                        SurveyStatus3 = 5;
                        break;
                }

                bundle.putInt(SURVEY_STATUS3, SurveyStatus3);
            }
        });

        RadioGroup radioGroup4;
        radioGroup4 = (RadioGroup) v.findViewById(R.id.radio_group4);
        radioGroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId)
                {
                    case R.id.radio4_1:
                        SurveyStatus4 = 1;
                        break;
                    case R.id.radio4_2:
                        SurveyStatus4 = 2;
                        break;
                    case R.id.radio4_3:
                        SurveyStatus4 = 3;
                        break;
                    case R.id.radio4_4:
                        SurveyStatus4 = 4;
                        break;
                    case R.id.radio4_5:
                        SurveyStatus4 = 5;
                        break;
                }

                bundle.putInt(SURVEY_STATUS4, SurveyStatus4);
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt(SURVEY_STATUS1, SurveyStatus1);
        state.putInt(SURVEY_STATUS2, SurveyStatus2);
        state.putInt(SURVEY_STATUS3, SurveyStatus3);
        state.putInt(SURVEY_STATUS4, SurveyStatus4);
    }

    @Override
    public String name() {
        return "Tab " + getArguments().getInt(SURVEY_STATUS1, 0);
    }

    @Override
    public boolean isOptional() {
        return true;
    }


    @Override
    public void onStepVisible() {
    }

    @Override
    public void onNext() {
        System.out.println("onNext");
        if (NextStep != null) {
            if ((SurveyStatus1 > NextStepTrigger.get(0)) &&
                    (SurveyStatus2 > NextStepTrigger.get(1)) &&
                    (SurveyStatus3 > NextStepTrigger.get(2)) &&
                    (SurveyStatus4 > NextStepTrigger.get(3)))
            {
                //Make it visible
                setVisibilityNextStep(true, NextStep.get(0));
            } else {
                //Check if the next step is visible
                if (getVisibilityNextStep(NextStep.get(0)) == true) {
                    //Make it invisible
                    setVisibilityNextStep(false, 1);
                }
            }
        } else {
            setVisibilityNextStep(true, 1);
        }

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
        return true;
    }

    @Override
    public String error() {
        return getResources().getString(R.string.question_error);
    }


}
