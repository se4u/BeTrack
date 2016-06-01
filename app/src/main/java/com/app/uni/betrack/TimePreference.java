package com.app.uni.betrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class TimePreference extends DialogPreference {
    private int lastHour=0;
    private int lastMinute=0;
    private TimePicker picker=null;

    public static int getHour(String time) {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[0]));
    }

    public static int getMinute(String time) {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[1]));
    }

    public TimePreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);

        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
    }

    @Override
    protected View onCreateDialogView() {
        picker=new TimePicker(getContext());

        picker.setIs24HourView(true);

        return(picker);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        if (Build.VERSION.SDK_INT >= 23 )
            picker.setHour(lastHour);
        else
            picker.setCurrentHour(lastHour);

        if (Build.VERSION.SDK_INT >= 23 )
            picker.setMinute(lastMinute);
        else
            picker.setCurrentMinute(lastMinute);

    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {

            if (Build.VERSION.SDK_INT >= 23 )
                lastMinute=picker.getHour();
            else
                lastHour=picker.getCurrentHour();

            if (Build.VERSION.SDK_INT >= 23 )
                lastMinute=picker.getMinute();
            else
                lastMinute=picker.getCurrentMinute();

            String time=String.valueOf(lastHour)+":"+String.valueOf(lastMinute);

            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(SettingsBetrack.STUDY_NOTIFICATION_TIME, time);
            editor.commit();
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return(a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String time=prefs.getString(SettingsBetrack.STUDY_NOTIFICATION_TIME, "20:00");

        lastHour=getHour(time);
        lastMinute=getMinute(time);
    }
}