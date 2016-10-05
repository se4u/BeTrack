package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.view.Display;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cevincent on 6/24/16.
 */
public class ReceiverScreen extends WakefulBroadcastReceiver {

    Handler mHandler;

    public enum StateScreen {
        UNKNOWN, OFF, ON
    }
    public static StateScreen ScreenState = StateScreen.UNKNOWN;

    private SettingsStudy ObjSettingsStudy;

    private static UtilsLocalDataBase localdatabase =  null;

    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    static final String TAG = "ReceiverScreen";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)  private static void CheckScreenStatusFromLollipop(Context context) {
        DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        for (Display display : dm.getDisplays()) {
            if (display.getState() != Display.STATE_OFF) {
                Log.d(TAG, "MANUAL CHECK SCREEN IS ON");
                ScreenState = StateScreen.ON;
                break;
            }
            else
            {
                Log.d(TAG, "MANUAL CHECK SCREEN IS OFF");
                ScreenState = StateScreen.OFF;
                break;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)  private static void CheckScreenStatusFromIceCream(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
        if  (powerManager.isScreenOn()) {
            Log.d(TAG, "MANUAL CHECK SCREEN IS ON");
            ScreenState = StateScreen.ON;
        }
        else
        {
            Log.d(TAG, "MANUAL CHECK SCREEN IS OFF");
            ScreenState = StateScreen.OFF;

        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String ActivityStopDate = "";
        String ActivityStopTime = "";
        ContentValues values = new ContentValues();
        ObjSettingsStudy = SettingsStudy.getInstance(context);

        if (null == mHandler) {
            mHandler = new Handler();
        }

        if (ObjSettingsStudy.getStartSurveyDone() == true) {
            if (null == localdatabase) {
                localdatabase =  new UtilsLocalDataBase(context);
            }

            Log.d(TAG, "Check screen state");
            if (intent.getAction().equals(SettingsBetrack.BROADCAST_CHECK_SCREEN_STATUS)) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    CheckScreenStatusFromLollipop(context);
                } else {
                    CheckScreenStatusFromIceCream(context);
                }
            }
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.d(TAG, "ACTION_SCREEN_ON");
                ScreenState = StateScreen.ON;

            }
            if ((intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) ||
                    (intent.getAction().equals(Intent.ACTION_SHUTDOWN))){

                Log.d(TAG, "ACTION_SCREEN_OFF or ACTION_SHUTDOWN");
                ScreenState = StateScreen.OFF;

            }

            if (ScreenState == StateScreen.OFF) {

                Log.d(TAG, "Screen is off we save the data to the local database");
                //Save the end time
                SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
                SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

                // We should never stop the alarm or from marshallow at some point the whole service goes into a kind of sleep mode
                //CreateTrackApp.StopAlarm(context);
                //Instead we decrease the frequency
                CreateTrackApp.CreateAlarm(context,SettingsBetrack.SAMPLING_RATE_SCREEN_OFF);
                try {
                    SettingsStudy.SemPhoneUsage.acquire();
                    if (IntentServiceTrackApp.ScreenOnStartTime != 0) {
                        int PhoneUsage = ObjSettingsStudy.getPhoneUsage();
                        Log.d(TAG, "Screen is off previous phone usage: " + PhoneUsage + " we add : " + (int) ((System.currentTimeMillis() - IntentServiceTrackApp.ScreenOnStartTime) / 1000));
                        ObjSettingsStudy.setPhoneUsage(PhoneUsage + (int) ((System.currentTimeMillis() - IntentServiceTrackApp.ScreenOnStartTime) / 1000));
                        IntentServiceTrackApp.ScreenOnStartTime = 0;
                    }
                     SettingsStudy.SemPhoneUsage.release();
                } catch (Exception e) {}


                values.clear();
                values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_APPWATCH, false);
                try {
                    ActivityStopDate = values.get(UtilsLocalDataBase.C_APPWATCH_DATESTOP).toString();
                    Log.d(TAG, "End monitoring date: nothing to save");
                } catch (Exception e) {
                    if (null != values) {
                        //Save the stop date
                        ActivityStopDate = sdf.format(new Date());
                        //Save the stop time
                        ActivityStopTime = shf.format(new Date());

                        values.put(UtilsLocalDataBase.C_APPWATCH_DATESTOP, ActivityStopDate);
                        values.put(UtilsLocalDataBase.C_APPWATCH_TIMESTOP, ActivityStopTime);
                        try {
                            this.AccesLocalDB().Update(values, values.getAsLong(UtilsLocalDataBase.C_APPWATCH_ID), UtilsLocalDataBase.TABLE_APPWATCH);
                        } catch (Exception f) {
                            Log.d(TAG, "Nothing to update in the database");
                        }
                        IntentServiceTrackApp.ActivityOnGoing = null;
                        IntentServiceTrackApp.ActivityStartDate = null;
                        IntentServiceTrackApp.ActivityStartTime = null;
                        IntentServiceTrackApp.ActivityStopDate = null;
                        IntentServiceTrackApp.ActivityStopTime = null;

                        Log.d(TAG, "End monitoring date:" + ActivityStopDate + " time:" + ActivityStopTime);
                    }
                }
            }
            else
            {
                CreateTrackApp.CreateAlarm(context, SettingsBetrack.SAMPLING_RATE);
                long DeltaLastTransfer = System.currentTimeMillis() - ObjSettingsStudy.getTimeLastTransfer();;
                if (DeltaLastTransfer >= SettingsBetrack.POSTDATA_SENDING_DELTA)  {
                    Intent msgIntent = new Intent(context, IntentServicePostData.class);
                    //Start the service for sending the data to the remote server
                    context.startService(msgIntent);
                }

                DeltaLastTransfer = System.currentTimeMillis() - ObjSettingsStudy.getTimeLastGPS();
                if ((DeltaLastTransfer >= SettingsBetrack.TRACKGPS_DELTA) || (DeltaLastTransfer < 0))  {
                    ReceiverGPSChange.StartGPS(context);
                }
            }
        }
    }
}
