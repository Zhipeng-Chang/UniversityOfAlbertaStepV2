package com.example.zhouyuhong.step;

import android.content.Context;
import android.location.Location;

import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;

import java.io.InputStream;

/**
 * Created by Jackson on 2018-03-04.
 */

public class Global {
    public static Context context = null;

    public static Location location = null;

    public static String activityType = null;
    public static Integer activityConfidence = 0;

    public static boolean running = false;
    public static long waitingTime = 0;

    public static InputStream is = null;
    //coordinate system
    public static SpatialReference wgs84=SpatialReference.create(4326);

    public static GraphicsOverlay graphicsOverlay;

    public static PointCollection trackpoints;

    public static Polyline trackRoute;

    public static SimpleLineSymbol lineSymbol;

    public static Graphic trackRouteGraphic;


}
