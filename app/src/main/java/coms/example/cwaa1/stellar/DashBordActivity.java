package coms.example.cwaa1.stellar;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.TimeZone;

import coms.example.cwaa1.stellar.LeadView.LeadViewFragment;
import coms.example.cwaa1.stellar.LeavForm.LeavFragment;
import coms.example.cwaa1.stellar.ManagerAssign.AprovedTaskActivity;
import coms.example.cwaa1.stellar.Task_List.ListOfTaskActivity;
import coms.example.cwaa1.stellar.TeamLeadView.TeamLeadActivity;
import coms.example.cwaa1.stellar.TeamLeaveView.LeaveTabFragment;
import coms.example.cwaa1.stellar.Weeklyplane.WeekPlanlyActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DashBordActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    public static String EmpId,etype;
    private String em;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static TextView tvtitle;
    private Switch attence;
    public static ProgressDialog progressDialog;
    private int date;
    private static NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_bord);

        preferences = getSharedPreferences("stelleruser", MODE_PRIVATE);
        editor = preferences.edit();
        EmpId = preferences.getString("empid", "");

        em = preferences.getString(String.valueOf(R.string.username), "");
        final String imgage = preferences.getString(String.valueOf(R.string.userimg), "");
        Log.e("imagname", imgage);

        etype = preferences.getString(String.valueOf(R.string.usertype), "");
        Log.e("userAddress", preferences.getString(String.valueOf(R.string.useraddress), ""));
        Log.e("number", preferences.getString(String.valueOf(R.string.useralter_number), ""));
        Log.e("mobile", preferences.getString(String.valueOf(R.string.usermobile), ""));
        Log.e("email", preferences.getString(String.valueOf(R.string.usrEmail), ""));

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        date = calendar.get(Calendar.DAY_OF_MONTH);
        Log.e("cu Date", "" + date);


        Log.e("dashboard ", etype);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please Wait...");
        progressDialog.setCancelable(false);

        ImageView img = findViewById(R.id.ctoolbar);
        tvtitle = findViewById(R.id.tvtitile);


        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("utype", etype);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fream, fragment);
        transaction.commit();
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);

                } else {
                    drawer.openDrawer(GravityCompat.START);
                }

            }
        });


         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headView = navigationView.getHeaderView(0);
        TextView idView = headView.findViewById(R.id.nav_name);
        LinearLayout update_linear = headView.findViewById(R.id.update_linear);
        ImageView image_show = headView.findViewById(R.id.imageView);
        attence = headView.findViewById(R.id.attendeceswitch);

        idView.setText(em);


        Log.e("sha print", getsha256("addAttendanceemp"));


         Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_app);
        MenuItem teamtask = menu.findItem(R.id.nav_teamlead);
        MenuItem teamleave = menu.findItem(R.id.nav_teamLeave);


        if (etype.equals("employee")) {
            teamtask.setVisible(false);
            menuItem.setVisible(false);
            teamleave.setVisible(false);
        }
        Picasso.get()
                .load(getString(R.string.Image_Path) + imgage)
                .placeholder(R.drawable.ic_person)
                .into(image_show);

        update_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(DashBordActivity.this, Update_ProfileActivity.class);
                in.putExtra("uname", preferences.getString(String.valueOf(R.string.username), ""));
                in.putExtra("uadd", preferences.getString(String.valueOf(R.string.useraddress), ""));
                in.putExtra("unum", preferences.getString(String.valueOf(R.string.useralter_number), ""));
                in.putExtra("umob", preferences.getString(String.valueOf(R.string.usermobile), ""));
                in.putExtra("uemail", preferences.getString(String.valueOf(R.string.usrEmail), ""));
                in.putExtra("uimg", imgage);
                in.putExtra("uid", preferences.getString("empid", ""));
                in.putExtra("utype", preferences.getString(String.valueOf(R.string.usertype), ""));
                startActivity(in);
            }
        });

        if (date == preferences.getInt(String.valueOf(R.string.attandenceStatus), 0)) {
            attence.setChecked(true);
        } else {
            attence.setChecked(false);
        }

        attence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    attenceApi();
                    // attence.setChecked(false);
                    // Toast.makeText(DashBordActivity.this, "on", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(DashBordActivity.this, "off", Toast.LENGTH_SHORT).show();

            }
        });


        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                Log.e("methos shubham", "yess");
                Fragment fragmentn = getSupportFragmentManager().findFragmentById(R.id.fream);
                String fragmentTag = "";
                if (fragmentn != null) fragmentTag = fragmentn.getTag();
                if (fragmentTag != null && fragmentTag.equalsIgnoreCase("Stellar")) {
                    //  tvtitle.setText("Stellar");
                    navigationView.getMenu().getItem(0).setChecked(true);
                } else if (fragmentTag != null && fragmentTag.equalsIgnoreCase("Weekly Plan")) {
                    //  tvtitle.setText("Weekly Plan");
                    navigationView.getMenu().getItem(1).setChecked(true);
                } else if (fragmentTag != null && fragmentTag.equalsIgnoreCase("Add Lead")) {
                    // tvtitle.setText("Add Lead");

                    navigationView.getMenu().getItem(2).setChecked(true);
                } else if (fragmentTag != null && fragmentTag.equalsIgnoreCase("Offer")) {
                    tvtitle.setText("");
                    navigationView.getMenu().getItem(3).setChecked(true);
                }
            }
        });

    }
    public static void menuchek(int i){
        navigationView.getMenu().getItem(i).setChecked(true);
    }
public static void titleName(String name){
        tvtitle.setText(name);

}
    private void attenceApi() {

        String key = getsha256("addAttendanceemp");
        Log.e("allLeaveList Key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "addAttendanceemp")
                .add("key", key)
                .add("employee_id", DashBordActivity.EmpId)

                .build();
        final Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast("Network Problem");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                String result = response.body().string();
                Log.e("attendance result", result);
                if (response.isSuccessful()) {


                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equals("true")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    editor.putInt(String.valueOf(R.string.attandenceStatus), date);
                                    editor.commit();
                                }
                            });
                            toast("attendance Done");


                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    attence.setChecked(false);
                                    toast("try Agin ");
                                    //    progressDialog.dismiss();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // progressDialog.dismiss();
                        }
                    });
                }

            }
        });


    }


    private void toast(final String s) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DashBordActivity.this, s, Toast.LENGTH_SHORT).show();
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


//            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("Do You  want to Exit");
//            builder.setTitle(" Exit").setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    finish();
//
//                }
//            }).setNegativeButton("no", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//
//                }
//            });
//            builder.show();
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//        return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Home) {
            HomeFragment fragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("utype", etype);
            fragment.setArguments(bundle);
            loadFragment(fragment, "Stellar");
        } else if (id == R.id.nav_weekplan) {
            WeekPlanlyActivity fragment = new WeekPlanlyActivity();
            // tvtitle.setText("Weekly Plan");
            loadFragment(fragment, "Weekly Plan");

        } else if (id == R.id.nav_alead) {

            AddNew_LeadActivity fragment = new AddNew_LeadActivity();
            // tvtitle.setText("Add Lead");
            loadFragment(fragment, "Add Lead");

        } else if (id == R.id.nav_teamLeave) {

            LeaveTabFragment fragment = new LeaveTabFragment();
            // tvtitle.setText("Add Lead");
            loadFragment(fragment, "Add Lead");

        } else if (id == R.id.nav_listtask) {
            ListOfTaskActivity fragment = new ListOfTaskActivity();
            //tvtitle.setText("Task List");
            loadFragment(fragment, "Task List");

        } else if (id == R.id.nav_listlead) {
            LeadViewFragment leadListActivity = new LeadViewFragment();
            // tvtitle.setText("Lead View");
            Bundle bundle = new Bundle();
            bundle.putString("type", etype);
            leadListActivity.setArguments(bundle);
            loadFragment(leadListActivity, "Lead View");

        } else if (id == R.id.nav_teamlead) {

            TeamLeadActivity leadListActivity = new TeamLeadActivity();
            //tvtitle.setText("Lead View");
            Bundle bundle = new Bundle();
            bundle.putString("type", etype);
            leadListActivity.setArguments(bundle);
            loadFragment(leadListActivity, "Team Lead View");

        } else if (id == R.id.nav_app) {

            Intent intent = new Intent(DashBordActivity.this, AprovedTaskActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_addleave) {
            LeavFragment leavFragment = new LeavFragment();
            loadFragment(leavFragment, "");

        } else if (id == R.id.nav_logout) {

            // alarmManager.cancel(pendingIntent);

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do You  want to Logout");
            builder.setTitle(" Exit").setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    editor = preferences.edit();
                    editor.remove("empid");
                    editor.remove("empEmail");
                    editor.remove(String.valueOf(R.string.username));
                    editor.remove(String.valueOf(R.string.usermobile));
                    editor.remove(String.valueOf(R.string.useralter_number));
                    editor.remove(String.valueOf(R.string.useraddress));
                    editor.remove(String.valueOf(R.string.userimg));
                    editor.remove(String.valueOf(R.string.usertype));
                    editor.remove(String.valueOf(R.string.attandenceStatus));
                    editor.putInt("log", 0);
                    editor.apply();
                    Intent intent = new Intent(DashBordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();


                }
            }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });
            builder.show();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void loadFragment(Fragment fragment, String stellar) {


        Fragment poppedFragment = getSupportFragmentManager().findFragmentByTag(stellar);
        if (poppedFragment != null) {
            getSupportFragmentManager().popBackStack(stellar, 0);
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fream, fragment, stellar);
            transaction.addToBackStack(stellar);
            transaction.commitAllowingStateLoss();
        }


//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fream, fragment)
//                .addToBackStack(null);
//        transaction.commit();
    }
}
