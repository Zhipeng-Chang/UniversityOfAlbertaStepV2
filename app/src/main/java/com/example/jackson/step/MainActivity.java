package com.example.jackson.step;


import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import android.widget.Toast;


import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;

import com.example.jackson.step.database.DatabaseHelperWaitingTime;
import com.example.jackson.step.locationTracking.LocationUpdatesIntentService;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    protected static final String TAG = "MainActivity";
    private Context mContext;
    private MainActivity activity = this;

    // location update setting 
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final long UPDATE_INTERVAL = 3000; // Every 3 seconds.
    private static final long FASTEST_UPDATE_INTERVAL = 1000; // 1 seconds
    private static final long MAX_WAIT_TIME = UPDATE_INTERVAL * 5; // 15 seconds.


    // The entry point for interacting with activity recognition & location tracking

    private ActivityRecognitionClient mActivityRecognitionClient;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;


    // UI elements.
    private Button mRequestUpdatesButton;
    private Button mRemoveUpdatesButton;
    private Button startTimerButton;
    private Button stopTimerButton;
    private MapView mMapView;
    private LocationDisplay mLocationDisplay;


    // SQLite data storage
    DatabaseHelperWaitingTime dbHelperWT;
    SQLiteDatabase myDB;
    private Animation animAlpha;


    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("STEPS");

        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .clear()
                .apply();

        SharedPreferences mPreferences = getSharedPreferences("CurrentUser",
                MODE_PRIVATE);
        boolean Agree = mPreferences.getBoolean("agree", false);
        if (Agree == false){
            Intent intent = new Intent(activity, ConsentAgreeActivity.class);
            startActivity(intent);
            finish();
        }
        //PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("Global.Agree", true).apply();

        mContext = this;
        dbHelperWT = new DatabaseHelperWaitingTime(this);
        myDB = dbHelperWT.getWritableDatabase();
        dbHelperWT.onCreate(myDB);

        // Get the UI widgets.
        mRequestUpdatesButton = (Button) findViewById(R.id.request_updates);
        mRemoveUpdatesButton = (Button) findViewById(R.id.remove_updates);
        startTimerButton = (Button) findViewById(R.id.start_Timer);
        stopTimerButton = (Button) findViewById(R.id.stop_Timer);
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        mMapView = (MapView) findViewById(R.id.mapView);




        startTimerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Toast.makeText(mContext,
                        "Timer started",
                        Toast.LENGTH_SHORT)
                        .show();
                double startTime = System.currentTimeMillis();
                dbHelperWT.insertData(startTime, "START");
                startTimerButton.setEnabled(false);
                stopTimerButton.setEnabled(true);
            }
        });

        stopTimerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Toast.makeText(mContext,
                        "Timer stoped",
                        Toast.LENGTH_SHORT)
                        .show();
                double stopTime = System.currentTimeMillis();
                dbHelperWT.insertData(stopTime, "STOP");
                startTimerButton.setEnabled(true);
                stopTimerButton.setEnabled(false);
            }
        });

        ArcGISMap map = new ArcGISMap("https://uofa.maps.arcgis.com/home/webmap/viewer.html?webmap=f3ed5f0f05454845ad4d9869a79d6c7f");
        // set the map to be displayed in this view
        mMapView.setMap(map);
        mLocationDisplay = mMapView.getLocationDisplay();
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
        if (!mLocationDisplay.isStarted())
            mLocationDisplay.startAsync();

        if (!checkPermissions()) {
            requestPermissions();
        }
        mActivityRecognitionClient = new ActivityRecognitionClient(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        setButtonsEnabledState();

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    protected void onStop() {

        super.onStop();

    }


    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);
    }


    /**
     * Registers for activity recognition updates using
     * {@link ActivityRecognitionClient#requestActivityUpdates(long, PendingIntent)}.
     * Registers success and failure callbacks.
     */
    public void requestUpdatesButtonHandler(View view) {
        view.startAnimation(animAlpha);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Task<Void> requestUpdatesTask = mActivityRecognitionClient.requestActivityUpdates(
                Constants.DETECTION_INTERVAL_IN_MILLISECONDS,
                getActivityDetectionPendingIntent());

        requestUpdatesTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(mContext,
                        "Tracking strated",
                        Toast.LENGTH_SHORT)
                        .show();
                setActivityUpdatesRequestedState(true);
                Log.i(TAG, "setRequestedState");
                updateDetectedActivity();
                Log.i(TAG, "updateDetectedActivity");

            }
        });

        requestUpdatesTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext,
                        "Tracking not started",
                        Toast.LENGTH_SHORT)
                        .show();
                setActivityUpdatesRequestedState(false);
            }
        });

        requestLocationUpdates();

    }

    /**
     * Removes activity recognition updates using
     * {@link ActivityRecognitionClient#removeActivityUpdates(PendingIntent)}. Registers success and
     * failure callbacks.
     */
    public void removeUpdatesButtonHandler(View view) {
        view.startAnimation(animAlpha);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Task<Void> removeUpdatesTask = mActivityRecognitionClient.removeActivityUpdates(
                getActivityDetectionPendingIntent());
        removeUpdatesTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(mContext,
                        "Tracking stoped",
                        Toast.LENGTH_SHORT)
                        .show();
                setActivityUpdatesRequestedState(false);
            }
        });

        removeUpdatesTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Tracking didn't stop.",
                        Toast.LENGTH_SHORT).show();
                setActivityUpdatesRequestedState(true);
            }
        });

        removeLocationUpdates();
    }


    /**
     * Handles the Request Updates button and requests start of location updates.
     */
    public void requestLocationUpdates() {
        try {
            Log.i(TAG, "request location updates");
            Utils.setRequestingLocationUpdates(this, true);
            setButtonsEnabledState();
            Task<Void> requestLocationTask = mFusedLocationClient.requestLocationUpdates(mLocationRequest, getLocationUpdatesPendingIntent());
            requestLocationTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Utils.setRequestingLocationUpdates(mContext, true);
                    updateLocation();
                }
            });

            requestLocationTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    setActivityUpdatesRequestedState(true);
                }
            });

        } catch (SecurityException e) {
            Utils.setRequestingLocationUpdates(this, false);
            e.printStackTrace();
        }
    }


    /**
     * Handles the Remove Updates button, and requests removal of location updates.
     */
    public void removeLocationUpdates() {
        Log.i(TAG, "Location update stops");
        Utils.setRequestingLocationUpdates(this, false);
        setButtonsEnabledState();
        mFusedLocationClient.removeLocationUpdates(getLocationUpdatesPendingIntent());

    }


    /**
     * Gets a PendingIntent to be sent for each activity detection.
     */
    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, DetectedActivitiesIntentService.class);
        Log.i(TAG, "getActivityPendingIntent");

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getLocationUpdatesPendingIntent() {
        Intent intent = new Intent(this, LocationUpdatesIntentService.class);
        intent.setAction(LocationUpdatesIntentService.ACTION_PROCESS_UPDATES);
        Log.i(TAG, "getLocationPendingIntent");

        return PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }



    /**
     * Ensures that only one button is enabled at any time. The Request Activity Updates button is
     * enabled if the user hasn't yet requested activity updates. The Remove Activity Updates button
     * is enabled if the user has requested activity updates.
     */
    private void setButtonsEnabledState() {
        if (getActivityUpdatesRequestedState() && Utils.getRequestingLocationUpdates(this)) {
            mRequestUpdatesButton.setEnabled(false);
            mRemoveUpdatesButton.setEnabled(true);
            startTimerButton.setEnabled(false);
            stopTimerButton.setEnabled(true);
        } else {
            mRequestUpdatesButton.setEnabled(true);
            mRemoveUpdatesButton.setEnabled(false);
            startTimerButton.setEnabled(true);
            stopTimerButton.setEnabled(false);
        }
    }

    /**
     * Retrieves the boolean from SharedPreferences that tracks whether we are requesting activity
     * updates.
     */
    private boolean getActivityUpdatesRequestedState() {
        return PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(Constants.KEY_ACTIVITY_UPDATES_REQUESTED, false);
    }

    /**
     * Sets the boolean in SharedPreferences that tracks whether we are requesting activity
     * updates.
     */
    private void setActivityUpdatesRequestedState(boolean requesting) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(Constants.KEY_ACTIVITY_UPDATES_REQUESTED, requesting)
                .apply();
        setButtonsEnabledState();
    }

    /**
     * Processes the list of freshly detected activities. Asks the adapter to update its list of
     * DetectedActivities with new {@code DetectedActivity} objects reflecting the latest detected
     * activities.
     */
    protected void updateDetectedActivity() {
        Global.context = mContext;
        Log.i(TAG, "detectedActivityType: " + Global.activityType);
        Log.i(TAG, "detectedActivityConfidence: " + Global.activityConfidence + "%");

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(Constants.KEY_DETECTED_ACTIVITY_TYPE) || s.equals(Constants.KEY_DETECTED_ACTIVITY_CONFIDENCE)) {
            updateDetectedActivity();
        }

        if (s.equals(Utils.KEY_LOCATION_UPDATES_RESULT)) {
            updateLocation();
        }
    }


    private void updateLocation() {
        String lastUpdate = Utils.getLocationUpdatesResult(this);
        Log.i(TAG, "updateLocationDisplay: " + lastUpdate);
    }

    public void recenterMapView (View view){
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        if (!mLocationDisplay.isStarted())
            mLocationDisplay.startAsync();
    }


//    permission control

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {

        Log.i(TAG, "Requesting permission");
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }


}