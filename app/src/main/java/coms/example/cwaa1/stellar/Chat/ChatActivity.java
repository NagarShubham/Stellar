package coms.example.cwaa1.stellar.Chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import android.text.format.Time;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import coms.example.cwaa1.stellar.DashBordActivity;
import coms.example.cwaa1.stellar.R;
import coms.example.cwaa1.stellar.SharedPreference;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    private BroadcastReceiver receiver;
    private int mPosition;
    private RecyclerView recyclerView;
    private ArrayList<ChatModel> list;
    private ChatAdapter adapterRecycler;
    private EditText input_message_text;
    private FloatingActionButton message_send;
    private String to_user, user, connection_id, stringmsg;
    private Time today;
    private String token;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;
    private String user_name;
    private TextView chat_title;
    private SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chat_title=findViewById(R.id.chat_title);
        sharedPreference = new SharedPreference(ChatActivity.this);
        today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        Dicler();
        messageList();
        adapterRecycler.notifyDataSetChanged();



        JSONArray jsonArray = null;
        String value = sharedPreference.getJsonArray();
        if (value != null) {
            try {
                jsonArray = new JSONArray(value);
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);

                    if (jsonObject.getString("Id").equals(to_user)){
                        int count1 = jsonObject.getInt("Count");
                        count1 = 0;
                        jsonObject.put("Count", count1);
                        jsonArray.remove(j);
                        jsonArray.put(jsonObject);
                        break;
                    }
                }

            } catch(JSONException e1){
                e1.printStackTrace();
            }
            sharedPreference.setJsonArray(jsonArray.toString());
        }












        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {

                    if (adapterRecycler != null) {

                        if (adapterRecycler.getItemCount() > 0) {

                            final int lastAdapterItem = adapterRecycler.getItemCount() - 1;
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    int recyclerViewPositionOffset = -1000000;
                                    View bottomView = linearLayoutManager.findViewByPosition(lastAdapterItem);
                                    if (bottomView != null) {
                                        recyclerViewPositionOffset = 0 - bottomView.getHeight();
                                    }
                                    linearLayoutManager.scrollToPositionWithOffset(lastAdapterItem, recyclerViewPositionOffset);
                                    adapterRecycler.notifyDataSetChanged();
                                }
                            });
                        } else {
                            //  Toast.makeText(ChatActivity.this, "No Chat yet", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


        message_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stringmsg = input_message_text.getText().toString().trim();
                if (!stringmsg.equals("")) {

                    Calendar c = Calendar.getInstance();
                    String time1 = new SimpleDateFormat("hh:mm:ss aa").format(c.getTime());

                    String tii = "" + today.monthDay + "/" + today.month + "/" + today.year + " " + time1;

                    list.add(new ChatModel(stringmsg, "1", tii, true));

                    adapterRecycler.notifyDataSetChanged();
                    input_message_text.setText("");
                    recyclerView.scrollToPosition(adapterRecycler.getItemCount());

                    Log.e("list", "count " + adapterRecycler.getItemCount());
                    final int lastAdapterItem = adapterRecycler.getItemCount() - 1;
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            int recyclerViewPositionOffset = -1000000;
                            View bottomView = linearLayoutManager.findViewByPosition(lastAdapterItem);
                            if (bottomView != null) {
                                recyclerViewPositionOffset = 0 - bottomView.getHeight();
                            }
                            linearLayoutManager.scrollToPositionWithOffset(lastAdapterItem, recyclerViewPositionOffset);
                            adapterRecycler.notifyDataSetChanged();

                        }
                    });

                    msgsend(list.size() - 1);


                }

            }
        });


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    String value = intent.getStringExtra("Message");
                    String id = intent.getStringExtra("senderid");
                    String ti = intent.getStringExtra("tem");
                  //  Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
                    if (user.equals(id)) {
                        list.add(new ChatModel(value, "1", ti, true));
                        adapterRecycler.notifyDataSetChanged();
                    } else {
                        if (to_user.equals(id)) {
                            list.add(new ChatModel(value, "0", ti, true));
                            adapterRecycler.notifyDataSetChanged();
                        }
                    }

                    recyclerView.scrollToPosition(adapterRecycler.getItemCount());

                    Log.e("list", "count " + adapterRecycler.getItemCount());
                    final int lastAdapterItem = adapterRecycler.getItemCount() - 1;
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            int recyclerViewPositionOffset = -1000000;
                            View bottomView = linearLayoutManager.findViewByPosition(lastAdapterItem);
                            if (bottomView != null) {
                                recyclerViewPositionOffset = 0 - bottomView.getHeight();
                            }
                            linearLayoutManager.scrollToPositionWithOffset(lastAdapterItem, recyclerViewPositionOffset);
                            adapterRecycler.notifyDataSetChanged();

                        }
                    });


                    Log.e("Message", value);
                } catch (Exception e) {
                    Log.e("In catch",e.getMessage());
                    e.printStackTrace();
                }
            }
        };
    }


    private void Dicler() {

        recyclerView = findViewById(R.id.re_view);
        input_message_text = findViewById(R.id.input_message_text);
        message_send = findViewById(R.id.button_message_send);

        list = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterRecycler = new ChatAdapter(list);
        recyclerView.setAdapter(adapterRecycler);

        makeConnection();

    }

    private void makeConnection() {

        Intent intent = getIntent();
        user = DashBordActivity.EmpId;
        to_user = intent.getStringExtra("id");
        user_name = intent.getStringExtra("Name");
        chat_title.setText(user_name);

        int com = user.compareTo(to_user);

        if (com < 0)
            connection_id = user + "_" + to_user;
        else
            connection_id = to_user + "_" + user;

        Log.e("Connection Id", connection_id + " : " + user_name + " : " + user);


    }

    private void messageList() {
        String key = DashBordActivity.getsha256("showChat");
        Log.e("Chat key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "showChat")
                .add("key", key)
                .add("connection_id", connection_id)
//                .add("sender_id", user)
//                .add("receiver_id", to_user)
                .build();

        final Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        Log.e("Messageahim", connection_id + " " + user + " " + to_user);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChatActivity.this, "Network Problem", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String message = response.body().string();

                Log.e("List Result1", message);
                if (response.isSuccessful() && response.body() != null) {


                    try {
                        JSONObject data = new JSONObject(message);

                        //Log.v("login", data.toString());

                        JSONArray jsonArray = data.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            final String msg = jsonObject.getString("message");
                            final String time = jsonObject.getString("time");
                            if (user.equals(jsonObject.getString("senderId"))) {
                                list.add(new ChatModel(msg, "1", time, true));
                            } else {
                                list.add(new ChatModel(msg, "0", time, true));

                            }
                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //  Toast.makeText(MsgActivity.this, "" + result, Toast.LENGTH_SHORT).show();

                                adapterRecycler = new ChatAdapter(list);
                                recyclerView.setAdapter(adapterRecycler);
                                progressBar.setVisibility(View.GONE);

                                recyclerView.scrollToPosition(adapterRecycler.getItemCount());


                                recyclerView.scrollToPosition(adapterRecycler.getItemCount());
                                input_message_text.setText("");

                                Log.e("list", "count " + adapterRecycler.getItemCount());
                                final int lastAdapterItem = adapterRecycler.getItemCount() - 1;
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        int recyclerViewPositionOffset = -1000000;
                                        View bottomView = linearLayoutManager.findViewByPosition(lastAdapterItem);
                                        if (bottomView != null) {
                                            recyclerViewPositionOffset = 0 - bottomView.getHeight();
                                        }
                                        linearLayoutManager.scrollToPositionWithOffset(lastAdapterItem, recyclerViewPositionOffset);
                                        adapterRecycler.notifyDataSetChanged();

                                    }
                                });

                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ChatActivity.this, "Network Issue in getting data", Toast.LENGTH_LONG).show();
                            //Log.v("login", "onFailure");
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }

            }
        });

    }


    public void msgsend(final int position) {

        String key = DashBordActivity.getsha256("chatdatainsert");
        Log.e("chatdatainsert key", key);

        OkHttpClient client = new OkHttpClient();


        RequestBody body = new FormBody.Builder()
                .add("action", "chatdatainsert")
                .add("key", key)
                .add("sender_id", user)
                .add("connection_id", connection_id)
                .add("message", list.get(position).getMsg())
                .add("reciverid", to_user)
                .add("time", list.get(position).getDate())
                .add("sender_name", user_name)
                .build();
        final Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ChatModel chatModel = list.get(position);
                        chatModel.setSend(false);
                        list.set(position, chatModel);
                        adapterRecycler.notifyDataSetChanged();
                        goadapter();

                        Toast.makeText(ChatActivity.this, "Network Problem", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                Log.e("send Api result", response.body().string());
                if (response.isSuccessful()) {
                    // final String result = response.body().string().trim();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ChatModel chatModel = list.get(position);
                            chatModel.setSend(true);
                            list.set(position, chatModel);
                            adapterRecycler.notifyDataSetChanged();
                            Log.e("Confirmatiuonmfg", "Successsful");
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ChatActivity.this, "Not sent message", Toast.LENGTH_SHORT).show();

                            ChatModel chatModel = list.get(position);
                            chatModel.setSend(false);
                            list.set(position, chatModel);
                            adapterRecycler.notifyDataSetChanged();
                            goadapter();

                        }
                    });
                }
            }
        });

    }

    private void goadapter() {
        if (adapterRecycler != null) {
            adapterRecycler.setClickListener(new ChatAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    msgsend(position);
                }
            });
        }

    }



    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter("hello")
        );
    }


    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_OK);
        finish();
        return super.onSupportNavigateUp();
    }



}