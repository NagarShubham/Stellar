package com.stellar.android.app;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.stellar.android.app.Lead_List.LeadListActivity;
import com.stellar.android.app.Model.checkBoxModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddTargetActivity extends Fragment {
    private String empid,leadid;
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    private Button btn_save;
    private List<checkBoxModel> list;
    private ListView listView;
    List<String> idlist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments()!=null)
        {
            empid=getArguments().getString("empid");
            leadid=getArguments().getString("lid");
            //list.hashCode(getArguments().getStringArrayList("chi"));

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_add_target,container,false);
        idlist=new ArrayList();
        list = new ArrayList<>();

        final TextView vi=view.findViewById(R.id.showid);
        listView =view. findViewById(R.id.listview);
        btn_save = view.findViewById(R.id.bt);
        Log.e("checbox page ","id"+empid+" "+leadid);




//        CheckAdapter adapter=new CheckAdapter(LeadListActivity.chidnamelist,getContext());
//        listView.setAdapter(adapter);

//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                if(((CheckedTextView) view).isChecked()){
//
//                    ((CheckedTextView) view).setChecked(false);
//                    idlist.remove(LeadListActivity.chidnamelist.get(position).getId());
//                  //  Toast.makeText(getContext(), "unchak "+LeadListActivity.chidnamelist.get(position).getName(), Toast.LENGTH_SHORT).show();
//                }else{
//
//                   // Toast.makeText(getContext(), "check "+LeadListActivity.chidnamelist.get(position).getName(), Toast.LENGTH_SHORT).show();
//                    ((CheckedTextView) view).setChecked(true);
//                    idlist.add(LeadListActivity.chidnamelist.get(position).getId());
//                }
//
//            }
//        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vi.setText(idlist.toString());

                JSONArray jsonArray=new JSONArray();

                for (int i=0;i<idlist.size();i++)
                {
                    jsonArray.put(idlist.get(i));
                }

                Log.e("Jsonarray",""+jsonArray);

                apicall(jsonArray);
            }
        });

        return view;
    }

    private void apicall(JSONArray jsonArray) {

        String key = getsha256("leadAssign");
        Log.e("key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "leadAssign")
                .add("key", key)
                .add("assign_by_employee_id", empid)
                .add("lead_id", leadid)
                .add("assign_to_employee_id",jsonArray.toString())
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.e("network", e.getMessage());
                        Toast.makeText(getActivity(), "network Problem", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Log.e("resulttttt",result);
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        Log.e("message   ", object.getString("message"));

                        String msg = object.getString("message");
                        if (msg.equals("true")) {

                            LeadListActivity leadListActivity=new LeadListActivity();
                            FragmentTransaction transaction = null;
                            if (getFragmentManager() != null) {
                                transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.fream, leadListActivity);
                                transaction.commit();
                            }



                        }
                        toast("Not Assign Try Again");

                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

            }
        });
    }








    public static String getsha256(String text) {
        String input = text.concat(private_key);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (md != null) {
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0".concat(hashtext);
            }
            return hashtext;
        } else return "";
    }

    private void toast(final String s) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
