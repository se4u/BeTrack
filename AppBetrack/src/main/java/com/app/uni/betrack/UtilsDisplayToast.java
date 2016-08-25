package com.app.uni.betrack;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by cedoctet on 24/08/2016.
 */
public class UtilsDisplayToast implements Runnable {
    private final Context mContext;
    String mText;

    public UtilsDisplayToast(Context mContext, String text){
        this.mContext = mContext;
        mText = text;
    }

    public void run(){
        Toast.makeText(mContext, mText, Toast.LENGTH_SHORT).show();
    }
}

