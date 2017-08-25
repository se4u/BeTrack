package com.app.uni.betrack;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: ConfigSettingsBetrack</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">ConfigSettingsBetrack
 * API Guide</a> for more information on developing a ConfigSettingsBetrack UI.
 */
public class ActivitySettings extends ActivityAppCompatPreference {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */

    private static SettingsBetrack ObjSettingsBetrack;
    private static SettingsStudy ObjSettingsStudy;
    private static int cntAppearanceNotifMenu = 0;
    private String CallingActivity;
    private static boolean PatchOreo = false;

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);


            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        PatchOreo = false;
        if (ObjSettingsBetrack == null)  {
            ObjSettingsBetrack = SettingsBetrack.getInstance();
        }
        if (ObjSettingsStudy == null) {
            ObjSettingsStudy = SettingsStudy.getInstance(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (PatchOreo == false) {
                    finish();
                } else {
                    PatchOreo = true;
                }
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {

        loadHeadersFromResource(R.xml.pref_headers, target);

        if (cntAppearanceNotifMenu == 5) {
            Header headerNotification = new Header();
            headerNotification.fragment = "com.app.uni.betrack.ActivitySettings$NotificationPreferenceFragment";
            headerNotification.title = getResources().getString(R.string.pref_header_notifications);
            headerNotification.iconRes = R.drawable.ic_notifications_black_24dp;
            target.add(headerNotification);
            cntAppearanceNotifMenu = 0;
        }
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName)
                || YourIdPreferenceFragment.class.getName().equals(fragmentName)
                || InfoPreferenceFragment.class.getName().equals(fragmentName)
                || CreditsPreferenceFragment.class.getName().equals(fragmentName);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class InfoPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_info);
            setHasOptionsMenu(true);
            Preference etp = findPreference("pref_info");
            PatchOreo = true;
            etp.setSummary(ObjSettingsStudy.getStudyDescription());
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                getActivity().onBackPressed();
                PatchOreo = false;
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {

        SharedPreferences prefs;
        SharedPreferences.OnSharedPreferenceChangeListener listener;
        private static Context mContext;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);
            mContext = this.getActivity();
            PatchOreo = true;
            prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            registerPreferenceListener();
            setHasOptionsMenu(true);
        }

        private void registerPreferenceListener()
        {
            listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    ObjSettingsBetrack.Update(mContext);
                }
            };
            prefs.registerOnSharedPreferenceChangeListener(listener);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                getActivity().onBackPressed();
                PatchOreo = false;
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class YourIdPreferenceFragment extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_user_id);
            setHasOptionsMenu(true);
            PatchOreo = true;
            Preference etp = findPreference("pref_user_id");
            etp.setSummary(ObjSettingsStudy.getIdUser());
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                cntAppearanceNotifMenu++;
                getActivity().onBackPressed();
                PatchOreo = false;
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class CreditsPreferenceFragment extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_credit);
            PatchOreo = true;
            setHasOptionsMenu(true);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                getActivity().onBackPressed();
                PatchOreo = false;
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        SharedPreferences prefs;
        SharedPreferences.OnSharedPreferenceChangeListener listener;
        private static Context mContext;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            mContext = this.getActivity();
            prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            registerPreferenceListener();
            PatchOreo = true;
            setHasOptionsMenu(true);

        }

        private void registerPreferenceListener()
        {
            listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    ObjSettingsBetrack.Update(mContext);
                    if (key.equals(mContext.getString(R.string.pref_key_study_notification_time))) {
                        CreateNotification.CreateAlarm(mContext,
                                ObjSettingsBetrack.GetStudyNotification(),
                                ObjSettingsBetrack.GetStudyNotificationTime(),
                                true);
                    }

                }
            };
            prefs.registerOnSharedPreferenceChangeListener(listener);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                getActivity().onBackPressed();
                PatchOreo = false;
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {

        SharedPreferences prefs;
        SharedPreferences.OnSharedPreferenceChangeListener listener;
        private static Context mContext;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);
            mContext = this.getActivity();
            prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            registerPreferenceListener();
            PatchOreo = true;
            setHasOptionsMenu(true);
        }

        private void registerPreferenceListener()
        {
            listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    ObjSettingsBetrack.Update(mContext);
                }
            };
            prefs.registerOnSharedPreferenceChangeListener(listener);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                getActivity().onBackPressed();
                PatchOreo = false;
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

    }


}
