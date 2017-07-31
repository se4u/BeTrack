package com.app.uni.betrack;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Created by cvincent on 29/07/17.
 */

public class UtilsNetworkStatus {

    public static enum ConnectionState {
        NONE,
        WIFI,
        LTE,
    };

    public  static ConnectionState hasNetworkConnection(ConnectivityManager cm) {

        ConnectionState NetworkState = ConnectionState.NONE;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    //Log.d(TAG, "hasNetworkConnection: WIFI");
                    NetworkState = ConnectionState.WIFI;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    //Log.d(TAG, "hasNetworkConnection: LTE/3G");
                    NetworkState = ConnectionState.LTE;
                }
            }else {
                NetworkInfo[] netInfo = cm.getAllNetworkInfo();
                for (NetworkInfo ni : netInfo) {
                    if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                        if (ni.isConnected()){
                            //Log.d(TAG, "hasNetworkConnection: WIFI");
                            NetworkState = ConnectionState.WIFI;
                        }

                    if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                        if (ni.isConnected()) {
                            //Log.d(TAG, "hasNetworkConnection: LTE/3G");
                            NetworkState = ConnectionState.LTE;
                        }
                }
            }
        }
        finally {
            if (ConnectionState.NONE == NetworkState) {
                //Log.d(TAG, "hasNetworkConnection: nope");
            }
            return NetworkState;
        }
    }
}
