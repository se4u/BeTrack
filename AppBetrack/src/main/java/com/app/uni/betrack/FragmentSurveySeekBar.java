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
    public static final String SURVEY_SEEKBAR_ANSWERS = "SURVEY_SEEKBAR_ANSWERS";

    private static final int SURVEY_SEEKBAR_MAX = 4;

    private TextView Title;
    private TextView Description;
    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private SeekBar seekBar3;
    private SeekBar seekBar4;

    private Bundle bundle;
    TextView[] seekBarText = new TextView[SURVEY_SEEKBAR_MAX];
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

        seekBarText[0] = (TextView) v.findViewById(R.id.TextInfoRight1);
        seekBarText[1] = (TextView) v.findViewById(R.id.TextInfoRight2);
        seekBarText[2] = (TextView) v.findViewById(R.id.TextInfoRight3);
        seekBarText[3] = (TextView) v.findViewById(R.id.TextInfoRight4);

        seekBarView[0] = (View) v.findViewById(R.id.SeekBar1);
        seekBarView[1] = (View) v.findViewById(R.id.SeekBar2);
        seekBarView[2] = (View) v.findViewById(R.id.SeekBar3);
        seekBarView[3] = (View) v.findViewById(R.id.SeekBar4);

        bundle = this.getArguments();
        String SurveyTitle = bundle.getString(SURVEY_SEEKBAR_CHOICES_TITLE, null);
        String SurveyDescription = bundle.getString(SURVEY_SEEKBAR_CHOICES_DESC, null);
        ArrayList<String> SeekBarText = bundle.getStringArrayList(SURVEY_SEEKBAR_ANSWERS);

        Title.setText(SurveyTitle);
        Description.setText(SurveyDescription);

        for (NbrTextVisible = 0; NbrTextVisible < SeekBarText.size(); NbrTextVisible++)
        {
            seekBarText[NbrTextVisible].setText(SeekBarText.get(NbrTextVisible));
        }

        while (NbrTextVisible < SURVEY_SEEKBAR_MAX)
        {
            seekBarView[NbrTextVisible].setVisibility(View.INVISIBLE);
            NbrTextVisible++;
        }


        if (SurveyStatus1 != -1) {
            seekBar1.setProgress(SurveyStatus1);
        } else {
            seekBar1.setProgress(50);
            SurveyStatus1 = 50;
            mStepper.getExtras().putInt(SURVEY_STATUS1, SurveyStatus1);
            bundle.putInt(SURVEY_STATUS1, SurveyStatus1);
        }

        if (SurveyStatus2 != -1) {
            seekBar2.setProgress(SurveyStatus2);
        } else {
            seekBar2.setProgress(50);
            SurveyStatus2 = 50;
            mStepper.getExtras().putInt(SURVEY_STATUS2, SurveyStatus2);
            bundle.putInt(SURVEY_STATUS2, SurveyStatus2);
        }

        if (SurveyStatus3 != -1) {
            seekBar3.setProgress(SurveyStatus3);
        } else {
            seekBar3.setProgress(50);
            SurveyStatus3 = 50;
            mStepper.getExtras().putInt(SURVEY_STATUS3, SurveyStatus3);
            bundle.putInt(SURVEY_STATUS3, SurveyStatus3);
        }

        if (SurveyStatus4 != -1) {
            seekBar4.setProgress(SurveyStatus4);
        } else {
            seekBar4.setProgress(50);
            SurveyStatus4 = 50;
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
                SurveyStatus1 = progress;
                mStepper.getExtras().putInt(SURVEY_STATUS1, SurveyStatus1);
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
                SurveyStatus2 = progress;
                mStepper.getExtras().putInt(SURVEY_STATUS2, SurveyStatus2);
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
                SurveyStatus3 = progress;
                mStepper.getExtras().putInt(SURVEY_STATUS3, SurveyStatus3);
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
                SurveyStatus4 = progress;
                mStepper.getExtras().putInt(SURVEY_STATUS4, SurveyStatus4);
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
        setVisibilityNextStep(true, 1);
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
