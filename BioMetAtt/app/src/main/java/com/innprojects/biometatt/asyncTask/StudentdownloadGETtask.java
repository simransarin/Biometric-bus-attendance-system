package com.innprojects.biometatt.asyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.innprojects.biometatt.activity.StudentActivity;
import com.innprojects.biometatt.fragments.StudentFragment;
import com.innprojects.biometatt.javaModels.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

import static com.innprojects.biometatt.R.drawable.student;

/**
 * Created by simransarin on 30/05/17.
 */

public class StudentdownloadGETtask extends AsyncTask<String, Void, Student> {

    private StudentFragment mFragment;
    private ProgressDialog progressDialog;

    public StudentdownloadGETtask(StudentFragment fragment) {
        mFragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(mFragment.getContext());
        progressDialog.setMessage("Fetching details...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        super.onPreExecute();
    }


    private Student parseJson(String json) {
        Student outputStudent;
        try {
            JSONArray parentArray = new JSONArray(json);
            JSONObject finalObject = parentArray.getJSONObject(0);
            String student_name = finalObject.getString("student_name");
            String parent_name = finalObject.getString("parent_name");
            String class_number = finalObject.getString("class_number");
            String section = finalObject.getString("section");
            String phone_number = finalObject.getString("phone_number");
            String student_bus_stop = finalObject.getString("student_bus_stop");
            String student_id = finalObject.getString("student_biometric_id");
            String bus = finalObject.getString("bus");
            String email = finalObject.getString("email_id");
            outputStudent = new Student(student_id, student_name, parent_name, class_number, section, phone_number, student_bus_stop, bus, email);
            return outputStudent;
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    protected Student doInBackground(String... params) {
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
    protected void onPostExecute(Student student) {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        if (student != null) {
            mFragment.processResults(student);
        } else {
            Toast.makeText(mFragment.getContext(), "Network error", Toast.LENGTH_SHORT).show();
        }
    }
}

