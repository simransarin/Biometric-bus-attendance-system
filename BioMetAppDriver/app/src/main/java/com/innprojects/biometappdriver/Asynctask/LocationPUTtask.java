package com.innprojects.biometappdriver.Asynctask;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.innprojects.biometappdriver.Activity.AttendantActivity;
import com.innprojects.biometappdriver.Activity.StudentActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;

/**
 * Created by simransarin on 11/06/17.
 */

public class LocationPUTtask extends AsyncTask<String, Void, String> {

    private StudentActivity mActivity;
    private ProgressDialog progressDialog;

    private SharedPreferences mPreferences;
    private String user_name;

    private String drivername,conductorname,numberplate,teachername,teacherphonenumber,driverphonenumber;

    private String latitude,longitude;

    public LocationPUTtask(StudentActivity activity, String driver, String conductor, String number, String teacher, String lati, String longi, String teachernumber, String drivernumber){
        mActivity = activity;
        drivername = driver;
        conductorname = conductor;
        numberplate = number;
        teachername = teacher;
        latitude = lati;
        longitude = longi;
        teacherphonenumber = teachernumber;
        driverphonenumber = drivernumber;
    }

    protected void onPreExecute() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity.getApplicationContext());
        user_name = mPreferences.getString("username", null);
        Log.e("Preferences: ", user_name);
        super.onPreExecute();
    }

    protected String doInBackground(String... params) {

        try {
            URL url = new URL(params[0]); // here is your URL path
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("driver_name", drivername);
            postDataParams.put("conductor_name", conductorname);
            postDataParams.put("number_plate", numberplate);
            postDataParams.put("bus_route_number", user_name);
            postDataParams.put("teacher_incharge",teachername );
            postDataParams.put("latitude",latitude );
            postDataParams.put("longitude",longitude );
            postDataParams.put("driver_phone_number", driverphonenumber);
            postDataParams.put("teacher_incharge_phone_number",teacherphonenumber );
            Log.i("params", postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { //HTTP_OK : 201 http_201_created
                BufferedReader in=new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";
                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }
                in.close();
                return sb.toString();
            }
            else {
                Log.e("doInBackground: ", new String("false : "+responseCode) ); // : 400 http_400_bad_request
                return new String("false : ");
            }
        } catch (Exception e) {
            Log.e("doInBackground: ", new String("Exception: " + e.getMessage()) );
            return new String("Exception: ");
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if (s.equals("Exception: ") || s.equals("false : ") || s.equals(null))
            Toast.makeText(mActivity, "Network error", Toast.LENGTH_SHORT).show();
        else
            super.onPostExecute(s);
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
