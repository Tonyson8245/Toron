package com.example.toron.Service;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.toron.Debate.Debate_room;
import com.example.toron.Main.Mainpage;
import com.example.toron.R;
import com.example.toron.Service.Class.Chat;
import com.example.toron.Service.Class.Request;
import com.example.toron.Service.Class.Response;
import com.example.toron.Service.Class.Room_data;
import com.example.toron.Service.Class.Status_Foreground;
import com.example.toron.Service.Class.System_data;
import com.example.toron.Service.Socket.ConnectionThread;
import com.example.toron.Service.Socket.SendToServerThread;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RemoteService extends Service {
    private Thread mainThread;

    MessageThread messageThread;
    Socket socket;
    Boolean isRunning = true;
    Gson gson = new Gson();

    public static Intent serviceIntent = null;

    private final String TAG = "!!!RemoteService";
    public static final int MSG_CLIENT_CONNECT = 1;
    public static final int MSG_CLIENT_DISCONNECT = 2;
    public static final int MSG_ADD_VALUE = 3;
    public static final int MSG_ADDED_VALUE = 4;
    public static final int MSG_SHOW_ROOM_LIST = 5;
    public static final int MSG_GET_CHAT_LIST = 6;
    public static final int MSG_SEND_MSG = 7;
    public static final int MSG_GET_CHAT = 8;
    public static final int MSG_CHECK_ACTIVITY = 9;
    public static final int MSG_SEND_IMG = 10;
    public static final int MSG_QUIT_SOCKET = 11;

    String user_idx;
    NotificationChannel notificationChannel_chat;

    private Messenger mClientCallbacks = new Messenger(new CallbackHandler());
    // 서비스에 연결된 액티비티(Messenger) 리스트
    // 서비스 -> 액티비티: 이벤트 전달 할 때 해당 Messenger 에 이벤트 전달
    final Messenger mMessenger = new Messenger(new CallbackHandler());
    // 액티비티 -> 서비스: 이벤트를 전달할 때, 사용 하는 객체.
    // 이벤트의 결과는 CallbackHandler 에서 처리

    int mValue = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences;
        sharedPreferences = getApplication().getSharedPreferences("user_data",0);
        user_idx = sharedPreferences.getString("user_idx",null);
        createNotificationChannel("DEFAULT", "default channel", NotificationManager.IMPORTANCE_HIGH);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //immortal service
        serviceIntent = intent; // 메인 액티비티에서 전달받은 인텐트 대입
//        showToast(getApplication(), "Start Service"); // 서비스 시작 토스트

        start_connect(user_idx);
        Log.d(TAG,"부활!!");

        return START_NOT_STICKY; //START_NOT_STICKY : 강제로 종료된 서비스가 재시작 하지 않음
     }

    public void showToast(final Application application, final String msg) {
        Handler h = new Handler(application.getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(application, msg, Toast.LENGTH_LONG).show();
            }
        });
    } // 토스트 보여주는 함수 서비스에서는 작동이 안되기때문에 어플리케이션 값을 받아오느는듯

    @Override
    public void onDestroy() { // 종료시 (=서비스가 꺼질 상황이 발생 시)
        super.onDestroy();
        setAlarmTimer(); // 여기서 알람을 만든다.

        Request request = new Request("QUIT",null);
        SendToServerThread sendToServerThread = new SendToServerThread(socket,gson.toJson(request));
        sendToServerThread.start(); // 채팅 연결 종료

        serviceIntent = null; // 서비스 인텐트 null
        Thread.currentThread().interrupt(); // 서비스 쓰레드를 인터럽트 시켜서 더이상 작동이 안되게 한다. 이러면 null 이 될거다.

        if (messageThread != null) { // mainThread 도 종료 시킨다.
            messageThread.interrupt();
            messageThread = null;
        }
    }

    public void quit_socket(){
        Request request = new Request("QUIT",null);
        SendToServerThread sendToServerThread = new SendToServerThread(socket,gson.toJson(request));
        sendToServerThread.start(); // 채팅 연결 종료

        serviceIntent = null; // 서비스 인텐트 null
        Thread.currentThread().interrupt(); // 서비스 쓰레드를 인터럽트 시켜서 더이상 작동이 안되게 한다. 이러면 null 이 될거다.

        if (messageThread != null) { // mainThread 도 종료 시킨다.
            messageThread.interrupt();
            messageThread = null;
        }
    }

    protected void setAlarmTimer() {
        final Calendar c = Calendar.getInstance(); // 캘린더변수 생성
        c.setTimeInMillis(System.currentTimeMillis()); // 현재 시간
        c.add(Calendar.MILLISECOND, 1); // 현재시간 + 1초
        Log.d("!!!time",c.getTime().toString());
        Intent intent = new Intent(this, AlarmReceiver.class); // 알람 리시버에 보낼 인텐트 생성
        intent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);

        PendingIntent sender = PendingIntent.getBroadcast(this, 0,intent,0); // 이 서비스로부터 알람 인텐를 포함한 PendingIntent 생성
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); // 실제 알람 설정할 시스템 알람 객체
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);// 알람 개체 알람을 포함하여 보내준다. //AlarmManager.RTC_WAKEUP 일정 기간 이후 브로드캐스트를 한 번 발생 시키는 타입
        //set으로 하면 지연시간 발생되더라
    }


    private class CallbackHandler  extends Handler {
        @Override
        public void handleMessage( Message msg ){
            switch( msg.what ){
                case MSG_CLIENT_CONNECT:
                    Log.d(TAG, "Received MSG_CLIENT_CONNECT message from client");
                    mClientCallbacks = msg.replyTo;
                    break;
                case MSG_CLIENT_DISCONNECT:
                    Log.d(TAG, "Received MSG_CLIENT_DISCONNECT message from client");
                    break;
                case MSG_ADD_VALUE:
                    Log.d(TAG, "Received message from client: MSG_ADD_VALUE");
                    mValue += msg.arg1;
//                    for (int i = mClientCallbacks.size() - 1; i >= 0; i--) {
                        try{
                            Log.d(TAG, "Send MSG_ADDED_VALUE message to client");
                            Message added_msg = Message.obtain(
                                    null, RemoteService.MSG_ADDED_VALUE);
                            added_msg.arg1 = mValue;
                            mClientCallbacks.send(added_msg);
                        }
                        catch( RemoteException e){
                            mClientCallbacks = null;
                        }
                    break;
                case MSG_SHOW_ROOM_LIST:
                    request_show_roomList();
                    break;
                case MSG_GET_CHAT_LIST:
                    request_chat_list(msg.arg1);
                    break;
                case MSG_SEND_MSG:
                    request_send_msg(msg);
                    break;
                case MSG_CHECK_ACTIVITY:
                    get_chat(msg);
                    break;
                case MSG_SEND_IMG:
                    request_send_img(msg);
                    break;
                case MSG_QUIT_SOCKET:
                    quit_socket();
            }
        }
    }
    // 액티비티에서 발생한 이벤트를 Messenger 를 통해 여기로 전달 받게 된다.
    // 따라서 구현 시에는 Handler 에 이벤트를 추가함으로써 서비스를 제어어
    public void start_connect(String user_idx){
        ConnectionThread connectionThread = new ConnectionThread("49.247.195.99",9990,user_idx);
        connectionThread.start(); // 소켓 연결 시도
        try{
            connectionThread.join();
        }catch (Exception e){

        }
        socket = connectionThread.getSocket();//완료되면 완료된 소켓을 메인 액티비티로 전달(방만들기 등에 사용될 예정)

        messageThread = new MessageThread(socket);
        messageThread.start(); // 지속 수신 쓰레드 시작
    }

    public void request_show_roomList(){
        System_data system_data = new System_data("show_roomList",null,null);
        Request request = new Request("SYSTEM",gson.toJson(system_data));
        SendToServerThread sendToServerThread = new SendToServerThread(socket,gson.toJson(request));
        sendToServerThread.start();
        Log.d(TAG,"request_show_roomList");

    }// 서버로 방 용청

    public void show_roomList(String data){
        Bundle bundle = new Bundle();
        bundle.putString("list",data);

        try{
            Log.d(TAG, "Send MSG_SHOW_ROOM_LIST message to client");
            Message msg = Message.obtain(
                    null, RemoteService.MSG_SHOW_ROOM_LIST);
            msg.obj = bundle;
            mClientCallbacks.send(msg);
        }
        catch( RemoteException e){
            mClientCallbacks  = null;
        }
    } // 방 정보 가져온거 액티비티로 보내기

    public void get_chat_list(String data){
        Bundle bundle = new Bundle();
        bundle.putString("list",data);

        try{
            Log.d(TAG, "Send MSG_GET_CHAT_LIST message to client");
            Message msg = Message.obtain(
                    null, RemoteService.MSG_GET_CHAT_LIST);
            msg.obj = bundle;
            mClientCallbacks.send(msg);
        }
        catch( RemoteException e){
            mClientCallbacks = null;
        }
    }

    public void get_chat(Message message){
        Bundle data = (Bundle) message.obj; // 받아온 채팅 데이터

        Bundle bundle =  new Bundle(); // 보낼 데이터 만들기
        bundle.putString("chat",data.getString("chat"));
        Chat chat = gson.fromJson(data.getString("chat"),Chat.class);

        Log.d(TAG,"status " + data.getString("name") + " size:" + mClientCallbacks.toString()); // 현재 top 액티비티 파악

        if(data.getString("name")!=null) {
            if (data.getString("name").equals("room") && data.getString("room_idx").equals(chat.getRoom_idx())) { //현재 top 액티비티 파악해서 방에 있고 그방이 챗의 방과 같다면 글을 보냄
                try {
                    Log.d(TAG, "Send MSG_GET_CHAT message to client");
                    Message msg = Message.obtain(
                            null, RemoteService.MSG_GET_CHAT);
                    msg.obj = bundle;
                    mClientCallbacks.send(msg);
                } catch (RemoteException e) {
                    mClientCallbacks = null;
                }
            }
            else{
                if(chat.getTag_user_idx()!=null) {
                    if (chat.getTag_user_idx().equals(user_idx)) create_notify(chat);
                }
            }
        }
        else{
            if(chat.getTag_user_idx()!=null) {
                if (chat.getTag_user_idx().equals(user_idx))  sendNotification_outapp(chat.getRoom_idx(), "TORON",chat.getNickname(), chat.getMsg(),chat.getChat_idx(), chat.getChat_mode());
            }
        }
    } // 액티비티로 채팅 보내주기 ,  노티 아니면 문자 추가

    public void choice_notify(String data){
        Bundle bundle = new Bundle();
        bundle.putString("chat",data);

        if(mClientCallbacks!=null) {
            try {
                Log.d(TAG, "Send MSG_CHECK_ACTIVITY message to client");
                Message msg = Message.obtain(
                        null, RemoteService.MSG_CHECK_ACTIVITY);
                msg.obj = bundle;
                mClientCallbacks.send(msg);
            } catch (RemoteException e) {
                mClientCallbacks=null;
            }
        }
        else {
            Chat chat = gson.fromJson(data,Chat.class);
            sendNotification_outapp(chat.getRoom_idx(), "TORON",chat.getNickname(), chat.getMsg(),chat.getChat_idx(), chat.getChat_mode());
        }
    }

    public void create_notify(Chat chat){
        sendNotification_inapp(chat.getRoom_idx(),"TORON",chat.getNickname(), chat.getMsg(),chat.getChat_idx(), chat.getChat_mode());
    }

    public void request_chat_list(Integer room_idx){
        System_data system_data = new System_data("get_chat_list",room_idx.toString(),null);
        Request request = new Request("SYSTEM",gson.toJson(system_data));
        SendToServerThread sendToServerThread = new SendToServerThread(socket,gson.toJson(request));
        sendToServerThread.start();
        Log.d(TAG,"request_chat_list");
    }

    public void request_send_msg(Message message){
        Bundle bundle = (Bundle) message.obj;
        String msg = bundle.getString("msg");
        String room_idx = bundle.getString("room_idx");
        String user_idx = bundle.getString("user_idx");
        String datetime = bundle.getString("datetime");
        String side = bundle.getString("side");
        String tag_user_idx = bundle.getString("tag_user_idx");

        Chat chat = new Chat("msg","",room_idx,msg,user_idx,datetime,side,"",tag_user_idx,""); // 닉네임이랑 chat_idx 는 서버에서 넣어준다. 왜 이딴 식으로 짰는지 참..
        Request request = new Request("CHAT",gson.toJson(chat));
        SendToServerThread sendToServerThread = new SendToServerThread(socket,gson.toJson(request));
        sendToServerThread.start();
        Log.d(TAG,"request_send_msg");
    }

    public void request_send_img(Message message){
        Bundle bundle = (Bundle) message.obj;
        String msg = bundle.getString("msg");
        String room_idx = bundle.getString("room_idx");
        String user_idx = bundle.getString("user_idx");
        String datetime = bundle.getString("datetime");
        String side = bundle.getString("side");
        String tag_user_idx = bundle.getString("tag_user_idx");
        String img_href = bundle.getString("img_href");

        Chat chat = new Chat("img","",room_idx,msg,user_idx,datetime,side,"",tag_user_idx,img_href); // 닉네임이랑 chat_idx 는 서버에서 넣어준다. 왜 이딴 식으로 짰는지 참..
        Request request = new Request("CHAT",gson.toJson(chat));
        SendToServerThread sendToServerThread = new SendToServerThread(socket,gson.toJson(request));
        sendToServerThread.start();
        Log.d(TAG,"request_send_chat_img");
    }

    void createNotification(String channelId, int id, String title,String nickname, String text){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)          // Head Up Display를 위해 PRIORITY_HIGH 설정
                .setSmallIcon(R.drawable.ic_launcher_foreground)        // 알림시 보여지는 아이콘. 반드시 필요
                .setContentTitle(title)
                .setContentText(nickname + ": " + text);
                //.setTimeoutAfter(1000)    // 지정한 시간 이후 알림 삭제
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(text))          // 한줄 이상의 텍스트를 모두 보여주고 싶을때 사용

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

    private void sendNotification_inapp(String room_idx, String title,String nickname, String text,String tag_chat_idx,String mode) { //todo 나중에 수정해라
        showToast(this.getApplication(),"inapp");
        Intent resultIntent = new Intent(this, Debate_room.class);
        resultIntent.putExtra("room_idx",room_idx);
        resultIntent.putExtra("tag_chat_idx",tag_chat_idx);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(mode.equals("img")) text = "사진";
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(Debate_room.class);
//        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(getBaseContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//
//
//        resultIntent.putExtra("room_idx",room_idx);
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(Debate_room.class);
//        stackBuilder.addNextIntent(resultIntent);
//
//        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

//        Intent intent = new Intent(this, Debate_room.class); // 메인 액티비티로 가는 인텐트
//        intent.putExtra("room_idx",room_idx);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //해당 인텐트를 FLAG_ACTIVITY_CLEAR_TOP 로 설정
           //FLAG_ACTIVITY_CLEAR_TOP :호출하는 액티비티가 스택에 존재할 경우에, 해당 액티비티를 최상위로 올리면서, 그 위에 존재하던 액티비티들은 모두 삭제를 하는 플래그

//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT); // 일회용 pending_intent 생성

        String channelId = "fcm_default_channel";//getString(R.string.default_notification_channel_id);
        NotificationCompat.Builder notificationBuilder = // 노티피케이션 설정
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)//drawable.splash)
                        .setContentTitle(title)
                        .setContentText(nickname + ": " + text)
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 노티피케이션 설정

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,"Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build()); // 노티피케이션
    } // 앱이 켜졌을때 울리는 notification
    private void sendNotification_outapp(String room_idx, String title,String nickname, String text,String tag_chat_idx,String mode) { //todo 나중에 수정해라

        showToast(this.getApplication(),"outapp");

        Intent resultIntent = new Intent(this, Debate_room.class);
        resultIntent.putExtra("room_idx",room_idx);
        resultIntent.putExtra("tag_chat_idx",tag_chat_idx);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Debate_room.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        if(mode.equals("img")) text = "사진";

//        Intent intent = new Intent(this, Debate_room.class); // 메인 액티비티로 가는 인텐트
//        intent.putExtra("room_idx",room_idx);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //해당 인텐트를 FLAG_ACTIVITY_CLEAR_TOP 로 설정
           //FLAG_ACTIVITY_CLEAR_TOP :호출하는 액티비티가 스택에 존재할 경우에, 해당 액티비티를 최상위로 올리면서, 그 위에 존재하던 액티비티들은 모두 삭제를 하는 플래그

//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT); // 일회용 pending_intent 생성

        String channelId = "fcm_default_channel";//getString(R.string.default_notification_channel_id);
        NotificationCompat.Builder notificationBuilder = // 노티피케이션 설정
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)//drawable.splash)
                        .setContentTitle(title)
                        .setContentText(nickname + ": " + text)
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 노티피케이션 설정

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,"Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build()); // 노티피케이션
    } // 앱이 꺼졌을때 울리는 notification

    void createNotificationChannel(String channelId, String channelName, int importance){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, importance));
        }
    }

    void destroyNotification(int id){
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }

    class MessageThread extends Thread {
        Socket socket;
        DataInputStream dis;
        String msg = null;
        Boolean isRunning = true;
        String TAG = "!!!MessageThread";

        public MessageThread(Socket socket) {
            try {
                Log.d(TAG, socket.toString());
                this.socket = socket; // socket 생성
                InputStream is = socket.getInputStream();
                dis = new DataInputStream(is);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            int length;
            while (isRunning) {
                try {
                    // 서버로부터 데이터를 수신받는다.
                    length = dis.readInt();
                    byte[] data=new byte[length];
                    dis.readFully(data);
                    String msg=new String(data,"UTF-8");
                    Log.d(TAG,msg);
                    receive_msg(msg);
                    // 화면에 출력
                } catch (Exception e) {
                    isRunning = false;
                    System.out.println("서버와 접속이 끊어졌습니다. 다시 접속하세요." + e.getMessage());
                    System.exit(0);
                }
                msg = null;
            }
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public void receive_msg(String msg){
            Response response;
            if(msg!=null){
                response = gson.fromJson(msg,Response.class);
                Log.d(TAG,"response " + response.getData());
                switch(response.getType()){
                    case "show_roomList":
                        show_roomList(response.getData());
                        break;
                    case "chat_list":
                        get_chat_list(response.getData());
                        break;
                    case "chat":
                        choice_notify(response.getData());
                    default:
                        break;
                }
            }
        }// 서버 쪽 내용 분석
    }
}