package coms.example.cwaa1.stellar.Location;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import coms.example.cwaa1.stellar.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LocationTrackerService extends Service {
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
        private double latitude = 0.0;
    private double longitude = 0.0;
    private String tii;
    private LocationManager locationManager;
    // private FusedLocationProviderClient mFusedLocationClient;
    private Time today;
    private String user_id;
    private String service_cordinator_id = "admin";
    private Location loc;
    private SharedPreferences preferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // context = this;
        today = new Time(Time.getCurrentTimezone());
        today.setToNow();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        methodForGettingCurrentTimeAndLocation();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());


        return super.onStartCommand(intent, flags, startId);

    }

    private void methodForGettingCurrentTimeAndLocation() {
        //Toast.makeText(this, "Method is running..", Toast.LENGTH_SHORT).show();
        preferences = getSharedPreferences("stelleruser", MODE_PRIVATE);
        service_cordinator_id = preferences.getString("empid", "");
        //empid


        Calendar c = Calendar.getInstance();
        String time1 = new SimpleDateFormat("HH:mm:ss").format(c.getTime());

        tii = "" + today.year + "-" + today.month + "-" + today.monthDay + " " + time1;


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (locationManager != null) {
            loc = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                latitude = loc.getLatitude();
                longitude = loc.getLongitude();
                Log.e("service emp id", " Hello " + service_cordinator_id);

                Log.e("Complete Location", "" + tii + " " + service_cordinator_id + " " + latitude + " " + longitude);
//                LocationModelClass lo = new LocationModelClass();
//                lo.setLatitude(String.valueOf(latitude));
//                lo.setLongitude(String.valueOf(longitude));
//                lo.setTime(tii);

                apicall(latitude,longitude);

//
//                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//                List<Address> addresses = null;
//                try {
//                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
//                    if (addresses.size() > 0) {
//                        String result = addresses.get(0).getAddressLine(0) + ":";
//
//                        Log.e("service geocoder res", result);
//                        Log.e("service geocoder cname", addresses.get(0).getCountryName());
//                        Log.e("service geocoder c code", addresses.get(0).getCountryCode());
////                        Log.e("service geocoder phone", addresses.get(0).getPhone());
//
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            } else {
                Log.e("Message","Location is null");

            }
        } else {
            Log.e("Message","Location manager is null");
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
        // mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            buildAlertMessageNoGps();
//        }
//        else {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "Method is running..again", Toast.LENGTH_SHORT).show();
//            Location location =
//           mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    if(location != null) {
//                        latitude = location.getLatitude();
//                        longitude = location.getLongitude();
//                    }
//                    else {
//                        Toast.makeText(LocationTrackerService.this, "Location is null", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });



    }

    private void apicall(double latitude, double longitude) {

        String lat=String.valueOf(latitude).trim();
        String log=String.valueOf(longitude).trim();
        Log.e("lat Long",lat+" /"+log);

         String key = getsha256("getGEOAddress");
        Log.e("Service key", key);
        OkHttpClient client=new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("action", "getGEOAddress")
                .add("key", key)
                .add("latitude",lat )
                .add("longitude",log )
                .add("employee_id",service_cordinator_id)
                .build();
        Request request=new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.e("Netwok Problem"+"inservice","here");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                Log.e("Service Api ","Is runnuing here "+response.body().string());
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


    //        sendAttendenceToServer();


//    }
//    }


//    @Override
//    public void onLocationChanged(Location location) {
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
//    }
//
//    @Override
//    public void onStatusChanged(String s, int i, Bundle bundle) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String s) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String s) {
//
//    }





    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.stellarlogo)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }












}
