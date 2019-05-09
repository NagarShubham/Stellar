package com.stellar.android.app.ManagerAssign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import com.stellar.android.app.R;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AprovedTaskActivity extends AppCompatActivity implements RequestListFragment.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ArrayList<TabIdModel> tabname=new ArrayList<>();
    private NameTab_Adapter adapter;
    private ViewPager viewPager;
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    private SharedPreferences preferences;
    private String userId;
    private String TAG = "AprovedTaskActivity ";
    private ImageView backbutton;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aproved_task);


        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabbar);
        backbutton = findViewById(R.id.bacButton);

        preferences = getSharedPreferences("stelleruser", MODE_PRIVATE);
        userId = preferences.getString("empid", "");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);

        backbutton.setOnClickListener(v -> finish());

        if (tabname.size()>0)
        {
            adapter = new NameTab_Adapter(getSupportFragmentManager(), tabname);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);

        }else {
            apicall();
        }


    }

    private void apicall() {

        progressDialog.show();
        String key = getsha256("manager_child_name");
        Log.e("manager_child_name key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "manager_child_name")
                .add("key", key)
                .add("employee_id", userId)
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
                    Toast.makeText(AprovedTaskActivity.this, "network Problem", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
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

                                TabIdModel model = new TabIdModel();
                                model.setName(full_name);
                                model.setNameId(employee_id);

                                tabname.add(model);

                            }
                            runOnUiThread(() -> {

                                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                                progressDialog.dismiss();

                                adapter = new NameTab_Adapter(getSupportFragmentManager(), tabname);
                                viewPager.setAdapter(adapter);


                                Intent intent=getIntent();
                                if (intent!=null && intent.getStringExtra("senderName")!=null) {
                                    String name=intent.getStringExtra("senderName");
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


                            });
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


    @Override
    public void onFragmentInteraction(Uri uri) {

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

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AprovedTaskActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
