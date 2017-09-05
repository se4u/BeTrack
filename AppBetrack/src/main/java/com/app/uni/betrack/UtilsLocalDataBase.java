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

    //Table for daily status
    static final String TABLE_DAILYSTATUS = "DailyStatus";
    static final String C_DAILYSTATUS_ID = BaseColumns._ID;
    static final String C_DAILYSTATUS_PID = "ParticipantID";
    static final String C_DAILYSTATUS_MOOD = "Mood";
    static final String C_DAILYSTATUS_SOCIAL = "Social";
    static final String C_DAILYSTATUS_BORED = "Bored";
    static final String C_DAILYSTATUS_RELAXED = "Relaxed";
    static final String C_DAILYSTATUS_INTERACT = "Interact";
    static final String C_DAILYSTATUS_USED_SOCIAL = "UsedSocial";
    static final String C_DAILYSTATUS_SOCIAL_COMPUTER = "SocialComputer";
    static final String C_DAILYSTATUS_WHICH_WEBSITES = "WhichWebsite";
    static final String C_DAILYSTATUS_ACTIVELY = "Actively";
    static final String C_DAILYSTATUS_DATE = "Date";
    static final String C_DAILYSTATUS_TIME = "Time";


    public static final ArrayList<String> DB_DAILYSTATUS = new ArrayList<String>() {{
        add(UtilsLocalDataBase.C_DAILYSTATUS_PID);
        add(UtilsLocalDataBase.C_DAILYSTATUS_MOOD);
        add(UtilsLocalDataBase.C_DAILYSTATUS_SOCIAL);
        add(UtilsLocalDataBase.C_DAILYSTATUS_BORED);
        add(UtilsLocalDataBase.C_DAILYSTATUS_RELAXED);
        add(UtilsLocalDataBase.C_DAILYSTATUS_INTERACT);
        add(UtilsLocalDataBase.C_DAILYSTATUS_USED_SOCIAL);
        add(UtilsLocalDataBase.C_DAILYSTATUS_SOCIAL_COMPUTER);
        add(UtilsLocalDataBase.C_DAILYSTATUS_WHICH_WEBSITES);
        add(UtilsLocalDataBase.C_DAILYSTATUS_ACTIVELY);
        add(UtilsLocalDataBase.C_DAILYSTATUS_DATE);
        add(UtilsLocalDataBase.C_DAILYSTATUS_TIME);
    }};

    public static final ArrayList<Boolean> DB_DAILYSTATUS_CYPHER = new ArrayList<Boolean>() {{
        add(false); //C_DAILYSTATUS_PID
        add(true);  //C_DAILYSTATUS_MOOD
        add(true);  //C_DAILYSTATUS_SOCIAL
        add(true);  //C_DAILYSTATUS_BORED
        add(true);  //C_DAILYSTATUS_RELAXED
        add(true);  //C_DAILYSTATUS_INTERACT
        add(true);  //C_DAILYSTATUS_USED_SOCIAL
        add(true);  //C_DAILYSTATUS_SOCIAL_COMPUTER
        add(true);  //C_DAILYSTATUS_WHICH_WEBSITES
        add(true);  //C_DAILYSTATUS_ACTIVELY
        add(true); //C_DAILYSTATUS_DATE
        add(true); //C_DAILYSTATUS_TIME
    }};

    //Table for daily sleep status
    static final String TABLE_SLEEPSTATUS = "SleepStatus";
    static final String C_SLEEPSTATUS_ID = BaseColumns._ID;
    static final String C_SLEEPSTATUS_PID = "ParticipantID";
    static final String C_SLEEPSTATUS_TIME_TO_BED = "TimeToBed";
    static final String C_SLEEPSTATUS_TIME_TO_SLEEP = "TimeToSleep";
    static final String C_SLEEPSTATUS_FALL_ASLEEP = "FallAsleep";
    static final String C_SLEEPSTATUS_HOWM_WAKEUP = "HowManyWakeUp";
    static final String C_SLEEPSTATUS_HOWL_WAKEUP = "HowLongWakeUp";
    static final String C_SLEEPSTATUS_WHAT_TIME_AWAKE = "WhatTimeLastAwaking";
    static final String C_SLEEPSTATUS_WHAT_TIME_OUT = "WhatTimeOutBed";
    static final String C_SLEEPSTATUS_QUALITY_SLEEP = "QualitySleep";
    static final String C_SLEEPSTATUS_COMMENTS = "Comments";
    static final String C_SLEEPSTATUS_DATE = "Date";
    static final String C_SLEEPSTATUS_TIME = "Time";


    public static final ArrayList<String> DB_SLEEPSTATUS = new ArrayList<String>() {{
        add(UtilsLocalDataBase.C_SLEEPSTATUS_PID);
        add(UtilsLocalDataBase.C_SLEEPSTATUS_TIME_TO_BED);
        add(UtilsLocalDataBase.C_SLEEPSTATUS_TIME_TO_SLEEP);
        add(UtilsLocalDataBase.C_SLEEPSTATUS_FALL_ASLEEP);
        add(UtilsLocalDataBase.C_SLEEPSTATUS_HOWM_WAKEUP);
        add(UtilsLocalDataBase.C_SLEEPSTATUS_HOWL_WAKEUP);
        add(UtilsLocalDataBase.C_SLEEPSTATUS_WHAT_TIME_AWAKE);
        add(UtilsLocalDataBase.C_SLEEPSTATUS_WHAT_TIME_OUT);
        add(UtilsLocalDataBase.C_SLEEPSTATUS_QUALITY_SLEEP);
        add(UtilsLocalDataBase.C_SLEEPSTATUS_COMMENTS);
        add(UtilsLocalDataBase.C_SLEEPSTATUS_DATE);
        add(UtilsLocalDataBase.C_SLEEPSTATUS_TIME);
    }};

    public static final ArrayList<Boolean> DB_SLEEPSTATUS_CYPHER = new ArrayList<Boolean>() {{
        add(false); //C_SLEEPSTATUS_PID
        add(true);  //C_SLEEPSTATUS_TIME_TO_BED
        add(true);  //C_SLEEPSTATUS_TIME_TO_SLEEP
        add(true);  //C_SLEEPSTATUS_FALL_ASLEEP
        add(true);  //C_SLEEPSTATUS_HOWM_WAKEUP
        add(true);  //C_SLEEPSTATUS_HOWL_WAKEUP
        add(true);  //C_SLEEPSTATUS_WHAT_TIME_AWAKE
        add(true);  //C_SLEEPSTATUS_WHAT_TIME_OUT
        add(true);  //C_SLEEPSTATUS_QUALITY_SLEEP
        add(true);  //C_SLEEPSTATUS_COMMENTS
        add(true); //C_SLEEPSTATUS_DATE
        add(true); //C_SLEEPSTATUS_TIME
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
    static final String C_STARTSTUDY_AGE = "age";
    static final String C_STARTSTUDY_SEX = "sex";
    static final String C_STARTSTUDY_ETHNICITY1 = "ethnicity1";
    static final String C_STARTSTUDY_ETHNICITY2 = "ethnicity2";
    static final String C_STARTSTUDY_STUDENT = "student";
    static final String C_STARTSTUDY_ENGLISH_LEVEL1 = "englishlevel1";
    static final String C_STARTSTUDY_ENGLISH_LEVEL2 = "englishlevel2";
    static final String C_STARTSTUDY_ENGLISH_LEVEL3 = "englishlevel3";
    static final String C_STARTSTUDY_ENGLISH_LEVEL4 = "englishlevel4";
    static final String C_STARTSTUDY_ENGLISH_LEVEL5 = "englishlevel5";
    static final String C_STARTSTUDY_ENGLISH_LEVEL6 = "englishlevel6";
    static final String C_STARTSTUDY_UNIVERSITY1 = "university1";
    static final String C_STARTSTUDY_UNIVERSITY2 = "university2";
    static final String C_STARTSTUDY_UNIVERSITY3 = "university3";
    static final String C_STARTSTUDY_DATE = "Date";
    static final String C_STARTSTUDY_TIME = "Time";

    public static final ArrayList<String> DB_START_STUDY = new ArrayList<String>() {{
        add(UtilsLocalDataBase.C_STARTSTUDY_PID);
        add(UtilsLocalDataBase.C_STARTSTUDY_AGE);
        add(UtilsLocalDataBase.C_STARTSTUDY_SEX);
        add(UtilsLocalDataBase.C_STARTSTUDY_ETHNICITY1);
        add(UtilsLocalDataBase.C_STARTSTUDY_ETHNICITY2);
        add(UtilsLocalDataBase.C_STARTSTUDY_STUDENT);
        add(UtilsLocalDataBase.C_STARTSTUDY_ENGLISH_LEVEL1);
        add(UtilsLocalDataBase.C_STARTSTUDY_ENGLISH_LEVEL2);
        add(UtilsLocalDataBase.C_STARTSTUDY_ENGLISH_LEVEL3);
        add(UtilsLocalDataBase.C_STARTSTUDY_ENGLISH_LEVEL4);
        add(UtilsLocalDataBase.C_STARTSTUDY_ENGLISH_LEVEL5);
        add(UtilsLocalDataBase.C_STARTSTUDY_ENGLISH_LEVEL6);
        add(UtilsLocalDataBase.C_STARTSTUDY_UNIVERSITY1);
        add(UtilsLocalDataBase.C_STARTSTUDY_UNIVERSITY2);
        add(UtilsLocalDataBase.C_STARTSTUDY_UNIVERSITY3);
        add(UtilsLocalDataBase.C_STARTSTUDY_DATE);
        add(UtilsLocalDataBase.C_STARTSTUDY_TIME);
    }};

    public static final ArrayList<Boolean> DB_START_STUDY_CYPHER = new ArrayList<Boolean>() {{
        add(false); //C_STARTSTUDY_PID
        add(true);  //C_STARTSTUDY_AGE
        add(true);  //C_STARTSTUDY_SEX
        add(true);  //C_STARTSTUDY_ETHNICITY1
        add(true);  //C_STARTSTUDY_ETHNICITY2
        add(true);  //C_STARTSTUDY_STUDENT
        add(true);  //C_STARTSTUDY_ENGLISH_LEVEL1
        add(true);  //C_STARTSTUDY_ENGLISH_LEVEL2
        add(true);  //C_STARTSTUDY_ENGLISH_LEVEL3
        add(true);  //C_STARTSTUDY_ENGLISH_LEVEL4
        add(true);  //C_STARTSTUDY_ENGLISH_LEVEL5
        add(true);  //C_STARTSTUDY_ENGLISH_LEVEL6
        add(true);  //C_STARTSTUDY_UNIVERSITY1
        add(true);  //C_STARTSTUDY_UNIVERSITY2
        add(true);  //C_STARTSTUDY_UNIVERSITY3
        add(true);  //C_STARTSTUDY_DATE
        add(true);  //C_STARTSTUDY_TIME
    }};

    //Table for End study
    static final String TABLE_END_STUDY = "EndStudy";
    static final String C_ENDSTUDY_ID = BaseColumns._ID;
    static final String C_ENDSTUDY_PID = "ParticipantID";
    static final String C_ENDSTUDY_PHONE_USAGE = "phoneusage";
    static final String C_ENDSTUDY_FACEBOOK = "facebook";
    static final String C_ENDSTUDY_MESSENGER = "messenger";
    static final String C_ENDSTUDY_INSTAGRAM = "instagram";
    static final String C_ENDSTUDY_HANGOUTS = "hangouts";
    static final String C_ENDSTUDY_GOOGLEPLUS = "googleplus";
    static final String C_ENDSTUDY_TWITTER = "twitter";
    static final String C_ENDSTUDY_PINTEREST = "pinterest";
    static final String C_ENDSTUDY_SNAPCHAT = "snapchat";
    static final String C_ENDSTUDY_WHATSAPP = "whatsapp";
    static final String C_ENDSTUDY_SKYPE = "skype";
    static final String C_ENDSTUDY_STUDY1 = "study1";
    static final String C_ENDSTUDY_STUDY2 = "study2";
    static final String C_ENDSTUDY_STUDY3 = "study3";
    static final String C_ENDSTUDY_RESEARCHAPP1 = "researchapp1";
    static final String C_ENDSTUDY_RESEARCHAPP2 = "researchapp2";
    static final String C_ENDSTUDY_RESEARCHAPP3 = "researchapp3";
    static final String C_ENDSTUDY_AVERAGE_PERIODICITY = "averageperiodicity";
    static final String C_ENDSTUDY_STD_DEVIATION = "standarddeviation";
    static final String C_ENDSTUDY_BETRACK_KILLED = "betrackkilled";
    static final String C_ENDSTUDY_BETRACK_POLLING = "betrackpolling";
    static final String C_ENDSTUDY_DATE = "Date";
    static final String C_ENDSTUDY_TIME = "Time";

    public static final ArrayList<String> DB_END_STUDY = new ArrayList<String>() {{
        add(UtilsLocalDataBase.C_ENDSTUDY_PID);
        add(UtilsLocalDataBase.C_ENDSTUDY_PHONE_USAGE);
        add(UtilsLocalDataBase.C_ENDSTUDY_FACEBOOK);
        add(UtilsLocalDataBase.C_ENDSTUDY_MESSENGER);
        add(UtilsLocalDataBase.C_ENDSTUDY_INSTAGRAM);
        add(UtilsLocalDataBase.C_ENDSTUDY_HANGOUTS);
        add(UtilsLocalDataBase.C_ENDSTUDY_GOOGLEPLUS);
        add(UtilsLocalDataBase.C_ENDSTUDY_TWITTER);
        add(UtilsLocalDataBase.C_ENDSTUDY_PINTEREST);
        add(UtilsLocalDataBase.C_ENDSTUDY_SNAPCHAT);
        add(UtilsLocalDataBase.C_ENDSTUDY_WHATSAPP);
        add(UtilsLocalDataBase.C_ENDSTUDY_SKYPE);
        add(UtilsLocalDataBase.C_ENDSTUDY_STUDY1);
        add(UtilsLocalDataBase.C_ENDSTUDY_STUDY2);
        add(UtilsLocalDataBase.C_ENDSTUDY_STUDY3);
        add(UtilsLocalDataBase.C_ENDSTUDY_RESEARCHAPP1);
        add(UtilsLocalDataBase.C_ENDSTUDY_RESEARCHAPP2);
        add(UtilsLocalDataBase.C_ENDSTUDY_RESEARCHAPP3);
        add(UtilsLocalDataBase.C_ENDSTUDY_AVERAGE_PERIODICITY);
        add(UtilsLocalDataBase.C_ENDSTUDY_STD_DEVIATION);
        add(UtilsLocalDataBase.C_ENDSTUDY_BETRACK_KILLED);
        add(UtilsLocalDataBase.C_ENDSTUDY_BETRACK_POLLING);
        add(UtilsLocalDataBase.C_ENDSTUDY_DATE);
        add(UtilsLocalDataBase.C_ENDSTUDY_TIME);
    }};

    public static final ArrayList<Boolean> DB_END_STUDY_CYPHER = new ArrayList<Boolean>() {{
        add(false); //C_ENDSTUDY_PID
        add(true);  //C_ENDSTUDY_PHONE_USAGE
        add(true);  //C_ENDSTUDY_FACEBOOK
        add(true);  //C_ENDSTUDY_MESSENGER
        add(true);  //C_ENDSTUDY_INSTAGRAM
        add(true);  //C_ENDSTUDY_HANGOUTS
        add(true);  //C_ENDSTUDY_GOOGLEPLUS
        add(true);  //C_ENDSTUDY_TWITTER
        add(true);  //C_ENDSTUDY_PINTEREST
        add(true);  //C_ENDSTUDY_SNAPCHAT
        add(true);  //C_ENDSTUDY_WHATSAPP
        add(true);  //C_ENDSTUDY_SKYPE
        add(true);  //C_ENDSTUDY_STUDY1
        add(true);  //C_ENDSTUDY_STUDY2
        add(true);  //C_ENDSTUDY_STUDY3
        add(true);  //C_ENDSTUDY_RESEARCHAPP1
        add(true);  //C_ENDSTUDY_RESEARCHAPP2
        add(true);  //C_ENDSTUDY_RESEARCHAPP3
        add(true);  //C_ENDSTUDY_AVERAGE_PERIODICITY
        add(true);  //C_ENDSTUDY_STD_DEVIATION
        add(true);  //C_ENDSTUDY_BETRACK_KILLED
        add(true);  //C_ENDSTUDY_BETRACK_POLLING
        add(true);  //C_ENDSTUDY_DATE
        add(true);  //C_ENDSTUDY_TIME
    }};

    //Table for phone usage follow up
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
                    + C_DAILYSTATUS_MOOD + " text, "
                    + C_DAILYSTATUS_SOCIAL + " text, "
                    + C_DAILYSTATUS_BORED + " text, "
                    + C_DAILYSTATUS_RELAXED + " text, "
                    + C_DAILYSTATUS_INTERACT + " text, "
                    + C_DAILYSTATUS_USED_SOCIAL + " text, "
                    + C_DAILYSTATUS_SOCIAL_COMPUTER + " text, "
                    + C_DAILYSTATUS_WHICH_WEBSITES + " text, "
                    + C_DAILYSTATUS_ACTIVELY + " text, "
                    + C_DAILYSTATUS_DATE + " text, "
                    + C_DAILYSTATUS_TIME + " text)"; //
            db.execSQL(sql2);
            Log.d(TAG, "onCreated sql: " + sql2);

            String sql3 = "create table " + TABLE_START_STUDY + " (" + C_STARTSTUDY_ID + " integer primary key autoincrement, "
                    +  C_STARTSTUDY_AGE + " text, "
                    +  C_STARTSTUDY_SEX + " text, "
                    + C_STARTSTUDY_ETHNICITY1 + " text, "
                    + C_STARTSTUDY_ETHNICITY2 + " text, "
                    + C_STARTSTUDY_STUDENT + " text, "
                    + C_STARTSTUDY_ENGLISH_LEVEL1 + " text, "
                    + C_STARTSTUDY_ENGLISH_LEVEL2 + " text, "
                    + C_STARTSTUDY_ENGLISH_LEVEL3 + " text, "
                    + C_STARTSTUDY_ENGLISH_LEVEL4 + " text, "
                    + C_STARTSTUDY_ENGLISH_LEVEL5 + " text, "
                    + C_STARTSTUDY_ENGLISH_LEVEL6 + " text, "
                    + C_STARTSTUDY_UNIVERSITY1 + " text, "
                    + C_STARTSTUDY_UNIVERSITY2 + " text, "
                    + C_STARTSTUDY_UNIVERSITY3 + " text, "
                    + C_STARTSTUDY_DATE + " text, "
                    + C_STARTSTUDY_TIME + " text)"; //
            db.execSQL(sql3);
            Log.d(TAG, "onCreated sql: " + sql3);

            String sql4 = "create table " + TABLE_SLEEPSTATUS + " (" + C_SLEEPSTATUS_ID + " integer primary key autoincrement, "
                    +  C_SLEEPSTATUS_PID + " text, "
                    + C_SLEEPSTATUS_TIME_TO_BED + " text, "
                    + C_SLEEPSTATUS_TIME_TO_SLEEP + " text, "
                    + C_SLEEPSTATUS_FALL_ASLEEP + " text, "
                    + C_SLEEPSTATUS_HOWM_WAKEUP + " text, "
                    + C_SLEEPSTATUS_HOWL_WAKEUP + " text, "
                    + C_SLEEPSTATUS_WHAT_TIME_AWAKE + " text, "
                    + C_SLEEPSTATUS_WHAT_TIME_OUT + " text, "
                    + C_SLEEPSTATUS_QUALITY_SLEEP + " text, "
                    + C_SLEEPSTATUS_COMMENTS + " text, "
                    + C_SLEEPSTATUS_DATE + " text, "
                    + C_SLEEPSTATUS_TIME + " text)"; //
            db.execSQL(sql4);
            Log.d(TAG, "onCreated sql: " + sql4);

            String sql6 = "create table " + TABLE_END_STUDY + " (" + C_ENDSTUDY_ID + " integer primary key autoincrement, "
                    + C_ENDSTUDY_PHONE_USAGE + " text, "
                    + C_ENDSTUDY_FACEBOOK + " text, "
                    + C_ENDSTUDY_MESSENGER + " text, "
                    + C_ENDSTUDY_INSTAGRAM + " text, "
                    + C_ENDSTUDY_HANGOUTS + " text, "
                    + C_ENDSTUDY_GOOGLEPLUS + " text, "
                    + C_ENDSTUDY_TWITTER + " text, "
                    + C_ENDSTUDY_PINTEREST + " text, "
                    + C_ENDSTUDY_SNAPCHAT + " text, "
                    + C_ENDSTUDY_WHATSAPP + " text, "
                    + C_ENDSTUDY_SKYPE + " text, "
                    + C_ENDSTUDY_STUDY1 + " text, "
                    + C_ENDSTUDY_STUDY2 + " text, "
                    + C_ENDSTUDY_STUDY3 + " text, "
                    + C_ENDSTUDY_RESEARCHAPP1 + " text, "
                    + C_ENDSTUDY_RESEARCHAPP2 + " text, "
                    + C_ENDSTUDY_RESEARCHAPP3 + " text, "
                    + C_ENDSTUDY_AVERAGE_PERIODICITY + " text, "
                    + C_ENDSTUDY_STD_DEVIATION + " text, "
                    + C_ENDSTUDY_BETRACK_KILLED + " text, "
                    + C_ENDSTUDY_BETRACK_POLLING + " text, "
                    + C_ENDSTUDY_DATE +  " text, "
                    + C_ENDSTUDY_TIME + " text)";

            db.execSQL(sql6);
            Log.d(TAG, "onCreated sql: " + sql6);
            String sql7 = "create table " + TABLE_SESSION_KEY + " (" + C_SESSION_KEY_ID + " integer primary key autoincrement, "
                    + C_SESSION_KEY_BLOB + " text, "
                    + C_SESSION_KEY_DATE + " text, "
                    + C_SESSION_KEY_TIME + " text)";
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

            db.execSQL("drop table if exists " + TABLE_SLEEPSTATUS); // drops the old database

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
