package com.hagen.fernuni.betrack;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RadioGroup;
import android.widget.TextView;

public class BeTrackActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1001;
    private static final String TAG = "Status";
    private String STUDY_ONGOING = "study_ongoing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        setContentView(R.layout.activity_betrack);

        new Eula(this).show();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            if(!hasPermission()){
                startActivityForResult(
                        new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                        MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
            }
        }
        try
        {
            final String StudyOnGoingKey = STUDY_ONGOING;
            boolean StudyOnGoing = prefs.getBoolean(StudyOnGoingKey, false);
            boolean StudyReady = false;
            do {
                //Check if a study is already going on
                if (false == StudyOnGoing) {

                    //Check if connection to the distant server worked
                    if (null != new GetStudiesAvailable().execute().get()) {
                        //No study is on yet, get the available one
                        //Display all the study available
                        RadioButton button[] = new RadioButton[GetStudiesAvailable.NbrMaxStudy];

                        button[0] = (RadioButton) findViewById(R.id.radio_studyone);
                        button[1] = (RadioButton) findViewById(R.id.radio_studytwo);
                        button[2] = (RadioButton) findViewById(R.id.radio_studythree);

                        for (int i = 0; i < GetStudiesAvailable.NbrMaxStudy; i++) {
                            if (i < GetStudiesAvailable.NbrStudyAvailable) {
                                button[i].setText(GetStudiesAvailable.StudyName[i]);
                                button[i].setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                button[i].setVisibility(View.GONE);
                            }
                        }
                        StudyReady = true;
                    }
                    else
                    {
                        new NetworkError(this).show();
                    }

                }
                else
                {
                    //A study is already going on
                    //Read the information of the study

                    //Update the study page

                    //we display the page of the study
                    findViewById(R.id.Layout_Welcome).setVisibility(View.INVISIBLE);
                    findViewById(R.id.Layout_Study).setVisibility(View.VISIBLE);
                }
            }while(false == StudyReady);

        }
        catch (Exception e) {

        }

        //Intent msgIntent = new Intent(this, TrackIntentService.class);
        //startService(msgIntent);
    }

    @TargetApi(19) private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settingsmenu, menu);
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
        //Broadcast an event to start the tracking service if not yet started

        //Show the study screen
        findViewById(R.id.Layout_Welcome).setVisibility(View.INVISIBLE);
        findViewById(R.id.Layout_Study).setVisibility(View.VISIBLE);
    }
}
