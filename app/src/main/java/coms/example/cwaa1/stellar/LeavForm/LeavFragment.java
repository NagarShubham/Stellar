package coms.example.cwaa1.stellar.LeavForm;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import coms.example.cwaa1.stellar.DashBordActivity;
import coms.example.cwaa1.stellar.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LeavFragment extends Fragment {
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    private Activity activity;

    private FloatingActionButton fabAdd;
    private RecyclerView rc_leave;
    private List<LeaveModel>list=new ArrayList<>();
private AdapterLeave adapterLeave;
    private ProgressDialog progressDialog;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LeavFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leav, container, false);
        rc_leave = view.findViewById(R.id.rcy_leav);
        fabAdd = view.findViewById(R.id.leave_add);

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        TextView title = activity.findViewById(R.id.tvtitile);
        title.setText("Add Leave");
        LinearLayoutManager layoutManager=new LinearLayoutManager(activity,OrientationHelper.VERTICAL,false);
        rc_leave.setLayoutManager(layoutManager);
        rc_leave.setItemAnimator(new DefaultItemAnimator());
        apicall();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLeaveapi();
            }
        });



        return view;
    }

    private void apicall() {
progressDialog.show();

        String key = getsha256("allLeaveList");
        Log.e("allLeaveList Key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "allLeaveList")
                .add("key", key)
                .add("employee_id", DashBordActivity.EmpId)

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
                Log.e("allLeaveList result ", result);
                if (response.isSuccessful()) {


                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equals("true")) {
                            JSONArray array = object.getJSONArray("data");
                            list.clear();
                            for (int i=0;i<array.length();i++)
                            {
                                JSONObject object1=array.getJSONObject(i);

                                String todate=object1.getString("to_date");
                                String formdate=object1.getString("from_date");
                                String reason=object1.getString("reason");
                                String reject_reason=object1.getString("reject_reason");
                                String leave_status=object1.getString("leave_status");
                                String leave_type=object1.getString("leave_type");

                                LeaveModel model=new LeaveModel();
                                model.setTodate(todate);
                                model.setFromdate(formdate);
                                model.setLeavType(leave_type);
                                model.setResonLeave(reason);
                                model.setAdminResonLeave(reject_reason);
                                model.setLeaveStatus(leave_status.replace(leave_status.charAt(0),String.valueOf(leave_status.charAt(0)).toUpperCase().charAt(0)));
                                list.add(model);

                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("allLeaveList api", "run");
                                    adapterLeave=new AdapterLeave(list);
                                    rc_leave.setAdapter(adapterLeave);
                                    progressDialog.dismiss();

                                    // dialog.cancel();
                                }
                            });

                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.getMessage();
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

    private void addLeaveapi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dview = inflater.inflate(R.layout.item_day_task, null);
        builder.setView(dview);
        final AlertDialog dialog = builder.create();
        dview.findViewById(R.id.li_dayweek).setVisibility(View.GONE);
        Log.e("Static Take id", DashBordActivity.EmpId);

        final TextView todate = dview.findViewById(R.id.leav_todate);
        final Spinner spinner = dview.findViewById(R.id.leav_sp);
        final TextView formdate = dview.findViewById(R.id.leav_fromdate);
        final EditText reson = dview.findViewById(R.id.leav_resion);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 2) {
                    todate.setVisibility(View.VISIBLE);

                } else {
                    todate.setVisibility(View.GONE);


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button save = dview.findViewById(R.id.leav_send);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String leavType = spinner.getSelectedItem().toString();
                String res = reson.getText().toString();
                String from_date = formdate.getText().toString().trim();
                String to_date = todate.getText().toString().trim();

                if (res.equals("")) {
                    reson.setError("is Empty");
                } else if (from_date.equals("")) {
                    formdate.setText("Select Date");
                } else {
                    progressDialog.show();

                    String key = getsha256("add_leave");
                    Log.e("add_leave Key", key);

                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("action", "add_leave")
                            .add("key", key)
                            .add("employee_id", DashBordActivity.EmpId)
                            .add("leave_type", leavType)
                            .add("from_date", from_date)
                            .add("to_date", to_date)
                            .add("reason", res)
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
                            Log.e("result ", result);
                            if (response.isSuccessful()) {


                                try {
                                    JSONObject object = new JSONObject(result);
                                    String msg = object.getString("message");
                                    if (msg.equals("true")) {
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //progressDialog.dismiss();
                                                apicall();
                                                dialog.cancel();
                                            }
                                        });

                                    } else {
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    e.getMessage();
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
            }
        });


        todate.setOnClickListener(new View.OnClickListener() {
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
//
//                                String s="";
//                                if ((monthOfYear + 1)<10)
//                                {
//                                    s="0"+(monthOfYear + 1);
//                                }else {
//                                    s=String.valueOf(monthOfYear + 1);
//                                }
//

                                todate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + "");


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);  //this for past date not clickbale


            }
        });
        formdate.setOnClickListener(new View.OnClickListener() {
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
                                formdate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + "");


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);  //this for past date not clickbale


            }
        });


        dview.findViewById(R.id.lev_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
