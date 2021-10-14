package com.example.toron.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class RebootRecever extends BroadcastReceiver { // 재 부팅 시 실행 되는 BroadcastReceiver
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent in = new Intent(context, RestartService.class);
            context.startForegroundService(in); // Oreo 이상부터는 이게 돌아감 // 실행된 서비스는 5초내에 startForeground 를 실행하여 노티를 띄워야 함. 이 서비스는 5초 뒤에 뒤짐
        } else {
            Intent in = new Intent(context, RemoteService.class);
            context.startService(in); // 이건 안되더라
        }
    }
}
