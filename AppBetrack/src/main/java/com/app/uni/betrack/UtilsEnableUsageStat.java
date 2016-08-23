package com.app.uni.betrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;

/**
 * Created by cevincent on 6/10/16.
 */
public class UtilsEnableUsageStat {
    private Activity mActivity;
    public static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1001;
    public UtilsEnableUsageStat(Activity context) {
        mActivity = context;
    }

    public void show() {
        // make a handler that throws a runtime exception when a message is received
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };
        //Includes the updates as well so users know what changed.
        String title =  mActivity.getString(R.string.UsageStat_enable_title);
        String message =  mActivity.getString(R.string.UsageStat_enable_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        mActivity.startActivityForResult(
                                new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                                MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
                    }

                });
        builder.create().show();
        // loop till a runtime exception is triggered.
        try { Looper.loop(); }
        catch(RuntimeException e2) {}

    }
}
