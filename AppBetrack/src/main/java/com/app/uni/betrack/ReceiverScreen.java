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

    static final String TAG = "ReceiverScreen";
    private static UtilsLocalDataBase localdatabase =  null;
    Handler mHandler;
    private SettingsStudy ObjSettingsStudy;
    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }
    private UtilsScreenState screenstate = null;
    private UtilsScreenState AccessScreenState() {return screenstate; }

    @Override
    public void onReceive(Context context, Intent intent) {
        String ActivityStartDate = "";
        String ActivityStartTime = "";
        String ActivityStopDate = "";
        String ActivityStopTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");
        long DeltaLastTransfer;

        ContentValues values = new ContentValues();
        ObjSettingsStudy = SettingsStudy.getInstance(context);

        if (null == mHandler) {
            mHandler = new Handler();
        }

        if (null == screenstate) {
            screenstate =  new UtilsScreenState(context);
        }


        if ((ObjSettingsStudy.getStartSurveyDone() == true) && (ObjSettingsStudy.getEndSurveyDone() == false)) {
            if (null == localdatabase) {
                localdatabase =  new UtilsLocalDataBase(context);
            }

            Log.d(TAG, "Check screen state");
            if ((intent.getAction().equals(Intent.ACTION_SCREEN_ON)) || (intent.getAction().equals(Intent.ACTION_USER_PRESENT))) {
                if ((intent.getAction().equals(Intent.ACTION_SCREEN_ON))) {
                    Log.d(TAG, "ACTION_SCREEN_ON");
                } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                    Log.d(TAG, "ACTION_USER_PRESENT");
                } else {
                    Log.d(TAG, "ACTION_UNKNOWN, should never happen!!!");
                }
                try {
                    SettingsStudy.SemScreenOn.acquire();
                    if (SettingsStudy.getStartScreenOn() == 0) {
                        SettingsStudy.setStartScreenOn(System.currentTimeMillis());
                        Log.d(TAG, "Screen ON saved " + System.currentTimeMillis());
                    }

                } catch (Exception eScreenOn) {
                }
                finally {
                    SettingsStudy.SemScreenOn.release();
                }

                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {

                    //Screen is on we save this status in the databse
                    //Save when the screen was switched off
                    values.clear();
                    //Save the date
                    ActivityStartDate = sdf.format(new Date());
                    //Save the time
                    ActivityStartTime = shf.format(new Date());

                    if ((intent.getAction().equals(Intent.ACTION_SCREEN_ON))) {
                        Log.d(TAG, "SCREEN_SWITCHED_ON saved in database " + ActivityStartDate + " " + ActivityStartTime);
                        values.put(UtilsLocalDataBase.C_PHONE_USAGE_STATE, SettingsBetrack.SCREEN_SWITCHED_ON);
                        AccessScreenState().UtilsSetSavedScreenState(UtilsScreenState.StateScreen.ON);
                    } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                        Log.d(TAG, "SCREEN_USER_PRESENT saved in database " + ActivityStartDate + " " + ActivityStartTime);
                        values.put(UtilsLocalDataBase.C_PHONE_USAGE_STATE, SettingsBetrack.SCREEN_USER_PRESENT);
                        AccessScreenState().UtilsSetSavedScreenState(UtilsScreenState.StateScreen.UNLOCKED);
                    } else {
                        Log.d(TAG, "SCREEN_BETRACK_ERROR saved in database " + ActivityStartDate + " " + ActivityStartTime);
                        values.put(UtilsLocalDataBase.C_PHONE_USAGE_STATE, SettingsBetrack.SCREEN_BETRACK_ERROR);
                    }

                    values.put(UtilsLocalDataBase.C_PHONE_USAGE_DATE, ActivityStartDate);
                    values.put(UtilsLocalDataBase.C_PHONE_USAGE_TIME, ActivityStartTime);
                    try {
                        AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_PHONE_USAGE);
                    } catch (Exception f) {
                        Log.d(TAG, "Nothing to update in the database");
                    }

                    DeltaLastTransfer = System.currentTimeMillis() - ObjSettingsStudy.getTimeLastTransfer();

                    if (DeltaLastTransfer >= SettingsBetrack.POSTDATA_SENDING_DELTA)  {
                        Intent msgIntent = new Intent(context, IntentServicePostData.class);
                        //Start the service for sending the data to the remote server
                        context.startService(msgIntent);
                        Log.d(TAG, "More than one hour without transfering any data");
                    }
                }


                DeltaLastTransfer = System.currentTimeMillis() - ObjSettingsStudy.getTimeLastGPS();
                if ((DeltaLastTransfer >= SettingsBetrack.TRACKGPS_DELTA) || (DeltaLastTransfer < 0)) {
                    ReceiverGPSChange.StartGPS(context);
                }

                CreateTrackApp.CreateAlarm(context, SettingsBetrack.SAMPLING_RATE);


            }

            if ((intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) ||
                    (intent.getAction().equals(Intent.ACTION_SHUTDOWN))) {

                AccessScreenState().UtilsSetSavedScreenState(UtilsScreenState.StateScreen.OFF);

                Log.d(TAG, "Screen is off we save the data to the local database");
                //Save the end time

                if ((SettingsBetrack.STUDY_ENABLE_CONTINUOUS_TRACKING == false) || (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N)) {
                    CreateTrackApp.StopAlarm(context);
                }

                //Clear the base time used for the average computation
                IntentServiceTrackApp.baseTime = 0;

                //Save when the screen was switched off
                values.clear();

                //Save the date
                ActivityStopDate = sdf.format(new Date());
                //Save the time
                ActivityStopTime = shf.format(new Date());

                values.put(UtilsLocalDataBase.C_PHONE_USAGE_STATE, SettingsBetrack.SCREEN_SWITCHED_OFF);
                values.put(UtilsLocalDataBase.C_PHONE_USAGE_DATE, ActivityStopDate);
                values.put(UtilsLocalDataBase.C_PHONE_USAGE_TIME, ActivityStopTime);
                try {
                    AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_PHONE_USAGE);
                    Log.d(TAG, "SCREEN_SWITCHED_OFF saved in database " + ActivityStopDate + " " + ActivityStopTime);
                } catch (Exception f) {
                    Log.d(TAG, "Nothing to update in the database");
                }


                try {
                    SettingsStudy.SemScreenOn.acquire();
                    long ScreenOn = SettingsStudy.getDurationScreenOn();
                    Log.d(TAG, "Duration Screen On:" + ScreenOn);
                    Log.d(TAG, "Duration last screen On:" + System.currentTimeMillis() + "-" +      SettingsStudy.getStartScreenOn() + "/1000= " + (System.currentTimeMillis() - SettingsStudy.getStartScreenOn())/1000);
                    ScreenOn += (System.currentTimeMillis() - SettingsStudy.getStartScreenOn())/1000;
                    Log.d(TAG, "New duration screen on:" + ScreenOn);
                    SettingsStudy.setDurationScreenOn(ScreenOn);
                    SettingsStudy.setStartScreenOn(0);


                } catch (Exception eScreenOn) {
                }
                finally {
                    SettingsStudy.SemScreenOn.release();
                }


                //Save in the database if needed when we stop monitoring an application
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

                        try {
                            Log.d(TAG, "RecScreenSem1 try acquire");
                            SettingsStudy.SemAppWatchMonitor.acquire();
                            Log.d(TAG, "RecScreenSem1 acquired");
                            if (SettingsStudy.getAppWatchId() != -1) {
                                int TimeWatched = (int) ((System.currentTimeMillis() - SettingsStudy.getAppWatchStartTime()) / 1000);
                                Log.d(TAG, "TimeWatched: " + TimeWatched + " AppWatchId: " + ObjSettingsStudy.getApplicationsToWatch().get(SettingsStudy.getAppWatchId()));
                                ObjSettingsStudy.setAppTimeWatched(SettingsStudy.getAppWatchId(), ObjSettingsStudy.getApplicationsToWatch().size(), TimeWatched);
                                SettingsStudy.setAppWatchId(-1);
                            }
                        } catch (Exception eWatchId) {}
                        finally {
                            SettingsStudy.SemAppWatchMonitor.release();
                            Log.d(TAG, "RecScreenSem1 try released");
                        }

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
        }
    }

}
