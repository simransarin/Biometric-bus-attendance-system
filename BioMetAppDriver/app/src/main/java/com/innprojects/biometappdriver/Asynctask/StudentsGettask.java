package com.innprojects.biometappdriver.Asynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.innprojects.biometappdriver.Activity.LoginActivity;
import com.innprojects.biometappdriver.Activity.StudentActivity;
import com.innprojects.biometappdriver.models.Student;

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
 * Created by simransarin on 24/06/17.
 */

public class StudentsGettask extends AsyncTask<String, Void, Student[]> {

    private LoginActivity mActivity;
    private ProgressDialog progressDialog;

    public StudentsGettask(LoginActivity loginActivity) {
        mActivity = loginActivity;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("Setting up...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        super.onPreExecute();
    }

    private Student[] parseJson(String json) {
        try {
//            JSONObject obj = new JSONObject(json);
//            JSONArray students = obj.getJSONArray("");
            JSONArray students = new JSONArray(json);
            Student[] output = new Student[students.length()];
            for (int i = 0; i < students.length(); i++) {
                JSONObject studentsJSONObject = students.getJSONObject(i);
                String student_id = studentsJSONObject.getString("student_biometric_id");
                String student_adm_no = studentsJSONObject.getString("admission_number");
                String student_name = studentsJSONObject.getString("student_name");
                String parent_name = studentsJSONObject.getString("parent_name");
                String class_number = studentsJSONObject.getString("class_number");
                String section = studentsJSONObject.getString("section");
                String phone_number = studentsJSONObject.getString("phone_number");
                String student_bus_stop = studentsJSONObject.getString("student_bus_stop");
                output[i] = new Student(student_adm_no, student_id, student_name, parent_name, class_number, section, phone_number, student_bus_stop);
            }
            return output;
        } catch (JSONException e) {
            Toast.makeText(mActivity, "Network error", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected Student[] doInBackground(String... params) {
        if (params.length == 0)
            return null;
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(params[0]);
            HttpURLConnection urlConnection =
                    (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream =
                    urlConnection.getInputStream();
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
    protected void onPostExecute(Student[] students) {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        if (students != null) {
            mActivity.processResults(students);
        } else {
            Toast.makeText(mActivity, "Network error", Toast.LENGTH_SHORT).show();
        }
    }
}

