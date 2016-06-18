package com.app.uni.betrack;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cevincent on 6/3/16.
 */
public class PostDataAvailable extends AsyncTask<String, Void, String> {
    static final String TAG = "PostStudyData";
    static public InfoStudy ContextInfoStudy;

    @Override protected String doInBackground(String... params) {
        InputStream inputStream = null;
        String result = null;
        HttpURLConnection urlConnection = null;
        java.net.URL url;
        try {

            /*
            http://www.ricphoto.fr/testtine.php?application=Tinder&date=11051980&timestart=1245&timestop=1255
             */
            //Connect to the remote database to get the available studies
            url = new URL(SettingsBetrack.STUDY_WEBSITE + "BeTrackPostAppData.php?table_name=TestPeriod_applications");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(SettingsBetrack.SERVER_TIMEOUT);
            urlConnection.connect();

            result = "OK";
        } catch (java.net.SocketTimeoutException e) {
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }
}
