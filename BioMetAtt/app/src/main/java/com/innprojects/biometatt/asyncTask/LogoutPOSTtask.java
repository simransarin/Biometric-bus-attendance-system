package com.innprojects.biometatt.asyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.innprojects.biometatt.activity.LoginActivity;
import com.innprojects.biometatt.activity.StudentActivity;

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

/**
 * Created by simransarin on 05/06/17.
 */

public class LogoutPOSTtask extends AsyncTask<String, Void, String> {

    StudentActivity mActivity;

    public LogoutPOSTtask(StudentActivity studentActivity){
        mActivity = studentActivity;
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... params) {

        try {
            URL url = new URL(params[0]); // here is your URL path
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.close();

            int responseCode=conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { //HTTP_OK : 201 http_201_created
                BufferedReader in=new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line="";
                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }
                in.close();
                return sb.toString();
            }
            else {
                Log.e("doInBackground: ", new String("false : ") ); // : 400 http_400_bad_request
                return new String("false : ");
            }
        } catch (Exception e) {
            Log.e("doInBackground: ", new String("Exception: " + e.getMessage()) );
            return new String("Exception: ");
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if (s.equals("Exception: ") || s.equals("false : "))
            Toast.makeText(mActivity, "Network error", Toast.LENGTH_SHORT).show();
        else
            super.onPostExecute(s);
    }
}
