package coms.example.cwaa1.stellar.LeadView;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import coms.example.cwaa1.stellar.R;

public class OldFollowAdapter extends RecyclerView.Adapter<OldFollowAdapter.Holder> {
    private OldFollowAdapter.ItemClickListener mClickListener;
private List<OldFollowUp>list;

    OldFollowAdapter(List<OldFollowUp> list) {
        this.list = list;
        Log.e("list Size",""+list.size());

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_old_followup,viewGroup,false);
        Holder holder=new Holder(view);
       return holder;


    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        Log.e("posion",""+i);

        holder.ldate.setText(list.get(i).getOldDate().toString());
        holder.ndate.setText(list.get(i).getNextDate().toString());
        holder.comm.setText(list.get(i).getOldcomant().toString());
        holder.res.setText(list.get(i).getOldresp().toString());
        holder.name.setText(list.get(i).getEmpname().toString());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        TextView ldate,ndate,comm,res,name;
        FloatingActionButton call;


        public Holder(@NonNull View itemView) {
            super(itemView);

            ldate=itemView.findViewById(R.id.o_ldate);
            ndate=itemView.findViewById(R.id.o_ndate);
            comm=itemView.findViewById(R.id.o_commant);
            res=itemView.findViewById(R.id.o_res);
            name=itemView.findViewById(R.id.o_ename);
            call=itemView.findViewById(R.id.o_call);

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onCall(getAdapterPosition());
                }
            });

        }
    }

    public void setClickListener(OldFollowAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onCall(int position);

    }
    }
