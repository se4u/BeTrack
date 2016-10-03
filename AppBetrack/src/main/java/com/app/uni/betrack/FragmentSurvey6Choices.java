package com.app.uni.betrack;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.ArrayList;

/**
 * Created by cedoctet on 18/09/2016.
 */
public class FragmentSurvey6Choices extends AbstractStep {

    //Output
    public  static final String SURVEY_STATUS = "SURVEY_STATUS";
    public int SurveyStatus = 0;
    public final int SURVEY_STATUS_CHOICE1 = 1;
    public final int SURVEY_STATUS_CHOICE2 = 2;
    public final int SURVEY_STATUS_CHOICE3 = 4;
    public final int SURVEY_STATUS_CHOICE4 = 8;
    public final int SURVEY_STATUS_CHOICE5 = 16;
    public final int SURVEY_STATUS_CHOICE6 = 32;

    //Input
    public static final String SURVEY_6_CHOICES_TITLE = "SURVEY_6_CHOICES_TITLE";
    public static final String SURVEY_6_CHOICES_DESC = "SURVEY_6_CHOICES_DESC";
    public static final String SURVEY_6_CHOICES_ICON = "SURVEY_6_CHOICES_ICON";
    public static final String SURVEY_6_CHOICES_ICON_TEXT = "SURVEY_6_CHOICES_ICON_TEXT";

    private TextView IconText1;
    private TextView IconText2;
    private TextView IconText3;
    private TextView IconText4;
    private TextView IconText5;
    private TextView IconText6;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;

    private ImageView imgbutton1;
    private ImageView imgbutton2;
    private ImageView imgbutton3;
    private ImageView imgbutton4;
    private ImageView imgbutton5;
    private ImageView imgbutton6;

    private TextView Title;
    private TextView Description;


    private static Drawable BackgroundNoSelection;
    private static Drawable BackgroundSelected;

    private static void InternalSetBackground(Drawable Background, Button button)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            button.setBackground(Background);
        }
        else
        {
            button.setBackgroundDrawable(Background);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.survey_6choices, container, false);
        final int tintPrimary = Color.parseColor(SettingsBetrack.colorPrimary);
        final int tintGray = Color.parseColor(SettingsBetrack.colorDarkGrey);
        final PorterDuff.Mode mode = PorterDuff.Mode.ADD;


        button1 = (Button) v.findViewById(R.id.ButtonChoice1);
        button2 = (Button) v.findViewById(R.id.ButtonChoice2);
        button3 = (Button) v.findViewById(R.id.ButtonChoice3);
        button4 = (Button) v.findViewById(R.id.ButtonChoice4);
        button5 = (Button) v.findViewById(R.id.ButtonChoice5);
        button6 = (Button) v.findViewById(R.id.ButtonChoice6);

        imgbutton1 = (ImageView) v.findViewById(R.id.ImgButtonChoice1);
        imgbutton2 = (ImageView) v.findViewById(R.id.ImgButtonChoice2);
        imgbutton3 = (ImageView) v.findViewById(R.id.ImgButtonChoice3);
        imgbutton4 = (ImageView) v.findViewById(R.id.ImgButtonChoice4);
        imgbutton5 = (ImageView) v.findViewById(R.id.ImgButtonChoice5);
        imgbutton6 = (ImageView) v.findViewById(R.id.ImgButtonChoice6);

        Title = (TextView) v.findViewById(R.id.survey_title);
        Description = (TextView) v.findViewById(R.id.survey_desc);

        final Bundle bundle = this.getArguments();
        String SurveyTitle = bundle.getString(SURVEY_6_CHOICES_TITLE, null);
        String SurveyDescription = bundle.getString(SURVEY_6_CHOICES_DESC, null);
        ArrayList<String> IconsText = bundle.getStringArrayList(SURVEY_6_CHOICES_ICON_TEXT);
        ArrayList<Integer> Icons = bundle.getIntegerArrayList(FragmentSurvey6Choices.SURVEY_6_CHOICES_ICON);
        Title.setText(SurveyTitle);
        Description.setText(SurveyDescription);

        int width = ((int)getResources().getDimension(R.dimen.survey_6choices_button_width))/4;
        int height = ((int)getResources().getDimension(R.dimen.survey_6choices_button_height))/4;;

        IconText1 = (TextView) v.findViewById(R.id.TextChoice1);
        IconText1.setText(IconsText.get(0));
        imgbutton1.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
                Icons.get(0), height, width));

        IconText2 = (TextView) v.findViewById(R.id.TextChoice2);
        IconText2.setText(IconsText.get(1));
        imgbutton2.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
                Icons.get(1), height, width));

        IconText3 = (TextView) v.findViewById(R.id.TextChoice3);
        IconText3.setText(IconsText.get(2));
        imgbutton3.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
                Icons.get(2), height, width));

        IconText4 = (TextView) v.findViewById(R.id.TextChoice4);
        IconText4.setText(IconsText.get(3));
        imgbutton4.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
                Icons.get(3), height, width));

        IconText5 = (TextView) v.findViewById(R.id.TextChoice5);
        IconText5.setText(IconsText.get(4));
        imgbutton5.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
                Icons.get(4), height, width));

        IconText6 = (TextView) v.findViewById(R.id.TextInfoRight);
        IconText6.setText(IconsText.get(5));
        imgbutton6.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
                Icons.get(5), height, width));

        if (savedInstanceState != null)
            SurveyStatus = savedInstanceState.getInt(SURVEY_STATUS, -1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BackgroundNoSelection = (Drawable)getResources().getDrawable(R.drawable.button_round_noselection, getContext().getTheme());
            BackgroundSelected = (Drawable)getResources().getDrawable(R.drawable.button_round_selection, getContext().getTheme());
        } else {
            BackgroundNoSelection = (Drawable)getResources().getDrawable(R.drawable.button_round_noselection);
            BackgroundSelected = (Drawable)getResources().getDrawable(R.drawable.button_round_selection);
        }

        if ((SurveyStatus & SURVEY_STATUS_CHOICE1) != SURVEY_STATUS_CHOICE1) {
            InternalSetBackground(BackgroundNoSelection, button1);
            imgbutton1.setColorFilter(tintGray, mode);
            IconText1.setTextColor(Color.parseColor(SettingsBetrack.colorDarkGrey));

        } else {
            InternalSetBackground(BackgroundSelected, button1);
            imgbutton1.setColorFilter(tintPrimary, mode);
            IconText1.setTextColor(Color.parseColor(SettingsBetrack.colorPrimary));
        }

        if ((SurveyStatus & SURVEY_STATUS_CHOICE2) != SURVEY_STATUS_CHOICE2) {
            InternalSetBackground(BackgroundNoSelection, button2);
            imgbutton2.setColorFilter(tintGray, mode);
            IconText2.setTextColor(Color.parseColor(SettingsBetrack.colorDarkGrey));

        } else {
            InternalSetBackground(BackgroundSelected, button2);
            imgbutton2.setColorFilter(tintPrimary, mode);
            IconText2.setTextColor(Color.parseColor(SettingsBetrack.colorPrimary));
        }

        if ((SurveyStatus & SURVEY_STATUS_CHOICE3) != SURVEY_STATUS_CHOICE3) {
            InternalSetBackground(BackgroundNoSelection, button3);
            imgbutton3.setColorFilter(tintGray, mode);
            IconText3.setTextColor(Color.parseColor(SettingsBetrack.colorDarkGrey));

        } else {
            InternalSetBackground(BackgroundSelected, button3);
            imgbutton3.setColorFilter(tintPrimary, mode);
            IconText3.setTextColor(Color.parseColor(SettingsBetrack.colorPrimary));
        }

        if ((SurveyStatus & SURVEY_STATUS_CHOICE4) != SURVEY_STATUS_CHOICE4) {
            InternalSetBackground(BackgroundNoSelection, button4);
            imgbutton4.setColorFilter(tintGray, mode);
            IconText4.setTextColor(Color.parseColor(SettingsBetrack.colorDarkGrey));

        } else {
            InternalSetBackground(BackgroundSelected, button4);
            imgbutton4.setColorFilter(tintPrimary, mode);
            IconText4.setTextColor(Color.parseColor(SettingsBetrack.colorPrimary));
        }

        if ((SurveyStatus & SURVEY_STATUS_CHOICE5) != SURVEY_STATUS_CHOICE5) {
            InternalSetBackground(BackgroundNoSelection, button5);
            imgbutton5.setColorFilter(tintGray, mode);
            IconText5.setTextColor(Color.parseColor(SettingsBetrack.colorDarkGrey));

        } else {
            InternalSetBackground(BackgroundSelected, button5);
            imgbutton5.setColorFilter(tintPrimary, mode);
            IconText5.setTextColor(Color.parseColor(SettingsBetrack.colorPrimary));
        }

        if ((SurveyStatus & SURVEY_STATUS_CHOICE6) != SURVEY_STATUS_CHOICE6) {
            InternalSetBackground(BackgroundNoSelection, button6);
            imgbutton6.setColorFilter(tintGray, mode);
            IconText6.setTextColor(Color.parseColor(SettingsBetrack.colorDarkGrey));

        } else {
            InternalSetBackground(BackgroundSelected, button6);
            imgbutton6.setColorFilter(tintPrimary, mode);
            IconText6.setTextColor(Color.parseColor(SettingsBetrack.colorPrimary));
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((SurveyStatus & SURVEY_STATUS_CHOICE1) == SURVEY_STATUS_CHOICE1) {
                    SurveyStatus &= ~SURVEY_STATUS_CHOICE1;
                    InternalSetBackground(BackgroundNoSelection, button1);
                    imgbutton1.setColorFilter(tintGray, mode);
                    IconText1.setTextColor(Color.parseColor(SettingsBetrack.colorDarkGrey));
                } else {
                    SurveyStatus |= SURVEY_STATUS_CHOICE1;
                    InternalSetBackground(BackgroundSelected, button1);
                    imgbutton1.setColorFilter(tintPrimary, mode);
                    IconText1.setTextColor(Color.parseColor(SettingsBetrack.colorPrimary));
                }
                mStepper.getExtras().putInt(SURVEY_STATUS, SurveyStatus);
                bundle.putInt(SURVEY_STATUS, SurveyStatus);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((SurveyStatus & SURVEY_STATUS_CHOICE2) == SURVEY_STATUS_CHOICE2) {
                    SurveyStatus &= ~SURVEY_STATUS_CHOICE2;
                    InternalSetBackground(BackgroundNoSelection, button2);
                    imgbutton2.setColorFilter(tintGray, mode);
                    IconText2.setTextColor(Color.parseColor(SettingsBetrack.colorDarkGrey));
                } else {
                    SurveyStatus |= SURVEY_STATUS_CHOICE2;
                    InternalSetBackground(BackgroundSelected, button2);
                    imgbutton2.setColorFilter(tintPrimary, mode);
                    IconText2.setTextColor(Color.parseColor(SettingsBetrack.colorPrimary));
                }
                mStepper.getExtras().putInt(SURVEY_STATUS, SurveyStatus);
                bundle.putInt(SURVEY_STATUS, SurveyStatus);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((SurveyStatus & SURVEY_STATUS_CHOICE3) == SURVEY_STATUS_CHOICE3) {
                    SurveyStatus &= ~SURVEY_STATUS_CHOICE3;
                    InternalSetBackground(BackgroundNoSelection, button3);
                    imgbutton3.setColorFilter(tintGray, mode);
                    IconText3.setTextColor(Color.parseColor(SettingsBetrack.colorDarkGrey));
                } else {
                    SurveyStatus |= SURVEY_STATUS_CHOICE3;
                    InternalSetBackground(BackgroundSelected, button3);
                    imgbutton3.setColorFilter(tintPrimary, mode);
                    IconText3.setTextColor(Color.parseColor(SettingsBetrack.colorPrimary));
                }
                mStepper.getExtras().putInt(SURVEY_STATUS, SurveyStatus);
                bundle.putInt(SURVEY_STATUS, SurveyStatus);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((SurveyStatus & SURVEY_STATUS_CHOICE4) == SURVEY_STATUS_CHOICE4) {
                    SurveyStatus &= ~SURVEY_STATUS_CHOICE4;
                    InternalSetBackground(BackgroundNoSelection, button4);
                    imgbutton4.setColorFilter(tintGray, mode);
                    IconText4.setTextColor(Color.parseColor(SettingsBetrack.colorDarkGrey));
                } else {
                    SurveyStatus |= SURVEY_STATUS_CHOICE4;
                    InternalSetBackground(BackgroundSelected, button4);
                    imgbutton4.setColorFilter(tintPrimary, mode);
                    IconText4.setTextColor(Color.parseColor(SettingsBetrack.colorPrimary));
                }
                mStepper.getExtras().putInt(SURVEY_STATUS, SurveyStatus);
                bundle.putInt(SURVEY_STATUS, SurveyStatus);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((SurveyStatus & SURVEY_STATUS_CHOICE5) == SURVEY_STATUS_CHOICE5) {
                    SurveyStatus &= ~SURVEY_STATUS_CHOICE5;
                    InternalSetBackground(BackgroundNoSelection, button5);
                    imgbutton5.setColorFilter(tintGray, mode);
                    IconText5.setTextColor(Color.parseColor(SettingsBetrack.colorDarkGrey));
                } else {
                    SurveyStatus |= SURVEY_STATUS_CHOICE5;
                    InternalSetBackground(BackgroundSelected, button5);
                    imgbutton5.setColorFilter(tintPrimary, mode);
                    IconText5.setTextColor(Color.parseColor(SettingsBetrack.colorPrimary));
                }
                mStepper.getExtras().putInt(SURVEY_STATUS, SurveyStatus);
                bundle.putInt(SURVEY_STATUS, SurveyStatus);
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((SurveyStatus & SURVEY_STATUS_CHOICE6) == SURVEY_STATUS_CHOICE6) {
                    SurveyStatus &= ~SURVEY_STATUS_CHOICE6;
                    InternalSetBackground(BackgroundNoSelection, button6);
                    imgbutton6.setColorFilter(tintGray, mode);
                    IconText6.setTextColor(Color.parseColor(SettingsBetrack.colorDarkGrey));
                } else {
                    SurveyStatus |= SURVEY_STATUS_CHOICE6;
                    InternalSetBackground(BackgroundSelected, button6);
                    imgbutton6.setColorFilter(tintPrimary, mode);
                    IconText6.setTextColor(Color.parseColor(SettingsBetrack.colorPrimary));
                }
                mStepper.getExtras().putInt(SURVEY_STATUS, SurveyStatus);
                bundle.putInt(SURVEY_STATUS, SurveyStatus);
            }
        });
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt(SURVEY_STATUS, SurveyStatus);
    }

    @Override
    public String name() {
        return "Tab " + getArguments().getInt(SURVEY_STATUS, 0);
    }

    @Override
    public boolean isOptional() {
        return true;
    }


    @Override
    public void onStepVisible() {
    }

    @Override
    public void onNext() {
        System.out.println("onNext");
    }

    @Override
    public void onPrevious() {
        System.out.println("onPrevious");
    }

    @Override
    public String optional() {
        return "You can skip";
    }

    @Override
    public boolean nextIf() {
        return SurveyStatus > -1;
    }

    @Override
    public String error() {
        return getResources().getString(R.string.question_error);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imgbutton1.setImageDrawable(null);
        imgbutton2.setImageDrawable(null);
        imgbutton3.setImageDrawable(null);
        imgbutton4.setImageDrawable(null);
        imgbutton5.setImageDrawable(null);
        imgbutton6.setImageDrawable(null);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}


