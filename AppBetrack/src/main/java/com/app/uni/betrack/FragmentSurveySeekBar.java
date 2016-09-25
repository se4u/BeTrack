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

/**
 * Created by cedoctet on 20/09/2016.
 */
public class FragmentSurveySeekBar extends AbstractStep {
    //Output
    public  static final String SURVEY_STATUS = "SURVEY_STATUS";
    public int SurveyStatus = -1;

    //Input
    public static final String SURVEY_SEEKBAR_CHOICES_TITLE = "SURVEY_SEEKBAR_CHOICES_TITLE";
    public static final String SURVEY_SEEKBAR_CHOICES_DESC = "SURVEY_SEEKBAR_CHOICES_DESC";

    private TextView Title;
    private TextView Description;
    private SeekBar seekBar1;

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

        Title = (TextView) v.findViewById(R.id.survey_title);
        Description = (TextView) v.findViewById(R.id.survey_desc);
        seekBar1 = (SeekBar) v.findViewById(R.id.volume_bar);

        final Bundle bundle = this.getArguments();
        String SurveyTitle = bundle.getString(SURVEY_SEEKBAR_CHOICES_TITLE, null);
        String SurveyDescription = bundle.getString(SURVEY_SEEKBAR_CHOICES_DESC, null);
        Title.setText(SurveyTitle);
        Description.setText(SurveyDescription);

        if (savedInstanceState != null)
            SurveyStatus = savedInstanceState.getInt(SURVEY_STATUS, -1);

        if (SurveyStatus != -1) {
            seekBar1.setProgress(SurveyStatus);
        } else {
            seekBar1.setProgress(50);
            SurveyStatus = 50;
            mStepper.getExtras().putInt(SURVEY_STATUS, SurveyStatus);
            bundle.putInt(SURVEY_STATUS, SurveyStatus);
        }

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SurveyStatus = progress;
                mStepper.getExtras().putInt(SURVEY_STATUS, SurveyStatus);
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
        return true;
    }


    @Override
    public void onStepVisible() {
    }

    @Override
    public void onNext() {
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
