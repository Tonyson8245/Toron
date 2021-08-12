package com.example.toron.Mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.toron.Login.Find_password;
import com.example.toron.Login.Login;
import com.example.toron.R;
import com.example.toron.Request.LoginRequest;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Userdata_response;
import com.example.toron.Retrofit.Interface.UserdataInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Change_password extends AppCompatActivity {

    Button btn_back,btn_change;
    EditText Ev_password,Ev_verified_password,Ev_old_password;
    TextView Tv_password_memo;
    Boolean password_check = false;

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences;

        sharedPreferences = getSharedPreferences("user_data",0);
        user_id = sharedPreferences.getString("user_id",null);

        setContentView(R.layout.activity_change_password);

        btn_back = findViewById(R.id.btn_back);
        btn_change =findViewById(R.id.btn_change);

        Ev_password  = findViewById(R.id.Ev_password);
        Ev_verified_password = findViewById(R.id.Ev_verified_password);
        Ev_old_password = findViewById(R.id.Ev_oldpassword);

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
                com.android.volley.Response.Listener<String> responseListener = new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("!!!receive", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {//성공시
                                JSONObject result = jsonObject.getJSONObject("result");
                                password_change(user_id,Ev_password.getText().toString(),Ev_verified_password.getText().toString());
                            } else {//실패시
                                Toast.makeText(getApplicationContext(), "기존 비밀번호가 일치 하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(user_id, Ev_old_password.getText().toString(), responseListener);
                Log.d("!!!send", loginRequest.toString());
                RequestQueue queue = Volley.newRequestQueue(Change_password.this);
                queue.add(loginRequest);
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
                        else if(Ev_old_password.getText().toString().equals(Ev_password.getText().toString())){
                            memo_change(Tv_password_memo, "red", "기존 비밀번호와 다른 비밀번호를 입력해주세요.");
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