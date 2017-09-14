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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by cedoctet on 23/08/2016.
 */
public class IntentServicePostData extends IntentService {
    static final String TAG = "IntentServicePostData";
    private static final String LOCK_NAME_STATIC = "com.app.uni.betrack.wakelock.postdata";

    public static final Semaphore SemPostData = new Semaphore(1, true);
    private SettingsBetrack ObjSettingsBetrack = null;
    private SettingsStudy ObjSettingsStudy = null;

    private UtilsLocalDataBase localdatabase = null;
    private UtilsLocalDataBase AccesLocalDB()
    {
        return localdatabase;
    }

    private static volatile PowerManager.WakeLock lockStatic;

    private static final char TABLE_APPWATCH_TRANSFERED = 1;
    private static final char TABLE_DAILYSTATUS_TRANSFERED = 2;
    private static final char TABLE_STARTSTUDY_TRANSFERED = 4;
    private static final char TABLE_ENDSTUDY_TRANSFERED = 8;
    private static final char TABLE_GPS_TRANSFERED = 16;
    private static final char TABLE_PHONE_USAGE_TRANSFERED = 32;
    private static final char TABLE_NOTIFICATION_TIME_TRANSFERED = 64;

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
        boolean rc = false;
        Long IdSql;
        UtilsNetworkStatus.ConnectionState NetworkState;
        getLock(getApplicationContext()).acquire();

        char TaskDone = TABLE_APPWATCH_TRANSFERED | TABLE_DAILYSTATUS_TRANSFERED |
                        TABLE_STARTSTUDY_TRANSFERED | TABLE_ENDSTUDY_TRANSFERED |
                        TABLE_GPS_TRANSFERED | TABLE_PHONE_USAGE_TRANSFERED |
                        TABLE_NOTIFICATION_TIME_TRANSFERED;

        //Check if there is a data connection
        NetworkState = UtilsNetworkStatus.hasNetworkConnection((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));

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
        if ((UtilsNetworkStatus.ConnectionState.WIFI == NetworkState) ||
                ((UtilsNetworkStatus.ConnectionState.LTE == NetworkState) && (ObjSettingsBetrack.GetEnableDataUsage())))
        {

            if (SettingsStudy.EndStudyTranferState.ERROR == ObjSettingsStudy.getEndSurveyTransferred()) {
                Log.d(TAG, "EndStudyTranferState is ready to be transfer, we switch back to IN_PROGRESS");
                ObjSettingsStudy.setEndSurveyTransferred(SettingsStudy.EndStudyTranferState.IN_PROGRESS);
            }

            while(true) {
                //BLOB KEY
                values.clear();
                values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_SESSION_KEY, false);
                if (0 != values.size()) {
                    ArrayList<String> StatusData;

                    IdSql = values.getAsLong(UtilsLocalDataBase.C_SESSION_KEY_ID);

                    //Everything is encrypted with the public key for the session key that's why no need to use the AES encption
                    //Prepare the data
                    StatusData = PrepareData(values, UtilsLocalDataBase.DB_SESSION_KEY, UtilsLocalDataBase.DB_SESSION_KEY_CYPHER,  false);
                    //Post the data
                    rc = PostData(SettingsBetrack.STUDY_POSTBLOBKEY, UtilsLocalDataBase.DB_SESSION_KEY, StatusData, UtilsLocalDataBase.DB_SESSION_KEY_CYPHER, false);
                    if (rc == true) {
                        AccesLocalDB().deleteELement(UtilsLocalDataBase.TABLE_SESSION_KEY, IdSql);
                    } else {
                        break;
                    }
                } else {
                    rc = true;
                    break;
                }
            }

            if (rc == true) {

                rc =false;

                while(TaskDone != 0)
                {
                    //APPLICATIONS WATCHED
                    values.clear();
                    values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_APPWATCH, true);
                    if (0 != values.size()) {
                        //Check if the time end is different of null which means that the entry is complete
                        if (values.get(UtilsLocalDataBase.DB_APPWATCH.get(5)) != null) {
                            ArrayList<String>  AppWatchData;

                            IdSql = values.getAsLong(UtilsLocalDataBase.C_APPWATCH_ID);

                            //Prepare the data
                            AppWatchData = PrepareData(values, UtilsLocalDataBase.DB_APPWATCH, UtilsLocalDataBase.DB_APPWATCH_CYPHER, true);

                            //Post the data
                            rc = PostData(SettingsBetrack.STUDY_POSTAPPWATCHED, UtilsLocalDataBase.DB_APPWATCH, AppWatchData, UtilsLocalDataBase.DB_APPWATCH_CYPHER, true);
                            if (rc == true) {
                                AccesLocalDB().deleteELement(UtilsLocalDataBase.TABLE_APPWATCH, IdSql);
                            } else {
                                break;
                            }
                        } else {
                            rc = true;
                            TaskDone &= ~TABLE_APPWATCH_TRANSFERED;
                        }
                    } else {
                        rc = true;
                        TaskDone &= ~TABLE_APPWATCH_TRANSFERED;
                    }

                    //DAILY STATUS
                    values.clear();
                    values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_DAILYSTATUS, true);
                    if (0 != values.size()) {
                        ArrayList<String>  DailyStatusData;

                        IdSql = values.getAsLong(UtilsLocalDataBase.C_DAILYSTATUS_ID);

                        //Prepare the data
                        DailyStatusData = PrepareData(values, UtilsLocalDataBase.DB_DAILYSTATUS, UtilsLocalDataBase.DB_DAILYSTATUS_CYPHER, true);

                        //Post the data
                        rc = PostData(SettingsBetrack.STUDY_POSTDAILYSTATUS, UtilsLocalDataBase.DB_DAILYSTATUS, DailyStatusData, UtilsLocalDataBase.DB_DAILYSTATUS_CYPHER, true);
                        if (rc == true) {
                            AccesLocalDB().deleteELement(UtilsLocalDataBase.TABLE_DAILYSTATUS, IdSql);
                        } else {
                            break;
                        }
                    } else {
                        rc = true;
                        TaskDone &= ~TABLE_DAILYSTATUS_TRANSFERED;
                    }

                    //START STUDY
                    values.clear();
                    values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_START_STUDY, false);
                    if (0 != values.size()) {
                        ArrayList<String>  StartStudyData;

                        IdSql = values.getAsLong(UtilsLocalDataBase.C_STARTSTUDY_ID);

                        //Prepare the data
                        StartStudyData = PrepareData(values, UtilsLocalDataBase.DB_START_STUDY, UtilsLocalDataBase.DB_START_STUDY_CYPHER, true);

                        //Post the data
                        rc = PostData(SettingsBetrack.STUDY_POSTSTARTSTUDY, UtilsLocalDataBase.DB_START_STUDY, StartStudyData, UtilsLocalDataBase.DB_START_STUDY_CYPHER, true);
                        if (rc == true) {
                            AccesLocalDB().deleteELement(UtilsLocalDataBase.TABLE_START_STUDY, IdSql);
                        } else {
                            break;
                        }
                    } else {
                        rc = true;
                        TaskDone &= ~TABLE_STARTSTUDY_TRANSFERED;
                    }

                    //END STUDY
                    values.clear();
                    values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_END_STUDY, true);
                    if (0 != values.size()) {
                        ArrayList<String>  EndStudyData;

                        IdSql = values.getAsLong(UtilsLocalDataBase.C_ENDSTUDY_ID);

                        //Prepare the data
                        EndStudyData = PrepareData(values, UtilsLocalDataBase.DB_END_STUDY, UtilsLocalDataBase.DB_END_STUDY_CYPHER, true);

                        //Post the data
                        rc = PostData(SettingsBetrack.STUDY_POSTENDSTUDY, UtilsLocalDataBase.DB_END_STUDY, EndStudyData, UtilsLocalDataBase.DB_END_STUDY_CYPHER, true);
                        if (rc == true) {
                            AccesLocalDB().deleteELement(UtilsLocalDataBase.TABLE_END_STUDY, IdSql);
                            CreateNotification.StopAlarm(this);
                            CreateTrackApp.StopAlarm(this);
                            Log.d(TAG, "setEndSurveyTransferred = DONE");
                            ObjSettingsStudy.setEndSurveyTransferred(SettingsStudy.EndStudyTranferState.DONE);
                            Log.d(TAG, "Display the end chart");
                            Intent intentBetrack = new Intent(this, ActivityBeTrack.class);
                            intentBetrack.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentBetrack);
                        } else {
                            Log.d(TAG, "setEndSurveyTransferred = ERROR");
                            ObjSettingsStudy.setEndSurveyTransferred(SettingsStudy.EndStudyTranferState.ERROR);
                            break;
                        }
                    }
                    else {
                        rc = true;
                        TaskDone &= ~TABLE_ENDSTUDY_TRANSFERED;
                    }

                    //GPS DATA
                    values.clear();
                    values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_GPS, true);
                    if (0 != values.size()) {
                        ArrayList<String>  GpsData;

                        IdSql = values.getAsLong(UtilsLocalDataBase.C_GPS_ID);

                        //Prepare the data
                        GpsData = PrepareData(values, UtilsLocalDataBase.DB_GPS, UtilsLocalDataBase.DB_GPS_CYPHER, true);

                        //Post the data
                        rc = PostData(SettingsBetrack.STUDY_POSTGPSDATA, UtilsLocalDataBase.DB_GPS, GpsData, UtilsLocalDataBase.DB_GPS_CYPHER, true);
                        if (rc == true) {
                            AccesLocalDB().deleteELement(UtilsLocalDataBase.TABLE_GPS, IdSql);
                        } else {
                            break;
                        }
                    } else {
                        rc = true;
                        TaskDone &= ~TABLE_GPS_TRANSFERED;
                    }

                    //PHONE USAGE DATA
                    values.clear();
                    values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_PHONE_USAGE, true);
                    if (0 != values.size()) {
                        ArrayList<String>  PhoneData;

                        IdSql = values.getAsLong(UtilsLocalDataBase.C_PHONE_USAGE_ID);

                        //Prepare the data
                        PhoneData = PrepareData(values, UtilsLocalDataBase.DB_PHONE_USAGE, UtilsLocalDataBase.DB_PHONE_USAGE_CYPHER, true);

                        //Post the data
                        rc = PostData(SettingsBetrack.STUDY_POSTPHONEUSAGEDATA, UtilsLocalDataBase.DB_PHONE_USAGE, PhoneData, UtilsLocalDataBase.DB_PHONE_USAGE_CYPHER, true);
                        if (rc == true) {
                            AccesLocalDB().deleteELement(UtilsLocalDataBase.TABLE_PHONE_USAGE, IdSql);
                        } else {
                            break;
                        }
                    } else {
                        rc = true;
                        TaskDone &= ~TABLE_PHONE_USAGE_TRANSFERED;
                    }

                    //NOTIFICATION TIME DATA
                    values.clear();
                    values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_NOTIFICATION_TIME, true);
                    if (0 != values.size()) {
                        ArrayList<String>  NotifData;

                        IdSql = values.getAsLong(UtilsLocalDataBase.C_NOTIFICATION_TIME_ID);

                        //Prepare the data
                        NotifData = PrepareData(values, UtilsLocalDataBase.DB_NOTIFICATION_TIME, UtilsLocalDataBase.DB_NOTIFICATION_TIME_CYPHER, true);

                        //Post the data
                        rc = PostData(SettingsBetrack.STUDY_POSTNOTIFICATIONTIME, UtilsLocalDataBase.DB_NOTIFICATION_TIME, NotifData, UtilsLocalDataBase.DB_NOTIFICATION_TIME_CYPHER, true);
                        if (rc == true) {
                            AccesLocalDB().deleteELement(UtilsLocalDataBase.TABLE_NOTIFICATION_TIME, IdSql);
                        } else {
                            break;
                        }
                    } else {
                        rc = true;
                        TaskDone &= ~TABLE_NOTIFICATION_TIME_TRANSFERED;
                    }


                }
            }
        }

        if (rc == true) {
            ObjSettingsStudy.setTimeLastTransfer(System.currentTimeMillis());
        } else {
            //Check if we were trying to transfer the end study
            values.clear();
            values = AccesLocalDB().getElementDb(UtilsLocalDataBase.TABLE_END_STUDY, true);
            if (0 != values.size()) {
                //We are trying to transfer the end survey but we don't have connectivity or we are not allowed to use it so we return an error
                Log.d(TAG, "setEndSurveyTransferred = ERROR");
                ObjSettingsStudy.setEndSurveyTransferred(SettingsStudy.EndStudyTranferState.ERROR);
                Intent iInternetConnectivity = new Intent(this, ActivityInternetConnectivity.class);
                iInternetConnectivity.putExtra(ActivityInternetConnectivity.STATUS_START_ACTIVITY, ActivityInternetConnectivity.END_STUDY_IN_ERROR);
                iInternetConnectivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(iInternetConnectivity);
            }
        }

        SemPostData.release();
        getLock(getApplicationContext()).release();
    }

    public ArrayList<String> PrepareData(ContentValues values, ArrayList<String> Field, ArrayList<Boolean> Cypher, boolean Encrypt) {
        byte[] encodedBytes = null;
        String valueToEncrypt = null;
        String Result = null;

        ArrayList<String> rc = new ArrayList<String>();
        //We skip the first element which is the user personnal id (generated automatically)
        for (int i=1; i<Field.size(); i++){
            //Log.d(TAG, Field.get(i));
            if (values.get(Field.get(i)) != null) {
                try {
                    if ((true == Encrypt) && (true == Cypher.get(i))) {
                        if (i!=1) {
                            valueToEncrypt += String.valueOf(Character.toChars(30)) + values.get(Field.get(i)).toString();
                        } else {
                            valueToEncrypt = values.get(Field.get(i)).toString();
                        }
                        rc.add(null);
                    } else {
                        rc.add(values.get(Field.get(i)).toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
            } else {
                if ((true == Encrypt) && (true == Cypher.get(i))) {
                    valueToEncrypt += String.valueOf(Character.toChars(30)) + null;
                }
                rc.add(null);
            }
        }
        if (true == Encrypt) {
            try {
                UtilsCryptoAES.CipherTextIvMac cipherTextIvMac = UtilsCryptoAES.encrypt(valueToEncrypt, ObjSettingsStudy.SessionKey);
                Result = cipherTextIvMac.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
            } finally {
                rc.add(Result);
            }
        }


        return rc;
    }

    public boolean PostData(String WebLink, ArrayList<String> Field, ArrayList<String> Data, ArrayList<Boolean> Cypher, boolean Encrypt) {

        boolean rc = false;
        HttpURLConnection urlConnection = null;
        java.net.URL urlPostData;
        BufferedWriter writer = null;

        try {
            //Connect to the remote database to get the available studies
            if (Encrypt == false) {
                urlPostData = new URL(SettingsBetrack.STUDY_WEBSITE + WebLink +".php?");
            }
            else {
                urlPostData = new URL(SettingsBetrack.STUDY_WEBSITE + WebLink +"_SSL.php?");
            }
            urlConnection = (HttpURLConnection) urlPostData.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setReadTimeout(SettingsBetrack.SERVER_TIMEOUT);
            urlConnection.setConnectTimeout(SettingsBetrack.SERVER_TIMEOUT);

            Uri.Builder builder = new Uri.Builder();

            if (Encrypt == false) {
                //Add the user ID to the PHP request
                builder.appendQueryParameter(Field.get(0).toString(), ObjSettingsStudy.getIdUserCypher());
                //Add the different fiels to the PHP request
                for (int i=1; i<Field.size(); i++){
                    if (Data.get(i-1) != null) {
                        builder.appendQueryParameter(Field.get(i).toString(), Data.get(i-1).toString());
                    } else {
                        builder.appendQueryParameter(Field.get(i).toString(), null);
                    }
                }
            } else {
                //Add the user ID to the PHP request
                builder.appendQueryParameter(Field.get(0).toString(), ObjSettingsStudy.getIdUserCypher());
                //Add the data encrypted to the PHP request
                builder.appendQueryParameter("encrypted", Data.get(Field.size()-1).toString());
                //Add the field that were not encrypted to the PHP request
                for (int i=1; i<Field.size(); i++) {
                    if ((Data.get(i-1) != null) && (false == Cypher.get(i))) {
                        builder.appendQueryParameter(Field.get(i).toString(), Data.get(i-1).toString());
                    }
                }
            }

            String query = builder.build().getEncodedQuery();

            writer = new BufferedWriter(
                    new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(query, 0, query.length());
            writer.flush();
            writer.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            urlConnection.getInputStream()));
            String decodedString;
            String decodedStringLast = null;
            while ((decodedString = in.readLine()) != null) {
                decodedStringLast = decodedString;
                Log.d(TAG, decodedString);
            }
            in.close();

            if (HttpsURLConnection.HTTP_OK == urlConnection.getResponseCode()) {
                if (!decodedStringLast.equals("OK")) {
                    Log.d(TAG, "Error during accessing the server... ");
                    rc = false;
                } else {
                    rc= true;
                }
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
