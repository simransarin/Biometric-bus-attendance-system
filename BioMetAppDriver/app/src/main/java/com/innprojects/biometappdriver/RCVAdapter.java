package com.innprojects.biometappdriver;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innprojects.biometappdriver.models.Student;

import java.util.ArrayList;

/**
 * Created by simransarin on 23/06/17.
 */

public class RCVAdapter extends RecyclerView.Adapter<RCVAdapter.OurHolder> {

    private Context mContext;
    private ArrayList<Student> students;

    public RCVAdapter(Context context, ArrayList<Student> data) {
        mContext = context;
        students = data;
    }

    public class OurHolder extends RecyclerView.ViewHolder{

        TextView tv1;
        TextView tv2;
        TextView tv3;

        public OurHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.id_row);
            tv2 = (TextView) itemView.findViewById(R.id.name_row);
            tv3 = (TextView) itemView.findViewById(R.id.phone_row);
        }
    }
    @Override
    public RCVAdapter.OurHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_item,parent,false);
        return new OurHolder(v);
    }

    @Override
    public void onBindViewHolder(RCVAdapter.OurHolder holder, final int position) {
        final Student b = students.get(position);
        String id = b.getStudent_id();
        holder.tv1.setText(b.getStudent_adm_no());
        holder.tv2.setText(b.getStudent_name());
        holder.tv3.setText(b.getPhone_number());

    }

    @Override
    public int getItemCount() {
        return students.size();
    }
}
