package com.app.uni.betrack;


import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cevincent on 4/13/16.
 */
public class NetworkGetStudiesAvailable extends AsyncTask<String, Void, String> {

    static final String TAG = "NetworkGetStudiesAvailable";
    private Context mContext;
    private SettingsStudy ObjSettingsStudy;

    static private String[] StudyID;
    static private String[] StudyActive;
    static private String[] StudyName;
    static private String[] StudyDescription;
    static private String[] StudyVersionApp;
    static private String[] StudyDuration;
    static private String[] StudyPublicKey;
    static private String[] StudyContactEmail;

    static private int NbrStudyAvailable;
    static private final int NbrMaxStudy = 3;

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public NetworkGetStudiesAvailable(Context context, AsyncResponse delegate){
        mContext = context;
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

    @Override protected String doInBackground(String... params) {
        String result = null;
        HttpURLConnection urlConnection = null;
        java.net.URL url;
        ObjSettingsStudy = SettingsStudy.getInstance(mContext);

        try {

            StudyID=new String[NbrMaxStudy];
            StudyActive=new String[NbrMaxStudy];
            StudyName=new String[NbrMaxStudy];
            StudyDescription=new String[NbrMaxStudy];
            StudyVersionApp=new String[NbrMaxStudy];
            StudyDuration=new String[NbrMaxStudy];
            StudyPublicKey=new String[NbrMaxStudy];
            StudyContactEmail=new String[NbrMaxStudy];

            //Connect to the remote database to get the available studies
            url = new URL(SettingsBetrack.STUDY_WEBSITE + SettingsBetrack.STUDY_GETSTUDIESAVAILABLE);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(SettingsBetrack.SERVER_TIMEOUT);
            urlConnection.connect();


            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));

            //We keep this code for later when we might have more than one study running in
            //the same database
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
                        StudyVersionApp[i] = jo.getString("VersionApp");
                        StudyDuration[i] = jo.getString("Duration");
                        StudyPublicKey[i] = jo.getString("PublicKey");
                        StudyContactEmail[i] = jo.getString("ContactEmail");
                        NbrStudyAvailable++;
                        //We limit the number of study to NbrMaxStudy
                        if (NbrStudyAvailable > NbrMaxStudy) break;
                    }
                }
            }

            //We just take in account the first study in the table
            ObjSettingsStudy.setStudyId(StudyID[0]);
            ObjSettingsStudy.setStudyName(StudyName[0]);
            ObjSettingsStudy.setStudyDescription(StudyDescription[0]);
            ObjSettingsStudy.setStudyVersionApp(StudyVersionApp[0]);
            ObjSettingsStudy.setStudyDuration(Integer.parseInt(StudyDuration[0]));
            ObjSettingsStudy.setStudyPublicKey(StudyPublicKey[0]);
            ObjSettingsStudy.setStudyContactEmail(StudyContactEmail[0]);

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
