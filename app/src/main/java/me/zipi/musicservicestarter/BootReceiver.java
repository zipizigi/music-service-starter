package me.zipi.musicservicestarter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

public class BootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.i("boot receiver", "received");
            //new Thread(() -> {
            new Handler().postDelayed(() -> {
                new MusicService().startService(context);
                new AppUpdater(context)
                        .setDisplay(Display.NOTIFICATION)
                        .setUpdateFrom(UpdateFrom.GITHUB)
                        .setGitHubUserAndRepo("zipizigi", "music-service-starter")
                        .start();
            }, 5000);
            //}).start();
        }
    }

}
