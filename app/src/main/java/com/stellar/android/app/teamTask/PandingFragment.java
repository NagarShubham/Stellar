package com.stellar.android.app.teamTask;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.stellar.android.app.AddNew_LeadActivity;
import com.stellar.android.app.Constants;
import com.stellar.android.app.DashBordActivity;
import com.stellar.android.app.R;
import com.stellar.android.app.Task_List.AdapterList;
import com.stellar.android.app.Task_List.ListModel;
import com.stellar.android.app.Utill;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PandingFragment extends Fragment {
    public static final String TAG="PandingFragment ";
    private Activity activity;
    private ProgressBar progressBar;
    private TextView tvNodata;

    private String id;
    private String name;
    private String day;
    private RecyclerView recyclerView;
    private TextView sun, mon, tue, wed, thu, fri, sat;
    public static String DAY_NAME = "sunday";
    private ArrayList<ListModel> list=new ArrayList<>();
    private TaskListAdapter adapterList;
    public PandingFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id=getArguments().getString(Constants.KEY_ID);
            name=getArguments().getString(Constants.KEY_NAME);
            day=getArguments().getString(Constants.KEY_DAY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_panding, container, false);
        recyclerView=view.findViewById(R.id.rcypanding);
        progressBar=view.findViewById(R.id.probar);
        tvNodata=view.findViewById(R.id.tvNodata);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, OrientationHelper.VERTICAL,false));

        tabID(view);
       if (activity.getIntent()!=null && activity.getIntent().getStringExtra(Constants.KEY_DAY)!=null)
       {
           setday(activity.getIntent().getStringExtra(Constants.KEY_DAY));

       }else setday("Tuesday");



    return view;}

    private void tabID(View view) {

        sun = view.findViewById(R.id.tab_lsun);
        mon = view.findViewById(R.id.tab_lmon);
        tue = view.findViewById(R.id.tab_ltue);
        wed = view.findViewById(R.id.tab_lwed);
        thu = view.findViewById(R.id.tab_lthu);
        fri = view.findViewById(R.id.tab_lfri);
        sat = view.findViewById(R.id.tab_lsat);

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

            if (dayOfTheWeek .equalsIgnoreCase("Monday")) {
                mon.setBackgroundColor(Color.WHITE);
                mon.setTextColor(Color.parseColor("#58BDC7"));
                DAY_NAME = "monday";
            }
        if (dayOfTheWeek .equalsIgnoreCase("Tuesday"))  {
                tue.setBackgroundColor(Color.WHITE);
                tue.setTextColor(Color.parseColor("#58BDC7"));
                DAY_NAME = "tuesday";
            }
        if (dayOfTheWeek .equalsIgnoreCase("Wednesday")) {
                wed.setBackgroundColor(Color.WHITE);
                wed.setTextColor(Color.parseColor("#58BDC7"));
                DAY_NAME = "wednesday";
            }
        if (dayOfTheWeek .equalsIgnoreCase("Thursday"))  {
                thu.setBackgroundColor(Color.WHITE);
                thu.setTextColor(Color.parseColor("#58BDC7"));
                DAY_NAME = "thursday";
            }
        if (dayOfTheWeek .equalsIgnoreCase("Friday")) {
                fri.setBackgroundColor(Color.WHITE);
                fri.setTextColor(Color.parseColor("#58BDC7"));
                DAY_NAME = "friday";
            }
        if (dayOfTheWeek .equalsIgnoreCase("Saturday")) {
                sat.setBackgroundColor(Color.WHITE);
                sat.setTextColor(Color.parseColor("#58BDC7"));
                DAY_NAME = "saturday";
            }
        if (dayOfTheWeek .equalsIgnoreCase("Sunday")) {
                sun.setBackgroundColor(Color.WHITE);
                sun.setTextColor(Color.parseColor("#58BDC7"));
                DAY_NAME = "sunday";
            }

            api();
    }

    private void api() {

        Log.e(TAG,"DAy "+DAY_NAME);


        progressBar.setVisibility(View.VISIBLE);
        String key = Utill.getsha256("List_for_48hour");
        Log.e(TAG + " APi", key);


        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "List_for_48hour")
                .add("key", key)
                .add("employee_id",id)
                .add("day", DAY_NAME)
                .build();
         Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                activity.runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                     Toast.makeText(activity, "NetWork Problem", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string().trim();
                Log.e(TAG + "Api", " Result " + result);
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equalsIgnoreCase("true")) {

                            JSONArray jsonArray = object.getJSONArray("data");
                            list.clear();
                            if (jsonArray.length() > 0) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objectmail = jsonArray.getJSONObject(i);



                                    String com_name = objectmail.getString("customer_name");
                                    String snma = objectmail.getString("plan_type");

                                    ListModel model = new ListModel();
                                    model.setId(objectmail.getString("employee_id"));
                                    model.setDay(objectmail.getString("day"));
                                    model.setPlan_type(objectmail.getString("plan_type"));
                                    model.setCustomer_name(com_name);
                                    model.setMobile(objectmail.getString("mobile")); //yha tk kr liya he
                                    model.setState(objectmail.getString("state"));
                                    model.setCity(objectmail.getString("city"));

                                    model.setAddress(objectmail.getString("address"));
                                    model.setDetails(objectmail.getString("details"));
                                    model.setTask_status(objectmail.getString("task_status"));
                                    model.setCreatedateandtime(objectmail.getString("createdateandtime"));

                                    model.setCategory(objectmail.getString("category"));
                                    model.setCompanyName(objectmail.getString("company_name"));
                                    model.setPurpose(objectmail.getString("purpose"));
                                    model.setFormtime(objectmail.getString("for_time"));
                                    model.setTotime(objectmail.getString("to_time"));
                                    model.setTypeofRequest(objectmail.getString("type_of_requirement"));
                                    model.setSource(objectmail.getString("source"));

                                    list.add(model);
                                }

                                activity.runOnUiThread(() -> {
                                    setadapter();
                                    progressBar.setVisibility(View.GONE);
                                    tvNodata.setVisibility(View.GONE);

                                });


                            } else {
                                activity.runOnUiThread(() -> {
                                    setadapter();
                                    progressBar.setVisibility(View.GONE);
                                    tvNodata.setVisibility(View.VISIBLE);
                                });
                            }



                        }else activity.runOnUiThread(() -> {progressBar.setVisibility(View.GONE);
                            Toast.makeText(activity, "Try Again", Toast.LENGTH_SHORT).show();});


                    } catch (JSONException e) {
                        e.printStackTrace();
                        activity.runOnUiThread(() -> {progressBar.setVisibility(View.GONE);
                            Toast.makeText(activity, "Some Thign is Wrong", Toast.LENGTH_SHORT).show();});
                    }

                } else activity.runOnUiThread(() -> {progressBar.setVisibility(View.GONE);
                    Toast.makeText(activity, "No Data Found", Toast.LENGTH_SHORT).show();});
            }
        });




    }

    private void setadapter() {

        adapterList= new TaskListAdapter(list);
        recyclerView.setAdapter(adapterList);
        adapterList.notifyDataSetChanged();


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=activity;
    }
}
