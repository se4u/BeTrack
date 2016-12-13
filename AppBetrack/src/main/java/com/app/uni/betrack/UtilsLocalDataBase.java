package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Created by cevincent on 6/3/16.
 */
public class UtilsLocalDataBase {
    public static final String KEY_ID = "_id";

    static final String TAG = "SQLLocalTable";
    static final String DB_NAME = "StudyDataBase.db";
    static final int DB_VERSION = 1; //

    //Table for application(s) watching
    static final String TABLE_APPWATCH = "AppWatch";
    static final String C_APPWATCH_ID = BaseColumns._ID;
    static final String C_APPWATCH_PID = "ParticipantID";
    static final String C_APPWATCH_APPLICATION = "Application";
    static final String C_APPWATCH_DATESTART = "DateStart";
    static final String C_APPWATCH_DATESTOP = "DateStop";
    static final String C_APPWATCH_TIMESTART = "TimeStart";
    static final String C_APPWATCH_TIMESTOP = "TimeStop";

    public static final ArrayList<String> DB_APPWATCH = new ArrayList<String>() {{
        add(UtilsLocalDataBase.C_APPWATCH_PID);
        add(UtilsLocalDataBase.C_APPWATCH_APPLICATION);
        add(UtilsLocalDataBase.C_APPWATCH_DATESTART);
        add(UtilsLocalDataBase.C_APPWATCH_DATESTOP);
        add(UtilsLocalDataBase.C_APPWATCH_TIMESTART);
        add(UtilsLocalDataBase.C_APPWATCH_TIMESTOP);
    }};

    public static final ArrayList<Boolean> DB_APPWATCH_CYPHER = new ArrayList<Boolean>() {{
        add(false); //C_APPWATCH_PID
        add(true);  //C_APPWATCH_APPLICATION
        add(true); //C_APPWATCH_DATESTART
        add(true); //C_APPWATCH_DATESTOP
        add(true); //C_APPWATCH_TIMESTART
        add(true); //C_APPWATCH_TIMESTOP
    }};

    //Table for period, libido... follow up
    static final String TABLE_DAILYSTATUS = "DailyStatus";
    static final String C_DAILYSTATUS_ID = BaseColumns._ID;
    static final String C_DAILYSTATUS_PID = "ParticipantID";
    static final String C_DAILYSTATUS_PERIOD = "Period";
    static final String C_DAILYSTATUS_SOCIAL1_LIFE = "SocialLife1";
    static final String C_DAILYSTATUS_SOCIAL2_LIFE = "SocialLife2";
    static final String C_DAILYSTATUS_MOOD1 = "Mood1";
    static final String C_DAILYSTATUS_MOOD2 = "Mood2";
    static final String C_DAILYSTATUS_MOOD3 = "Mood3";
    static final String C_DAILYSTATUS_MOOD4 = "Mood4";
    static final String C_DAILYSTATUS_DATE = "Date";
    static final String C_DAILYSTATUS_TIME = "Time";


    public static final ArrayList<String> DB_DAILYSTATUS = new ArrayList<String>() {{
        add(UtilsLocalDataBase.C_DAILYSTATUS_PID);
        add(UtilsLocalDataBase.C_DAILYSTATUS_PERIOD);
        add(UtilsLocalDataBase.C_DAILYSTATUS_SOCIAL1_LIFE);
        add(UtilsLocalDataBase.C_DAILYSTATUS_SOCIAL2_LIFE);
        add(UtilsLocalDataBase.C_DAILYSTATUS_MOOD1);
        add(UtilsLocalDataBase.C_DAILYSTATUS_MOOD2);
        add(UtilsLocalDataBase.C_DAILYSTATUS_MOOD3);
        add(UtilsLocalDataBase.C_DAILYSTATUS_MOOD4);
        add(UtilsLocalDataBase.C_DAILYSTATUS_DATE);
        add(UtilsLocalDataBase.C_DAILYSTATUS_TIME);
    }};

    public static final ArrayList<Boolean> DB_DAILYSTATUS_CYPHER = new ArrayList<Boolean>() {{
        add(false); //C_DAILYSTATUS_PID
        add(true);  //C_DAILYSTATUS_PERIOD
        add(true);  //C_DAILYSTATUS_SOCIAL1_LIFE
        add(true);  //C_DAILYSTATUS_SOCIAL2_LIFE
        add(true);  //C_DAILYSTATUS_MOOD1
        add(true);  //C_DAILYSTATUS_MOOD2
        add(true);  //C_DAILYSTATUS_MOOD3
        add(true);  //C_DAILYSTATUS_MOOD4
        add(true); //C_DAILYSTATUS_DATE
        add(true); //C_DAILYSTATUS_TIME
    }};

    //Table for GPS data
    static final String TABLE_GPS = "GpsFollowUp";
    static final String C_GPS_ID = BaseColumns._ID;
    static final String C_GPS_PID = "ParticipantID";
    static final String C_GPS_LATTITUDE = "Lattitude";
    static final String C_GPS_LONGITUDE = "Longitude";
    static final String C_GPS_DATE = "Date";
    static final String C_GPS_TIME = "Time";

    public static final ArrayList<String> DB_GPS = new ArrayList<String>() {{
        add(UtilsLocalDataBase.C_GPS_PID);
        add(UtilsLocalDataBase.C_GPS_LATTITUDE);
        add(UtilsLocalDataBase.C_GPS_LONGITUDE);
        add(UtilsLocalDataBase.C_GPS_DATE);
        add(UtilsLocalDataBase.C_GPS_TIME);
    }};

    public static final ArrayList<Boolean> DB_GPS_CYPHER = new ArrayList<Boolean>() {{
        add(false); //C_GPS_PID
        add(true);  //C_GPS_LATTITUDE
        add(true);  //C_GPS_LONGITUDE
        add(true); //C_GPS_DATE
        add(true); //C_GPS_TIME
    }};

    //Table for Status study
    static final String TABLE_SESSION_KEY = "SessionKey";
    static final String C_SESSION_KEY_ID = BaseColumns._ID;
    static final String C_SESSION_KEY_PID = "ParticipantID";
    static final String C_SESSION_KEY_BLOB = "Sessionkey";
    static final String C_SESSION_KEY_DATE = "Date";
    static final String C_SESSION_KEY_TIME = "Time";

    public static final ArrayList<String> DB_SESSION_KEY = new ArrayList<String>() {{
        add(UtilsLocalDataBase.C_SESSION_KEY_PID);
        add(UtilsLocalDataBase.C_SESSION_KEY_BLOB);
        add(UtilsLocalDataBase.C_SESSION_KEY_DATE);
        add(UtilsLocalDataBase.C_SESSION_KEY_TIME);
    }};

    public static final ArrayList<Boolean> DB_SESSION_KEY_CYPHER = new ArrayList<Boolean>() {{
        add(false); //C_SESSION_KEY_PID
        add(false); //C_SESSION_KEY_BLOB
        add(false); //C_SESSION_KEY_DATE
        add(false); //C_SESSION_KEY_TIME
    }};

    //Table for Beginning study
    static final String TABLE_START_STUDY = "StartStudy";
    static final String C_STARTSTUDY_ID = BaseColumns._ID;
    static final String C_STARTSTUDY_PID = "ParticipantID";
    static final String C_STARTSTUDY_AGE = "Age";
    static final String C_STARTSTUDY_SEX = "Sex";
    static final String C_STARTSTUDY_RELATIONSHIP = "RelationShip";
    static final String C_STARTSTUDY_CONTRACEPTION = "Contraception";
    static final String C_STARTSTUDY_AVGPERIODLENGHT = "AvgPeriodLenght";
    static final String C_STARTSTUDY_AVGMENSTRUALCYCLE = "AvgMenstrualCycle";
    static final String C_STARTSTUDY_SOCIAL1_LIFE = "SocialLife1";
    static final String C_STARTSTUDY_SOCIAL2_LIFE = "SocialLife2";
    static final String C_STARTSTUDY_MOOD1 = "Mood1";
    static final String C_STARTSTUDY_MOOD2 = "Mood2";
    static final String C_STARTSTUDY_MOOD3 = "Mood3";
    static final String C_STARTSTUDY_MOOD4 = "Mood4";
    static final String C_STARTSTUDY_DATE = "Date";
    static final String C_STARTSTUDY_TIME = "Time";

    public static final ArrayList<String> DB_START_STUDY = new ArrayList<String>() {{
        add(UtilsLocalDataBase.C_STARTSTUDY_PID);
        add(UtilsLocalDataBase.C_STARTSTUDY_AGE);
        add(UtilsLocalDataBase.C_STARTSTUDY_RELATIONSHIP);
        add(UtilsLocalDataBase.C_STARTSTUDY_CONTRACEPTION);
        add(UtilsLocalDataBase.C_STARTSTUDY_AVGPERIODLENGHT);
        add(UtilsLocalDataBase.C_STARTSTUDY_AVGMENSTRUALCYCLE);
        add(UtilsLocalDataBase.C_STARTSTUDY_SOCIAL1_LIFE);
        add(UtilsLocalDataBase.C_STARTSTUDY_SOCIAL2_LIFE);
        add(UtilsLocalDataBase.C_STARTSTUDY_MOOD1);
        add(UtilsLocalDataBase.C_STARTSTUDY_MOOD2);
        add(UtilsLocalDataBase.C_STARTSTUDY_MOOD3);
        add(UtilsLocalDataBase.C_STARTSTUDY_MOOD4);
        add(UtilsLocalDataBase.C_STARTSTUDY_DATE);
        add(UtilsLocalDataBase.C_STARTSTUDY_TIME);
    }};

    public static final ArrayList<Boolean> DB_START_STUDY_CYPHER = new ArrayList<Boolean>() {{
        add(false); //C_STARTSTUDY_PID
        add(true);  //C_STARTSTUDY_AGE
        add(true);  //C_STARTSTUDY_RELATIONSHIP
        add(true);  //C_STARTSTUDY_CONTRACEPTION
        add(true);  //C_STARTSTUDY_AVGPERIODLENGHT
        add(true);  //C_STARTSTUDY_AVGMENSTRUALCYCLE
        add(true);  //C_STARTSTUDY_SOCIAL1_LIFE
        add(true);  //C_STARTSTUDY_SOCIAL2_LIFE
        add(true);  //C_STARTSTUDY_MOOD1
        add(true);  //C_STARTSTUDY_MOOD2
        add(true);  //C_STARTSTUDY_MOOD3
        add(true);  //C_STARTSTUDY_MOOD4
        add(true);  //C_STARTSTUDY_DATE
        add(true);  //C_STARTSTUDY_TIME
    }};

    //Table for End study
    static final String TABLE_END_STUDY = "EndStudy";
    static final String C_ENDSTUDY_ID = BaseColumns._ID;
    static final String C_ENDSTUDY_PID = "ParticipantID";
    static final String C_ENDSTUDY_PERIOD = "Period";
    static final String C_ENDSTUDY_SOCIAL1_LIFE = "SocialLife1";
    static final String C_ENDSTUDY_SOCIAL2_LIFE = "SocialLife2";
    static final String C_ENDSTUDY_MOOD1 = "Mood1";
    static final String C_ENDSTUDY_MOOD2 = "Mood2";
    static final String C_ENDSTUDY_MOOD3 = "Mood3";
    static final String C_ENDSTUDY_MOOD4 = "Mood4";
    static final String C_ENDSTUDY_RELATIONSHIP = "RelationShip";
    static final String C_ENDSTUDY_CONTRACEPTION = "Contraception";
    static final String C_ENDSTUDY_DATE = "Date";
    static final String C_ENDSTUDY_TIME = "Time";

    public static final ArrayList<String> DB_END_STUDY = new ArrayList<String>() {{
        add(UtilsLocalDataBase.C_ENDSTUDY_PID);
        add(UtilsLocalDataBase.C_ENDSTUDY_PERIOD);
        add(UtilsLocalDataBase.C_ENDSTUDY_SOCIAL1_LIFE);
        add(UtilsLocalDataBase.C_ENDSTUDY_SOCIAL2_LIFE);
        add(UtilsLocalDataBase.C_ENDSTUDY_MOOD1);
        add(UtilsLocalDataBase.C_ENDSTUDY_MOOD2);
        add(UtilsLocalDataBase.C_ENDSTUDY_MOOD3);
        add(UtilsLocalDataBase.C_ENDSTUDY_MOOD4);
        add(UtilsLocalDataBase.C_ENDSTUDY_RELATIONSHIP);
        add(UtilsLocalDataBase.C_ENDSTUDY_CONTRACEPTION);
        add(UtilsLocalDataBase.C_ENDSTUDY_DATE);
        add(UtilsLocalDataBase.C_ENDSTUDY_TIME);
    }};

    public static final ArrayList<Boolean> DB_END_STUDY_CYPHER = new ArrayList<Boolean>() {{
        add(false); //C_ENDSTUDY_PID
        add(true);  //C_ENDSTUDY_PERIOD
        add(true);  //C_ENDSTUDY_SOCIAL1_LIFE
        add(true);  //C_ENDSTUDY_SOCIAL2_LIFE
        add(true);  //C_ENDSTUDY_MOOD1
        add(true);  //C_ENDSTUDY_MOOD2
        add(true);  //C_ENDSTUDY_MOOD3
        add(true);  //C_ENDSTUDY_MOOD4
        add(true);  //C_ENDSTUDY_RELATIONSHIP
        add(true);  //C_ENDSTUDY_CONTRACEPTION
        add(true); //C_ENDSTUDY_DATE
        add(true); //C_ENDSTUDY_TIME
    }};

    //Table for phone usage follow up every 24 hours
    static final String TABLE_PHONE_USAGE = "PhoneUsage";
    static final String C_PHONE_USAGE_ID = BaseColumns._ID;
    static final String C_PHONE_USAGE_PID = "ParticipantID";
    static final String C_PHONE_USAGE_STATE = "ScreenState";
    static final String C_PHONE_USAGE_DATE = "Date";
    static final String C_PHONE_USAGE_TIME = "Time";


    public static final ArrayList<String> DB_PHONE_USAGE = new ArrayList<String>() {{
        add(UtilsLocalDataBase.C_PHONE_USAGE_PID);
        add(UtilsLocalDataBase.C_PHONE_USAGE_STATE);
        add(UtilsLocalDataBase.C_PHONE_USAGE_DATE);
        add(UtilsLocalDataBase.C_PHONE_USAGE_TIME);
    }};

    public static final ArrayList<Boolean> DB_PHONE_USAGE_CYPHER = new ArrayList<Boolean>() {{
        add(false); //C_PHONE_USAGE_PID
        add(true);  //C_PHONE_USAGE_STATE
        add(true);  //C_PHONE_USAGE_DATE
        add(true);  //C_PHONE_USAGE_TIME
    }};

    private static final Semaphore SemUpdateDb = new Semaphore(1, true);


    class DbHelper extends SQLiteOpenHelper {
        Context context;

        // Constructor
        public DbHelper(Context context) { //
            super(context, DB_NAME, null, DB_VERSION);
            this.context = context;
        }

        // Called only once, first time the DB is created
        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "create table " + TABLE_APPWATCH + " (" + C_APPWATCH_ID + " integer primary key autoincrement, "
                    + C_APPWATCH_APPLICATION + " text, "
                    + C_APPWATCH_DATESTART + " text, "
                    + C_APPWATCH_DATESTOP + " text, "
                    + C_APPWATCH_TIMESTART + " text, "
                    + C_APPWATCH_TIMESTOP + " text)"; //
            db.execSQL(sql);
            Log.d(TAG, "onCreated sql: " + sql);

            String sql2 = "create table " + TABLE_DAILYSTATUS + " (" + C_DAILYSTATUS_ID + " integer primary key autoincrement, "
                    + C_DAILYSTATUS_PERIOD + " text, "
                    + C_DAILYSTATUS_SOCIAL1_LIFE + " text, "
                    + C_DAILYSTATUS_SOCIAL2_LIFE + " text, "
                    + C_DAILYSTATUS_MOOD1 + " text, "
                    + C_DAILYSTATUS_MOOD2 + " text, "
                    + C_DAILYSTATUS_MOOD3 + " text, "
                    + C_DAILYSTATUS_MOOD4 + " text, "
                    + C_DAILYSTATUS_DATE + " text, "
                    + C_DAILYSTATUS_TIME + " text)"; //
            db.execSQL(sql2);
            Log.d(TAG, "onCreated sql: " + sql2);

            String sql3 = "create table " + TABLE_START_STUDY + " (" + C_STARTSTUDY_ID + " integer primary key autoincrement, "
                    +  C_STARTSTUDY_AGE + " text, "
                    + C_STARTSTUDY_RELATIONSHIP + " text, "
                    + C_STARTSTUDY_CONTRACEPTION + " text, "
                    + C_STARTSTUDY_AVGPERIODLENGHT + " text, "
                    + C_STARTSTUDY_AVGMENSTRUALCYCLE + " text, "
                    + C_STARTSTUDY_SOCIAL1_LIFE + " text, "
                    + C_STARTSTUDY_SOCIAL2_LIFE + " text, "
                    + C_STARTSTUDY_MOOD1 + " text, "
                    + C_STARTSTUDY_MOOD2 + " text, "
                    + C_STARTSTUDY_MOOD3 + " text, "
                    + C_STARTSTUDY_MOOD4 + " text, "
                    + C_STARTSTUDY_DATE + " text, "
                    + C_STARTSTUDY_TIME + " text)"; //
            db.execSQL(sql3);
            Log.d(TAG, "onCreated sql: " + sql3);

            String sql4 = "create table " + TABLE_GPS + " (" + C_GPS_ID + " integer primary key autoincrement, "
                    +  C_GPS_LATTITUDE + " text, "
                    + C_GPS_LONGITUDE + " text, "
                    + C_GPS_DATE + " text, "
                    + C_GPS_TIME + " text)"; //
            db.execSQL(sql4);
            Log.d(TAG, "onCreated sql: " + sql4);

            String sql6 = "create table " + TABLE_END_STUDY + " (" + C_ENDSTUDY_ID + " integer primary key autoincrement, "
                    + C_ENDSTUDY_PERIOD + " text, "
                    + C_ENDSTUDY_SOCIAL1_LIFE + " text, "
                    + C_ENDSTUDY_SOCIAL2_LIFE + " text, "
                    + C_ENDSTUDY_MOOD1 + " text, "
                    + C_ENDSTUDY_MOOD2 + " text, "
                    + C_ENDSTUDY_MOOD3 + " text, "
                    + C_ENDSTUDY_MOOD4 + " text, "
                    + C_ENDSTUDY_RELATIONSHIP + " text, "
                    + C_ENDSTUDY_CONTRACEPTION + " text, "
                    + C_ENDSTUDY_DATE +  " text, "
                    + C_ENDSTUDY_TIME + " text)"; //
            db.execSQL(sql6);
            Log.d(TAG, "onCreated sql: " + sql6);

            String sql7 = "create table " + TABLE_SESSION_KEY + " (" + C_SESSION_KEY_ID + " integer primary key autoincrement, "
                    + C_SESSION_KEY_BLOB + " text, "
                    + C_SESSION_KEY_DATE + " text, "
                    + C_SESSION_KEY_TIME + " text)"; //
            db.execSQL(sql7);
            Log.d(TAG, "onCreated sql: " + sql7);

            String sql8 = "create table " + TABLE_PHONE_USAGE + " (" + C_PHONE_USAGE_ID + " integer primary key autoincrement, "
                    + C_PHONE_USAGE_STATE + " text, "
                    + C_PHONE_USAGE_DATE +  " text, "
                    + C_PHONE_USAGE_TIME + " text)"; //
            db.execSQL(sql8);
            Log.d(TAG, "onCreated sql: " + sql8);
        }

        // Called whenever newVersion != oldVersion
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //

            db.execSQL("drop table if exists " + TABLE_APPWATCH); // drops the old database

            db.execSQL("drop table if exists " + TABLE_DAILYSTATUS); // drops the old database

            db.execSQL("drop table if exists " + TABLE_START_STUDY); // drops the old database

            db.execSQL("drop table if exists " + TABLE_GPS); // drops the old database

            db.execSQL("drop table if exists " + TABLE_END_STUDY); // drops the old database

            db.execSQL("drop table if exists " + TABLE_SESSION_KEY); // drops the old database

            db.execSQL("drop table if exists " + TABLE_PHONE_USAGE); // drops the old database

            Log.d(TAG, "onUpdated");
            onCreate(db); // run onCreate to get new database
        }
    }

    private final DbHelper dbHelper; //

    public UtilsLocalDataBase(Context context) { //
        this.dbHelper = new DbHelper(context);
        //Log.i(TAG, "Initialized data");
    }

    public void close() { //
        this.dbHelper.close();
    }

    public void insertOrIgnore(ContentValues values, String Table) { //
        Log.d(TAG, "insertOrIgnore on " + values);
        SQLiteDatabase db = null;

        try {
            SemUpdateDb.acquire();
            db = this.dbHelper.getWritableDatabase();
            db.insertWithOnConflict(Table, null, values,
                    SQLiteDatabase.CONFLICT_IGNORE); //
        } catch (InterruptedException e) {
            Log.d(TAG, "Database action error!");
            SemUpdateDb.release();
        } finally {
            db.close(); //
            SemUpdateDb.release();
        }

    }

    public void Update(ContentValues values, long id, String Table) { //
        Log.d(TAG, "insertOrIgnore on " + values);
        SQLiteDatabase db = null;

        try {
            SemUpdateDb.acquire();
            db = this.dbHelper.getWritableDatabase();
            db.update(Table, values, KEY_ID+"="+id, null);
        } catch (InterruptedException e) {
            Log.d(TAG, "Database action error!");
            SemUpdateDb.release();
        } finally {
            db.close(); //
            SemUpdateDb.release();
        }

    }

    public void deleteELement(String Table, long id) { //
        Log.d(TAG, "deletedELement on " + id);
        SQLiteDatabase db =null;
        try {
            SemUpdateDb.acquire();
            db = this.dbHelper.getWritableDatabase();
            db.delete(Table, C_APPWATCH_ID + "=" + id, null);
        } catch (InterruptedException e) {
            Log.d(TAG, "Database action error!");
            SemUpdateDb.release();
        } finally {
            db.close(); //
            SemUpdateDb.release();
        }
    }

    public ContentValues getElementDb(String Table, boolean OldestElement) { //

        Cursor cursor = null;
        SQLiteDatabase db = null;
        String[] DB_TABLE = null;
        try {

            SemUpdateDb.acquire();
            db = this.dbHelper.getReadableDatabase();
            cursor = db.query(Table, null, null, null,
                    null, null, null);



            try {
                ContentValues values = new ContentValues();
                values.clear();

                if (cursor.getCount()>0) {
                    if (OldestElement == true) {
                        cursor.moveToFirst();
                    } else {
                        cursor.moveToLast();
                    }

                    String[] columns = cursor.getColumnNames();
                    int length = columns.length;
                    for (int i = 0; i < length; i++) {
                        switch (cursor.getType(i)) {
                            case Cursor.FIELD_TYPE_NULL:
                                values.putNull(columns[i]);
                                break;
                            case Cursor.FIELD_TYPE_INTEGER:
                                values.put(columns[i], cursor.getLong(i));
                                break;
                            case Cursor.FIELD_TYPE_FLOAT:
                                values.put(columns[i], cursor.getDouble(i));
                                break;
                            case Cursor.FIELD_TYPE_STRING:
                                values.put(columns[i], cursor.getString(i));
                                break;
                            case Cursor.FIELD_TYPE_BLOB:
                                values.put(columns[i], cursor.getBlob(i));
                                break;
                        }
                    }
                }

                return values;
            } finally {

            }
        } catch (InterruptedException e) {
            Log.d(TAG, "Database action error!");
            SemUpdateDb.release();
            return null;
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
            SemUpdateDb.release();
        }
    }
}
