package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by cevincent on 6/24/16.
 */
public class ReceiverStartTracking extends WakefulBroadcastReceiver {
    static final String TAG = "ReceiverStartTracking";

    public static boolean startTrackingRunning = false;

    private SettingsStudy ObjSettingsStudy;
    private SettingsBetrack ObjSettingsBetrack;

    @Override
    public void onReceive(Context context, Intent intent) { //

        Log.d(TAG, "onReceived");
        boolean next10MinutesTriggered = false;
        long currentTime =System.currentTimeMillis();

        ObjSettingsStudy = SettingsStudy.getInstance(context);

        ReceiverScreen.ScreenState = ReceiverScreen.StateScreen.ON;

        try {
            SettingsStudy.SemScreenOn.acquire();
            if (SettingsStudy.getStartScreenOn() == 0) {
                SettingsStudy.setStartScreenOn(currentTime);
                Log.d(TAG, "Screen ON saved " + currentTime);
            }

        } catch (Exception eScreenOn) {
        }
        finally {
            SettingsStudy.SemScreenOn.release();
        }

        if (null == ObjSettingsStudy)  {
            ObjSettingsStudy = SettingsStudy.getInstance(context);
        }

        //Read the preferences
        if (ObjSettingsBetrack == null) {
            ObjSettingsBetrack = SettingsBetrack.getInstance();
            ObjSettingsBetrack.Update(context);
        }

        Bundle results = getResultExtras(true);

        String id = intent.getStringExtra(SettingsBetrack.BROADCAST_ARG_MANUAL_START);
        //The system is just started
        if(id == null) {
            Log.d(TAG, "The system is just started");

            //Since we were off we check if we didn't miss the last notification NbrOfNotificationToDo

            if ( (true == ObjSettingsBetrack.GetStudyNotification())
                    && (ObjSettingsStudy.getTimeNextNotification() - currentTime < 0)
                    && (ObjSettingsStudy.getEndSurveyTransferred() == SettingsStudy.EndStudyTranferState.NOT_YET)
                    && (ObjSettingsStudy.getStartSurveyDone() == true)) {
                Log.d(TAG, "We missed the notification so we trigger it manually");

                //We check if the time is consistent if the counter
                //We adjust the NbrOfNotificationToDo counter depending of the time we are switched on
                // "09:30:00","13:00:00" NbrOfNotificationToDo mod 3 should be equal to 0 we trigger the notification in a time between 0s and 10 minutes
                // "13:00:00","13:30:00" NbrOfNotificationToDo mod 3 should be equal to 0 we trigger the next notification (btw 13:30 and 17h00)
                // "17:30:00","21:00:00" NbrOfNotificationToDo mod 3 should be equal to 1 we trigger the notification in a time between 0s and 10 minutes
                // "21:00:00","09:30:00" NbrOfNotificationToDo mod 3 should be equal to 1 we trigger the next notification (btw 09:30 and 13h00)
                // "13:30:00","17:00:00" NbrOfNotificationToDo mod 3 should be equal to 2 we trigger the notification in a time between 0s and 10 minutes
                // "17:00:00","17:30:00" NbrOfNotificationToDo mod 3 should be equal to we trigger the next notification (btw 17:30 and 21h00)

                int TimeWindowWanted = UtilsGetNotification.returnIndex(currentTime);
                if (TimeWindowWanted != ObjSettingsStudy.getNbrOfNotificationToDo() % UtilsGetNotification.getNbrPerDay()) {
                    //We are not consistent, we adjust the number of notification
                    int NbrOfNotificationToDo = ObjSettingsStudy.getNbrOfNotificationToDo();
                    int TimeWindowSaved = NbrOfNotificationToDo % UtilsGetNotification.getNbrPerDay();
                    int AdjustTimeWindow = 0;

                    AdjustTimeWindow = adjustTimeWindow(TimeWindowWanted, TimeWindowSaved);

                    ObjSettingsStudy.setNbrOfNotificationToDo(ObjSettingsStudy.getNbrOfNotificationToDo() + AdjustTimeWindow);

                }

                //Check if we are btw 2 notifications?
                if (UtilsGetNotification.CheckInBtwNotifications(currentTime) == false) {
                    //Nope we trigger the next notification in the next 10 minutes
                    CreateNotification.CreateAlarm(context,
                            ObjSettingsBetrack.GetStudyNotification(),
                            UtilsGetNotification.next10minutes(currentTime),
                            false);
                    next10MinutesTriggered = true;
                }
                else {
                    //We missed that notification
                    ObjSettingsStudy.setNbrOfNotificationToDo(ObjSettingsStudy.getNbrOfNotificationToDo() - 1);
                    //The next notification will be triggered in a couple of lines
                }

            } else {
                Log.d(TAG, "notification was not missed we go on with the startup");
                int TimeWindowWanted = UtilsGetNotification.returnIndex(currentTime);
                if (TimeWindowWanted != ObjSettingsStudy.getNbrOfNotificationToDo() % UtilsGetNotification.getNbrPerDay()) {
                    //We are not consistent, we adjust the number of notification
                    int NbrOfNotificationToDo = ObjSettingsStudy.getNbrOfNotificationToDo();
                    int TimeWindowSaved = NbrOfNotificationToDo % UtilsGetNotification.getNbrPerDay();
                    int AdjustTimeWindow = 0;

                    AdjustTimeWindow = adjustTimeWindow(TimeWindowWanted, TimeWindowSaved);

                    ObjSettingsStudy.setNbrOfNotificationToDo(ObjSettingsStudy.getNbrOfNotificationToDo() + AdjustTimeWindow);

                }
            }
        }


        if (ObjSettingsStudy.getStartSurveyDone() == true) {

            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                context.startService(new Intent(context, ServiceBetrack.class));
            }

            if (next10MinutesTriggered == false) {
                CreateNotification.CreateAlarm(context,
                        ObjSettingsBetrack.GetStudyNotification(),
                        UtilsGetNotification.next(
                                ObjSettingsStudy.getNbrOfNotificationToDo() % UtilsGetNotification.getNbrPerDay()
                        ),
                        false);
            }

            CreateTrackApp.CreateAlarm(context, SettingsBetrack.SAMPLING_RATE);

            startTrackingRunning = true;

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if(!hasPermission(context)){
                    Log.d(TAG, "No permission !");
                }
            }
        }
    }

    private int adjustTimeWindow(int TimeWindowWanted, int TimeWindowSaved) {
        int AdjustTimeWindow = 0;

        //0 Morning
        //1 Evening
        //2 Afternoon

        //We are in the morning (TimeWindowWanted = 0) but we should be in the afternoon (TimeWindowSaved = 2) we adjust the window location
        if ((TimeWindowWanted == 0) && (TimeWindowSaved == 2)) {
            AdjustTimeWindow = 1;
        }
        //We are in the evening (TimeWindowWanted = 1) but we should be in the morning (TimeWindowSaved = 0) we adjust the window location
        else if ((TimeWindowWanted == 1) && (TimeWindowSaved == 0)) {
            AdjustTimeWindow = 1;
        }
        //We are in the afternoon (TimeWindowWanted = 2) but we should be in the evening (TimeWindowSaved = 1) we adjust the window location
        else if ((TimeWindowWanted == 2) && (TimeWindowSaved == 1)) {
            AdjustTimeWindow = 1;
        }
        //We are in the morning (TimeWindowWanted = 0) but we should be in the evening (TimeWindowSaved = 1) we adjust the window location
        else if ((TimeWindowWanted == 0) && (TimeWindowSaved == 1)) {
            AdjustTimeWindow = 2;
        }
        //We are in the evening (TimeWindowWanted = 1) but we should be in the afternoon (TimeWindowSaved = 2) we adjust the window location
        else if ((TimeWindowWanted == 1) && (TimeWindowSaved == 2)) {
            AdjustTimeWindow = 2;
        }
        //We are in the afternoon (TimeWindowWanted = 2) but we should be in the morning (TimeWindowSaved = 0) we adjust the window location
        else if ((TimeWindowWanted == 2) && (TimeWindowSaved == 0)) {
            AdjustTimeWindow = 2;
        }
        return AdjustTimeWindow;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) private boolean hasPermission(Context context) {

        AppOpsManager appOps = (AppOpsManager)
                context.getSystemService(context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }
}
