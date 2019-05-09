package com.stellar.android.app.WonLossLead;

import android.app.Activity;
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
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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

public class WonLossFragment extends Fragment {
    private Activity activity;
    private RecyclerView rcy_won;
    private RecyclerView rcy_loss;
    private ProgressBar progressBarWon;
    private ProgressBar progressBarlos;
    private List<WonLossmodel> wonlist=new ArrayList<>();
    private List<WonLossmodel> losslist=new ArrayList<>();

    private static int WON=0;
    private static int LOSS=0;
    public WonLossFragment() {
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
        View view = inflater.inflate(R.layout.fragment_win_loss, container, false);
        DashBordActivity.titleName("Won & Loss Lead ");

        rcy_won=view.findViewById(R.id.rcy_won);
        rcy_loss=view.findViewById(R.id.rcy_loss);

        progressBarWon=view.findViewById(R.id.probar_won);
        progressBarlos=view.findViewById(R.id.probar_los);

        progressBarWon.setVisibility(View.GONE);
        progressBarlos.setVisibility(View.GONE);

        rcy_won.setNestedScrollingEnabled(false);
        rcy_loss.setNestedScrollingEnabled(false);

        rcy_won.setLayoutManager(new LinearLayoutManager(activity, OrientationHelper.VERTICAL,false));
        rcy_loss.setLayoutManager(new LinearLayoutManager(activity, OrientationHelper.VERTICAL,false));

        apiCallWon(WON);
        apiCallLoss(LOSS);


        return view;
    }

    private void apiCallLoss(int page) {

        progressBarlos.setVisibility(View.VISIBLE);

        String key = DashBordActivity.getsha256("getwonandlostbyid");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "getwonandlostbyid")
                .add("key", key)
                .add("assign_to", DashBordActivity.EmpId)
                .add("page", String.valueOf(page))
                .add("status", "reject")

                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(() -> {
                    progressBarlos.setVisibility(View.GONE);
                    Toast.makeText(activity, "NetWork Problem", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result=response.body().string().trim();
                Log.e("lost"," Result "+result);

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);

                        String msg = jsonObject.getString("message");
                        if (msg.equalsIgnoreCase("true")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                           // losslist.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                WonLossmodel model = new WonLossmodel();

                                model.setId(object.getString("id"));
                                model.setName(object.getString("name"));
                                model.setEmail(object.getString("email"));
                                model.setMobile(object.getString("mobile"));


                                model.setOfficephoen(object.getString("office_phone"));
                                model.setCompany_name(object.getString("company_name"));
                                model.setDepartment(object.getString("department"));
                                model.setDesignation(object.getString("designation"));

                                model.setContacttype(object.getString("contact_type"));
                                model.setAddress(object.getString("address"));

                                model.setDescription(object.getString("description"));

                                model.setLeadValue(object.getString("expected_lead_value"));
                                model.setLead_type(object.getString("lead_type"));
                                model.setSource(object.getString("sourceName"));
                                model.setCategory(object.getString("categoryName"));
                                model.setType_of_requirement(object.getString("requirementName"));
                                model.setDate(object.getString("expected_closing_date"));
                                model.setTotime(object.getString("to_time"));
                                model.setFormtime(object.getString("from_time"));

                                losslist.add(model);
                            }

                            activity.runOnUiThread(() -> {


                                WonLossAdapter adapter=new WonLossAdapter(losslist);
                                rcy_loss.setAdapter(adapter);
                                progressBarlos.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();

                                adapter.setClickListener(position -> {
//                                    LOSS++;
//                                    apiCallLoss(LOSS);
                                });

                            });
                        }else activity.runOnUiThread(() -> progressBarlos.setVisibility(View.GONE));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else activity.runOnUiThread(() -> {
                    progressBarlos.setVisibility(View.GONE);
                    Toast.makeText(activity, "SomeThing is Wrong", Toast.LENGTH_SHORT).show();
                });

            }
        });




    }

    private void apiCallWon( int page) {

        progressBarWon.setVisibility(View.VISIBLE);
        String key = DashBordActivity.getsha256("getwonandlostbyid");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "getwonandlostbyid")
                .add("key", key)
                .add("assign_to", DashBordActivity.EmpId)
                .add("page", String.valueOf(page))
                .add("status", "won")

                .build();
         Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

         client.newCall(request).enqueue(new Callback() {
             @Override
             public void onFailure(Call call, IOException e) {
                 activity.runOnUiThread(() -> {
                     progressBarWon.setVisibility(View.GONE);
                     Toast.makeText(activity, "NetWork Problem", Toast.LENGTH_SHORT).show();
                 });
             }

             @Override
             public void onResponse(Call call, Response response) throws IOException {

                 String result=response.body().string().trim();
                Log.e("WonLost"," Result "+result);

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);

                        String msg = jsonObject.getString("message");
                        if (msg.equalsIgnoreCase("true")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            //wonlist.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                WonLossmodel model = new WonLossmodel();

                                model.setId(object.getString("id"));
                                model.setName(object.getString("name"));
                                model.setEmail(object.getString("email"));
                                model.setMobile(object.getString("mobile"));


                                model.setOfficephoen(object.getString("office_phone"));
                                model.setCompany_name(object.getString("company_name"));
                                model.setDepartment(object.getString("department"));
                                model.setDesignation(object.getString("designation"));

                                model.setContacttype(object.getString("contact_type"));
                                model.setAddress(object.getString("address"));

                                model.setDescription(object.getString("description"));

                                model.setLeadValue(object.getString("expected_lead_value"));
                                model.setLead_type(object.getString("lead_type"));
                                model.setSource(object.getString("sourceName"));
                                model.setCategory(object.getString("categoryName"));
                                model.setType_of_requirement(object.getString("requirementName"));
                                model.setDate(object.getString("expected_closing_date"));
                                model.setTotime(object.getString("to_time"));
                                model.setFormtime(object.getString("from_time"));

                                wonlist.add(model);
                            }

                            activity.runOnUiThread(() -> {

                                WonLossAdapter adapter=new WonLossAdapter(wonlist);
                                rcy_won.setAdapter(adapter);
                                progressBarWon.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();
                                adapter.setClickListener(position -> {
                                    //Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();
//                                    WON++;
//                                    apiCallWon(WON);
                                    adapter.notifyDataSetChanged();
                                });

                            });
                        }else activity.runOnUiThread(() -> progressBarWon.setVisibility(View.GONE));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else activity.runOnUiThread(() -> {
                    Toast.makeText(activity, "SomeThing is Wrong", Toast.LENGTH_SHORT).show();
                    progressBarWon.setVisibility(View.GONE);
                });

             }
         });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
}
