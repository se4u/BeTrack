package com.app.uni.betrack;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cevincent on 13/07/2016.
 */
public class ActivitySurveyStart extends DotStepper {

    private int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setErrorTimeout(1500);
        setTitle("Let's start");

        addStep(createFragment(new StepSample()));
        addStep(createFragment(new StepSample()));
        addStep(createFragment(new StepSample()));
        addStep(createFragment(new StepSample()));
        addStep(createFragment(new StepSample()));

        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_survey);
    }

    private AbstractStep createFragment(AbstractStep fragment) {
        Bundle b = new Bundle();
        b.putInt("position", i++);
        fragment.setArguments(b);
        return fragment;
    }
    static public class StepSample extends AbstractStep {

        private int i = 1;
        private Button button;
        private final static String CLICK = "click";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            List<UtilsListBean> DATA_LIST = new ArrayList<>();
            View v = inflater.inflate(R.layout.survey_scrolling, container, false);
            //button = (Button) v.findViewById(R.id.ButtonChoice1);

            for(int i=10;i<=90;i++) {
                UtilsListBean listElement = new UtilsListBean("~" + i + " days");
                DATA_LIST .add(listElement);
            }

            UtilsCustomListView listView;
            listView = (UtilsCustomListView) v.findViewById(R.id.listView);

            listView.setAdapter(new UtilsListAdapter(getContext(), DATA_LIST));
            FrameLayout layout;
            layout = (FrameLayout) v.findViewById(R.id.listViewLayout);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)layout.getLayoutParams();

            params.setMargins(v.getMeasuredWidth()/4, 0, v.getMeasuredWidth()/4, 0);
            layout.setLayoutParams(params);

            if (savedInstanceState != null)
                i = savedInstanceState.getInt(CLICK, 0);

            //button.setText(Html.fromHtml("Tap <b>" + i + "</b>"));


            /*button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Button) view).setText(Html.fromHtml("Tap <b>" + (++i) + "</b>"));
                    mStepper.getExtras().putInt(CLICK, i);
                }
            });*/

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
}

