package com.example.gaffneca.fypfirebase.route;

/**
 * Created by gaffneca on 2/17/2018.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.gaffneca.fypfirebase.HomeScreen;
import com.example.gaffneca.fypfirebase.MakeReview;
import com.example.gaffneca.fypfirebase.MapActivity2;
import com.example.gaffneca.fypfirebase.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class GetDirectionsData extends AsyncTask<Object,String,String> {

    GoogleMap mMap;
    String url;
    String googleDirectionsData;
    String duration, distance;
    LatLng latLng;
    Button bCon;
    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        latLng = (LatLng)objects[2];


        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
      //  Log.d(TAG, "BIG TEST HERE AGAIN:" +  googleDirectionsData);
        return googleDirectionsData;


    }

    @Override
    protected void onPostExecute(String s) {



        String[] directionsList;
        DataParser parser = new DataParser();
        directionsList = parser.parseDirections(s);
        displayDirection(directionsList);


    }

    public void displayDirection(String[] directionsList)
    {

        int count = directionsList.length;
        for(int i = 0;i<count;i++)
        {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.RED);
            options.width(10);
            options.addAll(PolyUtil.decode(directionsList[i]));

            mMap.addPolyline(options);
        }
    }

    public String[] getInfo(String url){


        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<String,String> directionsList2 = null;
        DataParser parser = new DataParser();
        directionsList2 = parser.parseDirections2(googleDirectionsData);
        duration = directionsList2.get("duration");
        distance = directionsList2.get("distance");
        String[] info = new String[2];
        info[0]= duration;
        info[1]= distance;

        return info;
    }





}