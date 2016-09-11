package com.app.uni.betrack;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by cedoctet on 23/08/2016.
 */
public class IntentServiceTrackGPS  extends IntentService implements LocationListener {

    static final String TAG = "IntentServiceTrackGPS";

    private static UtilsLocalDataBase localdatabase = null;

    public UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    Handler mHandler;

    public IntentServiceTrackGPS() {

        super("IntentServiceTrackApp");
        mHandler = new Handler();
    }

    private LocationManager locationManager;
    private String provider;

    protected void onHandleIntent(Intent intent) {
        Location location;
        boolean enabled;

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.NO_REQUIREMENT);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);

        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if ((ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) && (enabled)) {
                    location = locationManager.getLastKnownLocation(provider);
                    saveLocation(location);
                } else {
                    Log.d(TAG, "No permission or GPS disabled");
                }
            } else {
                if (enabled) {
                    location = locationManager.getLastKnownLocation(provider);
                    saveLocation(location);

                } else {
                    Log.d(TAG, "No permission or GPS disabled");
                }
            }
        }

        CreateTrackGPS.SemTrackGPS.release();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
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
