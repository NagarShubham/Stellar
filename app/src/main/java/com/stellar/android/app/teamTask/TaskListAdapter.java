package com.stellar.android.app.teamTask;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stellar.android.app.R;
import com.stellar.android.app.Task_List.ListModel;

import java.util.ArrayList;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.Holder>{

    private ArrayList<ListModel> arrayList;

    public TaskListAdapter(ArrayList<ListModel> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.taskitem,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {


        holder.customername.setText(arrayList.get(i).getCustomer_name());
        holder.requrment.setText(arrayList.get(i).getTypeofRequest());
        holder.mobile.setText(arrayList.get(i).getMobile());
        holder.date.setText(arrayList.get(i).getDay());
        // holder.add.setText(arrayList.get(i).getDetails().toString());


        holder.source.setText(arrayList.get(i).getSource());
        holder.category.setText(arrayList.get(i).getCategory());
        holder.totime.setText(arrayList.get(i).getTotime());
        holder.fromtime.setText(arrayList.get(i).getFormtime());
        holder.compName.setText(arrayList.get(i).getCompanyName());
        holder.purpose.setText(arrayList.get(i).getPurpose());
        holder.details.setText(arrayList.get(i).getAddress());

holder.linearLayout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView status;
        TextView customername, mobile, date, add, details;
        ImageView req_btn, addlead;
        RelativeLayout layout;
        LinearLayout linearLayout;

        TextView category, source, requrment, compName, purpose, totime, fromtime;

        public Holder(@NonNull View itemView) {
            super(itemView);


            customername = itemView.findViewById(R.id.item_name);
            mobile = itemView.findViewById(R.id.item_imobile);
            date = itemView.findViewById(R.id.item_idate);
            requrment = itemView.findViewById(R.id.item_req);
            add = itemView.findViewById(R.id.item_iaddress);
            req_btn = itemView.findViewById(R.id.img_req);
            layout = itemView.findViewById(R.id.re_color);
            status = itemView.findViewById(R.id.texts);
            addlead = itemView.findViewById(R.id.item_taddlead);
            linearLayout = itemView.findViewById(R.id.libutton);

            details = itemView.findViewById(R.id.item_details);
            category = itemView.findViewById(R.id.item_category);
            source = itemView.findViewById(R.id.item_source);
            //requrment=itemView.findViewById(R.id.Llongclick);
            compName = itemView.findViewById(R.id.item_compname);
            purpose = itemView.findViewById(R.id.item_purpose);
            totime = itemView.findViewById(R.id.item_totime);
            fromtime = itemView.findViewById(R.id.item_fromtime);

        }
    }
}