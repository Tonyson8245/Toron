package com.example.toron.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Userdata_response;
import com.example.toron.Retrofit.Interface.UserdataInterface;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Find_password extends AppCompatActivity {

    Button btn_back,btn_change;
    EditText Ev_password,Ev_verified_password;
    TextView Tv_password_memo;
    Boolean password_check = false;

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        Intent getdata = getIntent();
        user_id = getdata.getStringExtra("user_id");

        btn_back = findViewById(R.id.btn_back);
        btn_change =findViewById(R.id.btn_change);

        Ev_password  = findViewById(R.id.Ev_password);
        Ev_verified_password = findViewById(R.id.Ev_verified_password);

        Tv_password_memo = findViewById(R.id.Tv_password_memo);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password_change(user_id,Ev_password.getText().toString(),Ev_verified_password.getText().toString());
            }
        });

        Ev_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {
                //포커스가 주어졌을 때 동작
                if (!gainFocus) {
                    if (Ev_password.getText().length() > 0) {
                        if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$",Ev_password.getText().toString()))
                        {
                            memo_change(Tv_password_memo, "red", "숫자,영문,특수 문자를 조합해서 8자 이상 입력해주세요.");
                            password_check = false;

                        }
                        else{
                            memo_change(Tv_password_memo, "green", "사용 가능한 비밀번호입니다.");
                            password_check = true;
                        }
                    }
                } else {
                    memo_change(Tv_password_memo, "black", "숫자,영문,특수 문자를 조합해서 8자 이상 입력해주세요.");
                    password_check = false;
                }
            }
        });
    }
    void memo_change(TextView textView,String color,String memo){
        textView.setText(memo);
        if(color.equals("red")){
            textView.setTextColor(0xAAef484a);
            textView.setTypeface(null, Typeface.BOLD);
        }
        else if(color.equals("green")){
            textView.setTextColor(Color.parseColor("#009900"));
            textView.setTypeface(null, Typeface.NORMAL);
        }
        else{
            textView.setTextColor(Color.parseColor("#777777"));
            textView.setTypeface(null, Typeface.NORMAL);
        }
    }
    void password_change(String user_id,String pw,String verified_pw){

        if(!pw.equals(verified_pw)){
            Toast.makeText(getApplicationContext(),"비밀번호가 일치 하지 않습니다.",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!password_check){
            Toast.makeText(getApplicationContext(),"비밀번호가 형식에 맞지 않습니다.",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            UserdataInterface userdataInterface = ApiClient.getApiClient().create(UserdataInterface.class);
            Call<Userdata_response> call = userdataInterface.password_change(user_id,pw);
            call.enqueue(new Callback<Userdata_response>() {
                @Override
                public void onResponse(Call<Userdata_response> call, Response<Userdata_response> response) {
                    if(response.isSuccessful() && response.body()!=null){
                        if(response.body().getResult().equals("success")){
                            Toast.makeText(getApplicationContext(),"비밀번호가 재설정 되었습니다.",Toast.LENGTH_SHORT).show();
                            Intent login = new Intent(Find_password.this,Login.class);
                            startActivity(login);
                            finish();
                        }
                        else Log.d("!!!FIND_PW: ",response.body().getResult().toString());
                    }
                }

                @Override
                public void onFailure(Call<Userdata_response> call, Throwable t) {
                    Log.d("!!!FIND_PW: ",t.getMessage());
                }
            });
        }
    }
}