package coms.example.cwaa1.stellar.Location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

public class LocationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        Log.e("Hello",""+calendar.getTime());
        //context.startService(new Intent(context,LocationTrackerService.class));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, LocationTrackerService.class));
        } else {
            context.startService(new Intent(context, LocationTrackerService.class));
        }
    }

}
