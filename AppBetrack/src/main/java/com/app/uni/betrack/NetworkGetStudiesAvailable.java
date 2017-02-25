package com.app.uni.betrack;


import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Iterator;

import static android.R.id.list;

/**
 * Created by cevincent on 4/13/16.
 */
public class NetworkGetStudiesAvailable extends AsyncTask<String, Void, String> {

    static final String TAG = "NetworkGetStudiesAvailable";
    private Context mContext;
    private SettingsStudy ObjSettingsStudy = null;
    private SettingsBetrack ObjSettingsBetrack = null;

    static private String[] StudyID;
    static private String[] StudyActive;
    static private String[] StudyName;
    static private String[] StudyDescription;
    static private String[] StudyVersionApp;
    static private String[] StudyDuration;
    static private String[] StudyPublicKey;
    static private String[] StudyContactEmail;
    static private String[] StudySignature;

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

        if (null == ObjSettingsBetrack) {
            //Read the preferences
            ObjSettingsBetrack = SettingsBetrack.getInstance();
            ObjSettingsBetrack.Update(mContext);
        }

        try {

            StudyID=new String[NbrMaxStudy];
            StudyActive=new String[NbrMaxStudy];
            StudyName=new String[NbrMaxStudy];
            StudyDescription=new String[NbrMaxStudy];
            StudyVersionApp=new String[NbrMaxStudy];
            StudyDuration=new String[NbrMaxStudy];
            StudyPublicKey=new String[NbrMaxStudy];
            StudyContactEmail=new String[NbrMaxStudy];
            StudySignature=new String[NbrMaxStudy];

            //Connect to the remote database to get the available studies
            url = new URL(SettingsBetrack.STUDY_WEBSITE + SettingsBetrack.STUDY_GETSTUDIESAVAILABLE);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(SettingsBetrack.SERVER_TIMEOUT);
            urlConnection.connect();


            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));


            String JsonLine;
            NbrStudyAvailable = 0;
            JsonLine = bufferedReader.readLine();
            JSONArray ja = new JSONArray(JsonLine);

            JSONObject jo = (JSONObject) ja.get(0);
            StudyActive[0] = jo.getString("StudyActive");
            StudyID[0] = jo.getString("StudyId");
            StudyName[0] = jo.getString("StudyName");
            StudyDescription[0] = jo.getString("StudyDescription");
            StudyVersionApp[0] = jo.getString("VersionApp");
            StudyDuration[0] = jo.getString("Duration");
            StudyPublicKey[0] = jo.getString("PublicKey");
            StudyContactEmail[0] = jo.getString("ContactEmail");

            //Compute the sha of the JSON string received
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            String[] toBeCheck = JsonLine.split("[}]");
            toBeCheck[0] += "}]";
            byte[] hash_computed = digest.digest(toBeCheck[0].getBytes("UTF-8"));

            //Read the signature
            jo = (JSONObject) ja.get(1);
            StudySignature[0] = jo.getString("Signature");

            //Decrypt the signature with the public key (String pemString, byte[] data, Context context)
            byte[] dataBytes = StudySignature[0].toString().getBytes("utf-8");
            byte[] hash_server = UtilsCryptoRSA.decryptWithPublicKey(ObjSettingsBetrack.STUDY_PUBLIC_KEY, dataBytes, mContext);
            boolean sha_equal = true;
            //Compare both SHA
            for (int i = 0; i < 32; i++)
            {
                if (hash_computed[i] != hash_server[i + 19])
                {
                    sha_equal = false;
                    break;
                }
            }

            if (sha_equal == true) {
                //We just take in account the first study in the table
                ObjSettingsStudy.setStudyId(StudyID[0]);
                ObjSettingsStudy.setStudyName(StudyName[0]);
                ObjSettingsStudy.setStudyDescription(StudyDescription[0]);
                ObjSettingsStudy.setStudyVersionApp(StudyVersionApp[0]);
                ObjSettingsStudy.setStudyDuration(Integer.parseInt(StudyDuration[0]));
                ObjSettingsStudy.setStudyPublicKey(StudyPublicKey[0]);
                ObjSettingsStudy.setStudyContactEmail(StudyContactEmail[0]);

                result = "OK";
            }
            else {
                result = null;
            }

        } catch (java.net.SocketTimeoutException e) {
            return result;
        } catch (Exception e) {
            return result;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }


        return result;
    }

}
