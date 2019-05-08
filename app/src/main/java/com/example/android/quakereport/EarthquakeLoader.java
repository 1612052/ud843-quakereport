package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Earthquake>> {

    private static final String TAG = "chienpm_log_tag";
    Context mContext;
    String mStringUrl;

    SharedPreferences.OnSharedPreferenceChangeListener mPrefsListener = null;

    public EarthquakeLoader(Context context, String stringUrl) {
        super(context);
        mContext = context;
        mStringUrl = stringUrl;
        Log.d(TAG, "EarthquakeLoader: contructor called");
    }



    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading: called");

        if(mPrefsListener == null){
            mPrefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    onContentChanged();
                    Log.d(TAG, "onContent changed called ");
                }
            };

        }

//        if(takeConte?ntChanged()){
            forceLoad();
//        }


    }

    @Override
    public ArrayList<Earthquake> loadInBackground() {
        Log.d(TAG, "loadInBackground: called");
        if(TextUtils.isEmpty(mStringUrl))
            return null;

        //Parce json to list of earthquake object
        ArrayList<Earthquake> earthquakes = null;
        try {
            earthquakes = QueryUtils.extractEarthquakes(mStringUrl);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Fetching earthquake data failed!", Toast.LENGTH_LONG);
        }

        return earthquakes;
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

}
