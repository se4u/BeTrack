package com.app.uni.betrack;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by cedoctet on 21/08/2016.
 */
public class ReceiverAlarmPostData extends BroadcastReceiver {
    private static final String TAG = "AlarmPostData";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try {
            //Make sure that the service is just running one time
            CreatePostData.SemUpdateServer.acquire();
        } catch (Exception e) {

        } finally {
            Intent msgIntent = new Intent(context, IntentServicePostData.class);

            //Start the service for sending the data to the remote server
            context.startService(msgIntent);

            if (false == CreatePostData.InternalFastCheck) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
                {
                    CreatePostData.alarmMgr.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                            SettingsBetrack.POSTDATA_SENDING_DELTA, CreatePostData.alarmIntent);
                }
                else
                {
                    CreatePostData.alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                            SettingsBetrack.POSTDATA_SENDING_DELTA, CreatePostData.alarmIntent);
                }
            } else {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
                {
                    CreatePostData.alarmMgr.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                            SettingsBetrack.POSTDATA_SENDING_DELTA_FASTCHECK, CreatePostData.alarmIntent);
                }
                else
                {
                    CreatePostData.alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                            SettingsBetrack.POSTDATA_SENDING_DELTA_FASTCHECK, CreatePostData.alarmIntent);
                }
            }
        }

    }

}
