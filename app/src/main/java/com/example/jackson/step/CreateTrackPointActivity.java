package com.example.jackson.step;

import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.example.jackson.step.database.DatabaseHelper;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class CreateTrackPointActivity extends AppCompatActivity {
    private static final int JOB_ID2 = 600;
    private static final String TAG = "TrackedPoint";
    DatabaseHelper dbHelper;
    SQLiteDatabase myDB;
    public double lat, lon;
    public String activityType;
    public Integer activityConfidence;

    public float speed;
    public long timeStamp;
    public boolean crosswalkCheck;



    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        Log.i(TAG, "CreateTrackPointIS open");
        dbHelper = new DatabaseHelper(this);
        myDB = dbHelper.getWritableDatabase();
        dbHelper.onCreate(myDB);

        lat = Global.location.getLatitude();
        lon = Global.location.getLongitude();
        speed = Global.location.getSpeed();
        timeStamp = Global.location.getTime();
        Resources resources = Global.context.getResources();


        activityType = Global.activityType;
        activityConfidence = Global.activityConfidence;
        //reset waitingTime to 0 every time once location is updated.
        Global.waitingTime=0;

        Log.i(TAG, "Type checking" +activityType);
        Log.i(TAG, "Confidence checking" +activityConfidence);


        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CANADA);
        String formatDate = df.format(timeStamp);

        singleTrackedPoint A = new singleTrackedPoint(lat, lon, speed, formatDate, activityType,
                activityConfidence, Global.waitingTime);

        Log.i(TAG, "TrackedPoint has created" +A.toString());

        //TODO Data table should be modified.
        boolean isInserted = dbHelper.insertData(Global.location, activityType, activityConfidence, crosswalkCheck, Global.waitingTime);

        if(isInserted == true) {
            Log.i(TAG, "TrackedPoint saved" + A.toString());
        }
        dbHelper.close();

        finish();


    }

}
