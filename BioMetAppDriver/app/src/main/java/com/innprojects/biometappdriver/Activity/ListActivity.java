package com.innprojects.biometappdriver.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telephony.SmsManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.innprojects.biometappdriver.R;
import com.innprojects.biometappdriver.RCVAdapter;
import com.innprojects.biometappdriver.SendMailTask;
import com.innprojects.biometappdriver.models.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    RecyclerView recyclerView;
    ArrayList<Student> students;
    RCVAdapter adapter;

    SharedPreferences mPreferences;
    String user_name;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        Drawable gradient = getResources().getDrawable(R.drawable.actionbar_gradient);
        getSupportActionBar().setBackgroundDrawable(gradient);

        students = (ArrayList<Student>) getIntent().getSerializableExtra("students_list");
        adapter = new RCVAdapter(this, students);
        recyclerView.setAdapter(adapter);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_name = mPreferences.getString("username", null);
        Log.e("Preferences: ", user_name);

//        String str = StringUtils.join(students.toArray(),"\n");

        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        lm.setOrientation(LinearLayoutManager.VERTICAL);

//        StaggeredGridLayoutManager gridLayoutManager =
//                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(gridLayoutManager);

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP |
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int i = viewHolder.getAdapterPosition();
                int j = target.getAdapterPosition();
                Student first = students.get(i);
                Student second = students.get(j);
                students.set(i, second);
                students.set(j, first);
                adapter.notifyItemMoved(j, i);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                students.remove(pos);
                adapter.notifyDataSetChanged();
            }
        };
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.mail) {
            String fromEmail = ("simran.sarin.03.ss@gmail.com");
            String fromPassword = ("dhinglakalaka");
            String toEmails = ("simran.sarin.03.ss@gmail.com");
            List<String> toEmailList = Arrays.asList(toEmails
                    .split("\\s*,\\s*"));
            Log.i("SendMailActivity", "To List: " + toEmailList);
            String emailSubject = ("Today's Attendance for bus number: " + user_name);
            StringBuilder buf = new StringBuilder();
            for (Student each : students) {
                buf.append(each.getStudent_id());
                buf.append(" : ");
                buf.append(each.getStudent_adm_no());
                buf.append(" : ");
                buf.append(each.getStudent_name());
                buf.append(" : ");
                buf.append(each.getPhone_number());
                buf.append("\n");
            }
            s = buf.toString();
            String emailBody = (s);
            new SendMailTask(this).execute(fromEmail,
                    fromPassword, toEmailList, emailSubject, emailBody);
        } else if (id == R.id.dropAtSchool) {
            for (Student each : students) {
                String phone = each.getPhone_number();
                if (phone != null)
                    sendSMSMessage(each.getStudent_name(), each.getPhone_number());
            }
        } else if (id == R.id.startDrop) {
            navigateUpTo(new Intent(getBaseContext(), StudentActivity.class));
        }
        return true;
    }

    protected void sendSMSMessage(String student_name, String phone) {
//        phoneNo = txtphoneNo.getText().toString();
//        message = txtMessage.getText().toString();

        String complete_message = "Dear parent, your ward :" + student_name + " has been dropped at school";

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
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone, null, complete_message, null, null);
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
}
