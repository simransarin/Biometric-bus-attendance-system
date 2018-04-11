package com.innprojects.biometatt.asyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.innprojects.biometatt.activity.StudentActivity;
import com.innprojects.biometatt.fragments.StudentFragment;
import com.innprojects.biometatt.javaModels.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by simransarin on 02/06/17.
 */

public class StudentdownloadPUTtask extends AsyncTask<String, Void, String> {

    private SharedPreferences mPreferences;
    private String biometricIDput;
    private String user_name;
    private String nameput;
    private String classput;
    private String sectionput;
    private String busput;
    private String parentput;
    private String busstopeditput;
    private String numbereditput;
    private String email_id;
    private StudentFragment mFragment;
    private ProgressDialog progressDialog;

    public StudentdownloadPUTtask(StudentFragment fragment) {
        mFragment = fragment;
    }

    protected void onPreExecute() {
        progressDialog = new ProgressDialog(mFragment.getContext());
        progressDialog.setMessage("posting details...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mPreferences = PreferenceManager.getDefaultSharedPreferences(mFragment.getActivity().getApplicationContext());
        user_name = mPreferences.getString("username", null);
        Log.e("Preferences: ", user_name);

        nameput = ((StudentActivity) mFragment.getActivity()).studentname;
        classput = ((StudentActivity) mFragment.getActivity()).classnumber;
        sectionput = ((StudentActivity) mFragment.getActivity()).sectionnumber;
        busput = ((StudentActivity) mFragment.getActivity()).busnumber;
        parentput = ((StudentActivity) mFragment.getActivity()).parentname;
        busstopeditput = ((StudentActivity) mFragment.getActivity()).editbusstop;
        numbereditput = ((StudentActivity) mFragment.getActivity()).editphonenumber;
        biometricIDput = ((StudentActivity) mFragment.getActivity()).biometricID;
        email_id = ((StudentActivity) mFragment.getActivity()).editemail_id;

        super.onPreExecute();
    }

    protected String doInBackground(String... params) {

        try {
            URL url = new URL(params[0]); // here is your URL path
//            JSONArray parentArray = new JSONArray();
//            JSONObject postDataParams = parentArray.getJSONObject(0);

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("admission_number", Integer.parseInt(user_name));
            postDataParams.put("student_name", nameput);
            postDataParams.put("parent_name", parentput);
            postDataParams.put("class_number", Integer.parseInt(classput));
            postDataParams.put("section", sectionput);
            postDataParams.put("phone_number", numbereditput);
            postDataParams.put("student_bus_stop", busstopeditput);
            postDataParams.put("student_biometric_id", Integer.parseInt(biometricIDput));
            postDataParams.put("bus", Integer.parseInt(busput));
            postDataParams.put("email_id", email_id);

            Log.i("params", postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { //HTTP_OK : 201 http_201_created
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                while ((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }
                in.close();
                return sb.toString();
            } else {
                Log.e("doInBackground: ", new String("false : " + responseCode)); // : 400 http_400_bad_request
                return new String("false : ");
            }
        } catch (Exception e) {
            Log.e("doInBackground: ", new String("Exception: " + e.getMessage()));
            return new String("Exception: ");
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        if (s.equals("Exception: ") || s.equals("false : "))
            Toast.makeText(mFragment.getContext(), "Network error", Toast.LENGTH_SHORT).show();
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
