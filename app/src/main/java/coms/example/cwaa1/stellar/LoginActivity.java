package coms.example.cwaa1.stellar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private EditText id, pass;
    private Button okay;
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    private ProgressDialog progressDialog;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("stelleruser", MODE_PRIVATE);
        editor = preferences.edit();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);



        id = findViewById(R.id.et_id);
        pass = findViewById(R.id.et_pass);
        okay = findViewById(R.id.btn_oky);

        int l = preferences.getInt("log", 0);

        if (l == 1) {
            Intent intent = new Intent(LoginActivity.this, DashBordActivity.class);
            startActivity(intent);
            finish();

        }


        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(LoginActivity.this, WeekPlanlyActivity.class);
//                startActivity(i);
                String number = id.getText().toString().trim();
                String pasw = pass.getText().toString().trim();

                if (number.length() < 9) {
                    id.setError("Enter 10 Digit");
                } else if (pasw.equals("")) {
                    pass.setError("is Empty");
                } else {
                    api(number, pasw);
                }

            }
        });


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("LoginActivity_tokefaild", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = Objects.requireNonNull(task.getResult()).getToken();

                        // Log and toast
                      //  String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("LoginActivity_token", token);
                        //  Log.d("LoginActivity_tokenhai", msg);
                        //Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void api(String number, String pasw) {
        progressDialog.show();

        String key = getsha256("login_auth");
        Log.e("key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "login_auth")
                .add("key", key)
                .add("mobile", number)
                .add("password", pasw)
                .add("token", token)
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
progressDialog.dismiss();
                        Log.e("network", e.getMessage());
                        Toast.makeText(LoginActivity.this, "network Problem", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String result = response.body().string().trim();
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        Log.e("message   ",msg);

                        if (msg.equalsIgnoreCase("login successfully")) {




                            JSONObject object1 = object.getJSONObject("data");
                            final String empid = object1.getString("employee_id");
                            final String empMail = object1.getString("email");
                         //   final String inherit = object1.getString("inherit");
                            final String full_name = object1.getString("full_name");
                            final String mobile = object1.getString("mobile");
                            final String alter_number = object1.getString("alter_number");
                            final String address = object1.getString("address");
                            final String image_name = object1.getString("image_name");
                            final String employee_type = object1.getString("employee_type");


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    editor.putString("empid", empid);
                                    editor.putString("empEmail", empMail);
                                   // editor.putString(String.valueOf(R.string.inherit),inherit);
                                    editor.putString(String.valueOf(R.string.username),full_name);
                                    editor.putString(String.valueOf(R.string.usermobile),mobile);
                                    editor.putString(String.valueOf(R.string.useralter_number),alter_number);
                                    editor.putString(String.valueOf(R.string.useraddress),address);
                                    editor.putString(String.valueOf(R.string.userimg),image_name);
                                    editor.putString(String.valueOf(R.string.usertype),employee_type);
                                    editor.putInt("log", 1);
                                    editor.commit();
                                    Log.e("log mehtod","yettt");
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, DashBordActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            });


                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    toast("not valid user");
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.show();
                            Log.e("Sucess", result);
                        }
                    });
                }

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
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
