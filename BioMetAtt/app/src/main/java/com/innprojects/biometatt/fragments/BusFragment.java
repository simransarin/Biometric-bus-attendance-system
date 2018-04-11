package com.innprojects.biometatt.fragments;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.innprojects.biometatt.R;
import com.innprojects.biometatt.activity.StudentActivity;
import com.innprojects.biometatt.asyncTask.Busdownloadtask;
import com.innprojects.biometatt.javaModels.Bus;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusFragment extends Fragment {

    Context context;
    String busno;
    String drivernumber;
    String teachernumber;

    Button whatsapp;
    Button call;

    TextView driver_name;
    TextView conductor_name;
    TextView number_plate;
    TextView bus_no;
    TextView teacher_incharge;

    public BusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus, container, false);
        driver_name = (TextView) view.findViewById(R.id.driver_name);
        conductor_name = (TextView) view.findViewById(R.id.conductor_name);
        number_plate = (TextView) view.findViewById(R.id.reg_no);
        bus_no = (TextView) view.findViewById(R.id.bus_no);
        teacher_incharge = (TextView) view.findViewById(R.id.teacher_name);

        whatsapp = (Button) view.findViewById(R.id.whatsapp);
        call = (Button) view.findViewById(R.id.call);

        busno = ((StudentActivity) getActivity()).busnumber;

        call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + drivernumber));
                startActivity(intent);
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                openWhatsApp();
            }
        });

        return view;
    }

    private void openWhatsApp() {
        String smsNumber = teachernumber;
         smsNumber= smsNumber.replace("+", "").replace(" ", "");
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix

            startActivity(sendIntent);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(getContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Busdownloadtask task = new Busdownloadtask(this);
        task.execute("https://biometric-app.herokuapp.com/scanapp/bus_primary_key/" + busno);
    }

    public void processResults(Bus bus) {
        driver_name.setText(bus.getDriver_name());
        conductor_name.setText(bus.getConductor_name());
        number_plate.setText(bus.getNumber_plate());
        bus_no.setText(bus.getBus_no());
        teacher_incharge.setText(bus.getTeacher_incharge());

        drivernumber = bus.getDriver_name_number();
        teachernumber = bus.getTeacher_incharge_number();
    }
}
