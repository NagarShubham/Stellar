package coms.example.cwaa1.stellar.TeamLeadView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import coms.example.cwaa1.stellar.Lead_List.AdapterLeadList;
import coms.example.cwaa1.stellar.Lead_List.LeadModel;
import coms.example.cwaa1.stellar.R;

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

        holder.customername.setText(list.get(i).getName().toString());
        holder.mobile.setText(list.get(i).getMobile().toString());
        holder.date.setText(list.get(i).getDate().toString());
        holder.ctype.setText(list.get(i).getContacttype().toString());
        holder.address.setText(list.get(i).getAddress().toString());

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
//            followup = itemView.findViewById(R.id.item_follow);
//            call = itemView.findViewById(R.id.item_call);
//            // linearLayout = itemView.findViewById(R.id.liner);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(getAdapterPosition());

                }
            });
//            followup.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    mClickListener.onFollowup(getAdapterPosition());
//                }
//            });
//            call.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mClickListener.onCall(getAdapterPosition());
//                }
//            });


        }

    }

    public void setClickListener(AdapterLeadList.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view ,int position);

//        void onFollowup(int i);
//
//        void onCall(int i);
    }

}
