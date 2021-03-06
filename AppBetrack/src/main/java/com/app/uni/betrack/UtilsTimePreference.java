package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilsTimePreference extends DialogPreference {
    private int lastHour=0;
    private int lastMinute=0;
    private TimePicker picker=null;

    private static UtilsLocalDataBase localdatabase =  null;

    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

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
        return(picker);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String time=null;
        if (!DateFormat.is24HourFormat(getContext())) {
            picker.setIs24HourView(false);
        } else {
            picker.setIs24HourView(true);
        }

        time=prefs.getString(getContext().getString(R.string.pref_key_study_notification_time), "20:00");

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
        String time = null;
        String ActivityStartDate = "";
        String ActivityStartTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

        ContentValues values = new ContentValues();

        if (positiveResult) {

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

            if (null == localdatabase) {
                localdatabase =  new UtilsLocalDataBase(getContext());
            }

            values.clear();
            //Save the date
            ActivityStartDate = sdf.format(new Date());
            //Save the time
            ActivityStartTime = shf.format(new Date());
            values.put(UtilsLocalDataBase.C_NOTIFICATION_TIME, time);
            values.put(UtilsLocalDataBase.C_NOTIFICATION_TIME_DATE, ActivityStartDate);
            values.put(UtilsLocalDataBase.C_NOTIFICATION_TIME_TIME, ActivityStartTime);
            try {
                AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_NOTIFICATION_TIME);
            } catch (Exception f) {}
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