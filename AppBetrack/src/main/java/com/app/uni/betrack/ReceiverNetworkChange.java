package com.app.uni.betrack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

/**
 * Created by cedoctet on 30/09/2016.
 */
public class ReceiverNetworkChange  extends BroadcastReceiver {

    private static final String LOCK_NAME_STATIC = "com.app.uni.betrack.wakelock.receivernetworkchange";

    private SettingsStudy ObjSettingsStudy;

    private static volatile PowerManager.WakeLock lockStatic;

    synchronized private static PowerManager.WakeLock getLock(Context context) {
        if (lockStatic == null) {
            PowerManager mgr = (PowerManager) context.getApplicationContext()
                    .getSystemService(Context.POWER_SERVICE);

            lockStatic = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    LOCK_NAME_STATIC);
            lockStatic.setReferenceCounted(true);
        }
        return (lockStatic);
    }
    @Override
    public void onReceive(final Context context, final Intent intent) {
        getLock(context).acquire();
        ObjSettingsStudy = SettingsStudy.getInstance(context);

        long DeltaLastTransfer = System.currentTimeMillis() - ObjSettingsStudy.getTimeLastTransfer();
        if (DeltaLastTransfer >= SettingsBetrack.POSTDATA_SENDING_DELTA)  {
            Intent msgIntent = new Intent(context, IntentServicePostData.class);
            //Start the service for sending the data to the remote server
            context.startService(msgIntent);
        }
        getLock(context).release();
    }
}
