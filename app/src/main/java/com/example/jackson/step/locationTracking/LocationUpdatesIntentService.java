package com.example.jackson.step.locationTracking;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.jackson.step.CreateTrackPointActivity;
import com.example.jackson.step.Global;
import com.example.jackson.step.Utils;
import com.example.jackson.step.database.DatabaseHelper;
import com.google.android.gms.location.LocationResult;

import java.time.LocalDateTime;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LocationUpdatesIntentService extends IntentService {

    DatabaseHelper myDB;

    public static final String ACTION_PROCESS_UPDATES =
            "com.example.jackson.step.locationtracking.locationupdatesintentservice.action" +
                    ".PROCESS_UPDATES";
    private static final String TAG = "LocationUpdatesIS";
    public static final String ACTION_FINISHED = "ACTION_FINISHED";



    public LocationUpdatesIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "LocationService open");
        IntentFilter filter = new IntentFilter();
        filter.addAction(LocationUpdatesIntentService.ACTION_FINISHED);




//        myDB = new DatabaseHelper(this);
    }
    @Override
    public void onDestroy(){



    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.i(TAG, action);
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
//                    List<Location> locations = result.getLocations();
//                    Utils.setLocationUpdatesResult(this, locations);
//                    Log.i(TAG, Utils.getLocationUpdatesResult(this));

                    Location lastLocation = result.getLastLocation();

                    Global.location = lastLocation;
                    LocalDateTime localDateTime = LocalDateTime.now();

                    Utils.setLocationUpdatesResult(this, lastLocation);
                    Intent createTP = new Intent(this, CreateTrackPointActivity.class);
                    startActivity(createTP);




                }
            }

        }
    }





}
