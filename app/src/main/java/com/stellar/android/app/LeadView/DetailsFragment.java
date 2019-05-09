package com.stellar.android.app.LeadView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.pix.Pix;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import com.stellar.android.app.DashBordActivity;
import com.stellar.android.app.PixAdapter;
import com.stellar.android.app.R;
import com.stellar.android.app.Utill;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class DetailsFragment extends Fragment {
    private String TAG = "DetailsFragment";
    private LinearLayout old_fwup;

    private Activity activity;
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    private List<OldFollowUp> list;
    private FloatingActionButton fcall, fadd;
    private FloatingActionMenu actionMenu;
    private String EMP_ID;
    private TextView ndate, lastdate, resp, com, rname, d_date;
    private FloatingActionButton cal;
    private LinearLayout cardView;
    private Button btn_win, btn_update, btn_save_changes, btn_loss;




    private ProgressDialog progressDialog;
    private RecyclerView oldFollowUp;
    private ProgressBar progressBar;
    private String id, sname, semail, sdate, smob, smob1, scont, sadd, soffno, scomname;
    private String sdep, sdesig, ssource, sdescrip, stor, sloanValue, sltype;
    private String totime, ftime;
    int vis;
    private AlertDialog dialog;
    private LinearLayout d_btn_l1;


    private ArrayList<String> categoryName = new ArrayList<>();
    private ArrayList<String> categoryId = new ArrayList<>();
    private ArrayAdapter adaptercategoryName;

    private ArrayList<String> sourceName = new ArrayList<>();
    private ArrayList<String> sourceId = new ArrayList<>();
    private ArrayAdapter adaptersourceName;

    private ArrayList<String> reqName = new ArrayList<>();
    private ArrayList<String> reqId = new ArrayList<>();
    private ArrayAdapter adapterreqName;


    private String cateId, reqstId, sourId;
    private String temp_source, temp_req, temp_cate, temp_contact;
    private String t, f;

    private Spinner sp_category, sp_typeofReq, d_source;

    private List<Bitmap> BitmapList=new ArrayList<>();

  private PixAdapter pixAdapter;
  private RecyclerView rcyPix;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
            sname = getArguments().getString("name");
            semail = getArguments().getString("email");
            sdate = getArguments().getString("date");
            smob = getArguments().getString("mob");
            smob1 = getArguments().getString("mob1");
            scont = getArguments().getString("conty");
            sadd = getArguments().getString("add");
            soffno = getArguments().getString("offno");
            scomname = getArguments().getString("comname");
            sdep = getArguments().getString("dep");
            sdesig = getArguments().getString("desig");
            ssource = getArguments().getString("source");
            sdescrip = getArguments().getString("descrip");
            stor = getArguments().getString("tor");
            sloanValue = getArguments().getString("lvalue");
            sltype = getArguments().getString("ltype");
            totime = getArguments().getString("toti");
            ftime = getArguments().getString("fti");
            vis = getArguments().getInt("twin");
            temp_cate = getArguments().getString("cat");

            temp_source = ssource;
            temp_req = stor;
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_details, container, false);
        SharedPreferences preferences = activity.getSharedPreferences("stelleruser", MODE_PRIVATE);

        EMP_ID = preferences.getString("empid", "");
        Log.e("DetailsActivty user id", EMP_ID);

        list = new ArrayList<>();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        multiapi();

        final TextView name = view.findViewById(R.id.d_name);
        final TextView d_email = view.findViewById(R.id.d_email);
        final TextView d_mobile = view.findViewById(R.id.d_mobile);
        final TextView d_mobile1 = view.findViewById(R.id.d_mobile1);
        final TextView d_officeno = view.findViewById(R.id.d_officeno);

        final TextView d_company_name = view.findViewById(R.id.d_company_name);

        final TextView d_department = view.findViewById(R.id.d_department);

        final TextView d_designation = view.findViewById(R.id.d_designation);

        final TextView d_contact_type = view.findViewById(R.id.d_contact_type);

        btn_save_changes = view.findViewById(R.id.btn_save_changes);
        d_date = view.findViewById(R.id.d_date);
        btn_save_changes.setVisibility(View.GONE);

        sp_typeofReq = view.findViewById(R.id.dd_type_of_requirement);
        d_btn_l1 = view.findViewById(R.id.d_btn_l1);
        btn_loss = view.findViewById(R.id.btn_loss);

        final EditText d_expected_lead_value = view.findViewById(R.id.d_expected_lead_value);
        final Spinner d_lead_type = view.findViewById(R.id.d_lead_type);
        d_lead_type.setEnabled(false);
        final TextView d_address = view.findViewById(R.id.d_address);
        final TextView d_description = view.findViewById(R.id.d_description);

        d_source = view.findViewById(R.id.dd_source);
        sp_category = view.findViewById(R.id.dd_category);

        d_source.setEnabled(false);
        sp_category.setEnabled(false);
        sp_typeofReq.setEnabled(false);


        final TextView d_totime = view.findViewById(R.id.d_totime);
        final TextView d_fromtime = view.findViewById(R.id.d_fromtime);

        btn_win = view.findViewById(R.id.btn_win);
        d_btn_l1.setVisibility(View.GONE);

        btn_update = view.findViewById(R.id.btn_update);

        ndate = view.findViewById(R.id.dd_ndate);
        lastdate = view.findViewById(R.id.dd_ldate);
        rname = view.findViewById(R.id.dd_ename);
        com = view.findViewById(R.id.dd_commant);
        resp = view.findViewById(R.id.dd_res);
        //  cal = view.findViewById(R.id.dd_call);
        cardView = view.findViewById(R.id.c);
        cardView.setVisibility(View.GONE);
        fcall = view.findViewById(R.id.fcall);
        fadd = view.findViewById(R.id.faddd);
        old_fwup = view.findViewById(R.id.old_fwup);
        old_fwup.setVisibility(View.GONE);
        actionMenu = view.findViewById(R.id.fabmenu);

        oldFollowUp = view.findViewById(R.id.rc_old);
        oldFollowUp.setNestedScrollingEnabled(false);
        progressBar = view.findViewById(R.id.old_progress);

        progressBar.setVisibility(View.GONE);

        if (vis == 1) {
            d_btn_l1.setVisibility(View.VISIBLE);
        }


        d_expected_lead_value.addTextChangedListener(new TextWatcher() {

            boolean isManualChange = false;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (isManualChange) {
                    isManualChange = false;
                    return;
                }

                try {
                    String value = s.toString().replace(",", "");
                    String reverseValue = new StringBuilder(value).reverse()
                            .toString();
                    StringBuilder finalValue = new StringBuilder();
                    for (int i = 1; i <= reverseValue.length(); i++) {
                        char val = reverseValue.charAt(i - 1);
                        finalValue.append(val);
                        if (i % 3 == 0 && i != reverseValue.length() && i > 0) {
                            finalValue.append(",");
                        }
                    }
                    isManualChange = true;
                    d_expected_lead_value.setText(finalValue.reverse());
                    d_expected_lead_value.setSelection(finalValue.length());
                } catch (Exception e) {
                    // Do nothing since not a number
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        d_date.setOnClickListener(v -> {
            int mYear, mMonth, mDay;

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            final DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                    (view12, year, monthOfYear, dayOfMonth) -> d_date.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + ""), mYear, mMonth, mDay);
            datePickerDialog.show();
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);  //this for past date not clickbale
        });


        d_fromtime.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(activity, (timePicker, selectedHour, selectedMinute) -> d_fromtime.setText(selectedHour + ":" + selectedMinute), hour, minute, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });
        d_totime.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(activity, (timePicker, selectedHour, selectedMinute) -> d_totime.setText(selectedHour + ":" + selectedMinute), hour, minute, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, OrientationHelper.VERTICAL, false);
        oldFollowUp.setLayoutManager(layoutManager);
        oldFollowUp.setItemAnimator(new DefaultItemAnimator());


        name.setText(sname);
        d_email.setText(semail);
        d_mobile.setText(smob);
        d_mobile1.setText(smob1);
        d_officeno.setText(soffno);
        d_company_name.setText(scomname);
        d_department.setText(sdep);
        d_designation.setText(sdesig);
        d_contact_type.setText(scont);
        d_expected_lead_value.setText(sloanValue);
        d_date.setText(sdate);
        d_address.setText(sadd);
        d_description.setText(sdescrip);

        d_totime.setText(totime);
        d_fromtime.setText(ftime);

        String[] in = getResources().getStringArray(R.array.leadtype);
        for (int i = 0; i < in.length; i++) {
            if (in[i].equals(sltype)) {
                d_lead_type.setSelection(i);
            }

        }

        recentapi();
        apicall();


        btn_update.setOnClickListener(v -> {

            d_email.setEnabled(true);
            d_officeno.setEnabled(true);
            d_company_name.setEnabled(true);
            d_department.setEnabled(true);
            d_designation.setEnabled(true);
            d_contact_type.setEnabled(true);
            //  d_type_of_requirement.setEnabled(true);
            d_expected_lead_value.setEnabled(true);
            d_lead_type.setEnabled(true);
            d_address.setEnabled(true);
            d_description.setEnabled(true);

            d_totime.setEnabled(true);
            d_fromtime.setEnabled(true);
            d_date.setEnabled(true);

            d_source.setEnabled(true);
            sp_category.setEnabled(true);
            sp_typeofReq.setEnabled(true);

            btn_save_changes.setVisibility(View.VISIBLE);

            //   d_source.setEnabled(true);
        });


        btn_win.setOnClickListener(v -> windialog());

        btn_loss.setOnClickListener(v -> {


            Log.e("dateMiliSe", String.valueOf(System.currentTimeMillis()));
            Log.e("Datess ", String.valueOf(Calendar.DATE));

            // Toast.makeText(activity, "Loss Lead", Toast.LENGTH_SHORT).show();
//

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflate = activity.getLayoutInflater();
            final View viewW = inflate.inflate(R.layout.item_loss_lead, null);
            builder.setView(viewW);
            final AlertDialog dialog = builder.create();

            final EditText commanet = viewW.findViewById(R.id.item_lcomment);
            Spinner lreson = viewW.findViewById(R.id.sp_lossType);

            viewW.findViewById(R.id.btn_lcancel).setOnClickListener(v1 -> dialog.cancel());


            viewW.findViewById(R.id.btn_lok).setOnClickListener(v1 -> {

                String resion = lreson.getSelectedItem().toString().trim();
                String comnt = commanet.getText().toString().trim();

                if (resion.equalsIgnoreCase(""))
                    Toast.makeText(activity, "Select Reason", Toast.LENGTH_SHORT).show();
                else LossleadApi(id, resion, comnt, dialog);
            });
            dialog.show();
        });

        btn_save_changes.setOnClickListener(v -> {

            d_email.setEnabled(false);
            d_officeno.setEnabled(false);
            d_company_name.setEnabled(false);
            d_department.setEnabled(false);
            d_designation.setEnabled(false);
            d_contact_type.setEnabled(false);
            //  d_type_of_requirement.setEnabled(true);
            d_expected_lead_value.setEnabled(false);
            d_lead_type.setEnabled(false);
            d_address.setEnabled(false);
            d_description.setEnabled(false);

            d_totime.setEnabled(false);
            d_fromtime.setEnabled(false);

            d_source.setEnabled(false);
            sp_category.setEnabled(false);
            sp_typeofReq.setEnabled(false);

            d_date.setEnabled(false);


            String sname = name.getText().toString().trim();
            String smail = d_email.getText().toString().trim();
            String smobile = d_mobile.getText().toString().trim();
            String sphone = d_officeno.getText().toString().trim();
            String scompname = d_company_name.getText().toString().trim();
            String sdesignation = d_designation.getText().toString().trim();
            String sdepart = d_department.getText().toString().trim();
            String sassignto = EMP_ID;
            String sleadstatus = "New Lead";
            String saddress = d_address.getText().toString().trim();
            String sdescription = d_description.getText().toString().trim();

            String stypereq = reqstId;
            String scategory = cateId;
            String ssource = sourId;


            Log.e(TAG + "InSide Update", reqstId + " reqID " + cateId + " CatId " + sourId + " Source");
            String formtime = d_fromtime.getText().toString();
            String totime = d_totime.getText().toString();


            String sleadvalue = d_expected_lead_value.getText().toString().trim();
            String sclosingdate = d_date.getText().toString().trim();
            String scontactTyape = d_contact_type.getText().toString().trim();
            String sleadtrype = d_lead_type.getSelectedItem().toString().trim();

            btn_save_changes.setVisibility(View.GONE);


            changesApi(sname, smail, smobile, sphone, scompname, sdesignation
                    , sdepart, saddress, sdescription, stypereq, scategory, ssource
                    , formtime, totime, sleadvalue, sclosingdate, scontactTyape
                    , sleadtrype
            );


        });


        fadd.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater1 = activity.getLayoutInflater();
            final View view1 = inflater1.inflate(R.layout.item_followup, null);
            builder.setView(view1);
            dialog = builder.create();

            final TextView date = view1.findViewById(R.id.et_date);
            final Spinner spinner = view1.findViewById(R.id.sp_res);
            final EditText comm = view1.findViewById(R.id.fcomm);
            Button save = view1.findViewById(R.id.fsave);
            final ProgressBar pbar = view1.findViewById(R.id.progg);
            pbar.setVisibility(View.GONE);

            save.setOnClickListener(v12 -> {
                String sp = spinner.getSelectedItem().toString();
                String com = comm.getText().toString();
                String d = date.getText().toString();
                if (d.equals("")) {
                    date.setError("is Empty");
                } else if (com.equals("")) {
                    comm.setError("Is Empty");
                } else {
                    pbar.setVisibility(View.VISIBLE);
                    followapi(sp, com, d, id, dialog, pbar);
                }
            });


            date.setOnClickListener(v13 -> {
                int mYear, mMonth, mDay;

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        (view2, year, monthOfYear, dayOfMonth) -> date.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + ""), mYear, mMonth, mDay);
                datePickerDialog.show();

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);  //this for past date not clickbale


            });
            view1.findViewById(R.id.fcancle).setOnClickListener(v14 -> dialog.cancel());
            dialog.show();


        });


        return view;
    }

    private void LossleadApi(String id, String resion, String comnt, AlertDialog dialog) {

        String key = getsha256("lead_lost");
        Log.e(TAG + "lossLead key", key);
        progressDialog.show();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "lead_lost")
                .add("key", key)
                .add("lead_id", id)
                .add("status", resion)
                .add("status_other_comm", comnt)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "NetWork Problem", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string().trim();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);

                        String msg = jsonObject.getString("message");
                        Log.e("loss Msg", msg);

                        if (msg.equalsIgnoreCase("true")) {
                            activity.runOnUiThread(() -> {
                                progressDialog.dismiss();
                                Toast.makeText(activity, "Sucess", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            });
                        } else {
                            activity.runOnUiThread(() -> {
                                progressDialog.dismiss();
                                Toast.makeText(activity, "Try Again", Toast.LENGTH_SHORT).show();
                            });

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        activity.runOnUiThread(() -> {
                            progressDialog.dismiss();
                            Toast.makeText(activity, "Some Things Is Wrong", Toast.LENGTH_SHORT).show();
                        });
                    }


                } else {
                    activity.runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(activity, "Try Again", Toast.LENGTH_SHORT).show();
                    });

                }

            }
        });
    }

    private void changesApi(String sname, String smail, String smobile, String sphone, String scompname, String sdesignation,
                            String sdepart, String saddress, String sdescription,
                            String stypereq, String scategory, String ssource, String formtime, String totime,
                            String sleadvalue, String sclosingdate, String scontactTyape, String sleadtrype) {


        String key = getsha256("lead_update");
        Log.e("lead_update key", key);
        progressDialog.show();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "lead_update")
                .add("key", key)
                .add("lead_id", id)
                .add("to_time", totime)
                .add("from_time", formtime)
                .add("category", scategory)
                .add("name", sname)
                .add("email", smail)
                .add("mobile", smobile)
                .add("office_phone", sphone)
                .add("company_name", scompname)
                .add("department", sdepart)
                .add("designation", sdesignation)
                .add("source", ssource)
                .add("contact_type", scontactTyape)
                .add("address", saddress)
                .add("description", sdescription)
                .add("type_of_requirement", stypereq)
                .add("expected_lead_value", sleadvalue)
                .add("expected_closing_date", sclosingdate)
                .add("lead_type", sleadtrype)
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
                        progressDialog.dismiss();

                        Toast.makeText(getActivity(), "network Problem", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();

                Log.e("change up responce", result);
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);

                        String msg = object.getString("message");
                        Log.e("message   ", msg);

                        if (msg.equals("true")) {
                            activity.runOnUiThread(() -> {
                                DetailsFragment.this.dialog.cancel();
                                progressDialog.dismiss();

                                Toast.makeText(getActivity(), "Sucess", Toast.LENGTH_SHORT).show();
                            });


                        } else {
                            activity.runOnUiThread(() -> {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }


                }
                progressDialog.dismiss();
            }
        });


    }

    private void lostapi(final DialogInterface dialog) {


        String key = getsha256("lead_followUp");
        Log.e("key", key);
        progressDialog.show();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "lead_followUp")
                .add("key", key)
                .add("lead_id", id)
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
                        progressDialog.dismiss();
                        dialog.cancel();

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
                            activity.runOnUiThread(() -> {
                                DetailsFragment.this.dialog.cancel();
                                progressDialog.dismiss();
                                dialog.cancel();

                                Toast.makeText(getActivity(), "Sucess", Toast.LENGTH_SHORT).show();
                            });


                        } else {
                            activity.runOnUiThread(() -> {
                                progressDialog.dismiss();
                                dialog.cancel();
                                Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                                DetailsFragment.this.dialog.cancel();
                            });
                        }
                    } catch (Exception e) {
                        dialog.cancel();
                        e.getMessage();
                    }


                }
                dialog.cancel();
                progressDialog.dismiss();
            }
        });


    }

    private void windialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = activity.getLayoutInflater();
        final View viewW = inflater.inflate(R.layout.item_win_dialog, null);
        builder.setView(viewW);
        final AlertDialog dialog = builder.create();

        final EditText oderNo = viewW.findViewById(R.id.w_purchase_order_no);
        final EditText odernValue = viewW.findViewById(R.id.w_purchase_order_value);
        final EditText saleOderNumber = viewW.findViewById(R.id.w_sales_order_number);
        final TextView deli_date = viewW.findViewById(R.id.w_exp_delivery_date);
        final Spinner payment = viewW.findViewById(R.id.sp_paymentType);

        deli_date.setOnClickListener(v -> {
            int mYear, mMonth, mDay;
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        String s = "";
                        String m = "";

                        if ((monthOfYear + 1) < 10) {
                            s = "0" + (monthOfYear + 1);
                        } else {
                            s = String.valueOf(monthOfYear + 1);
                        }

                        if ((dayOfMonth) < 10) {
                            m = "0" + (dayOfMonth);
                        } else {
                            m = String.valueOf(dayOfMonth);
                        }


                        deli_date.setText("" + year + "-" + s + "-" + m + "");
                    }, mYear, mMonth, mDay);
            // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 3000);  //this for past date not clickbale
            datePickerDialog.show();
        });

        viewW.findViewById(R.id.imgTake).setOnClickListener(v -> Pix.start(DetailsFragment.this, 20, 5));

        rcyPix=viewW.findViewById(R.id.rcyPix);

        rcyPix.setLayoutManager(new LinearLayoutManager(activity,OrientationHelper.HORIZONTAL,false));


      //  pixAdapter.notifyDataSetChanged();





        odernValue.addTextChangedListener(new TextWatcher() {

            boolean isManualChange = false;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (isManualChange) {
                    isManualChange = false;
                    return;
                }

                try {
                    String value = s.toString().replace(",", "");
                    String reverseValue = new StringBuilder(value).reverse()
                            .toString();
                    StringBuilder finalValue = new StringBuilder();
                    for (int i = 1; i <= reverseValue.length(); i++) {
                        char val = reverseValue.charAt(i - 1);
                        finalValue.append(val);
                        if (i % 3 == 0 && i != reverseValue.length() && i > 0) {
                            finalValue.append(",");
                        }
                    }
                    isManualChange = true;
                    odernValue.setText(finalValue.reverse());
                    odernValue.setSelection(finalValue.length());
                } catch (Exception e) {
                    // Do nothing since not a number
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });






        viewW.findViewById(R.id.btn_Wok).setOnClickListener(v -> {


            JSONArray resept = new JSONArray();
            for (int i = 0; i < BitmapList.size(); i++) {
                JSONObject object = new JSONObject();
                try {
                    if (BitmapList.get(i)!=null&& !(BitmapList.get(i).toString().equalsIgnoreCase("")))
                    {
                        object.put("imagename", Utill.generateImageName("resept"));
                        object.put("imagedata", Utill.getEncoded64ImageStringFromBitmap(BitmapList.get(i)));
                        resept.put(object);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("exp ", e.getMessage());
                }
            }
            Log.e(TAG,"Image data "+ resept.toString());






            String oderno = oderNo.getText().toString().trim();
            String oderval = odernValue.getText().toString().trim();
            String salno = saleOderNumber.getText().toString().trim();
            String date = deli_date.getText().toString().trim();
            String paym = payment.getSelectedItem().toString().trim();

            String idd = id;
            Log.e("lead idsss", id);
            if (oderno.equals("")) {
                oderNo.setError("Is Empty");

            } else if (oderval.equals("")) {
                odernValue.setError("Is Empty");
            } else if (salno.equals("")) {
                saleOderNumber.setError("Is Empty");
            } else if (date.equals("")) {
                deli_date.setError("Select Date");
            } else {
                WinApi(idd, oderno, oderval, salno, date, paym, dialog,resept);
            }


        });
        viewW.findViewById(R.id.btn_Wcancel).setOnClickListener(v -> dialog.cancel());
        dialog.show();




    }


    private void multiapi() {

        String key = getsha256("getAllmaster");
        Log.e("key", key);


        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "getAllmaster")
                .add("key", key)

                .build();
        final Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        toast("Network Problem");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                // Log.e("result ",result);
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equals("true")) {


                            JSONArray category = object.getJSONArray("category");
                            JSONArray arraytype_of_requirement = object.getJSONArray("type_of_requirement");
                            JSONArray arraySource = object.getJSONArray("Source");

                            categoryName.clear();
                            categoryId.clear();
                            if (category.length() > 0) {

                                for (int i = 0; i < category.length(); i++) {
                                    JSONObject objectcategory = category.getJSONObject(i);

                                    String id = objectcategory.getString("id");
                                    String categoryNam = objectcategory.getString("categoryName");
                                    categoryId.add(id);
                                    categoryName.add(categoryNam);

                                }

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        adaptercategoryName = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, categoryName);

                                        sp_category.setAdapter(adaptercategoryName);
                                        adaptercategoryName.notifyDataSetChanged();

                                        int inde = categoryName.indexOf(temp_cate);
                                        sp_category.setSelection(inde);

                                        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                cateId = categoryId.get(position);
                                                Log.e(TAG + " caterory", "name " + categoryName.get(position) + " id " + categoryId.get(position));
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });

                                    }
                                });
                            } else {
                                activity.runOnUiThread(() -> {
                                    adaptercategoryName = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, categoryName);

                                    sp_category.setAdapter(adaptercategoryName);
                                    //  toast("not fond data");


                                });
                            }

                            reqName.clear();
                            reqId.clear();
                            if (arraytype_of_requirement.length() > 0) {

                                for (int i = 0; i < arraytype_of_requirement.length(); i++) {
                                    JSONObject objectReq = arraytype_of_requirement.getJSONObject(i);

                                    String id = objectReq.getString("id");
                                    String categoryNam = objectReq.getString("requirementName");
                                    reqId.add(id);
                                    reqName.add(categoryNam);

                                }
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


                                        adapterreqName = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, reqName);

                                        sp_typeofReq.setAdapter(adapterreqName);
                                        adapterreqName.notifyDataSetChanged();

                                        int inde = reqName.indexOf(temp_req);
                                        sp_typeofReq.setSelection(inde);


                                        sp_typeofReq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                reqstId = reqId.get(position);
                                                Log.e(TAG + " Req", "name " + reqName.get(position) + " id " + reqId.get(position));
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                    }

                                });


                            } else {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        adapterreqName = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, reqName);

                                        sp_typeofReq.setAdapter(adapterreqName);
                                        adapterreqName.notifyDataSetChanged();

                                    }
                                });
                            }
                            sourceName.clear();
                            sourceId.clear();
                            if (arraySource.length() > 0) {

                                for (int i = 0; i < arraySource.length(); i++) {
                                    JSONObject objectSource = arraySource.getJSONObject(i);

                                    String id = objectSource.getString("id");
                                    String categoryNam = objectSource.getString("sourceName");
                                    sourceId.add(id);
                                    sourceName.add(categoryNam);

                                }

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        adaptersourceName = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, sourceName);

                                        d_source.setAdapter(adaptersourceName);
                                        adaptersourceName.notifyDataSetChanged();

                                        int inde = sourceName.indexOf(temp_source);
                                        d_source.setSelection(inde);

                                        d_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                sourId = sourceId.get(position);
                                                Log.e(TAG + " source", "name " + sourceName.get(position) + " id " + sourceId.get(position));
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                    }

                                });


                            } else {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        adaptersourceName = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, sourceName);

                                        d_source.setAdapter(adaptersourceName);
                                        adaptersourceName.notifyDataSetChanged();


                                    }
                                });
                            }


                        } else {
                            toast("Connection Failed");

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


    public void dialogclose() {
        activity.runOnUiThread(() -> progressDialog.dismiss());

    }


    private void WinApi(String id, String oderno, String oderval, String salno, String date,
                        String paym, final AlertDialog dialog, JSONArray resept) {

        String key = getsha256("insertWonAchieve");
        progressDialog.show();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "insertWonAchieve")
                .add("key", key)
                .add("lead_id", id)
                .add("employee_id", DashBordActivity.EmpId)
                .add("purchase_order_no", oderno)
                .add("purchase_order_value", oderval)
                .add("sales_order_number", salno)
                .add("exp_delivery_date", date)
                .add("payment_condition", paym)
                .add("image_name", resept.toString())
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                activity.runOnUiThread(() -> {

                    Log.e("network", e.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "network Problem", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();

                Log.e("Folw responce","dsfsdf "+ result);
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);

                        String msg = object.getString("message");
                        Log.e("message   ", msg);

                        if (msg.equals("true")) {
                            activity.runOnUiThread(() -> {
                                dialog.cancel();
                                progressDialog.dismiss();


                                Toast.makeText(getActivity(), "Sucess", Toast.LENGTH_SHORT).show();
                            });

                        } else {
                            activity.runOnUiThread(() -> {
                                progressDialog.dismiss();
                                dialog.show();
                                Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                                DetailsFragment.this.dialog.cancel();
                            });
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
                progressDialog.dismiss();
            }
        });


    }

    private void followapi(String sp, String com, String d, String id, final AlertDialog dialog, final ProgressBar pbar) {


        String key = getsha256("lead_followUp");
        Log.e("key", key);
        progressDialog.show();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "lead_followUp")
                .add("key", key)
                .add("lead_id", id)
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
                activity.runOnUiThread(() -> {

                    Log.e("network", e.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "network Problem", Toast.LENGTH_SHORT).show();
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
                            activity.runOnUiThread(() -> {
                                dialog.cancel();
                                progressDialog.dismiss();

                                recentapi();
                                apicall();
                                Toast.makeText(getActivity(), "Sucess", Toast.LENGTH_SHORT).show();
                            });


                        } else {
                            activity.runOnUiThread(() -> {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            });
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }


                }
                progressDialog.dismiss();
            }
        });


    }

    private void recentapi() {


        Log.e(TAG + " Lead Id", id);
        String key = getsha256("current_followup_data");
        //Log.e(TAG+"current_followup key", key);
        // progressBar.setVisibility(View.VISIBLE);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "current_followup_data")
                .add("key", key)
                .add("lead_id", id)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                activity.runOnUiThread(() -> {

                    Log.e("network", e.getMessage());
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "network Problem", Toast.LENGTH_SHORT).show();
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                String result = response.body().string();
                Log.e(TAG + " recrntfolow result ", result);

                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);


                        String msg = object.getString("message").trim();
                        //Log.e("recrnt followup message", msg);
                        if (msg.equals("True")) {

                            final String last_folloup_date;
                            final String next_folloup_date;
                            final String folloup_comment;
                            final String responsex;
                            final String full_name;
                            final String mobile;

                            final JSONArray array = object.getJSONArray("data");
                            // Log.e("recrnt dattattatat", array.toString());


                            JSONObject objectname = array.getJSONObject(0);


                            last_folloup_date = objectname.getString("last_folloup_date");
                            next_folloup_date = objectname.getString("next_folloup_date");
                            folloup_comment = objectname.getString("folloup_comment");
                            responsex = objectname.getString("response");
                            full_name = objectname.getString("full_name");
                            mobile = objectname.getString("mobile");


                            activity.runOnUiThread(() -> {

                                if (!array.equals("")) {
                                    cardView.setVisibility(View.VISIBLE);
                                    ndate.setText(next_folloup_date);
                                    lastdate.setText(last_folloup_date);
                                    com.setText(folloup_comment);
                                    resp.setText(responsex);
                                    rname.setText(full_name);

                                    fcall.setOnClickListener(v -> {
                                        String no = mobile;
                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:" + no));
                                        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 10);
                                        } else {
                                            startActivity(callIntent);
                                        }


                                    });
                                }

                            });
                        }
//                            else {
//                            activity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    cardView.setVisibility(View.VISIBLE);
//
//                                }
//                            });
//                        }


                    } catch (final Exception e) {
                        e.getMessage();
                        activity.runOnUiThread(() -> {
                            Log.e("ecx", e.getMessage());

                        });

                    }
                }


            }
        });


    }

    private void apicall() {
        String key = getsha256("followup_history");
        progressBar.setVisibility(View.VISIBLE);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "followup_history")
                .add("key", key)
                .add("lead_id", id)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                activity.runOnUiThread(() -> {

                    Log.e("network", e.getMessage());
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "network Problem", Toast.LENGTH_SHORT).show();
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                String result = response.body().string();
                Log.e(TAG + " old followUp result ", result);

                if (response.isSuccessful()) {

                    try {

                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message").trim();
                        if (msg.equals("True")) {

                            JSONArray array = object.getJSONArray("data");


                            if (array.length() > 0) {
                                list.clear();

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject objectname = array.getJSONObject(i);

                                    String last_folloup_date = objectname.getString("last_folloup_date");
                                    String next_folloup_date = objectname.getString("next_folloup_date");
                                    String folloup_comment = objectname.getString("folloup_comment");
                                    String old_response = objectname.getString("old_response");
                                    String followup_by_emp_name = objectname.getString("followup_by_emp_name");
                                    String followup_by_emp_mobile = objectname.getString("followup_by_emp_mobile");

                                    OldFollowUp model = new OldFollowUp();
                                    model.setOldDate(last_folloup_date);
                                    model.setNextDate(next_folloup_date);
                                    model.setOldcomant(folloup_comment);
                                    model.setOldresp(old_response);
                                    model.setEmpname(followup_by_emp_name);
                                    model.setNo(followup_by_emp_mobile);
                                    list.add(model);
                                }
                                activity.runOnUiThread(() -> {
                                    old_fwup.setVisibility(View.VISIBLE);

                                    OldFollowAdapter adapter = new OldFollowAdapter(list);
                                    oldFollowUp.setAdapter(adapter);
                                    progressBar.setVisibility(View.GONE);
                                    toast("success");
                                    adapter.setClickListener(position -> {
                                        String no = list.get(position).getNo();

                                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                        callIntent.setData(Uri.parse("tel:" + no));
//                                        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 10);
//                                        } else {
//                                            startActivity(callIntent);
//                                        }

                                    });

                                    adapter.notifyDataSetChanged();

                                });
                            } else {
                                activity.runOnUiThread(() -> {
                                    toast("No FollowUp Found");
                                    progressBar.setVisibility(View.GONE);
                                });
                            }


                        } else {
                            progressBar.setVisibility(View.GONE);

                            toast("try again");
                        }
                    } catch (final Exception e) {
                        e.getMessage();
                        activity.runOnUiThread(() -> {
                            Log.e("ecx", e.getMessage());
                            progressBar.setVisibility(View.GONE);
                        });
                    }
                }


            }
        });


    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
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
        activity.runOnUiThread(() -> Toast.makeText(activity, s, Toast.LENGTH_SHORT).show());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == RESULT_OK) {

          //  Toast.makeText(activity, "Its HErer", Toast.LENGTH_SHORT).show();
            List<String> pic = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            BitmapList.clear();
            for (String s : pic) {
                File f = new File(s);
                Bitmap d = new BitmapDrawable(getResources(), f.getAbsolutePath()).getBitmap();
                BitmapList.add(d);
            }
            pixAdapter =new PixAdapter(BitmapList);
            rcyPix.setAdapter(pixAdapter);
            pixAdapter.notifyDataSetChanged();

            pixAdapter.setClickListener(new PixAdapter.ItemClickListener() {
                @Override
                public void onItemClick(int position) {

                }

                @Override
                public void onCancel(int position) {
                    BitmapList.remove(position);
                    pixAdapter.notifyDataSetChanged();

                }
            });
        }
    }


}
