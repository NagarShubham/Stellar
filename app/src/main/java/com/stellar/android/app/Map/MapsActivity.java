package com.stellar.android.app.Map;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private LatLng sydney, sydney2;
    private GoogleMap mMap;
    private List<Polyline> polylines;
    private RelativeLayout filter;
    private static final int[] COLORS = new int[]{R.color.colorPrimary, R.color.colorPrimaryDark, R.color.textcolor, R.color.tabcolor, R.color.primary_dark_material_light};
    private TextView totime, fromtime, date;
    private Spinner sp_name;
    private Button btn_save;
    private List<String> childNameList = new ArrayList<>();
    private List<String> childIdList = new ArrayList<>();
    private String userid;
    private ArrayAdapter adapter;
    private String chidId;
    private ArrayList<LatLng> points;
    private PolylineOptions polylineOptions;
    private ProgressDialog progressDialog;

    private LatLng delhi= new LatLng(28.6139,77.2090);
    private LatLng chandigarh = new LatLng(30.7333,76.7794);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        filter = findViewById(R.id.map_filter);
        polylines = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.setCancelable(false);
        userid = DashBordActivity.EmpId;
        polylineOptions = new PolylineOptions();

        Intent intent = getIntent();

        if (!DashBordActivity.etype.equals("employee")) {

            if (childIdList.size() > 0) {
            } else {
                childNameApi();
            }
        }


        filter.setOnClickListener(v -> {

            // Toast.makeText(MapsActivity.this, "filter", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
            final LayoutInflater inflater = getLayoutInflater();
            final View view = inflater.inflate(R.layout.map_filter_items, null);
            builder.setView(view);
            final AlertDialog dialog = builder.create();

            sp_name = view.findViewById(R.id.filter_sp_name);
            totime = view.findViewById(R.id.filter_tv_totime);
            fromtime = view.findViewById(R.id.filter_tv_fromotime);
            date = view.findViewById(R.id.filter_date);
            btn_save = view.findViewById(R.id.btn_filter_save);
            if (DashBordActivity.etype.equals("employee")) {
                chidId = userid;
                sp_name.setVisibility(View.GONE);
            }

            date.setOnClickListener(v1 -> {
                int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MapsActivity.this,
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            String s = "";
                            String m = "";

                            if ((monthOfYear + 1) < 10) {
                                s = "0" + (monthOfYear + 1);
                            } else {
                                s = String.valueOf(monthOfYear + 1);
                            }

                            if ((dayOfMonth) < 10) {
                                m = "0" + (dayOfMonth);
                            } else {
                                m = String.valueOf(dayOfMonth);
                            }


                            date.setText("" + year + "-" + s + "-" + m + "");
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 3000);  //this for past date not clickbale
                datePickerDialog.show();
            });

            totime.setOnClickListener(v12 -> {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MapsActivity.this, (timePicker, selectedHour, selectedMinute) -> totime.setText(selectedHour + ":" + selectedMinute), hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            });

            fromtime.setOnClickListener(v13 -> {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MapsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        fromtime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            });

            sp_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    //Toast.makeText(MapsActivity.this, "name " + childNameList.get(position) + " id" + childIdList.get(position), Toast.LENGTH_SHORT).show();
                    Log.e("chid name", childNameList.get(position));
                    Log.e("chid name Id", childIdList.get(position));
                    chidId = childIdList.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            adapter = new ArrayAdapter(MapsActivity.this, android.R.layout.simple_spinner_item, childNameList);
            sp_name.setAdapter(adapter);
            progressDialog.dismiss();


            btn_save.setOnClickListener(v14 -> {
                String dat = date.getText().toString().trim();
                if (dat.equals("")) {
                    date.setError("Is Empty");
                } else {
                    drowlineapi(dat);
                    dialog.cancel();
                }


            });

            view.findViewById(R.id.btn_filter_cancle).setOnClickListener(v15 -> dialog.cancel());
            dialog.show();
        });

    }

    private void drowlineapi(String dat) {

        final PolylineOptions options = new PolylineOptions();
        progressDialog.show();
        points = new ArrayList<>();
        String key = DashBordActivity.getsha256("getAllLanAndlongByDate");
        Log.e("getAllLanAndlongByDate key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "getAllLanAndlongByDate")
                .add("key", key)
                .add("employee_id", chidId)
                .add("date", dat)
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
                        Toast.makeText(MapsActivity.this, "network Problem", Toast.LENGTH_SHORT).show();
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
                        Log.e("us message   ", chidId + " :: " + msg);

                        if (msg.equals("true")) {
//mMap.clear();


                            JSONArray array = object.getJSONArray("data");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject objectname = array.getJSONObject(i);

                                String latitude = objectname.getString("latitude");

                                String longitude = objectname.getString("longitude");

                                LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));

                                options.add(latLng).width(5f).color(Color.RED).geodesic(true);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    //mMap.addPolyline(options);
                                    mMap.addPolyline(options);
                                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
                                    progressDialog.dismiss();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(MapsActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            });
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.getMessage();
                    }
                }
                Log.e("poliline Result ", result);
            }
        });
    }

    private void childNameApi() {

        progressDialog.show();
        String key = DashBordActivity.getsha256("manager_child_name");
        Log.e("manager_child_name key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "manager_child_name")
                .add("key", key)
                .add("employee_id", userid)
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
                        Toast.makeText(MapsActivity.this, "network Problem", Toast.LENGTH_SHORT).show();
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
                            JSONArray array = object.getJSONArray("data");

                            childIdList.clear();
                            childNameList.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject objectname = array.getJSONObject(i);

                                String full_name = objectname.getString("full_name");

                                String employee_id = objectname.getString("employee_id");

                                childNameList.add(full_name);
                                childIdList.add(employee_id);


                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                }
                            });
                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.getMessage();
                    }
                }
                Log.e("TeamLead Tabname Resul ", result);
            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);


        googleMap.setIndoorEnabled(true);

//        String str_origin = "origin=" + delhi.latitude + "," + delhi.longitude;
//        String str_dest = "destination=" + chandigarh.latitude + "," + chandigarh.longitude;
//        String sensor = "sensor=false";
//        String parameters = str_origin + "&" + str_dest + "&" + sensor;
//        String output = "json";
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//
//        Log.d("onMapClick", url.toString());
//        FetchUrl FetchUrl = new FetchUrl();
//        FetchUrl.execute(url);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(delhi));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(7));
//
//        Location delhi_location = new Location("Delhi");
//        delhi_location.setLatitude(delhi.latitude);
//        delhi_location.setLongitude(delhi.longitude);
//
//        Location chandigarh_location = new Location("Chandigarh");
//        chandigarh_location.setLatitude(chandigarh.latitude);
//        chandigarh_location.setLongitude(chandigarh.longitude);
//
//        double distance = (delhi_location.distanceTo(chandigarh_location))* 0.000621371 ;
//
//        Log.e("MAp Activity","Distance " +distance +"Miles");
//



    }



    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";

            try {
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                JSONParserTask parser = new JSONParserTask();
                Log.d("ParserTask", parser.toString());
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }








//    public void AditysirCode() {
//
//        //  new DrawLine().execute();
//
//
//
//
//
////        sydney = new LatLng(22.7184297, 75.8659051);
////        LatLng sydney1 = new LatLng(22.7160477, 75.8593346);
////        sydney2 = new LatLng(22.7184297, 75.8659051);
////
////        PolylineOptions options = new PolylineOptions().add(sydney)
////                .add(sydney1).add(sydney2).width(10f).color(Color.RED).geodesic(true);
////
////        mMap.addPolyline(options);
//        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
//
//
////        GoogleDirection.withServerKey("AIzaSyCNiAydOeBXd5XvVddfFMNrVvqxLMzfYu4")
////                .from(new LatLng(22.719568, 75.857727))
////                .to(new LatLng(22.7139593, 75.8662712))
////                .execute(new DirectionCallback() {
////                    @Override
////                    public void onDirectionSuccess(Direction direction, String rawBody) {
////                        // Do something here
////                        Log.e("Direction", " " + direction.getRouteList() + "    --------  " + rawBody);
////                    }
////
////                    @Override
////                    public void onDirectionFailure(Throwable t) {
////                        // Do something here
////                        Log.e("faulath", "" + t);
////                    }
////                });
//
//
//
//
//        class DrawLine extends AsyncTask<Void, Void, ArrayList<Address>> {
//
//            @Override
//            protected ArrayList<Address> doInBackground(Void... strings) {
//
//                String[] strings1 = new String[2];
//
//                strings1[0] = "Indore";
//                strings1[1] = "Bhopal";
//
//                Geocoder coder = new Geocoder(MapsActivity.this);
//                ArrayList<Address> location = new ArrayList<>();
//                for (int i = 0; i < strings1.length; i++) {
//                    List<Address> address;
//
//                    try {
//                        address = coder.getFromLocationName(strings1[i], 5);
//                        if (address == null) {
//                            break;
//                        } else {
//                            location.add(address.get(0));
//                        }
//                    } catch (Exception e) {
//                        System.out.println("in catch");
//                        break;
//                    }
//                }
//
//
//                return location;
//            }
//
//
//            @Override
//            protected void onPostExecute(ArrayList<Address> address) {
//                super.onPostExecute(address);
//
//                if (address.size() == 2) {
//                    System.out.println("show address1 " + address);
//                    LatLng chandigarh = new LatLng(address.get(0).getLatitude(), address.get(0).getLongitude());
//                    LatLng delhi = new LatLng(address.get(1).getLatitude(), address.get(1).getLongitude());
//                    System.out.println("show address " + address.get(0).getLongitude());
//                    // chandigarh = new LatLng(8.0883,77.5385);
//
//                    Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.arrow_down)).getBitmap();
//                    mMap.addMarker(new MarkerOptions().position(chandigarh).title("Indore").icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
//                    mMap.addMarker(new MarkerOptions().position(delhi).title("Bhopal").icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
//
//                    String str_origin = "origin=" + "22.719568" + "," + "75.857727";
//                    String str_dest = "destination=" + "23.2687436" + "," + "77.4242152";
//                    String sensor = "sensor=false";
//                    String parameters = str_origin + "&" + str_dest + "&" + sensor;
//                    String output = "json";
//                    String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyCNiAydOeBXd5XvVddfFMNrVvqxLMzfYu4";
//
//                    Log.d("onMapClick", url);
//                    FetchUrl fetchUrl = new FetchUrl();
//                    fetchUrl.execute(url);
//
//                    final LatLngBounds.Builder builder = new LatLngBounds.Builder();
//
//                    builder.include(chandigarh);
//                    builder.include(delhi);
//
//
//                    mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
//                        @Override
//                        public void onMapLoaded() {
//                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 17));
//                        }
//                    });
//
//
//                } else {
//                    // popup.popupfun("No location");
//                }
//            }
//        }
//
//        private class FetchUrl extends AsyncTask<String, Void, String> {
//
//            @Override
//            protected String doInBackground(String... url) {
//                String data = "";
//
//                try {
//                    data = downloadUrl(url[0]);
//                    Log.d("Background Task data", data);
//                } catch (Exception e) {
//                    Log.d("Background Task", e.toString());
//                }
//                return data;
//
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
//
             //   ParserTask parserTask = new ParserTask();
//                parserTask.execute(result);
//
//            }
//        }
//
//        private String downloadUrl (String strUrl) throws IOException {
//            String data = "";
//            InputStream iStream = null;
//            HttpURLConnection urlConnection = null;
//            try {
//                URL url = new URL(strUrl);
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.connect();
//                iStream = urlConnection.getInputStream();
//                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
//                StringBuffer sb = new StringBuffer();
//                String line = "";
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                }
//                data = sb.toString();
//                br.close();
//            } catch (Exception e) {
//                Log.d("Exception", e.toString());
//            } finally {
//                iStream.close();
//                assert urlConnection != null;
//                urlConnection.disconnect();
//            }
//            return data;
//        }
//
//
//        private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
//
//
//            @Override
//            protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
//
//                JSONObject jObject;
//                List<List<HashMap<String, String>>> routes = null;
//
//                try {
//                    jObject = new JSONObject(jsonData[0]);
//                    Log.d("ParserTask", jsonData[0]);
//                    JSONParserTask parser = new JSONParserTask();
//                    Log.d("ParserTask", parser.toString());
//                    routes = parser.parse(jObject);
//                    Log.d("ParserTask", "Executing routes");
//                    Log.d("ParserTask", routes.toString());
//
//                } catch (Exception e) {
//                    Log.d("ParserTask", e.toString());
//                    e.printStackTrace();
//                }
//                return routes;
//            }
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//
//
//            }
//
//            @Override
//            protected void onPostExecute(List<List<HashMap<String, String>>> result) {
//                ArrayList<LatLng> points;
//                PolylineOptions lineOptions = null;
//                System.out.println("result show " + result);
//                for (int i = 0; i < result.size(); i++) {
//                    points = new ArrayList<>();
//                    lineOptions = new PolylineOptions();
//                    List<HashMap<String, String>> path = result.get(i);
//                    for (int j = 0; j < path.size(); j++) {
//                        HashMap<String, String> point = path.get(j);
//                        double lat = Double.parseDouble(point.get("lat"));
//                        double lng = Double.parseDouble(point.get("lng"));
//                        LatLng position = new LatLng(lat, lng);
//                        points.add(position);
//                    }
//                    lineOptions.addAll(points);
//                    lineOptions.width(8);
//                    lineOptions.color(getResources().getColor(R.color.red));
//
//                    Log.d("onPostExecute", "onPostExecute lineoptions decoded");
//
//                }
//                if (lineOptions != null) {
//                    mMap.addPolyline(lineOptions);
//                    //progress.setVisibility(View.GONE);
//                } else {
//                    Log.d("onPostExecute", "without Polylines drawn");
//                }
//            }
//        }
//
//    }



}
