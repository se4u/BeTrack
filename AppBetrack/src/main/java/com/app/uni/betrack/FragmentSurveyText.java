package com.app.uni.betrack;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;

/**
 * Created by cedoctet on 12/09/2016.
 */
public class FragmentSurveyText extends AbstractStep {
    private static final String TAG = "FragmentSurveyText";

    //Output
    public  static final String SURVEY_STATUS = "SURVEY_STATUS";
    public String SurveyStatus = null;

    //Input
    public static final String SURVEY_TEXT_TITLE = "SURVEY_TEXT_TITLE";
    public static final String SURVEY_TEXT_DESC = "SURVEY_TEXT_DESC";
    public static final String SURVEY_TEXT_COMMENT = "SURVEY_TEXT_COMMENT";
    public static final String SURVEY_TEXT_IS_OPTIONAL = "SURVEY_TEXT_IS_OPTIONAL";
    public static final String SURVEY_TEXT_IS_NUMBER_INPUT = "SURVEY_TEXT_IS_NUMBER_INPUT";
    public static final String SURVEY_TEXT_MAX_NBR_LINE = "SURVEY_TEXT_MAX_NBR_LINE";
    public static final String SURVEY_TEXT_MAX_NBR_CHAR = "SURVEY_TEXT_MAX_NBR_CHAR";

    private TextView Title;
    private TextView Description;
    private EditText Comment;

    private boolean isOptional = true;
    private boolean isNumberInput = false;
    private int maxNbrLines = 2;
    private int maxNbrChar = -1;

    private static void InternalSetBackground(Drawable Background, EditText eText)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            eText.setBackground(Background);
        }
        else
        {
            eText.setBackgroundDrawable(Background);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.survey_text, container, false);

        final Bundle bundle = this.getArguments();
        String SurveyTitle = bundle.getString(SURVEY_TEXT_TITLE, null);
        String SurveyDescription = bundle.getString(SURVEY_TEXT_DESC, null);
        String SurveyComment = bundle.getString(SURVEY_TEXT_COMMENT, null);
        isOptional = bundle.getBoolean(SURVEY_TEXT_IS_OPTIONAL, true);
        isNumberInput = bundle.getBoolean(SURVEY_TEXT_IS_NUMBER_INPUT, false);
        maxNbrLines = bundle.getInt(SURVEY_TEXT_MAX_NBR_LINE, 1);
        maxNbrChar = bundle.getInt(SURVEY_TEXT_MAX_NBR_CHAR, -1);

        Comment = (EditText) v.findViewById(R.id.survey_comment);
        Comment.setHint(SurveyComment);
        if (isNumberInput == true) {
            Comment.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        if (maxNbrChar != -1) {
            Comment.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxNbrChar)});
        }

        if (maxNbrLines > 1) {
            Comment.setSingleLine(false);
            Comment.setImeOptions(EditorInfo.IME_ACTION_DONE);
            Comment.setRawInputType(InputType.TYPE_CLASS_TEXT);
            Comment.setLines(maxNbrLines);
            Comment.setGravity(Gravity.TOP);
            // Create a border programmatically
            ShapeDrawable shape = new ShapeDrawable(new RectShape());
            shape.getPaint().setColor(Color.parseColor(SettingsBetrack.colorPrimary));
            shape.getPaint().setStyle(Paint.Style.STROKE);
            shape.getPaint().setStrokeWidth(3);

            // Assign the created border to EditText widget
            InternalSetBackground(shape, Comment);
        }

        Title = (TextView) v.findViewById(R.id.survey_title);
        Description = (TextView) v.findViewById(R.id.survey_desc);
        Title.setText(SurveyTitle);
        Description.setText(SurveyDescription);

        Comment.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence currentDigits, int start,
                                      int before, int count) {
                Log.d(TAG, "onTextChanged");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                Log.d(TAG, "beforeTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0) {
                    Log.d(TAG, "afterTextChanged");
                    SurveyStatus = s.toString();
                    mStepper.getExtras().putString(SURVEY_STATUS, SurveyStatus);
                    bundle.putString(SURVEY_STATUS, SurveyStatus);
                }
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString(SURVEY_STATUS, SurveyStatus);
    }

    @Override
    public String name() {
        return "Tab " + getArguments().getString(SURVEY_STATUS, null);
    }

    @Override
    public boolean isOptional() {
        return isOptional;
    }


    @Override
    public void onStepVisible() {
    }

    @Override
    public void onNext() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(Comment.getWindowToken(), 0);
        System.out.println("onNext");
        setVisibilityNextStep(true, 1);
    }

    @Override
    public void onPrevious() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(Comment.getWindowToken(), 0);
        System.out.println("onPrevious");
    }

    @Override
    public String optional() {
        return "You can skip";
    }

    @Override
    public boolean nextIf() {
        return SurveyStatus != null;
    }

    @Override
    public String error() {
        return getResources().getString(R.string.question_error);
    }

}
