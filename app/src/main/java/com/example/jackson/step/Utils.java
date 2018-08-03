package com.example.jackson.step;

/**
 * Created by Seolha Lee on 2018-02-17.
 */

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.preference.PreferenceManager;

import com.google.android.gms.location.DetectedActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utility methods used in this sample.
 */
public class Utils {

    private Utils() {}

    //Location Tracking Utils

    final static String KEY_LOCATION_UPDATES_REQUESTED = "location-updates-requested";
    final static String KEY_LOCATION_UPDATES_RESULT = "location-update-result";
    final static String CHANNEL_ID = "channel_01";

    static void setRequestingLocationUpdates(Context context, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_LOCATION_UPDATES_REQUESTED, value)
                .apply();
    }

    static boolean getRequestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_LOCATION_UPDATES_REQUESTED, false);
    }
    /**
     * Returns the title for reporting about a list of {@link Location} objects.
     *
     * @param context The {@link Context}.
     */
    static String getLocationResultTitle(Context context, List<Location> locations) {
        String numLocationsReported = context.getResources().getQuantityString(
                R.plurals.num_locations_reported, locations.size(), locations.size());
        return numLocationsReported + ": " + DateFormat.getDateTimeInstance().format(new Date());
    }

    /**
     * Returns te text for reporting about a list of  {@link Location} objects.
     *
     */
    private static String getLocationResultText(Context context, Location lastLocation) {
//        if (locations.isEmpty()) {
//            return context.getString(R.string.unknown_location);
//        }
        StringBuilder sb = new StringBuilder();
        if(lastLocation!=null) {

//            for (Location location : locations) {
                sb.append("\n");
                sb.append(lastLocation.getLatitude());
                sb.append(", ");
                sb.append(lastLocation.getLongitude());
                sb.append(", ");
                sb.append(lastLocation.getSpeed());
                sb.append(", ");
                sb.append(lastLocation.getTime());
//            sb.append("\n");
//            }
        }
        return sb.toString();
    }



//    public static void setLocationUpdatesResult(Context context, List<Location> locations) {
//        PreferenceManager.getDefaultSharedPreferences(context)
//                .edit()
//                .putString(KEY_LOCATION_UPDATES_RESULT, getLocationResultTitle(context, locations)
//                        + "\n" + getLocationResultText(context, locations))
//                .apply();
//
//    }

    public static void setLocationUpdatesResult(Context context, Location lastLocation) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_LOCATION_UPDATES_RESULT, getLocationResultText(context, lastLocation))
                .apply();

    }

    public static String getLocationUpdatesResult(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_UPDATES_RESULT, "");
    }

    public static String getLastUpdatedLocation (Context context){


        String lastLocation = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_UPDATES_RESULT, "");

        int startIndex = lastLocation.lastIndexOf("\n");

        String result = null;

        if(startIndex!=-1 && startIndex!= lastLocation.length()){
            result = lastLocation.substring(startIndex+1);
        }
        return result;
    }


    //Activity Detection Utils

    /**
     * Returns a human readable String corresponding to a detected activity type.
     */
    static String getActivityString(Context context, int detectedActivityType) {
        Resources resources = context.getResources();
        switch(detectedActivityType) {
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.in_vehicle);
            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.on_bicycle);
            case DetectedActivity.ON_FOOT:
                return resources.getString(R.string.on_foot);
            case DetectedActivity.RUNNING:
                return resources.getString(R.string.running);
            case DetectedActivity.STILL:
                return resources.getString(R.string.still);
            case DetectedActivity.TILTING:
                return resources.getString(R.string.tilting);
            case DetectedActivity.UNKNOWN:
                return resources.getString(R.string.unknown);
            case DetectedActivity.WALKING:
                return resources.getString(R.string.walking);
            default:
                return resources.getString(R.string.unidentifiable_activity, detectedActivityType);
        }
    }






}
