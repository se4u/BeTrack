package com.app.uni.betrack;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.io.IOException;

/**
 * Created by cedoctet on 30/09/2016.
 */
public class ReceiverNetworkChange  extends WakefulBroadcastReceiver {

    private SettingsStudy ObjSettingsStudy;
    public static int TIME_OUT = 5000;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        ObjSettingsStudy = SettingsStudy.getInstance(context);

        if ((ObjSettingsStudy.getStartSurveyDone() == true) && (ObjSettingsStudy.getEndSurveyDone() == false)) {
            long DeltaLastTransfer = System.currentTimeMillis() - ObjSettingsStudy.getTimeLastTransfer();
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            if (true == isConnected) {
                if (DeltaLastTransfer >= SettingsBetrack.POSTDATA_SENDING_DELTA) {
                    if (((ObjSettingsStudy.getEndSurveyTransferred() == SettingsStudy.EndStudyTranferState.NOT_YET) ||
                                    (ObjSettingsStudy.getEndSurveyTransferred() == SettingsStudy.EndStudyTranferState.ERROR))) {
                        Intent msgIntent = new Intent(context, IntentServicePostData.class);
                        //Start the service for sending the data to the remote server
                        context.startService(msgIntent);
                    }
                }
            }
        }
    }
}
