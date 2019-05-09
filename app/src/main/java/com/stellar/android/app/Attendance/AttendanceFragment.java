package com.stellar.android.app.Attendance;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.stellar.android.app.DashBordActivity;
import com.stellar.android.app.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AttendanceFragment extends Fragment {
    private Activity activity;
    private ArrayList monthList = new ArrayList();
    private RecyclerView rcy_attendance;
    private ProgressBar progressBar;
    private List<AttendenceModel> list = new ArrayList<>();
    private Spinner sp_month;

    private List<String> datelist=new ArrayList<>();
    private List<String> statuslist=new ArrayList<>();
    private String year,month;

    private int monthForcalender;
    public AttendanceFragment() {
    }

    public static AttendanceFragment newInstance(String param1, String param2) {
        AttendanceFragment fragment = new AttendanceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        sp_month = view.findViewById(R.id.sp_monthname);
        rcy_attendance = view.findViewById(R.id.rcy_attendance);
        progressBar=view.findViewById(R.id.probar);
        rcy_attendance.setLayoutManager(new GridLayoutManager(activity, 2));


        DashBordActivity.titleName("Attendance");

        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");

        ArrayAdapter spadapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, monthList);
        sp_month.setAdapter(spadapter);

        sp_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("month name", monthList.get(position).toString());
                monthForcalender=position;
                String p;
                if ((position+1)<10) {
                    p="0"+(position+1);
                month="0"+(position+1);
                }
                else
                {
                    p=""+(position+1);
                month=""+(position+1);
                }
                Log.e("month name", "" + p);


                attenApiCall(p);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return view;
    }

    private void attenApiCall(String p) {
        progressBar.setVisibility(View.VISIBLE);
        String key = DashBordActivity.getsha256("getemployeeattendancelist");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "getemployeeattendancelist")
                .add("key", key)
                .add("employee_id", DashBordActivity.EmpId)
                .add("month",p)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(() ->{
                    progressBar.setVisibility(View.GONE);
                 Toast.makeText(activity, "NetWork Problem", Toast.LENGTH_SHORT).show();});

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result =response.body().string().trim();
                Log.e("Attendece ","Result "+result);

                if (response.isSuccessful())
                {
                    try {

                        JSONObject jsonObject=new JSONObject(result);
                        String msg=jsonObject.getString("message");
                        if (msg.equalsIgnoreCase("true"))
                        {
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            datelist.clear();
                            statuslist.clear();
                            list.clear();

                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject object=jsonArray.getJSONObject(i);

                                statuslist.add(object.getString("attendance_status"));

                                String date=object.getString("create_date_time");
                                String [] spi=date.split("-");
                                year=spi[0];
                                month=spi[1];
                                datelist.add(spi[2]);
                            }
                            activity.runOnUiThread(() -> {

                                Calendar calendar1 = Calendar.getInstance();
                               int yar=0;
                                try {
                                    yar=Integer.parseInt(year);
                                }catch (Exception e){
                                    yar=Calendar.getInstance().get(Calendar.YEAR);;

                                }
                                calendar1.set(Calendar.YEAR,yar );
                                calendar1.set(Calendar.MONTH, monthForcalender);
                             int numDays = calendar1.getActualMaximum(Calendar.DATE);

                             Log.e("day in moth",""+numDays);
                                for (int i = 1; i < numDays+1; i++) {

                                    if (i<10)
                                    {
                                        String num=0+""+i;
                                        if (datelist.contains(num)){
                                           String sta= statuslist.get(datelist.indexOf(num));
                                            list.add(new AttendenceModel(""+num,month,""+yar,sta));
                                        }else {
                                            list.add(new AttendenceModel(""+num,month,""+yar,"reject"));
                                        }
                                    }else {
                                        if (datelist.contains(String.valueOf(i))){
                                            String sta= statuslist.get(datelist.indexOf(String.valueOf(i)));
                                            list.add(new AttendenceModel(""+i,month,""+yar,sta));
                                        }else {
                                            list.add(new AttendenceModel(""+i,month,""+yar,"reject"));
                                        }
                                    }
                                }

                                AttendenceAdapter adapter =new AttendenceAdapter(list,activity);
                                rcy_attendance.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();

                            });
                        }else activity.runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(activity, "Try Again", Toast.LENGTH_SHORT).show();
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else activity.runOnUiThread(() ->progressBar.setVisibility(View.GONE));
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
}
