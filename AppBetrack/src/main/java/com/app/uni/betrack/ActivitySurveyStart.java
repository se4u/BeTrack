package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cevincent on 13/07/2016.
 */
public class ActivitySurveyStart extends DotStepper {

    private static final String TAG = "ActivitySurveyStart";

    private int i = 1;
    private static Drawable BackgroundNoSelection;
    private static Drawable BackgroundSelected;


    private static int SurveyInRelation = -1;
    private static int SurveyAge = 24;
    private static int SurveyLengthPeriod = 4;
    private static int SurveyLenghCycle = 29;
    private static String SurveyContraception;

    private static final int SURVEY_AGE_START = 18;
    private static final int SURVEY_AGE_END = 40;
    private static final int SURVEY_DEFAULT_AGE = SurveyAge - SURVEY_AGE_START + 1;

    private static final int SURVEY_PERIOD_MIN = 1;
    private static final int SURVEY_PEIORD_MAX = 10;
    private static final int SURVEY_DEFAULT_PERIOD = SurveyLengthPeriod - SURVEY_PERIOD_MIN + 1;

    private static final int SURVEY_CYCLE_MIN = 10;
    private static final int SURVEY_CYCLE_MAX = 90;
    private static final int SURVEY_DEFAULT_CYCLE = SurveyLenghCycle - SURVEY_CYCLE_MIN + 1;

    private String DateStudyStart = null;

    private ContentValues values = new ContentValues();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
    private UtilsLocalDataBase localdatabase = new UtilsLocalDataBase(this);
    public UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }
    private SettingsStudy ObjSettingsStudy;

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
    public void onComplete() {
        super.onComplete();
        values.clear();
        values.put(UtilsLocalDataBase.C_STARTSTUDY_PID, ObjSettingsStudy.getIdUser());
        values.put(UtilsLocalDataBase.C_STARTSTUDY_AGE, SurveyAge);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_RELATIONSHIP, SurveyInRelation);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_CONTRACEPTION, SurveyContraception);
        values.put(UtilsLocalDataBase.C_STARTSTUDY_AVGMENSTRUALCYCLE, SurveyLenghCycle);
        DateStudyStart = sdf.format(new Date());
        values.put(UtilsLocalDataBase.C_STARTSTUDY_DATE, DateStudyStart);

        AccesLocalDB().insertOrIgnore(values, UtilsLocalDataBase.TABLE_START_STUDY);
        Log.d(TAG, "idUser: " + ObjSettingsStudy.getIdUser() + "Period status: " + 1 + " date: " + DateStudyStart);

        ObjSettingsStudy.setStartSurveyDone(true);

        Intent i = new Intent(ActivitySurveyStart.this, ActivityBeTrack.class);
        startActivity(i);
        finish();
        System.out.println("completed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setErrorTimeout(1500);
        setTitle(getResources().getString(R.string.app_name));

        ObjSettingsStudy = SettingsStudy.getInstance(this);

        //Step 1 of the survey
        Bundle bundle = new Bundle();
        bundle.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_TITLE, getResources().getString(R.string.title_ss_screen1));
        bundle.putString(FragmentSurvey2Choices.SURVEY_2_CHOICES_DESC, getResources().getString(R.string.question_ss_screen1));
        AbstractStep Step1 = new FragmentSurvey2Choices();
        Step1.setArguments(bundle);
        addStep(Step1);

        addStep(createFragment(new StepScreen2()));
        addStep(createFragment(new StepScreen3()));
        addStep(createFragment(new StepScreen4()));
        addStep(createFragment(new StepScreen5()));

        super.onCreate(savedInstanceState);

    }

    private AbstractStep createFragment(AbstractStep fragment) {
        Bundle b = new Bundle();
        b.putInt("position", i++);
        fragment.setArguments(b);
        return fragment;
    }

    static public class StepScreen2 extends AbstractStep {

        private int i = 1;
        private Button button;
        private final static String CLICK = "click";
        private TextView Title;
        private TextView Description;
        private boolean UserScrolled = false;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            List<UtilsListBean> DATA_LIST = new ArrayList<>();
            View v = inflater.inflate(R.layout.survey_scrolling, container, false);
            UtilsListBean listElement = new UtilsListBean(" ");
            DATA_LIST .add(listElement);
            listElement = new UtilsListBean(" ");
            DATA_LIST .add(listElement);
            for(int i=SURVEY_AGE_START;i<=SURVEY_AGE_END;i++) {
                listElement = new UtilsListBean(i + " " +  getResources().getString(R.string.survey_age));
                DATA_LIST .add(listElement);
            }

            listElement = new UtilsListBean(" ");
            DATA_LIST .add(listElement);

            Title = (TextView) v.findViewById(R.id.survey_title);
            Description = (TextView) v.findViewById(R.id.survey_desc);
            Title.setText(getResources().getString(R.string.title_ss_screen2));
            Description.setText(getResources().getString(R.string.question_ss_screen2));

            UtilsCustomListView listView;
            listView = (UtilsCustomListView) v.findViewById(R.id.listView);

            listView.setAdapter(new UtilsListAdapter(getContext(), DATA_LIST));

            listView.setSelection(SURVEY_DEFAULT_AGE);


            v.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    FrameLayout listViewLayout = (FrameLayout) v.findViewById(R.id.listViewLayout);
                    listViewLayout.setPadding((right-left)/4, 0, (right-left)/4, 0);
                }
            });


            listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view,
                                                 int scrollState) {
                    if ((scrollState == SCROLL_STATE_FLING) || (scrollState == SCROLL_STATE_TOUCH_SCROLL)) {
                        UserScrolled = true;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    if (UserScrolled == true) {
                        SurveyAge = SURVEY_AGE_START + firstVisibleItem;
                        UserScrolled = false;
                    }

                }
            });

            if (savedInstanceState != null)
                i = savedInstanceState.getInt(CLICK, 0);

            return v;
        }

        @Override
        public void onSaveInstanceState(Bundle state) {
            super.onSaveInstanceState(state);
            state.putInt(CLICK, i);
        }

        @Override
        public String name() {
            return "Tab " + getArguments().getInt("position", 0);
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
            return i > 1;
        }

        @Override
        public String error() {
            return "<b>You must click!</b> <small>this is the condition!</small>";
        }

    }

    static public class StepScreen3 extends AbstractStep {

        private int i = 1;
        private Button button;
        private final static String CLICK = "click";
        private TextView Title;
        private TextView Description;
        private boolean UserScrolled = false;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            List<UtilsListBean> DATA_LIST = new ArrayList<>();
            View v = inflater.inflate(R.layout.survey_scrolling, container, false);
            UtilsListBean listElement = new UtilsListBean(" ");
            DATA_LIST .add(listElement);
            listElement = new UtilsListBean(" ");
            DATA_LIST .add(listElement);
            for(int i=SURVEY_PERIOD_MIN;i<=SURVEY_PEIORD_MAX;i++) {
                listElement = new UtilsListBean("~" + i + " " +  getResources().getString(R.string.survey_days));
                DATA_LIST .add(listElement);
            }
            listElement = new UtilsListBean(" ");
            DATA_LIST .add(listElement);

            Title = (TextView) v.findViewById(R.id.survey_title);
            Description = (TextView) v.findViewById(R.id.survey_desc);
            Title.setText(getResources().getString(R.string.title_ss_screen3));
            Description.setText(getResources().getString(R.string.question_ss_screen3));

            UtilsCustomListView listView;
            listView = (UtilsCustomListView) v.findViewById(R.id.listView);

            listView.setAdapter(new UtilsListAdapter(getContext(), DATA_LIST));

            listView.setSelection(SURVEY_DEFAULT_PERIOD);

            v.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    FrameLayout listViewLayout = (FrameLayout) v.findViewById(R.id.listViewLayout);
                    listViewLayout.setPadding((right-left)/4, 0, (right-left)/4, 0);
                }
            });

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view,
                                                 int scrollState) {
                    if ((scrollState == SCROLL_STATE_FLING) || (scrollState == SCROLL_STATE_TOUCH_SCROLL)) {
                        UserScrolled = true;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    if (UserScrolled == true) {
                        SurveyLengthPeriod = SURVEY_PERIOD_MIN + firstVisibleItem;
                        UserScrolled = false;
                    }
                }
            });

            if (savedInstanceState != null)
                i = savedInstanceState.getInt(CLICK, 0);

            return v;
        }

        @Override
        public void onSaveInstanceState(Bundle state) {
            super.onSaveInstanceState(state);
            state.putInt(CLICK, i);
        }

        @Override
        public String name() {
            return "Tab " + getArguments().getInt("position", 0);
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
            return i > 1;
        }

        @Override
        public String error() {
            return "<b>You must click!</b> <small>this is the condition!</small>";
        }

    }

    static public class StepScreen4 extends AbstractStep {

        private int i = 1;
        private Button button;
        private final static String CLICK = "click";
        private TextView Title;
        private TextView Description;
        private boolean UserScrolled = false;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            List<UtilsListBean> DATA_LIST = new ArrayList<>();
            View v = inflater.inflate(R.layout.survey_scrolling, container, false);
            UtilsListBean listElement = new UtilsListBean(" ");
            DATA_LIST .add(listElement);
            listElement = new UtilsListBean(" ");
            DATA_LIST .add(listElement);
            for(int i=SURVEY_CYCLE_MIN;i<=SURVEY_CYCLE_MAX;i++) {
                listElement = new UtilsListBean("~" + i + " " + getResources().getString(R.string.survey_days));
                DATA_LIST .add(listElement);
            }
            listElement = new UtilsListBean(" ");
            DATA_LIST .add(listElement);

            Title = (TextView) v.findViewById(R.id.survey_title);
            Description = (TextView) v.findViewById(R.id.survey_desc);
            Title.setText(getResources().getString(R.string.title_ss_screen4));
            Description.setText(getResources().getString(R.string.question_ss_screen4));

            UtilsCustomListView listView;
            listView = (UtilsCustomListView) v.findViewById(R.id.listView);

            listView.setAdapter(new UtilsListAdapter(getContext(), DATA_LIST));

            listView.setSelection(SURVEY_DEFAULT_CYCLE);

            v.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
               @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                   FrameLayout listViewLayout = (FrameLayout) v.findViewById(R.id.listViewLayout);
                   listViewLayout.setPadding((right-left)/4, 0, (right-left)/4, 0);
                }
            });


            listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view,
                                                 int scrollState) {
                    if ((scrollState == SCROLL_STATE_FLING) || (scrollState == SCROLL_STATE_TOUCH_SCROLL)) {
                        UserScrolled = true;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    if (UserScrolled == true) {
                        SurveyLenghCycle = SURVEY_CYCLE_MIN + firstVisibleItem;
                        UserScrolled = false;
                    }
                }
            });

            if (savedInstanceState != null)
                i = savedInstanceState.getInt(CLICK, 0);

            return v;
        }

        @Override
        public void onSaveInstanceState(Bundle state) {
            super.onSaveInstanceState(state);
            state.putInt(CLICK, i);
        }

        @Override
        public String name() {
            return "Tab " + getArguments().getInt("position", 0);
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
            return i > 1;
        }

        @Override
        public String error() {
            return "<b>You must click!</b> <small>this is the condition!</small>";
        }

    }

    static public class StepScreen5 extends AbstractStep {
        private int i;
        private TextView Title;
        private TextView Description;
        private EditText Comment;


        private final static String CLICK = "click";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View v = inflater.inflate(R.layout.survey_text, container, false);


            Comment = (EditText) v.findViewById(R.id.survey_comment);

            Title = (TextView) v.findViewById(R.id.survey_title);
            Description = (TextView) v.findViewById(R.id.survey_desc);
            Title.setText(getResources().getString(R.string.title_ss_screen5));
            Description.setText(getResources().getString(R.string.question_ss_screen5));

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
                        SurveyContraception = s.toString();
                        i++;
                        mStepper.getExtras().putInt(CLICK, i);
                    }
                }
            });
            if (savedInstanceState != null)
                i = savedInstanceState.getInt(CLICK, 0);

            return v;
        }

        @Override
        public void onSaveInstanceState(Bundle state) {
            super.onSaveInstanceState(state);
            state.putInt(CLICK, i);
        }

        @Override
        public String name() {
            return "Tab " + getArguments().getInt("position", 0);
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
            return i > 0;
        }

        @Override
        public String error() {
            return getResources().getString(R.string.question_ss_error);
        }

    }
}

