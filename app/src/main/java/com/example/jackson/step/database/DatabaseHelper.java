package com.example.jackson.step.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import com.example.jackson.step.Global;
import com.google.android.gms.location.DetectedActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Seolha Lee on 2018-02-08.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static long TODAY = System.currentTimeMillis();
    public static SimpleDateFormat DFforTable = new SimpleDateFormat("ddMMyy", Locale.CANADA);
    public static String DATETAG = DFforTable.format(TODAY);
    //declare variables

    public static final String DATABASE_NAME = "STEPS"+DATETAG+".db";
    public static final String TABLE_NAME = "STEPS"+DATETAG+"_table";
    public static final String col_1 = "ID";
    public static final String col_2 = "LONGITUDE";
    public static final String col_3 = "LATITUDE";
    public static final String col_4 = "DATETIME";
    public static final String col_5 = "SPEED";
    public static final String col_6 = "ACTIVITY_TITLE";
    public static final String col_7 = "ACTIVITY_CONFIDENCE";
    public static final String col_8 = "CROSSWALK";
    public static final String col_9 = "WAITINGTIME";
    public static final String col_10 = "GEOFENCETYPE";
    public static final String col_11 = "TRIGGERING_GEOFENCE_ID_LIST";


    //default constructor. database created with this.
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);


    }

    //what happens to this database
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS  " + TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "LONGITUDE DOUBLE, LATITUDE DOUBLE, DATETIME TEXT, SPEED FLOAT, ACTIVITY_TITLE TEXT, ACTIVITY_CONFIDENCE INTEGER, CROSSWALK BOOLEAN, WAITINGTIME LONG, GEOFENCETYPE TEXT, TRIGGERING_GEOFENCE_ID_LIST TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_NAME);
        onCreate(db);

    }


    public boolean insertData (Location location, String ActivityType, int ACTIVITY_CONFIDENCE, boolean CROSSWALK, long WAITINGTIME, String GeofenceType, String TriggeringGeofenceIDList){
        SQLiteDatabase db = this.getWritableDatabase(); //check database creation
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CANADA);
        String formatDate = df.format(location.getTime());
        contentValues.put(col_2, location.getLongitude());
        contentValues.put(col_3, location.getLatitude());
        contentValues.put(col_4, formatDate);
        contentValues.put(col_5, location.getSpeed());
        contentValues.put(col_6, ActivityType);
        contentValues.put(col_7, ACTIVITY_CONFIDENCE);
        contentValues.put(col_8, CROSSWALK);
        contentValues.put(col_9, WAITINGTIME);
        contentValues.put(col_10, GeofenceType);
        contentValues.put(col_11, TriggeringGeofenceIDList);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME, null);
        return res;
    }
}
