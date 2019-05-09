package com.stellar.android.app.TargetList;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.stellar.android.app.R;

public class TargetAdapter extends RecyclerView.Adapter<TargetAdapter.Holder> {
    List<TargetModel> list;

    public TargetAdapter(List<TargetModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public TargetAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.target_item,viewGroup,false);
        TargetAdapter.Holder holder=new TargetAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TargetAdapter.Holder holder, int i) {

        holder.fromdate.setText(list.get(i).getFrom_date().toString());
        holder.todate.setText(list.get(i).getTo_date().toString());
        holder.assign.setText(list.get(i).getAssigned_by().toString());
        holder.achive.setText(list.get(i).getAcheived_value().toString());
        holder.target.setText(list.get(i).getTarget_value().toString());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView fromdate,todate,assign,achive,target;
        public Holder(@NonNull View itemView) {
            super(itemView);
            fromdate=itemView.findViewById(R.id.card_formdate);
            todate=itemView.findViewById(R.id.card_todate);
            assign=itemView.findViewById(R.id.card_assignby);
            achive=itemView.findViewById(R.id.card_achive);
            target=itemView.findViewById(R.id.card_targetvalue);

        }
    }

}
