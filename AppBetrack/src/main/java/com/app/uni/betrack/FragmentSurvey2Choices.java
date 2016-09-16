package com.app.uni.betrack;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;

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


    private Button button1;
    private Button button2;
    private ImageView imgbutton1;
    private ImageView imgbutton2;
    private TextView Title;
    private TextView Description;

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

        View v = inflater.inflate(R.layout.survey_2choices, container, false);
        button1 = (Button) v.findViewById(R.id.ButtonChoice1);
        button2 = (Button) v.findViewById(R.id.ButtonChoice2);

        imgbutton1 = (ImageView) v.findViewById(R.id.ImgButtonChoice1);
        imgbutton2 = (ImageView) v.findViewById(R.id.ImgButtonChoice2);

        Title = (TextView) v.findViewById(R.id.survey_title);
        Description = (TextView) v.findViewById(R.id.survey_desc);

        final Bundle bundle = this.getArguments();
        String SurveyTitle = bundle.getString(SURVEY_2_CHOICES_TITLE, null);
        String SurveyDescription = bundle.getString(SURVEY_2_CHOICES_DESC, null);
        Title.setText(SurveyTitle);
        Description.setText(SurveyDescription);

        if (savedInstanceState != null)
            SurveyStatus = savedInstanceState.getInt(SURVEY_STATUS, -1);

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
            imgbutton1.setImageResource(R.drawable.ic_action_ok_selected);
            imgbutton2.setImageResource(R.drawable.ic_action_ko);
        } else if (SurveyStatus == 0) {
            InternalSetBackground(BackgroundNoSelection, button1);
            InternalSetBackground(BackgroundSelected, button2);
            imgbutton1.setImageResource(R.drawable.ic_action_ok);
            imgbutton2.setImageResource(R.drawable.ic_action_ko_selected);
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InternalSetBackground(BackgroundSelected, button1);
                InternalSetBackground(BackgroundNoSelection, button2);
                imgbutton1.setImageResource(R.drawable.ic_action_ok_selected);
                imgbutton2.setImageResource(R.drawable.ic_action_ko);
                SurveyStatus = 1;
                mStepper.getExtras().putInt(SURVEY_STATUS, SurveyStatus);
                bundle.putInt(SURVEY_STATUS, SurveyStatus);
                setArguments(bundle);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InternalSetBackground(BackgroundNoSelection, button1);
                InternalSetBackground(BackgroundSelected, button2);
                imgbutton1.setImageResource(R.drawable.ic_action_ok);
                imgbutton2.setImageResource(R.drawable.ic_action_ko_selected);
                SurveyStatus = 0;
                mStepper.getExtras().putInt(SURVEY_STATUS, SurveyStatus);
                bundle.putInt(SURVEY_STATUS, SurveyStatus);
                setArguments(bundle);
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
        return SurveyStatus > 0;
    }

    @Override
    public String error() {
        return getResources().getString(R.string.question_error);
    }


}


