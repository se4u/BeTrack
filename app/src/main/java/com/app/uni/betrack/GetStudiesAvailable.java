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
 * Created by cevincent on 4/13/16.
 */
public class GetStudiesAvailable extends AsyncTask<String, Void, String> {

    static final String TAG = "GetStudiesAvailable";
    static public String[] StudyID;
    static public String[] StudyActive;
    static public String[] StudyName;
    static public String[] StudyDescription;
    static public int NbrStudyAvailable;
    static public final int NbrMaxStudy = 3;

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public GetStudiesAvailable(AsyncResponse delegate){
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

            StudyID=new String[NbrMaxStudy];
            StudyActive=new String[NbrMaxStudy];
            StudyName=new String[NbrMaxStudy];
            StudyDescription=new String[NbrMaxStudy];

            //Connect to the remote database to get the available studies
            url = new URL(SettingsBetrack.STUDY_WEBSITE + "BeTrackGetStudiesAvailable.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(SettingsBetrack.SERVER_TIMEOUT);
            urlConnection.connect();


            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));

            String next;
            NbrStudyAvailable = 0;
            while ((next = bufferedReader.readLine()) != null) {
                JSONArray ja = new JSONArray(next);

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = (JSONObject) ja.get(i);
                    StudyActive[i] = jo.getString("StudyActive");
                    if (StudyActive[i].equals("1")) {
                        StudyID[i] = jo.getString("StudyId");
                        StudyName[i] = jo.getString("StudyName");
                        StudyDescription[i] = jo.getString("StudyDescription");
                        NbrStudyAvailable++;
                        //We limit the number of study to NbrMaxStudy
                        if (NbrStudyAvailable > NbrMaxStudy) break;
                    }

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
