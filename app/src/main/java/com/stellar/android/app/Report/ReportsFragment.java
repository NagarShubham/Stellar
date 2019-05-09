package com.stellar.android.app.Report;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.stellar.android.app.DashBordActivity;
import com.stellar.android.app.R;
import com.stellar.android.app.Utill;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReportsFragment extends Fragment {
    private final String TAG = "ReportsFragment ";
    private Activity activity;
    private ProgressDialog progressDialog;
    private String empId;
    private TextView tvnoflead, tvnofconver, tvnoftask, tvleadtype;
    private TextView tvtarget, tvachive;
    private Button btnDownload, btneMail, btnSendtoMager;
    private Button btnshare;


    public ReportsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        DashBordActivity.titleName("Reports");
        findID(view);
        empId = DashBordActivity.EmpId;
         apiCall();

        btneMail.setOnClickListener(v -> sendMailApi());
        btnSendtoMager.setOnClickListener(v -> sendToManegerApi());
        btnDownload.setOnClickListener(v -> downloadApiCall());
        btnshare.setOnClickListener(v -> dialogShare());

        return view;
    }

    private void dialogShare() {

         AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dview = inflater.inflate(R.layout.dialog_sharemail, null);
        builder.setView(dview);
        AlertDialog dialog = builder.create();
        dialog.show();

        Button btn=dview.findViewById(R.id.btnSendmail);
        btn.setOnClickListener(v -> {
            String emailString=((EditText)dview.findViewById(R.id.etEmail)).getText().toString().trim();

            if (emailString.matches("^[a-zA-Z0-9_+&*-]+(?:\\."+"[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$"))
            {
                shareApi(emailString,dialog);

            }else Toast.makeText(activity, "Enter Valid Email", Toast.LENGTH_SHORT).show();

        });

        dialog.show();
    }
    private void shareApi(String emailString, AlertDialog dialog) {

        String key = Utill.getsha256("ShareReportByMail");
        Log.e(TAG, " " + key);
        progressDialog.show();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "ShareReportByMail")
                .add("key", key)
                .add("employee_id", empId)
                .add("email", emailString)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                activity.runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "network Problem", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string().trim();
                Log.e(TAG, " Result Repots " + result);
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equalsIgnoreCase("true")) {
                            activity.runOnUiThread(() -> {
                                dialog.cancel();
                                progressDialog.dismiss();
                                Toast.makeText(activity, "Mail Send ", Toast.LENGTH_SHORT).show();
                            });

                        } else activity.runOnUiThread(() -> {
                            progressDialog.dismiss();
                            Toast.makeText(activity, "Some Thing is Wrong", Toast.LENGTH_SHORT).show();
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        activity.runOnUiThread(() -> progressDialog.dismiss());
                    }

                } else activity.runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "Try Again", Toast.LENGTH_SHORT).show();
                });
            }
        });





    }

    private void downloadApiCall() {


        String key = Utill.getsha256("DownloadReport");
        Log.e(TAG + " key", key);
        progressDialog.show();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "DownloadReport")
                .add("key", key)
                .add("employee_id", empId)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                activity.runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "network Problem", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string().trim();
                Log.e(TAG, "DownloadReport Result " + result);
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equalsIgnoreCase("true")){
                            String path=object.getString("path");
                            activity.runOnUiThread(() -> {
                                progressDialog.dismiss();
                                DownloadManager downloadManager=(DownloadManager)activity.getSystemService(Context.DOWNLOAD_SERVICE);
                                Uri uri= Uri.parse(path);
                                DownloadManager.Request request=new DownloadManager.Request(uri);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                Long reLong=downloadManager.enqueue(request);
                                Toast.makeText(activity, "Downloading Start", Toast.LENGTH_SHORT).show();

                            });
                        }
                        else activity.runOnUiThread(() -> {
                            progressDialog.dismiss();
                            Toast.makeText(activity, "Download Reports fail ", Toast.LENGTH_SHORT).show();
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                        activity.runOnUiThread(() -> progressDialog.dismiss());
                    }
                } else activity.runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "try again ", Toast.LENGTH_SHORT).show();
                });

            }
        });















    }

    private void sendToManegerApi() {
        String key = Utill.getsha256("SendToManager");
        Log.e(TAG + " key", key);
        progressDialog.show();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "SendToManager")
                .add("key", key)
                .add("employee_id", empId)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                activity.runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "network Problem", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string().trim();
                Log.e(TAG, "SendManager Result " + result);
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equalsIgnoreCase("true")) activity.runOnUiThread(() -> {
                            progressDialog.dismiss();
                            Toast.makeText(activity, "Report Send successful", Toast.LENGTH_SHORT).show();

                        });
                        else activity.runOnUiThread(() -> {
                            progressDialog.dismiss();
                            Toast.makeText(activity, "Report  not Send ", Toast.LENGTH_SHORT).show();
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                        activity.runOnUiThread(() -> progressDialog.dismiss());
                    }
                } else activity.runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "try again ", Toast.LENGTH_SHORT).show();
                });

            }
        });

    }

    private void sendMailApi() {

        String key = Utill.getsha256("Send_mail");
        Log.e(TAG + " key", key);
        progressDialog.show();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "Send_mail")
                .add("key", key)
                .add("employee_id", empId)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                activity.runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "network Problem", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string().trim();
                Log.e(TAG, "SendMail Result " + result);
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equalsIgnoreCase("true")) activity.runOnUiThread(() -> {
                            progressDialog.dismiss();
                            Toast.makeText(activity, "Mail Send successful", Toast.LENGTH_SHORT).show();

                        });
                        else activity.runOnUiThread(() -> {
                            progressDialog.dismiss();
                            Toast.makeText(activity, "Mail  not Send ", Toast.LENGTH_SHORT).show();
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                        activity.runOnUiThread(() -> progressDialog.dismiss());
                    }
                } else activity.runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "try again ", Toast.LENGTH_SHORT).show();
                });

            }
        });
    }

    private void apiCall() {

        String key = Utill.getsha256("ReportList");
        Log.e(TAG, "ReportListkey " + key);
        progressDialog.show();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "ReportList")
                .add("key", key)
                .add("employee_id", empId)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                activity.runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "network Problem", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string().trim();
                Log.e(TAG, " Result Repots " + result);
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equalsIgnoreCase("true")) {
                            activity.runOnUiThread(() -> {
                                try {
                                    tvnoflead.setText("" + object.getInt("total_lead"));
                                    int w = object.getInt("won");
                                    int l = object.getInt("lost");
                                    tvnofconver.setText("" + (w + l));
                                    tvnoftask.setText("" + object.getInt("day") + "/" + "" + object.getInt("month") + "/" + "" + object.getInt("week"));
                                    String ladty = "" + object.getInt("very hot") + "/" + "" + object.getInt("hot") + "/" + "" + object.getInt("warm") + "/" + "" + object.getInt("cold");
                                    tvleadtype.setText(ladty);
                                    tvtarget.setText("" + object.getInt("target"));
                                    tvachive.setText("" + object.getInt("achieve"));
                                    progressDialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    activity.runOnUiThread(() -> progressDialog.dismiss());
                                }
                            });

                        } else activity.runOnUiThread(() -> {
                            progressDialog.dismiss();
                            Toast.makeText(activity, "Some Thing is Wrong ", Toast.LENGTH_SHORT).show();
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else activity.runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "Try Again", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void findID(View view) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        tvnoflead = view.findViewById(R.id.tvnoflead);
        tvnofconver = view.findViewById(R.id.tvnofconver);
        tvnoftask = view.findViewById(R.id.tvnoftask);
        tvleadtype = view.findViewById(R.id.tvleadtype);
        tvtarget = view.findViewById(R.id.tvtarget);
        tvachive = view.findViewById(R.id.tvachive);
        btnDownload = view.findViewById(R.id.btnDownload);
        btneMail = view.findViewById(R.id.btneMail);
        btnSendtoMager = view.findViewById(R.id.btnSendtoMager);
        btnshare = view.findViewById(R.id.btnshare);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
}