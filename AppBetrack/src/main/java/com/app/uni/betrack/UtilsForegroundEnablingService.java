package com.app.uni.betrack;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

public class UtilsForegroundEnablingService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ServiceTrack.instance == null)
            throw new RuntimeException(ServiceTrack.class.getSimpleName() + " not running");

        //Set both services to foreground using the same notification id, resulting in just one notification
        startForeground(ServiceTrack.instance);
        startForeground(this);

        //Cancel this service's notification, resulting in zero notifications
        stopForeground(true);

        //Stop this service so we don't waste RAM
        stopSelf();

        return START_NOT_STICKY;
    }

    private static final int NOTIFICATION_ID = 10;

    private static void startForeground(Service service) {
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification.Builder(service).build();
        } else {
            notification = new Notification.Builder(service).getNotification();
        }

        service.startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
