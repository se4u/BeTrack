package com.app.uni.betrack;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ActivityBeTrack extends AppCompatActivity {

    private static final String TAG = "Status";

    public static ProgressDialog dialog;
    public static ActionBar actionBar;

    private Context mContext;
    private UtilsLocalDataBase localdatabase = new UtilsLocalDataBase(this);
    public UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
    private String DatePeriod = null;

    private Menu SaveMenuRef = null;

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
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        mContext = this;

        ObjSettingsStudy = SettingsStudy.getInstance(this);

        actionBar = getSupportActionBar();
        actionBar.hide();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_logo_padding);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_betrack);

        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(SettingsBetrack.NOTIFICATION_ID);

    }


    private void StartStudy()  {

        TextView StudyTitle = new TextView(this);
        StudyTitle = (TextView) findViewById(R.id.study_title);
        //StudyTitle.setText(NetworkGetStudiesAvailable.StudyName[0]);

        TextView StudyDescription = new TextView(this);
        StudyDescription = (TextView) findViewById(R.id.study_description);
        //StudyDescription.setText(NetworkGetStudiesAvailable.StudyDescription[0]);

        //we display the page of the study
        actionBar.show();

        //Set up the main screen of the study
        LinearLayout item = (LinearLayout)findViewById(R.id.LinearLayout_Layout_List);

        int[] imgs = {R.drawable.blood_drop, R.drawable.blood_drop};
        final View child1 = new ActivityCardBetrack(mContext,"Do you have your period today ?","Yes","No",imgs);

        Button mAnswerYes;
        mAnswerYes = (Button) child1.findViewById(R.id.CardBetrackButtonYes);
        mAnswerYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button Icon = (Button) child1.findViewById(R.id.CardBetrackButton);
                ActivityCardBetrack.InternalSetBackground((Drawable) getResources().getDrawable(R.drawable.button_round_custom_neutral), Icon);

                values.clear();
                values.put(UtilsLocalDataBase.C_USER_PERIOD, "1");
                DatePeriod = sdf.format(new Date());
                values.put(UtilsLocalDataBase.C_USER_DATE, DatePeriod);

                AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_USER);
                Log.d(TAG, "idUser: " + ObjSettingsStudy.getIdUser() + "Period status: " + 1 + " date: " + DatePeriod);
            }
        });
        Button mAnswerNo;
        mAnswerNo = (Button) child1.findViewById(R.id.CardBetrackButtonNo);
        mAnswerNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button Icon = (Button) child1.findViewById(R.id.CardBetrackButton);
                ActivityCardBetrack.InternalSetBackground((Drawable) getResources().getDrawable(R.drawable.button_round_custom_red), Icon);

                values.clear();
                values.put(UtilsLocalDataBase.C_USER_PERIOD, "0");
                DatePeriod = sdf.format(new Date());
                values.put(UtilsLocalDataBase.C_USER_DATE, DatePeriod);

                AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_USER);
                Log.d(TAG, "idUser: " + ObjSettingsStudy.getIdUser() + "Period status: " + 0 + " date: " + DatePeriod);
            }
        });

        item.addView(child1);

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

    public void onButtonClicked(View view) {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();



        //We set up the study
        /*if (SettingsStudy.returnCode.NETWORKERROR == ObjSettingsStudy.Update(this)) {
            NetworkError();
        } else {
            StartStudy();
        }*/
    }

}
