package com.app.uni.betrack;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

    private TextView Title;
    private TextView Description;
    private EditText Comment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.survey_text, container, false);

        final Bundle bundle = this.getArguments();
        String SurveyTitle = bundle.getString(SURVEY_TEXT_TITLE, null);
        String SurveyDescription = bundle.getString(SURVEY_TEXT_DESC, null);
        String SurveyComment = bundle.getString(SURVEY_TEXT_COMMENT, null);

        Comment = (EditText) v.findViewById(R.id.survey_comment);
        Comment.setHint(SurveyComment);
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
        if (savedInstanceState != null)
            SurveyStatus = savedInstanceState.getString(SURVEY_STATUS, null);

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
