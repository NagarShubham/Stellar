package coms.example.cwaa1.stellar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;

import java.util.List;

import coms.example.cwaa1.stellar.Model.checkBoxModel;
import coms.example.cwaa1.stellar.R;

public class CheckAdapter extends BaseAdapter {

    List<checkBoxModel>list;
    Context context;

    public CheckAdapter(List<checkBoxModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
      LayoutInflater  inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
            vi = inflater.inflate(R.layout.custom_checkbox, null);



        CheckedTextView checkBox= vi.findViewById(R.id.check1);

            checkBox.setText(list.get(position).getName());

           if (checkBox.isChecked())
           {
               Log.e("is chakes",list.get(position).getName());
           }




        return vi;
    }
}
