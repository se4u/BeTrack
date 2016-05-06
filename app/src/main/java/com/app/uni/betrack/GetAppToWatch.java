package com.app.uni.betrack;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by cevincent on 5/6/16.
 */
public class GetAppToWatch  extends AsyncTask<String, Void, String> {
    static final String TAG = "GetAppToWatch";
    static ArrayList<String> ApplicationName = new ArrayList<String>();

    @Override protected String doInBackground(String... params) {
        InputStream inputStream = null;
        String result = null;
        HttpURLConnection urlConnection = null;
        java.net.URL url;
        try {

            //Connect to the remote database to get the available studies
            url = new URL("http://www.ricphoto.fr/BeTrackGetAppToWatch.php?table_name=TestPeriod_applications"); //TODO hardcoded has to be change
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();


            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));

            String next;

            while ((next = bufferedReader.readLine()) != null) {
                JSONArray ja = new JSONArray(next);

                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jo = (JSONObject) ja.get(i);
                    ApplicationName.add(jo.getString("StudyActive"));

                }


            }
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
    protected void onPostExecute(String feed) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}
