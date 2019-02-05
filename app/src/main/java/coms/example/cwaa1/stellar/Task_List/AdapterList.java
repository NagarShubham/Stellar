package coms.example.cwaa1.stellar.Task_List;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import coms.example.cwaa1.stellar.R;

public class AdapterList extends RecyclerView.Adapter<AdapterList.child>{
    public ItemClickListener mClickListener;

  private ArrayList<ListModel> arrayList;

    public AdapterList(ArrayList<ListModel> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
        @Override
        public child onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view=LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.taskitem,viewGroup,false);

           child  child=new child(view);

            return child;
        }

        @Override
        public void onBindViewHolder(@NonNull child child, int i) {

            child.customername.setText(arrayList.get(i).getCustomer_name().toString());
            child.requrment.setText(arrayList.get(i).getTypeofRequest().toString());
            child.mobile.setText(arrayList.get(i).getMobile().toString());
            child.date.setText(arrayList.get(i).getDay().toString());
           // child.add.setText(arrayList.get(i).getDetails().toString());


            child.source.setText(arrayList.get(i).getSource().toString());
            child.category.setText(arrayList.get(i).getCategory().toString());
            child.totime.setText(arrayList.get(i).getTotime().toString());
            child.fromtime.setText(arrayList.get(i).getFormtime().toString());
            child.compName.setText(arrayList.get(i).getCompanyName().toString());
child.purpose.setText(arrayList.get(i).getPurpose());
child.details.setText(arrayList.get(i).getAddress());

            Log.v("Status", "status " + arrayList.get(i).getTask_status() + " " + i);
            if (arrayList.get(i).getTask_status().equals("request for approvel"))
            {
                child.status.setText("Pending");
                child.status.setVisibility(View.VISIBLE);
                child.layout.setBackgroundColor(Color.parseColor("#58BDC7"));
                child.req_btn.setVisibility(View.GONE);
            }else if (arrayList.get(i).getTask_status().equals("reject"))
            {
                child.status.setText("Reject");
                child.status.setVisibility(View.VISIBLE);
                child.layout.setBackgroundColor(Color.RED);
                child.req_btn.setVisibility(View.GONE);
            }else if (arrayList.get(i).getTask_status().equals("approved"))
            {
                child.status.setText("Approve");
                child.status.setVisibility(View.VISIBLE);
                child.layout.setBackgroundColor(Color.GREEN);
                child.req_btn.setVisibility(View.GONE);
            }
            else {
                child.status.setVisibility(View.GONE);
                child.layout.setBackgroundColor(Color.parseColor("#58BDC7"));
                child.req_btn.setVisibility(View.VISIBLE);
            }
        }

    @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public  class child extends RecyclerView.ViewHolder  {
         TextView status;
            TextView customername ,mobile,date,add,details;
            ImageView req_btn,addlead;
            RelativeLayout layout;
            LinearLayout linearLayout;

            TextView category,source,requrment,compName,purpose,totime,fromtime;


            public child(@NonNull View itemView) {
                super(itemView);
                customername=itemView.findViewById(R.id.item_name);
                mobile=itemView.findViewById(R.id.item_imobile);
                date=itemView.findViewById(R.id.item_idate);
                requrment=itemView.findViewById(R.id.item_req);
                add=itemView.findViewById(R.id.item_iaddress);
                req_btn=itemView.findViewById(R.id.img_req);
                layout=itemView.findViewById(R.id.re_color);
                status=itemView.findViewById(R.id.texts);
                addlead=itemView.findViewById(R.id.item_taddlead);
                linearLayout=itemView.findViewById(R.id.Llongclick);

                details=itemView.findViewById(R.id.item_details);
                category=itemView.findViewById(R.id.item_category);
                source=itemView.findViewById(R.id.item_source);
                //requrment=itemView.findViewById(R.id.Llongclick);
                compName=itemView.findViewById(R.id.item_compname);
                purpose=itemView.findViewById(R.id.item_purpose);
                totime=itemView.findViewById(R.id.item_totime);
                fromtime=itemView.findViewById(R.id.item_fromtime);

                linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
            mClickListener.onLongClick(getAdapterPosition());
            return true;
                    }
                });


                req_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickListener.onItemClick(v,getAdapterPosition());

                    }
                });
                addlead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickListener.onleadAdd(getAdapterPosition());
                    }
                });
               // req_btn.setVisibility(View.GONE);

            }

        }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;

    }

    public interface ItemClickListener {

        void onItemClick(View view, int position);
       void onleadAdd( int position);
       void onLongClick(int position);

    }

    }


