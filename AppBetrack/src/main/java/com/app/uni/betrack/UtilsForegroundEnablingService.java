package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class UtilsForegroundEnablingService extends Service {

    private static void startForeground(Service service) {
        Notification notification;
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            notification = SetNotificationFromO(service);
        }
        else */if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = SetNotificationFromJellyBean(service);
        } else {
            notification = SetNotificationFromIceCream(service);
        }

        service.startForeground(SettingsBetrack.ID_NOTIFICATION_SERVICE, notification);
    }

/*
    @TargetApi(Build.VERSION_CODES.O)  private static Notification SetNotificationFromO(Service service) {
        String CHANNEL_ID = "betrack_channel";
        return new Notification.Builder(service).setChannel(CHANNEL_ID).build();
    }
*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)  private static Notification SetNotificationFromJellyBean(Service service) {
        return new NotificationCompat.Builder(service).build();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)  private static Notification SetNotificationFromIceCream(Service service) {
        return new NotificationCompat.Builder(service).getNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ServiceBetrack.instance == null)
            throw new RuntimeException(ServiceBetrack.class.getSimpleName() + " not running");

        //Set both services to foreground using the same notification id, resulting in just one notification
        startForeground(ServiceBetrack.instance);
        startForeground(this);

        //Cancel this service's notification, resulting in zero notifications
        stopForeground(true);

        //Stop this service so we don't waste RAM
        stopSelf();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
