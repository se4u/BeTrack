package com.app.uni.betrack;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by cevincent on 17/01/2017.
 */

public class FragmentSurveyDatePicker  extends AbstractStep {
    private DatePicker picker = null;

    //Output
    public static final String SURVEY_STATUS = "SURVEY_STATUS";
    public String SurveyStatus = null;

    //Input
    public static final String SURVEY_DATEPICKER_CHOICES_TITLE = "SURVEY_DATEPICKER_CHOICES_TITLE";
    public static final String SURVEY_DATEPICKER_CHOICES_DESC = "SURVEY_DATEPICKER_CHOICES_DESC";

    private Bundle bundle;
    private TextView Title;
    private TextView Description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Date theDate = null;
        Calendar myCal;
        View v = inflater.inflate(R.layout.survey_datepicker, container, false);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");

        picker = (DatePicker) v.findViewById(R.id.date_picker);
        picker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

        Title = (TextView) v.findViewById(R.id.survey_title);
        Description = (TextView) v.findViewById(R.id.survey_desc);

        bundle = this.getArguments();
        String SurveyTitle = bundle.getString(SURVEY_DATEPICKER_CHOICES_TITLE, null);
        String SurveyDescription = bundle.getString(SURVEY_DATEPICKER_CHOICES_DESC, null);
        Title.setText(SurveyTitle);
        Description.setText(SurveyDescription);

        if (SurveyStatus != null) {
            try {
                theDate = sdf.parse(SurveyStatus);
            } catch (Exception e) {
                e.printStackTrace();
            }

            myCal = new GregorianCalendar();
            myCal.setTime(theDate);

            int day = myCal.get(Calendar.DAY_OF_MONTH);
            int month = myCal.get(Calendar.MONTH) + 1;
            int year = myCal.get(Calendar.YEAR);
            picker.updateDate(year,month,day);
        } else {
            myCal = Calendar.getInstance();
            myCal.setTimeInMillis(System.currentTimeMillis());
            SurveyStatus=myCal.get(Calendar.DAY_OF_MONTH) + "/" + (myCal.get(Calendar.MONTH) + 1) + "/" + myCal.get(Calendar.YEAR);
            bundle.putString(SURVEY_STATUS, SurveyStatus);
        }

        picker.init(myCal.get(Calendar.YEAR), myCal.get(Calendar.MONTH), myCal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                SurveyStatus=picker.getDayOfMonth() + "/" + (picker.getMonth() + 1) + "/" + picker.getYear();
                mStepper.getExtras().putString(SURVEY_STATUS, SurveyStatus);
                bundle.putString(SURVEY_STATUS, SurveyStatus);
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString(SURVEY_STATUS, SurveyStatus);
    }

    @Override
    public String name() {
        return "Tab " + getArguments().getString(SURVEY_STATUS, null);
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
        return SurveyStatus != null;
    }

    @Override
    public String error() {
        return getResources().getString(R.string.question_error);
    }
}