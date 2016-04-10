package com.hagen.fernuni.betrack;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

public class BeTrackActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1001;
    private static final String TAG = "Status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betrack);

        new Eula(this).show();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            if(!hasPermission()){
                startActivityForResult(
                        new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                        MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
            }
        }

        //Intent msgIntent = new Intent(this, TrackIntentService.class);
        //startService(msgIntent);
    }

    @TargetApi(19) private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settingsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.settings_menu:
                startActivity(new Intent(this, SettingsActivity.class));
        }
        return true;
    }
}
