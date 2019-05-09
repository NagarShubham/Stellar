package com.stellar.android.app.teamTask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.stellar.android.app.Constants;
import com.stellar.android.app.DashBordActivity;
import com.stellar.android.app.ManagerAssign.AprovedTaskActivity;
import com.stellar.android.app.ManagerAssign.NameTab_Adapter;
import com.stellar.android.app.ManagerAssign.TabIdModel;
import com.stellar.android.app.R;
import com.stellar.android.app.Utill;

import org.json.JSONArray;
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

public class TeamPandingTaskActivity extends AppCompatActivity {
    private String TAG = "TeamPandingTask ";

    private TabLayout tabLayout;
    private ArrayList<PandingModel> tabname=new ArrayList<>();
    private TabAdapeter adapter;
    private ViewPager viewPager;
    private ImageView backbutton;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    private String empid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_panding_task);

        preferences = getSharedPreferences("stelleruser", MODE_PRIVATE);
        empid = preferences.getString("empid", "");
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabbar);
        backbutton = findViewById(R.id.bacButton);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);

        backbutton.setOnClickListener(v -> finish());

        if (tabname.size()>0)
        {
            adapter = new TabAdapeter(getSupportFragmentManager(), tabname);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }else {
            tabApiCall();
        }
    }

    private void tabApiCall() {

        progressDialog.show();
        String key = Utill.getsha256("manager_child_name");
        Log.e(TAG+ "key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "manager_child_name")
                .add("key", key)
                .add("employee_id", empid)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Log.e(TAG + " network", e.getMessage());
                    Toast.makeText(TeamPandingTaskActivity.this, "network Problem", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string().trim();
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        Log.e(TAG + "message   ", object.getString("message"));


                        String msg = object.getString("message");
                        if (msg.equals("true")) {
                            JSONArray array = object.getJSONArray("data");

                            tabname.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject objectname = array.getJSONObject(i);

                                String full_name = objectname.getString("full_name");
                                String employee_id = objectname.getString("employee_id");

                                PandingModel model = new PandingModel();
                                model.setName(full_name);
                                model.setId(employee_id);
                                model.setDay("");

                                tabname.add(model);

                            }
                            runOnUiThread(() -> setDateAdapter());
                        }

                    } catch (Exception e) {
                        runOnUiThread(() -> progressDialog.dismiss());
                        e.getMessage();
                    }
                }
                runOnUiThread(() -> progressDialog.dismiss());
                Log.e(TAG + "aproveed Result", result);
            }
        });


    }

    private void setDateAdapter() {
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        progressDialog.dismiss();

        adapter = new TabAdapeter(getSupportFragmentManager(), tabname);
        viewPager.setAdapter(adapter);


            Intent intent=getIntent();
            if (intent!=null && intent.getStringExtra("name")!=null) {
                String name=intent.getStringExtra("name");

                Log.e("Name ","get "+name);
                for (int i = 0; i < tabname.size(); i++) {
                    if (name.equalsIgnoreCase(tabname.get(i).getName())) {
                        viewPager.setCurrentItem(i);

                    }
                }
            }


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
    }
}
