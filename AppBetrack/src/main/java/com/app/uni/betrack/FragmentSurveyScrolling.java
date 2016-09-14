package com.app.uni.betrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cedoctet on 12/09/2016.
 */
public class FragmentSurveyScrolling extends AbstractStep {
    private int i = 1;
    private Button button;
    private final static String CLICK = "click";
    private TextView Title;
    private TextView Description;
    private boolean UserScrolled = false;


    //Output
    public int SurveyStatus = -1;

    //Input
    public static final String SURVEY_SCROLLING_TITLE = "SURVEY_SCROLLING_TITLE";
    public static final String SURVEY_SCROLLING_DESC = "SURVEY_SCROLLING_DESC";
    public static final String SURVEY_SCROLLING_UNIT = "SURVEY_SCROLLING_UNIT";
    public static final String SURVEY_SCROLLING_START_RANGE = "SURVEY_SCROLLING_START_RANGE";
    public static final String SURVEY_SCROLLING_END_RANGE = "SURVEY_SCROLLING_END_RANGE";
    public static final String SURVEY_SCROLLING_DEFAULT_VALUE = "SURVEY_SCROLLING_DEFAULT_VALUE";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        List<UtilsListBean> DATA_LIST = new ArrayList<>();
        View v = inflater.inflate(R.layout.survey_scrolling, container, false);
        UtilsListBean listElement = new UtilsListBean(" ");
        DATA_LIST .add(listElement);
        listElement = new UtilsListBean(" ");
        DATA_LIST .add(listElement);

        Bundle bundle = this.getArguments();
        String SurveyTitle = bundle.getString(SURVEY_SCROLLING_TITLE, null);
        String SurveyDescription = bundle.getString(SURVEY_SCROLLING_DESC, null);
        String SurveyUnit = bundle.getString(SURVEY_SCROLLING_UNIT, null);
        final int SurveyStartRange = bundle.getInt(SURVEY_SCROLLING_START_RANGE, 0);
        int SurveyEndRange = bundle.getInt(SURVEY_SCROLLING_END_RANGE, 0);
        int SurveyDefaultValue = bundle.getInt(SURVEY_SCROLLING_DEFAULT_VALUE, 0);

        for(int i=SurveyStartRange;i<=SurveyEndRange;i++) {
            listElement = new UtilsListBean(i + " " +  SurveyUnit);
            DATA_LIST .add(listElement);
        }

        listElement = new UtilsListBean(" ");
        DATA_LIST .add(listElement);

        Title = (TextView) v.findViewById(R.id.survey_title);
        Description = (TextView) v.findViewById(R.id.survey_desc);
        Title.setText(SurveyTitle);
        Description.setText(SurveyDescription);

        UtilsCustomListView listView;
        listView = (UtilsCustomListView) v.findViewById(R.id.listView);

        listView.setAdapter(new UtilsListAdapter(getContext(), DATA_LIST));

        listView.setSelection(SurveyDefaultValue);


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
                    SurveyStatus = SurveyStartRange + firstVisibleItem;
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
