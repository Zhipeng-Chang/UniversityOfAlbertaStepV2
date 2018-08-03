package com.example.jackson.step;


import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.Map;

public class MapActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private MapView mMapView;
    private LocationDisplay mLocationDisplay;

    private ImageView recenterImage;

    private int requestCode = 2;
    String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        recenterImage = (ImageView) findViewById(R.id.imageView2);
        // inflate MapView from layout
        mMapView = (MapView) findViewById(R.id.mapView);
        // create a map with the BasemapType topographic
        ArcGISMap map = new ArcGISMap("https://uofa.maps.arcgis.com/home/webmap/viewer.html?webmap=f3ed5f0f05454845ad4d9869a79d6c7f");
        // set the map to be displayed in this view
        mMapView.setMap(map);

        mLocationDisplay = mMapView.getLocationDisplay();
        mLocationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {

                // If LocationDisplay started OK, then continue.
                if (dataSourceStatusChangedEvent.isStarted()) {
                    Log.i(TAG, "dataSourceStatusChangedEvent is started.");
                    return;
                }
                // No error is reported, then continue.
                if (dataSourceStatusChangedEvent.getError() == null) {
                    Log.i(TAG, "no error in dataSourceStatusChangedEvent");
                    return;
                }
                Log.i(TAG, "checking an error found in dataSourceStatusChangedEvent, ");

                boolean permissionCheck1 = ContextCompat.checkSelfPermission(MapActivity.this, reqPermissions[0]) ==
                        PackageManager.PERMISSION_GRANTED;
                boolean permissionCheck2 = ContextCompat.checkSelfPermission(MapActivity.this, reqPermissions[1]) ==
                        PackageManager.PERMISSION_GRANTED;

                if (!(permissionCheck1 && permissionCheck2)) {
                    // If permissions are not already granted, request permission from the user.
                    ActivityCompat.requestPermissions(MapActivity.this, reqPermissions, requestCode);
                } else {
                    Log.i(TAG,String.format("Error in DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent
                            .getSource().getLocationDataSource().getError().getMessage())); // Report other unknown failure types to the user - for example, location services may not
                }


            }
        });
        // START Location Display with AutoPanMode
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
        if (!mLocationDisplay.isStarted())
            mLocationDisplay.startAsync();

    }

    @Override
    protected void onPause(){
        super.onPause();
        mMapView.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mMapView.resume();
    }

    public void recenterMapView (View view){
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        if (!mLocationDisplay.isStarted())
            mLocationDisplay.startAsync();
    }
}

