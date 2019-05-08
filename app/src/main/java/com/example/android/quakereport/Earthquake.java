package com.example.android.quakereport;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

class Earthquake {
    double mMagnitude;
    String mLocation;
    long mTimestamp;
    String mUrl;

    public String getUrl() {
        return mUrl;
    }

    static SimpleDateFormat mFormatter1 = new SimpleDateFormat("MMM dd, yyyy");
    static SimpleDateFormat mFormatter2 = new SimpleDateFormat("hh:mm a");

    public Earthquake(String location, double magnitude, long timestamp, String url) {
        mLocation = location;
        mMagnitude = magnitude;
        mTimestamp = timestamp;
        mUrl = url;
    }

    public String getDateString() {
        Date date = new Date(mTimestamp);
        return Earthquake.mFormatter1.format(date);
    }


    public String getTimeString(){
        Date date = new Date(mTimestamp);
        return Earthquake.mFormatter2.format(date);
    }


    public String getLocationOffset() {

        String strPlaceOffset;

        int place_split =mLocation.indexOf("of", 0);

        if(place_split!=-1) {
            place_split += 2;

            strPlaceOffset = mLocation.substring(0, place_split);

        }
        else{
            strPlaceOffset = "";
        }


        return strPlaceOffset;
    }

    public String getLocationPlace() {
        String strPlace ;

        int place_split =mLocation.indexOf("of", 0);

        if(place_split!=-1) {
            place_split += 2;

            strPlace = mLocation.substring(place_split, mLocation.length());

        }
        else{
            strPlace = mLocation;
        }


        return strPlace;
    }

    public double getMagnitude() {
        return mMagnitude;
    }
}
