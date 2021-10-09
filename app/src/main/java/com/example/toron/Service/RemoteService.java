package com.example.toron.Service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.toron.Service.Class.Chat;
import com.example.toron.Service.Class.Request;
import com.example.toron.Service.Class.Response;
import com.example.toron.Service.Class.Room_data;
import com.example.toron.Service.Class.System_data;
import com.example.toron.Service.Socket.ConnectionThread;
import com.example.toron.Service.Socket.SendToServerThread;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.DataInputStream;
import java.io.InputStream;
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
    String user_idx;


    private ArrayList<Messenger> mClientCallbacks = new ArrayList<Messenger>();
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
                    mClientCallbacks.add(msg.replyTo);
                    break;
                case MSG_CLIENT_DISCONNECT:
                    Log.d(TAG, "Received MSG_CLIENT_DISCONNECT message from client");
                    mClientCallbacks.remove(msg.replyTo);
                    break;
                case MSG_ADD_VALUE:
                    Log.d(TAG, "Received message from client: MSG_ADD_VALUE");
                    mValue += msg.arg1;
                    for (int i = mClientCallbacks.size() - 1; i >= 0; i--) {
                        try{
                            Log.d(TAG, "Send MSG_ADDED_VALUE message to client");
                            Message added_msg = Message.obtain(
                                    null, RemoteService.MSG_ADDED_VALUE);
                            added_msg.arg1 = mValue;
                            mClientCallbacks.get(i).send(added_msg);
                        }
                        catch( RemoteException e){
                            mClientCallbacks.remove( i );
                        }
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

        for (int i = mClientCallbacks.size() - 1; i >= 0; i--) {
            try{
                Log.d(TAG, "Send MSG_SHOW_ROOM_LIST message to client");
                Message msg = Message.obtain(
                        null, RemoteService.MSG_SHOW_ROOM_LIST);
                msg.obj = bundle;
                mClientCallbacks.get(i).send(msg);
            }
            catch( RemoteException e){
                mClientCallbacks.remove( i );
            }
        }
    } // 방 정보 가져온거 액티비티로 보내기

    public void get_chat_list(String data){
        Bundle bundle = new Bundle();
        bundle.putString("list",data);

        for (int i = mClientCallbacks.size() - 1; i >= 0; i--) {
            try{
                Log.d(TAG, "Send MSG_GET_CHAT_LIST message to client");
                Message msg = Message.obtain(
                        null, RemoteService.MSG_GET_CHAT_LIST);
                msg.obj = bundle;
                mClientCallbacks.get(i).send(msg);
            }
            catch( RemoteException e){
                mClientCallbacks.remove( i );
            }
        }
    }

    public void get_chat(String data){
        Bundle bundle = new Bundle();
        bundle.putString("chat",data);

        for (int i = mClientCallbacks.size() - 1; i >= 0; i--) {
            try{
                Log.d(TAG, "Send MSG_GET_CHAT message to client");
                Message msg = Message.obtain(
                        null, RemoteService.MSG_GET_CHAT);
                msg.obj = bundle;
                mClientCallbacks.get(i).send(msg);
            }
            catch( RemoteException e){
                mClientCallbacks.remove( i );
            }
        }
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
        String side = bundle.getString("side");

        Chat chat = new Chat(room_idx,msg,user_idx,null,side,"");
        Request request = new Request("CHAT",gson.toJson(chat));
        SendToServerThread sendToServerThread = new SendToServerThread(socket,gson.toJson(request));
        sendToServerThread.start();
        Log.d(TAG,"request_send_msg");
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
            while (isRunning) {
                try {
                    // 서버로부터 데이터를 수신받는다.
                    msg = dis.readUTF();
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
            msg = messageThread.getMsg();
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
                        get_chat(response.getData());
                    default:
                        break;
                }
            }
        }// 서버 쪽 내용 분석
    }
}