package coms.example.cwaa1.stellar.FireBaseNotification;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import coms.example.cwaa1.stellar.LoginActivity;

public class FirebaseInstanceIdservice  extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Intent intent = new Intent(this, LoginActivity.class);
        startService(intent);
        Log.d("MyRefreshedToken", token);
    }
}