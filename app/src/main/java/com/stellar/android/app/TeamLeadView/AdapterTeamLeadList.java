package com.stellar.android.app.TeamLeadView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.stellar.android.app.Lead_List.AdapterLeadList;
import com.stellar.android.app.Lead_List.LeadModel;
import com.stellar.android.app.R;

public class AdapterTeamLeadList  extends RecyclerView.Adapter<AdapterTeamLeadList.Holder> {
    private List<LeadModel> list;
    private AdapterLeadList.ItemClickListener mClickListener;

    AdapterTeamLeadList(List<LeadModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterTeamLeadList.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_team_lead, viewGroup, false);
        AdapterTeamLeadList.Holder holder = new AdapterTeamLeadList.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTeamLeadList.Holder holder, int i) {

        holder.customername.setText(list.get(i).getName());
        holder.mobile.setText(list.get(i).getMobile());
        holder.date.setText(list.get(i).getDate());
        holder.ctype.setText(list.get(i).getContacttype());
        holder.address.setText(list.get(i).getAddress());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView customername, mobile, date, ctype, address;
        LinearLayout linearLayout;
        ImageView iv, followup, call;

        public Holder(@NonNull View itemView) {
            super(itemView);
            customername = itemView.findViewById(R.id.item_leadname);
            mobile = itemView.findViewById(R.id.item_leadmobile);
            date = itemView.findViewById(R.id.item_leaddate);
            ctype = itemView.findViewById(R.id.item_leadcontacttype);
            address = itemView.findViewById(R.id.item_leadaddress);

            iv = itemView.findViewById(R.id.item_view);

            iv.setOnClickListener(v -> mClickListener.onItemClick(getAdapterPosition()));

        }

    }

    public void setClickListener(AdapterLeadList.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view ,int position);
    }

}
