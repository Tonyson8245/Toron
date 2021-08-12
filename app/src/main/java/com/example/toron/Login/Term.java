package com.example.toron.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Userdata_response;
import com.example.toron.Retrofit.Interface.UserdataInterface;

import retrofit2.Call;
import retrofit2.Callback;

public class Term extends AppCompatActivity {

    TextView Tv_read_service,Tv_read_info;
    Button btn_join,btn_back;
    CheckBox checkBox_service,checkBox_info,checkBox_all;
    private PopupWindow mPopupWindow ;

    Boolean check_service=false,check_info=false,check_all=false;
    String user_id,user_password,user_nickname,user_phonenum,user_birthday;

    Join join_activity = (Join) Join.Join_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        Intent getdata = getIntent();
        user_id = getdata.getStringExtra("user_id");
        user_password = getdata.getStringExtra("user_password");
        user_nickname = getdata.getStringExtra("user_nickname");
        user_phonenum = getdata.getStringExtra("user_phonenum");
        user_birthday = getdata.getStringExtra("user_birthday");

        Tv_read_service = findViewById(R.id.read_service);
        Tv_read_info = findViewById(R.id.read_info);

        checkBox_service = findViewById(R.id.checkbox_service);
        checkBox_info = findViewById(R.id.checkbox_info);
        checkBox_all = findViewById(R.id.checkbox_all);

        btn_join = findViewById(R.id.btn_join);
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Tv_read_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = getLayoutInflater().inflate(R.layout.activity_term_service, null);
                mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //popupView 에서 (LinearLayout 을 사용) 레이아웃이 둘러싸고 있는 컨텐츠의 크기 만큼 팝업 크기를 지정

                mPopupWindow.setFocusable(true);
                // 외부 영역 선택히 PopUp 종료

                mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                Button ok = (Button) popupView.findViewById(R.id.Ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                    }
                });
            }
        });
        Tv_read_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = getLayoutInflater().inflate(R.layout.activity_term_info, null);
                mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //popupView 에서 (LinearLayout 을 사용) 레이아웃이 둘러싸고 있는 컨텐츠의 크기 만큼 팝업 크기를 지정

                mPopupWindow.setFocusable(true);
                // 외부 영역 선택히 PopUp 종료

                mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                Button ok = (Button) popupView.findViewById(R.id.Ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                    }
                });
            }
        });

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_service = checkBox_service.isChecked();
                check_info = checkBox_info.isChecked();

                if(check_all||(check_info && check_service)) {
                    Toast.makeText(getApplicationContext(),"회원가입 성공!",Toast.LENGTH_SHORT).show();
                    join(user_id,user_password,user_nickname,user_phonenum,user_birthday);
                }
                else Toast.makeText(getApplicationContext(),"모든 약관에 동의해주세요",Toast.LENGTH_SHORT).show();
            }
        });

        checkBox_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox_all.isChecked()){
                    checkBox_service.setChecked(true);
                    checkBox_info.setChecked(true);
                }
                else{
                    checkBox_service.setChecked(false);
                    checkBox_info.setChecked(false);
                }
            }
        });
        checkBox_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkBox_service.isChecked()){
                    checkBox_all.setChecked(false);
                }
                else if(checkBox_service.isChecked() &&checkBox_info.isChecked()) checkBox_all.setChecked(true);
            }
        });
        checkBox_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkBox_info.isChecked()){
                    checkBox_all.setChecked(false);
                }
                else if(checkBox_service.isChecked() &&checkBox_info.isChecked()) checkBox_all.setChecked(true);
            }
        });
    }


    public void join(String id, String pw,String nickname, String phonenum,String birthday){
        UserdataInterface userdataInterface = ApiClient.getApiClient().create(UserdataInterface.class);
        Call<Userdata_response> call = userdataInterface.insertUserdata(id,birthday,pw,nickname,phonenum);
        call.enqueue(new Callback<Userdata_response>() {
            @Override
            public void onResponse(Call<Userdata_response> call, retrofit2.Response<Userdata_response> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    if(response.body().getResult().equals("success")) {
                        Intent intent = new Intent(Term.this,Login.class);
                        startActivity(intent);
                        join_activity.finish();
                        finish();
                    }
                    else Log.e("!!!TERM: ","php 실패");
                }
                else Log.e("!!!TERM: ","실패");
            }
            @Override
            public void onFailure(Call<Userdata_response> call, Throwable t) {
                Log.e("!!!TERM: t:",t.getMessage());
            }
        });
    }
}