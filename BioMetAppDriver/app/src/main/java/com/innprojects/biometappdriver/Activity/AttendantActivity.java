package com.innprojects.biometappdriver.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.innprojects.biometappdriver.Asynctask.emergencyContactTask;
import com.innprojects.biometappdriver.MySQLiteHelper;
import com.innprojects.biometappdriver.RCVAdapter;
import com.innprojects.biometappdriver.RCVAdapterEmergency;
import com.innprojects.biometappdriver.models.Bus;
import com.innprojects.biometappdriver.Asynctask.BusDownloadGETTask;
import com.innprojects.biometappdriver.Asynctask.BusdownloadPUTtask;
import com.innprojects.biometappdriver.Asynctask.LogoutPOSTtask;
import com.innprojects.biometappdriver.R;
import com.innprojects.biometappdriver.models.EmergencyContact;
import com.innprojects.biometappdriver.models.Student;

import java.util.ArrayList;

import static com.innprojects.biometappdriver.MySQLiteHelper.DATABASE_NAME;
import static com.innprojects.biometappdriver.MySQLiteHelper.STUDENTS;

public class AttendantActivity extends AppCompatActivity implements RCVAdapterEmergency.EmergencyContactListListener {

    String drivername, conductorname, numberplate, teacherincharge, drivernumbervalue, teacherinchargeNumber;

    SharedPreferences mPreferences;
    String user_name, editregnovalue, editdrivervalue, editconductorvalue, editteachervalue;

    TextView driver_name, conductor_name, number_plate, bus_no, teacher_incharge;

    ViewSwitcher regnoSwitcher, driverSwitcher, conductorSwitcher, teacherSwitcher;
    Button save, startAttendance;
    ImageButton regno_edit, driver_edit, conductor_edit, teacher_edit;
    EditText editregno, editdriver, editconductor, editteacher;

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        setContentView(R.layout.activity_attendant);

        Drawable gradient = getResources().getDrawable(R.drawable.actionbar_gradient);
        getSupportActionBar().setBackgroundDrawable(gradient);

        driver_name = (TextView) findViewById(R.id.driver_name);
        conductor_name = (TextView) findViewById(R.id.conductor_name);
        number_plate = (TextView) findViewById(R.id.reg_no);
        bus_no = (TextView) findViewById(R.id.bus_no);
        teacher_incharge = (TextView) findViewById(R.id.teacher_name);

        regnoSwitcher = (ViewSwitcher) findViewById(R.id.regno_switcher);
        driverSwitcher = (ViewSwitcher) findViewById(R.id.driver_switcher);
        conductorSwitcher = (ViewSwitcher) findViewById(R.id.conductor_switcher);
        teacherSwitcher = (ViewSwitcher) findViewById(R.id.teacher_switcher);

        save = (Button) findViewById(R.id.save);
        startAttendance = (Button) findViewById(R.id.startattendance);

        regno_edit = (ImageButton) findViewById(R.id.regno_edit_button);
        driver_edit = (ImageButton) findViewById(R.id.driver_edit_button);
        conductor_edit = (ImageButton) findViewById(R.id.conductorr_edit_button);
        teacher_edit = (ImageButton) findViewById(R.id.teacher_edit_button);

        editregno = (EditText) findViewById(R.id.regno_edit);
        editdriver = (EditText) findViewById(R.id.driver_edit);
        editconductor = (EditText) findViewById(R.id.conductor_edit);
        editteacher = (EditText) findViewById(R.id.teacher_edit);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_name = mPreferences.getString("username", null);
        Log.e("Preferences: ", user_name);

        regno_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show Previous View
                if (regnoSwitcher.getCurrentView() != editregno)
                    regnoSwitcher.showPrevious();

            }
        });

        driver_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show Previous View
                if (driverSwitcher.getCurrentView() != editdriver)
                    driverSwitcher.showPrevious();

            }
        });

        conductor_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show Previous View
                if (conductorSwitcher.getCurrentView() != editconductor)
                    conductorSwitcher.showPrevious();

            }
        });

        teacher_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show Previous View
                if (teacherSwitcher.getCurrentView() != editteacher)
                    teacherSwitcher.showPrevious();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                editregnovalue = editregno.getText().toString();
                editdrivervalue = editdriver.getText().toString();
                editconductorvalue = editconductor.getText().toString();
                editteachervalue = editteacher.getText().toString();
                if (isConnected(AttendantActivity.this)) {
                    new BusdownloadPUTtask(AttendantActivity.this, editdrivervalue, editconductorvalue, editregnovalue, editteachervalue, teacherinchargeNumber, drivernumbervalue)
                            .execute("https://biometric-app.herokuapp.com/scanapp/bus_route_number/" + user_name);
                    new BusDownloadGETTask(AttendantActivity.this).execute("https://biometric-app.herokuapp.com/scanapp/bus_route_number/" + user_name);
                } else
                    Toast.makeText(AttendantActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                if (regnoSwitcher.getCurrentView() != number_plate)
                    regnoSwitcher.showNext();
                if (driverSwitcher.getCurrentView() != driver_name)
                    driverSwitcher.showNext();
                if (conductorSwitcher.getCurrentView() != conductor_name)
                    conductorSwitcher.showNext();
                if (teacherSwitcher.getCurrentView() != teacher_incharge)
                    teacherSwitcher.showNext();
            }
        });

        startAttendance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(AttendantActivity.this, StudentActivity.class);
                i.putExtra("regno", numberplate);
                i.putExtra("driver", drivername);
                i.putExtra("conductor", conductorname);
                i.putExtra("teacher", teacherincharge);
                i.putExtra("teachernumber", teacherinchargeNumber);
                i.putExtra("drivernumber", drivernumbervalue);
                startActivity(i);
            }
        });
        if (isConnected(AttendantActivity.this))
            new BusDownloadGETTask(this).execute("https://biometric-app.herokuapp.com/scanapp/bus_route_number/" + user_name);
        else
            Toast.makeText(AttendantActivity.this, "No internet", Toast.LENGTH_SHORT).show();
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
                        new LogoutPOSTtask(AttendantActivity.this).execute("https://biometric-app.herokuapp.com/rest-auth/logout/");
                        Intent i = new Intent();
                        i.setClass(AttendantActivity.this, LoginActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void opendialogemergency(ArrayList<EmergencyContact> emergencycontacts1) {
        RecyclerView recyclerView;
        RCVAdapterEmergency adapter;

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View view = layoutInflater.inflate(R.layout.dialog_layout_emergency, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewemergency);

        adapter = new RCVAdapterEmergency(this, emergencycontacts1, this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        lm.setOrientation(LinearLayoutManager.VERTICAL);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        alertDialogBuilder.setView(view);
        alertDialogBuilder
                .setTitle("EMERGENCY CONTACTS")
                .setCancelable(false)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        alertD.getButton(alertD.BUTTON_NEGATIVE).setTextColor(Color.RED);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.logout) {
            openDialog();
        } else if (id == R.id.emergency) {
            new emergencyContactTask(AttendantActivity.this).execute("https://biometric-app.herokuapp.com/govt-emergency-contact/contacts_list/");
        }
        return true;
    }


    public void processResults(Bus bus) {

        driver_name.setText(bus.getDriver_name());
        conductor_name.setText(bus.getConductor_name());
        number_plate.setText(bus.getNumber_plate());
        bus_no.setText(bus.getBus_no());
        teacher_incharge.setText(bus.getTeacher_incharge());

        drivername = bus.getDriver_name();
        conductorname = bus.getConductor_name();
        numberplate = bus.getNumber_plate();
        teacherincharge = bus.getTeacher_incharge();
        teacherinchargeNumber = bus.getTeacher_incharge_number();
        drivernumbervalue = bus.getDriver_name_number();

        editregno.setText(numberplate);
        editdriver.setText(drivername);
        editconductor.setText(conductorname);
        editteacher.setText(teacherincharge);
    }

    public void processResultsemergency(EmergencyContact[] contacts) {
        ArrayList<EmergencyContact> emergencycontacts = new ArrayList<>();

        if (contacts == null)
            return;
        for (EmergencyContact contact : contacts) {
            emergencycontacts.add(contact);
        }
        opendialogemergency(emergencycontacts);
    }

    @Override
    public void contactCLicked(EmergencyContact e) {
        Log.e("call","call");
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:101"));
            Log.e("call","call");
            startActivity(intent);

    }
}
