package coms.example.cwaa1.stellar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Update_ProfileActivity extends AppCompatActivity {
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    private int PICK_IMAGE_REQUEST = 1;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ArrayList<String> stateLists;
    private ArrayList<String> stateListsId;
    private ArrayList<String> cityLists;
    private ArrayList<String> cityListsid;

    private ImageView pro_image;
    private Button btn_save;

    private EditText name, pro_dob, pro_aniversary;
    private EditText email, pro_designation, pro_pin;
    private EditText mobile, pro_pass;
    private EditText no;
    private EditText add;
    private Spinner pro_gender, pro_state, pro_city;
    private TextView pro_datejoning, pro_department;

    private String et_pro_image;
    private String uid, utype;
    private String et_name, et_mail, et_mobile, et_no, et_add;
    private String et_dob, et_pin, et_gender, et_aniversary, et_datejoning;
    private String et_department, et_designation, et_pass;
    private String et_state, et_city, et_con;


    String getimage1 = "select image";
    String image1_name = "layout1_image1.png";

    String state, city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__profile);
        preferences = getSharedPreferences("stelleruser", MODE_PRIVATE);
        editor = preferences.edit();

        stateLists = new ArrayList();
        stateListsId = new ArrayList();
        cityLists = new ArrayList();
        cityListsid = new ArrayList();
        findid();


        //stateLists.contains("Madhya Pradesh");


        pro_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_name = name.getText().toString().trim();
                et_mail = email.getText().toString().trim();
                et_mobile = mobile.getText().toString().trim();
                et_no = no.getText().toString().trim();
                et_add = add.getText().toString().trim();
                et_dob = pro_dob.getText().toString().trim();
                et_pin = pro_pin.getText().toString().trim();
                et_gender = pro_gender.getSelectedItem().toString().trim();
                // et_state = pro_state.getSelectedItem().toString().trim();
                // et_city = pro_city.getSelectedItem().toString().trim();


                et_aniversary = pro_aniversary.getText().toString().trim();
                et_datejoning = pro_datejoning.getText().toString().trim();
                et_department = pro_department.getText().toString().trim();
                et_designation = pro_designation.getText().toString().trim();
                et_pass = pro_pass.getText().toString().trim();

                Log.e("aniversery ", et_aniversary);
                Log.e("state idddddd ", et_state);
                Log.e("city idddddd", et_city);


                if (et_name.equals("")) {
                    name.setError("is empty");
                } else if (et_mail.equals("")) {
                    email.setError("is Empty");
                } else if (et_mobile.length() < 9) {
                    mobile.setError("10 Digit Need");
                } else if (et_no.length() < 9) {
                    no.setError("10 Digit Need");
                } else if (et_add.equals("")) {
                    add.setError("is Empty");
                } else if (et_dob.equals("")) {
                    pro_dob.setError("is Empty");
                } else if (et_pin.equals("")) {
                    pro_pin.setError("is Empty");
                }
//                } else if (et_aniversary.equals("")) {
//                    pro_aniversary.setError("is Empty");
//                }

//                else if (et_datejoning.equals("")) {
//                    pro_datejoning.setError("is Empty");
//                }

                else if (et_department.equals("")) {
                    pro_department.setError("is Empty");
                } else if (et_designation.equals("")) {
                    pro_designation.setError("is Empty");
                } else if (et_pass.equals("")) {
                    pro_pass.setError("is Empty");
                } else {

                    api();
                }


            }
        });


    }

    private void api() {
        String key = getsha256("profile_update");
        Log.e("key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("action", "profile_update")
                .add("key", key)
                .add("employee_id", uid)
                .add("full_name", et_name)
                .add("email", et_mail)
                .add("mobile", et_mobile)
                .add("alter_number", et_no)
                .add("dob", et_dob)
                .add("address", et_add)
                .add("pincode", et_pin)
                .add("gender", et_gender)
                .add("country", "101")
                .add("state", et_state)
                .add("city", et_city)
                .add("aniversarydate", et_aniversary)
                .add("any_reference", "")
                .add("date_of_joining", et_datejoning)
                .add("department", et_department)
                .add("designation", et_designation)
                .add("employee_type", utype)
                .add("image_name", image1_name)
                .add("image_data", getimage1)
                .build();

        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Update_ProfileActivity.this, "Network Problem", Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equals("true")) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Update_ProfileActivity.this, "update Sucess", Toast.LENGTH_SHORT).show();

                                    editor.putString("empEmail", et_mail);
                                    // editor.putString(String.valueOf(R.string.inherit),inherit);
                                    editor.putString(String.valueOf(R.string.username), et_name);
                                    editor.putString(String.valueOf(R.string.usermobile), et_mobile);
                                    editor.putString(String.valueOf(R.string.useralter_number), et_no);
                                    editor.putString(String.valueOf(R.string.useraddress), et_add);
                                    editor.putString(String.valueOf(R.string.userimg), et_pro_image);

                                    editor.putInt("log", 1);
                                    editor.commit();

                                    finish();

                                }
                            });

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(Update_ProfileActivity.this, "update Fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
                Log.e("ulod responce", result);
            }
        });


    }

    private void findid() {

        pro_image = findViewById(R.id.pro_image);
        name = findViewById(R.id.pro_name);
        mobile = findViewById(R.id.pro_mobile);
        no = findViewById(R.id.pro_alter);
        add = findViewById(R.id.pro_add);
        pro_dob = findViewById(R.id.pro_dob);
        pro_pin = findViewById(R.id.pro_pin);
        pro_aniversary = findViewById(R.id.pro_aniversary);
        email = findViewById(R.id.pro_email);
        pro_designation = findViewById(R.id.pro_designation);
        pro_pass = findViewById(R.id.pro_pass);
        pro_gender = findViewById(R.id.pro_gender);
        pro_datejoning = findViewById(R.id.pro_datejoning);
        pro_department = findViewById(R.id.pro_department);
        pro_state = findViewById(R.id.pro_state);
        pro_city = findViewById(R.id.pro_city);
        btn_save = findViewById(R.id.pro_btn_save);


        Intent intent = getIntent();
        et_name = intent.getStringExtra("uname");
        et_add = intent.getStringExtra("uadd");
        et_no = intent.getStringExtra("unum");
        et_mobile = intent.getStringExtra("umob");
        et_pro_image = intent.getStringExtra("uimg");
        uid = intent.getStringExtra("uid");
        utype = intent.getStringExtra("utype");

        //  getdetail();

        getdetail();


        Log.e("iddddd", uid);


//        name.setText(et_name);
//        mobile.setText(et_mobile);
//        no.setText(et_no);
//        add.setText(et_add);


        pro_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_state = stateListsId.get(position);
                getcity(et_state);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pro_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                et_city = cityListsid.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void getdetail() {

        String key = getsha256("login_profile_data");
        Log.e("update_profile_data key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "login_profile_data")
                .add("key", key)
                .add("employee_id", uid)
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
                        Toast.makeText(Update_ProfileActivity.this, "network Problem", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string().trim();
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        Log.e(" geetttt message", msg);

                        if (msg.equalsIgnoreCase("true")) {


                            Log.e("in suihdfonsdfnsdlfns", "");

//                            JSONObject object1 = object.getJSONObject("data");
                            JSONArray array = object.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {

                                final JSONObject object1 = array.getJSONObject(i);


                                final String pin = object1.getString("pincode");
                                final String e = object1.getString("email");
                                final String img = object1.getString("image_name");
                                final String nm = object1.getString("full_name");
                                final String mob = object1.getString("mobile");
                                final String alt = object1.getString("alter_number");
                                final String addss = object1.getString("address");
                                final String dob = object1.getString("dob");
                                final String anu = object1.getString("aniversary_date");
                                final String des = object1.getString("designation");
                                final String dep = object1.getString("department");
                                final String daj = object1.getString("date_of_joining");
                                final String password = object1.getString("password");
                                final String gender = object1.getString("gender");

                                et_con = object1.getString("country_name");
                                state = object1.getString("state_name");
                                city = object1.getString("city_name");


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        pro_pin.setText(pin);
                                        email.setText(e);
                                        // ring inherit = object1.getString("inherit");
                                        name.setText(nm);
                                        mobile.setText(mob);
                                        no.setText(alt);
                                        add.setText(addss);
                                        // pro_pin.setText( object1.getString("image_name");
                                        // pro_pin.setText(object1.getString("employee_type");
                                        //pro_gender.setText( object1.getString("gender");
                                        pro_aniversary.setText(anu);
                                        pro_designation.setText(des);
                                        pro_department.setText(dep);
                                        pro_datejoning.setText(daj);
                                        pro_dob.setText(dob);
                                        pro_pass.setText(password);

                                        String[] ig = getResources().getStringArray(R.array.Gander);
                                        for (int i = 0; i < ig.length; i++) {
                                            if (ig[i].equals(gender)) {
                                                pro_gender.setSelection(i);
                                            }
                                        }

//                                        et_con = object1.getString("country_name");
//                                        et_state = object1.getString("state_name");
//                                        et_city = object1.getString("city_name");
                                        Picasso.get()
                                                .load(getString(R.string.Image_Path) + img)
                                                .placeholder(R.drawable.ic_person)
                                                .into(pro_image);
//
                                        et_pro_image = img;
                                        getState();


//                                        for (int i=0;i<cityLists.size();i++)
//                                        {
//                                            if (cityLists.contains(city))
//                                            {
//                                                pro_city.setSelection(i);
//                                            }
//                                        }

                                    }
                                });


                            }
                        } else {

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                Log.e(" getdetails  Sucess", result);

            }
        });


    }

    private void getcity(String et_state) {

        String key = getsha256("getCity");
        Log.e("key", key);
        Log.e("state yaaaa", et_state);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "getCity")
                .add("key", key)
                .add("state_id", et_state)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Update_ProfileActivity.this, "Network Problem", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equals("true")) {

                            JSONArray array = object.getJSONArray("data");

                            cityLists.clear();
                            cityListsid.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object2 = array.getJSONObject(i);


                                String sid = object2.getString("id");
                                String snma = object2.getString("name");

//                                Log.e("city id  ", object2.getString("id"));
//                                Log.e("city name  ", object2.getString("name"));

                                cityLists.add(snma);
                                cityListsid.add(sid);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        ArrayAdapter cityadapter = new ArrayAdapter(Update_ProfileActivity.this, R.layout.custom_spiner, cityLists);
                                        pro_city.setAdapter(cityadapter);

                                        int index = cityLists.indexOf(city);
                                        pro_city.setSelection(index);

                                    }
                                });

                            }
                        } else {
                            // toast("not valid user");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                Log.e("Sucess city", result);


            }
        });


    }

    private void getState() {


        String key = getsha256("getState");
        Log.e("key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "getState")
                .add("key", key)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Update_ProfileActivity.this, "Network Problem", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        String msg = object.getString("message");
                        if (msg.equals("true")) {

                            JSONArray array = object.getJSONArray("data");

                            stateLists.clear();
                            stateListsId.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object2 = array.getJSONObject(i);


                                String sid = object2.getString("id");
                                String snma = object2.getString("name");

//                                Log.e("state id  ", object2.getString("id"));
//                                Log.e("state name  ", object2.getString("name"));

                                stateLists.add(snma);
                                stateListsId.add(sid);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


                                        // stateLists.add(0,et_state);
                                        ArrayAdapter stateadapter = new ArrayAdapter(Update_ProfileActivity.this, R.layout.custom_spiner, stateLists);
                                        pro_state.setAdapter(stateadapter);

                                        int index = stateLists.indexOf(state);
                                        pro_state.setSelection(index);


                                    }
                                });

                            }
                        } else {
                            // toast("not valid user");
                        }

                    } catch (JSONException e) {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                Calendar calendar = Calendar.getInstance();
                java.util.Date mdformat = calendar.getTime();
                Date date1 = new Date(mdformat.getTime());

                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                pro_image.setImageBitmap(bitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] b = baos.toByteArray();

                getimage1 = Base64.encodeToString(b, Base64.DEFAULT);
                image1_name = date1.getTime() + ".png";

                Log.e("imagedata", getimage1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
