package com.stellar.android.app.Weeklyplane;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.stellar.android.app.DashBordActivity;
import com.stellar.android.app.R;
import com.stellar.android.app.Scanner.OcrCaptureActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class WeekPlanlyActivity extends Fragment {
    private static final String TAG = "Weekly plan";
    private Activity activity;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static String EMP_ID;
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    private static final int REQUEST_CODE = 101;
    private EditText et_add, et_details;
    private TextView sun, mon, tue, wed, thu, fri, sat;
    private Spinner state, city, sp_planeType;

    private EditText company_name;
    private Spinner purpose;
    private TextView formTime, toTime;
    private Spinner sp_category, sp_source, sp_req;

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

    private Button saveW, next;
    private String stateid, cityid;
    private List<String> stateLists;
    private List<String> stateListsid;
    private List<String> cityLists;
    private List<String> cityListsid;
    private ArrayAdapter stateadapter;
    private ArrayAdapter cityadapter;
    private LinearLayout linearLayout;
    private ImageView backButton;
    private ImageView scan;
    private ProgressDialog progressDialog;

    public static String DAY_NAME = "";


    private List<String> autoName = new ArrayList<>();
    private List<String> autoMobile = new ArrayList<>();

    private AutoCompleteTextView auto_customername;
    private AutoCompleteTextView auto_mobile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_week_planly);
    }


    @android.support.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @android.support.annotation.Nullable ViewGroup container, @android.support.annotation.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_week_planly, container, false);
        preferences = activity.getSharedPreferences("stelleruser", MODE_PRIVATE);
        editor = preferences.edit();
        EMP_ID = preferences.getString("empid", "");
        Log.e("weekliy emp id", EMP_ID);
        DashBordActivity.menuchek(1);

        progressDialog = new ProgressDialog(activity);

        auto_customername = view.findViewById(R.id.auto_customername);

        auto_mobile = view.findViewById(R.id.auto_mobile);
        et_add = view.findViewById(R.id.et_add);
        et_details = view.findViewById(R.id.et_details);
        state = view.findViewById(R.id.sp_state);
        city = view.findViewById(R.id.sp_city);


        company_name = view.findViewById(R.id.et_company_name);
        purpose = view.findViewById(R.id.et_purpose);
        toTime = view.findViewById(R.id.et_to_time);
        formTime = view.findViewById(R.id.et_from_time);

        sp_category = view.findViewById(R.id.sp_category);
        sp_source = view.findViewById(R.id.sp_source);
        sp_req = view.findViewById(R.id.sp_req);


        sp_planeType = view.findViewById(R.id.sp_planeType);
        saveW = view.findViewById(R.id.btn_saveW);
        next = view.findViewById(R.id.next);
        scan = activity.findViewById(R.id.scan);
        next.setVisibility(View.GONE);
        scan.setVisibility(View.VISIBLE);

        DashBordActivity.titleName("Weekly Task");


        stateLists = new ArrayList<>();
        stateListsid = new ArrayList<>();
        cityLists = new ArrayList<>();
        cityListsid = new ArrayList<>();
        Tabid(view);

        getState();

        multiapi();

        autoSuggetionApi();


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
            mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    toTime.setText(selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });


        next.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), DashBordActivity.class);
            startActivity(intent);

        });

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //  ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                //  Toast.makeText(WeekPlanlyActivity.this, "" + stateLists.get(position), Toast.LENGTH_SHORT).show();
                stateid = stateListsid.get(position);
                Log.e("state id given", stateListsid.get(position));
                String s = stateListsid.get(position).trim();

                getCity(s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cityid = cityListsid.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        saveW.setOnClickListener(v -> {


            Log.e("useidddd", EMP_ID);
            Log.e("DAYS", DAY_NAME);

            String cname = auto_customername.getText().toString().trim();
            String cmobile = auto_mobile.getText().toString().trim();
            String cadress = et_add.getText().toString().trim();
            String cdetails = et_details.getText().toString().trim();
            String cplanetype = sp_planeType.getSelectedItem().toString();

            String ccname = company_name.getText().toString().trim();
            String cpurpose = purpose.getSelectedItem().toString().trim();
            String ctotime = toTime.getText().toString().trim();
            String cfromtime = formTime.getText().toString().trim();


            String cstate = stateid;
            String ccity = cityid;


            if (cname.equals("")) {
                auto_customername.setError("Is Empty");
            } else if (cmobile.length() < 9) {
                auto_mobile.setError("10 no need");
            } else if (cadress.equals("")) {
                et_add.setError("Is Empty");
            } else if (cdetails.equals("")) {
                et_details.setError("Is Empty");
            } else if (ccname.equals("")) {
                company_name.setError("empty");
            } else {


                json(cname, cmobile, cadress, cdetails, cplanetype, cstate, ccity, ccname, cpurpose, ctotime, cfromtime);

            }


        });


        scan.setOnClickListener(v -> {
            Intent in = new Intent(getContext(), OcrCaptureActivity.class);
            startActivityForResult(in, REQUEST_CODE);
        });


        return view;


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
                                auto_customername.setAdapter(nameAdaper);
                                auto_customername.setThreshold(1);
                                ArrayAdapter mobileAdapet = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, autoMobile);
                                auto_mobile.setAdapter(mobileAdapet);
                                auto_mobile.setThreshold(1);


                                Log.e("nameList ", autoName.toString());
                                Log.e("moblieList ", autoMobile.toString());

                                auto_customername.setOnItemClickListener((parent, view1, position, id) -> {

                                    String selectedItem = (String) parent.getItemAtPosition(position);
                                    auto_mobile.setText(autoMobile.get(autoName.indexOf(selectedItem)));

                                    //     auto_mobile.setText(autoMobile.get(position));
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

                activity.runOnUiThread(() -> {
                    progressDialog.dismiss();
                    toast("Network Problem");
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

                                    sp_req.setAdapter(adapterreqName);
                                    adapterreqName.notifyDataSetChanged();

                                    sp_req.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                                    sp_req.setAdapter(adapterreqName);
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

    private void Tabid(View view) {

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        Log.e("Dayyyyyyyyy", dayOfTheWeek);


        linearLayout = view.findViewById(R.id.tab);

        sun = view.findViewById(R.id.tab_sun);
        mon = view.findViewById(R.id.tab_mon);
        tue = view.findViewById(R.id.tab_tue);
        wed = view.findViewById(R.id.tab_wed);
        thu = view.findViewById(R.id.tab_thu);
        fri = view.findViewById(R.id.tab_fri);
        sat = view.findViewById(R.id.tab_sat);


        setday(dayOfTheWeek);


        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                //  Toast.makeText(WeekPlanlyActivity.this, "sun", Toast.LENGTH_SHORT).show();
            }
        });
        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                //  Toast.makeText(WeekPlanlyActivity.this, "mon", Toast.LENGTH_SHORT).show();
            }
        });
        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                // Toast.makeText(WeekPlanlyActivity.this, "tue", Toast.LENGTH_SHORT).show();
            }
        });
        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                // Toast.makeText(WeekPlanlyActivity.this, "wed", Toast.LENGTH_SHORT).show();
            }
        });
        thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                //  Toast.makeText(WeekPlanlyActivity.this, "thu", Toast.LENGTH_SHORT).show();
            }
        });
        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                // Toast.makeText(WeekPlanlyActivity.this, "fri", Toast.LENGTH_SHORT).show();
            }
        });
        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                //  Toast.makeText(WeekPlanlyActivity.this, "sat", Toast.LENGTH_SHORT).show();
            }
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

    private void json(String cname, String cmobile, String cadress, String cdetails, String cplanetype,
                      String cstate, String ccity, String ccname, String cpurpose,
                      String ctotime, String cfromtime) {


        progressDialog.show();
        String key = getsha256("plan_task_save");
        Log.e("key", key);


        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "plan_task_save")
                .add("key", key)
                .add("day", DAY_NAME)

                .add("company_name", ccname)
                .add("purpose", cpurpose)
                .add("to_time", ctotime)
                .add("from_time", cfromtime)
                .add("source", sourId)
                .add("category", cateId)
                .add("type_of_requiremt", reqstId)
                .add("plan_type", cplanetype)
                .add("customer_name", cname)
                .add("mobile", cmobile)
                .add("state", cstate)
                .add("city", ccity)
                .add("address", cadress)
                .add("details", cdetails)
                .add("employee_id", EMP_ID)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG + " result", result);
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equals("Plan Save Successfully")) {
                            toast("Plan Save Successfully");
                            Log.e("planeaaaddddddding", object.getString("data"));

                            activity.runOnUiThread(() -> {
                                progressDialog.dismiss();
                                auto_customername.setText("");
                                auto_mobile.setText("");
                                et_add.setText("");
                                et_details.setText("");
                                company_name.setText("");
                               // purpose.setText("");
                                toTime.setText("");
                                formTime.setText("");
                            });
                        } else {
                            dialogclose();
                            toast("Not add");
                        }
                    } catch (Exception e) {
                        dialogclose();
                        Log.e("exepteion on add", e.getMessage());

                    }
                } else {
                    dialogclose();
                }
            }
        });
    }

    public void dialogclose() {
        activity.runOnUiThread(() -> progressDialog.dismiss());

    }


    private void getCity(String s) {

        String key = getsha256("getCity");
        Log.e("key", key);
        Log.e("state yaaaa", s);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "getCity")
                .add("key", key)
                .add("state_id", s)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                activity.runOnUiThread(() -> toast("Network Problem"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string().trim();
                Log.e("Sucess city", result);

                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equals("true")) {

                            JSONArray array = object.getJSONArray("data");

                            cityLists.clear();
                            cityListsid.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object2 = array.getJSONObject(i);


                                String sid = object2.getString("id");
                                String snma = object2.getString("name");

//                                Log.e("city id  ", object2.getString("id"));
//                                Log.e("city name  ", object2.getString("name"));

                                cityLists.add(snma);
                                cityListsid.add(sid);
                                activity.runOnUiThread(() -> {

                                    cityadapter = new ArrayAdapter(activity, R.layout.custom_spiner, cityLists) {

                                    };
                                    city.setAdapter(cityadapter);
                                });

                            }
                        } else {
                            toast("not valid user");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }
        });


    }

    private void getState() {

        String key = getsha256("getState");
        Log.e("key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "getState")
                .add("key", key)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                activity.runOnUiThread(() -> toast("Network Problem"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equals("true")) {

                            JSONArray array = object.getJSONArray("data");

                            stateLists.clear();
                            stateListsid.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object2 = array.getJSONObject(i);


                                String sid = object2.getString("id");
                                String snma = object2.getString("name");

//                                Log.e("state id  ", object2.getString("id"));
//                                Log.e("state name  ", object2.getString("name"));

                                stateLists.add(snma);
                                stateListsid.add(sid);
                                activity.runOnUiThread(() -> {

                                    stateadapter = new ArrayAdapter(activity, R.layout.custom_spiner, stateLists);
                                  String sta= preferences.getString(getString(R.string.state),"");
                                  Log.e("statess ","ddd "+sta);
                                    state.setAdapter(stateadapter);
                                    state.setSelection(stateLists.indexOf(sta));
                                   // state.setAdapter(stateadapter);

                                });

                            }
                        } else {
                            toast("not valid user");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                Log.e("Sucess", result);


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

        activity.runOnUiThread(() -> Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("WeekLy Activity", "on activity" + data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String textphone = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    String textmail = data.getStringExtra(OcrCaptureActivity.TextBlockObject1);
                    String textname = data.getStringExtra(OcrCaptureActivity.TextBlockObject2);
                    auto_mobile.setText(textphone);
                    auto_customername.setText(textname);


                    Log.e("phonnnnnnnnn", textphone);
                    //   Log.e("maalllllll",textmail);

                }
            }


            //  toast(Objects.requireNonNull(data).getStringExtra("email"));

        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }


}


