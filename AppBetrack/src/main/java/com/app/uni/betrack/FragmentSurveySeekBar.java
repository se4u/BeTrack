package com.app.uni.betrack;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.ArrayList;

/**
 * Created by cedoctet on 20/09/2016.
 */
public class FragmentSurveySeekBar extends AbstractStep {
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
    public static final String SURVEY_SEEKBAR_CHOICES_TITLE = "SURVEY_SEEKBAR_CHOICES_TITLE";
    public static final String SURVEY_SEEKBAR_CHOICES_DESC = "SURVEY_SEEKBAR_CHOICES_DESC";
    public static final String SURVEY_SEEKBAR_ANSWERS_RIGHT = "SURVEY_SEEKBAR_ANSWERS_RIGHT";
    public static final String SURVEY_SEEKBAR_ANSWERS_LEFT = "SURVEY_SEEKBAR_ANSWERS_LEFT";
    public static final String SURVEY_SEEKBAR_ENABLE_NEXT_STEP = "SURVEY_SEEKBAR_ENABLE_NEXT_STEP";
    public static final String SURVEY_SEEKBAR_ENABLE_NEXT_STEP_TRIGGER = "SURVEY_SEEKBAR_ENABLE_NEXT_STEP_TRIGGER";

    private static final int SURVEY_SEEKBAR_MAX = 4;
    private static final int SURVEY_SEEKBAR_DEFAULT_VALUE = 50;

    private ArrayList<Integer> NextStepTrigger = null;
    private ArrayList<Integer> NextStep = null;

    private TextView Title;
    private TextView Description;
    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private SeekBar seekBar3;
    private SeekBar seekBar4;

    private Bundle bundle;
    TextView[] seekBarTextRight = new TextView[SURVEY_SEEKBAR_MAX];
    TextView[] seekBarTextLeft = new TextView[SURVEY_SEEKBAR_MAX];
    TextView[] seekBarPercentage = new TextView[SURVEY_SEEKBAR_MAX];
    View[] seekBarView = new View[SURVEY_SEEKBAR_MAX];


    private static Drawable BackgroundNoSelection;
    private static Drawable BackgroundSelected;

    private static void InternalSetBackground(Drawable Background, Button button)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            button.setBackground(Background);
        }
        else
        {
            button.setBackgroundDrawable(Background);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.survey_seek_bar, container, false);
        int NbrTextVisible = 0;

        Title = (TextView) v.findViewById(R.id.survey_title);
        Description = (TextView) v.findViewById(R.id.survey_desc);
        seekBar1 = (SeekBar) v.findViewById(R.id.volume_bar1);
        seekBar2 = (SeekBar) v.findViewById(R.id.volume_bar2);
        seekBar3 = (SeekBar) v.findViewById(R.id.volume_bar3);
        seekBar4 = (SeekBar) v.findViewById(R.id.volume_bar4);

        seekBarPercentage[0] = (TextView) v.findViewById(R.id.survey_percentage1);
        seekBarPercentage[1] = (TextView) v.findViewById(R.id.survey_percentage2);
        seekBarPercentage[2] = (TextView) v.findViewById(R.id.survey_percentage3);
        seekBarPercentage[3] = (TextView) v.findViewById(R.id.survey_percentage4);

        seekBarTextRight[0] = (TextView) v.findViewById(R.id.TextInfoRight1);
        seekBarTextRight[1] = (TextView) v.findViewById(R.id.TextInfoRight2);
        seekBarTextRight[2] = (TextView) v.findViewById(R.id.TextInfoRight3);
        seekBarTextRight[3] = (TextView) v.findViewById(R.id.TextInfoRight4);

        seekBarTextLeft[0] = (TextView) v.findViewById(R.id.TextInfoLeft1);
        seekBarTextLeft[1] = (TextView) v.findViewById(R.id.TextInfoLeft2);
        seekBarTextLeft[2] = (TextView) v.findViewById(R.id.TextInfoLeft3);
        seekBarTextLeft[3] = (TextView) v.findViewById(R.id.TextInfoLeft4);

        seekBarView[0] = (View) v.findViewById(R.id.SeekBar1);
        seekBarView[1] = (View) v.findViewById(R.id.SeekBar2);
        seekBarView[2] = (View) v.findViewById(R.id.SeekBar3);
        seekBarView[3] = (View) v.findViewById(R.id.SeekBar4);

        bundle = this.getArguments();
        String SurveyTitle = bundle.getString(SURVEY_SEEKBAR_CHOICES_TITLE, null);
        String SurveyDescription = bundle.getString(SURVEY_SEEKBAR_CHOICES_DESC, null);
        ArrayList<String> SeekBarTextRight = bundle.getStringArrayList(SURVEY_SEEKBAR_ANSWERS_RIGHT);
        ArrayList<String> SeekBarTextLeft = bundle.getStringArrayList(SURVEY_SEEKBAR_ANSWERS_LEFT);
        NextStep = bundle.getIntegerArrayList(SURVEY_SEEKBAR_ENABLE_NEXT_STEP);
        NextStepTrigger = bundle.getIntegerArrayList(SURVEY_SEEKBAR_ENABLE_NEXT_STEP_TRIGGER);

        Title.setText(SurveyTitle);
        Description.setText(SurveyDescription);

        for (NbrTextVisible = 0; NbrTextVisible < SeekBarTextRight.size(); NbrTextVisible++)
        {
            seekBarTextRight[NbrTextVisible].setText(SeekBarTextRight.get(NbrTextVisible));
            if (SeekBarTextLeft != null) {
                if (NbrTextVisible < SeekBarTextLeft.size()) {
                    seekBarTextLeft[NbrTextVisible].setText(SeekBarTextLeft.get(NbrTextVisible));
                }
            }

        }

        while (NbrTextVisible < SURVEY_SEEKBAR_MAX)
        {
            seekBarView[NbrTextVisible].setVisibility(View.INVISIBLE);
            seekBarPercentage[NbrTextVisible].setVisibility(View.INVISIBLE);
            NbrTextVisible++;
        }


        if (SurveyStatus1 != -1) {
            seekBar1.setProgress(SurveyStatus1);
            seekBarPercentage[0].setText(Integer.toString(SurveyStatus1));
        } else {
            seekBar1.setProgress(SURVEY_SEEKBAR_DEFAULT_VALUE);
            SurveyStatus1 = SURVEY_SEEKBAR_DEFAULT_VALUE;
            mStepper.getExtras().putInt(SURVEY_STATUS1, SurveyStatus1);
            bundle.putInt(SURVEY_STATUS1, SurveyStatus1);
        }

        if (SurveyStatus2 != -1) {
            seekBar2.setProgress(SurveyStatus2);
            seekBarPercentage[1].setText(Integer.toString(SurveyStatus2));
        } else {
            seekBar2.setProgress(SURVEY_SEEKBAR_DEFAULT_VALUE);
            SurveyStatus2 = SURVEY_SEEKBAR_DEFAULT_VALUE;
            mStepper.getExtras().putInt(SURVEY_STATUS2, SurveyStatus2);
            bundle.putInt(SURVEY_STATUS2, SurveyStatus2);
        }

        if (SurveyStatus3 != -1) {
            seekBar3.setProgress(SurveyStatus3);
            seekBarPercentage[2].setText(Integer.toString(SurveyStatus3));
        } else {
            seekBar3.setProgress(SURVEY_SEEKBAR_DEFAULT_VALUE);
            SurveyStatus3 = SURVEY_SEEKBAR_DEFAULT_VALUE;
            mStepper.getExtras().putInt(SURVEY_STATUS3, SurveyStatus3);
            bundle.putInt(SURVEY_STATUS3, SurveyStatus3);
        }

        if (SurveyStatus4 != -1) {
            seekBar4.setProgress(SurveyStatus4);
            seekBarPercentage[3].setText(Integer.toString(SurveyStatus4));
        } else {
            seekBar4.setProgress(SURVEY_SEEKBAR_DEFAULT_VALUE);
            SurveyStatus4 = SURVEY_SEEKBAR_DEFAULT_VALUE;
            mStepper.getExtras().putInt(SURVEY_STATUS4, SurveyStatus4);
            bundle.putInt(SURVEY_STATUS4, SurveyStatus4);
        }

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SurveyStatus1 = formatValueProgress(progress);
                mStepper.getExtras().putInt(SURVEY_STATUS1, SurveyStatus1);
                seekBarPercentage[0].setText(Integer.toString(formatValueProgress(progress)));
                bundle.putInt(SURVEY_STATUS1, SurveyStatus1);
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SurveyStatus2 = formatValueProgress(progress);
                mStepper.getExtras().putInt(SURVEY_STATUS2, SurveyStatus2);
                seekBarPercentage[1].setText(Integer.toString(formatValueProgress(progress)));
                bundle.putInt(SURVEY_STATUS2, SurveyStatus2);
            }
        });

        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SurveyStatus3 = formatValueProgress(progress);
                mStepper.getExtras().putInt(SURVEY_STATUS3, SurveyStatus3);
                seekBarPercentage[2].setText(Integer.toString(formatValueProgress(progress)));
                bundle.putInt(SURVEY_STATUS3, SurveyStatus3);
            }
        });

        seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SurveyStatus4 = formatValueProgress(progress);
                mStepper.getExtras().putInt(SURVEY_STATUS4, SurveyStatus4);
                seekBarPercentage[3].setText(Integer.toString(formatValueProgress(progress)));
                bundle.putInt(SURVEY_STATUS4, SurveyStatus4);
            }
        });

        return v;
    }


    private int formatValueProgress(int value)
    {
        int newValue = 0;
        if ((value >= 0) && (value <= 5)) {
            newValue = 0;
        } else if ((value > 5) && (value <= 15)) {
            newValue = 10;
        } else if ((value > 15) && (value <= 25)) {
            newValue = 20;
        } else if ((value > 25) && (value <= 35)) {
            newValue = 30;
        } else if ((value > 35) && (value <= 45)) {
            newValue = 40;
        } else if ((value > 45) && (value <= 55)) {
            newValue = 50;
        } else if ((value > 55) && (value <= 65)) {
            newValue = 60;
        } else if ((value > 65) && (value <= 75)) {
            newValue = 70;
        } else if ((value > 75) && (value <= 85)) {
            newValue = 80;
        } else if ((value > 85) && (value <= 95)) {
            newValue = 90;
        } else if ((value > 95) && (value <= 100)) {
            newValue = 100;
        }

        return newValue;
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
