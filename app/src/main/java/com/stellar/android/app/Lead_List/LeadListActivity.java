package com.stellar.android.app.Lead_List;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.stellar.android.app.Model.checkBoxModel;
import com.stellar.android.app.R;

public class LeadListActivity extends Fragment {

    private String mpar;

    private RecyclerView rcy_leadTask;
    private RecyclerView rcy_Teamlead;
private  ProgressBar progressBar;
    TextView tilead;
    private SharedPreferences preferences;
    private static String EMP_ID;
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    private List<LeadModel> list;
    private List<LeadModel> teamlist;
    public  List<checkBoxModel> chidnamelist;
    private Spinner spinner;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   setContentView(R.layout.activity_lead_list);

        if (getArguments() != null) {

            mpar = getArguments().getString("type");

        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lead_list, container, false);
        list = new ArrayList<>();
        teamlist = new ArrayList<>();
        chidnamelist=new ArrayList<>();


        progressBar = (ProgressBar) view.findViewById(R.id.pbforevent);

        progressBar.setVisibility(View.GONE);



        tilead = view.findViewById(R.id.ti_team);
        rcy_Teamlead = view.findViewById(R.id.lead_team);
        spinner=view.findViewById(R.id.c_name);

        Log.e("list in type ", mpar);
        if (mpar.equals("employee")) {
            tilead.setVisibility(View.GONE);
            rcy_Teamlead.setVisibility(View.GONE);

        }

       // lisfun(view);

        return view;
    }
//
//    private void lisfun(View view) {
//        preferences = getActivity().getSharedPreferences("stelleruser", MODE_PRIVATE);
//
//        Log.e("next user id", preferences.getString("empid", ""));
//        EMP_ID = preferences.getString("empid", "");
//
//
//        rcy_leadTask = view.findViewById(R.id.rcy_leadtask);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
//        LinearLayoutManager teamlinearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
//        rcy_leadTask.setLayoutManager(linearLayoutManager);
//        rcy_leadTask.setItemAnimator(new DefaultItemAnimator());
//
//        rcy_Teamlead.setLayoutManager(teamlinearLayoutManager);
//        rcy_Teamlead.setItemAnimator(new DefaultItemAnimator());
//
//
//        String key = getsha256("getAllLeadList");
//        Log.e("key", key);
//
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("action", "getAllLeadList")
//                .add("key", key)
//                .add("employee_id", EMP_ID)
//                .build();
//        Request request = new Request.Builder()
//                .url(getString(R.string.link))
//                .post(body)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, final IOException e) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        Log.e("network", e.getMessage());
//                        Toast.makeText(getActivity(), "network Problem", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                String result = response.body().string();
//
//                if (response.isSuccessful()) {
//
//                    try {
//                        JSONObject object = new JSONObject(result);
//                        Log.e("message   ", object.getString("message"));
//
//                        String msg = object.getString("message");
//                        if (msg.equals("true")) {
//
//
//                            JSONArray array = object.getJSONArray("self");
//                            JSONArray array1 = object.getJSONArray("team");
//                            JSONArray arrayChild = object.getJSONArray("child_name");
//
//                            Log.e("tamarraayy",array1.toString());
//
//                            if (array.length() > 0) {
//                                list.clear();
//
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject objectname = array.getJSONObject(i);
//
//                                    String id = objectname.getString("lead_id");
//                                    String name = objectname.getString("name");
//                                    String email = objectname.getString("email");
//                                    String expected_closing_date = objectname.getString("expected_closing_date");
//                                    String mobile = objectname.getString("mobile");
//                                    String contact_type = objectname.getString("contact_type");
//                                    String address = objectname.getString("address");
//                                    String office_phone = objectname.getString("office_phone");
//                                    String company_name = objectname.getString("company_name");
//                                    String department = objectname.getString("department");
//                                    String designation = objectname.getString("designation");
//                                    String source = objectname.getString("source");
//                                    String description = objectname.getString("description");
//                                    String type_of_requirement = objectname.getString("type_of_requirement");
//                                    String expected_lead_value = objectname.getString("expected_lead_value");
//                                    String lead_type = objectname.getString("lead_type");
//
//                                    LeadModel Homemodel = new LeadModel();
//                                    Homemodel.setId(id);
//                                    Homemodel.setName(name);
//                                    Homemodel.setDate(expected_closing_date);
//                                    Homemodel.setMobile(mobile);
//                                    Homemodel.setContacttype(contact_type);
//                                    Homemodel.setAddress(address);
//                                    Homemodel.setEmail(email);
//                                    Homemodel.setOfficephoen(office_phone);
//                                    Homemodel.setCompany_name(company_name);
//                                    Homemodel.setDepartment(department);
//                                    Homemodel.setDesignation(designation);
//                                    Homemodel.setSource(source);
//                                    Homemodel.setDescription(description);
//                                    Homemodel.setType_of_requirement(type_of_requirement);
//                                    Homemodel.setExpected_lead_value(expected_lead_value);
//                                    Homemodel.setLead_type(lead_type);
//
//                                    list.add(Homemodel);
//
//
//                                }
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        AdapterLeadList adapterLeadList = new AdapterLeadList(list);
//                                        rcy_leadTask.setAdapter(adapterLeadList);
//                                        adapterLeadList.setClickListener(new AdapterLeadList.ItemClickListener() {
//                                            @Override
//                                            public void onItemClick(View view, int position) {
//                                                if (!mpar.equals("employee")) {
//                                                    AddTargetActivity targetActivity = new AddTargetActivity();
//                                                    Bundle bundle = new Bundle();
//                                                    bundle.putString("empid", EMP_ID);
//                                                    bundle.putString("lid", list.get(position).getId());
//                                                    targetActivity.setArguments(bundle);
//                                                    loadFragment(targetActivity);
//
//
//                                                   // Toast.makeText(getContext(), "lead List" + list.get(position).getId(), Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//
//
//                                    }
//                                });
//
//                            }
//
//                            if (array1.length() > 0) {
//                                teamlist.clear();
//
//                                for (int i = 0; i < array1.length(); i++) {
//                                    JSONObject objectn = array1.getJSONObject(i);
//
//                                    String id = objectn.getString("lead_id");
//                                    String name = objectn.getString("name");
//                                    String email = objectn.getString("email");
//                                    String expected_closing_date = objectn.getString("expected_closing_date");
//                                    String mobile = objectn.getString("mobile");
//                                    String contact_type = objectn.getString("contact_type");
//                                    String address = objectn.getString("address");
//                                    String office_phone = objectn.getString("office_phone");
//                                    String company_name = objectn.getString("company_name");
//                                    String department = objectn.getString("department");
//                                    String designation = objectn.getString("designation");
//                                    String source = objectn.getString("source");
//                                    String description = objectn.getString("description");
//                                    String type_of_requirement = objectn.getString("type_of_requirement");
//                                    String expected_lead_value = objectn.getString("expected_lead_value");
//                                    String lead_type = objectn.getString("lead_type");
//
//                                    LeadModel Homemodel = new LeadModel();
//                                    Homemodel.setId(id);
//                                    Homemodel.setName(name);
//                                    Homemodel.setDate(expected_closing_date);
//                                    Homemodel.setMobile(mobile);
//                                    Homemodel.setContacttype(contact_type);
//                                    Homemodel.setAddress(address);
//                                    Homemodel.setEmail(email);
//                                    Homemodel.setOfficephoen(office_phone);
//                                    Homemodel.setCompany_name(company_name);
//                                    Homemodel.setDepartment(department);
//                                    Homemodel.setDesignation(designation);
//                                    Homemodel.setSource(source);
//                                    Homemodel.setDescription(description);
//                                    Homemodel.setType_of_requirement(type_of_requirement);
//                                    Homemodel.setExpected_lead_value(expected_lead_value);
//                                    Homemodel.setLead_type(lead_type);
//
//                                    teamlist.add(Homemodel);
//                                }
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        AdapterLeadList teamAdapter = new AdapterLeadList(teamlist);
//                                        rcy_Teamlead.setAdapter(teamAdapter);
//                                        teamAdapter.setClickListener(new AdapterLeadList.ItemClickListener() {
//                                            @Override
//                                            public void onItemClick(View view, int position) {
//                                                if (!mpar.equals("employee")) {
//
//                                                    AddTargetActivity targetActivity = new AddTargetActivity();
//                                                    Bundle bundle = new Bundle();
//                                                    bundle.putString("empid", EMP_ID);
//                                                    bundle.putString("lid", teamlist.get(position).getId());
//                                                    //  bundle.putStringArrayList("chi",chidnamelist);
//
//                                                    targetActivity.setArguments(bundle);
//                                                    loadFragment(targetActivity);
//
//
//                                                   // Toast.makeText(getContext(), "teanLead List" + teamlist.get(position).getId(), Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//
//                                    }
//                                });
//
//                            }
//
//                            if (arrayChild.length() > 0) {
//                                chidnamelist.clear();
//
//                                for (int i = 0; i < arrayChild.length(); i++) {
//                                    JSONObject objectn = arrayChild.getJSONObject(i);
//
//                                    String id = objectn.getString("employee_id");
//                                    String name = objectn.getString("full_name");
//
//                                    checkBoxModel Homemodel=new checkBoxModel();
//                                    Homemodel.setId(id);
//                                    Homemodel.setName(name);
//
//                                    chidnamelist.add(Homemodel);
//
//                                }
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        ArrayAdapter adapter=new ArrayAdapter(getActivity(),R.layout.custom_spiner,chidnamelist);
//                                        spinner.setAdapter(adapter);
//                                    }
//                                });
//
//                            }
//
//
//
//
//
//                        } else {
//                            toast("try again");
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Log.e("Sucess", result);
//
//            }
//        });
//
//
//    }

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
    private void loadFragment(android.support.v4.app.Fragment fragment) {

        FragmentTransaction transaction = null;
        if (getFragmentManager() != null) {
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fream, fragment)
                    .addToBackStack(null);
            transaction.commit();
        }

    }

}
