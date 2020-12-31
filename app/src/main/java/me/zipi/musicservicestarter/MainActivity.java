package me.zipi.musicservicestarter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainActivity extends AppCompatActivity {

    private final MusicService musicService = new MusicService();

    @Override
    protected void onResume() {
        super.onResume();

        AppUpdater appUpdater = new AppUpdater(this)
                .setDisplay(Display.DIALOG)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setGitHubUserAndRepo("zipizigi", "music-service-starter");
        appUpdater.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.onRefresh(null);

        ((TextView) findViewById(R.id.viewLog)).setMovementMethod(new ScrollingMovementMethod());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                log.debug("Permission is granted");
            } else {
                log.warn("Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        }
    }

    public void onStartMelon(View view) {
        new Thread(() -> {
            String log = musicService.startService(this, ServiceEnum.Melon);
            this.writeLog(log);
            this.onRefresh(null);
        }).start();
    }

    public void onStopMelon(View view) {
        new Thread(() -> {
            String log = musicService.stopService(this, ServiceEnum.Melon);
            this.writeLog(log);
            this.onRefresh(null);
        }).start();
    }

    public void setDefaultMelon(View view) {
        new Thread(() -> {
            String log = musicService.saveService(this, ServiceEnum.Melon);
            this.writeLog(log);
            this.onRefresh(null);
        }).start();
    }

    public void onStartVibe(View view) {
        new Thread(() -> {
            String log = musicService.startService(this, ServiceEnum.Vibe);
            this.writeLog(log);
            this.onRefresh(null);
        }).start();
    }

    public void onStopVibe(View view) {
        new Thread(() -> {
            String log = musicService.stopService(this, ServiceEnum.Vibe);
            this.writeLog(log);
            this.onRefresh(null);
        }).start();
    }

    public void setDefaultVibe(View view) {
        new Thread(() -> {
            String log = musicService.saveService(this, ServiceEnum.Vibe);
            this.writeLog(log);
            this.onRefresh(null);
        }).start();
    }

    public void onRefresh(View view) {
        new Thread(() -> {
            ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
            List<String> services = musicService.getRunningService(am);


            StringBuilder sb = new StringBuilder();
            for (String service : services) {
                sb.append(service).append("\n");
            }
            runOnUiThread(() -> {
                TextView viewRunningService = (TextView) findViewById(R.id.viewRunningService);
                viewRunningService.setText(sb.toString());
                TextView appLabel = (TextView) findViewById(R.id.defaultApp);

                appLabel.setText(
                        musicService.loadSavedService(getApplicationContext()).name()
                );
            });
        }).start();
    }

    @SuppressLint("SetTextI18n")
    public void writeLog(String logString) {
        this.runOnUiThread(() -> {
            TextView viewLog = (TextView) findViewById(R.id.viewLog);

            viewLog.setText(viewLog.getText() + "\n" + logString);

            log.info(logString);
        });
    }


}