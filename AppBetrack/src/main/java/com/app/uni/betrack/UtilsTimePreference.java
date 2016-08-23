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

public class UtilsTimePreference extends DialogPreference {
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

    public UtilsTimePreference(Context ctxt, AttributeSet attrs) {
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
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String time=prefs.getString(getContext().getString(R.string.pref_key_study_notification_time), "20:00");

        lastHour=getHour(time);
        lastMinute=getMinute(time);
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
                lastHour=picker.getHour();
            else
                lastHour=picker.getCurrentHour();

            if (Build.VERSION.SDK_INT >= 23 )
                lastMinute=picker.getMinute();
            else
                lastMinute=picker.getCurrentMinute();

            String time=String.valueOf(lastHour)+":"+String.valueOf(lastMinute);

            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getContext().getString(R.string.pref_key_study_notification_time), time);
            editor.commit();
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return(a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

    }
}