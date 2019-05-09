package com.stellar.android.app.TeamLeadView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


import com.stellar.android.app.DashBordActivity;
import com.stellar.android.app.ManagerAssign.TabIdModel;
import com.stellar.android.app.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class TeamLeadActivity extends Fragment {
    private Activity activity;
    private TabLayout tabLayout;
    private ArrayList<TabIdModel> tabname=new ArrayList<>();
    private TeamAdapter adapter;
    private ViewPager viewPager;
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    private SharedPreferences preferences;
    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_team_lead,container,false);

        DashBordActivity.titleName("Team Lead");

        viewPager =view.findViewById(R.id.viewpagerteam);
        tabLayout =view. findViewById(R.id.tab_team);

        preferences = activity.getSharedPreferences("stelleruser", MODE_PRIVATE);
        userId=preferences.getString("empid","");

        Log.e("user id is ",userId);


        if (tabname.size()>0)
        {
            Log.e("TeamLead","Lis not 0 "+tabname);
            adapter = new TeamAdapter(getChildFragmentManager(), tabname);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }else {
            apicall();
        }
        return view;
    }

    private void apicall() {

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
                activity.runOnUiThread(() -> {
                    Log.e("network", e.getMessage());
                    Toast.makeText(activity, "network Problem", Toast.LENGTH_SHORT).show();
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

                                TabIdModel model=new TabIdModel();
                                model.setName(full_name);
                                model.setNameId(employee_id);

                                tabname.add(model);

                            }
                            activity.runOnUiThread(() -> {

                                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                                adapter = new TeamAdapter(getChildFragmentManager(), tabname);
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=activity;
    }
}
