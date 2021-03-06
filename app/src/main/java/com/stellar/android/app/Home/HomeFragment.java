package com.stellar.android.app.Home;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import com.stellar.android.app.AddNew_LeadActivity;
import com.stellar.android.app.AssignLead.AssignLeadFragment;
import com.stellar.android.app.Attendance.AttendanceFragment;
import com.stellar.android.app.Chat.ChatListFragment;
import com.stellar.android.app.DashBordActivity;
import com.stellar.android.app.LeadView.LeadViewFragment;
import com.stellar.android.app.Location.LocationReceiver;
import com.stellar.android.app.ManagerAssign.AprovedTaskActivity;
import com.stellar.android.app.Map.MapsActivity;
import com.stellar.android.app.R;
import com.stellar.android.app.Report.ReportsFragment;
import com.stellar.android.app.Scanner.OcrCaptureActivity;
import com.stellar.android.app.Task_List.ListOfTaskActivity;
import com.stellar.android.app.TeamLeadView.TeamLeadActivity;
import com.stellar.android.app.TeamLeaveView.LeaveTabFragment;
import com.stellar.android.app.Weeklyplane.WeekPlanlyActivity;
import com.stellar.android.app.WonLossLead.WonLossFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "utype";
    private static final String ARG_PARAM2 = "param2";
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    private String mParam1;
    private String mParam2;
    private Activity activity;
    protected static final String TAG = "LocationOnOff";
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private LinearLayout pilayout;


    private TextView totoal, remain, achi, dayl;

    private Switch swi_sednLoc;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
    }


    private CardView task, lead, ltask, llead;
    private CardView aprov, assign_list;
    private CardView team_lead, team_leave;
    private CardView self_map;
    private CardView attendance;
    private CardView winloss;
    private CardView report;
    private FloatingActionButton chat;
    private TextView title;
    private ArrayList<String> xVals = new ArrayList<>();


    private ArcProgress arcProgress;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("utype");
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        arcProgress = view.findViewById(R.id.arc_progress);


        task = view.findViewById(R.id.wplane);
        lead = view.findViewById(R.id.alead);
        ltask = view.findViewById(R.id.listTask);
        llead = view.findViewById(R.id.list_load);
        pilayout = view.findViewById(R.id.pi);
        aprov = view.findViewById(R.id.aprov);
        self_map = view.findViewById(R.id.self_map);
        //team_map = view.findViewById(R.id.tem_map);
        assign_list = view.findViewById(R.id.assign_list);
        attendance = view.findViewById(R.id.attendence);
        team_lead = view.findViewById(R.id.tem_lead);
        team_leave = view.findViewById(R.id.tem_leave);
        chat = view.findViewById(R.id.btn_chatList);
        swi_sednLoc = view.findViewById(R.id.swi_sednLoc);
        winloss = view.findViewById(R.id.btn_winloss);
        report = view.findViewById(R.id.card_report);

        DashBordActivity.menuchek(0);
        DashBordActivity.titleName("Stellar");






        pilayout.setVisibility(View.GONE);
        pichart(view);



        ImageView scan = activity.findViewById(R.id.scan);
        scan.setVisibility(View.GONE);

        if (getArguments() != null) {
            mParam1 = getArguments().getString("utype");
        }
        System.out.print("typessss  " + mParam1);
        if (Objects.equals(mParam1, "employee")) {
            aprov.setVisibility(View.GONE);
            assign_list.setVisibility(View.GONE);
            team_leave.setVisibility(View.GONE);
            team_lead.setVisibility(View.GONE);
        }


        task.setOnClickListener(v -> {
            WeekPlanlyActivity frag = new WeekPlanlyActivity();
            loadFragment(frag);

        });
        lead.setOnClickListener(v -> {
            AddNew_LeadActivity leadActivity = new AddNew_LeadActivity();
            loadFragment(leadActivity);
        });
        ltask.setOnClickListener(v -> {

            ListOfTaskActivity list = new ListOfTaskActivity();
            loadFragment(list);
        });
        llead.setOnClickListener(v -> {

            LeadViewFragment leadListActivity = new LeadViewFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", mParam1);
            leadListActivity.setArguments(bundle);
            loadFragment(leadListActivity);


        });
        aprov.setOnClickListener(v -> {

            Intent intent = new Intent(getContext(), AprovedTaskActivity.class);
            startActivity(intent);
        });
        assign_list.setOnClickListener(v -> {
            AssignLeadFragment assignLeadFragment = new AssignLeadFragment();
            loadFragment(assignLeadFragment);
        });

        team_lead.setOnClickListener(v -> {

            TeamLeadActivity assignLeadFragment = new TeamLeadActivity();
            loadFragment(assignLeadFragment);
        });
        team_leave.setOnClickListener(v -> {

            LeaveTabFragment assignLeadFragment = new LeaveTabFragment();
            loadFragment(assignLeadFragment);
        });
        attendance.setOnClickListener(v -> {

            AttendanceFragment attendanceFragment = new AttendanceFragment();
            loadFragment(attendanceFragment);
        });


        swi_sednLoc.setOnCheckedChangeListener((buttonView, isChecked) -> {


            LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            if (locationManager != null) {
                Location loc = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (loc != null) {

                    double lat = loc.getLatitude();
                    double log = loc.getLongitude();

                    Log.e("latStatic", "here: " + loc.getLatitude());
                    Log.e("longStatic", "here: " + loc.getLongitude());

                    sendLocaion(lat, log);
                } else {
                    Toast.makeText(activity, "Location Not Found", Toast.LENGTH_SHORT).show();
                    Log.e("in eled", "here");
                }
            }


        });


        chat.setOnClickListener(v -> {
            ChatListFragment chatListFragment = new ChatListFragment();
            loadFragment(chatListFragment);
        });

        self_map.setOnClickListener(v -> {
            Intent intent = new Intent(activity, MapsActivity.class);
            startActivity(intent);
        });


        winloss.setOnClickListener(v -> loadFragment(new WonLossFragment()));

        report.setOnClickListener(v -> loadFragment(new ReportsFragment()));

        //TODO:GPS need to On by below here


        if (ActivityCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            checkLocationEnable();

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, 1);
            }
        }


        return view;
    }


    private void sendLocaion(double lat, double log) {


        String key = getsha256("task_geolocation");
        Log.e("btnSend location key", key);


        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "task_geolocation")
                .add("key", key)
                .add("employee_id", DashBordActivity.EmpId)
                .add("longitude", key)
                .add("latitude", key)

                .build();
        final Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(() -> Toast.makeText(activity, "Network Problem", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string().trim();
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        Log.e("SendLocation ", "msg " + msg);
                        if (msg.equalsIgnoreCase("true")) {
                            activity.runOnUiThread(() -> {
                                swi_sednLoc.setChecked(true);
                                Toast.makeText(activity, "Location Send", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            swi_sednLoc.setChecked(false);
                            activity.runOnUiThread(() -> Toast.makeText(activity, "Location not Send", Toast.LENGTH_SHORT).show());
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    activity.runOnUiThread(() -> Toast.makeText(activity, "somthing is wrong", Toast.LENGTH_SHORT).show());
                }


            }
        });


    }


    private void checkLocationEnable() {

        activity.setFinishOnTouchOutside(true);
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (!hasGPSDevice(activity)) {
            Toast.makeText(activity, "Gps not Supported", Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(activity)) {
            Toast.makeText(activity, "Gps not enabled", Toast.LENGTH_SHORT).show();
            enableLoc();
        } else {
            // Toast.makeText(DashBordActivity.this,"Gps already enabled",Toast.LENGTH_SHORT).show();
            locationStart();
        }
    }


    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(activity)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(connectionResult -> Log.d("Location error", "Location error " + connectionResult.getErrorCode())).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

            result.setResultCallback(result1 -> {
                final Status status = result1.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(activity, REQUEST_LOCATION);


                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                }
            });
        }
    }

    private void locationStart() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(activity, LocationReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 600000L, pendingIntent);

    }


    private void pichart(final View view) {


        totoal = view.findViewById(R.id.ttotal);
        remain = view.findViewById(R.id.tremaing);
        achi = view.findViewById(R.id.tachiv);
        dayl = view.findViewById(R.id.tday);
        SharedPreferences preferences = activity.getSharedPreferences("stelleruser", MODE_PRIVATE);

        String id = preferences.getString("empid", "");
        String key = getsha256("getAlltargetList");

        Log.e(" Target Key ", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "getAlltargetList")
                .add("key", key)
                .add("employee_id", id)
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
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                String result = response.body().string();
                Log.e("Target result ", result);

                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);


                        String msg = object.getString("message").trim();
                        Log.e("Target message ", msg);
                        if (msg.equalsIgnoreCase("true")) {

                            final String last_date;
                            final String tarVAlue;
                            final String achiv;
                            final String remaningvalue;
                            final double rePer;
                            final double achiPer;

                            final JSONArray array = object.getJSONArray("data");

                            JSONObject objectname = array.getJSONObject(0);


                            tarVAlue = objectname.getString("total_target");
                            achiv = objectname.getString("acheived_value");
                            remaningvalue = objectname.getString("reamin_target_in_amount");

                            rePer = objectname.getDouble("remain_target_in_percent");
                            achiPer = objectname.getDouble("complete_target_in_percent");

                            last_date = object.getString("day_left");


                            activity.runOnUiThread(() -> {
                                float r = (float) rePer;
                                float a = (float) achiPer;

                                arcProgress.setProgress((int)a);

                                if (!tarVAlue.equals("")) {
                                    pilayout.setVisibility(View.VISIBLE);
                                }
                                totoal.setText(tarVAlue);
                                achi.setText(achiv);
                                remain.setText(remaningvalue);
                                dayl.setText(last_date + " Day Left");




                            });
                        }


                    } catch (final Exception e) {
                        e.getMessage();
                        activity.runOnUiThread(() -> {
                            Log.e("ecx", e.getMessage());
                        });
                    }
                }
            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    private void loadFragment(android.support.v4.app.Fragment fragment) {

        FragmentTransaction transaction = null;
        if (getFragmentManager() != null) {
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fream, fragment)
                    .addToBackStack(null);
            transaction.commit();
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                checkLocationEnable();
            } else {
                activity.finish();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        Log.e("DAsh onActivity ", "hhhh " + data.getStringExtra(OcrCaptureActivity.TextBlockObject));
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Toast.makeText(activity, "Gps enabled", Toast.LENGTH_SHORT).show();
                        locationStart();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(activity, "Gps enabled cancelled", Toast.LENGTH_SHORT).show();
                        activity.finish();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

}
