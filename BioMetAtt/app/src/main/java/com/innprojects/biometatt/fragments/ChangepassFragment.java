package com.innprojects.biometatt.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ConfigurationHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.innprojects.biometatt.R;
import com.innprojects.biometatt.activity.LoginActivity;
import com.innprojects.biometatt.asyncTask.ForgotPassGETtask;
import com.innprojects.biometatt.asyncTask.changePassGettask;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangepassFragment extends Fragment {

    SharedPreferences mPreferences;
    ChangepassFragment changepassFragment;

    Toast toast;

    String password1;
    String password2;
    String user_name;

    EditText pass1;
    EditText pass2;
    Button change;

    public ChangepassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_changepass, container, false);
        change = (Button) view.findViewById(R.id.changePassword);
        pass1 = (EditText) view.findViewById(R.id.pass1);
        pass2 = (EditText) view.findViewById(R.id.pass2);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        user_name = mPreferences.getString("username", null);
        Log.e("Preferences: ", user_name);

        change.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                password1 = pass1.getText().toString();
                password2 = pass2.getText().toString();
                new changePassGettask(ChangepassFragment.this).execute("https://biometric-app.herokuapp.com/reset-pass/" + user_name);
                toast.makeText(ChangepassFragment.this.getContext() ,"Password reset email sent",Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
