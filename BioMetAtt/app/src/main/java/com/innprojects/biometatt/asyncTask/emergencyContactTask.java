package com.innprojects.biometatt.asyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.innprojects.biometatt.activity.StudentActivity;
import com.innprojects.biometatt.fragments.StudentFragment;
import com.innprojects.biometatt.javaModels.EmergencyContact;
import com.innprojects.biometatt.javaModels.Student;

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
 * Created by simransarin on 04/07/17.
 */

public class emergencyContactTask extends AsyncTask<String, Void, EmergencyContact[]> {

    private StudentActivity mActivity;

    public emergencyContactTask(StudentActivity studentActivity) {
        mActivity = studentActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private EmergencyContact[] parseJson(String json) {
        try {
//            JSONObject obj = new JSONObject(json);
//            JSONArray students = obj.getJSONArray("");
            JSONArray contacts = new JSONArray(json);
            EmergencyContact[] contact = new EmergencyContact[contacts.length()];
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject scontactsJSONObject = contacts.getJSONObject(i);
                String id = scontactsJSONObject.getString("id");
                String contact_name = scontactsJSONObject.getString("contact_name");
                String phone_number = scontactsJSONObject.getString("phone_number");
                String email_id = scontactsJSONObject.getString("email_id");
                contact[i] = new EmergencyContact(id, contact_name, phone_number, email_id);
            }
            return contact;
        } catch (JSONException e) {
            Toast.makeText(mActivity, "Network error", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected EmergencyContact[] doInBackground(String... params) {
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
    protected void onPostExecute(EmergencyContact[] contacts) {
        if (contacts != null) {
            mActivity.processResultsemergency(contacts);
        } else {
            Toast.makeText(mActivity, "Network error", Toast.LENGTH_SHORT).show();
        }
    }
}

