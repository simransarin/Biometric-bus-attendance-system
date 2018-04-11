package com.innprojects.biometappdriver.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.innprojects.biometappdriver.Asynctask.LogindownloadPOSTtask;
import com.innprojects.biometappdriver.Asynctask.StudentsGettask;
import com.innprojects.biometappdriver.MySQLiteHelper;
import com.innprojects.biometappdriver.R;
import com.innprojects.biometappdriver.models.Student;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences mPreferences;
    EditText userNameEditText,passwordEditText;
    String userName,password;
    CheckBox check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (mPreferences.contains("username")) {
            // start Main activity

            Intent i = new Intent();
            i.setClass(LoginActivity.this, AttendantActivity.class);
            String userName = mPreferences.getString("username", null);
            i.putExtra("user_name", userName);
            startActivity(i);
        } else {
            final Button loginButton = (Button) findViewById(R.id.login_button);
            userNameEditText = (EditText) findViewById(R.id.editText);
            passwordEditText = (EditText) findViewById(R.id.editText2);
            check = (CheckBox) findViewById(R.id.checkbox);
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                 @Override
                                                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                     if (buttonView.isChecked()) {
                                                         passwordEditText.setTransformationMethod(null);
                                                     } else {
                                                         passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
                                                     }
                                                 }
                                             }
            );
            //java.util.Arrays.toString(userName.split("/"));


            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userName = userNameEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    new LogindownloadPOSTtask(LoginActivity.this, password, userName).execute("https://biometric-app.herokuapp.com/rest-auth/login/");
                }
            });
        }
    }

    public void processResultsLogin(String s) {
        if (s.equals("false : ")) {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        } else {

            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("username", userName);
            editor.commit();
            new StudentsGettask(LoginActivity.this).execute("https://biometric-app.herokuapp.com/scanapp/bus_route_number/" + userName + "/student_all/");
        }
    }

    public void processResults(Student[] students) {
        MySQLiteHelper db = new MySQLiteHelper(this);
        db.deleteStudentTable();
        db.close();
        if (students == null)
            return;
        for (Student s : students)
            db.addStudent(new Student(s.getStudent_adm_no(), s.getStudent_id(), s.getStudent_name(), s.getParent_name(), s.getClass_number(), s.getSection(), s.getPhone_number(), s.getStudent_bus_stop()));
//        List<Student> studentsgot = db.getAllStudents();
//        for (Student each : studentsgot) {
//            Log.e("students added in db", each.getStudent_adm_no() + each.getStudent_id()+each.getStudent_name()+each.getParent_name()+each.getClass_number()+each.getSection()+each.getPhone_number()+each.getStudent_bus_stop());
//        }
        Intent i = new Intent();
        i.setClass(LoginActivity.this, AttendantActivity.class);
        i.putExtra("user_name", userName);
        startActivity(i);
    }
}
