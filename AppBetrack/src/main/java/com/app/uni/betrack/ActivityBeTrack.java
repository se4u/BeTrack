package com.app.uni.betrack;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
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

import java.util.ArrayList;


public class ActivityBeTrack extends AppCompatActivity {
    public static final int[] BETRACK_COLORS_END = {
            rgb("#1A237E"), rgb("#1976D2"), rgb("#009688"), rgb("#43A047"),
            rgb("#EF6C00"), rgb("#EF5350"), rgb("#C62828"), rgb("#C2185B"),
    };
    static final String TAG = "ActivityBeTrack";
    public static boolean OnForeground = false;
    final String FB_MESSENGER = "orca";
    final String FB_MESSENGER_COMMON_NAME = "messenger";
    final String FB_FACEBOOK = "katana";
    final String FB_FACEBOOK_COMMON_NAME = "facebook";
    private Menu SaveMenuRef = null;
    private PieChart mChart;
    private ContentValues values = new ContentValues();
    private Animation animTranslate;
    private SettingsStudy ObjSettingsStudy = null;
    private SettingsBetrack ObjSettingsBetrack = null;

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

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onStop() {
        super.onStop();
        OnForeground = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView textWelcome = (TextView) findViewById(R.id.TextWelcome);
        if (mChart == null) {
            mChart = (PieChart) findViewById(R.id.chart1);
        }

        OnForeground = true;

        if (((ObjSettingsStudy.getStudyDuration() - UtilsTimeManager.ComputeTimeRemaing(this)) >= 0) || (true == ObjSettingsStudy.getEndSurveyDone()))
        {
            textWelcome.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            mChart.setVisibility(View.VISIBLE);

            if (false == ObjSettingsStudy.getEndSurveyDone()) {
                if (ObjSettingsStudy.getNbrOfDaysToDo() >= 1) {
                    if (false == ObjSettingsStudy.getSetupBetrackDone()) {
                        Intent i = new Intent(ActivityBeTrack.this, ActivitySetupBetrack.class);
                        startActivity(i);
                        finish();
                    } else if (ObjSettingsStudy.getDailySurveyDone() == false) {

                        Log.d(TAG, "Nbr of notifications to do: " + (ObjSettingsStudy.getNbrOfNotificationToDo() + 1) % UtilsGetNotification.getNbrPerDay() + " DailySurveyState: " + ObjSettingsStudy.getDailySurveyState());
                        if (UtilsGetNotification.ListSurveys
                                [(ObjSettingsStudy.getNbrOfNotificationToDo() + 1) % UtilsGetNotification.getNbrPerDay()]
                                [ObjSettingsStudy.getDailySurveyState()] != null) {
                            Intent i = new Intent(ActivityBeTrack.this,
                                    UtilsGetNotification.ListSurveys
                                            [(ObjSettingsStudy.getNbrOfNotificationToDo() + 1) % UtilsGetNotification.getNbrPerDay()]
                                            [ObjSettingsStudy.getDailySurveyState()]);
                            int DailySurveyState = ObjSettingsStudy.getDailySurveyState();

                            if (DailySurveyState == 0) {
                                Log.d(TAG, "No more surveys to trigger for this time slot");
                                ObjSettingsStudy.setDailySurveyState(UtilsGetNotification.getNbrSurveysPerDay());
                                ObjSettingsStudy.setDailySurveyDone(true);
                            } else {
                                ObjSettingsStudy.setDailySurveyState(DailySurveyState - 1);
                            }


                            startActivity(i);
                            finish();
                        } else {
                            Log.d(TAG, "No more surveys to trigger for this time slot");
                            ObjSettingsStudy.setDailySurveyState(UtilsGetNotification.getNbrSurveysPerDay());
                            ObjSettingsStudy.setDailySurveyDone(true);
                        }
                    }
                    prepareChart(false);
                    textWelcome.setText(getResources().getString(R.string.Betrack_welcome));

                    //We start the study if not started yet
                    StartStudy();

                } else {
                    if (SettingsStudy.LastDayStudyState.ALL_SURVEYS_PENDING == ObjSettingsStudy.getLastDayStudyState()) {
                        Intent i = new Intent(ActivityBeTrack.this, ActivitySurveyDaily.class);
                        startActivity(i);
                        finish();

                    } else if (SettingsStudy.LastDayStudyState.START_SURVEY_DONE == ObjSettingsStudy.getLastDayStudyState()) {
                        Intent i = new Intent(ActivityBeTrack.this, ActivitySurveyEnd.class);
                        startActivity(i);
                        finish();
                    }
                }
            } else {
                SettingsStudy.EndStudyTranferState endStudyTranferState = ObjSettingsStudy.getEndSurveyTransferred();
                if (endStudyTranferState == SettingsStudy.EndStudyTranferState.DONE) {
                    Log.d(TAG, "getEndSurveyTransferred = DONE");
                    prepareChart(true);
                    textWelcome.setText(getResources().getString(R.string.Betrack_end));
                } else {
                    if (endStudyTranferState == SettingsStudy.EndStudyTranferState.IN_PROGRESS) {
                        Log.d(TAG, "getEndSurveyTransferred = IN_PROGRESS");
                        Intent iInternetConnectivity = new Intent(ActivityBeTrack.this, ActivityInternetConnectivity.class);

                        Intent msgIntent = new Intent(getApplicationContext(), IntentServicePostData.class);
                        //Start the service for sending the data to the remote server
                        startService(msgIntent);

                        iInternetConnectivity.putExtra(ActivityInternetConnectivity.STATUS_START_ACTIVITY, ActivityInternetConnectivity.END_STUDY_IN_PROGRESS);
                        startActivity(iInternetConnectivity);
                        finish();
                    } else {
                        if (endStudyTranferState == SettingsStudy.EndStudyTranferState.NOT_YET) {
                            Log.d(TAG, "getEndSurveyTransferred = NOT_YET");
                        } else {
                            Log.d(TAG, "getEndSurveyTransferred = ERROR");
                        }

                        Intent iInternetConnectivity = new Intent(ActivityBeTrack.this, ActivityInternetConnectivity.class);
                        iInternetConnectivity.putExtra(ActivityInternetConnectivity.STATUS_START_ACTIVITY, ActivityInternetConnectivity.END_STUDY_IN_ERROR);
                        startActivity(iInternetConnectivity);
                        finish();
                    }
                }
            }
        } else {
            textWelcome.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            textWelcome.setText(getResources().getString(R.string.Betrack_start));
            mChart.setVisibility(View.GONE);

            //We start the study if not started yet
            StartStudy();

        }

        textWelcome.setTextColor(Color.parseColor(SettingsBetrack.colorPrimary));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_betrack);

        getSupportActionBar().hide();

        if (null == ObjSettingsStudy) {
            //Read the setting of the study
            ObjSettingsStudy = SettingsStudy.getInstance(this);
        }

        if (null == ObjSettingsBetrack) {
            //Read the preferences
            ObjSettingsBetrack = SettingsBetrack.getInstance();
            ObjSettingsBetrack.Update(this);
        }

        if ((ObjSettingsStudy.getStudyDuration() - UtilsTimeManager.ComputeTimeRemaing(this)) >= 0)
        {
            //Set the notification to make sure that we never got killed
            final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(SettingsBetrack.ID_NOTIFICATION_BETRACK);
        }
    }

    private void prepareChart(boolean endStudy) {
        //Set up the chart

        mChart.setUsePercentValues(false);
        mChart.setDescription("");

        Legend legend = mChart.getLegend();

        if (endStudy==false) {
            legend.setEnabled(false);
        } else {
            legend.setEnabled(true);
            legend.setTextSize(12f);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            legend.setWordWrapEnabled(true);
            legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        }

        mChart.setDragDecelerationFrictionCoef(0.95f);

        if (endStudy==false) {
            mChart.setDrawEntryLabels(true);
            mChart.setCenterText(generateCenterSpannableText());
        } else {
            mChart.setDrawEntryLabels(false);
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
        mChart.setCenterTextColor(R.color.colorGrey);

        mChart.setRotationAngle(0);

        // enable rotation of the chart by touch
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(false);


        setData(endStudy);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private SpannableString generateCenterSpannableText() {
        SpannableString s;

        int NbrDays = ObjSettingsStudy.getNbrOfDaysToDo();

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
        int Done = 0;
        int NbrNotifications  = ObjSettingsStudy.getNbrOfDaysToDo();
        int NbrDays  = ObjSettingsStudy.getStudyDuration();
        float sumUsage = 0;
        float sumOthersAppUsage=0;
        float mult = 100;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        PieDataSet dataSet = new PieDataSet(entries, " ");


        if (endStudy == true) {

            for(int i =0;i<ObjSettingsStudy.getApplicationsToWatch().size();i++) {
                UsagePerApp[i] = ObjSettingsStudy.getAppTimeWatched(i, ObjSettingsStudy.getApplicationsToWatch().size()) / NbrDays;
                Log.d(TAG, "ApplicationsToWatch: " + i + " Name: " + ObjSettingsStudy.getApplicationsToWatch().get(i) + " Usage: " + UsagePerApp[i]);
                sumUsage += UsagePerApp[i];
            }

            sumOthersAppUsage = (SettingsStudy.getDurationScreenOn() / NbrDays) - sumUsage;
            sumUsage = (SettingsStudy.getDurationScreenOn() / NbrDays);

            for(int i =0;i<ObjSettingsStudy.getApplicationsToWatch().size();i++) {
                if (UsagePerApp[i] > 0) {
                    if (ObjSettingsStudy.getApplicationsToWatch().get(i).equals(FB_MESSENGER)) {
                        entries.add(new PieEntry((float) ((UsagePerApp[i] * mult) / sumUsage), FB_MESSENGER_COMMON_NAME));
                    } else if (ObjSettingsStudy.getApplicationsToWatch().get(i).equals(FB_FACEBOOK)) {
                        entries.add(new PieEntry((float) ((UsagePerApp[i] * mult) / sumUsage), FB_FACEBOOK_COMMON_NAME));
                    } else {
                        entries.add(new PieEntry((float) ((UsagePerApp[i] * mult) / sumUsage), ObjSettingsStudy.getApplicationsToWatch().get(i)));
                    }
                }
            }

            entries.add(new PieEntry((float) ((sumOthersAppUsage * mult) / sumUsage), getResources().getString(R.string.Betrack_screen_on)));

        } else {
            for(int i =0;i<ObjSettingsStudy.getStudyDuration();i++) {
                entries.add(new PieEntry((float) (100 / ObjSettingsStudy.getStudyDuration()), ""));
            }

            int done = rgb("#ECEFF1");
            int todo = rgb("#90CAF9");
            int[] graph_colors = new int[ObjSettingsStudy.getStudyDuration()];

            for(int i =0;i<ObjSettingsStudy.getStudyDuration();i++) {
                if (i < NbrNotifications) {
                    graph_colors[i] = done;
                } else {
                    graph_colors[i] = todo;
                }
            }

            dataSet.setColors(graph_colors);
        }


        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        PieData data = new PieData(dataSet);

        if (endStudy == true) {

            data.setValueFormatter(new UtilsValueFormatter(sumUsage));
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);

            dataSet.setColors(BETRACK_COLORS_END);
        }

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private void StartStudy()  {
        TextView textWelcome = (TextView) findViewById(R.id.TextWelcome);

        //Broadcast an event to start the tracking service if not yet started
        if (!ReceiverStartTracking.startTrackingRunning) {
            Intent intent = new Intent();
            intent.setAction(SettingsBetrack.BROADCAST_START_TRACKING_NAME);
            intent.putExtra(SettingsBetrack.BROADCAST_ARG_MANUAL_START, "1");
            sendBroadcast(intent);
            if ( (SettingsBetrack.STUDY_JUST_STARTED == false) && (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) ) {
                //We got killed, it should never happen so we inform the participant
                textWelcome.setText(getResources().getString(R.string.Betrack_battery_manager));
                textWelcome.setTextColor(Color.RED);
                //Save the info to be transfer to the distant database
                ObjSettingsStudy.setBetrackKilled(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if (false == ObjSettingsStudy.getEndSurveyDone()) {
            //inflater.inflate(R.menu.settingsmenu, menu);
            for (int j = 0; j < menu.size(); j++) {
                MenuItem item = menu.getItem(j);
                item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
            SaveMenuRef = menu;
        }

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
            case  R.id.ImgButtonChoice1:
                startActivity(new Intent(this, ActivitySettings.class));
                break;
        }
    }
}
