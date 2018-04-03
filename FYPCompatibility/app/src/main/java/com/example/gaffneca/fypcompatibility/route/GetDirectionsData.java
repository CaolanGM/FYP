package com.example.gaffneca.fypcompatibility.route;

/**
 * Created by gaffneca on 2/17/2018.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;


import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class GetDirectionsData extends AsyncTask<Object,String,String> {

    String url;
    String googleDirectionsData;
    String duration, distance;
    Button bCon;
    @Override
    protected String doInBackground(Object... objects) {
        url = (String)objects[0];


        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleDirectionsData;


    }

    @Override
    protected void onPostExecute(String s) {



        String[] directionsList;
        DataParser parser = new DataParser();
        directionsList = parser.parseDirections(s);


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