package com.app.uni.betrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

/**
 * Created by cevincent on 4/9/16.
 */
public class UtilsEula {
    private String EULA_PREFIX = "eula_";
    private Activity mActivity;

    static public String IdUser = null;

    public UtilsEula(Activity context) {
        mActivity = context;
    }

    private PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            pi = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;
    }

    public void show() {
        PackageInfo versionInfo = getPackageInfo();

        // the eulaKey changes every time you increment the version number in the AndroidManifest.xml
        final String eulaKey = EULA_PREFIX + versionInfo.versionCode;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity.getApplicationContext());
        boolean hasBeenShown = prefs.getBoolean(eulaKey, false);


        if(hasBeenShown == false){

            // Show the ConfigEula
            String title = mActivity.getString(R.string.disclaimers_title) + " v" + versionInfo.versionName;

            //Includes the updates as well so users know what changed.
            String message =  mActivity.getString(R.string.disclaimers);

            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // Mark this version as read.
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(eulaKey, true);
                            editor.commit();
                            // Create/Use an unique identifier for the phone
                            IdUser = UtilsDeviceIdGenerator.readDeviceId(mActivity);
                            editor.putString(SettingsStudy.ID_USER, IdUser);
                            editor.commit();

                            SettingsStudy.IdUser = IdUser;

                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Close the activity as they have declined the EULA
                            mActivity.finish();
                        }

                    });
            builder.create().show();

        }
    }
}