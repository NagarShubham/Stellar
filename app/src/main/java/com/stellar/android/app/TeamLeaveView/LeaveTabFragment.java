package com.stellar.android.app.TeamLeaveView;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import com.stellar.android.app.DashBordActivity;
import com.stellar.android.app.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LeaveTabFragment extends Fragment {
    private Activity activity;
    private TabLayout tabLayout;
    private ArrayList<LeaveIdModel> tabname=new ArrayList<>();
    private LeaveTabAdapter adapter;
    private ViewPager viewPager;

    public LeaveTabFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_leave_tab, container, false);
        viewPager =view.findViewById(R.id.viewpagerleave);
        tabLayout =view. findViewById(R.id.tab_leave);

        DashBordActivity.titleName("Team Leave List");

        if (tabname.size()>0)
        {
            adapter = new LeaveTabAdapter(getChildFragmentManager(), tabname);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
//            Toast.makeText(activity, "reSetData", Toast.LENGTH_SHORT).show();

        }
        else {
            apicall();

        }

    return view;

    }

    private void apicall() {


        String key = DashBordActivity.getsha256("manager_child_name");
        Log.e("manager_child_name key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "manager_child_name")
                .add("key", key)
                .add("employee_id", DashBordActivity.EmpId)
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
                        Toast.makeText(activity, "network Problem", Toast.LENGTH_SHORT).show();

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
                            JSONArray array = object.getJSONArray("data");

                            tabname.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject objectname = array.getJSONObject(i);

                                String full_name = objectname.getString("full_name");

                                String employee_id = objectname.getString("employee_id");

                                LeaveIdModel model=new LeaveIdModel();
                                model.setName(full_name);
                                model.setId(employee_id);

                                tabname.add(model);

                            }
                            activity.runOnUiThread(() -> {

                                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                                adapter = new LeaveTabAdapter(getChildFragmentManager(), tabname);
                                viewPager.setAdapter(adapter);
                                tabLayout.setupWithViewPager(viewPager);

                                viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

                                tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
                                    @Override
                                    public void onTabSelected(TabLayout.Tab tab) {


                                        viewPager.setCurrentItem(tab.getPosition());


                                    }

                                    @Override
                                    public void onTabUnselected(TabLayout.Tab tab) {

                                    }

                                    @Override
                                    public void onTabReselected(TabLayout.Tab tab) {
                                    }
                                });
                            });
                        }
                    } catch (Exception e)
                    {
                        e.getMessage();
                    }
                }
                Log.e("TeamLead Tabname Resul ",result);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=activity;
    }
}