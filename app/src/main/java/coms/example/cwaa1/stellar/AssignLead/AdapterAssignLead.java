package coms.example.cwaa1.stellar.AssignLead;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import coms.example.cwaa1.stellar.Lead_List.AdapterLeadList;
import coms.example.cwaa1.stellar.Lead_List.LeadModel;
import coms.example.cwaa1.stellar.R;

public class AdapterAssignLead extends RecyclerView.Adapter<AdapterAssignLead.Holder> {
    private List<LeadModel> list;
    private AdapterAssignLead.ItemClickListener mClickListener;
    public AdapterAssignLead(List<LeadModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_check_lead,viewGroup,false);
        AdapterAssignLead.Holder holder=new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
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
        CheckedTextView CheckedTextView;
        RelativeLayout cview;

        public Holder(@NonNull final View itemView) {
            super(itemView);
            customername = itemView.findViewById(R.id.item_checkname);
            mobile = itemView.findViewById(R.id.item_checkmobile);
            date = itemView.findViewById(R.id.item_checkdate);
            ctype = itemView.findViewById(R.id.item_checkcontacttype);
            address = itemView.findViewById(R.id.item_checkaddress);
            cview = itemView.findViewById(R.id.item_cview);

            CheckedTextView=itemView.findViewById(R.id.item_checkbox);

            CheckedTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(CheckedTextView,getAdapterPosition());

                }
            });
            cview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onView(getAdapterPosition());
                }
            });

            if (CheckedTextView.isChecked())
            {
                Log.e("is chakes","here");
            }





        }
    }

    public void setClickListener(AdapterAssignLead.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view,int position);
        void onView(int position);
    }



}
