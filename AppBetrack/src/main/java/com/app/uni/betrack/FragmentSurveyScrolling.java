package com.app.uni.betrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;

/**
 * Created by cedoctet on 12/09/2016.
 */
public class FragmentSurveyScrolling extends AbstractStep {
    private TextView Title;
    private TextView Description;
    private NumberPicker pickers;
    private Bundle bundle;
    private int SurveyStartRange;


    //Output
    public  static final String SURVEY_STATUS = "SURVEY_STATUS";
    public int SurveyStatus = -1;

    //Input
    public static final String SURVEY_SCROLLING_TITLE = "SURVEY_SCROLLING_TITLE";
    public static final String SURVEY_SCROLLING_DESC = "SURVEY_SCROLLING_DESC";
    public static final String SURVEY_SCROLLING_UNIT = "SURVEY_SCROLLING_UNIT";
    public static final String SURVEY_SCROLLING_START_RANGE = "SURVEY_SCROLLING_START_RANGE";
    public static final String SURVEY_SCROLLING_END_RANGE = "SURVEY_SCROLLING_END_RANGE";
    public static final String SURVEY_SCROLLING_DEFAULT_VALUE = "SURVEY_SCROLLING_DEFAULT_VALUE";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.survey_scrolling, container, false);

        bundle = this.getArguments();
        String SurveyTitle = bundle.getString(SURVEY_SCROLLING_TITLE, null);
        String SurveyDescription = bundle.getString(SURVEY_SCROLLING_DESC, null);
        String SurveyUnit = bundle.getString(SURVEY_SCROLLING_UNIT, null);
        SurveyStartRange = bundle.getInt(SURVEY_SCROLLING_START_RANGE, 0);
        int SurveyEndRange = bundle.getInt(SURVEY_SCROLLING_END_RANGE, 0);
        int SurveyDefaultValue = bundle.getInt(SURVEY_SCROLLING_DEFAULT_VALUE, 0);

        String[] arrayPicker= new String[SurveyEndRange-SurveyStartRange];
        for(int i=SurveyStartRange;i<SurveyEndRange;i++) {
            arrayPicker[i-SurveyStartRange] = i + " " +  SurveyUnit;
        }

        Title = (TextView) v.findViewById(R.id.survey_title);
        Description = (TextView) v.findViewById(R.id.survey_desc);
        Title.setText(SurveyTitle);
        Description.setText(SurveyDescription);

        pickers = (NumberPicker) v.findViewById(R.id.genericPicker);

        //set min value zero
        pickers.setMinValue(0);
        //set max value from length array string reduced 1
        pickers.setMaxValue(arrayPicker.length - 1);
        //implement array string to number picker
        pickers.setDisplayedValues(arrayPicker);
        //disable soft keyboard
        pickers.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        //set wrap true or false, try it you will know the difference
        pickers.setWrapSelectorWheel(false);
        if (SurveyStatus == -1) {
            pickers.setValue(SurveyDefaultValue);
        } else {
            pickers.setValue(SurveyStatus-SurveyStartRange);
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
        return true;
    }


    @Override
    public void onStepVisible() {
    }

    @Override
    public void onNext() {
        int pos = pickers.getValue();
        SurveyStatus = SurveyStartRange + pos;
        mStepper.getExtras().putInt(SURVEY_STATUS, SurveyStatus);
        bundle.putInt(SURVEY_STATUS, SurveyStatus);
        System.out.println("onNext");
        setVisibilityNextStep(true, 1);
    }

    @Override
    public void onPrevious() {
        int pos = pickers.getValue();
        SurveyStatus = SurveyStartRange + pos;
        mStepper.getExtras().putInt(SURVEY_STATUS, SurveyStatus);
        bundle.putInt(SURVEY_STATUS, SurveyStatus);
        System.out.println("onPrevious");
    }

    @Override
    public String optional() {
        return "You can skip";
    }

    @Override
    public boolean nextIf() {
        return SurveyStatus > 1;
    }

    @Override
    public String error() {
        return "<b>You must click!</b> <small>this is the condition!</small>";
    }
}
