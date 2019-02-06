package com.example.zhouyuhong.step.database;

import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.location.Location;
import android.util.Log;

import java.text.SimpleDateFormat;
        import java.util.Locale;

/**
 * Created by Jackson on 2018-08-25.
 */

public class DatabaseQuestionaire extends SQLiteOpenHelper {

    public static long TODAY = System.currentTimeMillis();
    public static SimpleDateFormat DFforTable = new SimpleDateFormat("ddMMyy", Locale.CANADA);
    public static String DATETAG = DFforTable.format(TODAY);
    //declare variables

    public static final String DATABASE_NAME = "QuestionnaireDatabase.db";
    public static final String TABLE_NAME = "Questionnaire"+DATETAG+"_table";
    public static final String col_1 = "ID";
    public static final String col_2 = "Question1";
    public static final String col_3 = "Question2";
    public static final String col_4 = "Question3";
    public static final String col_5 = "Question4";
    public static final String col_6 = "Question5";


    //default constructor. database created with this.
    public DatabaseQuestionaire(Context context) {
        super(context, DATABASE_NAME, null, 3);


    }

    //what happens to this database
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS  " + TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Question1 TEXT, Question2 TEXT, Question3 TEXT, Question4 TEXT, Question5 TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_NAME);
        onCreate(db);

    }


    public boolean insertData (int Question, String Answer) {
        SQLiteDatabase db = this.getWritableDatabase(); //check database creation
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CANADA);
        Log.i("Database", "Saving question to dababase:" + Question);
        if (Question == 1)
            contentValues.put(col_2, Answer);
        else if (Question == 2)
            contentValues.put(col_3, Answer);
        else if (Question == 3)
            contentValues.put(col_4, Answer);
        else if (Question == 4)
            contentValues.put(col_5, Answer);
        else if (Question == 5)
            contentValues.put(col_6, Answer);

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
