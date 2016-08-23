package com.app.uni.betrack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;
/**
 * Created by cevincent on 13/07/2016.
 */
public class ActivitySurvey extends DotStepper {

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

            View v = inflater.inflate(R.layout.survey_2choices, container, false);
            button = (Button) v.findViewById(R.id.ButtonChoice1);

            if (savedInstanceState != null)
                i = savedInstanceState.getInt(CLICK, 0);

            button.setText(Html.fromHtml("Tap <b>" + i + "</b>"));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Button) view).setText(Html.fromHtml("Tap <b>" + (++i) + "</b>"));
                    mStepper.getExtras().putInt(CLICK, i);
                }
            });

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

