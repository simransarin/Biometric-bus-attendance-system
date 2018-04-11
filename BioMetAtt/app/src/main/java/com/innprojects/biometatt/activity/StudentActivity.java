package com.innprojects.biometatt.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.innprojects.biometatt.asyncTask.LogindownloadPOSTtask;
import com.innprojects.biometatt.asyncTask.LogoutPOSTtask;
import com.innprojects.biometatt.asyncTask.emergencyContactTask;
import com.innprojects.biometatt.fragments.BusFragment;
import com.innprojects.biometatt.R;
import com.innprojects.biometatt.fragments.ChangepassFragment;
import com.innprojects.biometatt.fragments.StudentFragment;
import com.innprojects.biometatt.fragments.SuggestionsFragment;
import com.innprojects.biometatt.javaModels.EmergencyContact;
import com.innprojects.biometatt.javaModels.Student;

import java.util.ArrayList;


public class StudentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    public String biometricID;
    public String studentname;
    public String classnumber;
    public String sectionnumber;
    public String parentname;
    public String busnumber;
    public String editbusstop;
    public String editphonenumber;
    public String phonenumber;
    public String editemail_id;
    String username;
    DrawerLayout drawer;
    View drawerView;
    View drawerContent;
    View mainContent;
    TextView userNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        setContentView(R.layout.activity_student);

        username = getIntent().getStringExtra("user_name");

        NavigationView mView = (NavigationView) findViewById(R.id.nav_view);
        View navHeaderView = mView.getHeaderView(0);
        userNameText = (TextView) navHeaderView.findViewById(R.id.navuserame);
        userNameText.setText(username);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Drawable gradient = getResources().getDrawable(R.drawable.actionbar_gradient);
        toolbar.setBackgroundDrawable(gradient);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(StudentActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawer, float slideOffset) {
                super.onDrawerSlide(drawer, slideOffset);

                drawerView = findViewById(R.id.nav_view);
                drawerContent = findViewById(R.id.layout_navigation);
                mainContent = findViewById(R.id.relative_layout_navigation);

                drawerContent.setX(-drawerView.getWidth() * (slideOffset));
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        StudentFragment studentFragment = new StudentFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.relative_layout_navigation,
                        studentFragment,
                        studentFragment.getTag())
                .commit();
    }

    public void onRestart() {
        super.onRestart();
        Intent intent = new Intent();
        intent.setClass(this, this.getClass());
        finish();
        this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.navmenuright) {
            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                drawer.closeDrawer(Gravity.RIGHT);
            } else {
                drawer.openDrawer(Gravity.RIGHT);
            }
        } else if (id == R.id.contactemergency)
        {
            new emergencyContactTask(StudentActivity.this).execute("https://biometric-app.herokuapp.com/emergency/emergency_contacts/");
        }
        return true;

    }

    public void processResultsemergency(EmergencyContact[] contacts) {
        if (contacts == null)
            return;
        for (EmergencyContact contact : contacts) {
            sendSMSMessage(studentname, username,contact.getPhone_number());
        }
    }
    protected void sendSMSMessage(String student_name, String id, String phone) {
        String complete_message = "An emergency alert has been sent by student: " + student_name + " id no:" + id;
        Log.e("yo", "yo");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

            try {
                Log.e("yo", "yo");

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("+918447369635", null, complete_message, null, null);
                Toast.makeText(getApplicationContext(), "SMS Sent!",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "SMS failed, please try again later!",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    public void openDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.clear();   // This will delete all your preferences, check how to delete just one
                        editor.commit();
                        new LogoutPOSTtask(StudentActivity.this).execute("https://biometric-app.herokuapp.com/rest-auth/logout/");
                        Intent i = new Intent();
                        i.setClass(StudentActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        alertD.getButton(alertD.BUTTON_NEGATIVE).setTextColor(Color.RED);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_studentdetails) {
            setTitle("Student Details");
            StudentFragment studentFragment = new StudentFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.anim_slide_in_from_left, R.anim.anim_slide_out_from_left)
                    .replace(R.id.relative_layout_navigation,
                            studentFragment,
                            studentFragment.getTag())
                    .commit();

        } else if (id == R.id.nav_busdetails) {
            setTitle("Bus Details");
            BusFragment busFragment = new BusFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.anim_slide_in_from_left, R.anim.anim_slide_out_from_left)
                    .replace(R.id.relative_layout_navigation,
                            busFragment,
                            busFragment.getTag())
                    .commit();

        } else if (id == R.id.nav_suggestions) {
            setTitle("Suggestions");
            SuggestionsFragment suggestionsFragment = new SuggestionsFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.anim_slide_in_from_left, R.anim.anim_slide_out_from_left)
                    .replace(R.id.relative_layout_navigation,
                            suggestionsFragment,
                            suggestionsFragment.getTag())
                    .commit();

        } else if (id == R.id.logout) {
            openDialog();
        } else if (id == R.id.payment) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.google.co.in"));
            startActivity(intent);
        } else if (id == R.id.changePass) {
            setTitle("Change Password");
            ChangepassFragment changepassFragment = new ChangepassFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.anim_slide_in_from_left, R.anim.anim_slide_out_from_left)
                    .replace(R.id.relative_layout_navigation,
                            changepassFragment,
                            changepassFragment.getTag())
                    .commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }
}
