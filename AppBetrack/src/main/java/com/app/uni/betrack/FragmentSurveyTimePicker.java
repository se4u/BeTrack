package com.app.uni.betrack;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.fcannizzaro.materialstepper.AbstractStep;

/**
 * Created by cedoctet on 21/09/2016.
 */
public class FragmentSurveyTimePicker extends AbstractStep {

    //Output
    public  static final String SURVEY_STATUS = "SURVEY_STATUS";
    public int SurveyStatus = -1;
    private TimePicker picker=null;

    //Input
    public static final String SURVEY_TIMEPICKER_CHOICES_TITLE = "SURVEY_TIMEPICKER_CHOICES_TITLE";
    public static final String SURVEY_TIMEPICKER_CHOICES_DESC = "SURVEY_TIMEPICKER_CHOICES_DESC";

    private TextView Title;
    private TextView Description;

    private int lastHour=0;
    private int lastMinute=0;

    private static SettingsBetrack ObjSettingsBetrack;

    private static int getHour(String time) {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[0]));
    }

    private static int getMinute(String time) {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[1]));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String time = null;
        View v = inflater.inflate(R.layout.survey_timepicker, container, false);

        picker = (TimePicker) v.findViewById(R.id.time_picker);

        if (!DateFormat.is24HourFormat(getContext())) {
            picker.setIs24HourView(false);
        } else {
            picker.setIs24HourView(true);
        }

        time=prefs.getString(getContext().getString(R.string.pref_key_study_notification_time), "20:00");
        picker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

        lastHour=getHour(time);
        lastMinute=getMinute(time);

        if (Build.VERSION.SDK_INT >= 23 )  {
            picker.setHour(lastHour);
            picker.setMinute(lastMinute);
        }
        else {
            picker.setCurrentHour(lastHour);
            picker.setCurrentMinute(lastMinute);
        }

        Title = (TextView) v.findViewById(R.id.survey_title);
        Description = (TextView) v.findViewById(R.id.survey_desc);

        final Bundle bundle = this.getArguments();
        String SurveyTitle = bundle.getString(SURVEY_TIMEPICKER_CHOICES_TITLE, null);
        String SurveyDescription = bundle.getString(SURVEY_TIMEPICKER_CHOICES_DESC, null);
        Title.setText(SurveyTitle);
        Description.setText(SurveyDescription);

        if (savedInstanceState != null)
            SurveyStatus = savedInstanceState.getInt(SURVEY_STATUS, -1);

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
        UpdateNotificationTime();
    }

    @Override
    public void onPrevious() {
        System.out.println("onPrevious");
        UpdateNotificationTime();
    }

    private void UpdateNotificationTime() {
        String time = null;

        if (ObjSettingsBetrack == null)  {
            ObjSettingsBetrack = SettingsBetrack.getInstance();
        }

        if (Build.VERSION.SDK_INT >= 23 )
            lastHour=picker.getHour();
        else
            lastHour=picker.getCurrentHour();

        if (Build.VERSION.SDK_INT >= 23 )
            lastMinute=picker.getMinute();
        else
            lastMinute=picker.getCurrentMinute();

        time = String.valueOf(lastHour) + ":" + String.valueOf(lastMinute);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getContext().getString(R.string.pref_key_study_notification_time), time);
        editor.commit();
        ObjSettingsBetrack.Update(getContext());
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(picker.getWindowToken(), 0);
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
