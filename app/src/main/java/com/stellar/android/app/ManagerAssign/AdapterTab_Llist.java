package com.stellar.android.app.ManagerAssign;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.stellar.android.app.R;

public class AdapterTab_Llist extends RecyclerView.Adapter<AdapterTab_Llist.Holder> {


    private ItemClickListener mClickListener;
   private ArrayList<DetailsModel> list;

    public AdapterTab_Llist(ArrayList<DetailsModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_aprove,viewGroup,false);

        Holder holder=new Holder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.name.setText(list.get(i).getName().toString());
        holder.mobile.setText(list.get(i).getMobile().toString());
        holder.contype.setText(list.get(i).getContacttype().toString());
        holder.address.setText(list.get(i).getAddress().toString());
        holder.details.setText(list.get(i).getDetails().toString());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder
    {

        TextView name,mobile,address,contype,details;
        ImageView ys,no;
        Holder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.item_customername);
            mobile=itemView.findViewById(R.id.item_mobile);
            address=itemView.findViewById(R.id.item_address);
            contype=itemView.findViewById(R.id.item_contacttype);
            details=itemView.findViewById(R.id.item_details);

            ys=itemView.findViewById(R.id.item_yes);
            no=itemView.findViewById(R.id.item_no);

            ys.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(getAdapterPosition());

                    ys.setVisibility(View.GONE);
                    no.setVisibility(View.GONE);


                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onCanleClick(getAdapterPosition());
                    ys.setVisibility(View.GONE);
                    no.setVisibility(View.GONE);
                }
            });



        }
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick( int position);
        void onCanleClick(int i);
    }
}
