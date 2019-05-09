package com.stellar.android.app;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;


public class PixAdapter  extends RecyclerView.Adapter<PixAdapter.Holder>{
    private ItemClickListener mClickListener;
    private List<Bitmap>list;

    public PixAdapter(List<Bitmap> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pix,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.imageView.setImageBitmap(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView imageView,imgCancel;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgPix);
            imgCancel=itemView.findViewById(R.id.imgCancel);

            imgCancel.setOnClickListener(v -> mClickListener.onCancel(getAdapterPosition()));
            imageView.setOnClickListener(v -> mClickListener.onItemClick(getAdapterPosition()));

        }
    }
    public void setClickListener(PixAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
        void onCancel(int position);
    }
}