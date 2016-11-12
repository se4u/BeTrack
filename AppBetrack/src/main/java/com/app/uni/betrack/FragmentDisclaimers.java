package com.app.uni.betrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;

/**
 * Created by cevincent on 11/11/2016.
 */

public class FragmentDisclaimers  extends AbstractStep {

    //Output
    public  static final String ACTIVITY_DISCLAIMER = "ACTIVITY_DISCLAIMER";
    public int DisclaimerAgreed = -1;

    //Input
    public static final String ACTIVITY_DISCLAIMER_TITLE = "ACTIVITY_DISCLAIMER_TITLE";
    public static final String ACTIVITY_DISCLAIMER_DESC = "ACTIVITY_DISCLAIMER_DESC";
    public static final String ACTIVITY_DISCLAIMER_AGREE = "ACTIVITY_DISCLAIMER_AGREE";

    private CheckBox Checkbox;
    private TextView Title;
    private TextView Description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_disclaimers, container, false);
        Checkbox = (CheckBox) v.findViewById(R.id.checkbox_agree);

        Title = (TextView) v.findViewById(R.id.disclaimer_title);
        Description = (TextView) v.findViewById(R.id.disclaimers);

        final Bundle bundle = this.getArguments();
        String ActivityTitle = bundle.getString(ACTIVITY_DISCLAIMER_TITLE, null);
        String ActivityDescription = bundle.getString(ACTIVITY_DISCLAIMER_DESC, null);
        String ActivityAgreement = bundle.getString(ACTIVITY_DISCLAIMER_AGREE, null);

        Title.setText(ActivityTitle);
        Description.setText(ActivityDescription);
        Checkbox.setText(ActivityAgreement);

        if (savedInstanceState != null)
            DisclaimerAgreed = savedInstanceState.getInt(ACTIVITY_DISCLAIMER, -1);

        if (DisclaimerAgreed == 1) {

        } else if (DisclaimerAgreed == -1) {

        }

        Checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            boolean checked = ((CheckBox) view).isChecked();
                if (checked)
                    DisclaimerAgreed = 1;
                else
                    DisclaimerAgreed = -1;

            mStepper.getExtras().putInt(ACTIVITY_DISCLAIMER, DisclaimerAgreed);
            bundle.putInt(ACTIVITY_DISCLAIMER, DisclaimerAgreed);
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt(ACTIVITY_DISCLAIMER, DisclaimerAgreed);
    }

    @Override
    public String name() {
        return "Tab " + getArguments().getInt(ACTIVITY_DISCLAIMER, 0);
    }

    @Override
    public boolean isOptional() {
        return false;
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
        return DisclaimerAgreed > -1;
    }

    @Override
    public String error() {
        return getResources().getString(R.string.disclaimer_error);
    }


}
