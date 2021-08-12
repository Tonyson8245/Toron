package com.example.toron.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.toron.Request.CheckphoneRequest;
import com.example.toron.Request.SendRequest;
import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Term_data;
import com.example.toron.Retrofit.Class.Userdata_response;
import com.example.toron.Retrofit.Class.Yesno;
import com.example.toron.Retrofit.Interface.TermInterface;
import com.example.toron.Retrofit.Interface.UserdataInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class Join extends AppCompatActivity {


    public static Activity Join_activity;

    Boolean id_overlay=false,nickname_overlay=false,sms_check=false; // 중복 검사 내용

    private static final int MILLISINFUTURE = 29*1000; //인증 카운다운 시간
    private static final int COUNT_DOWN_INTERVAL = 1000;

    private int count = 29,auth_time=5; // 인증 카운다운 시간(디스플레이)
    private TextView countTxt ;
    private CountDownTimer countDownTimer;

    Boolean password_check=false,date_check=false;

    Button btn_send,btn_join,btn_back,btn_auth,checkbox_service,checkbox_info;
    EditText Ev_check,Ev_phone,Ev_nickname,Ev_pw,Ev_verifed_pw,Ev_id,Ev_date;
    TextView Tv_password_memo,Tv_time,Tv_id_memo,Tv_nickname_memo,Tv_read_service,Tv_read_info;
    String auth_num="null",service_term,info_term;

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

        Join_activity= Join.this;

        setContentView(R.layout.activity_join);

        btn_back = findViewById(R.id.btn_back);
        btn_send = findViewById(R.id.btn_send);
        btn_join = findViewById(R.id.btn_join);
        btn_auth = findViewById(R.id.btn_auth);
        checkbox_service = (Button) findViewById(R.id.checkbox_service);
        checkbox_info = (Button) findViewById(R.id.checkbox_info);

        Tv_time = findViewById(R.id.Tv_time);
        Tv_id_memo = findViewById(R.id.Tv_id_memo);
        Tv_nickname_memo =findViewById(R.id.Tv_nickname_memo);
        Tv_password_memo =findViewById(R.id.Tv_password_memo);
        Tv_read_service = findViewById(R.id.read_service);
        Tv_read_info = findViewById(R.id.read_info);

        Ev_date  = (EditText) findViewById(R.id.Date);
        Ev_check = findViewById(R.id.Ev_check);
        Ev_phone = findViewById(R.id.Ev_phone);
        Ev_nickname = findViewById(R.id.Ev_nickname);
        Ev_pw = findViewById(R.id.Ev_password);
        Ev_id = findViewById(R.id.Ev_id);
        Ev_verifed_pw = findViewById(R.id.Ev_verified_password);

        Ev_nickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {
                //포커스가 주어졌을 때 동작
                if (!gainFocus) {
                    if (Ev_nickname.getText().length() > 0) {
                        //to do
                        UserdataInterface userdataInterface = ApiClient.getApiClient().create(UserdataInterface.class);
                        Call<Yesno> call = userdataInterface.get_Usernicknameoverlay(Ev_nickname.getText().toString());
                        call.enqueue(new Callback<Yesno>() {
                            @Override
                            public void onResponse(Call<Yesno> call, retrofit2.Response<Yesno> response) {
                                //성공하면 "사용가능한 아이디입니다"로 변경
                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body().getReponse().equals("success")) {
                                        Boolean result = response.body().isYesno();
                                        if (result) {
                                            nickname_overlay = true;
                                            memo_change(Tv_nickname_memo, "green", "사용 가능한 닉네임입니다.");
                                        } else { //실패하면 "중복된 아이디입니다."로 변경
                                            nickname_overlay = false;
                                            memo_change(Tv_nickname_memo, "red", "이미 존재하는 닉네임입니다.");
                                        }
                                    } else Log.e("!!!ERROR", "잘못된 요청입니다.");
                                }
                            }

                            @Override
                            public void onFailure(Call<Yesno> call, Throwable t) {
                                Log.e("!!!ERROR", t.getMessage());
                            }
                        });
                        //원하는 동작
                    }
                } else {
                    memo_change(Tv_nickname_memo, "black", "닉네임를 입력해주세요(2글자 이상)");
                }
            }
        });
        Ev_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {
                //포커스가 주어졌을 때 동작
                if (!gainFocus) {
                    if (Ev_id.getText().length() > 0) {
                        //to do
                        UserdataInterface userdataInterface = ApiClient.getApiClient().create(UserdataInterface.class);
                        Call<Yesno> call = userdataInterface.get_Useridoverlay(Ev_id.getText().toString());
                        call.enqueue(new Callback<Yesno>() {
                            @Override
                            public void onResponse(Call<Yesno> call, retrofit2.Response<Yesno> response) {
                                //성공하면 "사용가능한 아이디입니다"로 변경
                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body().getReponse().equals("success")) {
                                        Boolean result = response.body().isYesno();
                                        if (result) {
                                            if(Ev_id.getText().toString().trim().length()<=3) //정규식 확인 5글자
                                            {
                                                memo_change(Tv_id_memo, "red", "4글자 이상인지 확인해보세요.");
                                            }
                                            else if(Pattern.matches("^[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$",Ev_id.getText().toString())){
                                                memo_change(Tv_id_memo, "red", "아이디가 영문인지 확인해보세요.");
                                            }
                                            else {
                                                id_overlay = true;
                                                memo_change(Tv_id_memo, "green", "사용 가능한 아이디입니다.");
                                            }
                                        } else { //실패하면 "중복된 아이디입니다."로 변경
                                            id_overlay = false;
                                            Log.d("!!!", "사용 불가능");
                                            memo_change(Tv_id_memo, "red", "이미 존재하는 아이디입니다.");
                                        }
                                    } else Log.e("!!!ERROR", "잘못된 요청입니다.");
                                }
                            }

                            @Override
                            public void onFailure(Call<Yesno> call, Throwable t) {
                                Log.e("!!!ERROR", t.getMessage());
                            }
                        });
                        //원하는 동작
                    }
                } else {
                    memo_change(Tv_id_memo, "black", "아이디를 입력해주세요(영문,4글자 이상)");
                }
            }
        });
        Ev_pw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {
                //포커스가 주어졌을 때 동작
                if (!gainFocus) {
                    if (Ev_pw.getText().length() > 0) {
                        if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$",Ev_pw.getText().toString()))
                        {
                            memo_change(Tv_password_memo, "red", "숫자,영문,특수 문자를 조합해서 8자 이상 입력해주세요.");
                            password_check = false;
                        }
                        else {
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


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_num = Ev_phone.getText().toString();
                if(phone_num.trim().length()==0) Toast.makeText(getApplicationContext(),"핸드폰 번호를 입력해주세요." ,Toast.LENGTH_SHORT).show();
                else if(!Pattern.matches("^\\d{3}\\d{3,4}\\d{4}$", phone_num))
                {
                    Toast.makeText(getApplicationContext(),"올바른 형식이 아닙니다." ,Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    auth_num = null;
                    check_phone_number(phone_num);
                }
            }
        });
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = Ev_nickname.getText().toString();
                String id = Ev_id.getText().toString();
                String pw = Ev_pw.getText().toString();
                String verified_pw = Ev_verifed_pw.getText().toString();
                if(!sms_check)check_auth_num(); // 인증 번호 확인

                if(nickname.isEmpty()||id.isEmpty()||pw.isEmpty()||verified_pw.isEmpty()){
                    Toast.makeText(getApplicationContext(),"입력되지 않은 항목이 있습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!sms_check) {
                    Toast.makeText(getApplicationContext(),"전화번호 인증을 진행해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(check_userdata(nickname_overlay,id_overlay,pw,verified_pw)) {
                    Intent term = new Intent(Join.this,Term.class);
                    term.putExtra("user_id",Ev_id.getText().toString());
                    term.putExtra("user_password",Ev_pw.getText().toString());
                    term.putExtra("user_nickname",Ev_nickname.getText().toString());
                    term.putExtra("user_birthday",Ev_date.getText().toString());
                    term.putExtra("user_phonenum",Ev_phone.getText().toString());
                    startActivity(term);
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(Join.this,Login.class);
                startActivity(login);
                finish();
            }
        });
        btn_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth_time==0) auth_num=null;
                if(auth_num!=null){
                    if(auth_num.equals(Ev_check.getText().toString())){
                        sms_check = true;
                        auth_lock("lock"); // 인증 잠금
                        Toast.makeText(getApplicationContext(),"인증 되었습니다.",Toast.LENGTH_SHORT).show();
                        countDownTimer.cancel();
                    }
                    else if(auth_time>0){
                        auth_time--;
                        Toast.makeText(getApplicationContext(),"다시 시도하세요. (남은 횟수 " + auth_time + "회)",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    if(count>0 && count<24) Toast.makeText(getApplicationContext(),count + "초 후에 인증 번호를 다시 받으세요.",Toast.LENGTH_SHORT).show();
                    else Toast.makeText(getApplicationContext(),"인증 번호를 받으세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        EditText Ev_date = (EditText) findViewById(R.id.Date);
        Ev_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Join.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
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

    public void auth_lock(String status){
        if(status.equals("lock")) {
            Ev_phone.setEnabled(false);
            Ev_check.setEnabled(false);
            btn_send.setVisibility(View.VISIBLE);
            countTxt.setVisibility(View.GONE);
            btn_send.setEnabled(false);
            btn_auth.setEnabled(false);
        }
        else{
            Ev_phone.setEnabled(true);
            Ev_check.setEnabled(true);
            btn_send.setVisibility(View.VISIBLE);
            countTxt.setVisibility(View.GONE);
            btn_send.setEnabled(true);
            btn_auth.setEnabled(true);
        }
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

    private Boolean check_userdata(Boolean nickname,Boolean id,String pw,String verified_Pw) {
        Boolean result = false;
        if(!pw.equals(verified_Pw)){
            Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
        }
        else if(!nickname_overlay) Toast.makeText(getApplicationContext(),"닉네임을 다시 확인해주세요",Toast.LENGTH_SHORT).show();
        else if(!id_overlay) Toast.makeText(getApplicationContext(),"아이디를 다시 확인해주세요",Toast.LENGTH_SHORT).show();
        else if(!password_check) Toast.makeText(getApplicationContext(),"비밀번호 형식을 확인해주세요.",Toast.LENGTH_SHORT).show();
        else {
            result =true;
        }
        return result;
    }
    void send_msg(String phone_num){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String rand_num = jsonObject.getString("rand_num");
                    String phone_num = jsonObject.getString("phone_num");
                    if(!rand_num.equals("failed")) {
                        Log.d("!!!rand_num", rand_num);
                        auth_num = rand_num;
                        auth_time = 5;
                        btn_send.setVisibility(View.GONE);
                        Tv_time.setVisibility(View.VISIBLE);
                        Ev_check.setEnabled(true);
                        Ev_phone.setEnabled(false);
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
        RequestQueue queue = Volley.newRequestQueue(Join.this);
        queue.add(sendRequest);
    } //메세지 발송
    void start_timer(){
        if(count>0 && count<29) countDownTimer.cancel();
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
                btn_send.setVisibility(View.VISIBLE);
                Tv_time.setVisibility(View.GONE);
                Ev_check.setEnabled(false);
                Ev_phone.setEnabled(true);
                Ev_check.setText("");
                auth_num = "null";
            }
        };
    } // 타이머 내려가는 거~끝까지
    void check_auth_num() {
        String num = Ev_check.getText().toString();
//        Toast.makeText(getApplicationContext(),auth_num + " " + num,Toast.LENGTH_SHORT).show();
        if(auth_num.equals("null")) {
            Toast.makeText(getApplicationContext(), "인증을 진행해주세요", Toast.LENGTH_SHORT).show();
            sms_check = false;
        }
        else if(auth_num.equals(num)){
            Ev_phone.setEnabled(false);
            sms_check = true;
        }
        else {
            Toast.makeText(getApplicationContext(),"인증번호가 올바르지 않습니다.",Toast.LENGTH_SHORT).show();
            sms_check = false;
        }
    }
    void check_phone_number(String phone_num) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("result");

                    if(result.equals("success")){
                        send_msg(phone_num);
                            // 문자 보내기
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"이미 등록된 번호입니다.",Toast.LENGTH_SHORT).show();
                        Ev_phone.setEnabled(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };

        CheckphoneRequest checkphoneRequest = new CheckphoneRequest(phone_num,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Join.this);
        queue.add(checkphoneRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;
    }


    void get_term(String type){
        TermInterface termInterface = ApiClient.getApiClient().create(TermInterface.class);
        Call<Term_data> call = termInterface.get_term(type);
        call.enqueue(new Callback<Term_data>() {
            @Override
            public void onResponse(Call<Term_data> call, retrofit2.Response<Term_data> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("success")){
                        if(type.equals("service")) service_term = response.body().getData();
                        else info_term = response.body().getData();
                    }
                }
            }
            @Override
            public void onFailure(Call<Term_data> call, Throwable t) {
                Log.d("!!!JOIN:약관","실패" + t.getMessage());
            }
        });
    }
}