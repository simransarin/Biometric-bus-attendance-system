package com.innprojects.biometatt.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.innprojects.biometatt.R;
import com.innprojects.biometatt.asyncTask.ForgotPassGETtask;
import com.innprojects.biometatt.asyncTask.LogindownloadPOSTtask;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences mPreferences;
    TextView forgotpass;
    EditText userNameEditText;
    EditText passwordEditText;
    String userName;
    String password;
    CheckBox check;

    String m_Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        setContentView(R.layout.activity_login);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (mPreferences.contains("username")) {
            // start Main activity

            Intent i = new Intent();
            i.setClass(LoginActivity.this, StudentActivity.class);
            String userName = mPreferences.getString("username", null);
            i.putExtra("user_name", userName);
            startActivity(i);
        } else {
            // ask him to enter his credentials

            final Button loginButton = (Button) findViewById(R.id.login_button);
            userNameEditText = (EditText) findViewById(R.id.editText);
            passwordEditText = (EditText) findViewById(R.id.editText2);
            forgotpass = (TextView) findViewById(R.id.forgotpass);
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

            forgotpass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
                    builder.setTitle("Forgot password");

//                  Set up the input
                    final EditText input = new EditText(LoginActivity.this);
//                  Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setHint("Enter username");
                    builder.setView(input);
                    builder.setCancelable(false);

//                  Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            m_Text = input.getText().toString();
                            new ForgotPassGETtask(LoginActivity.this).execute("https://biometric-app.herokuapp.com/reset-pass/" + m_Text);
                            Toast.makeText(LoginActivity.this,"If username valid, Password reset email sent",Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userName = userNameEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    if(isConnected(LoginActivity.this))
                    new LogindownloadPOSTtask(LoginActivity.this, password, userName).execute("https://biometric-app.herokuapp.com/rest-auth/login/");
                    else
                        Toast.makeText(LoginActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void processResultsLogin(String s) {
        if (s.equals("false : ")) {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("username", userName);
            editor.commit();
            Intent i = new Intent();
            i.setClass(LoginActivity.this, StudentActivity.class);
            i.putExtra("user_name", userName);
            startActivity(i);
        }
    }
}
