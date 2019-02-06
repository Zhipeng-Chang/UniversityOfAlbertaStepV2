package com.example.zhouyuhong.step;

/**
 * Created by Seolha Lee on 2018-02-17.
 */

import android.app.IntentService;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.zhouyuhong.step.database.DatabaseHelper;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

/**
 *  IntentService for handling incoming intents that are generated as a result of requesting
 *  activity updates using
 *  {@link com.google.android.gms.location.ActivityRecognitionApi#requestActivityUpdates}.
 */
public class DetectedActivitiesIntentService extends IntentService {

    protected static final String TAG = "DetectedActivitiesIS";
    DatabaseHelper myDB;
    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public DetectedActivitiesIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    @Override
    public void onCreate() {

        super.onCreate();
        Log.i(TAG, "ActivityService open");
//
//        myDB = new DatabaseHelper(this);
    }

    /**
     * Handles incoming intents.
     * @param intent The Intent is provided (inside a PendingIntent) when requestActivityUpdates()
     *               is called.
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void onHandleIntent(Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.
        ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();
        DetectedActivity detectedActivity = result.getMostProbableActivity();

        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(Constants.KEY_DETECTED_ACTIVITY_TYPE,
                        Utils.getActivityString(getApplicationContext(), detectedActivity.getType()))
                .putInt(Constants.KEY_DETECTED_ACTIVITY_CONFIDENCE, detectedActivity.getConfidence())
                .apply();
        Global.activityType = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.KEY_DETECTED_ACTIVITY_TYPE, "");
        Global.activityConfidence = PreferenceManager.getDefaultSharedPreferences(this).getInt(Constants.KEY_DETECTED_ACTIVITY_CONFIDENCE, 0);

        Log.i(TAG, "activities detected");
        Log.i(TAG, Utils.getActivityString(
                    getApplicationContext(),
                    detectedActivity.getType()) + " " + detectedActivity.getConfidence() + "%"
            );
        }

}
