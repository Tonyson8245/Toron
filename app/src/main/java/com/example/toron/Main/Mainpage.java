package com.example.toron.Main;

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
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.toron.Debate.Debate_SelectSide;
import com.example.toron.Debate.Debate_room;
import com.example.toron.Fragment.Devate_fragment;
import com.example.toron.Fragment.Mypage_fragment;
import com.example.toron.Fragment.News_fragment;
import com.example.toron.Fragment.Vote_fragment;
import com.example.toron.News.News_detail;
import com.example.toron.R;
import com.example.toron.Service.RemoteService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.security.auth.callback.CallbackHandler;

public class Mainpage extends AppCompatActivity {

    private BottomNavigationView mBottomNV;
    private Messenger mServiceCallback = null;
    // 서비스로부터 전달 받는 객체 바인딩 시 제공하는 IBinder 로 만들어진 Messenger 객체
    private Messenger mClientCallback = new Messenger(new CallbackHandler());
    // 액티비티 <-> 서비스 : 서비스에서 액티비티로 결과를 리턴을 받을 때 쓰임 ; HTTP 통신과 유사한 개념

    String TAG = "Mainpage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator_test);

        mBottomNV = findViewById(R.id.nav_view);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                BottomNavigate(menuItem.getItemId());
                return true;
            }
        });
        mBottomNV.setSelectedItemId(R.id.navigation_news);


        Log.d(TAG, "Trying to connect to service");
        Intent intent = new Intent(getApplicationContext(), RemoteService.class); // 바인드를 위한 intent
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE); // 여기서 액티비티와 서비스를 바인드 ㅎ해줌
    }
    private void BottomNavigate(int id) {  //BottomNavigation 페이지 변경
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            if (id == R.id.navigation_news) {
                fragment = new News_fragment();

            } else if (id == R.id.navigation_devate){

                fragment = new Devate_fragment();
            }else if (id == R.id.navigation_vote) {
                fragment = new Vote_fragment();
            }else {
                fragment = new Mypage_fragment();
            }

            fragmentTransaction.add(R.id.content_layout, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();


    }

    public void click_news_detail(String href,String title,String img,String writing,String datetime,String news_idx) {
        Intent news_detail = new Intent(this, News_detail.class);
        news_detail.putExtra("href",href);
        news_detail.putExtra("title",title);
        news_detail.putExtra("img",img);
        news_detail.putExtra("writing",writing);
        news_detail.putExtra("datetime",datetime);
        news_detail.putExtra("news_idx",news_idx);
        startActivity(news_detail);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) { //해당 서비스의 IBinder 라는 객체 생성
            Log.d(TAG, "onServiceConnected");
            mServiceCallback = new Messenger(service); // IBinder 를 통해 Messenger 객체 생성 가능
            // mServiceCallback : 원하는 서비스의 Messegner 객체

            // connect to service
            Message connect_msg = Message.obtain(null, RemoteService.MSG_CLIENT_CONNECT);
            connect_msg.replyTo = mClientCallback;
            try {
                mServiceCallback.send(connect_msg);
                Log.d(TAG, "Send MSG_CLIENT_CONNECT message to Service");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } // 액티비티의 messenger 객체를 서비스에 전달

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            mServiceCallback = null;
        }
    }; // 앱을 실행하면 바로 서비스 실행

    private class CallbackHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

            }
        }
    }  // 변화가 있을때 사용될 핸들러

    public void enter_room(String room_idx,String room_status,String room_subject,String room_description) {
        if(room_status.equals("1")){
            Intent room = new Intent(this, Debate_room.class);
            room.putExtra("room_subject",room_subject);
            room.putExtra("room_description",room_description);
            room.putExtra("room_idx",room_idx);
            startActivity(room);
        }// 이미 참석
        else{
            Intent select_side = new Intent(this, Debate_SelectSide.class);
            select_side.putExtra("room_idx",room_idx);
            select_side.putExtra("room_subject",room_subject);
            select_side.putExtra("room_description",room_description);
            startActivity(select_side);
        }// 노참석
    }
}