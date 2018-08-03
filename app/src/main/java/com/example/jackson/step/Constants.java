package com.example.jackson.step;

import com.google.android.gms.location.DetectedActivity;

public final class Constants {

    public Constants() {}

    public static final String PACKAGE_NAME =
            "com.google.android.gms.location.activityrecognition";

    static final String KEY_ACTIVITY_UPDATES_REQUESTED = PACKAGE_NAME +
            ".ACTIVITY_UPDATES_REQUESTED";

    public static final String KEY_DETECTED_ACTIVITY_TYPE = PACKAGE_NAME + ".DETECTED_ACTIVITY_TYPE";

    static final String KEY_DETECTED_ACTIVITY_CONFIDENCE = PACKAGE_NAME + ".DETECTED_ACTIVITY_CONFIDENCE";

    /**
     * The desired time between activity detections. Larger values result in fewer activity
     * detections while improving battery life. A value of 0 results in activity detections at the
     * fastest possible rate.
     */
    static final long DETECTION_INTERVAL_IN_MILLISECONDS = 5 * 1000; // 10 seconds
    /**
     * List of DetectedActivity types that we monitor in this sample.
     */
    static final int[] MONITORED_ACTIVITIES = {
            DetectedActivity.STILL,
            DetectedActivity.ON_FOOT,
            DetectedActivity.WALKING,
            DetectedActivity.RUNNING,
            DetectedActivity.ON_BICYCLE,
            DetectedActivity.IN_VEHICLE,
            DetectedActivity.TILTING,
            DetectedActivity.UNKNOWN
    };
}
