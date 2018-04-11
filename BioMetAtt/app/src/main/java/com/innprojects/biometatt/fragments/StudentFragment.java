package com.innprojects.biometatt.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.innprojects.biometatt.R;
import com.innprojects.biometatt.activity.StudentActivity;
import com.innprojects.biometatt.asyncTask.StudentdownloadGETtask;
import com.innprojects.biometatt.asyncTask.StudentdownloadPUTtask;
import com.innprojects.biometatt.javaModels.Student;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentFragment extends Fragment {

    Toast toast;

    SharedPreferences mPreferences;
    String user_name;
    String editphoneno;
    String editstopvalue;
    String editemailvalue;

    Context context;

    ViewSwitcher phoneSwitcher;
    ViewSwitcher stopSwitcher;
    ViewSwitcher emailSwitcher;
    Button save;
    ImageButton phone_edit;
    ImageButton stop_edit;
    ImageButton email_edit;

    TextView name;
    TextView class_no;
    TextView section_no;
    TextView Parent_name;
    TextView bus_stop;
    TextView phone_no;
    TextView emailID;

    EditText editPhone;
    EditText editStop;
    EditText editEmail;

    public StudentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student, container, false);
        phoneSwitcher = (ViewSwitcher) view.findViewById(R.id.phone_switcher);
        stopSwitcher = (ViewSwitcher) view.findViewById(R.id.stop_switcher);
        emailSwitcher = (ViewSwitcher) view.findViewById(R.id.email_switcher);
        save = (Button) view.findViewById(R.id.save);
        phone_edit = (ImageButton) view.findViewById(R.id.phone_edit_button);
        stop_edit = (ImageButton) view.findViewById(R.id.stop_edit_button);
        email_edit = (ImageButton) view.findViewById(R.id.email_edit_button);
        editStop = (EditText) view.findViewById(R.id.stop_edit);
        editPhone = (EditText) view.findViewById(R.id.phone_edit);
        editEmail = (EditText) view.findViewById(R.id.email_edit);
        context = getActivity().getApplicationContext();

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        user_name = mPreferences.getString("username", null);
        Log.e("Preferences: ", user_name);

        phone_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show Previous View
                if (phoneSwitcher.getCurrentView() != editPhone)
                    phoneSwitcher.showPrevious();

            }
        });

        stop_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show Previous View
                if (stopSwitcher.getCurrentView() != editStop)
                    stopSwitcher.showPrevious();

            }
        });

        email_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show Previous View
                if (emailSwitcher.getCurrentView() != editEmail)
                    emailSwitcher.showPrevious();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                editphoneno = editPhone.getText().toString();
                editstopvalue = editStop.getText().toString();
                editemailvalue = editEmail.getText().toString();
                ((StudentActivity) getActivity()).editphonenumber = editphoneno;
                ((StudentActivity) getActivity()).editbusstop = editstopvalue;
                ((StudentActivity) getActivity()).editemail_id = editemailvalue;
                if (stopSwitcher.getCurrentView() != bus_stop)
                    stopSwitcher.showNext();
                if (phoneSwitcher.getCurrentView() != phone_no)
                    phoneSwitcher.showNext();
                if (emailSwitcher.getCurrentView() != emailID)
                    emailSwitcher.showNext();
                new StudentdownloadPUTtask(StudentFragment.this).execute("https://biometric-app.herokuapp.com/scanapp/parent_student_admission_number/" + user_name);
                new StudentdownloadGETtask(StudentFragment.this).execute("https://biometric-app.herokuapp.com/scanapp/parent_student_admission_number/" + user_name);
            }
        });

        name = (TextView) view.findViewById(R.id.name);
        class_no = (TextView) view.findViewById(R.id.class_no);
        section_no = (TextView) view.findViewById(R.id.section);
        Parent_name = (TextView) view.findViewById(R.id.parent_name);
        bus_stop = (TextView) view.findViewById(R.id.bus_stop);
        phone_no = (TextView) view.findViewById(R.id.phone);
        emailID = (TextView) view.findViewById(R.id.email);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(isConnected(context))
        new StudentdownloadGETtask(this).execute("https://biometric-app.herokuapp.com/scanapp/parent_student_admission_number/" + user_name);
        else
            toast.makeText(context, "No internet", Toast.LENGTH_SHORT).show();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void processResults(Student student) {

        name.setText(student.getStudent_name());
        class_no.setText(student.getClass_number());
        section_no.setText(student.getSection());
        Parent_name.setText(student.getParent_name());
        bus_stop.setText(student.getStudent_bus_stop());
        phone_no.setText(student.getPhone_number());
        emailID.setText(student.getEmailID());

        editStop.setText(student.getStudent_bus_stop());
        editPhone.setText(student.getPhone_number());
        editEmail.setText(student.getEmailID());

        ((StudentActivity) getActivity()).busnumber = student.getBus();
        ((StudentActivity) getActivity()).studentname = student.getStudent_name();
        ((StudentActivity) getActivity()).phonenumber = student.getPhone_number();
        ((StudentActivity) getActivity()).classnumber = student.getClass_number();
        ((StudentActivity) getActivity()).sectionnumber = student.getSection();
        ((StudentActivity) getActivity()).parentname = student.getParent_name();
        ((StudentActivity) getActivity()).biometricID = student.getStudent_id();
    }
}
