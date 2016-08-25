package com.app.uni.betrack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by cedoctet on 21/08/2016.
 */
public class ReceiverAlarmNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Trigger a notification
        CreateNotification.Create(context);
        //Restart for a new notification in 24 hours
        CreateNotification.ResetAlarm(context);

    }

}
