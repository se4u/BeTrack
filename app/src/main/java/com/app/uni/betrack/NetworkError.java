package com.app.uni.betrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.app.uni.betrack.R;

/**
 * Created by cevincent on 4/22/16.
 */
public class NetworkError {

    private Activity mActivity;

    public NetworkError(Activity context) {
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
        String title =  mActivity.getString(R.string.network_error_title);
        String message =  mActivity.getString(R.string.network_error_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(android.R.string.ok, new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the activity as they have declined the EULA
                        mActivity.finish();
                    }

                });
        builder.create().show();

        // loop till a runtime exception is triggered.
        try { Looper.loop(); }
        catch(RuntimeException e2) {}

    }
}
