package com.example.jackson.step;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.Geofence;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Jackson on 2018-03-04.
 */

public class Global {
    public static Context context = null;

    public static Location location = null;

    public static String activityType = null;
    public static Integer activityConfidence = 0;

    public static String geofenceTransitionType = "";
    public static String triggeringGeofenceIDList = null;

    public static boolean running = false;
    public static long startTime = 0;
    public static long stopTime = 0;
    public static long waitingTime = 0;

    public static InputStream is = null;

    public static List<Geofence> triggeringGeofences = null;
}
