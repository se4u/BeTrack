package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Semaphore;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by cevincent on 6/3/16.
 */
public class PostDataAvailable  {
    static final String TAG = "PostDataAvailable";

    public static final Semaphore SemUpdateServer = new Semaphore(1, true);
    public static LocalDataBase localdatabase;

    public static LocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    public static final void Start()
    {
        InputStream inputStream = null;
        String result = null;
        HttpURLConnection urlConnection = null;
        ContentValues values = new ContentValues();
        java.net.URL url;
        BufferedWriter writer = null;
        String AppName;
        String StartDate;
        String StopDate;
        String StartTime;
        String StopTime;
        Long IdSql;

        while(true)
        {
            values.clear();
            values = AccesLocalDB().getOldestElementDb(LocalDataBase.TABLE_APPWATCH);
            if (0 != values.size()) {

                try {
                //Connect to the remote database to get the available studies
                url = new URL("http://www.ricphoto.fr/BeTrackPostAppWatch.php?"/*SettingsBetrack.STUDY_WEBSITE + "BeTrackPostAppData.php"SettingsBetrack.STUDY_POSTAPPWATCHED*/);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setReadTimeout(SettingsBetrack.SERVER_TIMEOUT);
                urlConnection.setConnectTimeout(SettingsBetrack.SERVER_TIMEOUT);

                IdSql = values.getAsLong(LocalDataBase.C_APPWATCH_ID);
                AppName = values.get(LocalDataBase.C_APPWATCH_APPLICATION).toString();
                StartDate = values.get(LocalDataBase.C_APPWATCH_DATESTART).toString();
                StopDate = values.get(LocalDataBase.C_APPWATCH_DATESTOP).toString();
                StartTime = values.get(LocalDataBase.C_APPWATCH_TIMESTART).toString();
                StopTime = values.get(LocalDataBase.C_APPWATCH_TIMESTOP).toString();

                Log.d(TAG, "PHP request: " + SettingsBetrack.STUDY_WEBSITE + SettingsBetrack.STUDY_POSTAPPWATCHED + "?" +
                        "application=" + AppName + "&datestart=" + StartDate + "&datestop=" + StopDate + "&timestart=" + StartTime + "&timestop=" + StopTime);

                Uri.Builder builder = new Uri.Builder()
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
                    AccesLocalDB().deleteELement(LocalDataBase.TABLE_APPWATCH, IdSql);
                }

                } catch (java.net.SocketTimeoutException e) {
                    Log.d(TAG, "Unable to access to server... ");
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
            else
            {
                Log.d(TAG, "Database has been transfered to the remote server, we stop");
                break;
            }
        }

        SemUpdateServer.release();

    }

}
