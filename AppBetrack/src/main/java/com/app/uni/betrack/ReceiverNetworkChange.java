package com.app.uni.betrack;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by cedoctet on 30/09/2016.
 */
public class ReceiverNetworkChange  extends WakefulBroadcastReceiver {

    private SettingsStudy ObjSettingsStudy;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        ObjSettingsStudy = SettingsStudy.getInstance(context);

        long DeltaLastTransfer = System.currentTimeMillis() - ObjSettingsStudy.getTimeLastTransfer();
        if ((DeltaLastTransfer >= SettingsBetrack.POSTDATA_SENDING_DELTA) ||
                ((ObjSettingsStudy.getEndSurveyDone()) && (!ObjSettingsStudy.getEndSurveyTransferred())))  {
            if (IntentServicePostData.SemPostData.tryAcquire()) {
                Intent msgIntent = new Intent(context, IntentServicePostData.class);
                //Start the service for sending the data to the remote server
                context.startService(msgIntent);
            }
        }
    }
}
