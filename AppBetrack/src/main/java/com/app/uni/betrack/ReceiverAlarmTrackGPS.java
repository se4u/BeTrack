package com.app.uni.betrack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.commonsware.cwac.locpoll.LocationPollerResult;

/**
 * Created by cedoctet on 24/08/2016.
 */
public class ReceiverAlarmTrackGPS extends BroadcastReceiver {
    static final String TAG = "ReceiverAlarmTrackGPS";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle b=intent.getExtras();

        LocationPollerResult locationResult = new LocationPollerResult(b);

        Location loc=locationResult.getLocation();

        if (loc==null) {
            loc=locationResult.getLastKnownLocation();

            if (loc==null) {
                Log.d(TAG, "GPS location not available");
            }
            else {
                saveLocation(loc);
            }
        }
        else {
            saveLocation(loc);
        }

        //Set next alarm
        CreateTrackGPS.CreateAlarm(context);

    }

    private void saveLocation(Location location) {
        double lat = 0;
        double lng = 0;
        if (null != location) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            Log.d(TAG, "GPS position lat: " + lat + " lng: " + lng);
        }
        else {
            Log.d(TAG, "GPS location not available");
        }
    }
}
