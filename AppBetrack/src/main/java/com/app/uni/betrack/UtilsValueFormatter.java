package com.app.uni.betrack;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Lo on 2017-05-26.
 */

public class UtilsValueFormatter implements IValueFormatter, IAxisValueFormatter {
    private DecimalFormat percentageFormat;
    private DecimalFormat minFormat;
    private DecimalFormat hourFormat;
    private float sum;


    public UtilsValueFormatter(float sumTime) {
        percentageFormat = new DecimalFormat("###,###,##0");
        minFormat = new DecimalFormat("###,###,###0");
        minFormat.setRoundingMode(RoundingMode.UP);
        hourFormat = new DecimalFormat("###,###,##0.0");
        sum = sumTime;
    }


    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return FormatValues(value);
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return FormatValues(value);
    }

    public String FormatValues(float value)
    {
        float mins = ((value/100) * sum) / 60;

        if (mins < 60) {
            return percentageFormat.format(value) + "% (" + minFormat.format(mins) + "min)";
        } else {
            return percentageFormat.format(value) + "% (" + hourFormat.format(mins/60) + "h)";
        }
    }

    @Override
    public int getDecimalDigits() {
        return 1;
    }


}
