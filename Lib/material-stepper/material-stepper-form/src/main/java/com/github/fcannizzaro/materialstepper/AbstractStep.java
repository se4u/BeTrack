package com.github.fcannizzaro.materialstepper;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.fcannizzaro.materialstepper.interfaces.Nextable;
import com.github.fcannizzaro.materialstepper.style.BaseNavigation;
import com.github.fcannizzaro.materialstepper.style.BaseStyle;
import com.github.fcannizzaro.materialstepper.style.DotStepper;

/**
 * Created by Francesco Cannizzaro on 23/12/2015.
 */
public abstract class AbstractStep extends Fragment implements Nextable {

    protected BaseStyle mStepper;

    public AbstractStep stepper(BaseStyle mStepper) {
        this.mStepper = mStepper;
        return this;
    }

    protected Bundle getStepData() {
        return mStepper.getStepData();
    }

    protected Bundle getStepDataFor(int step) {
        return mStepper.getStepDataFor(step);
    }

    protected Bundle getLastStepData(){
        return mStepper.getStepDataFor(mStepper.steps()-1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public String optional() {
        return isAdded() ? getString(R.string.ms_optional) : "";
    }

    public abstract String name();

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public void onStepVisible() {
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onPrevious() {

    }

    //call this method to move to next screen
    public void moveToNext() {
        BaseNavigation.moveToNext();
    }

    public boolean getVisibilityNextStep(int valInc)
    {
        return mStepper.getVisibilityNextStep(valInc);
    }

    public void setVisibilityNextStep(boolean state, int valInc)
    {
        mStepper.setVisibilityNextStep(state, valInc);
    }

    @Override
    public boolean nextIf() {
        return true;
    }

    @Override
    public String error() {
        return "No error";
    }
}
