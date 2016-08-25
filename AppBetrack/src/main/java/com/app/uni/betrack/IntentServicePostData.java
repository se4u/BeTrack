package com.app.uni.betrack;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by cedoctet on 23/08/2016.
 */
public class IntentServicePostData extends IntentService {
    static final String TAG = "IntentServicePostData";

    private static ConfigSettingsBetrack ObjSettingsBetrack = null;

    private static UtilsLocalDataBase localdatabase  = null;

    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    private enum ConnectionState {
        NONE,
        WIFI,
        LTE,
    };

    private static final char TABLE_APPWATCH_TRANSFERED = 1;
    private static final char TABLE_USER_TRANSFERED = 2;

    private boolean FastCheckDataConnection = false;

    Handler mHandler;

    public IntentServicePostData()  {
        super("IntentServicePostData");
        mHandler = new Handler();
    }


    protected void onHandleIntent(Intent intent) {
        InputStream inputStream = null;
        String result = null;
        HttpURLConnection urlConnection = null;
        ContentValues values = new ContentValues();
        BufferedWriter writer = null;
        //TABLE_APPWATCH
        String AppName;
        String StartDate;
        String StopDate;
        String StartTime;
        String StopTime;
        //TABLE_USER
        String PeriodStatus;
        String Date;
        //COMMON TO ALL TABLES
        String UserId;
        Long IdSql;
        ConnectionState NetworkState;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        java.net.URL urlPostAppwatched;
        java.net.URL urlPostDailyStatus;

        char TaskDone = TABLE_APPWATCH_TRANSFERED | TABLE_USER_TRANSFERED;

        //Check if there is a data connection
        NetworkState = hasNetworkConnection();

        if (null == ObjSettingsBetrack) {
            //Read the preferences
            ObjSettingsBetrack = ConfigSettingsBetrack.getInstance();
            ObjSettingsBetrack.UpdateSettingsBetrack(prefs, this);
        }
        if (null == localdatabase) {
            localdatabase = new UtilsLocalDataBase(this);
        }

        //Log.d(TAG, "onHandleIntent");

        // To transfer the data either we have access to a WIFI network or
        // we have are allowed to use the 3G/LTE
        if ((ConnectionState.WIFI == NetworkState) ||
                ((ConnectionState.LTE == NetworkState) && (ObjSettingsBetrack.GetEnableDataUsage())))
        {
            if (true == FastCheckDataConnection)  {
                FastCheck(false);
            }

            while(TaskDone != 0)
            {
                values.clear();
                values = AccesLocalDB().getOldestElementDb(UtilsLocalDataBase.TABLE_APPWATCH);
                if (0 != values.size()) {

                    try {

                        urlPostAppwatched = new URL(ConfigSettingsBetrack.STUDY_WEBSITE + ConfigSettingsBetrack.STUDY_POSTAPPWATCHED +"?");
                        urlConnection = (HttpURLConnection) urlPostAppwatched.openConnection();
                        urlConnection.setRequestMethod("POST");
                        urlConnection.setRequestProperty("Content-Type",
                                "application/x-www-form-urlencoded");
                        urlConnection.setDoOutput(true);
                        urlConnection.setDoInput(true);
                        urlConnection.setReadTimeout(ConfigSettingsBetrack.SERVER_TIMEOUT);
                        urlConnection.setConnectTimeout(ConfigSettingsBetrack.SERVER_TIMEOUT);

                        IdSql = values.getAsLong(UtilsLocalDataBase.C_APPWATCH_ID);

                        if (null != ConfigInfoStudy.IdUser) {
                            UserId = prefs.getString(ConfigInfoStudy.ID_USER, "NOID ?????");
                            ConfigInfoStudy.IdUser = UserId;
                        }
                        else
                        {
                            TaskDone &= ~TABLE_APPWATCH_TRANSFERED;
                            Log.d(TAG, "User ID for table appwatch not accessible yet we'll try later");
                            break;
                        }

                        AppName = values.get(UtilsLocalDataBase.C_APPWATCH_APPLICATION).toString();
                        StartDate = values.get(UtilsLocalDataBase.C_APPWATCH_DATESTART).toString();
                        StartTime = values.get(UtilsLocalDataBase.C_APPWATCH_TIMESTART).toString();
                        StopDate = values.get(UtilsLocalDataBase.C_APPWATCH_DATESTOP).toString();
                        StopTime = values.get(UtilsLocalDataBase.C_APPWATCH_TIMESTOP).toString();

                        mHandler.post(new UtilsDisplayToast(this, "Betrack: Post app watched: " + AppName));

                        Log.d(TAG, "PHP request: " + ConfigSettingsBetrack.STUDY_WEBSITE + ConfigSettingsBetrack.STUDY_POSTAPPWATCHED + "?" +
                                "userid=" + UserId + "&application=" + AppName + "&datestart=" + StartDate + "&datestop=" + StopDate + "&timestart=" + StartTime + "&timestop=" + StopTime);

                        Uri.Builder builder = new Uri.Builder()
                                .appendQueryParameter("userid", UserId)
                                .appendQueryParameter("application", AppName)
                                .appendQueryParameter("datestart", StartDate)
                                .appendQueryParameter("datestop", StopDate)
                                .appendQueryParameter("timestart", StartTime)
                                .appendQueryParameter("timestop", StopTime)
                                ;
                        String query = builder.build().getEncodedQuery();

                        writer = new BufferedWriter(
                                new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                        writer.write(query,0, query.length());
                        writer.flush();
                        writer.close();

                        if (HttpsURLConnection.HTTP_OK == urlConnection.getResponseCode()) {
                            AccesLocalDB().deleteELement(UtilsLocalDataBase.TABLE_APPWATCH, IdSql);
                        }
                        else {
                            Log.d(TAG, "Unable to access to server... ");
                            //Check if an internet connection is available more often to get more chance to be able to transfer the data
                            FastCheck(true);
                        }

                    } catch (java.net.SocketTimeoutException e) {
                        Log.d(TAG, "Unable to access to server... ");
                        //Check if an internet connection is available more often to get more chance to be able to transfer the data
                        FastCheck(true);
                        break;
                    } catch (Exception e) {
                        //Log.d(TAG, "Last request not completed we don't transfer the data ");
                        break;
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                }
                else
                {
                    TaskDone &= ~TABLE_APPWATCH_TRANSFERED;
                    //Log.d(TAG, "TABLE_APPWATCH has been transfered to the remote server, we can stop");
                }

                values.clear();
                values = AccesLocalDB().getOldestElementDb(UtilsLocalDataBase.TABLE_USER);
                if (0 != values.size()) {
                    try {
                        //Connect to the remote database to get the available studies
                        urlPostDailyStatus = new URL(ConfigSettingsBetrack.STUDY_WEBSITE + ConfigSettingsBetrack.STUDY_POSTDAILYSTATUS +"?");
                        urlConnection = (HttpURLConnection) urlPostDailyStatus.openConnection();
                        urlConnection.setRequestMethod("POST");
                        urlConnection.setRequestProperty("Content-Type",
                                "application/x-www-form-urlencoded");
                        urlConnection.setDoOutput(true);
                        urlConnection.setDoInput(true);
                        urlConnection.setReadTimeout(ConfigSettingsBetrack.SERVER_TIMEOUT);
                        urlConnection.setConnectTimeout(ConfigSettingsBetrack.SERVER_TIMEOUT);

                        IdSql = values.getAsLong(UtilsLocalDataBase.C_USER_ID);

                        if (null != ConfigInfoStudy.IdUser) {
                            UserId = prefs.getString(ConfigInfoStudy.ID_USER, "NOID ?????");
                            ConfigInfoStudy.IdUser = UserId;
                        }
                        else
                        {
                            TaskDone &= ~TABLE_USER_TRANSFERED;
                            Log.d(TAG, "User ID for table user not accessible yet we'll try later");
                            break;
                        }

                        PeriodStatus = values.get(UtilsLocalDataBase.C_USER_PERIOD).toString();
                        Date = values.get(UtilsLocalDataBase.C_USER_DATE).toString();

                        mHandler.post(new UtilsDisplayToast(this, "Betrack: Post survey status: " + PeriodStatus));

                        Log.d(TAG, "PHP request: " + ConfigSettingsBetrack.STUDY_WEBSITE + ConfigSettingsBetrack.STUDY_POSTDAILYSTATUS + "?" +
                                "userid=" + UserId + "&periodstatus=" + PeriodStatus + "&date=" + Date);

                        Uri.Builder builder = new Uri.Builder()
                                .appendQueryParameter("userid", UserId)
                                .appendQueryParameter("periodstatus", PeriodStatus)
                                .appendQueryParameter("date", Date)
                                ;
                        String query = builder.build().getEncodedQuery();

                        writer = new BufferedWriter(
                                new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                        writer.write(query, 0, query.length());
                        writer.flush();
                        writer.close();

                        if (HttpsURLConnection.HTTP_OK == urlConnection.getResponseCode()) {
                            AccesLocalDB().deleteELement(UtilsLocalDataBase.TABLE_USER, IdSql);
                        }
                        else {
                            Log.d(TAG, "Unable to access to server... ");
                            //Check if an internet connection is available more often to get more chance to be able to transfer the data
                            FastCheck(true);
                        }


                    } catch (java.net.SocketTimeoutException e) {
                        Log.d(TAG, "Unable to access to server... ");
                        //Check if an internet connection is available more often to get more chance to be able to transfer the data
                        FastCheck(true);
                        break;
                    } catch (Exception e) {
                        //Log.d(TAG, "Last request not completed we don't transfer the data ");
                        break;
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                }
                else
                {
                    TaskDone &= ~TABLE_USER_TRANSFERED;
                    //Log.d(TAG, "TABLE_USER has been transfered to the remote server, we can stop");
                }
            }
        }
        else
        {
            //Check if an internet connection is available more often to get more chance to be able to transfer the data
            FastCheck(true);

        }
        CreatePostData.SemUpdateServer.release();
    }

    private void FastCheck(boolean Enable) {
        FastCheckDataConnection = Enable;
        CreatePostData.CreateAlarm(getApplicationContext(), Enable);
    }

    private ConnectionState hasNetworkConnection() {

        ConnectionState NetworkState = ConnectionState.NONE;

        try {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    //Log.d(TAG, "hasNetworkConnection: WIFI");
                    NetworkState = ConnectionState.WIFI;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    //Log.d(TAG, "hasNetworkConnection: LTE/3G");
                    NetworkState = ConnectionState.LTE;
                }
            }else {
                NetworkInfo[] netInfo = cm.getAllNetworkInfo();
                for (NetworkInfo ni : netInfo) {
                    if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                        if (ni.isConnected()){
                            //Log.d(TAG, "hasNetworkConnection: WIFI");
                            NetworkState = ConnectionState.WIFI;
                        }

                    if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                        if (ni.isConnected()) {
                            //Log.d(TAG, "hasNetworkConnection: LTE/3G");
                            NetworkState = ConnectionState.LTE;
                        }
                }
            }
        }
        finally {
            if (ConnectionState.NONE == NetworkState) {
                //Log.d(TAG, "hasNetworkConnection: nope");
            }
            return NetworkState;
        }
    }
}
