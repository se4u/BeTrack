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
 * Created by cevincent on 5/6/16.
 */
public class NetworkGetWhatToWatch extends AsyncTask<String, Void, String> {
    static final String TAG = "NetworkGetWhatToWatch";
    static public ConfigInfoStudy ContextInfoStudy;

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public NetworkGetWhatToWatch(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

    @Override protected String doInBackground(String... params) {
        InputStream inputStream = null;
        String result = null;
        HttpURLConnection urlConnection = null;
        java.net.URL url;
        try {

            //Connect to the remote database to get the available studies
            url = new URL(ConfigSettingsBetrack.STUDY_WEBSITE + ConfigSettingsBetrack.STUDY_GETAPPTOWATCH);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(ConfigSettingsBetrack.SERVER_TIMEOUT);
            urlConnection.connect();


            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));

            String next;

            while ((next = bufferedReader.readLine()) != null) {
                JSONArray ja = new JSONArray(next);

                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jo = (JSONObject) ja.get(i);
                    ContextInfoStudy.ApplicationsToWatch.add(jo.getString("ApplicationName"));

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

}
