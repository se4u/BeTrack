package com.app.uni.betrack;

import android.app.ActivityManager;
import android.content.Intent;

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


        setContentView(R.layout.activity_betrack);

        //Update settings with value of preferences from the shared preference editor or default values
        ObjSettingsBetrack.StudyEnable = prefs.getBoolean(SettingsBetrack.STUDY_ENABLE, true);
        ObjSettingsBetrack.EnableDataUsage = prefs.getBoolean(SettingsBetrack.ENABLE_DATA_USAGE, true);
        ObjSettingsBetrack.StudyNotification = prefs.getBoolean(SettingsBetrack.STUDY_NOTIFICATION, true);
        ObjSettingsBetrack.StudyNotificationTime= prefs.getString(SettingsBetrack.STUDY_NOTIFICATION_TIME, "20:00");
        ObjSettingsBetrack.FrequencyUpdateServer = prefs.getInt(SettingsBetrack.FREQ_UPDATE_SERVER, 6);

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
                //No study is on yet, get the available one
                //Display all the study available
                RadioButton button[] = new RadioButton[GetStudiesAvailable.NbrMaxStudy];

                button[0] = (RadioButton) findViewById(R.id.radio_studyone);
                button[1] = (RadioButton) findViewById(R.id.radio_studytwo);
                button[2] = (RadioButton) findViewById(R.id.radio_studythree);

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
            }
            else
            {

                //Read from the preference the information of the study
                new SetupStudy(this, ObjInfoStudy);

                //Update the study page

                //we display the page of the study
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


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        RadioButton button[] = new RadioButton[GetStudiesAvailable.NbrMaxStudy];
        TextView StudyDescription = new TextView(this);
        StudyDescription = (TextView) findViewById(R.id.test_description);
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_studyone:
                if (checked)
                    StudyDescription.setText(GetStudiesAvailable.StudyDescription[0]);
                    break;
            case R.id.radio_studytwo:
                if (checked)
                    StudyDescription.setText(GetStudiesAvailable.StudyDescription[1]);
                    break;
            case R.id.radio_studythree:
                if (checked)
                    StudyDescription.setText(GetStudiesAvailable.StudyDescription[2]);
                    break;
        }
    }

    public void onButtonClicked(View view) {

        switch(view.getId()) {
            case R.id.buttonUpdate:
                //Disable the radio button
                RadioButton button[] = new RadioButton[GetStudiesAvailable.NbrMaxStudy];

                button[0] = (RadioButton) findViewById(R.id.radio_studyone);
                button[1] = (RadioButton) findViewById(R.id.radio_studytwo);
                button[2] = (RadioButton) findViewById(R.id.radio_studythree);

                for (int i = 0; i < GetStudiesAvailable.NbrMaxStudy; i++) {
                    if (i < GetStudiesAvailable.NbrStudyAvailable) {
                        button[i].setEnabled(false);
                    }
                }

                //Disable the setting menu
                SaveMenuRef.clear();

                //Hide the start button
                Button StartButton;
                StartButton = (Button) findViewById(R.id.buttonUpdate);
                StartButton.setVisibility(View.GONE);

                //Display the progress bar
                ProgressBar WaitData;
                WaitData = (ProgressBar) findViewById(R.id.ProgressBarWaitData);
                WaitData.setVisibility(View.VISIBLE);
                TextView LoadingText;
                LoadingText = (TextView) findViewById(R.id.WelcomeLoading);
                LoadingText.setVisibility(View.VISIBLE);
                //We set up the study
                new SetupStudy(this, ObjInfoStudy);

                break;
            case R.id.ButtonNetwork:
                //We set up the study
                new SetupStudy(this, ObjInfoStudy);
                break;
        }


    }
}
