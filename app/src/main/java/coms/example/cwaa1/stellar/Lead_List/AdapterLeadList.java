package coms.example.cwaa1.stellar.Lead_List;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


import coms.example.cwaa1.stellar.ManagerAssign.AdapterTab_Llist;
import coms.example.cwaa1.stellar.R;
import coms.example.cwaa1.stellar.Task_List.AdapterList;

public class AdapterLeadList extends RecyclerView.Adapter<AdapterLeadList.Holder> {
    List<LeadModel> list;
    public AdapterLeadList.ItemClickListener mClickListener;

    public AdapterLeadList(List<LeadModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_leadlist, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        holder.customername.setText(list.get(i).getName().toString());
        holder.mobile.setText(list.get(i).getMobile().toString());
        holder.date.setText(list.get(i).getDate().toString());
        holder.ctype.setText(list.get(i).getContacttype().toString());
        holder.address.setText(list.get(i).getAddress().toString());


        holder.compName.setText(list.get(i).getCompany_name().toString());
        holder.totime.setText(list.get(i).getTotime().toString());
        holder.formtime.setText(list.get(i).getFormtime().toString());
        holder.leadvalue.setText(list.get(i).getExpected_lead_value().toString());
        holder.category.setText(list.get(i).getCategory().toString());
        holder.tyRequ.setText(list.get(i).getType_of_requirement().toString());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView customername, mobile, date, ctype, address,compName,totime,formtime,leadvalue,category,tyRequ;
        LinearLayout linearLayout;
        ImageView iv, followup, call;

        public Holder(@NonNull View itemView) {
            super(itemView);
            customername = itemView.findViewById(R.id.item_leadname);
            mobile = itemView.findViewById(R.id.item_leadmobile);
            date = itemView.findViewById(R.id.item_leaddate);
            ctype = itemView.findViewById(R.id.item_leadcontacttype);
            address = itemView.findViewById(R.id.item_leadaddress);

            compName = itemView.findViewById(R.id.item_compname);
            totime = itemView.findViewById(R.id.item_totime);
            formtime = itemView.findViewById(R.id.item_fromtime);
            leadvalue = itemView.findViewById(R.id.item_leadvalue);
            category = itemView.findViewById(R.id.item_category);
            tyRequ = itemView.findViewById(R.id.item_req);

            iv = itemView.findViewById(R.id.item_view);
            followup = itemView.findViewById(R.id.item_follow);
            call = itemView.findViewById(R.id.item_call);
            // linearLayout = itemView.findViewById(R.id.liner);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(getAdapterPosition());

                }
            });
            followup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mClickListener.onFollowup(getAdapterPosition());
                }
            });
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onCall(getAdapterPosition());
                }
            });


        }

    }

    public void setClickListener(AdapterLeadList.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);

        void onFollowup(int i);

        void onCall(int i);
    }

}
