package com.stellar.android.app.Task_List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.stellar.android.app.AddNew_LeadActivity;
import com.stellar.android.app.DashBordActivity;
import com.stellar.android.app.R;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class ListOfTaskActivity extends Fragment {
    private final String TAG = "ListOfTaskActivity";
    private Activity acti;
    private SharedPreferences preferences;
    private static String EMP_ID;
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    private TextView sun, mon, tue, wed, thu, fri, sat;
    public static String DAY_NAME = "sunday";

    private LinearLayout linearLayout;
    private ArrayList<ListModel> phoneList;
    private ArrayList<ListModel> MailList;
    private ArrayList<ListModel> VisitList;

    private RecyclerView recyclerView;
    private RecyclerView recyclerView_phone;
    private RecyclerView recyclerView_mail;
    private RecyclerView recyclerView_visit;

    private AdapterList adapterListphone;
    private AdapterList adapterListmail;
    private AdapterList adapterListvist;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list_of_task, container, false);
        preferences = acti.getSharedPreferences("stelleruser", MODE_PRIVATE);
        EMP_ID = preferences.getString("empid", "");

        DashBordActivity.menuchek(3);
        DashBordActivity.titleName("Task List");
        Tabid(view);




        Log.e("next user id", preferences.getString("empid", ""));
        phoneList = new ArrayList<>();
        MailList = new ArrayList<>();
        VisitList = new ArrayList<>();
        progressDialog = new ProgressDialog(acti);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);

        recyclerView_phone = view.findViewById(R.id.list_recycler);
        recyclerView_mail = view.findViewById(R.id.rec_mail);
        recyclerView_visit = view.findViewById(R.id.rec_visit);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL, false);
        LinearLayoutManager linearLayoutManagernma = new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL, false);
        LinearLayoutManager linearLayoutManagernvisit = new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL, false);

        recyclerView_phone.setLayoutManager(linearLayoutManager);
        recyclerView_phone.setItemAnimator(new DefaultItemAnimator());
        recyclerView_phone.setNestedScrollingEnabled(false);

        recyclerView_mail.setLayoutManager(linearLayoutManagernma);
        recyclerView_mail.setItemAnimator(new DefaultItemAnimator());
        recyclerView_mail.setNestedScrollingEnabled(false);

        recyclerView_visit.setLayoutManager(linearLayoutManagernvisit);
        recyclerView_visit.setItemAnimator(new DefaultItemAnimator());
        recyclerView_visit.setNestedScrollingEnabled(false);


        if (DAY_NAME.equals("")) {
            toast("Select Day First");
        } else {
            api();
        }

        if (getArguments() != null && getArguments().getString("day") != null) {
            Log.e("Bord Msg ", "IS here " + getArguments().getString("taskname"));
            String dayy = getArguments().getString("day");
            setdayByNotification(dayy);
        }

        return view;
    }

    private void setdayByNotification(String dayy) {

        if (dayy.equalsIgnoreCase("monday")) {
            mon.setBackgroundColor(Color.WHITE);
            mon.setTextColor(Color.parseColor("#58BDC7"));

            sun.setBackgroundColor(Color.parseColor("#58BDC7"));
            tue.setBackgroundColor(Color.parseColor("#58BDC7"));
            wed.setBackgroundColor(Color.parseColor("#58BDC7"));
            thu.setBackgroundColor(Color.parseColor("#58BDC7"));
            fri.setBackgroundColor(Color.parseColor("#58BDC7"));
            sat.setBackgroundColor(Color.parseColor("#58BDC7"));
            sun.setTextColor(Color.WHITE);
            tue.setTextColor(Color.WHITE);
            wed.setTextColor(Color.WHITE);
            thu.setTextColor(Color.WHITE);
            fri.setTextColor(Color.WHITE);
            sat.setTextColor(Color.WHITE);



            DAY_NAME = "monday";

            api();
        }
        if (dayy.equalsIgnoreCase("Tuesday")) {
            tue.setBackgroundColor(Color.WHITE);
            tue.setTextColor(Color.parseColor("#58BDC7"));
            DAY_NAME = "tuesday";


            mon.setBackgroundColor(Color.parseColor("#58BDC7"));
            sun.setBackgroundColor(Color.parseColor("#58BDC7"));
            wed.setBackgroundColor(Color.parseColor("#58BDC7"));
            thu.setBackgroundColor(Color.parseColor("#58BDC7"));
            fri.setBackgroundColor(Color.parseColor("#58BDC7"));
            sat.setBackgroundColor(Color.parseColor("#58BDC7"));
            mon.setTextColor(Color.WHITE);
            sun.setTextColor(Color.WHITE);
            wed.setTextColor(Color.WHITE);
            thu.setTextColor(Color.WHITE);
            fri.setTextColor(Color.WHITE);
            sat.setTextColor(Color.WHITE);


            api();
        }
        if (dayy.equalsIgnoreCase("Wednesday")) {
            wed.setBackgroundColor(Color.WHITE);
            wed.setTextColor(Color.parseColor("#58BDC7"));
            DAY_NAME = "wednesday";

            mon.setBackgroundColor(Color.parseColor("#58BDC7"));
            tue.setBackgroundColor(Color.parseColor("#58BDC7"));
            sun.setBackgroundColor(Color.parseColor("#58BDC7"));
            thu.setBackgroundColor(Color.parseColor("#58BDC7"));
            fri.setBackgroundColor(Color.parseColor("#58BDC7"));
            sat.setBackgroundColor(Color.parseColor("#58BDC7"));
            mon.setTextColor(Color.WHITE);
            tue.setTextColor(Color.WHITE);
            sun.setTextColor(Color.WHITE);
            thu.setTextColor(Color.WHITE);
            fri.setTextColor(Color.WHITE);
            sat.setTextColor(Color.WHITE);



            api();
        }
        if (dayy.equalsIgnoreCase("Thursday")) {
            thu.setBackgroundColor(Color.WHITE);
            thu.setTextColor(Color.parseColor("#58BDC7"));
            DAY_NAME = "thursday";

            mon.setBackgroundColor(Color.parseColor("#58BDC7"));
            tue.setBackgroundColor(Color.parseColor("#58BDC7"));
            wed.setBackgroundColor(Color.parseColor("#58BDC7"));
            sun.setBackgroundColor(Color.parseColor("#58BDC7"));
            fri.setBackgroundColor(Color.parseColor("#58BDC7"));
            sat.setBackgroundColor(Color.parseColor("#58BDC7"));
            mon.setTextColor(Color.WHITE);
            tue.setTextColor(Color.WHITE);
            wed.setTextColor(Color.WHITE);
            sun.setTextColor(Color.WHITE);
            fri.setTextColor(Color.WHITE);
            sat.setTextColor(Color.WHITE);


            api();
        }
        if (dayy.equalsIgnoreCase("Friday")) {
            fri.setBackgroundColor(Color.WHITE);
            fri.setTextColor(Color.parseColor("#58BDC7"));
            DAY_NAME = "friday";
            mon.setBackgroundColor(Color.parseColor("#58BDC7"));
            tue.setBackgroundColor(Color.parseColor("#58BDC7"));
            wed.setBackgroundColor(Color.parseColor("#58BDC7"));
            thu.setBackgroundColor(Color.parseColor("#58BDC7"));
            sun.setBackgroundColor(Color.parseColor("#58BDC7"));
            sat.setBackgroundColor(Color.parseColor("#58BDC7"));
            mon.setTextColor(Color.WHITE);
            tue.setTextColor(Color.WHITE);
            wed.setTextColor(Color.WHITE);
            thu.setTextColor(Color.WHITE);
            sun.setTextColor(Color.WHITE);
            sat.setTextColor(Color.WHITE);


            api();
        }
        if (dayy.equalsIgnoreCase("Saturday")) {
            sat.setBackgroundColor(Color.WHITE);
            sat.setTextColor(Color.parseColor("#58BDC7"));
            DAY_NAME = "saturday";

            mon.setBackgroundColor(Color.parseColor("#58BDC7"));
            tue.setBackgroundColor(Color.parseColor("#58BDC7"));
            wed.setBackgroundColor(Color.parseColor("#58BDC7"));
            thu.setBackgroundColor(Color.parseColor("#58BDC7"));
            fri.setBackgroundColor(Color.parseColor("#58BDC7"));
            sun.setBackgroundColor(Color.parseColor("#58BDC7"));
            mon.setTextColor(Color.WHITE);
            tue.setTextColor(Color.WHITE);
            wed.setTextColor(Color.WHITE);
            thu.setTextColor(Color.WHITE);
            fri.setTextColor(Color.WHITE);
            sun.setTextColor(Color.WHITE);

            api();
        }
        if (dayy.equalsIgnoreCase("Sunday")) {
            sun.setBackgroundColor(Color.WHITE);
            sun.setTextColor(Color.parseColor("#58BDC7"));
            DAY_NAME = "sunday";

            mon.setBackgroundColor(Color.parseColor("#58BDC7"));
            tue.setBackgroundColor(Color.parseColor("#58BDC7"));
            wed.setBackgroundColor(Color.parseColor("#58BDC7"));
            thu.setBackgroundColor(Color.parseColor("#58BDC7"));
            fri.setBackgroundColor(Color.parseColor("#58BDC7"));
            sat.setBackgroundColor(Color.parseColor("#58BDC7"));
            mon.setTextColor(Color.WHITE);
            tue.setTextColor(Color.WHITE);
            wed.setTextColor(Color.WHITE);
            thu.setTextColor(Color.WHITE);
            fri.setTextColor(Color.WHITE);
            sat.setTextColor(Color.WHITE);



            api();
        }
    }

    private void api() {


        progressDialog.show();
        String key = getsha256("getAllTaskList");
        Log.e(TAG + " APi", key);


        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "getAllTaskList")
                .add("key", key)
                .add("employee_id", EMP_ID)
                .add("day", DAY_NAME)
                .build();
        final Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                acti.runOnUiThread(() -> {
                    progressDialog.dismiss();
                    toast("Network Problem");

                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Log.e(TAG + "Api", " Result " + result);
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equals("true")) {


                            JSONArray arraymail = object.getJSONArray("Emaildata");
                            JSONArray arrayphone = object.getJSONArray("phonedata");
                            JSONArray arrayvisit = object.getJSONArray("visitdata");
                            MailList.clear();
                            if (arraymail.length() > 0) {

                                for (int i = 0; i < arraymail.length(); i++) {
                                    JSONObject objectmail = arraymail.getJSONObject(i);

                                    String com_name = objectmail.getString("customer_name");
                                    String snma = objectmail.getString("plan_type");

                                    ListModel model = new ListModel();
                                    model.setId(objectmail.getString("id"));
                                    model.setDay(objectmail.getString("day"));
                                    model.setPlan_type(objectmail.getString("plan_type"));
                                    model.setCustomer_name(com_name);
                                    model.setMobile(objectmail.getString("mobile"));
                                    model.setState(objectmail.getString("state"));
                                    model.setCity(objectmail.getString("city"));
                                    model.setAddress(objectmail.getString("address"));
                                    model.setDetails(objectmail.getString("details"));
                                    model.setTask_status(objectmail.getString("task_status"));
                                    model.setCreatedateandtime(objectmail.getString("createdateandtime"));

                                    model.setCategory(objectmail.getString("categoryName"));
                                    model.setCompanyName(objectmail.getString("company_name"));
                                    model.setPurpose(objectmail.getString("purpose"));
                                    model.setFormtime(objectmail.getString("for_time"));
                                    model.setTotime(objectmail.getString("to_time"));
                                    model.setTypeofRequest(objectmail.getString("requirementName"));
                                    model.setSource(objectmail.getString("sourceName"));

                                    MailList.add(model);
                                }

                                acti.runOnUiThread(() -> {

                                    adapterListmail = new AdapterList(MailList);
                                    recyclerView_mail.setAdapter(adapterListmail);
                                    adapterListmail.notifyDataSetChanged();

                                    adapterListmail.setClickListener(new AdapterList.ItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {


                                            //   Toast.makeText(ComplainListActivity.this, ""+arrayList.get(position).getDate().split(" ")[0], Toast.LENGTH_SHORT).show();

                                            ListModel model = MailList.get(position);

                                            String taskid = model.getId();

                                            Log.e("List of Tassk " + " mail id", taskid);

                                            requestforTask(taskid, EMP_ID, position, 1);

                                        }

                                        @Override
                                        public void onleadAdd(int position) {
                                            ListModel model = MailList.get(position);
                                            AddNew_LeadActivity leadActivity = new AddNew_LeadActivity();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("id", model.getId());
                                            bundle.putString("tname", model.getCustomer_name());
                                            bundle.putString("tmobile", model.getMobile());
                                            bundle.putString("tadd", model.getAddress());
                                            bundle.putString("tdetais", model.getDetails());

                                            bundle.putString("ttype", model.getPlan_type());
                                            bundle.putString("tcompname", model.getCompanyName());
                                            bundle.putString("tpurpose", model.getPurpose());
                                            bundle.putString("tcategory", model.getCategory());
                                            bundle.putString("tsource", model.getSource());
                                            bundle.putString("ttreq", model.getTypeofRequest());

                                            bundle.putString("ttime", model.getTotime());
                                            bundle.putString("tftime", model.getFormtime());

                                            Log.e("list tt : ft", " dd " + model.getTotime() + " : " + model.getFormtime());
                                            leadActivity.setArguments(bundle);
                                            loadFragment(leadActivity);

                                            // Toast.makeText(acti, "add lead click", Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onLongClick(int position) {
                                            changeDay(MailList.get(position).getId());
                                        }
                                    });

                                });


                            } else {
                                acti.runOnUiThread(() -> {

                                    MailList.clear();
                                    adapterListmail = new AdapterList(MailList);
                                    recyclerView_mail.setAdapter(adapterListmail);
                                    adapterListmail.notifyDataSetChanged();
                                    //  toast("not fond data");


                                });
                            }

                            phoneList.clear();

                            if (arrayphone.length() > 0) {

                                for (int i = 0; i < arrayphone.length(); i++) {
                                    JSONObject objectphone = arrayphone.getJSONObject(i);

                                    String com_name = objectphone.getString("customer_name");
                                    String snma = objectphone.getString("plan_type");

                                    ListModel model = new ListModel();
                                    model.setId(objectphone.getString("id"));
                                    model.setDay(objectphone.getString("day"));
                                    model.setPlan_type(objectphone.getString("plan_type"));
                                    model.setCustomer_name(com_name);
                                    model.setMobile(objectphone.getString("mobile"));
                                    model.setState(objectphone.getString("state"));
                                    model.setCity(objectphone.getString("city"));
                                    model.setAddress(objectphone.getString("address"));
                                    model.setDetails(objectphone.getString("details"));
                                    model.setTask_status(objectphone.getString("task_status"));
                                    model.setCreatedateandtime(objectphone.getString("createdateandtime"));


                                    model.setCategory(objectphone.getString("categoryName"));
                                    model.setCompanyName(objectphone.getString("company_name"));
                                    model.setPurpose(objectphone.getString("purpose"));
                                    model.setFormtime(objectphone.getString("for_time"));
                                    model.setTotime(objectphone.getString("to_time"));
                                    model.setTypeofRequest(objectphone.getString("requirementName"));
                                    model.setSource(objectphone.getString("sourceName"));


                                    Log.e("List of Tassk " + " phone id", com_name);
                                    phoneList.add(model);

                                }
                                acti.runOnUiThread(() -> {

                                    adapterListphone = new AdapterList(phoneList);
                                    recyclerView_phone.setAdapter(adapterListphone);
                                    adapterListphone.notifyDataSetChanged();


                                    adapterListphone.setClickListener(new AdapterList.ItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {


                                            //   Toast.makeText(ComplainListActivity.this, ""+arrayList.get(position).getDate().split(" ")[0], Toast.LENGTH_SHORT).show();

                                            ListModel model = phoneList.get(position);

                                            String taskid = model.getId();

                                            Log.e("List of task " + " phone", phoneList.get(position).getAddress());
                                            requestforTask(taskid, EMP_ID, position, 2);

                                            // adapterListphone.notifyDataSetChanged();

                                        }

                                        @Override
                                        public void onleadAdd(int position) {
                                            ListModel model = phoneList.get(position);
                                            AddNew_LeadActivity leadActivity = new AddNew_LeadActivity();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("id", model.getId());
                                            bundle.putString("tname", model.getCustomer_name());
                                            bundle.putString("tmobile", model.getMobile());
                                            bundle.putString("tadd", model.getAddress());
                                            bundle.putString("tdetais", model.getDetails());
                                            bundle.putString("ttype", model.getPlan_type());
                                            bundle.putString("tcompname", model.getCompanyName());
                                            bundle.putString("tpurpose", model.getPurpose());
                                            bundle.putString("tcategory", model.getCategory());
                                            bundle.putString("tsource", model.getSource());
                                            bundle.putString("ttreq", model.getTypeofRequest());
                                            bundle.putString("ttime", model.getTotime());
                                            bundle.putString("tftime", model.getFormtime());

                                            Log.e("list tt : ft", " dd " + model.getTotime() + " : " + model.getFormtime());
                                            leadActivity.setArguments(bundle);
                                            loadFragment(leadActivity);
                                            Toast.makeText(acti, "add lead click", Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onLongClick(int position) {
                                            changeDay(phoneList.get(position).getId());
                                        }

                                    });


                                });


                            } else {
                                acti.runOnUiThread(() -> {

                                    phoneList.clear();
                                    adapterListphone = new AdapterList(phoneList);
                                    recyclerView_phone.setAdapter(adapterListphone);
                                    adapterListphone.notifyDataSetChanged();
                                    // toast("not fond data");

                                });
                            }
                            VisitList.clear();
                            if (arrayvisit.length() > 0) {

                                for (int i = 0; i < arrayvisit.length(); i++) {
                                    JSONObject objectvisit = arrayvisit.getJSONObject(i);

                                    String com_name = objectvisit.getString("customer_name");
                                    String snma = objectvisit.getString("plan_type");

                                    ListModel model = new ListModel();
                                    model.setId(objectvisit.getString("id"));
                                    model.setDay(objectvisit.getString("day"));
                                    model.setPlan_type(objectvisit.getString("plan_type"));
                                    model.setCustomer_name(com_name);
                                    model.setMobile(objectvisit.getString("mobile"));
                                    model.setState(objectvisit.getString("state"));
                                    model.setCity(objectvisit.getString("city"));
                                    model.setAddress(objectvisit.getString("address"));
                                    model.setDetails(objectvisit.getString("details"));
                                    model.setTask_status(objectvisit.getString("task_status"));
                                    model.setCreatedateandtime(objectvisit.getString("createdateandtime"));


                                    model.setCategory(objectvisit.getString("categoryName"));
                                    model.setCompanyName(objectvisit.getString("company_name"));
                                    model.setPurpose(objectvisit.getString("purpose"));
                                    model.setFormtime(objectvisit.getString("for_time"));
                                    model.setTotime(objectvisit.getString("to_time"));
                                    model.setTypeofRequest(objectvisit.getString("requirementName"));
                                    model.setSource(objectvisit.getString("sourceName"));


                                    // Log.e("emp_name", com_name);

                                    VisitList.add(model);

                                }

                                acti.runOnUiThread(() -> {

                                    adapterListvist = new AdapterList(VisitList);
                                    recyclerView_visit.setAdapter(adapterListvist);
                                    adapterListvist.notifyDataSetChanged();
                                    dialogclose();

                                    recyclerView_visit.setOnLongClickListener(v -> false);
                                    adapterListvist.setClickListener(new AdapterList.ItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {

                                            view.findViewById(R.id.img_req).setVisibility(View.GONE);

                                            ListModel model = VisitList.get(position);

                                            String taskid = model.getId();

                                            requestforTask(taskid, EMP_ID, position, 3);

                                            //   adapterListvist.notifyDataSetChanged();


                                            Log.e("clicking", VisitList.get(position).toString());
                                        }

                                        @Override
                                        public void onleadAdd(int position) {
                                            ListModel model = VisitList.get(position);
                                            AddNew_LeadActivity leadActivity = new AddNew_LeadActivity();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("id", model.getId());
                                            bundle.putString("tname", model.getCustomer_name());
                                            bundle.putString("tmobile", model.getMobile());
                                            bundle.putString("tadd", model.getAddress());
                                            bundle.putString("tdetais", model.getDetails());

                                            bundle.putString("ttype", model.getPlan_type());
                                            bundle.putString("tcompname", model.getCompanyName());
                                            bundle.putString("tpurpose", model.getPurpose());
                                            bundle.putString("tcategory", model.getCategory());
                                            bundle.putString("tsource", model.getSource());
                                            bundle.putString("ttreq", model.getTypeofRequest());

                                            bundle.putString("ttime", model.getTotime());
                                            bundle.putString("tftime", model.getFormtime());
                                            Log.e("list tt : ft", " dd " + model.getTotime() + " : " + model.getFormtime());

                                            leadActivity.setArguments(bundle);
                                            loadFragment(leadActivity);
                                            Toast.makeText(acti, "add lead click " + model.getCustomer_name(), Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onLongClick(int position) {
                                            changeDay(VisitList.get(position).getId());

                                            Toast.makeText(acti, "clik", Toast.LENGTH_SHORT).show();

                                        }

                                    });


                                });

                            } else {
                                acti.runOnUiThread(() -> {

                                    VisitList.clear();
                                    adapterListvist = new AdapterList(VisitList);
                                    recyclerView_visit.setAdapter(adapterListvist);
                                    adapterListvist.notifyDataSetChanged();
                                    dialogclose();
                                });
                            }
                        } else {

                            acti.runOnUiThread(() -> {

                                MailList.clear();
                                adapterListmail = new AdapterList(MailList);
                                recyclerView_mail.setAdapter(adapterListmail);
                                adapterListmail.notifyDataSetChanged();


                                phoneList.clear();
                                adapterListphone = new AdapterList(phoneList);
                                recyclerView_phone.setAdapter(adapterListphone);
                                adapterListphone.notifyDataSetChanged();

                                VisitList.clear();
                                adapterListvist = new AdapterList(VisitList);
                                recyclerView_visit.setAdapter(adapterListvist);
                                adapterListvist.notifyDataSetChanged();
                                dialogclose();

                            });

                        }


                    } catch (JSONException e) {

                        e.printStackTrace();
                        dialogclose();
                    }
                } else {
                    dialogclose();
                }
                Log.e("Phone  Sucess", result);


            }
        });


    }

    private void dialogclose() {
        acti.runOnUiThread(() -> progressDialog.dismiss());

    }

    private void requestforTask(String taskid, String empId, final int position, final int i) {

        progressDialog.show();
        String key = getsha256("taskapproverequestbyparent");
        Log.e("key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "taskapproverequestbyparent")
                .add("key", key)
                .add("employee_id", EMP_ID)
                .add("plan_task_id", taskid)
                .build();
        final Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                acti.runOnUiThread(() -> {
                    progressDialog.dismiss();
                    toast("Network Problem");
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Log.e("result ", result);
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equals("true")) {
                            acti.runOnUiThread(() -> api());
                            toast("Request Send");
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }
        });


    }

    private void changeDay(final String id) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(acti);
        LayoutInflater inflater = acti.getLayoutInflater();
        final View dview = inflater.inflate(R.layout.item_day_task, null);
        builder.setView(dview);
        final AlertDialog dialog = builder.create();
        final Spinner spinner = dview.findViewById(R.id.sp_weekday);
        final Button savesp = dview.findViewById(R.id.btn_day_save);
        dview.findViewById(R.id.li_leav).setVisibility(View.GONE);

        savesp.setOnClickListener(v -> {
            String sp = spinner.getSelectedItem().toString().trim();

            progressDialog.show();

            String key = getsha256("taskreschedule");
            Log.e("weekDay Key", key);

            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("action", "taskreschedule")
                    .add("key", key)
                    .add("task_id", id)
                    .add("task_day", sp)
                    .build();
            final Request request = new Request.Builder()
                    .url(getString(R.string.link))
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    acti.runOnUiThread(() -> {
                        progressDialog.dismiss();
                        toast("Network Problem");
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {


                    String result = response.body().string();
                    Log.e("result ", result);
                    if (response.isSuccessful()) {


                        try {
                            JSONObject object = new JSONObject(result);
                            String msg = object.getString("message");
                            if (msg.equals("true")) {
                                acti.runOnUiThread(() -> {
                                    progressDialog.dismiss();
                                    api();
                                    dialog.cancel();
                                });
                            } else {
                                acti.runOnUiThread(() -> progressDialog.dismiss());
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else {
                        acti.runOnUiThread(() -> progressDialog.dismiss());
                    }

                }
            });
        });
        dialog.show();

    }


    private void Tabid(View view) {
        linearLayout = view.findViewById(R.id.ltab);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        Log.e("Dayyyyyyyyy", dayOfTheWeek);

        sun = view.findViewById(R.id.tab_lsun);
        mon = view.findViewById(R.id.tab_lmon);
        tue = view.findViewById(R.id.tab_ltue);
        wed = view.findViewById(R.id.tab_lwed);
        thu = view.findViewById(R.id.tab_lthu);
        fri = view.findViewById(R.id.tab_lfri);
        sat = view.findViewById(R.id.tab_lsat);

        setday(dayOfTheWeek);
        sun.setOnClickListener(v -> {
            mon.setBackgroundColor(Color.parseColor("#58BDC7"));
            tue.setBackgroundColor(Color.parseColor("#58BDC7"));
            wed.setBackgroundColor(Color.parseColor("#58BDC7"));
            thu.setBackgroundColor(Color.parseColor("#58BDC7"));
            fri.setBackgroundColor(Color.parseColor("#58BDC7"));
            sat.setBackgroundColor(Color.parseColor("#58BDC7"));
            mon.setTextColor(Color.WHITE);
            tue.setTextColor(Color.WHITE);
            wed.setTextColor(Color.WHITE);
            thu.setTextColor(Color.WHITE);
            fri.setTextColor(Color.WHITE);
            sat.setTextColor(Color.WHITE);

            sun.setBackgroundColor(Color.WHITE);
            sun.setTextColor(Color.parseColor("#58BDC7"));


            DAY_NAME = "sunday";
            api();
        });
        mon.setOnClickListener(v -> {
            sun.setBackgroundColor(Color.parseColor("#58BDC7"));
            tue.setBackgroundColor(Color.parseColor("#58BDC7"));
            wed.setBackgroundColor(Color.parseColor("#58BDC7"));
            thu.setBackgroundColor(Color.parseColor("#58BDC7"));
            fri.setBackgroundColor(Color.parseColor("#58BDC7"));
            sat.setBackgroundColor(Color.parseColor("#58BDC7"));
            sun.setTextColor(Color.WHITE);
            tue.setTextColor(Color.WHITE);
            wed.setTextColor(Color.WHITE);
            thu.setTextColor(Color.WHITE);
            fri.setTextColor(Color.WHITE);
            sat.setTextColor(Color.WHITE);

            mon.setBackgroundColor(Color.WHITE);
            mon.setTextColor(Color.parseColor("#58BDC7"));
            DAY_NAME = "monday";
            api();
        });
        tue.setOnClickListener(v -> {

            mon.setBackgroundColor(Color.parseColor("#58BDC7"));
            sun.setBackgroundColor(Color.parseColor("#58BDC7"));
            wed.setBackgroundColor(Color.parseColor("#58BDC7"));
            thu.setBackgroundColor(Color.parseColor("#58BDC7"));
            fri.setBackgroundColor(Color.parseColor("#58BDC7"));
            sat.setBackgroundColor(Color.parseColor("#58BDC7"));
            mon.setTextColor(Color.WHITE);
            sun.setTextColor(Color.WHITE);
            wed.setTextColor(Color.WHITE);
            thu.setTextColor(Color.WHITE);
            fri.setTextColor(Color.WHITE);
            sat.setTextColor(Color.WHITE);


            tue.setBackgroundColor(Color.WHITE);
            tue.setTextColor(Color.parseColor("#58BDC7"));
            DAY_NAME = "tuesday";
            api();
        });
        wed.setOnClickListener(v -> {
            mon.setBackgroundColor(Color.parseColor("#58BDC7"));
            tue.setBackgroundColor(Color.parseColor("#58BDC7"));
            sun.setBackgroundColor(Color.parseColor("#58BDC7"));
            thu.setBackgroundColor(Color.parseColor("#58BDC7"));
            fri.setBackgroundColor(Color.parseColor("#58BDC7"));
            sat.setBackgroundColor(Color.parseColor("#58BDC7"));
            mon.setTextColor(Color.WHITE);
            tue.setTextColor(Color.WHITE);
            sun.setTextColor(Color.WHITE);
            thu.setTextColor(Color.WHITE);
            fri.setTextColor(Color.WHITE);
            sat.setTextColor(Color.WHITE);

            wed.setBackgroundColor(Color.WHITE);
            wed.setTextColor(Color.parseColor("#58BDC7"));
            DAY_NAME = "wednesday";
            api();

        });
        thu.setOnClickListener(v -> {
            mon.setBackgroundColor(Color.parseColor("#58BDC7"));
            tue.setBackgroundColor(Color.parseColor("#58BDC7"));
            wed.setBackgroundColor(Color.parseColor("#58BDC7"));
            sun.setBackgroundColor(Color.parseColor("#58BDC7"));
            fri.setBackgroundColor(Color.parseColor("#58BDC7"));
            sat.setBackgroundColor(Color.parseColor("#58BDC7"));
            mon.setTextColor(Color.WHITE);
            tue.setTextColor(Color.WHITE);
            wed.setTextColor(Color.WHITE);
            sun.setTextColor(Color.WHITE);
            fri.setTextColor(Color.WHITE);
            sat.setTextColor(Color.WHITE);

            thu.setBackgroundColor(Color.WHITE);
            thu.setTextColor(Color.parseColor("#58BDC7"));
            DAY_NAME = "thursday";
            api();

        });
        fri.setOnClickListener(v -> {
            mon.setBackgroundColor(Color.parseColor("#58BDC7"));
            tue.setBackgroundColor(Color.parseColor("#58BDC7"));
            wed.setBackgroundColor(Color.parseColor("#58BDC7"));
            thu.setBackgroundColor(Color.parseColor("#58BDC7"));
            sun.setBackgroundColor(Color.parseColor("#58BDC7"));
            sat.setBackgroundColor(Color.parseColor("#58BDC7"));
            mon.setTextColor(Color.WHITE);
            tue.setTextColor(Color.WHITE);
            wed.setTextColor(Color.WHITE);
            thu.setTextColor(Color.WHITE);
            sun.setTextColor(Color.WHITE);
            sat.setTextColor(Color.WHITE);

            fri.setBackgroundColor(Color.WHITE);
            fri.setTextColor(Color.parseColor("#58BDC7"));
            DAY_NAME = "friday";
            api();
        });
        sat.setOnClickListener(v -> {
            mon.setBackgroundColor(Color.parseColor("#58BDC7"));
            tue.setBackgroundColor(Color.parseColor("#58BDC7"));
            wed.setBackgroundColor(Color.parseColor("#58BDC7"));
            thu.setBackgroundColor(Color.parseColor("#58BDC7"));
            fri.setBackgroundColor(Color.parseColor("#58BDC7"));
            sun.setBackgroundColor(Color.parseColor("#58BDC7"));
            mon.setTextColor(Color.WHITE);
            tue.setTextColor(Color.WHITE);
            wed.setTextColor(Color.WHITE);
            thu.setTextColor(Color.WHITE);
            fri.setTextColor(Color.WHITE);
            sun.setTextColor(Color.WHITE);

            sat.setBackgroundColor(Color.WHITE);
            sat.setTextColor(Color.parseColor("#58BDC7"));
            DAY_NAME = "saturday";
            api();
        });


    }


    private void setday(String dayOfTheWeek) {

        switch (dayOfTheWeek) {
            case "Monday": {
                mon.setBackgroundColor(Color.WHITE);
                mon.setTextColor(Color.parseColor("#58BDC7"));
                mon.setText("Today");
                DAY_NAME = "monday";
                break;
            }
            case "Tuesday": {
                tue.setBackgroundColor(Color.WHITE);
                tue.setTextColor(Color.parseColor("#58BDC7"));
                tue.setText("Today");
                DAY_NAME = "tuesday";
                break;
            }
            case "Wednesday": {
                wed.setBackgroundColor(Color.WHITE);
                wed.setTextColor(Color.parseColor("#58BDC7"));
                wed.setText("Today");
                DAY_NAME = "wednesday";
                break;
            }
            case "Thursday": {
                thu.setBackgroundColor(Color.WHITE);
                thu.setTextColor(Color.parseColor("#58BDC7"));
                thu.setText("Today");
                DAY_NAME = "thursday";
                break;
            }
            case "Friday": {
                fri.setBackgroundColor(Color.WHITE);
                fri.setTextColor(Color.parseColor("#58BDC7"));
                fri.setText("Today");
                DAY_NAME = "friday";
                break;
            }
            case "Saturday": {
                sat.setBackgroundColor(Color.WHITE);
                sat.setTextColor(Color.parseColor("#58BDC7"));
                sat.setText("Today");
                DAY_NAME = "saturday";
                break;
            }
            case "Sunday": {
                sun.setBackgroundColor(Color.WHITE);
                sun.setTextColor(Color.parseColor("#58BDC7"));
                sun.setText("Today");
                DAY_NAME = "sunday";
                break;
            }


        }


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

        acti.runOnUiThread(() -> Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        acti = activity;

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
