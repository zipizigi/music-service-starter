package me.zipi.musicservicestarter;

import com.stericson.RootShell.RootShell;
import com.stericson.RootShell.execution.Command;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MusicService {

    private static final String _SAVE_FILE_NAME = "SERVICE";

    public String saveService(Context ctx, ServiceEnum serviceEnum) {
        StringBuilder sb = new StringBuilder();
        try (OutputStreamWriter osw = new OutputStreamWriter(ctx.openFileOutput(_SAVE_FILE_NAME, Context.MODE_PRIVATE))) {
            osw.write(serviceEnum.name());
            sb.append("Save service success: ").append(serviceEnum.toString());
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            sb.append("Save fail: ").append(serviceEnum.toString())
                    .append("\n").append(e.toString());
        }
        return sb.toString();
    }

    public ServiceEnum loadSavedService(Context context) {
        ServiceEnum serviceEnum = ServiceEnum.Melon;

        try (InputStream inputStream = context.openFileInput(_SAVE_FILE_NAME);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
        ) {
            String receiveString;
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append("\n").append(receiveString);
            }

            serviceEnum = ServiceEnum.valueOf(stringBuilder.toString().trim());

        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return serviceEnum;
    }

    public void startService(Context ctx) {
        this.startService(ctx, loadSavedService(ctx));
    }

    public String startService(Context ctx, ServiceEnum serviceEnum) {
        return this.controlService(ctx, true, serviceEnum);
    }

    public String stopService(Context ctx, ServiceEnum serviceEnum) {
        return this.controlService(ctx, false, serviceEnum);
    }

    private String controlService(Context ctx, boolean isStart, ServiceEnum serviceEnum) {
        String pkg = serviceEnum.getPkg();
        String service = serviceEnum.getService();
        StringBuilder sb = new StringBuilder();


        sb.append("try ").append((isStart ? "running...\n" : "stopping...\n"))
                .append("Package: ").append(pkg).append("\n")
                .append("Service: ").append(service).append("\n");
        try {
            if (RootShell.isRootAvailable() && RootShell.isAccessGiven()) {
                sb.append("with root. permission \n");
                Set<String> status = new HashSet<>();
                Command command = new Command(0, String.format("am %s -n %s/%s", (isStart ? "startservice" : "stopservice"), pkg, service)) {
                    @Override
                    public void commandOutput(int id, String line) {
                        sb.append("\t").append(line).append("\n");
                        super.commandOutput(id, line);
                    }

                    @Override
                    public void commandTerminated(int id, String reason) {
                        status.add("terminated");
                        sb.append("\tcommand terminated.")
                                .append(reason);
                    }

                    @Override
                    public void commandCompleted(int id, int exitcode) {
                        status.add("completed");
                        sb.append("\tcommand completed.");
                    }

                };
                RootShell.getShell(true).add(command);
                for (int i = 0; i < 50; i++) {
                    if (status.size() > 0)
                        break;
                    Thread.sleep((i + 1) * 1000);
                }

            } else {
                Intent i = new Intent();
                i.setComponent(new ComponentName(pkg, service));
                if (isStart)
                    ctx.startService(i);
                else
                    ctx.stopService(i);
            }
            Toast.makeText(ctx, (isStart ? "Start. " : "Stop. ") + serviceEnum.getPkg(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            sb.append("Error: ").append(e.toString());

            Toast.makeText(ctx, "fail. " + serviceEnum.getPkg(), Toast.LENGTH_SHORT).show();
        }

        return sb.toString();
    }

    public List<String> getRunningService(ActivityManager am) {
        List<String> result = new ArrayList<>();
        Set<String> enumSet = new HashSet<>();

        for (ServiceEnum serviceEnum : ServiceEnum.values()) {
            enumSet.add(serviceEnum.getService().toLowerCase());
        }
        for (ActivityManager.RunningServiceInfo service : am.getRunningServices(Integer.MAX_VALUE)) {
            if (enumSet.contains(service.service.getClassName().toLowerCase())) {
                result.add(service.service.getClassName());
            }
        }
        return result;
    }
}

