package com.stellar.android.app.WonLossLead;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import com.stellar.android.app.R;

public class WonLossAdapter extends RecyclerView.Adapter<WonLossAdapter.Holder> {

    private List<WonLossmodel>list;
    public WonLossAdapter.ItemClickListener mClickListener;


    public WonLossAdapter(List<WonLossmodel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wonloss,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        WonLossmodel lossmodel = list.get(i);

        holder.customername.setText(lossmodel.getName());
        holder.mobile.setText(lossmodel.getMobile());
        holder.compName.setText(lossmodel.getCompany_name());
        holder.date.setText(lossmodel.getDate());
        holder.formtime.setText(lossmodel.getFormtime());
        holder.totime.setText(lossmodel.getTotime());

        holder.leadvalue.setText(lossmodel.getLeadValue());
        holder.category.setText(lossmodel.getCategory());
        holder.tyRequ.setText(lossmodel.getType_of_requirement());

        if (9==i && list.size()==9){
            holder.btnShowMore.setVisibility(View.VISIBLE);
            holder.btnShowMore.setOnClickListener(v ->mClickListener.onItemChange(i));
        }else holder.btnShowMore.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView customername, mobile, date, compName,totime,formtime,leadvalue,category,tyRequ;
        Button btnShowMore;
        public Holder(@NonNull View itemView) {
            super(itemView);

            customername=itemView.findViewById(R.id.item_wonname);
            mobile=itemView.findViewById(R.id.item_wonmobile);
            compName=itemView.findViewById(R.id.item_woncompname);
            date=itemView.findViewById(R.id.item_wondate);
            formtime=itemView.findViewById(R.id.item_wonfromtime);
            totime=itemView.findViewById(R.id.item_wontotime);
            leadvalue=itemView.findViewById(R.id.item_wonvalue);
            category=itemView.findViewById(R.id.item_woncategory);
            tyRequ=itemView.findViewById(R.id.item_wonreq);
            btnShowMore=itemView.findViewById(R.id.btnShowMore);


        }
    }

    public void setClickListener(WonLossAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemChange(int position);
    }



}
