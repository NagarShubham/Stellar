package com.stellar.android.app.Chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.stellar.android.app.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holdere> {


    private ArrayList<ChatModel> list;
    public ItemClickListener itemClickListener;


    public ChatAdapter(ArrayList<ChatModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holdere onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_chat_view, viewGroup, false);

        return new Holdere(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holdere holdere, int i) {

        final ChatModel model=list.get(i);

        if (model.getId().equals("1")) {
            if(!model.getSend()) {
                holdere.resend.setVisibility(View.VISIBLE);
                holdere.s.setVisibility(View.GONE);
                holdere.r.setVisibility(View.VISIBLE);
                holdere.receiver_ti.setText(model.getDate());
                holdere.reciver.setText(model.getMsg());
            }
            else {
                holdere.resend.setVisibility(View.GONE);
                holdere.s.setVisibility(View.GONE);
                holdere.r.setVisibility(View.VISIBLE);
                holdere.receiver_ti.setText(model.getDate());
                holdere.reciver.setText(model.getMsg());
            }
        } else {
            holdere.resend.setVisibility(View.GONE);
            holdere.r.setVisibility(View.GONE);
            holdere.s.setVisibility(View.VISIBLE);
            holdere.send_ti.setText(model.getDate());
            holdere.send.setText(model.getMsg());

        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holdere extends RecyclerView.ViewHolder {

        TextView send, reciver;
        TextView send_ti, receiver_ti;
        RelativeLayout r, s;
        TextView resend;

        public Holdere(@NonNull View itemView) {
            super(itemView);

            send = itemView.findViewById(R.id.rc_sender);
            reciver = itemView.findViewById(R.id.rc_receiver);
            s = itemView.findViewById(R.id.send);
            r = itemView.findViewById(R.id.recive);
            resend = itemView.findViewById(R.id.resend);

            send_ti = itemView.findViewById(R.id.ti_sender);
            receiver_ti = itemView.findViewById(R.id.ti_receiver);

            resend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null)  itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });

        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}