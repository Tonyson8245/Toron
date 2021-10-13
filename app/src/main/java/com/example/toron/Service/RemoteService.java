package com.example.toron.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
import java.util.ArrayList;
import java.util.List;

public class RemoteService extends Service {

    MessageThread messageThread;
    Socket socket;
    Boolean isRunning = true;
    Gson gson = new Gson();

    private final String TAG = "RemoteService";
    public static final int MSG_CLIENT_CONNECT = 1;
    public static final int MSG_CLIENT_DISCONNECT = 2;
    public static final int MSG_ADD_VALUE = 3;
    public static final int MSG_ADDED_VALUE = 4;
    public static final int MSG_SHOW_ROOM_LIST = 5;
    public static final int MSG_GET_CHAT_LIST = 6;
    public static final int MSG_SEND_MSG = 7;
    public static final int MSG_GET_CHAT = 8;
    public static final int MSG_CHECK_ACTIVITY = 9;
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
        start_connect(user_idx);
        createNotificationChannel("DEFAULT", "default channel", NotificationManager.IMPORTANCE_HIGH);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
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
                    mClientCallbacks = null;
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
        Bundle data = (Bundle) message.obj;

        Bundle bundle =  new Bundle();
        bundle.putString("chat",data.getString("chat"));
        Chat chat = gson.fromJson(data.getString("chat"),Chat.class);

        Log.d(TAG,"status " + data.getString("name") + " size:" + mClientCallbacks.toString());

        if(data.getString("name").equals("room") && data.getString("room_idx").equals(chat.getRoom_idx())){
            try {
                Log.d(TAG, "Send MSG_GET_CHAT message to client");
                Message msg = Message.obtain(
                        null, RemoteService.MSG_GET_CHAT);
                msg.obj = bundle;
                mClientCallbacks.send(msg);
            } catch (RemoteException e) {
                mClientCallbacks= null;
            }
        }
        else{
            if(chat.getTag_user_idx()!=null) {
                if (chat.getTag_user_idx().equals(user_idx)) create_notify(chat);
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
            createNotification("DEFAULT", 1, "TORON",chat.getNickname(), chat.getMsg());
        }
    }

    public void create_notify(Chat chat){
        createNotification("DEFAULT", 1, "TORON",chat.getNickname(), chat.getMsg());
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

        Chat chat = new Chat("",room_idx,msg,user_idx,datetime,side,"",tag_user_idx); // 닉네임이랑 chat_idx 는 서버에서 넣어준다. 왜 이딴 식으로 짰는지 참..
        Request request = new Request("CHAT",gson.toJson(chat));
        SendToServerThread sendToServerThread = new SendToServerThread(socket,gson.toJson(request));
        sendToServerThread.start();
        Log.d(TAG,"request_send_msg");
    }

    void createNotification(String channelId, int id, String title,String nickname, String text){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)          // Head Up Display를 위해 PRIORITY_HIGH 설정
                .setSmallIcon(R.drawable.ic_launcher_foreground)        // 알림시 보여지는 아이콘. 반드시 필요
                .setContentTitle(title)
                .setContentText(nickname + ": " + text)
                //.setTimeoutAfter(1000)    // 지정한 시간 이후 알림 삭제
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(text))          // 한줄 이상의 텍스트를 모두 보여주고 싶을때 사용
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);  // 알림시 효과음, 진동 여부

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

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
        String TAG = "MessageThread";

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
                    Log.d("CHECK",msg);
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
                Log.d(TAG,"response " + msg);
                response = gson.fromJson(msg,Response.class);
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