package com.app.uni.betrack;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class BeTrackActivity extends AppCompatActivity {

    private static final String TAG = "Status";

    public static ProgressDialog dialog;
    public static ActionBar actionBar;

    private Menu SaveMenuRef = null;


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
        ObjSettingsBetrack.StudyNotificationTime= prefs.getString(SettingsBetrack.STUDY_NOTIFICATION_TIME, "20:00");
        ObjSettingsBetrack.FrequencyUpdateServer = prefs.getInt(SettingsBetrack.FREQ_UPDATE_SERVER, 6);

        //Display an Eula if needed
        new Eula(this).show();

        try
        {


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

            }
            else
            {
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

        }
        catch (Exception e) {

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.settingsmenu, menu);
        SaveMenuRef = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.settings_menu:
                startActivity(new Intent(this, SettingsActivity.class));
        }
        return true;
    }

    public void onButtonClicked(View view) {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();

        switch(view.getId()) {
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
}
