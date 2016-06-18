package com.app.uni.betrack;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by cevincent on 6/3/16.
 */
public class LocalDataBase extends SQLiteOpenHelper {

    static final String TAG = "SQLLocalTable";
    static final String DB_NAME = "StudyDataBase.db"; //
    static final int DB_VERSION = 1; //
    static final String TABLE = "Study"; //
    static final String C_ID = BaseColumns._ID;
    static final String C_APPLICATION = "Application";
    static final String C_DATE = "Date";
    static final String C_TIMESTART = "TimeStart";
    static final String C_TIMESTOP = "TimeStop";
    Context context;

    /*$result = mysqli_query($con,"INSERT INTO UniTestTable (Application, Date, TimeStart, TimeStop)
                           VALUES ('$application', '$date', '$timestart', '$timestop' )");*/

    // Constructor
    public LocalDataBase(Context context) { //
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }
    // Called only once, first time the DB is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE + " (" + C_ID + " int primary key, "
                + C_APPLICATION + " text, " + C_DATE + " text, " + C_TIMESTART + " text, " + C_TIMESTOP + " text)"; //
        db.execSQL(sql);
        Log.d(TAG, "onCreated sql: " + sql);
    }

    // Called whenever newVersion != oldVersion
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //

        db.execSQL("drop table if exists " + TABLE); // drops the old database
        Log.d(TAG, "onUpdated");
        onCreate(db); // run onCreate to get new database
    }

}
