package com.example.toron.Mypage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toron.Debate.Debate_room;
import com.example.toron.Login.Join;
import com.example.toron.Login.Login;
import com.example.toron.Main.Mainpage;
import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Image_upload;
import com.example.toron.Retrofit.Class.Userdata_response;
import com.example.toron.Retrofit.Class.Yesno;
import com.example.toron.Retrofit.Interface.Inquire_intface;
import com.example.toron.Retrofit.Interface.UserdataInterface;
import com.example.toron.Service.Class.Chat;
import com.example.toron.Service.RemoteService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Profile_setting extends AppCompatActivity {


    private Messenger mServiceCallback = null;
    // 서비스로부터 전달 받는 객체 바인딩 시 제공하는 IBinder 로 만들어진 Messenger 객체
    private Messenger mClientCallback = new Messenger(new CallbackHandler());
    // 액티비티 <-> 서비스 : 서비스에서 액티비티로 결과를 리턴을 받을 때 쓰임 ; HTTP 통신과 유사한 개념


    Boolean nickname_overlay=false;
    String TAG = "!!!profile",user_nickname,user_id,user_birthday,temp_nickname;
    ImageView img_profile,img_camera;
    Button btn_setting,btn_back,btn_overlay_check;
    EditText Ev_date,Ev_nickname;
    LinearLayout btn_change_password,btn_change_phonenum;
    TextView Tv_nickname_memo,btn_logout;
    final static int TAKE_PICTURE = 0;
    static final int FROM_ALBUM = 1;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private PopupWindow mPopupWindow ;
    Uri profile_uri;

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(user_birthday);
        }
    }; // 캘린더


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        sharedPreferences = getSharedPreferences("user_data",0);
        editor = sharedPreferences.edit();

        img_profile = findViewById(R.id.img_profile);
        img_camera = findViewById(R.id.img_camera);
        Tv_nickname_memo = findViewById(R.id.Tv_nickname_memo);

        btn_setting = findViewById(R.id.btn_setting);
        btn_back = findViewById(R.id.btn_back);
        btn_change_password = findViewById(R.id.btn_change_password);
        btn_overlay_check = findViewById(R.id.btn_overlay_check);
        btn_logout = findViewById(R.id.btn_logout);

        Ev_date  = (EditText) findViewById(R.id.Date);
        Ev_nickname = (EditText) findViewById(R.id.Ev_nickname);

        get_userdata();

        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog(v);
            }
        });
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { makeDialog(v); }
        });

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profile_save()) {
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            // 시간 지난 후 실행할 코딩
//                            Intent mypage = new Intent(Profile_setting.this, Mainpage.class);
//                            startActivity(mypage);
                            finish();
                        }
                    }, 100); // 0.5초후
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent change_password = new Intent(Profile_setting.this,Change_password.class);
                startActivity(change_password);
            }
        });
        btn_overlay_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname_overlay_check();
            }
        });
        Ev_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Profile_setting.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        Ev_nickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                memo_change(Tv_nickname_memo,"black","변경할 닉네임을 2글자 이상으로 입력해주세요");
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent(Profile_setting.this,Login.class);
                sharedPreferences = getSharedPreferences("auto_login",0);
                editor = sharedPreferences.edit();
                editor.putString("auto_login","OFF");
                editor.commit();
                quit_socket();
                startActivity(logout);
                finish();

                ActivityCompat.finishAffinity(Profile_setting.this);
            }
        });
    }




    // 서비스 연결 끊기
    public class CallbackHandler extends Handler {
        Gson gson = new Gson();

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

            }
        }
    }

    /// 서비스와 연결하고 , 서비스에서 오는 데이터에 따라 핸들러를 구동 시키는 부분
    private ServiceConnection mConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) { //해당 서비스의 IBinder 라는 객체 생성
            Log.d(TAG, "onServiceConnected");
            mServiceCallback = new Messenger(service); // IBinder 를 통해 Messenger 객체 생성 가능
            // mServiceCallback : 원하는 서비스의 Messegner 객체

            // connect to service
            Message connect_msg = Message.obtain( null, RemoteService.MSG_CLIENT_CONNECT);
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
            mServiceCallback = null;
        }
    };


    protected void onResume() {
        super.onResume();
        Log.d("mClient", "onResume:Debate_room");
        Intent intent = new Intent(this, RemoteService.class); // 바인드를 위한 intent
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE); // 여기서 액티비티와 서비스를 바인드 해줌
    }

    public void quit_socket(){
        if (mServiceCallback != null) {
            // request 'add value' to service
            Message message = Message.obtain(
                    null, RemoteService.MSG_QUIT_SOCKET);
            try {
                mServiceCallback.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    //




    private Boolean profile_save() {
        if(profile_uri!=null) {
            try {
//                Bitmap bitmap = ((BitmapDrawable) img_profile.getDrawable()).getBitmap();
                String user_idx = sharedPreferences.getString("user_idx", null);

//                File file = SaveBitmapToFileCache(bitmap, (Environment.getExternalStorageDirectory().getAbsolutePath()) + "/Toron/Temp/" + user_idx + ".jpg");

                Log.d(TAG,profile_uri.toString());
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), getRealFile(profile_uri));
                MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", user_idx + ".jpg", requestFile);
                Inquire_intface inquire_intface = ApiClient.getApiClient().create(Inquire_intface.class);
                Call<Image_upload> resultCall = inquire_intface.uploadImage(body);
                resultCall.enqueue(new Callback<Image_upload>() {
                    @Override
                    public void onResponse(Call<Image_upload> call, Response<Image_upload> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d(TAG, "success" + response.body().getResult() + response.body().getValue());
                            try {
                                String url = "http://49.247.195.99/storage/profile_img/" + user_idx + ".jpg";

                                Picasso.get().invalidate(url);
                                Picasso.get().load(url).into(img_profile); // picasso 에서 glide 로 교체
//                                Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(img_profile); // picasso 에서 glide 로 교체
                            } catch (Exception e) {
                                img_profile.setImageResource(R.mipmap.ic_launcher_round);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Image_upload> call, Throwable t) {
                        Log.d(TAG, "error" + t.getMessage());
                    }
                });
            } catch (Exception e) {
            }
        }
        String nickname = user_nickname;
        String birthday = user_birthday;

        if(Ev_date.getText().length()>0){
            birthday = Ev_date.getText().toString();
        }
        if(Ev_nickname.getText().length()>0){
            nickname = Ev_nickname.getText().toString();
        }
        if(nickname_overlay|| Ev_nickname.getText().length()==0){
            if(Ev_nickname.getText().toString().equals(temp_nickname)|| Ev_nickname.getText().length()==0){
                update_profile(user_id,nickname,birthday);
                return true;
            }
            else{
                Toast.makeText(this, "닉네임 중복검사를 다시 하세요", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else{
            Toast.makeText(this, "닉네임 중복검사를 하세요", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK && intent.hasExtra("data")) {
                    Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
                    if (bitmap != null) {
                        img_profile.setImageBitmap(bitmap);
                    }
                    profile_uri = getImageUri(this,bitmap);
                }
                break;
            case FROM_ALBUM:
                try{
                    InputStream in = getContentResolver().openInputStream(intent.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    img_profile.setImageBitmap(img);
                    profile_uri = intent.getData();
                }catch(Exception e)
                {

                }
                break;

        }
    }
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void makeDialog(View v){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(Profile_setting.this);
        alt_bld.setTitle("사진 업로드").setIcon(R.drawable.ic_baseline_image_24).setCancelable(
                false).setPositiveButton("사진촬영",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v("알림", "다이얼로그 > 사진촬영 선택");
                        // 사진 촬영 클릭
                        takePhoto(v);
                    }
                }).setNegativeButton("앨범선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        Log.v("알림", "다이얼로그 > 앨범선택 선택");
                        //앨범에서 선택
                        selectAlbum();
                    }
                }).setNeutralButton("취소   ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v("알림", "다이얼로그 > 취소 선택");
                        // 취소 클릭. dialog 닫기.
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    private void selectAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        startActivityForResult(intent, FROM_ALBUM);
    }

    private void takePhoto(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                ActivityCompat.requestPermissions(Profile_setting.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                Log.d(TAG, "권한 설정 요청");
            }
        }

        switch (v.getId()) {
            case R.id.img_camera:
            case R.id.img_profile:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, TAKE_PICTURE);
                break;
        }
    }

    private File SaveBitmapToFileCache(Bitmap bitmap, String strFilePath) {
        File fileCacheItem = new File(strFilePath);
        OutputStream out = null;
        try
        {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d(TAG,e.getMessage());
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return fileCacheItem;
    }
    void get_userdata(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("user_data",0);

        user_nickname = sharedPreferences.getString("user_nickname","nick");
        user_id = sharedPreferences.getString("user_id",null);
        user_birthday = sharedPreferences.getString("user_birthday",null);
        String user_idx = sharedPreferences.getString("user_idx",null);

//        String uristr = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Toron/Storage/Image/profile_img.jpg";

        Ev_date.setHint(user_birthday.replace("-","/"));
        Ev_nickname.setHint(user_nickname);
        try{
            Picasso.get().load("http://49.247.195.99/storage/profile_img/" + user_idx + ".jpg").into(img_profile);// glide 로 교체
        }catch (Exception e){
            img_profile.setImageResource(R.mipmap.ic_launcher_round);
        }
//
//        File files = new File(uristr);
//        //파일 유무를 확인합니다.
//        //파일이 없을시
//        if(files.exists()==true) {
//            Uri uri = Uri.parse(uristr);
//            img_profile.setImageURI(uri);
//            //파일이 있을시
//        } else {
//            img_profile.setImageResource(R.mipmap.ic_launcher_round);
//        }

    }

    private void updateLabel(String user_birthday) {
        String myFormat = "yyyy/MM/dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        Ev_date.setText(sdf.format(myCalendar.getTime()));
    } // 날짜 텍스트 변경해줌
    private void nickname_overlay_check(){
        if(Ev_nickname.getHint().equals(Ev_nickname.getText().toString())){
            memo_change(Tv_nickname_memo, "green", "기존 닉네임과 동일하여, 사용 가능합니다.");
            nickname_overlay = true;
            temp_nickname = Ev_nickname.getText().toString();
        }
        else if (Ev_nickname.getText().length() > 2) {
            //to do
            UserdataInterface userdataInterface = ApiClient.getApiClient().create(UserdataInterface.class);
            Call<Yesno> call = userdataInterface.get_Usernicknameoverlay(Ev_nickname.getText().toString());
            call.enqueue(new Callback<Yesno>() {
                @Override
                public void onResponse(Call<Yesno> call, retrofit2.Response<Yesno> response) {
                    //성공하면 "사용가능한 아이디입니다"로 변경
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getResponse().equals("success")) {
                            Boolean result = response.body().isYesno();
                            if (result) {
                                nickname_overlay = true;
                                temp_nickname = Ev_nickname.getText().toString();
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
        else {
        memo_change(Tv_nickname_memo, "red", "닉네임를 입력해주세요(2글자 이상)");
        nickname_overlay=false;
        }
    }
    private void memo_change(TextView textView, String color, String memo){
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
    private void update_profile(String id,String nickname,String birthday){
        UserdataInterface userdataInterface = ApiClient.getApiClient().create(UserdataInterface.class);
        Call<Userdata_response> call = userdataInterface.updateUserprofile(id,birthday,nickname);
        call.enqueue(new Callback<Userdata_response>() {
            @Override
            public void onResponse(Call<Userdata_response> call, Response<Userdata_response> response) {
                if(response.body()!=null && response.isSuccessful()){

                }
            }
            @Override
            public void onFailure(Call<Userdata_response> call, Throwable t) {
                Log.d(TAG,t.getMessage().toString());
            }
        });

        editor.putString("user_nickname",nickname);// 쉐어드에 저장
        editor.putString("user_id",id);// 쉐어드에 저장
        editor.putString("user_birthday",birthday);
        editor.commit();
    }
    private File getRealFile(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        if(uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor cursor = getContentResolver().query(uri, projection, null, null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if(cursor == null || cursor.getColumnCount() <1 ) {
            return null;
        }
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        String path = cursor.getString(column_index);

        if(cursor != null) {
            cursor.close();
            cursor = null;
        }

        return new File(path);
    }
}