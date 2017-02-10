package com.app.uni.betrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;

/**
 * Created by cedoctet on 12/09/2016.
 */
public class FragmentSurveyScrolling extends AbstractStep {
    private TextView Title;
    private TextView Description;
    private NumberPicker pickers1;
    private NumberPicker pickers2;
    private Bundle bundle;
    private int SurveyStartRange1;
    private int SurveyStartRange2;
    private int SurveyEndRange2;


    //Output
    public  static final String SURVEY_STATUS1 = "SURVEY_STATUS1";
    public  static final String SURVEY_STATUS2 = "SURVEY_STATUS2";

    private int SurveyStatus1 = -1;
    private String SurveyPreUnit1 = null;
    private String SurveyPostUnit1 = null;
    private int OffsetSurveyMin1;

    private int SurveyStatus2 = -1;
    private String SurveyPreUnit2 = null;
    private String SurveyPostUnit2 = null;
    private int OffsetSurveyMin2;

    //Input
    public static final String SURVEY_SCROLLING_TITLE = "SURVEY_SCROLLING_TITLE";
    public static final String SURVEY_SCROLLING_DESC = "SURVEY_SCROLLING_DESC";

    public static final String SURVEY_SCROLLING_PRE_UNIT_1 = "SURVEY_SCROLLING_PRE_UNIT_1";
    public static final String SURVEY_SCROLLING_POST_UNIT_1 = "SURVEY_SCROLLING_POST_UNIT_1";
    public static final String SURVEY_SCROLLING_START_RANGE_1 = "SURVEY_SCROLLING_START_RANGE_1";
    public static final String SURVEY_SCROLLING_END_RANGE_1 = "SURVEY_SCROLLING_END_RANGE_1";
    public static final String SURVEY_SCROLLING_MIN_TXT_VALUE_1 = "SURVEY_SCROLLING_MIN_TXT_VALUE_1";
    public static final String SURVEY_SCROLLING_MAX_TXT_VALUE_1 = "SURVEY_SCROLLING_MAX_TXT_VALUE_1";
    public static final String SURVEY_SCROLLING_DEFAULT_VALUE_1 = "SURVEY_SCROLLING_DEFAULT_VALUE_1";

    public static final String SURVEY_SCROLLING_PRE_UNIT_2 = "SURVEY_SCROLLING_PRE_UNIT_2";
    public static final String SURVEY_SCROLLING_POST_UNIT_2 = "SURVEY_SCROLLING_POST_UNIT_2";
    public static final String SURVEY_SCROLLING_START_RANGE_2 = "SURVEY_SCROLLING_START_RANGE_2";
    public static final String SURVEY_SCROLLING_END_RANGE_2 = "SURVEY_SCROLLING_END_RANGE_2";
    public static final String SURVEY_SCROLLING_MIN_TXT_VALUE_2 = "SURVEY_SCROLLING_MIN_TXT_VALUE_2";
    public static final String SURVEY_SCROLLING_MAX_TXT_VALUE_2 = "SURVEY_SCROLLING_MAX_TXT_VALUE_2";
    public static final String SURVEY_SCROLLING_DEFAULT_VALUE_2 = "SURVEY_SCROLLING_DEFAULT_VALUE_2";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.survey_scrolling, container, false);

        bundle = this.getArguments();
        String SurveyTitle = bundle.getString(SURVEY_SCROLLING_TITLE, null);
        String SurveyDescription = bundle.getString(SURVEY_SCROLLING_DESC, null);

        SurveyPreUnit1 = bundle.getString(SURVEY_SCROLLING_PRE_UNIT_1, "");
        SurveyPostUnit1 = bundle.getString(SURVEY_SCROLLING_POST_UNIT_1, "");
        SurveyStartRange1 = bundle.getInt(SURVEY_SCROLLING_START_RANGE_1, 0);
        int SurveyEndRange1 = bundle.getInt(SURVEY_SCROLLING_END_RANGE_1, 0);
        String SurveyMinTxt1 = bundle.getString(SURVEY_SCROLLING_MIN_TXT_VALUE_1, null);
        String SurveyMaxTxt1 = bundle.getString(SURVEY_SCROLLING_MAX_TXT_VALUE_1, null);
        int SurveyDefaultValue1 = bundle.getInt(SURVEY_SCROLLING_DEFAULT_VALUE_1, 0);
        OffsetSurveyMin1 = 0;
        int AdditionnalSpace1 = 0;
        if (SurveyMinTxt1 != null) {AdditionnalSpace1++;}
        if (SurveyMaxTxt1 != null) {AdditionnalSpace1++;}

        SurveyPreUnit2 = bundle.getString(SURVEY_SCROLLING_PRE_UNIT_2, "");
        SurveyPostUnit2 = bundle.getString(SURVEY_SCROLLING_POST_UNIT_2, "");
        SurveyStartRange2 = bundle.getInt(SURVEY_SCROLLING_START_RANGE_2, 0);
        SurveyEndRange2 = bundle.getInt(SURVEY_SCROLLING_END_RANGE_2, 0);
        String SurveyMinTxt2 = bundle.getString(SURVEY_SCROLLING_MIN_TXT_VALUE_2, null);
        String SurveyMaxTxt2 = bundle.getString(SURVEY_SCROLLING_MAX_TXT_VALUE_2, null);
        int SurveyDefaultValue2 = bundle.getInt(SURVEY_SCROLLING_DEFAULT_VALUE_2, 0);
        OffsetSurveyMin2 = 0;
        int AdditionnalSpace2 = 0;
        if (SurveyMinTxt2 != null) {AdditionnalSpace2++;}
        if (SurveyMaxTxt2 != null) {AdditionnalSpace2++;}

        String[] arrayPicker1= new String[SurveyEndRange1-SurveyStartRange1 + AdditionnalSpace1];

        if (SurveyMinTxt1 != null) {
            OffsetSurveyMin1 = 1;
            arrayPicker1[0] = SurveyMinTxt1;
        }

        for(int i=SurveyStartRange1;i<SurveyEndRange1;i++) {
            arrayPicker1[i-SurveyStartRange1+OffsetSurveyMin1] = SurveyPreUnit1 + i + SurveyPostUnit1;
        }

        if (SurveyMaxTxt1 != null) {
            arrayPicker1[SurveyEndRange1-SurveyStartRange1+OffsetSurveyMin1] = SurveyMaxTxt1;
        }

        String[] arrayPicker2= new String[SurveyEndRange2-SurveyStartRange2 + AdditionnalSpace2];

        if (SurveyEndRange2 != SurveyStartRange2) {
            if (SurveyMinTxt2 != null) {
                OffsetSurveyMin2 = 1;
                arrayPicker2[0] = SurveyMinTxt2;
            }

            for(int i=SurveyStartRange2;i<SurveyEndRange2;i++) {
                arrayPicker2[i-SurveyStartRange2+OffsetSurveyMin2] = SurveyPreUnit2 + i + SurveyPostUnit2;
            }

            if (SurveyMaxTxt2 != null) {
                arrayPicker2[SurveyEndRange2-SurveyStartRange2+OffsetSurveyMin2] = SurveyMaxTxt2;
            }
        }

        Title = (TextView) v.findViewById(R.id.survey_title);
        Description = (TextView) v.findViewById(R.id.survey_desc);
        Title.setText(SurveyTitle);
        Description.setText(SurveyDescription);

        pickers1 = (NumberPicker) v.findViewById(R.id.genericPicker1);

        //set min value zero
        pickers1.setMinValue(0);
        //set max value from length array string reduced 1
        pickers1.setMaxValue(arrayPicker1.length - 1);
        //implement array string to number picker
        pickers1.setDisplayedValues(arrayPicker1);
        //disable soft keyboard
        pickers1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        pickers1.setWrapSelectorWheel(false);
        if (SurveyStatus1 == -1) {
            pickers1.setValue(SurveyDefaultValue1 + OffsetSurveyMin1);
            SurveyStatus1 = SurveyStartRange1 + SurveyDefaultValue1 + OffsetSurveyMin1;
            mStepper.getExtras().putInt(SURVEY_STATUS1, SurveyStatus1);
            bundle.putInt(SURVEY_STATUS1, SurveyStatus1);
        } else {
            pickers1.setValue(SurveyStatus1 - SurveyStartRange1 + OffsetSurveyMin1);
        }

        pickers2 = (NumberPicker) v.findViewById(R.id.genericPicker2);
        if (SurveyEndRange2 != SurveyStartRange2) {

            //set min value zero
            pickers2.setMinValue(0);
            //set max value from length array string reduced 1
            pickers2.setMaxValue(arrayPicker2.length - 1);
            //implement array string to number picker
            pickers2.setDisplayedValues(arrayPicker2);
            //disable soft keyboard
            pickers2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

            pickers2.setWrapSelectorWheel(false);
            if (SurveyStatus2 == -1) {
                pickers2.setValue(SurveyDefaultValue2 + OffsetSurveyMin2);
                SurveyStatus2 = SurveyStartRange2 + SurveyDefaultValue2 + OffsetSurveyMin2;
                mStepper.getExtras().putInt(SURVEY_STATUS2, SurveyStatus2);
                bundle.putInt(SURVEY_STATUS2, SurveyStatus2);
            } else {
                pickers2.setValue(SurveyStatus2 - SurveyStartRange2 + OffsetSurveyMin2);
            }
        }
        else {
            pickers2.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 0);
            pickers1.setLayoutParams(params);
        }


        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt(SURVEY_STATUS1, SurveyStatus1);
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
        int pos = pickers1.getValue();
        SurveyStatus1 = SurveyStartRange1 + pos - OffsetSurveyMin1;
        mStepper.getExtras().putInt(SURVEY_STATUS1, SurveyStatus1);
        bundle.putInt(SURVEY_STATUS1, SurveyStatus1);
        if (SurveyEndRange2 != SurveyStartRange2) {
            pos = pickers2.getValue();
            SurveyStatus2 = SurveyStartRange2 + pos - OffsetSurveyMin2;
            mStepper.getExtras().putInt(SURVEY_STATUS2, SurveyStatus2);
            bundle.putInt(SURVEY_STATUS2, SurveyStatus2);
        }
        System.out.println("onNext");
        setVisibilityNextStep(true, 1);
    }

    @Override
    public void onPrevious() {
        int pos = pickers1.getValue();
        SurveyStatus1 = SurveyStartRange1 + pos - OffsetSurveyMin1;
        mStepper.getExtras().putInt(SURVEY_STATUS1, SurveyStatus1);
        bundle.putInt(SURVEY_STATUS1, SurveyStatus1);
        if (SurveyEndRange2 != SurveyStartRange2) {
            pos = pickers2.getValue();
            SurveyStatus2 = SurveyStartRange2 + pos - OffsetSurveyMin2;
            mStepper.getExtras().putInt(SURVEY_STATUS2, SurveyStatus2);
            bundle.putInt(SURVEY_STATUS2, SurveyStatus2);
        }
        System.out.println("onPrevious");
    }

    @Override
    public String optional() {
        return "You can skip";
    }

    @Override
    public boolean nextIf() {
        return SurveyStatus1 > 1;
    }

    @Override
    public String error() {
        return "<b>You must click!</b> <small>this is the condition!</small>";
    }
}
