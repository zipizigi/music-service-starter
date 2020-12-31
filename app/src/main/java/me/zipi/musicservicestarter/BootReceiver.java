package me.zipi.musicservicestarter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            log.info("boot complete event received");
            //new Thread(() -> {
            new Handler().postDelayed(() -> {
                new MusicService().startService(context);
                new AppUpdater(context)
                        .setDisplay(Display.NOTIFICATION)
                        .setUpdateFrom(UpdateFrom.GITHUB)
                        .setGitHubUserAndRepo("zipizigi", "music-service-starter")
                        .start();
                log.info("request music service start");
            }, 5000);
            //}).start();
        }
    }

}
