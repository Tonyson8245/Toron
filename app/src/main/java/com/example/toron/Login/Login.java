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
                                if (success) {//?????????
                                    JSONObject result = jsonObject.getJSONObject("result");
                                    Toast.makeText(getApplicationContext(), result.get("user_nickname")+ "??? ???????????????.", Toast.LENGTH_SHORT).show();
                                    save_userdata(result);
                                    auto_login_save();
                                } else {//?????????
                                    if(jsonObject.getString("message").equals("Wrong password"))  Toast.makeText(getApplicationContext(), "???????????? ?????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                                    else  Toast.makeText(getApplicationContext(), "???????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
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
                else Toast.makeText(getApplicationContext(), "???????????? ??????????????? ???????????????", Toast.LENGTH_SHORT).show();
            }
        }); // ????????? ??????

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent join = new Intent(Login.this, Join.class);
                startActivity(join);
                finish();
            }
        });//?????????????????? ??????

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
            Toast.makeText(getApplicationContext(),"?????? ????????????????????????.",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    void auto_login_save(){
        auto_login_editor =  auto_login.edit();
        if(CB_auto_login.isChecked()) auto_login_editor.putString("auto_login","ON");// ???????????? ??????
        else auto_login_editor.putString("auto_login","OFF");// ???????????? ??????
        auto_login_editor.commit();
    }

    void save_userdata(JSONObject result) throws JSONException {
        editor.putString("user_nickname",result.get("user_nickname").toString());// ???????????? ??????
        editor.putString("user_id",result.get("user_id").toString());// ???????????? ??????
        editor.putString("user_idx",result.get("user_idx").toString());// ???????????? ??????
        editor.putString("user_birthday",result.get("user_birthday").toString());
        editor.commit();


//        String url = "http://49.247.195.99/storage/profile_img/"+result.get("user_idx")+".jpg";
//        ImageLoadTask task = new ImageLoadTask(url,"profile_img");
//        task.execute();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                // ?????? ?????? ??? ????????? ??????
                Intent intent = new Intent(Login.this, Mainpage.class);
                intent.putExtra("page","login");
                startActivity(intent);
                finish();
            }
        }, 300); // 0.5??????
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