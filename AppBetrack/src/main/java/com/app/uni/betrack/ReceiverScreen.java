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
    public static StateScreen ScreenState = StateScreen.UNKNOWN;
    private static UtilsLocalDataBase localdatabase =  null;
    Handler mHandler;
    private SettingsStudy ObjSettingsStudy;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)  public static void CheckScreenStatusFromLollipop(Context context) {
        DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        for (Display display : dm.getDisplays()) {
            if (display.getState() != Display.STATE_OFF) {
                //Log.d(TAG, "MANUAL CHECK SCREEN IS ON");
                ScreenState = StateScreen.ON;
                break;
            }
            else
            {
                //Log.d(TAG, "MANUAL CHECK SCREEN IS OFF");
                ScreenState = StateScreen.OFF;
                break;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)  private static void CheckScreenStatusFromIceCream(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
        if  (powerManager.isScreenOn()) {
            //Log.d(TAG, "MANUAL CHECK SCREEN IS ON");
            ScreenState = StateScreen.ON;
        }
        else
        {
            //Log.d(TAG, "MANUAL CHECK SCREEN IS OFF");
            ScreenState = StateScreen.OFF;

        }
    }

    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String ActivityStartDate = "";
        String ActivityStartTime = "";
        String ActivityStopDate = "";
        String ActivityStopTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

        ContentValues values = new ContentValues();
        ObjSettingsStudy = SettingsStudy.getInstance(context);

        if (null == mHandler) {
            mHandler = new Handler();
        }

        if ((ObjSettingsStudy.getStartSurveyDone() == true) && (ObjSettingsStudy.getEndSurveyDone() == false)) {
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
                long DeltaLastTransfer;
                Log.d(TAG, "ACTION_SCREEN_ON");

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

                ScreenState = StateScreen.ON;
                //Screen is on and not locked we save this status in the databse
                //Save when the screen was switched off
                values.clear();
                //Save the date
                ActivityStartDate = sdf.format(new Date());
                //Save the time
                ActivityStartTime = shf.format(new Date());
                values.put(UtilsLocalDataBase.C_PHONE_USAGE_STATE, 1);
                values.put(UtilsLocalDataBase.C_PHONE_USAGE_DATE, ActivityStartDate);
                values.put(UtilsLocalDataBase.C_PHONE_USAGE_TIME, ActivityStartTime);
                try {
                    AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_PHONE_USAGE);
                    Log.d(TAG, "Screen On saved in database " + ActivityStartDate + " " + ActivityStartTime);
                } catch (Exception f) {
                    Log.d(TAG, "Nothing to update in the database");
                }

            }

            if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                Log.d(TAG, "ACTION_USER_PRESENT");

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

                // We should never stop the alarm or from marshallow at some point the whole service goes into a kind of sleep mode
                //CreateTrackApp.StopAlarm(context);

                //Instead we decrease the frequency
                CreateTrackApp.CreateAlarm(context,SettingsBetrack.SAMPLING_RATE_SCREEN_OFF);

                //Clear the base time used for the average computation
                IntentServiceTrackApp.baseTime = 0;

                //Save when the screen was switched off
                values.clear();

                //Save the date
                ActivityStopDate = sdf.format(new Date());
                //Save the time
                ActivityStopTime = shf.format(new Date());

                values.put(UtilsLocalDataBase.C_PHONE_USAGE_STATE, 0);
                values.put(UtilsLocalDataBase.C_PHONE_USAGE_DATE, ActivityStopDate);
                values.put(UtilsLocalDataBase.C_PHONE_USAGE_TIME, ActivityStopTime);
                try {
                    AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_PHONE_USAGE);
                    Log.d(TAG, "Screen Off saved in database " + ActivityStopDate + " " + ActivityStopTime);
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
            else
            {
                CreateTrackApp.CreateAlarm(context, SettingsBetrack.SAMPLING_RATE);
                long DeltaLastTransfer = System.currentTimeMillis() - ObjSettingsStudy.getTimeLastTransfer();
                if (DeltaLastTransfer >= SettingsBetrack.POSTDATA_SENDING_DELTA)  {
                    Intent msgIntent = new Intent(context, IntentServicePostData.class);
                    //Start the service for sending the data to the remote server
                    context.startService(msgIntent);
                }
            }
        }
    }

    public enum StateScreen {
        UNKNOWN, OFF, ON
    }
}
