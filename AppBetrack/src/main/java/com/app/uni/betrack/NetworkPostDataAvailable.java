package com.app.uni.betrack;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Semaphore;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by cevincent on 6/3/16.
 */
public class NetworkPostDataAvailable {
    static final String TAG = "NetworkPostDataAvailable";

    public static final Semaphore SemUpdateServer = new Semaphore(1, true);
    public static UtilsLocalDataBase localdatabase;

    public static UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    private static final char TABLE_APPWATCH_TRANSFERED = 1;
    private static final char TABLE_USER_TRANSFERED = 2;

    public static final void Start()
    {
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

        java.net.URL urlPostAppwatched = null;
        java.net.URL urlPostDailyStatus = null;

        char TaskDone = TABLE_APPWATCH_TRANSFERED | TABLE_USER_TRANSFERED;

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
                        UserId = ConfigInfoStudy.IdUser;
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

                } catch (java.net.SocketTimeoutException e) {
                    Log.d(TAG, "Unable to access to server... ");
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
                        UserId = ConfigInfoStudy.IdUser;
                    }
                    else
                    {
                        TaskDone &= ~TABLE_USER_TRANSFERED;
                        Log.d(TAG, "User ID for table user not accessible yet we'll try later");
                        break;
                    }

                    PeriodStatus = values.get(UtilsLocalDataBase.C_USER_PERIOD).toString();
                    Date = values.get(UtilsLocalDataBase.C_USER_DATE).toString();

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
                    writer.write(query,0, query.length());
                    writer.flush();
                    writer.close();

                    if (HttpsURLConnection.HTTP_OK == urlConnection.getResponseCode()) {
                        AccesLocalDB().deleteELement(UtilsLocalDataBase.TABLE_USER, IdSql);
                    }

                } catch (java.net.SocketTimeoutException e) {
                    Log.d(TAG, "Unable to access to server... ");
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

        SemUpdateServer.release();

    }

}
