/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Earthquake>> {

    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";

    private static final String TAG = "chienpm_log_tag";
    private static final int SETTING_REQUEST_CODE = 13498;

    ArrayAdapter mAdapter;
    ProgressDialog progressDialog;
    TextView mTvEmpty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching earthquake data ...");
        progressDialog.show();

        mTvEmpty = (TextView) findViewById(R.id.tvEmpty);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create textview to set empty state of listview
        earthquakeListView.setEmptyView(mTvEmpty);

        // Create a new {@link ArrayAdapter} of earthquakes: gắn cái datalist vào layout
        mAdapter = new EarthquakeAdapter(
                this, new ArrayList<Earthquake>());

        // Set the mAdapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);


        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake selectedItem = (Earthquake) mAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedItem.getUrl()));
                startActivity(intent);
            }
        });

        Log.d(TAG, "initLoader (activity create) called");
        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent settingIntent = new Intent(this, SettingsActivity.class);
            startActivityForResult(settingIntent, SETTING_REQUEST_CODE);
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SETTING_REQUEST_CODE){
            Log.d(TAG, "onActivityResult: back from setting");
//
//            finish();
//            startActivity(getIntent());

        }
    }

    private boolean isInternetConnectionIsGood() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork !=null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: called");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String minMaginitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String amountOfRecords = sharedPrefs.getString(
                getString(R.string.settings_amount_key),
                getString(R.string.settings_amount_default));


        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson")
                .appendQueryParameter("limit", amountOfRecords)
                .appendQueryParameter("minmag", minMaginitude)
                .appendQueryParameter("orderby", "time");

        Log.d(TAG, "URL: "+uriBuilder.toString());
        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> data) {
        Log.d(TAG, "onLoadFinished: called");

        //Check internet connection
        if(!isInternetConnectionIsGood()){
            mTvEmpty.setText("No internet connection.");
        }
        else {
            // Set text to listview empty state if no item fetched to listview
            mTvEmpty.setText("No earthquake found.");
        }
        //clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if(data != null && !data.isEmpty())
            mAdapter.addAll(data);

        progressDialog.hide();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Earthquake>> loader) {
        Log.d(TAG, "onLoaderReset: called");
//        mAdapter.addAll(new ArrayList<Earthquake>());
        mAdapter.clear();
    }

}
