package com.example.toron.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.toron.R;
import com.example.toron.Request.CheckphoneRequest;
import com.example.toron.Request.SendRequest;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Userdata_response;
import com.example.toron.Retrofit.Class.Userinfo;
import com.example.toron.Retrofit.Interface.UserdataInterface;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Find_account extends AppCompatActivity {

    Boolean id_overlay=false,nickname_overlay=false,sms_check=false,timer_reset=false; // 중복 검사 내용

    EditText Ev_id_phonenum,Ev_pw_id,Ev_pw_phonenum,Ev_check,Ev_date;
    Button btn_find_id,btn_find_pw,btn_send,btn_auth,btn_back;
    TextView Tv_time;
    String auth_num="null";
    private int auth_time=5;

    private static final int MILLISINFUTURE = 29*1000; //인증 카운다운 시간
    private static final int COUNT_DOWN_INTERVAL = 1000;

    private int count = 29; // 인증 카운다운 시간(디스플레이)
    private TextView countTxt ;
    private CountDownTimer countDownTimer;


    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    }; // 캘린더

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_acount);

        Ev_id_phonenum = findViewById(R.id.EV_id_phone);
        Ev_pw_phonenum = findViewById(R.id.Ev_pw_phone);
        Ev_pw_id = findViewById(R.id.Ev_id);
        Ev_check = findViewById(R.id.Ev_check);
        Ev_date = findViewById(R.id.Ev_date);

        btn_back = findViewById(R.id.btn_back);
        btn_send = findViewById(R.id.btn_send);
        btn_find_id = findViewById(R.id.btn_find_id);
        btn_find_pw = findViewById(R.id.btn_find_password);
        btn_auth = findViewById(R.id.btn_auth);

        Tv_time = findViewById(R.id.Tv_time);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_phonenum = Ev_id_phonenum.getText().toString();
                String user_birthday = Ev_date.getText().toString();
                UserdataInterface userdataInterface = ApiClient.getApiClient().create(UserdataInterface.class);
                Call<Userdata_response> call = userdataInterface.get_Userid("find_id",user_phonenum,user_birthday);
                call.enqueue(new Callback<Userdata_response>() {
                    @Override
                    public void onResponse(Call<Userdata_response> call, Response<Userdata_response> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            String result = response.body().getResult();
                            if(result.equals("success")){
                                String user_id = response.body().getUserinfo().getUser_id();
                                Ev_id_phonenum.setText("");
                                Intent Find_id_Activity = new Intent(Find_account.this, Find_id_Activity.class);
                                Find_id_Activity.putExtra("user_id",user_id);
                                startActivity(Find_id_Activity);
                            }
                            else if(result.equals("birthday")){
                                Toast.makeText(getApplicationContext(),"입력된 생년월일이 올바르지 않습니다.",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"해당 번호로 등록된 아이디가 없습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Userdata_response> call, Throwable t) {
                    }
                });

            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth_time=5;
                String phone_num = Ev_pw_phonenum.getText().toString();
                if(phone_num.trim().length()==0) Toast.makeText(getApplicationContext(),"핸드폰 번호를 입력해주세요." ,Toast.LENGTH_SHORT).show();
                else if(Pattern.matches("^\\d{3}\\d{3,4}\\d{4}$", phone_num))
                {
                    auth_num = null;
                    check_phone_number(phone_num);
                }
                else {
                    Toast.makeText(getApplicationContext(),"올바른 형식이 아닙니다." ,Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        btn_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth_num!=null){
                    if(auth_num.equals(Ev_check.getText().toString())){
                        sms_check = true;
                        auth_lock("lock"); // 인증 잠금
                        Toast.makeText(getApplicationContext(),"인증 되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                    else if(auth_time>0){
                        auth_time--;
                        Toast.makeText(getApplicationContext(),"다시 시도하세요. (남은 횟수 " + auth_time + "회)",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        auth_num=null;
                    }
                }
                else{
                    if(count>0 && count<24) Toast.makeText(getApplicationContext(),count + "초 후에 인증 번호를 다시 받으세요.",Toast.LENGTH_SHORT).show();
                    else Toast.makeText(getApplicationContext(),"인증 번호를 받으세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_find_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = Ev_pw_id.getText().toString();
                String user_phonenum = Ev_pw_phonenum.getText().toString();
                UserdataInterface userdataInterface = ApiClient.getApiClient().create(UserdataInterface.class);
                Call<Userdata_response> call = userdataInterface.get_Userpw("find_pw",user_id,user_phonenum);

                call.enqueue(new Callback<Userdata_response>() {
                    @Override
                    public void onResponse(Call<Userdata_response> call, Response<Userdata_response> response) {
                        if(response.isSuccessful() && response.body() != null){
                            String result = response.body().getResult();
                            if(result.equals("success")){
                                Intent Find_password = new Intent(Find_account.this, Find_password.class);
                                Find_password.putExtra("user_id",response.body().getUserinfo().getUser_id());
                                startActivity(Find_password);
                            }
                            else if(result.equals("id_not_find")){
                                Toast.makeText(getApplicationContext(),"아이디를 다시 확인해주세요.",Toast.LENGTH_SHORT).show();
                            }
                            else if(result.equals("user_phonenum_not_match")){
                                Toast.makeText(getApplicationContext(),"가입 시, 인증된 전화번호가 아닙니다.",Toast.LENGTH_SHORT).show();
                                Ev_check.setText("");
                                sms_check = false;
                                auth_lock("unlock"); // 인증 잠금 해제
                            }
                            else Log.d("!!!FIND:","Failed");
                        }
                        else Log.d("!!!FIND:",response.toString());
                    }
                    @Override
                    public void onFailure(Call<Userdata_response> call, Throwable t) {
                        Log.d("!!!FIND:",t.getMessage());
                    }
                });
            }
        });
        Ev_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Find_account.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
    }


    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        Ev_date.setText(sdf.format(myCalendar.getTime()));
    } // 날짜 텍스트 변경해줌

    void check_phone_number(String phone_num) {
        com.android.volley.Response.Listener<String> responseListener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("result");
                    if(result.equals("success")){
                        Toast.makeText(getApplicationContext(),"등록되지 않은 번호입니다.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        send_msg(phone_num); // 문자 보내기
//                        btn_send.setVisibility(View.GONE);
//                        Tv_time.setVisibility(View.VISIBLE);
//                        Ev_check.setEnabled(true);
                        start_timer();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };

        CheckphoneRequest checkphoneRequest = new CheckphoneRequest(phone_num,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Find_account.this);
        queue.add(checkphoneRequest);
    }
    void send_msg(String phone_num){
        com.android.volley.Response.Listener<String> responseListener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String rand_num = jsonObject.getString("rand_num");
                    String phone_num = jsonObject.getString("phone_num");
                    Log.d("!!!rand_num",rand_num);
                    if(!rand_num.equals("failed")) {
                        Log.d("!!!rand_num", rand_num);
                        auth_num = rand_num;
                        auth_time = 5;
                        btn_send.setVisibility(View.GONE);
                        Tv_time.setVisibility(View.VISIBLE);
                        Ev_check.setEnabled(true);
                        Ev_pw_phonenum.setEnabled(false);
                        start_timer();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"동일 번호로 6회 이상 요청되었습니다.\n24시간 뒤에 시도하세요.",Toast.LENGTH_SHORT).show();
                    }
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("!!!rand_num","ERROR");
                    return;
                }
            }
        };

        SendRequest sendRequest = new SendRequest(phone_num,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Find_account.this);
        queue.add(sendRequest);
    } //메세지 발송
    void start_timer(){
        if(count>0 && count<29) countDownTimer.cancel();
        count = 29;
        countTxt = (TextView)findViewById(R.id.Tv_time);
        countDownTimer();
        countDownTimer.start();
    } //타이머
    public void countDownTimer(){
        count = 29; // 인증 카운다운 시간(디스플레이)
        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                countTxt.setText(String.valueOf("00:")+String.valueOf(String.format("%02d",count)));
                count --;
            }
            public void onFinish() {
                timer_reset=false;
                btn_send.setVisibility(View.VISIBLE);
                Tv_time.setVisibility(View.GONE);
                Ev_check.setEnabled(false);
                if(!sms_check) {
                    Ev_pw_phonenum.setEnabled(true);
                    Ev_check.setText("");
                }
                auth_num = "null";
            }
        };
    } // 타이머 내려가는 거~끝까지
    public void auth_lock(String status){
        if(status.equals("lock")) {
            Ev_pw_phonenum.setEnabled(false);
            Ev_check.setEnabled(false);
            btn_send.setVisibility(View.VISIBLE);
            countTxt.setVisibility(View.GONE);
            btn_send.setEnabled(false);
            btn_auth.setEnabled(false);
        }
        else{
            Ev_pw_phonenum.setEnabled(true);
            Ev_check.setEnabled(true);
            btn_send.setVisibility(View.VISIBLE);
            countTxt.setVisibility(View.GONE);
            btn_send.setEnabled(true);
            btn_auth.setEnabled(true);
        }
    }
}