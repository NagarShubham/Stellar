package com.stellar.android.app.Attendance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.stellar.android.app.R;

public class AttendenceAdapter extends RecyclerView.Adapter<AttendenceAdapter.Holder>
{
    private List<AttendenceModel> list;
    private Context context;

    public AttendenceAdapter(List<AttendenceModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_attendance,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        AttendenceModel  model=list.get(i);
        if (model.getStatus().equalsIgnoreCase("approve"))
        {
            holder.tv_date.setTextColor(context.getResources().getColor(R.color.call));
            holder.img.setBackgroundResource(R.drawable.checked);
        }
        if (model.getStatus().equalsIgnoreCase("reject")){
            holder.tv_date.setTextColor(context.getResources().getColor(R.color.red));
            holder.img.setBackgroundResource(R.drawable.cancel);
        }
 if (model.getStatus().equalsIgnoreCase("pending")){
            holder.tv_date.setTextColor(context.getResources().getColor(R.color.red));
            holder.img.setBackgroundResource(R.drawable.cancel);
        }

        holder.tv_date.setText(model.getDate());
        holder.tv_month.setText(model.getMonth()+"-"+model.getYear());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView tv_date,tv_month;
        ImageView img;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_date=itemView.findViewById(R.id.tv_date);
            tv_month=itemView.findViewById(R.id.tv_month);
            img=itemView.findViewById(R.id.im_status);


        }
    }
}
