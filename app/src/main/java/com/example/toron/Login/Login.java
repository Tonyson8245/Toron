package com.example.toron.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.toron.Class.ImageLoadTask;
import com.example.toron.Main.Mainpage;
import com.example.toron.Mypage.Mypage_main;
import com.example.toron.Request.LoginRequest;
import com.example.toron.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class Login extends AppCompatActivity {

    EditText textID, textPW;
    Button login_btn, signup_btn,account_find_btn;
    CheckBox CB_auto_login;

    SharedPreferences sharedPreferences;
    SharedPreferences auto_login;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor auto_login_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textID = findViewById(R.id.id);
        textPW = findViewById(R.id.pw);
        login_btn = findViewById(R.id.login_button);
        signup_btn = findViewById(R.id.signup_button);
        account_find_btn = findViewById(R.id.btn_find_account);
        CB_auto_login = findViewById(R.id.CB_auto_login);

        sharedPreferences = getSharedPreferences("user_data",0);
        auto_login = getSharedPreferences("auto_login",0);
        editor = sharedPreferences.edit();

        login_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String Lid = textID.getText().toString();
                String Lpw = textPW.getText().toString();

                if (Lid.length() > 0 && Lpw.length() > 0) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("!!!receive", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {//성공시
                                    JSONObject result = jsonObject.getJSONObject("result");
                                    Toast.makeText(getApplicationContext(), result.get("user_nickname")+ "님 어서오세요.", Toast.LENGTH_SHORT).show();
                                    save_userdata(result);
                                    auto_login_save();
                                } else {//실패시
                                    if(jsonObject.getString("message").equals("Wrong password"))  Toast.makeText(getApplicationContext(), "올바르지 않은 비밀번호입니다.", Toast.LENGTH_SHORT).show();
                                    else  Toast.makeText(getApplicationContext(), "존재하지 않는 회원입니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(Lid, Lpw, responseListener);
                    Log.d("!!!send", loginRequest.toString());
                    RequestQueue queue = Volley.newRequestQueue(Login.this);
                    queue.add(loginRequest);
                }
                else Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            }
        }); // 로그인 과정

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent join = new Intent(Login.this, Join.class);
                startActivity(join);
                finish();
            }
        });//회원가입으로 이동

        account_find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent account_find = new Intent(Login.this, Find_account.class);
                startActivity(account_find);
            }
        });

        if(auto_login.getString("auto_login","OFF").equals("ON")){
            Intent intent = new Intent(Login.this, Mainpage.class);
            intent.putExtra("page","login");
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"자동 로그인되었습니다.",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    void auto_login_save(){
        auto_login_editor =  auto_login.edit();
        if(CB_auto_login.isChecked()) auto_login_editor.putString("auto_login","ON");// 쉐어드에 저장
        else auto_login_editor.putString("auto_login","OFF");// 쉐어드에 저장
        auto_login_editor.commit();
    }

    void save_userdata(JSONObject result) throws JSONException {
        editor.putString("user_nickname",result.get("user_nickname").toString());// 쉐어드에 저장
        editor.putString("user_id",result.get("user_id").toString());// 쉐어드에 저장
        editor.putString("user_idx",result.get("user_idx").toString());// 쉐어드에 저장
        editor.putString("user_birthday",result.get("user_birthday").toString());
        editor.commit();


//        String url = "http://49.247.195.99/storage/profile_img/"+result.get("user_idx")+".jpg";
//        ImageLoadTask task = new ImageLoadTask(url,"profile_img");
//        task.execute();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                // 시간 지난 후 실행할 코딩
                Intent intent = new Intent(Login.this, Mainpage.class);
                intent.putExtra("page","login");
                startActivity(intent);
                finish();
            }
        }, 300); // 0.5초후
    }

    @Override
    protected void onStart() {
        super.onStart();
        String strpath = (Environment.getExternalStorageDirectory().getAbsolutePath()) + "/Toron/Storage/Image/profile_img.jpg";
        try{
            File file = new File(strpath);
            if(file.exists()){
                file.delete();
            }
        }catch (Exception e){

        }
    }
}