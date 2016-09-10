package com.app.uni.betrack;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ActivitySetupBetrack extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1001;
    private SettingsStudy ObjSettingsStudy;
    private boolean EnableUsageStat = false;
    private boolean EnableHuaweiProtMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_betrack);

        ObjSettingsStudy = SettingsStudy.getInstance(this);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            if(!hasPermission()) {
                findViewById(R.id.EnableUsageStat).setVisibility(View.VISIBLE);
            }
            else {
                findViewById(R.id.EnableUsageStat).setVisibility(View.GONE);
                EnableUsageStat = true;
            }

            Intent intent = new Intent();
            intent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            if (isCallable(intent)) {
                findViewById(R.id.EnableHuawei).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.EnableHuawei).setVisibility(View.GONE);
                EnableHuaweiProtMode = true;
            }
        }
    }

    @Override
    public void onResume() {
        Intent i;
        super.onResume();
        if(!hasPermission()) {
            findViewById(R.id.EnableUsageStat).setVisibility(View.VISIBLE);
        }
        else  {
            findViewById(R.id.EnableUsageStat).setVisibility(View.GONE);
            EnableUsageStat = true;
            if (EnableHuaweiProtMode && EnableUsageStat) {
                ObjSettingsStudy.setSetupBetrackDone(true);
                i = new Intent(ActivitySetupBetrack.this, ActivitySurveyStart.class);
                startActivity(i);
                finish();
            } else {
                ObjSettingsStudy.setSetupBetrackDone(false);
            }
        }
    }

    public void onButtonClicked(View view) {
        Intent i;
        switch (view.getId()) {
            case  R.id.EnableUsageStatBetrack:
                this.startActivityForResult(
                        new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                        MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
                break;
            case  R.id.EnableHuaweiBetrack:
                huaweiProtectedApps();
                if (ObjSettingsStudy.getSetupBetrackDone()) {
                    i = new Intent(ActivitySetupBetrack.this, ActivitySurveyStart.class);
                    startActivity(i);
                    finish();
                }
                break;
        }
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean isChecked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case  R.id.CheckHuaweiWhiteList:
                if ( isChecked ) {
                    EnableHuaweiProtMode = true;
                } else {
                    EnableHuaweiProtMode = false;
                }
                if (EnableHuaweiProtMode && EnableUsageStat) {
                    ObjSettingsStudy.setSetupBetrackDone(true);
                } else {
                    ObjSettingsStudy.setSetupBetrackDone(false);
                }
                break;
        }
    }

    @TargetApi(19) private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void huaweiProtectedApps() {
        try {
            String cmd = "am start -n com.huawei.systemmanager/.optimize.process.ProtectActivity";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                cmd += " --user " + getUserSerial();
            }
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ignored) {
        }
    }

    private String getUserSerial() {
        //noinspection ResourceType
        Object userManager = getSystemService("user");
        if (null == userManager) return "";

        try {
            Method myUserHandleMethod = android.os.Process.class.getMethod("myUserHandle", (Class<?>[]) null);
            Object myUserHandle = myUserHandleMethod.invoke(android.os.Process.class, (Object[]) null);
            Method getSerialNumberForUser = userManager.getClass().getMethod("getSerialNumberForUser", myUserHandle.getClass());
            Long userSerial = (Long) getSerialNumberForUser.invoke(userManager, myUserHandle);
            if (userSerial != null) {
                return String.valueOf(userSerial);
            } else {
                return "";
            }
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ignored) {
        }
        return "";
    }

    private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
