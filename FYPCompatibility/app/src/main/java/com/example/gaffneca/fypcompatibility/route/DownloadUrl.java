package com.example.gaffneca.fypcompatibility.route;

/**
 * Created by gaffneca on 2/17/2018.
 */

import android.os.NetworkOnMainThreadException;
import android.renderscript.ScriptGroup;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import static android.content.ContentValues.TAG;


public class DownloadUrl {

    public String readUrl(String myUrl) throws IOException
    {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(myUrl);
           // Log.d(TAG, "CHECK OUT THIS URL:  "+myUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();
           // urlConnection.getInputStream();
//            urlConnection.getResponseCode();
         //   urlConnection.getResponseMessage();

            inputStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while((line = br.readLine()) != null)
            {
                sb.append(line);

            }

            data = sb.toString();
            //Log.d("downloadUrl", data.toString());

            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(TAG, "CAOLAN MalformedURLException");

        } catch (UnknownHostException e){
            e.printStackTrace();
            Log.d(TAG, "CAOLAN UnknownHostException");


        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "CAOLAN IOException");

        }catch (NetworkOnMainThreadException ex){
            Log.d(TAG, "CAOLAN Exception 343");
            Log.d(TAG, "CAOLAN error stream:  "+ ex);
            Log.d(TAG, "CAOLAN Exception");



        }


        finally {
            if(inputStream != null)
                inputStream.close();
            urlConnection.disconnect();
        }

        return data;

    }
}