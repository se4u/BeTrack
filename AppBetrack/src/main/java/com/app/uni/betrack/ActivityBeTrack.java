package com.app.uni.betrack;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ActivityBeTrack extends AppCompatActivity {

    private Menu SaveMenuRef = null;
    public static boolean OnForeground = false;

    /**
     * Converts the given hex-color-string to rgb.
     *
     * @param hex
     * @return
     */
    public static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }
    public static final int[] BETRACK_COLORS = {
            rgb("#C5CAE9"), rgb("#7986CB"), rgb("#3F51B5"), rgb("#303F9F")
    };

    private PieChart mChart;

    private ContentValues values = new ContentValues();

    private Animation animTranslate;

    private SettingsStudy ObjSettingsStudy = null;
    private SettingsBetrack ObjSettingsBetrack = null;

    @Override
    public void onStop() {
        super.onStop();
        OnForeground = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        OnForeground = true;
        if (false == ObjSettingsStudy.getEndSurveyDone()) {
            if ((UtilsTimeManager.ComputeTimeRemaing(this) > 0) || (UtilsTimeManager.LastDayTimeToNotification(this) > 0)) {
                if (false == ObjSettingsStudy.getSetupBetrackDone()) {
                    Intent i = new Intent(ActivityBeTrack.this, ActivitySetupBetrack.class);
                    startActivity(i);
                    finish();
                }
                else if (ObjSettingsStudy.getDailySurveyDone() == false) {
                    Intent i = new Intent(ActivityBeTrack.this, ActivitySurveyDaily.class);
                    startActivity(i);
                    finish();
                }
                mChart.setCenterText(generateCenterSpannableText());
            } else {
                Intent i = new Intent(ActivityBeTrack.this, ActivitySurveyEnd.class);
                startActivity(i);
                finish();
            }
        } else {
            if (ObjSettingsStudy.getEndSurveyTransferred()) {
                prepareChart(true);
                View buttonSetting = findViewById(R.id.FrameLayoutBetrackBtnSetting);
                buttonSetting.setVisibility(View.GONE);
                TextView textWelcome = (TextView) findViewById(R.id.TextWelcome);
                textWelcome.setText(getResources().getString(R.string.Betrack_end));
            } else {
                Intent iError = new Intent(ActivityBeTrack.this, ActivityErrors.class);
                iError.putExtra(ActivityErrors.STATUS_START_ACTIVITY, ActivityErrors.END_STUDY);
                startActivity(iError);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnForeground = true;
        if (null == ObjSettingsStudy) {
            //Read the setting of the study
            ObjSettingsStudy = SettingsStudy.getInstance(this);
        }

        if (null == ObjSettingsBetrack) {
            //Read the preferences
            ObjSettingsBetrack = SettingsBetrack.getInstance();
            ObjSettingsBetrack.Update(this);
        }

        //Setup the action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_logo_padding);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_betrack);


        if (false == ObjSettingsStudy.getEndSurveyDone()) {
            //Set the notification to make sure that we never got killed
            final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(SettingsBetrack.ID_NOTIFICATION_BETRACK);

            if ((UtilsTimeManager.ComputeTimeRemaing(this) > 0) || (UtilsTimeManager.LastDayTimeToNotification(this) > 0)) {
                if (ObjSettingsStudy.getDailySurveyDone() == false) {
                    Intent i = new Intent(ActivityBeTrack.this, ActivitySurveyDaily.class);
                    startActivity(i);
                    finish();
                } else {
                    //Prepare the chart to be display
                    prepareChart(false);
                    //We start the study
                    StartStudy();
                }
            }  else {
                Intent i = new Intent(ActivityBeTrack.this, ActivitySurveyEnd.class);
                startActivity(i);
                finish();
            }

        } else {
            prepareChart(true);
            View buttonSetting = findViewById(R.id.FrameLayoutBetrackBtnSetting);
            buttonSetting.setVisibility(View.GONE);
            TextView textWelcome = (TextView)findViewById(R.id.TextWelcome);
            textWelcome.setText(getResources().getString(R.string.Betrack_end));
        }
    }

    private void prepareChart(boolean endStudy) {
        //Set up the chart
        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        if (endStudy==false) {
            mChart.setCenterText(generateCenterSpannableText());
        } else {
            mChart.setCenterText(generateEmptySpannableText());
        }


        if (endStudy==false) {
            mChart.setDrawHoleEnabled(true);
        } else {
            mChart.setDrawHoleEnabled(false);
        }
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(80f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);

        // enable rotation of the chart by touch
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(false);


        setData(endStudy);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private SpannableString generateCenterSpannableText() {
        SpannableString s;
        int NbrDays = UtilsTimeManager.ComputeTimeRemaing(this);
        if (NbrDays > 1) {
            String Desc = getResources().getString(R.string.survey_days_left);
            s = new SpannableString(NbrDays+"\n"+Desc);
            if (NbrDays > 99) {
                s.setSpan(new RelativeSizeSpan(3.0f), 0, 3, 0);
                s.setSpan(new RelativeSizeSpan(2.0f), 3, 13, 0);
            }
            if (NbrDays > 9) {
                s.setSpan(new RelativeSizeSpan(3.0f), 0, 2, 0);
                s.setSpan(new RelativeSizeSpan(2.0f), 2, 12, 0);
            } else {
                s.setSpan(new RelativeSizeSpan(3.0f), 0, 1, 0);
                s.setSpan(new RelativeSizeSpan(2.0f), 1, 11, 0);
            }
        } else {
            String Desc = getResources().getString(R.string.survey_day_left);
            s = new SpannableString(NbrDays+"\n"+Desc);
            s.setSpan(new RelativeSizeSpan(3.0f), 0, 1, 0);
            s.setSpan(new RelativeSizeSpan(2.0f), 1, 10, 0);
        }

        return s;
    }

    private SpannableString generateEmptySpannableText() {
        SpannableString s = new SpannableString("");
        return s;
    }

    private void setData(boolean endStudy) {
        int[] UsagePerApp = new int[ObjSettingsStudy.getApplicationsToWatch().size()];

        int sumUsage = 0;
        float mult = 100;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        if (endStudy == true) {
            for(int i =0;i<ObjSettingsStudy.getApplicationsToWatch().size();i++) {
                UsagePerApp[i] = ObjSettingsStudy.getAppTimeWatched(i, ObjSettingsStudy.getApplicationsToWatch().size());
                sumUsage += UsagePerApp[i];
            }
            for(int i =0;i<ObjSettingsStudy.getApplicationsToWatch().size();i++) {
                if (UsagePerApp[i] > 0) {
                    entries.add(new PieEntry((float) ((UsagePerApp[i] * mult) / sumUsage), ObjSettingsStudy.getApplicationsToWatch().get(i)));
                }
            }
        } else {
            int Done = ((ObjSettingsStudy.getStudyDuration() - UtilsTimeManager.ComputeTimeRemaing(this)) * 100) / ObjSettingsStudy.getStudyDuration();
            int ToBeDone = (UtilsTimeManager.ComputeTimeRemaing(this) * 100) / ObjSettingsStudy.getStudyDuration();
            entries.add(new PieEntry((float) (ToBeDone), ""));
            entries.add(new PieEntry((float) (Done), ""));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Days study");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(BETRACK_COLORS);

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
        //Broadcast an event to start the tracking service if not yet started
        if (!ReceiverStartTracking.startTrackingRunning) {
            Intent intent = new Intent();
            intent.setAction(SettingsBetrack.BROADCAST_START_TRACKING_NAME);
            intent.putExtra(SettingsBetrack.BROADCAST_ARG_MANUAL_START, "1");
            sendBroadcast(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        //inflater.inflate(R.menu.settingsmenu, menu);
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

    public void onButtonClicked(View view) {
        switch (view.getId()) {
            case  R.id.ButtonSettings:
                startActivity(new Intent(this, ActivitySettings.class));
                break;
        }
    }
}
