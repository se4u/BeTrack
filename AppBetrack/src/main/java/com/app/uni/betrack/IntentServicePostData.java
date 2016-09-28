package com.app.uni.betrack;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by cedoctet on 23/08/2016.
 */
public class IntentServicePostData extends IntentService {
    static final String TAG = "IntentServicePostData";
    private static final String LOCK_NAME_STATIC = "com.app.uni.betrack.wakelock.postdata";

    private SettingsBetrack ObjSettingsBetrack = null;
    private SettingsStudy ObjSettingsStudy = null;

    private UtilsLocalDataBase localdatabase = null;
    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    private static volatile PowerManager.WakeLock lockStatic;

    private enum ConnectionState {
        NONE,
        WIFI,
        LTE,
    };

    private static final char TABLE_APPWATCH_TRANSFERED = 1;
    private static final char TABLE_DAILYSTATUS_TRANSFERED = 2;
    private static final char TABLE_STARTSTUDY_TRANSFERED = 4;
    private static final char TABLE_ENDSTUDY_TRANSFERED = 8;
    private static final char TABLE_GPS_TRANSFERED = 16;

    Handler mHandler;

    public IntentServicePostData()  {
        super("IntentServicePostData");
        mHandler = new Handler();

    }


    synchronized private static PowerManager.WakeLock getLock(Context context) {
        if (lockStatic == null) {
            PowerManager mgr = (PowerManager) context.getApplicationContext()
                    .getSystemService(Context.POWER_SERVICE);

            lockStatic = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    LOCK_NAME_STATIC);
            lockStatic.setReferenceCounted(true);
        }
        return (lockStatic);
    }

    protected void onHandleIntent(Intent intent) {
        ContentValues values = new ContentValues();
        Long IdSql;
        ConnectionState NetworkState;
        getLock(getApplicationContext()).acquire();

        char TaskDone = TABLE_APPWATCH_TRANSFERED | TABLE_DAILYSTATUS_TRANSFERED |
                        TABLE_STARTSTUDY_TRANSFERED | TABLE_ENDSTUDY_TRANSFERED |
                        TABLE_GPS_TRANSFERED;

        //Check if there is a data connection
        NetworkState = hasNetworkConnection();

        if (null == localdatabase) {
            localdatabase =  new UtilsLocalDataBase(this);
        }

        if (null == ObjSettingsBetrack) {
            //Read the preferences
            ObjSettingsBetrack = SettingsBetrack.getInstance();
            ObjSettingsBetrack.Update(this);
        }

        if (null == ObjSettingsStudy)  {
            ObjSettingsStudy = SettingsStudy.getInstance(this);
        }

        Log.d(TAG, "try to post the data");

        // To transfer the data either we have access to a WIFI network or
        // we have are allowed to use the 3G/LTE
        if ((ConnectionState.WIFI == NetworkState) ||
                ((ConnectionState.LTE == NetworkState) && (ObjSettingsBetrack.GetEnableDataUsage())))
        {
            while(TaskDone != 0)
            {
                //APPLICATIONS WATCHED
                values.clear();
                values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_APPWATCH, true);
                if (0 != values.size()) {
                    //Check if the time end is different of null which means that the entry is complete
                    if (values.get(UtilsLocalDataBase.DB_APPWATCH.get(4)) != null) {
                        boolean rc;

                        ArrayList<String>  AppWatchData;

                        IdSql = values.getAsLong(UtilsLocalDataBase.C_APPWATCH_ID);

                        //Encrypt the data
                        AppWatchData = EncryptData(values, UtilsLocalDataBase.DB_APPWATCH, false);
                        /*mHandler.post(new UtilsDisplayToast(this,  getResources().getString(R.string.app_name)
                                + "Post app watched: " +  AppWatchData.get(0) + " Date start:" + AppWatchData.get(1) + " Time start:" + AppWatchData.get(2)
                                + " Date end:" + AppWatchData.get(3) + " Time end:" + AppWatchData.get(4)));*/

                        //Post the data
                        rc = PostData(SettingsBetrack.STUDY_POSTAPPWATCHED, UtilsLocalDataBase.DB_APPWATCH, AppWatchData, ObjSettingsStudy.getIdUser());
                        if (rc == true) {
                            AccesLocalDB().deleteELement(UtilsLocalDataBase.TABLE_APPWATCH, IdSql);
                            FastCheck(false);
                        } else {
                            FastCheck(true);
                        }
                    }
                    else
                    {
                        TaskDone &= ~TABLE_APPWATCH_TRANSFERED;
                    }
                }
                else
                {
                    TaskDone &= ~TABLE_APPWATCH_TRANSFERED;
                }

                //DAILY STATUS
                values.clear();
                values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_USER, true);
                if (0 != values.size()) {
                    boolean rc;
                    ArrayList<String>  DailyStatusData;

                    IdSql = values.getAsLong(UtilsLocalDataBase.C_USER_ID);

                    //Encrypt the data
                    DailyStatusData = EncryptData(values, UtilsLocalDataBase.DB_DAILYSTATUS, false);
                    mHandler.post(new UtilsDisplayToast(this, getResources().getString(R.string.app_name)+": Post survey status: " + DailyStatusData.get(0) +
                            " Post social 1: " + DailyStatusData.get(1) + " Post social 2: " + DailyStatusData.get(2) +
                            " Post mood: " + DailyStatusData.get(4)
                            + " Date: " +  DailyStatusData.get(3)));
                    //Post the data
                    rc = PostData(SettingsBetrack.STUDY_POSTDAILYSTATUS, UtilsLocalDataBase.DB_DAILYSTATUS, DailyStatusData, ObjSettingsStudy.getIdUser());
                    if (rc == true) {
                        AccesLocalDB().deleteELement(UtilsLocalDataBase.TABLE_USER, IdSql);
                    }
                }
                else
                {
                    TaskDone &= ~TABLE_DAILYSTATUS_TRANSFERED;
                }

                //START STUDY
                values.clear();
                values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_START_STUDY, true);
                if (0 != values.size()) {
                    boolean rc;
                    ArrayList<String>  StartStudyData;

                    IdSql = values.getAsLong(UtilsLocalDataBase.C_STARTSTUDY_ID);

                    //Encrypt the data
                    StartStudyData = EncryptData(values, UtilsLocalDataBase.DB_START_STUDY, false);

                    //Post the data
                    rc = PostData(SettingsBetrack.STUDY_POSTSTARTSTUDY, UtilsLocalDataBase.DB_START_STUDY, StartStudyData, ObjSettingsStudy.getIdUser());
                    if (rc == true) {
                        AccesLocalDB().deleteELement(UtilsLocalDataBase.TABLE_START_STUDY, IdSql);
                    }
                }
                else
                {
                    TaskDone &= ~TABLE_STARTSTUDY_TRANSFERED;
                }

                //END STUDY
                values.clear();
                values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_END_STUDY, true);
                if (0 != values.size()) {
                    boolean rc;
                    ArrayList<String>  EndStudyData;

                    IdSql = values.getAsLong(UtilsLocalDataBase.C_ENDSTUDY_ID);

                    //Encrypt the data
                    EndStudyData = EncryptData(values, UtilsLocalDataBase.DB_END_STUDY, false);

                    //Post the data
                    rc = PostData(SettingsBetrack.STUDY_POSTENDSTUDY, UtilsLocalDataBase.DB_END_STUDY, EndStudyData, ObjSettingsStudy.getIdUser());
                    if (rc == true) {
                        AccesLocalDB().deleteELement(UtilsLocalDataBase.TABLE_END_STUDY, IdSql);
                        CreateNotification.StopAlarm(this);
                        CreatePostData.StopAlarm(this);
                        CreateTrackApp.StopAlarm(this);
                        CreateTrackGPS.StopAlarm(this);
                    }
                }
                else
                {
                    TaskDone &= ~TABLE_ENDSTUDY_TRANSFERED;
                }

                //GPS DATA
                values.clear();
                values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_GPS, true);
                if (0 != values.size()) {
                    boolean rc;
                    ArrayList<String>  GpsData;

                    IdSql = values.getAsLong(UtilsLocalDataBase.C_GPS_ID);

                    //Encrypt the data
                    GpsData = EncryptData(values, UtilsLocalDataBase.DB_GPS, false);

                    //Post the data
                    rc = PostData(SettingsBetrack.STUDY_POSTGPSDATA, UtilsLocalDataBase.DB_GPS, GpsData, ObjSettingsStudy.getIdUser());
                    if (rc == true) {
                        AccesLocalDB().deleteELement(UtilsLocalDataBase.TABLE_GPS, IdSql);
                    }
                }
                else
                {
                    TaskDone &= ~TABLE_GPS_TRANSFERED;
                }
            }
        }
        else
        {
            //Check if an internet connection is available more often to get more chance to be able to transfer the data
            FastCheck(true);
        }

        getLock(getApplicationContext()).release();
        CreatePostData.SemUpdateServer.release();
    }

    private void FastCheck(boolean Enable) {
        CreatePostData.CreateAlarm(getApplicationContext(), Enable);
    }

    private ConnectionState hasNetworkConnection() {

        ConnectionState NetworkState = ConnectionState.NONE;

        try {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    //Log.d(TAG, "hasNetworkConnection: WIFI");
                    NetworkState = ConnectionState.WIFI;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    //Log.d(TAG, "hasNetworkConnection: LTE/3G");
                    NetworkState = ConnectionState.LTE;
                }
            }else {
                NetworkInfo[] netInfo = cm.getAllNetworkInfo();
                for (NetworkInfo ni : netInfo) {
                    if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                        if (ni.isConnected()){
                            //Log.d(TAG, "hasNetworkConnection: WIFI");
                            NetworkState = ConnectionState.WIFI;
                        }

                    if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                        if (ni.isConnected()) {
                            //Log.d(TAG, "hasNetworkConnection: LTE/3G");
                            NetworkState = ConnectionState.LTE;
                        }
                }
            }
        }
        finally {
            if (ConnectionState.NONE == NetworkState) {
                //Log.d(TAG, "hasNetworkConnection: nope");
            }
            return NetworkState;
        }
    }

    public ArrayList<String> EncryptData(ContentValues values, ArrayList<String> Field, boolean Encrypt) {

        ArrayList<String> rc = new ArrayList<String>();
        //We skip the first element which is the user personnal id (generated automatically)
        for (int i=1; i<Field.size(); i++){
            //Log.d(TAG, Field.get(i));
            if (values.get(Field.get(i)) != null) {
                rc.add(values.get(Field.get(i)).toString());
            } else {
                rc.add(null);
            }
            if (Encrypt == true) {

            }
        }
        return rc;
    }

    public boolean PostData(String WebLink, ArrayList<String> Field, ArrayList<String> Data, String IdUser) {

        boolean rc = false;
        HttpURLConnection urlConnection = null;
        java.net.URL urlPostData;
        BufferedWriter writer = null;

        try {
            //Connect to the remote database to get the available studies
            urlPostData = new URL(SettingsBetrack.STUDY_WEBSITE + WebLink +"?");
            urlConnection = (HttpURLConnection) urlPostData.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setReadTimeout(SettingsBetrack.SERVER_TIMEOUT);
            urlConnection.setConnectTimeout(SettingsBetrack.SERVER_TIMEOUT);

            Uri.Builder builder = new Uri.Builder();
            builder.appendQueryParameter(Field.get(0).toString(), ObjSettingsStudy.getIdUser());
            for (int i=1; i<Field.size(); i++){
                if (Data.get(i-1) != null) {
                    builder.appendQueryParameter(Field.get(i).toString(), Data.get(i-1).toString());
                } else {
                    builder.appendQueryParameter(Field.get(i).toString(), null);
                }
            }
            String query = builder.build().getEncodedQuery();

            writer = new BufferedWriter(
                    new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(query, 0, query.length());
            writer.flush();
            writer.close();

            if (HttpsURLConnection.HTTP_OK == urlConnection.getResponseCode()) {
                rc= true;
            }
            else {
                Log.d(TAG, "Unable to access to server... ");
                //Check if an internet connection is available more often to get more chance to be able to transfer the data
                rc = false;
            }


        } catch (java.net.SocketTimeoutException e) {
            Log.d(TAG, "Unable to access to server... ");
            rc= false;
        } catch (Exception e) {
            rc = false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            return rc;
        }
    }
}
