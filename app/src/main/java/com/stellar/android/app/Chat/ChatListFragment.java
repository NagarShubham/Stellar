package com.stellar.android.app.Chat;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import com.stellar.android.app.DashBordActivity;
import com.stellar.android.app.R;
import com.stellar.android.app.SharedPreference;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatListFragment extends Fragment {
  private Activity activity;
  private RecyclerView rcy_chatList;
    private ArrayList<ChatListModel> arrayList=new ArrayList<>();
    private SharedPreference sharedPreference;
    private String value = "";



    public ChatListFragment() {
        // Required empty public constructor
    }

    public static ChatListFragment newInstance(String param1, String param2) {
        ChatListFragment fragment = new ChatListFragment();
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

        View view= inflater.inflate(R.layout.fragment_chat_list, container, false);
        rcy_chatList=view.findViewById(R.id.rcy_chatList);

        DashBordActivity.titleName("Chat");

        sharedPreference = new SharedPreference(activity);
        //adminid = sharedPreference.getUid();
        LinearLayoutManager layoutManager=new LinearLayoutManager(activity,OrientationHelper.VERTICAL,false);
        rcy_chatList.setItemAnimator(new DefaultItemAnimator());
        rcy_chatList.setLayoutManager(layoutManager);

        chatListApi();

        return view;
    }

    private void chatListApi() {
        value = sharedPreference.getJsonArray();

        String key = DashBordActivity.getsha256("allParentAndChildName");
        Log.e("Chat key", key);

        DashBordActivity.progressDialog.show();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "allParentAndChildName")
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
                activity.runOnUiThread(() -> {

                    Log.e("network", e.getMessage());
                    //progressBar.setVisibility(View.GONE);
                    DashBordActivity.progressDialog.dismiss();
                    Toast.makeText(getActivity(), "network Problem", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Log.e("chat list result", result);
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);

                        String msg = object.getString("message");
                        Log.e("message   ",msg);

                        if (msg.equals("true")) {
                            arrayList.clear();
                            JSONArray array = object.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject objectname = array.getJSONObject(i);
                                    String id=objectname.getString("employee_id");
                                    String name=objectname.getString("full_name");
                                    String count = "0";
                                    if (value != null) {
                                        Log.v("login", value);
                                        JSONArray jsonArray1 = new JSONArray(value);
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                            if (id.equals(jsonObject1.getString("Id"))) {
                                                count = String.valueOf(jsonObject1.getInt("Count"));
                                            }
                                        }
                                    }

                                    ChatListModel model=new ChatListModel();
                                    model.setId(id);
                                    model.setName(name);
                                    model.setCount(count);
                                    arrayList.add(model);

                                }
                                activity.runOnUiThread(() -> {
                                    ChatListAdapter chatListAdapter=new ChatListAdapter(arrayList,activity,ChatListFragment.this);
                                    rcy_chatList.setAdapter(chatListAdapter);
                                    DashBordActivity.progressDialog.dismiss();
                                    chatListAdapter.notifyDataSetChanged();
                                });
                        }
                    }
                    catch (Exception e)
                    {
                        activity.runOnUiThread(() -> {
                            Toast.makeText(activity, "Some thing is wrong", Toast.LENGTH_SHORT).show();
                            DashBordActivity.progressDialog.dismiss();
                        });
                        e.getMessage();
                    }
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
