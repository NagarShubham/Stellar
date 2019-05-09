package com.stellar.android.app.Chat;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.stellar.android.app.R;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder> {
    private ArrayList<ChatListModel> arrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private String count;
    private Activity context;
    private ChatListFragment fragment;


    public ChatListAdapter(ArrayList<ChatListModel> arrayList, Activity context, ChatListFragment fragment) {
        this.arrayList = arrayList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_list, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {


        final ChatListModel e = arrayList.get(i);

        holder.name.setText(e.getName());
        holder.executive_message_counter.setText(e.getCount());

        if(e.getCount().equals("0")) {
            holder.executive_message_counter.setVisibility(View.GONE);
        }

        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(context, ChatActivity.class);
                i.putExtra("id", e.getId());
                i.putExtra("Name", e.getName());
                fragment.startActivityForResult(i,1);

            }
        });




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,executive_message_counter;
        ImageView chat;

        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            chat = itemView.findViewById(R.id.chat);
            executive_message_counter = itemView.findViewById(R.id.executive_message_counter);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }



}
