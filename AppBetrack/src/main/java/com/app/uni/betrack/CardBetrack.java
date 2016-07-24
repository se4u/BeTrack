package com.app.uni.betrack;

/**
 * Created by cevincent on 08/07/2016.
 */
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class CardBetrack extends RelativeLayout implements ViewSwitcher.ViewFactory{

    //Top part
    private ImageSwitcher mSwitcher;
    private TextView mQuestion;

    //Bottom part

    private Button mIcon;

    private int [] mImgs;
    private Context mContext;
    private Drawable BackgroundRed;
    private Drawable BackgroundGreen;

    private View mCardBetrack;
    private LinearLayout mBottomCard;
    private LinearLayout mTopCard;

    public static void InternalSetBackground(Drawable Background, Button button)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            button.setBackground(Background);
        }
        else
        {
            button.setBackgroundDrawable(Background);
        }
    }

    public CardBetrack(Context context,
                       String question,
                       String answersYes,
                       String answersNo,
                       int [] imgs)  {
        super(context);
        mContext = context;
        mImgs = imgs;
        initSimple(answersYes, answersNo);
        mQuestion.setText(question);

    }

    public CardBetrack(Context context,
                       String question,
                       int [] imgs) {
        super(context);
        mContext = context;
        mImgs = imgs;
        initComplex();
        mQuestion.setText(question);
    }

    public View makeView() {
        ImageView i = new ImageView(mContext);
        i.setBackgroundColor(Color.TRANSPARENT);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return i;
    }
    private void initComplex() {

        mCardBetrack = inflate(getContext(), R.layout.card_betrack, this);
        mCardBetrack.setPadding(0, 20, 0, 20);

        mBottomCard = (LinearLayout)mCardBetrack.findViewById(R.id.CardBetrackLinearLayout_Bottom);
        mTopCard = (LinearLayout)mCardBetrack.findViewById(R.id.CardBetrackLinearLayout_Top);

        mSwitcher = (ImageSwitcher)mCardBetrack.findViewById(R.id.CardBetrackSwitcher);
        mSwitcher.setFactory(this);
        mSwitcher.setInAnimation(AnimationUtils.loadAnimation(mContext,
                android.R.anim.fade_in));
        mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(mContext,
                android.R.anim.fade_out));
        mSwitcher.setImageResource(mImgs[0]);

        mIcon = (Button) mCardBetrack.findViewById(R.id.CardBetrackButton);

        mQuestion = (TextView)mCardBetrack.findViewById(R.id.CardBetrackQuestion);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BackgroundRed = (Drawable)getResources().getDrawable(R.drawable.button_round_custom_red, mContext.getTheme());
            BackgroundGreen = (Drawable)getResources().getDrawable(R.drawable.button_round_custom_neutral, mContext.getTheme());
        } else {
            BackgroundRed = (Drawable)getResources().getDrawable(R.drawable.button_round_custom_red);
            BackgroundGreen = (Drawable)getResources().getDrawable(R.drawable.button_round_custom_neutral);
        }

        View child = new CardBottomBetrack(mContext);

        mBottomCard.addView(child);

    }

    private void initSimple(String answersYes, String answersNo) {

        mCardBetrack = inflate(getContext(), R.layout.card_betrack, this);
        mCardBetrack.setPadding(0, 20, 0, 20);

        mBottomCard = (LinearLayout)mCardBetrack.findViewById(R.id.CardBetrackLinearLayout_Bottom);
        mTopCard = (LinearLayout)mCardBetrack.findViewById(R.id.CardBetrackLinearLayout_Top);

        mSwitcher = (ImageSwitcher)mCardBetrack.findViewById(R.id.CardBetrackSwitcher);
        mSwitcher.setFactory(this);
        mSwitcher.setInAnimation(AnimationUtils.loadAnimation(mContext,
                android.R.anim.fade_in));
        mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(mContext,
                android.R.anim.fade_out));
        mSwitcher.setImageResource(mImgs[0]);

        mIcon = (Button) mCardBetrack.findViewById(R.id.CardBetrackButton);

        mQuestion = (TextView)mCardBetrack.findViewById(R.id.CardBetrackQuestion);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BackgroundRed = (Drawable)getResources().getDrawable(R.drawable.button_round_custom_red, mContext.getTheme());
            BackgroundGreen = (Drawable)getResources().getDrawable(R.drawable.button_round_custom_neutral, mContext.getTheme());

        } else {
            BackgroundRed = (Drawable)getResources().getDrawable(R.drawable.button_round_custom_red);
            BackgroundGreen = (Drawable)getResources().getDrawable(R.drawable.button_round_custom_neutral);
        }

        View child = new CardBottomBetrack(mContext,answersYes,answersNo);
        //View child = new CardBottomBetrack(mContext);

        mBottomCard.addView(child);

    }

    private void animateUpDown(final LinearLayout Layout){
        int TRANSLATION_Y = Layout.getHeight();
        Layout.animate()
                //.alpha(AlphaEnd)
                .translationYBy(-TRANSLATION_Y)
                .setDuration(500)
                .setStartDelay(0)
                .setListener(new AnimatorListenerAdapter() {
                    int TRANSLATION_Y = Layout.getHeight();
                    @Override
                    public void onAnimationEnd(final Animator animation) {
                        Layout.animate()
                                //.alpha(AlphaEnd)
                                .translationYBy(TRANSLATION_Y)
                                .setDuration(500)
                                .setStartDelay(0)
                                .setListener(new AnimatorListenerAdapter() {

                                    @Override
                                    public void onAnimationEnd(final Animator animation) {

                                    }});
                    }});
    }

    private class CardBottomBetrack extends LinearLayout {

        private View mCardBottomSimple;
        private View mCardBottomComplex;
        private Button mAnswerYes;
        private Button mAnswerNo;

        public CardBottomBetrack(Context context,
                           String answersYes,
                           String answersNo)  {
            super(context);
            initSimple();
            mAnswerYes.setText(answersYes);
            mAnswerNo.setText(answersNo);

        }


        private void initSimple() {

            mCardBottomSimple = inflate(getContext(), R.layout.card_bottom_simple, this);

            mAnswerYes = (Button) mCardBottomSimple.findViewById(R.id.CardBetrackButtonYes);
            mAnswerNo = (Button) mCardBottomSimple.findViewById(R.id.CardBetrackButtonNo);
            mBottomCard.bringToFront();
            mTopCard.bringToFront();

            mAnswerNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSwitcher.setImageResource(mImgs[0]);
                    InternalSetBackground(BackgroundGreen, mIcon);
                    //animateUpDown(mBottomCard);
                }
            });
            mAnswerYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSwitcher.setImageResource(mImgs[1]);
                    InternalSetBackground(BackgroundRed, mIcon);
                    //animateUpDown(mBottomCard);
                }
            });
        }

        public CardBottomBetrack(Context context)  {
            super(context);
            initComplex();

        }

        private void initComplex() {
            mCardBottomComplex = inflate(getContext(), R.layout.card_bottom_complex, this);
            mCardBottomComplex.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        }

    }

}