package me.zipi.musicservicestarter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            new Handler().postDelayed(
                    () -> {
                        Log.i("boot-receiver", "Start service");
                        new MusicService().startService(context);
                    },
                    5000);
        }
    }

}
