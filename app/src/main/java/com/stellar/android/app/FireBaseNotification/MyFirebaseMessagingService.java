package com.stellar.android.app.FireBaseNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stellar.android.app.Chat.ChatActivity;
import com.stellar.android.app.Constants;
import com.stellar.android.app.DashBordActivity;
import com.stellar.android.app.ManagerAssign.AprovedTaskActivity;
import com.stellar.android.app.R;
import com.stellar.android.app.SharedPreference;
import com.stellar.android.app.teamTask.TeamPandingTaskActivity;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    LocalBroadcastManager broadcaster;

    String msg;
    private SharedPreference sharedPreference;
    private String value;
    private JSONArray jsonArray;
    private JSONObject jsonObject;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        Log.e("Borad cast", "in the msg " + remoteMessage.toString());


        sharedPreference = new SharedPreference(this);
        value = sharedPreference.getJsonArray();


        SharedPreferences preferences =  getSharedPreferences("stelleruser", MODE_PRIVATE);
        int l = preferences.getInt("log", 0);

        if (remoteMessage.getData() != null && l==1) {


            Log.d("CompleteFormat", remoteMessage.getData().toString());
            String type = remoteMessage.getData().get("type");
            System.out.println("type " + type);

            if (type.equalsIgnoreCase("chat")) {
                chatRechiver(remoteMessage.getData());
            }
            if (type.equalsIgnoreCase("task approve request")) {
                getRequestForApprove(remoteMessage.getData());
            }
            if (type.equalsIgnoreCase("approve task_by parent")) {
                getApprovelResult(remoteMessage.getData());
            }
            if (type.equalsIgnoreCase("Task not done 48 Man")) {
                getPandingTaskToManager(remoteMessage.getData());
            }
            if (type.equalsIgnoreCase("Next followup")) {
                getNextFollowUp(remoteMessage.getData());
            }
            if (type.equalsIgnoreCase("Pending followup")) {
                getPandingFollowUp(remoteMessage.getData());
            }
            if (type.equalsIgnoreCase("next day schedule")) {
                getNextDayShedule(remoteMessage.getData());
            }
            if (type.equalsIgnoreCase("pending task for approval")) {
                getTodaysPendingTask(remoteMessage.getData());
            }

        } else {
            //Toast.makeText(this, "Currently network not available", Toast.LENGTH_SHORT).show();

        }
    }

    private void getTodaysPendingTask(Map<String, String> data) {
        Intent intent = new Intent(this, DashBordActivity.class);
        String date = "", count = "", day = "";
        try {
            JSONObject object = new JSONObject(data.get("body"));
            date = object.getString("date");
            count = object.getString("count");
            day = object.getString("day");


            intent.putExtra("taskname", date);
            intent.putExtra("status", count);
            intent.putExtra("day", day);
            intent.putExtra(Constants.Type, Constants.ApproveResult);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        showNotification("Pending Tasks ", "You have " + count + " Tasks is Pending " + day, intent);


    }

    private void getNextDayShedule(Map<String, String> data) {

        Intent intent = new Intent(this, DashBordActivity.class);
        String date = "", count = "", day = "";
        try {
            JSONObject object = new JSONObject(data.get("body"));
            date = object.getString("date");
            count = object.getString("count");
            day = object.getString("day");


            intent.putExtra("taskname", date);
            intent.putExtra("status", count);
            intent.putExtra("day", day);
            intent.putExtra(Constants.Type, Constants.ApproveResult);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        showNotification("Next Day Schedule ", "You have " + count + " Tasks For Tomorrow" + day, intent);




    }

    private void getPandingFollowUp(Map<String, String> data) {

        Intent intent = new Intent(this, DashBordActivity.class);
        String lead_id = "", name = "", last_followup_date = "";
        try {
            JSONObject object = new JSONObject(data.get("body"));
            lead_id = object.getString("lead_id");
            name = object.getString("name");
            last_followup_date = object.getString("last_followup_date");


            intent.putExtra("lead_id", lead_id);
            intent.putExtra("name", name);
            intent.putExtra("lastDate", last_followup_date);
            intent.putExtra(Constants.Type, Constants.NEXTFOLLWUP);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        showNotification("Your FollowUp is Pending ", getString(R.string.nextfollowup) + "" + name + " Last Date Was " + last_followup_date, intent);


    }

    private void getNextFollowUp(Map<String, String> data) {

        Intent intent = new Intent(this, DashBordActivity.class);
        String lead_id = "", name = "", last_followup_date = "";
        try {
            JSONObject object = new JSONObject(data.get("body"));
            lead_id = object.getString("lead_id");
            name = object.getString("name");
            last_followup_date = object.getString("last_followup_date");


            intent.putExtra("lead_id", lead_id);
            intent.putExtra("name", name);
            intent.putExtra("lastDate", last_followup_date);
            intent.putExtra(Constants.Type, Constants.NEXTFOLLWUP);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        showNotification("Next FollowUp ", getString(R.string.nextfollowup) + " " + name + " Last Date is " + last_followup_date, intent);


    }


    private void getPandingTaskToManager(Map<String, String> data) {

        System.out.print("48Hours task " + data.get("body"));

        Intent intent = new Intent(this, TeamPandingTaskActivity.class);
        String name = "", pendingtask = "", day = "";
        try {
            JSONObject object = new JSONObject(data.get("body"));
            name = object.getString("name");
            pendingtask = object.getString("pending task");
            day = object.getString("day");

            //Log.e("fcm ", "" + in + " name " + name);
            intent.putExtra("name", name);
            intent.putExtra("taskcount", pendingtask);
            intent.putExtra(Constants.KEY_DAY, day);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        showNotification("Employee Task Pending ", "Name :  " + name + " Task " + pendingtask, intent);


    }

    private void getApprovelResult(Map<String, String> data) {

        Intent intent = new Intent(this, DashBordActivity.class);
        String task_name = "", status = "", day = "";
        try {
            JSONObject object = new JSONObject(data.get("body"));
            task_name = object.getString("task_name");
            status = object.getString("status");
            day = object.getString("day");


            intent.putExtra("taskname", task_name);
            intent.putExtra("status", status);
            intent.putExtra("day", day);
            intent.putExtra(Constants.Type, Constants.ApproveResult);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        showNotification("Response By  " + task_name, "Status " + status + " Day " + day, intent);


    }

    private void getRequestForApprove(Map<String, String> data) {

        Intent intent = new Intent(this, AprovedTaskActivity.class);
        String name = "", date = "", taskname = "";
        try {
            JSONObject object = new JSONObject(data.get("body"));
            name = object.getString("name");
            date = object.getString("scheduled_date");
            taskname = object.getString("task_name");

            intent.putExtra("senderName", name);
            intent.putExtra("date", date);
            intent.putExtra("taskname", taskname);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        showNotification(name + " Send Approve Task Request ", "TaskName " + taskname + " Date " + date, intent);

    }

    private void chatRechiver(Map<String, String> data) {

        try {
            JSONObject object = new JSONObject(data.get("body"));


            String st = object.getString("message");
            String[] mg = st.split("=");

            System.out.println("dddddd mg[0]  " + mg[0]);
            System.out.println("dddd id mg[2]  " + mg[2]);
            System.out.println("dddddd time  mg[4]  " + mg[4]);
            System.out.println("dddddd senderName mg[6]  " + mg[6]);

            if (value == null) {

                jsonArray = new JSONArray();
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("Id", mg[2]);
                    jsonObject.put("Count", 1);
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                int i;
                try {
                    jsonArray = new JSONArray(value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.getString("Id").equals(mg[2])) {
                            int count1 = jsonObject.getInt("Count");
                            count1++;
                            jsonObject.put("Count", count1);
                            jsonArray.remove(i);
                            jsonArray.put(jsonObject);
                            break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (i == jsonArray.length()) {
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("Id", mg[2]);
                        jsonObject.put("Count", 1);
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            sharedPreference.setJsonArray(jsonArray.toString());

            Log.e("Error is cming", sharedPreference.getJsonArray());


            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("executiveid", mg[2]);
            intent.putExtra("Name", mg[4]);

            broadcaster = LocalBroadcastManager.getInstance(getBaseContext());
            Intent intent1 = new Intent("hello");
            intent1.putExtra("Message", mg[0]);
            intent1.putExtra("senderid", mg[2]);
            intent1.putExtra("tem", mg[4]);
            broadcaster.sendBroadcast(intent1);

            showNotification(mg[6], mg[0], intent);

        } catch (Exception e) {
            e.getMessage();
        }


    }


    public void showNotification(String title, String discription, Intent intent) {

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "default")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.stellarnewlogo))
                .setSmallIcon(R.drawable.stellarnewlogo)
                .setContentTitle(title)
                .setContentText(discription)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(contentIntent)
                .setVibrate(new long[]{1000, 1000});

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
        initChannels(getApplicationContext());
    }

    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "Channel name",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
    }
}