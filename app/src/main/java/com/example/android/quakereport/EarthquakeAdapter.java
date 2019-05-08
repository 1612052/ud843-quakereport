package com.example.android.quakereport;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Currency;

class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    private static final String TAG = "chienpm_adapter";

    ArrayList<Earthquake> mEarthquakes;
    public EarthquakeAdapter(EarthquakeActivity earthquakeActivity, ArrayList<Earthquake> earthquakes) {
//        mEarthquakes = earthquakes;
        super(earthquakeActivity, 0, earthquakes);
        mEarthquakes = earthquakes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View listViewItem = convertView;
        if(listViewItem==null)
        {
            listViewItem = LayoutInflater.from(getContext())
                    .inflate(R.layout.earthquake_list_item, parent, false);
        }

        //Get the EarthquakeItem from position in list view
        Earthquake currItem = getItem(position);


        ((TextView)listViewItem.findViewById(R.id.tv_location_offset)).setText(currItem.getLocationOffset());
        ((TextView)listViewItem.findViewById(R.id.tv_location_place)).setText(currItem.getLocationPlace());


        ((TextView)listViewItem.findViewById(R.id.tv_date)).setText(currItem.getDateString());
        ((TextView)listViewItem.findViewById(R.id.tv_time)).setText(currItem.getTimeString());


        TextView magnitudeView = (TextView) listViewItem.findViewById(R.id.tvMagnitude);
        magnitudeView.setText(String.valueOf(currItem.getMagnitude()));

        //draw ,magnitude circle
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        int magnitudeColor = getMagnitudeColorByLevel(currItem.getMagnitude());

        //set color apdapt with magnitude level
        magnitudeCircle.setColor(magnitudeColor);

        return listViewItem;

    }

    private int getMagnitudeColorByLevel(double magnitude) {
        int colorId;

        switch ((int)Math.floor(magnitude)){
            case 0: colorId = R.color.magnitude1; break;
            case 1: colorId = R.color.magnitude1; break;
            case 2: colorId = R.color.magnitude2; break;
            case 3: colorId = R.color.magnitude3; break;
            case 4: colorId = R.color.magnitude4; break;
            case 5: colorId = R.color.magnitude5; break;
            case 6: colorId = R.color.magnitude6; break;
            case 7: colorId = R.color.magnitude7; break;
            case 8: colorId = R.color.magnitude8; break;
            case 9: colorId = R.color.magnitude9; break;
            default: colorId = R.color.magnitude10plus;
        }
        return ContextCompat.getColor(getContext(), colorId);
    }


    @Nullable
    @Override
    public Earthquake getItem(int position) {
        if(mEarthquakes.isEmpty())
            return  null;
        return mEarthquakes.get(position);
    }
}
