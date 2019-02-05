package coms.example.cwaa1.stellar.FireBaseNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import coms.example.cwaa1.stellar.Chat.ChatActivity;
import coms.example.cwaa1.stellar.R;
import coms.example.cwaa1.stellar.SharedPreference;

public class MyFirebaseMessagingService  extends FirebaseMessagingService {


    LocalBroadcastManager broadcaster;

    String msg;
    private SharedPreference sharedPreference;
    private String value;
    private JSONArray jsonArray;
    private JSONObject jsonObject;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        Log.e("Borad cast","in the msg"+remoteMessage.toString());

        Log.d("Msg", "Message received [" + remoteMessage.toString() + "]");


        System.out.println("eroorrrr uper "+ remoteMessage.getNotification());
        System.out.println("eroor2 data" +remoteMessage.getData());
        sharedPreference = new SharedPreference(this);
        value = sharedPreference.getJsonArray();




        if (remoteMessage.getData()!= null) {

            Log.d("CompleteFormat",remoteMessage.getData().toString());

            String[] mg= remoteMessage.getData().get("message").split("=");

            System.out.println("dddddd mg[0]  " +mg[0]);
            System.out.println("dddd id mg[2]  " +mg[2]);
            System.out.println("dddddd time  mg[4]  " +mg[4]);
            System.out.println("dddddd senderName mg[6]  " +mg[6]);

            if(value == null) {
                jsonArray = new JSONArray();
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("Id",mg[2]);
                    jsonObject.put("Count",1);
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else{
                int i;
                try {
                    jsonArray = new JSONArray(value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for( i = 0; i<jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        if(jsonObject.getString("Id").equals(mg[2])) {
                            int count1 = jsonObject.getInt("Count");
                            count1++;
                            jsonObject.put("Count",count1);
                            jsonArray.remove(i);
                            jsonArray.put(jsonObject);
                            break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(i == jsonArray.length()) {
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("Id",mg[2]);
                        jsonObject.put("Count",1);
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            sharedPreference.setJsonArray(jsonArray.toString());

            Log.e("Error is cming",sharedPreference.getJsonArray());





            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("executiveid",mg[2]);
            intent.putExtra("Name",mg[4]);

            broadcaster = LocalBroadcastManager.getInstance(getBaseContext());
            Intent intent1 = new Intent("hello");
            intent1.putExtra("Message", mg[0]);
            intent1.putExtra("senderid",mg[2]);
            intent1.putExtra("tem",mg[4]);
            broadcaster.sendBroadcast(intent1);

            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),1,intent,PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"default")
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                    .setSmallIcon(R.drawable.stellarlogo)
                    .setContentTitle(mg[6])
                    .setContentText(mg[0])
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(contentIntent)
                    .setVibrate( new long[]{1000,1000});

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1,notificationBuilder.build());
            initChannels(getApplicationContext());

        }
        else{
            Toast.makeText(this, "Currently network not available", Toast.LENGTH_SHORT).show();

        }
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