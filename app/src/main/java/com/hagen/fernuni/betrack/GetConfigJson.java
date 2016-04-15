package com.hagen.fernuni.betrack;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cevincent on 4/13/16.
 */
public class GetConfigJson extends AsyncTask<String, Void, String> {

    static final String TAG = "GetConfigJson";
    static public String StudyName;

    @Override protected String doInBackground(String... params) {
        InputStream inputStream = null;
        String result = null;
        HttpURLConnection urlConnection = null;
        java.net.URL url;
        //ArrayList items = new ArrayList();
        try {

            url = new URL("http://www.ricphoto.fr/ReadConfig.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));

            String next;
            while ((next = bufferedReader.readLine()) != null) {
                JSONArray ja = new JSONArray(next);

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = (JSONObject) ja.get(i);
                    //items.add(jo.getString("StudyName"));
                    Log.d(TAG, "StudyName: " + jo.getString("StudyName"));
                    StudyName = jo.getString("StudyName");
                }

            }
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
