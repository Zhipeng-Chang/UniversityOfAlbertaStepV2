package com.example.zhouyuhong.step.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class DatabaseHelperWaitingTime extends SQLiteOpenHelper{

    private static final String TAG = "DB Helper WT";
    public static long TODAY = System.currentTimeMillis();
    public static SimpleDateFormat DFforTable = new SimpleDateFormat("ddMMyy", Locale.CANADA);
    public static String DATETAG = DFforTable.format(TODAY);

    public static final String DATABASE_NAME = "STEPS"+DATETAG+".db";
    public static final String TABLE_NAME = "WAITINGTIME"+DATETAG+"_table";
    public static final String col_1 = "ID";
    public static final String col_2 = "TIMER_STATUS";
    public static final String col_3 = "DATETIME";

    public DatabaseHelperWaitingTime(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS  " + TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TIMER_STATUS TEXT, DATETIME TEXT)");
        Log.i(TAG, "DB has created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData (double timeStamp, String timerStatus){
        SQLiteDatabase db = this.getWritableDatabase(); //check database creation
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CANADA);
        String formatDate = df.format(timeStamp);
        contentValues.put(col_2, timerStatus);
        contentValues.put(col_3, formatDate);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
}
