package com.innprojects.biometappdriver.Asynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.innprojects.biometappdriver.Activity.AttendantActivity;
import com.innprojects.biometappdriver.models.Bus;

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
 * Created by simransarin on 06/06/17.
 */

 public class BusDownloadGETTask extends AsyncTask<String, Void, Bus> {
    private AttendantActivity mActivity;
    private ProgressDialog progressDialog;

    public BusDownloadGETTask(AttendantActivity activity) {
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("Fetching details...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        super.onPreExecute();
    }

    private Bus parseJson(String json) {
        Bus outputBus;
        try {
            JSONArray parentArray = new JSONArray(json);
            JSONObject finalObject = parentArray.getJSONObject(0);
            String driver_name = finalObject.getString("driver_name");
            String conductor_name = finalObject.getString("conductor_name");
            String number_plate = finalObject.getString("number_plate");
            String bus_no = finalObject.getString("bus_route_number");
            String teacher_incharge = finalObject.getString("teacher_incharge");
            String driver_name_number = finalObject.getString("driver_phone_number");
            String teacher_incharge_number = finalObject.getString("teacher_incharge_phone_number");
            outputBus = new Bus(driver_name, conductor_name, number_plate, bus_no, teacher_incharge, driver_name_number,teacher_incharge_number);
            Log.i("Log_BUSDownloadTask", outputBus.getDriver_name());
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
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        if (bus != null) {
            mActivity.processResults(bus);
        } else {
            Toast.makeText(mActivity, "Network error", Toast.LENGTH_SHORT).show();
        }
    }
}