package com.app.uni.betrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

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
    static final String C_APPWATCH_APPLICATION = "Application";
    static final String C_APPWATCH_DATESTART = "DateStart";
    static final String C_APPWATCH_DATESTOP = "DateStop";
    static final String C_APPWATCH_TIMESTART = "TimeStart";
    static final String C_APPWATCH_TIMESTOP = "TimeStop";

    private static final String[] DB_APPWATCH = {
            C_APPWATCH_ID,
            C_APPWATCH_APPLICATION,
            C_APPWATCH_DATESTART,
            C_APPWATCH_DATESTOP,
            C_APPWATCH_TIMESTART,
            C_APPWATCH_TIMESTOP,};

    //Table for period, libido... follow up
    static final String TABLE_USER = "User";
    static final String C_USER_ID = BaseColumns._ID;
    static final String C_USER_PERIOD = "Period";
    static final String C_USER_LIBIDO = "Libido";
    static final String C_USER_SOCIAL_LIFE = "SocialLife";
    static final String C_USER_SEXUAL_LIFE = "SexualLife";
    static final String C_USER_DATE = "Date";
    static final String C_USER_TIME = "Time";

    private static final String[] DB_DAILYSTATUS = {
            C_USER_ID,
            C_USER_PERIOD,
            C_USER_DATE,};

    //Table for GPS data
    static final String TABLE_GPS = "GpsFollowUp";
    static final String C_GPS_ID = BaseColumns._ID;
    static final String C_GPS_LATTITUDE = "Lattitude";
    static final String C_GPS_LONGITUDE = "Longitude";
    static final String C_GPS_DATE = "Date";
    static final String C_GPS_TIME = "Time";

    private static final String[] DB_GPS = {
            C_GPS_ID,
            C_GPS_LATTITUDE,
            C_GPS_LONGITUDE,
            C_GPS_DATE,
            C_GPS_TIME};

    //Table for Status study
    static final String TABLE_STATUS = "StatusStudy";
    static final String C_STATUS_ID = BaseColumns._ID;
    static final String C_STATUS_GPS = "StatusGPS";
    static final String C_STATUS_STUDY = "StatusStudy";
    static final String C_STATUS_DATE = "Date";

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
    static final String C_STARTSTUDY_DATE = "Date";

    private static final String[] DB_START_STUDY = {
            C_STARTSTUDY_ID,
            C_STARTSTUDY_PID,
            C_STARTSTUDY_AGE,
            C_STARTSTUDY_RELATIONSHIP,
            C_STARTSTUDY_CONTRACEPTION,
            C_STARTSTUDY_AVGPERIODLENGHT,
            C_STARTSTUDY_AVGMENSTRUALCYCLE,
            C_STARTSTUDY_DATE};

    //Table for End study
    static final String TABLE_END_STUDY = "EndStudy";
    static final String C_ENDSTUDY_ID = BaseColumns._ID;
    static final String C_ENDSTUDY_RELATIONSHIP = "RelationShip";
    static final String C_ENDSTUDY_CONTRACEPTION = "Contraception";
    static final String C_ENDSTUDY_DATE = "Date";

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
            String sql = "create table " + TABLE_APPWATCH + " (" + C_USER_ID + " integer primary key autoincrement, "
                    + C_APPWATCH_APPLICATION + " text, " + C_APPWATCH_DATESTART + " text, " + C_APPWATCH_DATESTOP + " text, " + C_APPWATCH_TIMESTART + " text, " + C_APPWATCH_TIMESTOP + " text)"; //
            db.execSQL(sql);
            Log.d(TAG, "onCreated sql: " + sql);

            String sql2 = "create table " + TABLE_USER + " (" + C_APPWATCH_ID + " integer primary key autoincrement, "
                    + C_USER_PERIOD + " text, " + C_USER_DATE + " text)"; //
            db.execSQL(sql2);
            Log.d(TAG, "onCreated sql: " + sql2);

            String sql3 = "create table " + TABLE_START_STUDY + " (" + C_STARTSTUDY_ID + " integer primary key autoincrement, "
                    + C_STARTSTUDY_PID + " text, " + C_STARTSTUDY_AGE + " text, " + C_STARTSTUDY_RELATIONSHIP + " text, " + C_STARTSTUDY_CONTRACEPTION + " text, " + C_STARTSTUDY_AVGPERIODLENGHT + " text, " + C_STARTSTUDY_AVGMENSTRUALCYCLE + " text, " + C_STARTSTUDY_DATE + " text)"; //
            db.execSQL(sql3);
            Log.d(TAG, "onCreated sql: " + sql3);

            String sql4 = "create table " + TABLE_GPS + " (" + C_GPS_ID + " integer primary key autoincrement, "
                    + C_GPS_LATTITUDE + " text, " + C_GPS_LONGITUDE + " text, " + C_GPS_DATE + " text, " + C_GPS_TIME + " text)"; //
            db.execSQL(sql4);
            Log.d(TAG, "onCreated sql: " + sql4);
        }

        // Called whenever newVersion != oldVersion
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //

            db.execSQL("drop table if exists " + TABLE_APPWATCH); // drops the old database

            db.execSQL("drop table if exists " + TABLE_USER); // drops the old database

            db.execSQL("drop table if exists " + TABLE_START_STUDY); // drops the old database

            db.execSQL("drop table if exists " + TABLE_GPS); // drops the old database

            Log.d(TAG, "onUpdated");
            onCreate(db); // run onCreate to get new database
        }
    }

    private final DbHelper dbHelper; //

    public UtilsLocalDataBase(Context context) { //
        this.dbHelper = new DbHelper(context);
        Log.i(TAG, "Initialized data");
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

    public ContentValues getOldestElementDb(String Table) { //

        Cursor cursor = null;
        SQLiteDatabase db = null;
        String[] DB_TABLE = null;
        try {
            if (Table.equals(TABLE_APPWATCH)) {
                DB_TABLE = DB_APPWATCH;
            }

            if (Table.equals(TABLE_USER)) {
                DB_TABLE = DB_DAILYSTATUS;
            }

            if (Table.equals(TABLE_GPS)) {
                DB_TABLE = DB_GPS;
            }

            if (Table.equals(TABLE_START_STUDY)) {
                DB_TABLE = DB_START_STUDY;
            }

            SemUpdateDb.acquire();
            db = this.dbHelper.getReadableDatabase();
            cursor = db.query(Table, DB_TABLE, null, null,
                    null, null, null);



            try {
                ContentValues values = new ContentValues();
                values.clear();

                if (cursor.getCount()>0) {
                    cursor.moveToLast();

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
