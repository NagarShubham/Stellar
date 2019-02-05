package coms.example.cwaa1.stellar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import coms.example.cwaa1.stellar.Scanner.OcrCaptureActivity;
import coms.example.cwaa1.stellar.Scanner.ScannerActivity;
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
    private EditText et_name, et_email, et_mobile, et_phone, et_comp_name, et_designation;
    private Spinner sp_typeofReq, sp_source, sp_category;
    private EditText et_address, et_description, et_department, et_leadvalue, et_closingDate;
    private Spinner sp_contactType;
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


        formTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        formTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
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
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


        if (getArguments() != null)

        {
            et_name.setText(getArguments().getString("tname"));
            et_mobile.setText(getArguments().getString("tmobile"));
            et_address.setText(getArguments().getString("tadd"));
            et_description.setText(getArguments().getString("tdetais"));
            et_comp_name.setText(getArguments().getString("tcompname"));
            temp_cate = (getArguments().getString("tcategory"));
            temp_source = (getArguments().getString("tsource"));
            temp_req = (getArguments().getString("ttreq"));
            temp_contact = getArguments().getString("ttype");
            //bundle.putString("ttype",model.getPlan_type());

            t = (getArguments().getString("ttime"));
            f = (getArguments().getString("tftime"));

            toTime.setText(t);
            formTime.setText(f);

            Log.e("tt : ft", t + " : " + f);
//            bundle.putString("ttime",model.getTotime());
//            bundle.putString("tftime",model.getFormtime());


            String[] temp = getResources().getStringArray(R.array.contacttype);
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].equals(temp_contact)) {
                    sp_contactType.setSelection(i);
                }
            }
        }

        multiapi();

        et_closingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear, mMonth, mDay, mHour, mMinute;

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                et_closingDate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + "");
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);  //this for past date not clickbale
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sname = et_name.getText().toString().trim();
                String smail = et_email.getText().toString().trim();
                String smobile = et_mobile.getText().toString().trim();
                String sphone = et_phone.getText().toString().trim();
                String scompname = et_comp_name.getText().toString().trim();
                String sdesignation = et_designation.getText().toString().trim();
                String sdepart = et_department.getText().toString().trim();
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

                Log.e("iddddddddddddddd", EMP_ID);


                //  Toast.makeText(getActivity(), "" +sleadvalue , Toast.LENGTH_SHORT).show();

                if (sname.equals("")) {
                    et_name.setError("is Empty");
                } else if (smail.equals("")) {
                    et_email.setError("is Empty");

                } else if (smobile.length() < 9) {
                    et_mobile.setError("10 no need");
                } else if (sphone.equals("")) {
                    et_phone.setError("is Empty");
                } else if (scompname.equals("")) {
                    et_comp_name.setError("is Empty");
                } else if (sdesignation.equals("")) {
                    et_designation.setError("is Empty");
                } else if (sdepart.equals("")) {
                    et_department.setError("is Empty");
                } else if (saddress.equals("")) {
                    et_address.setError("is Empty");
                } else if (sdescription.equals("")) {
                    et_description.setError("is Empty");
                } else if (sleadvalue.equals("")) {
                    et_leadvalue.setError("is Empty");
                } else if (sclosingdate.equals("")) {
                    et_closingDate.setError("is Empty");
                } else {
                    json(sname, smail, smobile, sphone, scompname, sdesignation, sdepart, ssource, sassignto, sleadstatus, saddress, sdescription, stypereq, sleadvalue, sclosingdate, scontactTyape, sleadtrype, scategory, formtime, totime);
                }
            }
        });

        return view;
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
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adaptercategoryName = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, categoryName);

                                        sp_category.setAdapter(adaptercategoryName);
                                        //  toast("not fond data");


                                    }
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
                                    }

                                });


                            } else {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        adaptersourceName = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, sourceName);

                                        sp_source.setAdapter(adaptersourceName);
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
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });

    }


    private void json(String sname, String smail, String smobile, String sphone,
                      String scompname, String sdesignation, String sdepart, String ssource,
                      String sassignto, String sleadstatus, String saddress, String sdescription,
                      String stypereq, String sleadvalue, String sclosingdate, String scontactTyape, String sleadtrype,
                      String scategory, String formtime, String totime) {


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
                .add("task_id", LEAD_Id)
                .build();
        Request request = new Request.Builder()
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

                        Toast.makeText(activity, "Network Problem", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG + "save result", result);
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        Log.e("message   ", object.getString("message"));
                        if (msg.equals("Lead Save Successfully")) {

                            toast("Lead Save Successfully");
                            Log.e("planeaaaddddddding", object.getString("data"));

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    LEAD_Id = "";
                                    STATUS = "New Lead";
                                    blankfilds();

                                }
                            });
                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    toast("Lead Not Save Successfully");
                                }
                            });
                        }

                    } catch (Exception e) {
                        Log.e("exepteion on add", e.getMessage());

                    }


                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });


    }

    private void blankfilds() {

        et_name.setText("");
        et_email.setText("");
        et_mobile.setText("");
        et_phone.setText("");
        et_comp_name.setText("");
        et_designation.setText("");
        et_department.setText("");
        toTime.setText("");

        formTime.setText("");
        et_address.setText("");
        et_description.setText("");
        //et_typeofReq.setText("");
        et_leadvalue.setText("");
        et_closingDate.setText("");


    }

    private void Findigid(View view) {

        et_name = view.findViewById(R.id.et_name);
        et_email = view.findViewById(R.id.et_mail);
        et_mobile = view.findViewById(R.id.et_mobile);
        et_phone = view.findViewById(R.id.et_phone);
        et_comp_name = view.findViewById(R.id.et_comp_name);
        et_designation = view.findViewById(R.id.et_designation);
        et_department = view.findViewById(R.id.et_department);
        sp_source = view.findViewById(R.id.et_source);
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

//        toTime.setText(t);
//        formTime.setText(f);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getContext(), OcrCaptureActivity.class);
                startActivityForResult(in, REQUEST_CODE);
            }
        });
        Intent intent = activity.getIntent();
        if (intent.getIntExtra("veri", 0) == 1) {
            System.out.print(intent.getStringExtra("model"));
            String id, day, plan_type, com_name, mobile, state, city, addres, details, creatdatetime;


            id = intent.getStringExtra("id");
            LEAD_Id = id;
            STATUS = "Task to Lead";
            day = intent.getStringExtra("day");
            plan_type = intent.getStringExtra("plan_type");
            com_name = intent.getStringExtra("customer_name");
            mobile = intent.getStringExtra("mobile");
            state = intent.getStringExtra("state");
            city = intent.getStringExtra("city");
            addres = intent.getStringExtra("address");
            details = intent.getStringExtra("details");
            creatdatetime = intent.getStringExtra("createdateandtime");

            et_name.setText(com_name);
            et_mobile.setText(mobile);
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

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {

            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String textphone = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    String textmail = data.getStringExtra(OcrCaptureActivity.TextBlockObject1);
                    et_mobile.setText(textphone);
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

