package com.app.uni.betrack;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;


public class BeTrackActivity extends AppCompatActivity  implements VerticalStepperForm {

    private static final String TAG = "Status";

    public static ProgressDialog dialog;
    public static ActionBar actionBar;

    private Context mContext;
    private LocalDataBase localdatabase = new LocalDataBase(this);
    public LocalDataBase AccesLocalDB()
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

    public InfoStudy ObjInfoStudy = new InfoStudy();
    private SettingsBetrack ObjSettingsBetrack;

    private int id_period;
    private VerticalStepperFormLayout verticalStepperForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        mContext = this;

        ObjSettingsBetrack = new SettingsBetrack(prefs, this);
        actionBar = getSupportActionBar();
        actionBar.hide();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_logo_padding);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_betrack);

        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //In case it's an Huawei phone we ask the user to put our app in the protected list
        ifHuaweiAlert();

        notificationManager.cancel(SettingsBetrack.NOTIFICATION_ID);

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


        String[] mySteps = {"Name", "Email", "Phone Number"};
        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);

        // Finding the view
      //verticalStepperForm = (VerticalStepperFormLayout) findViewById(R.id.vertical_stepper_form);

        // Setting up and initializing the form
      /*VerticalStepperFormLayout.Builder.newInstance(verticalStepperForm, mySteps, this, this)
                .primaryColor(colorPrimary)
                .primaryDarkColor(colorPrimaryDark)
                .displayBottomNavigation(false) // It is true by default, so in this case this line is not necessary
                n/*it();*/

        LinearLayout item = (LinearLayout)findViewById(R.id.LinearLayout_Layout_List);

        int[] imgs = {R.drawable.blood_drop, R.drawable.blood_drop};
        final View child1 = new CardBetrack(mContext,"Do you have your period today ?","Yes","No",imgs);

        Button mAnswerYes;
        mAnswerYes = (Button) child1.findViewById(R.id.CardBetrackButtonYes);
        mAnswerYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button Icon = (Button) child1.findViewById(R.id.CardBetrackButton);
                CardBetrack.InternalSetBackground((Drawable)getResources().getDrawable(R.drawable.button_round_custom_neutral), Icon);

                values.clear();
                values.put(LocalDataBase.C_USER_PERIOD, "1");
                DatePeriod = sdf.format(new Date());
                values.put(LocalDataBase.C_USER_DATE, DatePeriod);

                AccesLocalDB().insertOrIgnore(values, LocalDataBase.TABLE_USER);
                Log.d(TAG, "idUser: " + InfoStudy.IdUser + "Period status: " + 1 + " date: " + DatePeriod);
            }
        });
        Button mAnswerNo;
        mAnswerNo = (Button) child1.findViewById(R.id.CardBetrackButtonNo);
        mAnswerNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button Icon = (Button) child1.findViewById(R.id.CardBetrackButton);
                CardBetrack.InternalSetBackground((Drawable)getResources().getDrawable(R.drawable.button_round_custom_red), Icon);

                values.clear();
                values.put(LocalDataBase.C_USER_PERIOD, "0");
                DatePeriod = sdf.format(new Date());
                values.put(LocalDataBase.C_USER_DATE, DatePeriod);

                AccesLocalDB().insertOrIgnore(values, LocalDataBase.TABLE_USER);
                Log.d(TAG, "idUser: " + InfoStudy.IdUser + "Period status: " + 0 + " date: " + DatePeriod);
            }
        });

        item.addView(child1);
    }

    @Override
    public View createStepContentView(int stepNumber) {
        View view = null;
        switch (stepNumber) {
            case 0:
                view = createNameStep();
                break;
            case 1:
                view = createEmailStep();
                break;
            case 2:
                view = createPhoneNumberStep();
                break;
        }
        return view;
    }

    private View createNameStep() {
        int[] imgs = {R.drawable.ic_girl_1, R.drawable.blood_drop};
        return new CardBetrack(mContext,"Do you have your period today ?","Yes","No",imgs);
    }

    private View createEmailStep() {
        // In this case we generate the view by inflating a XML file
        int[] imgs = {R.drawable.ic_girl_1, R.drawable.blood_drop};
        return  new CardBetrack(mContext,"Do you have your period today ?",imgs);
    }

    private View createPhoneNumberStep() {
        // In this case we generate the view by inflating a XML file
        int[] imgs = {R.drawable.ic_girl_1, R.drawable.blood_drop};
        return  new CardBetrack(mContext,"Do you have your period today ?",imgs);
    }

    @Override
    public void onStepOpening(int stepNumber) {
        switch (stepNumber) {
            case 0:
                verticalStepperForm.setStepAsCompleted(0);
                break;
            case 1:
                verticalStepperForm.setStepAsCompleted(1);
                break;
            case 2:
                // As soon as the phone number step is open, we mark it as completed in order to show the "Continue"
                // button (We do it because this field is optional, so the user can skip it without giving any info)
                verticalStepperForm.setStepAsCompleted(2);
                // In this case, the instruction above is equivalent to:
                // verticalStepperForm.setActiveStepAsCompleted();
                break;
        }
    }

    @Override
    public void sendData() {

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

    private void ifHuaweiAlert() {
        final SharedPreferences settings = getSharedPreferences("ProtectedApps", MODE_PRIVATE);
        final String saveIfSkip = "skipProtectedAppsMessage";
        boolean skipMessage = settings.getBoolean(saveIfSkip, false);
        if (!skipMessage) {
            String title =  this.getString(R.string.huawei_title);
            String message =  this.getString(R.string.huawei_desc);
            String check =  this.getString(R.string.huawei_check);
            String buttonpositive =  this.getString(R.string.huawei_positive_button);

            final SharedPreferences.Editor editor = settings.edit();
            Intent intent = new Intent();
            intent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            if (isCallable(intent)) {
                final AppCompatCheckBox dontShowAgain = new AppCompatCheckBox(this);
                dontShowAgain.setText(check);
                dontShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        editor.putBoolean(saveIfSkip, isChecked);
                        editor.apply();
                    }
                });


                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(title)
                        .setMessage(String.format("%s " + message + ".%n", getString(R.string.app_name)))
                        .setView(dontShowAgain)
                        .setPositiveButton(buttonpositive, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                huaweiProtectedApps();
                            }
                        })
                        .show();
            } else {
                editor.putBoolean(saveIfSkip, true);
                editor.apply();
            }
        }
    }

    private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void huaweiProtectedApps() {
        try {
            String cmd = "am start -n com.huawei.systemmanager/.optimize.process.ProtectActivity";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                cmd += " --user " + getUserSerial();
            }
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ignored) {
        }
    }

    private String getUserSerial() {
        //noinspection ResourceType
        Object userManager = getSystemService("user");
        if (null == userManager) return "";

        try {
            Method myUserHandleMethod = android.os.Process.class.getMethod("myUserHandle", (Class<?>[]) null);
            Object myUserHandle = myUserHandleMethod.invoke(android.os.Process.class, (Object[]) null);
            Method getSerialNumberForUser = userManager.getClass().getMethod("getSerialNumberForUser", myUserHandle.getClass());
            Long userSerial = (Long) getSerialNumberForUser.invoke(userManager, myUserHandle);
            if (userSerial != null) {
                return String.valueOf(userSerial);
            } else {
                return "";
            }
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ignored) {
        }
        return "";
    }
}
