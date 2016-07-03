package com.app.uni.betrack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class BeTrackActivity extends AppCompatActivity {

    private static final String TAG = "Status";

    public static ProgressDialog dialog;
    public static ActionBar actionBar;

    private Menu SaveMenuRef = null;

    private Animation animTranslate;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SettingsBetrack.SERVICE_TRACKING_NAME.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public InfoStudy ObjInfoStudy = new InfoStudy();
    private SettingsBetrack ObjSettingsBetrack = new SettingsBetrack();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();

        actionBar = getSupportActionBar();
        actionBar.hide();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_girl_1_padding);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_betrack);

        //Update settings with value of preferences from the shared preference editor or default values
        ObjSettingsBetrack.StudyEnable = prefs.getBoolean(SettingsBetrack.STUDY_ENABLE, true);
        ObjSettingsBetrack.EnableDataUsage = prefs.getBoolean(SettingsBetrack.ENABLE_DATA_USAGE, true);
        ObjSettingsBetrack.StudyNotification = prefs.getBoolean(SettingsBetrack.STUDY_NOTIFICATION, true);
        ObjSettingsBetrack.StudyNotificationTime = prefs.getString(SettingsBetrack.STUDY_NOTIFICATION_TIME, "20:00");
        ObjSettingsBetrack.FrequencyUpdateServer = prefs.getInt(SettingsBetrack.FREQ_UPDATE_SERVER, 6);

        //Display an Eula if needed
        new Eula(this).show();

        try {


            findViewById(R.id.Layout_Welcome).setVisibility(View.INVISIBLE);
            findViewById(R.id.Layout_Study).setVisibility(View.INVISIBLE);

            ObjInfoStudy.StudyStarted = prefs.getBoolean(ObjInfoStudy.STUDY_STARTED, false);

            //Check if a study is already going on
            if (false == ObjInfoStudy.StudyStarted) {

                findViewById(R.id.Layout_Welcome).setVisibility(View.VISIBLE);
                findViewById(R.id.Layout_Study).setVisibility(View.INVISIBLE);

                /* That was done in case we'll have multiple study, I keep it for now maybe I will reuse for multiple language
                for (int i = 0; i < GetStudiesAvailable.NbrMaxStudy; i++) {
                    if (i < GetStudiesAvailable.NbrStudyAvailable) {
                        button[i].setText(GetStudiesAvailable.StudyName[i]);
                        button[i].setEnabled(true);
                        button[i].setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        button[i].setVisibility(View.GONE);
                    }
                }
                */
                TextView StudyTitle = new TextView(this);
                StudyTitle = (TextView) findViewById(R.id.study_title);
                StudyTitle.setText(GetStudiesAvailable.StudyName[0]);

                TextView StudyDescription = new TextView(this);
                StudyDescription = (TextView) findViewById(R.id.study_description);
                StudyDescription.setText(GetStudiesAvailable.StudyDescription[0]);

            } else {
                //Get the unique ID of that user
                InfoStudy.IdUser = prefs.getString(InfoStudy.ID_USER, "No user ID !");
                //Get the description of the study
                InfoStudy.StudyDescription = prefs.getString(InfoStudy.STUDY_DESCRIPTION, "No study description !");

                //Read from the preference the information of the study
                new SetupStudy(this, ObjInfoStudy);

                //Update the study page

                //we display the page of the study
                actionBar.show();
                findViewById(R.id.Layout_Welcome).setVisibility(View.INVISIBLE);
                findViewById(R.id.Layout_Study).setVisibility(View.VISIBLE);

                //Broadcast an event to start the tracking service if not yet started
                if (!isMyServiceRunning()) {
                    Intent intent = new Intent();
                    intent.setAction(SettingsBetrack.BROADCAST_START_TRACKING_NAME);
                    sendBroadcast(intent);
                }

            }

        } catch (Exception e) {

        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
                startActivity(new Intent(this, SettingsActivity.class));
        }
        return true;
    }

    public void onButtonClicked(View view) {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();

        switch (view.getId()) {
            case R.id.buttonUpdate:
                TextView StudyDescription = new TextView(this);
                //Get the unique ID of that user
                if (null == Eula.IdUser) {
                    InfoStudy.IdUser = prefs.getString(InfoStudy.ID_USER, "No user ID !");
                }

                //Save the study description
                StudyDescription = (TextView) findViewById(R.id.study_description);
                editor.putString(InfoStudy.STUDY_DESCRIPTION, StudyDescription.getText().toString());
                //Get the description of the study
                InfoStudy.StudyDescription = StudyDescription.getText().toString();
                editor.commit();

                dialog = ProgressDialog.show(this, this.getString(R.string.welcome_loading),
                        this.getString(R.string.welcome_wait), true);

                //We set up the study
                new SetupStudy(this, ObjInfoStudy);

                break;
            case R.id.ButtonNetwork:

                dialog = ProgressDialog.show(this, this.getString(R.string.welcome_loading),
                        this.getString(R.string.welcome_wait), true);
                //We set up the study
                new SetupStudy(this, ObjInfoStudy);
                break;
        }
    }

    //private ViewGroup viewGroup;

    public void onButtonPeriodClicked(View view) {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();

        View viewPeriod = findViewById(R.id.LinearLayout_Period_Bottom);

        View[] viewLibido = {findViewById(R.id.LinearLayout_Libido_Top),
                             findViewById(R.id.LinearLayout_Libido_Bottom)};

        View[] viewOther = {findViewById(R.id.LinearLayout_Study_Top),
                findViewById(R.id.LinearLayout_Sexual_Top),
                findViewById(R.id.LinearLayout_Sexual_Bottom),
                findViewById(R.id.LinearLayout_Social_Top),
                findViewById(R.id.LinearLayout_Social_Bottom),
                findViewById(R.id.LinearLayout_Libido_Top),
                findViewById(R.id.LinearLayout_Libido_Bottom)};

        View[] viewNext = {findViewById(R.id.LinearLayout_Study_Top),
                findViewById(R.id.LinearLayout_Sexual_Top),
                findViewById(R.id.LinearLayout_Sexual_Bottom),
                findViewById(R.id.LinearLayout_Social_Top),
                findViewById(R.id.LinearLayout_Social_Bottom)};

        switch (view.getId()) {

            case R.id.ButtonPeriod:
                if (View.VISIBLE == viewPeriod.getVisibility()) {
                    animFirst(viewPeriod, null,  viewOther, null, false);
                }
                else
                {
                    animFirst(viewPeriod, null, viewOther, null, true);
                }
                break;

            case R.id.ButtonPeriodNo:
            case R.id.ButtonPeriodYes:

                if (View.VISIBLE == viewPeriod.getVisibility()) {
                    animFirst(viewPeriod, viewLibido, viewOther, viewNext, false);
                }
                else
                {
                    animFirst(viewPeriod, null, viewOther, null, true);
                }
                break;
        }

    }

    private void animFirst(final View viewPeriod, final View NextView[], final View listView[], final View listnextView[], boolean Visible) {

        float AlphaStart = 0;
        float AlphaEnd = 0;
        final int translation_y;
        final int DURATION = 500;
        final int TRANSLATION_Y = viewPeriod.getHeight();

        if (true == Visible) {
            AlphaStart = 0;
            AlphaEnd = 1;
            translation_y = TRANSLATION_Y;
        }
        else
        {
            AlphaStart = 1;
            AlphaEnd = 0;
            translation_y = -TRANSLATION_Y;
        }

        viewPeriod.setAlpha(AlphaStart);

        if (true == Visible) {

            for (int i = 0; i < listView.length; i++) {
                if (View.VISIBLE == listView[i].getVisibility())
                    listView[i].animate()
                            .translationYBy(translation_y)
                            .setDuration(DURATION)
                            .setStartDelay(0)
                            .start();

            }

            viewPeriod.setVisibility(View.VISIBLE);

            viewPeriod.animate()
                    .alpha(AlphaEnd)
                    .setDuration(DURATION)
                    .setStartDelay(DURATION)
                    .setListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(final Animator animation) {

                        }
                    })
                    .start();

        }
        else
        {
            viewPeriod.animate()
                    .alpha(AlphaEnd)
                    .setDuration(DURATION)
                    .setStartDelay(0)
                    .setListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(final Animator animation) {

                            if (View.VISIBLE == viewPeriod.getVisibility()) {
                                viewPeriod.setVisibility(View.INVISIBLE);
                            }
                            else
                            {
                                viewPeriod.setVisibility(View.VISIBLE);
                            }

                            if (null == NextView) {
                                for (int i = 0; i < listView.length; i++) {
                                    if (View.VISIBLE == listView[i].getVisibility())
                                        listView[i].animate()
                                                .translationYBy(translation_y)
                                                .setDuration(DURATION)
                                                .setStartDelay(0)
                                                .setListener(new AnimatorListenerAdapter() {

                                                    @Override
                                                    public void onAnimationStart(final Animator animation) {


                                                    }
                                                })
                                                .start();
                                }
                            }
                            else {
                                NextView[1].animate()
                                        .translationYBy(translation_y)
                                        .setDuration(DURATION)
                                        .setStartDelay(0)
                                        .start();
                                NextView[0].animate()
                                        .translationYBy(translation_y)
                                        .setDuration(DURATION)
                                        .setStartDelay(0)
                                        .setListener(new AnimatorListenerAdapter() {

                                            @Override
                                            public void onAnimationEnd(final Animator animation) {

                                                NextView[1].setAlpha(0);
                                                NextView[1].setVisibility(View.VISIBLE);
                                                NextView[1].animate()
                                                        .alpha(1)
                                                        .setDuration(DURATION)
                                                        .setStartDelay(0)
                                                        .setListener(new AnimatorListenerAdapter() {

                                                            @Override
                                                            public void onAnimationEnd(final Animator animation) {
                                                                final int NEXTVIEW_TRANSLATION_Y = NextView[1].getHeight();
                                                                for (int i = 0; i < listnextView.length; i++) {
                                                                    if (View.VISIBLE == listnextView[i].getVisibility())
                                                                        listnextView[i].animate()
                                                                                .translationYBy(-NEXTVIEW_TRANSLATION_Y)
                                                                                .setDuration(DURATION)
                                                                                .setStartDelay(0)
                                                                                .start();
                                                                }
                                                            }
                                                        })
                                                        .start();

                                            }
                                        })
                                        .start();

                            }

                        }
                    })
                    .start();
        }

    }



    public void onButtonBehaviourClicked(View view) {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();

        View[] viewLibido = {findViewById(R.id.LinearLayout_Libido_Top),
                findViewById(R.id.LinearLayout_Libido_Bottom)};

        View[] viewNext = {findViewById(R.id.LinearLayout_Study_Top),
                findViewById(R.id.LinearLayout_Sexual_Top),
                findViewById(R.id.LinearLayout_Sexual_Bottom),
                findViewById(R.id.LinearLayout_Social_Top),
                findViewById(R.id.LinearLayout_Social_Bottom)};

        switch (view.getId()) {

            case R.id.ButtonLibido:

                break;

            case R.id.ButtonLibidoMax:
            case R.id.ButtonLibidoAvg:
            case R.id.ButtonLibidoMin:
                RemoveMood(viewLibido, viewNext);
                break;
        }

    }

    private void RemoveMood(final View viewToRemove[], final View listView[]) {

        final int DURATION = 500;
        final int NEXTVIEW_TRANSLATION_X = viewToRemove[0].getWidth();
        viewToRemove[1].animate()
                .alpha(0)
                .setDuration(DURATION)
                .setStartDelay(DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(final Animator animation) {
                        viewToRemove[0].animate()
                                .translationXBy(NEXTVIEW_TRANSLATION_X)
                                .setDuration(DURATION)
                                .setStartDelay(0)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(final Animator animation) {
                                        viewToRemove[1].setVisibility(View.GONE);
                                    }
                                })
                                .start();
                    }
                    @Override
                    public void onAnimationEnd(final Animator animation) {
                        viewToRemove[0].setVisibility(View.GONE);
                    }
                })
                .start();

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BeTrack Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.app.uni.betrack/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BeTrack Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.app.uni.betrack/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
