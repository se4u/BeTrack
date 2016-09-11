package com.app.uni.betrack;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;


public class ActivityBeTrack extends AppCompatActivity {

    private static final String TAG = "Status";

    public static ProgressDialog dialog;

    protected String[] mParties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    private UtilsLocalDataBase localdatabase = new UtilsLocalDataBase(this);
    public UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
    private String DatePeriod = null;

    private Menu SaveMenuRef = null;

    private PieChart mChart;

    private ContentValues values = new ContentValues();

    private Animation animTranslate;

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SettingsBetrack.SERVICE_TRACKING_NAME.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private SettingsStudy ObjSettingsStudy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Read the setting of the study
        ObjSettingsStudy = SettingsStudy.getInstance(this);

        //Setup the action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_logo_padding);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_betrack);

        //Set the notification to make sure that we never got killed
        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(SettingsBetrack.NOTIFICATION_ID);

        //Set up the chart
        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);

        // enable rotation of the chart by touch
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(false);

        setData(25, 1);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        //We start the study
        StartStudy();
    }

    private SpannableString generateCenterSpannableText() {

        String Title = "UBC";
        String Desc = "Days of the study";
        SpannableString s = new SpannableString(Title+"\n"+Desc);
        s.setSpan(new RelativeSizeSpan(1.7f), 0, Title.length(), 0);
        return s;
    }

    private void setData(int daysRemaining, int daysUsed) {

        int count = 2;
        float mult = 100;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        if ((daysUsed * mult) / (daysRemaining + daysUsed)!=100) {
            entries.add(new PieEntry((float) ((daysRemaining * mult) / (daysRemaining + daysUsed)), "Remaining"));
        }

        if ((daysRemaining * mult) / (daysRemaining + daysUsed)!=100) {
            entries.add(new PieEntry((float) ((daysUsed * mult) / (daysRemaining + daysUsed)), "Done"));
        }


        PieDataSet dataSet = new PieDataSet(entries, "Days study");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private void StartStudy()  {
/*
        values.clear();
        values.put(UtilsLocalDataBase.C_USER_PERIOD, "1");
        DatePeriod = sdf.format(new Date());
        values.put(UtilsLocalDataBase.C_USER_DATE, DatePeriod);

        AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_USER);
        Log.d(TAG, "idUser: " + ObjSettingsStudy.getIdUser() + "Period status: " + 1 + " date: " + DatePeriod);

        values.clear();
        values.put(UtilsLocalDataBase.C_USER_PERIOD, "0");
        DatePeriod = sdf.format(new Date());
        values.put(UtilsLocalDataBase.C_USER_DATE, DatePeriod);

        AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_USER);
        Log.d(TAG, "idUser: " + ObjSettingsStudy.getIdUser() + "Period status: " + 0 + " date: " + DatePeriod);
*/
        //Broadcast an event to start the tracking service if not yet started
        if (!isMyServiceRunning()) {
            Intent intent = new Intent();
            intent.setAction(SettingsBetrack.BROADCAST_START_TRACKING_NAME);
            sendBroadcast(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.settingsmenu, menu);
        SaveMenuRef = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu:
                startActivity(new Intent(this, ActivitySettings.class));
        }
        return true;
    }

}
