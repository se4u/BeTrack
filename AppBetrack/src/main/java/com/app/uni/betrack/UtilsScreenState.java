package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;

import java.util.concurrent.Semaphore;

/**
 * Created by cvincent on 07/10/17.
 */

public class UtilsScreenState {

    private static final Semaphore SemUpdateScreenState= new Semaphore(1, true);

    public enum StateScreen {
        UNKNOWN, OFF, ON, UNLOCKED
    }

    class State {
        Context context;

        // Constructor
        public State(Context context) {
            this.context = context;
        }

        private StateScreen ScreenState = StateScreen.UNKNOWN;

        public void setScreenState(StateScreen statescreen) {
            try {

                SemUpdateScreenState.acquire();
                ScreenState = statescreen;
            } catch (InterruptedException e) {
                SemUpdateScreenState.release();
            } finally {
                SemUpdateScreenState.release();
            }
        }
        public StateScreen getScreenState() {
            StateScreen localStateScreen;
            try {
                SemUpdateScreenState.acquire();
            } catch (InterruptedException e) {
                SemUpdateScreenState.release();
                return StateScreen.UNKNOWN;

            } finally {
                localStateScreen = ScreenState;
                SemUpdateScreenState.release();
                return localStateScreen;
            }

        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)  public StateScreen CheckScreenStatusFromLollipop(Context context) {
            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            boolean locked = km.inKeyguardRestrictedInputMode();
            StateScreen localStateScreen = StateScreen.UNKNOWN;
            DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
            for (Display display : dm.getDisplays()) {
                if (display.getState() != Display.STATE_OFF) {
                    if (!locked) {
                        localStateScreen = StateScreen.UNLOCKED;
                    } else {
                        localStateScreen = StateScreen.ON;
                    }
                    break;
                }
                else
                {
                    localStateScreen = StateScreen.OFF;
                    break;
                }
            }
            return localStateScreen;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)  public StateScreen CheckScreenStatusFromIceCream(Context context) {
            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            boolean locked = km.inKeyguardRestrictedInputMode();
            PowerManager powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
            StateScreen localStateScreen = StateScreen.UNKNOWN;
            if  (powerManager.isScreenOn()) {
                if (!locked) {
                    localStateScreen = StateScreen.UNLOCKED;
                } else {
                    localStateScreen = StateScreen.ON;
                }
            }
            else
            {
                localStateScreen = StateScreen.OFF;

            }
            return localStateScreen;
        }

    }

    private final State state; //

    public UtilsScreenState(Context context) {
        this.state = new UtilsScreenState.State(context);
    }

    public void UtilsSetSavedScreenState(StateScreen stateScreen) {
        this.state.setScreenState(stateScreen);
    }

    public StateScreen UtilsGetSavedScreenState() {
        return this.state.getScreenState();
    }

    public void UtilsUpdateScreenStateFromHardware(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.state.setScreenState(this.state.CheckScreenStatusFromLollipop(context));
        } else {
            this.state.setScreenState(this.state.CheckScreenStatusFromIceCream(context));
        }
    }

}
