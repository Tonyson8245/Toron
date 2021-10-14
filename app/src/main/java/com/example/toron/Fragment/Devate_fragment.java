package com.example.toron.Fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toron.Adapter.RoomRecyclerAdapter;
import com.example.toron.Debate.Debate_make;
import com.example.toron.R;
import com.example.toron.Service.Class.Room_data;
import com.example.toron.Service.RemoteService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Devate_fragment extends Fragment {

    private Messenger mServiceCallback = null;
    // 서비스로부터 전달 받는 객체 바인딩 시 제공하는 IBinder 로 만들어진 Messenger 객체
    private Messenger mClientCallback = new Messenger(new CallbackHandler());
    // 액티비티 <-> 서비스 : 서비스에서 액티비티로 결과를 리턴을 받을 때 쓰임 ; HTTP 통신과 유사한 개념


    Button btn_insert_room,btn_search;

    private Activity Devate_Activity;
    private RecyclerView recyclerView;
    private RoomRecyclerAdapter roomRecyclerAdapter;
    String TAG = "Devate_fragment";

    List<Room_data> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_devate_fragment, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.news_recyclerView);
        btn_search = rootView.findViewById(R.id.btn_search);

        recyclerView.setHasFixedSize(true);
        roomRecyclerAdapter = new RoomRecyclerAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(roomRecyclerAdapter);

        setHasOptionsMenu(true);

        btn_insert_room = rootView.findViewById(R.id.btn_add);
        btn_search = rootView.findViewById(R.id.btn_search);

        btn_insert_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent make_room = new Intent(getActivity(), Debate_make.class);
                startActivity(make_room);
            }
        });


        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(-1)) {
                    get_roomList();
                }
            }
        });

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }




    @Override
    public void onResume() {
        super.onResume();
        Log.d("mClient", "onResume:Debate_fragement");
        Intent intent = new Intent(Devate_Activity.getApplicationContext(), RemoteService.class);
        Devate_Activity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
//
//        Intent intent = new Intent(getActivity(), RemoteService.class);// 바인드를 위한 intent
//        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE); // 여기서 액티비티와 서비스를 바인드 ㅎ해줌

        get_roomList(); //  방 불러오기 서비스에 요청
    }



    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        if(context instanceof Activity){
            Devate_Activity = (Activity) context;
            Log.d(TAG,Devate_Activity.toString());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("mClient", "onPause:Debate_fragment");
        disconnect_service();
        getActivity().unbindService(mConnection);
    }

    private ServiceConnection mConnection = new ServiceConnection()
    {
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

            get_roomList();
        } // 액티비티의 messenger 객체를 서비스에 전달

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceCallback = null;
        }
    };

    private class CallbackHandler extends Handler
    {
        Gson gson = new Gson();

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RemoteService.MSG_SHOW_ROOM_LIST:
                    Bundle bundle = (Bundle) msg.obj;;
                    Type type = new TypeToken<List<Room_data>>() {}.getType();

                    List<Room_data> temp_list = gson.fromJson(bundle.getString("list"), type);
                    list = temp_list;
                    roomRecyclerAdapter.set_RoomList(list);
                    roomRecyclerAdapter.notifyDataSetChanged();
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
        bundle.putString("name","Devate_fragment");
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



    private void get_roomList(){
        if (mServiceCallback != null) {
            // request 'add value' to service
            Message msg = Message.obtain(
                    null, RemoteService.MSG_SHOW_ROOM_LIST);
            msg.arg1 = 10;
            try {
                mServiceCallback.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Send MSG_SHOW_ROOM_LIST message to Service");
        }
    }
    private void disconnect_service(){
        if (mServiceCallback != null) {
            // request 'add value' to service
            Message msg = Message.obtain(
                    null, RemoteService.MSG_CLIENT_DISCONNECT);
            try {
                mServiceCallback.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Send MSG_CLIENT_DISCONNECT message to Service");
        }
    }
}