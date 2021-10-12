package com.example.toron.Debate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toron.Adapter.ChatAdapter;
import com.example.toron.Fragment.Devate_fragment;
import com.example.toron.R;
import com.example.toron.Service.Class.Chat;
import com.example.toron.Service.Class.Room_data;
import com.example.toron.Service.Class.Status_Foreground;
import com.example.toron.Service.RemoteService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.time.LocalDateTime.now;

public class Debate_room extends AppCompatActivity {

    private RecyclerView chat_RecyclerView;
    private ChatAdapter chatAdapter;
    private Messenger mServiceCallback = null;
    // 서비스로부터 전달 받는 객체 바인딩 시 제공하는 IBinder 로 만들어진 Messenger 객체
    private Messenger mClientCallback = new Messenger(new CallbackHandler());
    // 액티비티 <-> 서비스 : 서비스에서 액티비티로 결과를 리턴을 받을 때 쓰임 ; HTTP 통신과 유사한 개념

    ArrayList<Chat> chat_list = new ArrayList<>();
    LinearLayout tag_layout;
    TextView Tv_subject,Tv_description,Tv_tag_nickname,Tv_tag_content;
    EditText Ev_message_content;
    Button btn_send,btn_back,btn_tag_close;
    String TAG = "Debate_room",room_idx,side;
    Date time = new Date();
    SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debate_room);

        Intent getData = getIntent();
        side = getData.getStringExtra("side");

        Tv_subject = findViewById(R.id.Tv_subject);
        Tv_tag_content = findViewById(R.id.Tv_tag_content);
        Tv_tag_nickname = findViewById(R.id.Tv_tag_nickname);
        Tv_description = findViewById(R.id.Tv_description);
        btn_send = findViewById(R.id.insert_message);
        Ev_message_content = findViewById(R.id.Ev_message_content);
        btn_back = findViewById(R.id.btn_back);
        tag_layout = findViewById(R.id.tag_layout);
        btn_tag_close = findViewById(R.id.btn_tag_close);

        Tv_subject.setText(getData.getStringExtra("room_subject"));
        Tv_description.setText(getData.getStringExtra("room_description"));
        room_idx = getData.getStringExtra("room_idx");

        chat_RecyclerView = findViewById(R.id.dabate_recyclerview);
        chat_RecyclerView.setHasFixedSize(true);
        chatAdapter = new ChatAdapter(chat_list, this);
        chat_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chat_RecyclerView.setAdapter(chatAdapter);
        // 채팅 리사이클러뷰 세팅

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_msg(Ev_message_content.getText().toString());
                Ev_message_content.setText("");
            }
        });
        btn_tag_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag_layout.setVisibility(View.GONE);
            }
        });
    }

    private ServiceConnection mConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) { //해당 서비스의 IBinder 라는 객체 생성
            Log.d(TAG, "onServiceConnected");
            mServiceCallback = new Messenger(service); // IBinder 를 통해 Messenger 객체 생성 가능
            // mServiceCallback : 원하는 서비스의 Messegner 객체

            // connect to service
            Message connect_msg = Message.obtain( null, RemoteService.MSG_CLIENT_CONNECT);
            connect_msg.replyTo = mClientCallback;
            try {
                mServiceCallback.send(connect_msg);
                Log.d(TAG, "Send MSG_CLIENT_CONNECT message to Service");
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            request_chat_list(Integer.valueOf(room_idx));
        } // 액티비티의 messenger 객체를 서비스에 전달

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceCallback = null;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mClient","onResume:Debate_room");
        Intent intent = new Intent(this, RemoteService.class); // 바인드를 위한 intent
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE); // 여기서 액티비티와 서비스를 바인드 해줌

        request_chat_list(Integer.valueOf(room_idx));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("mClient", "onPause:Debate_room");
        unbindService(mConnection);
    }


    private class CallbackHandler extends Handler  {
        Gson gson = new Gson();
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RemoteService.MSG_GET_CHAT_LIST:
                    Bundle bundle = (Bundle) msg.obj;
                    Type type = new TypeToken<ArrayList<Chat>>() {}.getType();

                    Log.d(TAG,bundle.getString("list"));
                    ArrayList<Chat> temp_list = gson.fromJson(bundle.getString("list"), type);
                    chat_list = temp_list;
                    chatAdapter.set_chatlist(chat_list);
                    chatAdapter.notifyDataSetChanged();
                    toBottom();
                    break;
                case RemoteService.MSG_GET_CHAT:
                    Bundle bundle2 = (Bundle) msg.obj;;
                    String temp_msg = bundle2.getString("chat");

                    Log.d(TAG,"Chat" + bundle2.getString("chat"));
                    Chat chat = gson.fromJson(temp_msg,Chat.class);
                    if(chat.getRoom_idx().equals(room_idx)) {
                        chat_list.add(new Chat(chat.getRoom_idx(), chat.getMsg(), chat.getUser_idx(), chat.getDatetime(), chat.getSide(), chat.getNickname()));
                        chatAdapter.notifyItemChanged(chat_list.size() - 1);
                        toBottom();
                    }
                    break;
                case RemoteService.MSG_CHECK_ACTIVITY:
                    sendBackName(msg);
                    break;
            }
        }
    }
    private void sendBackName(Message message){
        Bundle data = (Bundle) message.obj;

        Bundle bundle = new Bundle();
        bundle.putString("name","room");
        bundle.putString("room_idx",room_idx);
        bundle.putString("chat",data.getString("chat"));

        if (mServiceCallback != null) {
            // request 'add value' to service
            Message msg = Message.obtain(
                    null, RemoteService.MSG_CHECK_ACTIVITY);
            msg.obj = bundle;
            try {
                mServiceCallback.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Send MSG_CHECK_ACTIVITY message to Service " + bundle.getString("name"));

        }
    }

    private void request_chat_list(Integer room_idx){
        Log.d(TAG,"request_chat_list" + room_idx);
        if (mServiceCallback != null) {
            // request 'add value' to service
            Message msg = Message.obtain(
                    null, RemoteService.MSG_GET_CHAT_LIST);
            msg.arg1 = room_idx;
            try {
                mServiceCallback.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Send MSG_GET_CHAT_LIST message to Service");
        }
    }

    private void toBottom(){
        chat_RecyclerView.post(new Runnable() {
            @Override
            public void run() {
                chat_RecyclerView.scrollToPosition(chat_RecyclerView.getAdapter().getItemCount() - 1);
            }
        });
    }

    private void send_msg(String msg){
        SharedPreferences sharedPreferences;
        sharedPreferences = this.getSharedPreferences("user_data",0);
        String user_idx = sharedPreferences.getString("user_idx",null);
        String user_nickname = sharedPreferences.getString("user_nickname",null);

        String time_data = format1.format(time);
        Log.d(TAG,"time" + time_data);

        chat_list.add(new Chat(room_idx,msg,user_idx,time_data,side,user_nickname));
        chatAdapter.notifyItemChanged(chat_list.size()-1);
        //클라에 추가

        Bundle bundle = new Bundle();
        bundle.putString("msg",msg);
        bundle.putString("room_idx",room_idx);
        bundle.putString("user_idx",user_idx);
        bundle.putString("datetime",time_data);
        bundle.putString("side",side);

        if (mServiceCallback != null) {
            // request 'add value' to service
            Message message = Message.obtain(
                    null, RemoteService.MSG_SEND_MSG);
            message.obj = bundle;
            try {
                mServiceCallback.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        toBottom();
    }

    public void set_TagMode(String chat_idx,String nickname, String msg){
        tag_layout.setVisibility(View.VISIBLE);

        Tv_tag_nickname.setText(nickname);
        Tv_tag_content.setText(": "+msg);

        Log.d(TAG,chat_idx + " " + nickname + " " + msg);
    }
}