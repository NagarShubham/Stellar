package com.stellar.android.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.stellar.android.app.Scanner.OcrCaptureActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class AddNew_LeadActivity extends Fragment {
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    private static final String TAG = "New Lead";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Activity activity;
    private static String EMP_ID;
    private static final int REQUEST_CODE = 102;
    private EditText et_email, et_phone, et_comp_name;
    private Spinner sp_typeofReq, sp_source, sp_category;

    private EditText mobile1;

    private EditText et_address, et_description, et_leadvalue, et_closingDate;
    private Spinner sp_contactType;
    private Spinner sp_department;
    private Spinner sp_designation;

    private Spinner sp_leadtype;
    //  private Spinner et_leadStatus;
    private Button save;
    private ImageView scan;
    private ImageView backspace;
    private static String STATUS = "New Lead";
    private static String LEAD_Id = "";
    private ProgressDialog progressDialog;


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

    private TextView formTime, toTime;

    private List<String> autoName = new ArrayList<>();
    private List<String> autoMobile = new ArrayList<>();

    private AutoCompleteTextView autoname;
    private AutoCompleteTextView automobile;

    private String tastId = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_new__lead, container, false);

        DashBordActivity.menuchek(2);
        DashBordActivity.titleName("Add Lead");

        preferences = activity.getSharedPreferences("stelleruser", MODE_PRIVATE);
        editor = preferences.edit();

        Log.e("next user id", preferences.getString("empid", ""));
        EMP_ID = preferences.getString("empid", "");
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        Findigid(view);
//Log.e("timeinMi","timreeeee "+System.currentTimeMillis());
//Log.e("timeindate","timreeeee "+Calendar.DAY_OF_MONTH);


        formTime.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(activity, (timePicker, selectedHour, selectedMinute) -> formTime.setText(selectedHour + ":" + selectedMinute), hour, minute, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });
        toTime.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(activity, (timePicker, selectedHour, selectedMinute) -> toTime.setText(selectedHour + ":" + selectedMinute), hour, minute, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });


        et_leadvalue.addTextChangedListener(new TextWatcher() {

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
                    et_leadvalue.setText(finalValue.reverse());
                    et_leadvalue.setSelection(finalValue.length());
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


        if (getArguments() != null) {
            STATUS = "Task to Lead";
            tastId = getArguments().getString("id");

            Log.e("task id ", "old " + tastId);
            autoname.setText(getArguments().getString("tname"));
            automobile.setText(getArguments().getString("tmobile"));
            et_address.setText(getArguments().getString("tadd"));
            et_description.setText(getArguments().getString("tdetais"));
            et_comp_name.setText(getArguments().getString("tcompname"));
            temp_cate = (getArguments().getString("tcategory"));
            temp_source = (getArguments().getString("tsource"));
            temp_req = (getArguments().getString("ttreq"));
            temp_contact = getArguments().getString("ttype");
            //bundle.putString("ttype",Homemodel.getPlan_type());

            t = (getArguments().getString("ttime"));
            f = (getArguments().getString("tftime"));

            toTime.setText(t);
            formTime.setText(f);


            String[] temp = getResources().getStringArray(R.array.contacttype);
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].equals(temp_contact)) {
                    sp_contactType.setSelection(i);
                }
            }
        }

        multiapi();
        autoSuggetionApi();

        et_closingDate.setOnClickListener(v -> {
            int mYear, mMonth, mDay;

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            final DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                    (view1, year, monthOfYear, dayOfMonth) -> {

                        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        et_closingDate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + "");

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        try {

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");

                            try {

                                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                                int d = calendar.get(Calendar.DAY_OF_MONTH);
                                int m = calendar.get(Calendar.MONTH) + 1;
                                int y = calendar.get(Calendar.YEAR);

                                Log.e("date   ", "" + d + "/" + m + "/" + y);
                                Log.e("cu Date", "" + date);
                                Date date1 = simpleDateFormat.parse("" + d + "/" + m + "/" + y);
                                Date date2 = simpleDateFormat.parse(date);

                                long dif = printDifference(date1, date2);

                                if (dif >= 0 && dif <= 10) {
                                    sp_leadtype.setSelection(0);
                                }
                                if (dif >= 11 && dif <= 20) {
                                    sp_leadtype.setSelection(1);
                                }
                                if (dif >= 21 && dif <= 30) {
                                    sp_leadtype.setSelection(2);
                                }
                                if (dif > 31) {
                                    sp_leadtype.setSelection(3);
                                }


                                Log.e("Diffence ", "dd " + dif);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);  //this for past date not clickbale
        });


        save.setOnClickListener(v -> {

            String sname = autoname.getText().toString().trim();
            String smail = et_email.getText().toString().trim();
            String smobile = automobile.getText().toString().trim();
            String smobile1 = mobile1.getText().toString().trim();

            String sphone = et_phone.getText().toString().trim();
            String scompname = et_comp_name.getText().toString().trim();
            String sdesignation = sp_designation.getSelectedItem().toString().trim();
            String sdepart = sp_department.getSelectedItem().toString().trim();
            String sassignto = EMP_ID;
            String sleadstatus = "New Lead";
            String saddress = et_address.getText().toString().trim();
            String sdescription = et_description.getText().toString().trim();

            String stypereq = reqstId;
            String scategory = cateId;
            String ssource = sourId;
            String formtime = formTime.getText().toString();
            String totime = toTime.getText().toString();


            String sleadvalue = et_leadvalue.getText().toString().trim();
            String sclosingdate = et_closingDate.getText().toString().trim();
            String scontactTyape = sp_contactType.getSelectedItem().toString().trim();
            String sleadtrype = sp_leadtype.getSelectedItem().toString().trim();

            if (sname.equals("")) {
                autoname.setError("is Empty");
            } else if (smail.equals("")) {
                et_email.setError("is Empty");

            } else if (smobile.length() < 9) {
                automobile.setError("10 no need");
            } else if (sphone.equals("")) {
                et_phone.setError("is Empty");
            } else if (scompname.equals("")) {
                et_comp_name.setError("is Empty");
            } else if (saddress.equals("")) {
                et_address.setError("is Empty");
            } else if (sdescription.equals("")) {
                et_description.setError("is Empty");
            } else if (sleadvalue.equals("")) {
                et_leadvalue.setError("is Empty");
            } else if (sclosingdate.equals("")) {
                et_closingDate.setError("is Empty");
            } else {
                json(sname, smail, smobile, sphone, scompname, sdesignation, sdepart, ssource, sassignto,
                        sleadstatus, saddress, sdescription, stypereq, sleadvalue, sclosingdate, scontactTyape,
                        sleadtrype, scategory, formtime, totime, smobile1);
            }
        });

        return view;
    }

    public long printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        return elapsedDays;
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

                                activity.runOnUiThread(() -> {

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
                                activity.runOnUiThread(() -> {


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
                                });


                            } else {
                                activity.runOnUiThread(() -> {

                                    adapterreqName = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, reqName);

                                    sp_typeofReq.setAdapter(adapterreqName);
                                    adapterreqName.notifyDataSetChanged();

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

                                activity.runOnUiThread(() -> {

                                    adaptersourceName = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, sourceName);

                                    sp_source.setAdapter(adaptersourceName);
                                    adaptersourceName.notifyDataSetChanged();

                                    int inde = sourceName.indexOf(temp_source);
                                    sp_source.setSelection(inde);

                                    sp_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            sourId = sourceId.get(position);
                                            Log.e(TAG + " source", "name " + sourceName.get(position) + " id " + sourceId.get(position));
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                });


                            } else {
                                activity.runOnUiThread(() -> {

                                    adaptersourceName = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, sourceName);

                                    sp_source.setAdapter(adaptersourceName);
                                    adaptersourceName.notifyDataSetChanged();


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


    private void json(String sname, String smail, String smobile, String sphone,
                      String scompname, String sdesignation, String sdepart, String ssource,
                      String sassignto, String sleadstatus, String saddress, String sdescription,
                      String stypereq, String sleadvalue, String sclosingdate, String scontactTyape, String sleadtrype,
                      String scategory, String formtime, String totime, String smobile1) {


        String key = getsha256("leadFormation");
        Log.e("key josn", key);
        progressDialog.show();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "leadFormation")
                .add("key", key)
                .add("employee_id", EMP_ID)
                .add("to_time", totime)
                .add("from_time", formtime)
                .add("name", sname)
                .add("email", smail)
                .add("mobile", smobile)
                .add("mobile_one", smobile1)
                .add("office_phone", sphone)
                .add("company_name", scompname)
                .add("designation", sdesignation)
                .add("department", sdepart)
                .add("source", ssource)
                .add("assigned_to", sassignto)
                .add("contact_type", scontactTyape)
                .add("lead_status", sleadstatus)
                .add("address", saddress)
                .add("description", sdescription)
                .add("type_of_requirement", stypereq)
                .add("expected_lead_value", sleadvalue)
                .add("expected_closing_date", sclosingdate)
                .add("lead_type", sleadtrype)
                .add("category", scategory)
                .add("status", STATUS)
                .add("task_id", tastId)
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

                    Toast.makeText(activity, "Network Problem", Toast.LENGTH_SHORT).show();
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string().trim();
                Log.e(TAG + "save result", result);
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        Log.e("message   ", object.getString("message"));
                        if (msg.equals("Lead Save Successfully")) {

                            toast("Lead Save Successfully");
                            Log.e("planeaaaddddddding", object.getString("data"));

                            activity.runOnUiThread(() -> {
                                progressDialog.dismiss();
                                LEAD_Id = "";
                                STATUS = "New Lead";
                                blankfilds();

                            });
                        } else {
                            activity.runOnUiThread(() -> {
                                progressDialog.dismiss();
                                toast("Lead Not Save Successfully");
                            });
                        }

                    } catch (Exception e) {
                        Log.e("exepteion on add", e.getMessage());

                    }


                } else {
                    activity.runOnUiThread(() -> progressDialog.dismiss());
                }
            }
        });


    }

    private void blankfilds() {

        autoname.setText("");
        et_email.setText("");
        automobile.setText("");
        et_phone.setText("");
        et_comp_name.setText("");
        toTime.setText("");

        formTime.setText("");
        et_address.setText("");
        et_description.setText("");
        mobile1.setText("");
        et_leadvalue.setText("");
        et_closingDate.setText("");


    }

    private void autoSuggetionApi() {

        String key = getsha256("AllemployeewithNameAndMobile");
        Log.e("allName11 key", key);


        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "AllemployeewithNameAndMobile")
                .add("key", key)

                .build();
        final Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(() -> Toast.makeText(activity, "network problem", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string().trim();
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        Log.e("all name msg ", msg);
                        if (msg.equalsIgnoreCase("true")) {
                            JSONArray jsonArray = object.getJSONArray("data");

                            autoMobile.clear();
                            autoName.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);

                                String name = object1.getString("full_name");
                                String mobile = object1.getString("mobile");

                                autoName.add(name);
                                autoMobile.add(mobile);
                            }
                            activity.runOnUiThread(() -> {
                                ArrayAdapter nameAdaper = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, autoName);
                                autoname.setAdapter(nameAdaper);
                                autoname.setThreshold(1);
                                ArrayAdapter mobileAdapet = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, autoMobile);
                                automobile.setAdapter(mobileAdapet);
                                automobile.setThreshold(1);


                                Log.e("nameList ", autoName.toString());
                                Log.e("moblieList ", autoMobile.toString());

                                autoname.setOnItemClickListener((parent, view1, position, id) -> {

                                    String selectedItem = (String) parent.getItemAtPosition(position);
                                    automobile.setText(autoMobile.get(autoName.indexOf(selectedItem)));
                                    Log.e("nameAuto ", "se " + selectedItem);

                                });
                            });


                        } else {
                            activity.runOnUiThread(() -> Toast.makeText(activity, "no data found", Toast.LENGTH_SHORT).show());
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        activity.runOnUiThread(() -> Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_SHORT).show());

                    }


                } else {
                    activity.runOnUiThread(() -> Toast.makeText(activity, "Some thing is ", Toast.LENGTH_SHORT).show());
                }


            }
        });


    }

    private void Findigid(View view) {

        autoname = view.findViewById(R.id.auto_name);
        et_email = view.findViewById(R.id.et_mail);
        automobile = view.findViewById(R.id.auto_mobile);
        mobile1 = view.findViewById(R.id.et_mobile1);

        et_phone = view.findViewById(R.id.et_phone);
        et_comp_name = view.findViewById(R.id.et_comp_name);
        sp_department = view.findViewById(R.id.sp_department);
        sp_source = view.findViewById(R.id.et_source);
        sp_designation = view.findViewById(R.id.sp_designation);
        sp_category = view.findViewById(R.id.et_Category);
        et_address = view.findViewById(R.id.et_address);
        et_description = view.findViewById(R.id.et_description);
        sp_typeofReq = view.findViewById(R.id.et_typeofReq);
        et_leadvalue = view.findViewById(R.id.et_leadvalue);
        et_closingDate = view.findViewById(R.id.et_closingDate);

        toTime = view.findViewById(R.id.et_to_time);
        formTime = view.findViewById(R.id.et_from_time);
        sp_contactType = view.findViewById(R.id.sp_contactType);
        sp_leadtype = view.findViewById(R.id.sp_leadtype);
        save = view.findViewById(R.id.btn_save);
        scan = activity.findViewById(R.id.scan);
        scan.setVisibility(View.VISIBLE);

        sp_leadtype.setEnabled(false);
//        toTime.setText(t);
//        formTime.setText(f);

        scan.setOnClickListener(v -> {
            Intent in = new Intent(getContext(), OcrCaptureActivity.class);
            startActivityForResult(in, REQUEST_CODE);
        });

        Intent intent = activity.getIntent();
        if (intent.getIntExtra("veri", 0) == 1) {
            System.out.print(intent.getStringExtra("Homemodel"));
            String id, day, plan_type, com_name, mobile, state, city, addres, details, creatdatetime;


            id = intent.getStringExtra("id");
            LEAD_Id = id;
            Log.e("task to lead ", "Olderst id " + LEAD_Id);

            day = intent.getStringExtra("day");
            plan_type = intent.getStringExtra("plan_type");
            com_name = intent.getStringExtra("customer_name");
            mobile = intent.getStringExtra("mobile");
            state = intent.getStringExtra("state");
            city = intent.getStringExtra("city");
            addres = intent.getStringExtra("address");
            details = intent.getStringExtra("details");
            creatdatetime = intent.getStringExtra("createdateandtime");

            autoname.setText(com_name);
            automobile.setText(mobile);
            et_address.setText(addres);


            Log.e("intend data", intent.getStringExtra("id"));
            Log.e("intend data", intent.getStringExtra("day"));
            Log.e("intend data", intent.getStringExtra("plan_type"));
            Log.e("intend data", intent.getStringExtra("customer_name"));
            Log.e("intend data", intent.getStringExtra("mobile"));
            Log.e("intend data", intent.getStringExtra("state"));
            Log.e("intend data", intent.getStringExtra("city"));
            Log.e("intend data", intent.getStringExtra("address"));
            Log.e("intend data", intent.getStringExtra("details"));
            Log.e("intend data", intent.getStringExtra("createdateandtime"));
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

        activity.runOnUiThread(() -> Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {

            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String textphone = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    String textmail = data.getStringExtra(OcrCaptureActivity.TextBlockObject1);
                    automobile.setText(textphone);
                    et_email.setText(textmail);
//                    Toast.makeText(activity, "ph"+textphone, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(activity, "mail"+textmail, Toast.LENGTH_SHORT).show();
                    Log.e("phonnnnnnnnn", textphone);
                    //   Log.e("maalllllll",textmail);

                }
            }


        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
}

