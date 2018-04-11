package com.innprojects.biometappdriver;

import android.content.Context;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.innprojects.biometappdriver.models.EmergencyContact;
import com.innprojects.biometappdriver.models.Student;

import java.util.ArrayList;

/**
 * Created by simransarin on 04/07/17.
 */

public class RCVAdapterEmergency extends RecyclerView.Adapter<RCVAdapterEmergency.OurHolder> {

    private Context mContext;
    private ArrayList<EmergencyContact> contacts;
    EmergencyContactListListener listener;

    public interface EmergencyContactListListener{
        void contactCLicked(EmergencyContact e);
    }

    public RCVAdapterEmergency(Context context, ArrayList<EmergencyContact> data, EmergencyContactListListener listener) {
        mContext = context;
        contacts = data;
        this.listener = listener;
    }

    public class OurHolder extends RecyclerView.ViewHolder{

        TextView tv1;
        TextView tv2;
        ImageButton b;

        public OurHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.emergency_name);
            tv2 = (TextView) itemView.findViewById(R.id.emergency_number);
            b = (ImageButton) itemView.findViewById(R.id.callemergency);
        }
    }
    @Override
    public RCVAdapterEmergency.OurHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_item_emergency,parent,false);
        return new RCVAdapterEmergency.OurHolder(v);
    }

    @Override
    public void onBindViewHolder(RCVAdapterEmergency.OurHolder holder, final int position) {
        final EmergencyContact e = contacts.get(position);
        holder.tv1.setText(e.getContact_name());
        holder.tv2.setText(e.getPhone_number());
        holder.b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.contactCLicked(e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
