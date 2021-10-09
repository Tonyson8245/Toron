package com.example.toron.Debate;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.toron.Fragment.Devate_fragment;
import com.example.toron.Main.Mainpage;
import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Detail_article;
import com.example.toron.Retrofit.Class.Room_idx;
import com.example.toron.Retrofit.Class.Yesno;
import com.example.toron.Retrofit.Interface.DebateInterface;
import com.example.toron.Retrofit.Interface.NewsInterface;
import com.example.toron.Service.Class.Room_data;
import com.example.toron.Service.RemoteService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import javax.security.auth.callback.CallbackHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Debate_make extends AppCompatActivity {

    EditText Ev_subject,Ev_description;
    RadioButton btn_politics,btn_economy,btn_society,btn_etc,btn_overseas;
    Button btn_next;
    RadioGroup radio_group;
    String subject,description,category,TAG ="Debate_make",user_idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debate_make);

        Ev_subject = findViewById(R.id.Ev_subject);
        Ev_description = findViewById(R.id.Ev_description);
        btn_politics = findViewById(R.id.btn_politic);
        btn_economy = findViewById(R.id.btn_economy);
        btn_society = findViewById(R.id.btn_society);
        btn_etc = findViewById(R.id.btn_etc);
        btn_overseas = findViewById(R.id.btn_overseas);
        btn_next = findViewById(R.id.btn_next);
        radio_group = findViewById(R.id.radio_group);

        SharedPreferences sharedPreferences;
        sharedPreferences = this.getSharedPreferences("user_data",0);
        user_idx = sharedPreferences.getString("user_idx",null);


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject = Ev_subject.getText().toString();
                description = Ev_description.getText().toString();

                insert_room(subject,description,category);
            }
        });

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.btn_politic: category = "politic";
                        break;
                    case R.id.btn_economy: category = "economy";
                        break;
                    case R.id.btn_society: category = "society";
                        break;
                    case R.id.btn_etc: category = "etc";
                        break;
                    case R.id.btn_overseas: category = "overseas";
                        break;
                  default:
                        break;
                }
            }
        });
    }
    private void insert_room(String sub,String des, String cat){
        DebateInterface debateInterface = ApiClient.getApiClient().create(DebateInterface.class);
        Call<Room_idx> call = debateInterface.insert_room_list(sub,des,cat,user_idx);
        call.enqueue(new Callback<Room_idx>() {
            @Override
            public void onResponse(Call<Room_idx> call, Response<Room_idx> response) {
                if(response.body()!=null && response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "토론방이 생성되었습니다." , Toast.LENGTH_SHORT).show();
                    move_to_debate(response.body().getRoom_idx());
                }
            }

            @Override
            public void onFailure(Call<Room_idx> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"토론방이 생성에 실패하였습니다." ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void move_to_debate(String idx){

        //데이터 담아서 팝업(액티비티) 호출
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        // 제목 설정
        ad.setTitle("토론방으로 바로 이동하시겠습니까?").setMessage("예를 누르시면 찬반 선택화면으로 이동합니다.");
        // 확인 버튼 설정
        ad.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();     //닫기
                // Event
            }
        });

        // 취소 버튼 설정
        ad.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent select = new Intent(Debate_make.this,Debate_SelectSide.class);
                select.putExtra("room_idx",idx);
                startActivity(select);
                finish();
                dialog.dismiss();     //닫기

                // Event
            }
        });
        // 창 띄우기
        ad.show();
    }
}

