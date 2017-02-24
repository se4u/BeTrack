package com.app.uni.betrack;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by cevincent on 24/02/2017.
 */

public class UtilsHandWritingTextView extends TextView {

    public UtilsHandWritingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public UtilsHandWritingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UtilsHandWritingTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Alamain.ttf");
        setTypeface(tf);
    }
}
