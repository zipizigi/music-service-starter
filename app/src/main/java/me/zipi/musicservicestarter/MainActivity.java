package me.zipi.musicservicestarter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private final MusicService musicService = new MusicService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.onRefresh(null);

        ((TextView) findViewById(R.id.viewLog)).setMovementMethod(new ScrollingMovementMethod());

        AppUpdater appUpdater = new AppUpdater(this)
                .setDisplay(Display.DIALOG)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setGitHubUserAndRepo("zipizigi", "music-service-starter")

                ;
        appUpdater.start();
    }

    public void onStartMelon(View view) {
        String log = musicService.startService(getApplicationContext(), ServiceEnum.Melon);
        this.writeLog(log);
        this.onRefresh(null);
    }

    public void onStopMelon(View view) {
        String log = musicService.stopService(getApplicationContext(), ServiceEnum.Melon);
        this.writeLog(log);
        this.onRefresh(null);
    }

    public void setDefaultMelon(View view) {
        String log = musicService.saveService(getApplicationContext(), ServiceEnum.Melon);
        this.writeLog(log);
        this.onRefresh(null);
    }

    public void onStartVibe(View view) {
        String log = musicService.startService(getApplicationContext(), ServiceEnum.Vibe);
        this.writeLog(log);
        this.onRefresh(null);
    }

    public void onStopVibe(View view) {
        String log = musicService.stopService(getApplicationContext(), ServiceEnum.Vibe);
        this.writeLog(log);
        this.onRefresh(null);
    }

    public void setDefaultVibe(View view) {
        String log = musicService.saveService(getApplicationContext(), ServiceEnum.Vibe);
        this.writeLog(log);
        this.onRefresh(null);
    }

    public void onRefresh(View view) {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<String> services = musicService.getRunningService(am);
        TextView viewRunningService = (TextView) findViewById(R.id.viewRunningService);

        StringBuilder sb = new StringBuilder();
        for (String service : services) {
            sb.append(service).append("\n");
        }
        viewRunningService.setText(sb.toString());
        TextView appLabel = (TextView) findViewById(R.id.defaultApp);

        appLabel.setText(
                musicService.loadSavedService(getApplicationContext()).name()
        );
    }

    @SuppressLint("SetTextI18n")
    public void writeLog(String log) {
        TextView viewLog = (TextView) findViewById(R.id.viewLog);

        viewLog.setText(viewLog.getText() + "\n" + log);
    }

}