package com.example.zhouyuhong.step.locationTracking;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.example.zhouyuhong.step.CreateTrackPointActivity;
import com.example.zhouyuhong.step.Global;
import com.example.zhouyuhong.step.Utils;
import com.example.zhouyuhong.step.database.DatabaseHelper;
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
            "com.example.zhouyuhong.step.locationtracking.locationupdatesintentservice.action" +
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
                    Location lastLocation = result.getLastLocation();

                    Global.location = lastLocation;
                    LocalDateTime localDateTime = LocalDateTime.now();

                    Utils.setLocationUpdatesResult(this, lastLocation);

                    //implement the last location add new points and
                    //draw once
                    //global variables
                    Global.trackpoints.add(new Point(lastLocation.getLongitude(),lastLocation.getLatitude(),Global.wgs84));
                    Global.trackRoute=new Polyline(Global.trackpoints);
                    Global.lineSymbol=new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID,Color.rgb(128,0,128),2);
                    Global.trackRouteGraphic=new Graphic(Global.trackRoute,Global.lineSymbol);
                    Global.graphicsOverlay.getGraphics().add(Global.trackRouteGraphic);

                    Intent createTP = new Intent(this, CreateTrackPointActivity.class);
                    startActivity(createTP);

                }
            }

        }
    }
}
