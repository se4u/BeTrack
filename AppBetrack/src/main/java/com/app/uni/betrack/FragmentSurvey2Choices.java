package com.app.uni.betrack;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.ArrayList;

/**
 * Created by cedoctet on 12/09/2016.
 */
public class FragmentSurvey2Choices extends AbstractStep {

    //Output
    public  static final String SURVEY_STATUS = "SURVEY_STATUS";
    public int SurveyStatus = -1;

    //Input
    public static final String SURVEY_2_CHOICES_TITLE = "SURVEY_2_CHOICES_TITLE";
    public static final String SURVEY_2_CHOICES_DESC = "SURVEY_2_CHOICES_DESC";
    public static final String SURVEY_2_CHOICES_ENABLE_NEXT_STEP = "SURVEY_2_CHOICES_ENABLE_NEXT_STEP";
    public static final String SURVEY_2_CHOICES_ENABLE_AUTOMOVE = "SURVEY_2_CHOICES_ENABLE_AUTOMOVE";

    private Bundle bundle;
    private Button button1;
    private Button button2;
    private ImageView imgbutton1;
    private ImageView imgbutton2;
    private TextView Title;
    private TextView Description;
    private ArrayList<Integer> NextStep;
    boolean AutoMove=true;

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
        int tintPrimary = Color.parseColor(SettingsBetrack.colorPrimary);
        int tintGray = Color.parseColor(SettingsBetrack.colorDarkGrey);
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;
        View v = inflater.inflate(R.layout.survey_2choices, container, false);
        button1 = (Button) v.findViewById(R.id.ButtonChoice1);
        button2 = (Button) v.findViewById(R.id.ButtonChoice2);

        imgbutton1 = (ImageView) v.findViewById(R.id.ImgButtonChoice1);
        imgbutton2 = (ImageView) v.findViewById(R.id.ImgButtonChoice2);

        Title = (TextView) v.findViewById(R.id.survey_title);
        Description = (TextView) v.findViewById(R.id.survey_desc);

        bundle = this.getArguments();
        String SurveyTitle = bundle.getString(SURVEY_2_CHOICES_TITLE, null);
        String SurveyDescription = bundle.getString(SURVEY_2_CHOICES_DESC, null);
        NextStep = bundle.getIntegerArrayList(SURVEY_2_CHOICES_ENABLE_NEXT_STEP);
        AutoMove = bundle.getBoolean(SURVEY_2_CHOICES_ENABLE_AUTOMOVE, true);

        Title.setText(SurveyTitle);
        Description.setText(SurveyDescription);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BackgroundNoSelection = (Drawable)getResources().getDrawable(R.drawable.button_round_noselection, getContext().getTheme());
            BackgroundSelected = (Drawable)getResources().getDrawable(R.drawable.button_round_selection, getContext().getTheme());
        } else {
            BackgroundNoSelection = (Drawable)getResources().getDrawable(R.drawable.button_round_noselection);
            BackgroundSelected = (Drawable)getResources().getDrawable(R.drawable.button_round_selection);
        }

        if (SurveyStatus == 1) {
            InternalSetBackground(BackgroundSelected, button1);
            InternalSetBackground(BackgroundNoSelection, button2);
            imgbutton1.setColorFilter(tintPrimary, mode);
            imgbutton2.setColorFilter(tintGray, mode);
        } else if (SurveyStatus == 0) {
            InternalSetBackground(BackgroundNoSelection, button1);
            InternalSetBackground(BackgroundSelected, button2);
            imgbutton1.setColorFilter(tintGray, mode);
            imgbutton2.setColorFilter(tintPrimary, mode);
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tintPrimary = Color.parseColor(SettingsBetrack.colorPrimary);
                int tintGray = Color.parseColor(SettingsBetrack.colorDarkGrey);
                PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;


                InternalSetBackground(BackgroundSelected, button1);
                InternalSetBackground(BackgroundNoSelection, button2);
                imgbutton1.setColorFilter(tintPrimary, mode);
                imgbutton2.setColorFilter(tintGray, mode);

                if ((SurveyStatus == -1) && (AutoMove == true)) {
                    SurveyStatus = 1;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            moveToNext();
                        }
                    }, 500);
                }
                SurveyStatus = 1;
                mStepper.getExtras().putInt(SURVEY_STATUS, SurveyStatus);
                bundle.putInt(SURVEY_STATUS, SurveyStatus);

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tintPrimary = Color.parseColor(SettingsBetrack.colorPrimary);
                int tintGray = Color.parseColor(SettingsBetrack.colorDarkGrey);
                PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;

                InternalSetBackground(BackgroundNoSelection, button1);
                InternalSetBackground(BackgroundSelected, button2);
                imgbutton1.setColorFilter(tintGray, mode);
                imgbutton2.setColorFilter(tintPrimary, mode);

                if ((SurveyStatus == -1) && (AutoMove == true)) {
                    SurveyStatus = 0;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            moveToNext();
                        }
                    }, 1);
                }
                SurveyStatus = 0;
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
        return false;
    }


    @Override
    public void onStepVisible() {
    }

    @Override
    public void onNext() {
        System.out.println("onNext");
        if ((NextStep != null) && (SurveyStatus != -1)) {
            if (SurveyStatus == 1) {
                if (NextStep.get(0) >= 1) {
                    //Check if the next step is invisible
                    if (getVisibilityNextStep(NextStep.get(0)) == false) {
                        //Make it visible
                        setVisibilityNextStep(true, NextStep.get(0));
                    }

                } else {
                    //Check if the next step is visible
                    if (getVisibilityNextStep(NextStep.get(0)) == true) {
                        //Make it visible
                        setVisibilityNextStep(false, 1);
                    }
                }
            } else {
                if (NextStep.get(1) >= 0) {
                    //Check if the next step is invisible
                    if (getVisibilityNextStep(NextStep.get(1)) == false) {
                        //Make it visible
                        setVisibilityNextStep(true, NextStep.get(1));
                    }

                } else {
                    //Check if the next step is visible
                    if (getVisibilityNextStep(NextStep.get(1)) == true) {
                        //Make it visible
                        setVisibilityNextStep(false, 1);
                    }
                }
            }
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
        return SurveyStatus > -1;
    }

    @Override
    public String error() {
        return getResources().getString(R.string.question_error);
    }


}


