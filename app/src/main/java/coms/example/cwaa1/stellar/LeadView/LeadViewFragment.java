package coms.example.cwaa1.stellar.LeadView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import coms.example.cwaa1.stellar.AddTargetActivity;
import coms.example.cwaa1.stellar.DashBordActivity;
import coms.example.cwaa1.stellar.Lead_List.AdapterLeadList;
import coms.example.cwaa1.stellar.Lead_List.LeadModel;
import coms.example.cwaa1.stellar.Model.checkBoxModel;
import coms.example.cwaa1.stellar.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link LeadViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeadViewFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public LeadViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeadViewFragment.
     */
    // TODO: Rename and change types and number of parameters


    private String mpar;
    Activity activity;
    private RecyclerView rcy_leadTask;
    private SharedPreferences preferences;
    private static String EMP_ID;
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    private List<LeadModel> list = new ArrayList<>();

    private ProgressDialog progressDialog;
    private AdapterLeadList adapterLeadList = new AdapterLeadList(list);


    public static LeadViewFragment newInstance(String param1, String param2) {
        LeadViewFragment fragment = new LeadViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mpar = getArguments().getString("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_lead_view, container, false);
        DashBordActivity.menuchek(4);
//        TextView title = activity.findViewById(R.id.tvtitile);
//        title.setText("Lead View");
        DashBordActivity.titleName("Lead View");
        rcy_leadTask = view.findViewById(R.id.vrcy_leadtask);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        LinearLayoutManager teamlinearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        rcy_leadTask.setLayoutManager(linearLayoutManager);
        rcy_leadTask.setItemAnimator(new DefaultItemAnimator());

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

//
//        if (list.size() > 0) {
//            adapterset();
//        } else {
            lisfun(view);
//        }
        return view;
    }

    private void lisfun(final View view) {
        preferences = activity.getSharedPreferences("stelleruser", MODE_PRIVATE);

        Log.e("next user id", preferences.getString("empid", ""));
        EMP_ID = preferences.getString("empid", "");


        String key = getsha256("getAllLeadList");
        Log.e("key", key);
        //  progressBar.setVisibility(View.VISIBLE);
        progressDialog.show();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "getAllLeadList")
                .add("key", key)
                .add("employee_id", EMP_ID)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.e("network", e.getMessage());
                        //progressBar.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "network Problem", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();

                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        Log.e("message   ", object.getString("message"));

                        String msg = object.getString("message");
                        if (msg.equals("true")) {


                            JSONArray array = object.getJSONArray("self");
                            JSONArray array1 = object.getJSONArray("team");
                            JSONArray arrayChild = object.getJSONArray("child_name");

                            Log.e("tamarraayy", array1.toString());

                            if (array.length() > 0) {
                                list.clear();

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject objectname = array.getJSONObject(i);

                                    String id = objectname.getString("lead_id");
                                    String name = objectname.getString("name");
                                    String email = objectname.getString("email");
                                    String expected_closing_date = objectname.getString("expected_closing_date");
                                    String mobile = objectname.getString("mobile");
                                    String contact_type = objectname.getString("contact_type");
                                    String address = objectname.getString("address");
                                    String office_phone = objectname.getString("office_phone");
                                    String company_name = objectname.getString("company_name");
                                    String department = objectname.getString("department");
                                    String designation = objectname.getString("designation");
                                    String source = objectname.getString("sourceName");
                                    String description = objectname.getString("description");
                                    String lead_type = objectname.getString("lead_type");

                                    String type_of_requirement = objectname.getString("requirementName");
                                    String expected_lead_value = objectname.getString("expected_lead_value");
                                    String to_time = objectname.getString("to_time");
                                    String from_time = objectname.getString("from_time");
                                    String category = objectname.getString("categoryName");


                                    LeadModel model = new LeadModel();

                                    model.setCompany_name(company_name);

                                    model.setTotime(to_time);
                                    model.setFormtime(from_time);
                                    model.setCategory(category);
                                    model.setType_of_requirement(type_of_requirement);
                                    model.setExpected_lead_value(expected_lead_value);


                                    model.setId(id);
                                    model.setName(name);
                                    model.setDate(expected_closing_date);
                                    model.setMobile(mobile);
                                    model.setContacttype(contact_type);
                                    model.setAddress(address);
                                    model.setEmail(email);
                                    model.setOfficephoen(office_phone);
                                    model.setDepartment(department);
                                    model.setDesignation(designation);
                                    model.setSource(source);
                                    model.setDescription(description);
                                    model.setLead_type(lead_type);


                                    list.add(model);


                                }
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapterset();
                                        Log.e("Api Runing", "here");


                                        adapterLeadList.setClickListener(new AdapterLeadList.ItemClickListener() {
                                            @Override
                                            public void onItemClick(int position) {

                                                DetailsFragment detailsFragment = new DetailsFragment();
                                                Bundle bundle = new Bundle();


                                                bundle.putString("id", list.get(position).getId());
                                                bundle.putString("name", list.get(position).getName());
                                                bundle.putString("email", list.get(position).getEmail());
                                                bundle.putString("date", list.get(position).getDate());
                                                bundle.putString("mob", list.get(position).getMobile());
                                                bundle.putString("conty", list.get(position).getContacttype());
                                                bundle.putString("add", list.get(position).getAddress());
                                                bundle.putString("offno", list.get(position).getOfficephoen());
                                                bundle.putString("comname", list.get(position).getCompany_name());
                                                bundle.putString("dep", list.get(position).getDepartment());
                                                bundle.putString("desig", list.get(position).getDesignation());
                                                bundle.putString("souce", list.get(position).getSource());
                                                bundle.putString("descrip", list.get(position).getDescription());
                                                bundle.putString("tor", list.get(position).getType_of_requirement());
                                                bundle.putString("lvalue", list.get(position).getExpected_lead_value());
                                                bundle.putString("ltype", list.get(position).getLead_type());

                                                bundle.putString("source", list.get(position).getSource());
                                                bundle.putString("cat", list.get(position).getCategory());
                                                bundle.putString("toti", list.get(position).getTotime());
                                                bundle.putString("fti", list.get(position).getFormtime());
                                                bundle.putInt("twin",1 );

                                                detailsFragment.setArguments(bundle);
                                                loadFragment(detailsFragment);


                                            }

                                            @Override
                                            public void onFollowup(int i) {
                                                Log.e("lead iddd", list.get(i).getId());
                                                final String leadid = list.get(i).getId();

                                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                // Get the layout inflater
                                                LayoutInflater inflater = activity.getLayoutInflater();
                                                // Inflate and set the layout for the dialog
                                                // Pass null as the parent view because its going in the dialog layout
                                                final View view1 = inflater.inflate(R.layout.item_followup, null);
                                                builder.setView(view1);
                                                final AlertDialog dialog = builder.create();

                                                final TextView date = view1.findViewById(R.id.et_date);
                                                final Spinner spinner = view1.findViewById(R.id.sp_res);
                                                final EditText comm = view1.findViewById(R.id.fcomm);
                                                Button save = view1.findViewById(R.id.fsave);
                                                final ProgressBar pbar = view1.findViewById(R.id.progg);
                                                pbar.setVisibility(View.GONE);

                                                save.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        String sp = spinner.getSelectedItem().toString();
                                                        String com = comm.getText().toString();
                                                        String d = date.getText().toString();
                                                        if (d.equals("")) {
                                                            date.setError("is Empty");
                                                        } else if (com.equals("")) {
                                                            comm.setError("Is Empty");
                                                        } else {
                                                            pbar.setVisibility(View.VISIBLE);
                                                            followapi(sp, com, d, leadid, dialog, pbar);
                                                        }
                                                    }
                                                });


                                                date.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        int mYear, mMonth, mDay, mHour, mMinute;

                                                        final Calendar c = Calendar.getInstance();
                                                        mYear = c.get(Calendar.YEAR);
                                                        mMonth = c.get(Calendar.MONTH);
                                                        mDay = c.get(Calendar.DAY_OF_MONTH);


                                                        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                                                new DatePickerDialog.OnDateSetListener() {

                                                                    @Override
                                                                    public void onDateSet(DatePicker view, int year,
                                                                                          int monthOfYear, int dayOfMonth) {
                                                                        date.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + "");


                                                                    }
                                                                }, mYear, mMonth, mDay);
                                                        datePickerDialog.show();

                                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);  //this for past date not clickbale


                                                    }
                                                });
                                                view1.findViewById(R.id.fcancle).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.cancel();
                                                    }
                                                });
                                                dialog.show();
                                            }

                                            @Override
                                            public void onCall(int i) {

                                                String no = list.get(i).getMobile();

                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:" + no));
                                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 10);
                                                } else {
                                                    startActivity(callIntent);
                                                }
                                            }
                                        });

                                    }
                                });

                            } else {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(activity, "No lead Yet", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }


                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                }
                            });
                            toast("try again");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.e("Sucess", result);

            }
        });


    }

    private void adapterset() {
        rcy_leadTask.setAdapter(adapterLeadList);
        progressDialog.dismiss();
    }

    private void followapi(String sp, String com, String d, String leadid, final AlertDialog dialog, final ProgressBar pbar) {


        String key = getsha256("lead_followUp");
        Log.e("key", key);
        pbar.setVisibility(View.VISIBLE);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "lead_followUp")
                .add("key", key)
                .add("lead_id", leadid)
                .add("response", sp)
                .add("next_followUp_date", d)
                .add("followUp_comment", com)
                .add("followup_by_emp", EMP_ID)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.e("network", e.getMessage());
                        pbar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "network Problem", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();

                Log.e("Folw responce", result);
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        Log.e("message   ", object.getString("message"));

                        String msg = object.getString("message");
                        if (msg.equals("lead Update Successfully")) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    pbar.setVisibility(View.GONE);
                                    dialog.cancel();
                                    Toast.makeText(getActivity(), "Sucess", Toast.LENGTH_SHORT).show();
                                }
                            });


                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    pbar.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });
                        }
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

        activity.runOnUiThread(new Runnable() {
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;

    }
}
