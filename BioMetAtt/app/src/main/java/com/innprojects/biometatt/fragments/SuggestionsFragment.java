package com.innprojects.biometatt.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.innprojects.biometatt.R;
import com.innprojects.biometatt.activity.StudentActivity;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestionsFragment extends Fragment {

    EditText suggestions;
    ImageButton suggButton;

    SuggestionsFragment mFragment;
    StudentActivity studentActivity;

    SharedPreferences mPreferences;
    String user_name;
    String student_name;

    public SuggestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggestions, container, false);
        suggButton = (ImageButton) view.findViewById(R.id.suggestion_button);
        suggestions = (EditText) view.findViewById(R.id.suggestions_frag);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        user_name = mPreferences.getString("username", null);
        Log.e("Preferences: ", user_name);

        student_name = ((StudentActivity) getActivity()).studentname;

        suggButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                String fromEmail = ("simran.sarin.03.ss@gmail.com");
                String fromPassword = ("dhinglakalaka");
                String toEmails = ("simran.sarin.03.ss@gmail.com");
                List<String> toEmailList = Arrays.asList(toEmails
                        .split("\\s*,\\s*"));
                Log.i("SendMailActivity", "To List: " + toEmailList);
                String emailSubject = ("suggestions by:" + user_name + " ~Student's name: " + student_name);
                String emailBody = (suggestions.getText().toString());
                new SendMailTask(SuggestionsFragment.this).execute(fromEmail,
                        fromPassword, toEmailList, emailSubject, emailBody);
            }
        });
        return view;
    }
}
