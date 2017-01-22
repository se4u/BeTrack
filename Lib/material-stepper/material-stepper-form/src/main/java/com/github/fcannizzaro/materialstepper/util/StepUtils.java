package com.github.fcannizzaro.materialstepper.util;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Francesco Cannizzaro (fcannizzaro).
 */
public class StepUtils {

    private ArrayList<AbstractStep> mSteps = new ArrayList<>();
    private ArrayList<Boolean> mActiveDots = new ArrayList<>();
    private ArrayList<Boolean> mVisibleStep = new ArrayList<>();
    private ArrayList<Boolean> mOptionalStep = new ArrayList<>();
    private int mCurrent;



    public ArrayList<AbstractStep> getSteps() {
        return mSteps;
    }

    public AbstractStep get(int position) {
        return mSteps.get(position);
    }

    public boolean isActive(int i) {
        return mActiveDots.get(i);
    }

    public boolean setActive(int i, boolean set) {
        return mActiveDots.set(i, set);
    }

    public AbstractStep getCurrent() {
        return get(mCurrent);
    }


    public int total() {
        return mSteps.size();
    }

    public void add(AbstractStep step, boolean stateVisibility, boolean stateOptional) {
        mSteps.add(step);
        mVisibleStep.add(stateVisibility);
        mOptionalStep.add(stateOptional);
        mActiveDots.add(false);
    }

    public void addAll(List<AbstractStep> mSteps) {
        this.mSteps.addAll(mSteps);
        Collections.fill(mActiveDots, false);
    }

    public void current(int mCurrent) {
        this.mCurrent = mCurrent;
    }

    public int current() {
        return mCurrent;
    }

    public boolean nextVisible(int stepNumber) {
        return mVisibleStep.get(mCurrent+stepNumber);
    }

    public boolean previousOptional() {
        if ((mCurrent - 1) > 0) {
            return mOptionalStep.get(mCurrent - 1);
        } else {
            return false;
        }
    }

    public void setNextVisibility(boolean state) {
        if ( (mCurrent + 1) < mSteps.size() ) {
            mVisibleStep.set(mCurrent + 1, state);
        }
    }

    public void setPreviousVisibility(boolean state) {
        if ((mCurrent - 1) > 0) {
            mVisibleStep.set(mCurrent - 1, state);
        }
    }

    public boolean previousVisible(int stepNumber) {
        int current = 0;
        if ( (mCurrent - stepNumber) >= 0) {
            current = mCurrent-stepNumber;
        }
        return mVisibleStep.get(current);
    }

}
