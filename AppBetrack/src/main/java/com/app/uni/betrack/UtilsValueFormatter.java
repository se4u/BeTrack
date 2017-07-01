package com.app.uni.betrack;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Lo on 2017-05-26.
 */

public class MyValueFormatter implements IValueFormatter, IAxisValueFormatter {
    private DecimalFormat percentageFormat;
    private DecimalFormat valueFormat;
    private float sum;


    public MyValueFormatter(float sumTime) {
        percentageFormat = new DecimalFormat("###,###,##0.0");
        valueFormat = new DecimalFormat("###,###,###0.00");
        sum = sumTime;
    }


    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        float mins = ((value/100) * sum) / 60;

        return percentageFormat.format(value) + " % ( " + valueFormat.format(mins) + " mins) ";
    }

    // IAxisValueFormatter
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        float mins = ((value/100) * sum) / 60;
        return percentageFormat.format(value) + " % ( " + valueFormat.format(mins) + " mins) ";
    }

    @Override
    public int getDecimalDigits() {
        return 1;
    }


}
