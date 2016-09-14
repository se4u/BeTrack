package com.app.uni.betrack;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;

import com.commonsware.cwac.locpoll.LocationPollerResult;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cedoctet on 24/08/2016.
 */
public class ReceiverAlarmTrackGPS extends BroadcastReceiver {
    static final String TAG = "ReceiverAlarmTrackGPS";
    private ContentValues values = new ContentValues();
    private UtilsLocalDataBase localdatabase = null;
    public UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }
    private SettingsStudy ObjSettingsStudy;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        ObjSettingsStudy = SettingsStudy.getInstance(context);
        Bundle b=intent.getExtras();

        if (null == localdatabase) {
            localdatabase =  new UtilsLocalDataBase(context);
        }

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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");
        double lat = 0;
        double lng = 0;
        String GpsDate = null;
        String GpsTime = null;
        if (null != location) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            values.clear();
            values.put(UtilsLocalDataBase.C_GPS_ID, ObjSettingsStudy.getIdUser());
            values.put(UtilsLocalDataBase.C_GPS_LATTITUDE, lat);
            values.put(UtilsLocalDataBase.C_GPS_LONGITUDE, lng);
            GpsDate = sdf.format(new Date());
            values.put(UtilsLocalDataBase.C_GPS_DATE, GpsDate);
            GpsTime = shf.format(new Date());
            values.put(UtilsLocalDataBase.C_GPS_TIME, GpsTime);

            AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_GPS);

            Log.d(TAG, "GPS position lat: " + lat + " lng: " + lng);
        }
        else {
            Log.d(TAG, "GPS location not available");
        }
    }
}
