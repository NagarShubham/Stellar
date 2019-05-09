package com.stellar.android.app.LeavForm;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.stellar.android.app.R;

public class AdapterLeave extends RecyclerView.Adapter<AdapterLeave.Holder> {

    private List<LeaveModel>list;

    public AdapterLeave(List<LeaveModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_leave,viewGroup,false);
       AdapterLeave.Holder holder=new Holder(view);
       return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        LeaveModel model=list.get(i);

        holder.todate.setText(model.getTodate());
        holder.formdate.setText(model.getFromdate());
        holder.status.setText(model.getLeaveStatus());
        holder.responce.setText(model.getResonLeave());
        holder.mresponce.setText(model.getAdminResonLeave());
        holder.ldaytype.setText(model.getLeavType());

        if (model.getLeaveStatus().equals("Approved"))
        {
            holder.mresponce.setVisibility(View.GONE);
            holder.layout.setBackgroundColor(Color.parseColor("#68C758"));
        }
        else if (model.getLeaveStatus().equals("Pending"))
        {
            holder.mresponce.setVisibility(View.GONE);
            holder.layout.setBackgroundColor(Color.parseColor("#58BDC7"));

        }
        else {
            holder.mresponce.setVisibility(View.VISIBLE);
            holder.layout.setBackgroundColor(Color.parseColor("#DE3F3F"));

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView todate,formdate,ldaytype,status,responce,mresponce;
        private LinearLayout layout;

        public Holder(@NonNull View itemView) {
            super(itemView);

            todate=itemView.findViewById(R.id.item_ltodate);
            formdate=itemView.findViewById(R.id.item_lformdate);
            ldaytype=itemView.findViewById(R.id.item_ldayType);
            status=itemView.findViewById(R.id.item_lstatus);
            responce=itemView.findViewById(R.id.item_lresponce);
            mresponce=itemView.findViewById(R.id.mresponce);
            layout=itemView.findViewById(R.id.item_llayout);

        }
    }
}
