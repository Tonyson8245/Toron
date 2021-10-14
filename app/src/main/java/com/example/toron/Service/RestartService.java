package com.example.toron.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import androidx.core.app.NotificationCompat;

import com.example.toron.Main.Mainpage;
import com.example.toron.R;

public class RestartService extends Service { // 알람이 실행 됬을때 시작되는 서비스
    public RestartService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) { // 부활 시켜주는 서비스

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default"); // 노티피케이션 제작
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("부활");
        Intent notificationIntent = new Intent(this, Mainpage.class); // 인텐트 생성
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0); // pending 인텐트 생성
        builder.setContentIntent(pendingIntent); // 노티피케이션 안에 pending intent 포함

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); // 노티피케이션 매니저 객체 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_NONE));
        } // 채널 설정

        Notification notification = builder.build(); // 노티피케이션 빌드
        startForeground(1, notification);
        stopForeground(false); // 제거 됨 // 이걸 이용하면 Foreground를 안 시킬수 있다.

        /////////////////////////////////////////////////////////////////////
        Intent in = new Intent(this, RemoteService.class);
        startService(in); // 서비스를 실행 시키고

        stopSelf(); // 자살;

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

}
