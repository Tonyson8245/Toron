package com.example.toron.Main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.RemoteException;
import android.text.TextUtils;
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
import com.example.toron.Vote.Vote_history;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.security.auth.callback.CallbackHandler;

public class Mainpage extends AppCompatActivity {
    private Intent serviceIntent;
    private BottomNavigationView mBottomNV;
    private Messenger mServiceCallback = null;
    // 서비스로부터 전달 받는 객체 바인딩 시 제공하는 IBinder 로 만들어진 Messenger 객체
    private Messenger mClientCallback = new Messenger(new CallbackHandler());
    // 액티비티 <-> 서비스 : 서비스에서 액티비티로 결과를 리턴을 받을 때 쓰임 ; HTTP 통신과 유사한 개념

    String TAG = "Mainpage";
    String page = "news";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent getData = getIntent();

        setContentView(R.layout.activity_navigator_test);

        mBottomNV = findViewById(R.id.nav_view);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                BottomNavigate(menuItem.getItemId());
                return true;
            }
        });

        if(!TextUtils.isEmpty(getData.getStringExtra("page"))){
            page = getData.getStringExtra("page");
            Log.d(TAG,"tag" + page);
            mBottomNV.setSelectedItemId(R.id.navigation_news);
        }// tag_메세지 때문에 들어옴
        else mBottomNV.setSelectedItemId(R.id.navigation_devate);

        ///////immortal service

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE); //안드로이드 절전 모드 설정을 위한 Power_Manager

        boolean isWhiteListing = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            isWhiteListing = pm.isIgnoringBatteryOptimizations(getApplicationContext().getPackageName()); // SDK 버전 M 이상 부터는 Power_Manager 를 통해 절전모드 해제 해줘야한다.
        }
        if (!isWhiteListing) {
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS); // 절전모드 설정
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(intent);
        }

        if (RemoteService.serviceIntent==null) { // 기존에 서비스가 없다면
            serviceIntent = new Intent(this, RemoteService.class); // 서비스를 생성
            startService(serviceIntent); // 서비스 실행
        } else {  //기존 서비스가 있다면
            serviceIntent = RemoteService.serviceIntent;//getInstance().getApplication(); // 서비스의 인텐트를 가져와서 메인 서비스에 다시 설정
//            Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show(); // 이미 돌고 있다는 토스트 띄운다.
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mClient","onResume:Mainpage");
        Intent intent = new Intent(getApplicationContext(), RemoteService.class); // 바인드를 위한 intent
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE); // 여기서 액티비티와 서비스를 바인드 ㅎ해줌
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
        disconnect_service();
        Log.d("mClient","onPause:Mainpage");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceIntent!=null) { // 서비스가 있을 경우에는
            stopService(serviceIntent); // 서비스를 종료하고
            serviceIntent = null; // 서비스를 null 상태로 만들어둔다.
        }
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

                case RemoteService.MSG_CHECK_ACTIVITY:
                    sendBackName(msg);
                    break;
            }
        }
    }
    private void sendBackName(Message message){
        Bundle data = (Bundle) message.obj;

        Bundle bundle = new Bundle();
        bundle.putString("name","Mainpage");
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

    public void enter_room(String room_idx,String room_status,String room_subject,String room_description) {
        if(room_status!=null){
            Intent room = new Intent(this, Debate_room.class);
            room.putExtra("room_subject",room_subject);
            room.putExtra("room_description",room_description);
            room.putExtra("room_idx",room_idx);
            room.putExtra("side",room_status);
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

    public void moveToHistory(String vote_idx) {
//        Toast.makeText(this,"토론 기록으로 이동합니다.",Toast.LENGTH_SHORT).show();
        Intent history = new Intent(Mainpage.this, Vote_history.class);
        history.putExtra("vote_idx",vote_idx);
        startActivity(history);
    }

}