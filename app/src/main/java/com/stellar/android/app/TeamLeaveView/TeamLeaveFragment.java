package com.stellar.android.app.TeamLeaveView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.stellar.android.app.DashBordActivity;
import com.stellar.android.app.LeavForm.AdapterLeave;
import com.stellar.android.app.LeavForm.LeaveModel;
import com.stellar.android.app.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TeamLeaveFragment extends Fragment {
    private Activity activity;
    private String id ;
    private String name;
    private RecyclerView rc_leave;
    private List<LeaveModel> list=new ArrayList<>();
    private AdapterLeave adapterLeave;

    public TeamLeaveFragment() {
        // Required empty public constructor
    }

 private TextView textView;

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
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_team_leave, container, false);
    textView=view.findViewById(R.id.k);
    if (getArguments()!=null)
    {
        id=getArguments().getString("rootId");
        name=getArguments().getString("root");
        textView.setText(getArguments().getString("root")+"  "+getArguments().getString("rootId"));
    textView.setVisibility(View.GONE);
    }
        rc_leave = view.findViewById(R.id.rcy_teamLeave);
        LinearLayoutManager layoutManager=new LinearLayoutManager(activity,OrientationHelper.VERTICAL,false);
        rc_leave.setLayoutManager(layoutManager);
        rc_leave.setItemAnimator(new DefaultItemAnimator());




    api();
    return view;
    }

    private void api() {

        DashBordActivity.progressDialog.show();

        String key = DashBordActivity.getsha256("allLeaveList");
        Log.e("allLeaveList Key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "allLeaveList")
                .add("key", key)
                .add("employee_id", id)

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
                        DashBordActivity.progressDialog.dismiss();
                        Toast.makeText(activity, "Network Problem", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                String result = response.body().string();
                Log.e("TeamLeave result ", result);
                if (response.isSuccessful()) {


                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equals("true")) {
                            JSONArray array = object.getJSONArray("data");
                            list.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object1 = array.getJSONObject(i);

                                String todate = object1.getString("to_date");
                                String formdate = object1.getString("from_date");
                                String reason = object1.getString("reason");
                                String reject_reason = object1.getString("reject_reason");
                                String leave_status = object1.getString("leave_status");
                                String leave_type = object1.getString("leave_type");

                                LeaveModel model = new LeaveModel();
                                model.setTodate(todate);
                                model.setFromdate(formdate);
                                model.setLeavType(leave_type);
                                model.setResonLeave(reason);
                                model.setAdminResonLeave(reject_reason);
                                model.setLeaveStatus(leave_status.replace(leave_status.charAt(0), String.valueOf(leave_status.charAt(0)).toUpperCase().charAt(0)));
                                list.add(model);

                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("allLeaveList api", "run");
                                    adapterLeave = new AdapterLeave(list);
                                    rc_leave.setAdapter(adapterLeave);
                                    DashBordActivity.progressDialog.dismiss();

                                    // dialog.cancel();
                                }
                            });

                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DashBordActivity.progressDialog.dismiss();
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
                            DashBordActivity.progressDialog.dismiss();
                        }
                    });
                }

            }
        });

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    this.activity=activity;

    }
}
