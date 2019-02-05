package coms.example.cwaa1.stellar.TargetList;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import coms.example.cwaa1.stellar.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Target_ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SharedPreferences preferences;
    private static String EMP_ID;
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    private List<TargetModel>list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target__list);
        preferences = getSharedPreferences("stelleruser", MODE_PRIVATE);

        Log.e("next user id", preferences.getString("empid", ""));
        EMP_ID = preferences.getString("empid", "");
        list=new ArrayList<>();

        recyclerView=findViewById(R.id.tr_recy);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        api();

    }

    private void api() {

        String key = getsha256("getAlltargetList");
        Log.e("key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "getAlltargetList")
                .add("key", key)
                .add("employee_id", EMP_ID)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.e("network", e.getMessage());
                        Toast.makeText(Target_ListActivity.this, "network Problem", Toast.LENGTH_SHORT).show();
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
                            JSONArray array=object.getJSONArray("data");

                            list.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject objectname = array.getJSONObject(i);

                                String fromdate = objectname.getString("from_date");
                                String todate = objectname.getString("to_date");
                                String assign = objectname.getString("assigned_by");
                                String achive = objectname.getString("acheived_value");
                                String target = objectname.getString("target_value");

                                TargetModel model=new TargetModel();

                                model.setFrom_date(fromdate);
                                model.setTo_date(todate);
                                model.setAssigned_by(assign);
                                model.setAcheived_value(achive);
                                model.setTarget_value(target);

                                list.add(model);

                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    TargetAdapter adapter=new TargetAdapter(list);
                                    recyclerView.setAdapter(adapter);



                                }
                            });



                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                Log.e("Sucess", result);
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

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Target_ListActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
