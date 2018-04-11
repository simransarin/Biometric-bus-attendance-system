package com.innprojects.biometatt.asyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.innprojects.biometatt.activity.MapsActivity;
import com.innprojects.biometatt.javaModels.Bus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by simransarin on 12/06/17.
 */

public class LocationGETtask extends AsyncTask<String, Void, Bus> {
    private MapsActivity mapsActivity;
    private ProgressDialog progressDialog;

    public LocationGETtask(MapsActivity mapsActivity1) {
        mapsActivity = mapsActivity1;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private Bus parseJson(String json) {
        Bus outputBus;
        try {
            JSONArray parentArray = new JSONArray(json);
            JSONObject finalObject = parentArray.getJSONObject(0);
            String longitude = finalObject.getString("longitude");
            String latitude = finalObject.getString("latitude");
            outputBus = new Bus(longitude, latitude);
//            Log.i("Log_BUSDownloadTask", outputBus.getDriver_name());
            return outputBus;
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    protected Bus doInBackground(String... params) {
        if (params.length == 0)
            return null;
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(15000);
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return null;
            }
            Scanner s = new Scanner(inputStream);
            while (s.hasNext()) {
                buffer.append(s.nextLine());
            }
            Log.i("jsondata", buffer.toString());
        } catch (MalformedURLException e) {
            return null;
        } catch (ProtocolException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return parseJson(buffer.toString());
    }

    @Override
    protected void onPostExecute(Bus bus) {
        if (bus != null) {
            mapsActivity.processResults(bus);
        }
//        else {
//            Toast.makeText(mapsActivity, "Network error", Toast.LENGTH_SHORT).show();
//        }
    }
}