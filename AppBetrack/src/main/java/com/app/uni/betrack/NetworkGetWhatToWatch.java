package com.app.uni.betrack;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

/**
 * Created by cevincent on 5/6/16.
 */
public class NetworkGetWhatToWatch extends AsyncTask<String, Void, String> {
    static final String TAG = "NetworkGetWhatToWatch";
    private SettingsStudy ObjSettingsStudy;
    private SettingsBetrack ObjSettingsBetrack = null;

    private Context mContext;

    static private String StudySignature;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public NetworkGetWhatToWatch(Context context, AsyncResponse delegate){
        mContext = context;
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
        ObjSettingsStudy = SettingsStudy.getInstance(mContext);

        if (null == ObjSettingsBetrack) {
            //Read the preferences
            ObjSettingsBetrack = SettingsBetrack.getInstance();
            ObjSettingsBetrack.Update(mContext);
        }

        try {

            //Connect to the remote database to get the available studies
            url = new URL(SettingsBetrack.STUDY_WEBSITE + SettingsBetrack.STUDY_GETAPPTOWATCH);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(SettingsBetrack.SERVER_TIMEOUT);
            urlConnection.connect();

            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));

            String JsonLine;
            JsonLine = bufferedReader.readLine();

            JSONArray ja = new JSONArray(JsonLine);
            JSONObject jo;
            for (int i = 0; i < (ja.length()-1); i++) {
                jo = (JSONObject) ja.get(i);
                ObjSettingsStudy.setApplicationsToWatch(jo.getString("ApplicationName"));
            }

            //Compute the sha of the JSON string received
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            String[] toBeCheck = JsonLine.split("[}]");
            String toBeCheckFinal = "";
            for (int i=0; i<(toBeCheck.length-2); i++)
            {
                toBeCheckFinal += toBeCheck[i];
                toBeCheckFinal += "}";
            }
            toBeCheckFinal += "]";
            byte[] hash_computed = digest.digest(toBeCheckFinal.getBytes("UTF-8"));

            //Read the signature
            jo = (JSONObject) ja.get(ja.length()-1);
            StudySignature = jo.getString("Signature");

            //Decrypt the signature with the public key (String pemString, byte[] data, Context context)
            byte[] dataBytes = StudySignature.toString().getBytes("utf-8");
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
                result = "OK";
            } else {
                result = null;
            }
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
