package com.example.jackson.step;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Seolha Lee on 2018-03-02.
 */

public class singleTrackedPoint {
    double lat, lon;
    float speed;
    String timeStamp;
    String ActivityType;
    int ActivityConfidence;
    long waitingTime;

    public singleTrackedPoint(double lat, double lon, float speed, String timeStamp, String ActivityType, int ActivityConfidence,
                               long waitingTime) {
        this.lat = lat;
        this.lon = lon;
        this.speed = speed;
        this.timeStamp = timeStamp;
        this.ActivityType = ActivityType;
        this.ActivityConfidence = ActivityConfidence;
        this.waitingTime = waitingTime;

    }

    public String toString (){

        return (" lat: "+ Double.toString(lat) +" | lon: "+ Double.toString(lon)+" | sped: "+ Float.toString(speed) +" | timestamp: "
                + timeStamp);
    }
    }

