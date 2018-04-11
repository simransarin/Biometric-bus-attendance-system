package com.innprojects.biometatt.asyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.innprojects.biometatt.activity.LoginActivity;
import com.innprojects.biometatt.fragments.BusFragment;
import com.innprojects.biometatt.fragments.ChangepassFragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by simransarin on 07/06/17.
 */

public class changePassGettask extends AsyncTask<String, Void, String> {

    private ChangepassFragment mFragment;
    private ProgressDialog progressDialog;

    public changePassGettask(ChangepassFragment changepassFragment) {
        mFragment = changepassFragment;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(mFragment.getContext());
        progressDialog.setMessage("Sending mail...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
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
        return null;
    }

    @Override
    protected void onPostExecute(String string) {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        super.onPostExecute(string);
    }
}

