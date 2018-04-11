package com.innprojects.biometatt.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

/**
 * Created by simransarin on 03/06/17.
 */

public class SendMailTask extends AsyncTask {

    private ProgressDialog statusDialog;
    private SuggestionsFragment sendMailActivity;

    public SendMailTask(SuggestionsFragment fragment) {
        sendMailActivity = fragment;

    }

    protected void onPreExecute() {
        statusDialog = new ProgressDialog(sendMailActivity.getContext());
        statusDialog.setMessage("Getting ready...");
        statusDialog.setIndeterminate(false);
        statusDialog.setCancelable(false);
        statusDialog.show();
    }

    @Override
    protected Object doInBackground(Object... args) {
        try {
            Log.i("SendMailTask", "About to instantiate GMail...");
            publishProgress("Processing input....");
            GMail androidEmail = new GMail(args[0].toString(),
                    args[1].toString(), (List) args[2], args[3].toString(),
                    args[4].toString());
            publishProgress("Preparing suggestion....");
            androidEmail.createEmailMessage();
            publishProgress("Storing suggestion....");
            androidEmail.sendEmail();
            publishProgress("Suggestion safe.");
            Log.i("SendMailTask", "Mail Sent.");
        } catch (Exception e) {
            publishProgress(e.getMessage());
            Log.e("SendMailTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void onProgressUpdate(Object... values) {
        statusDialog.setMessage(values[0].toString());

    }

    @Override
    public void onPostExecute(Object result) {
        statusDialog.dismiss();
    }

}
