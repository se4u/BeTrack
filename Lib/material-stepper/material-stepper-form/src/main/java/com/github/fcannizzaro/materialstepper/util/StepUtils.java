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

    public void add(AbstractStep step, boolean state) {
        mSteps.add(step);
        mVisibleStep.add(state);
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

    public boolean nextVisible() {
        return mVisibleStep.get(mCurrent+1);
    }

    public void setNextVisibility(boolean state) {
        mVisibleStep.set(mCurrent+1, state);
    }

    public boolean previousVisible() {
        int current = 0;
        if (mCurrent > 0) {
            current = mCurrent-1;
        }
        return mVisibleStep.get(current);
    }

}
