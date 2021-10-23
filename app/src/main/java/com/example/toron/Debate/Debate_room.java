 package com.example.toron.Debate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toron.Adapter.AttachArticleRecyclerAdapter;
import com.example.toron.Adapter.AttachArticleRecyclerAdapter_room;
import com.example.toron.Adapter.ChatAdapter;
import com.example.toron.Fragment.Devate_fragment;
import com.example.toron.Main.Mainpage;
import com.example.toron.News.Search_news;
import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Image_upload;
import com.example.toron.Retrofit.Class.New_article;
import com.example.toron.Retrofit.Class.Result;
import com.example.toron.Retrofit.Class.Room_data;
import com.example.toron.Retrofit.Class.Yesno;
import com.example.toron.Retrofit.Interface.DebateInterface;
import com.example.toron.Retrofit.Interface.Inquire_intface;
import com.example.toron.Retrofit.Interface.NewsInterface;
import com.example.toron.Service.Class.Chat;
import com.example.toron.Service.Class.Status_Foreground;
import com.example.toron.Service.RemoteService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
//import com.squareup.picasso.MemoryPolicy;
//import com.squareup.picasso.NetworkPolicy;
//import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.time.LocalDateTime.now;

public class Debate_room extends AppCompatActivity {

    private RecyclerView chat_RecyclerView,cons_recyclerview,pros_recyclerview;
    private ChatAdapter chatAdapter;
    private AttachArticleRecyclerAdapter_room articleAdapter_cons;
    private AttachArticleRecyclerAdapter_room articleAdapter_pros;
    int ATTACH_MODE = 11;

    private Messenger mServiceCallback = null;
    // 서비스로부터 전달 받는 객체 바인딩 시 제공하는 IBinder 로 만들어진 Messenger 객체
    private Messenger mClientCallback = new Messenger(new CallbackHandler());
    // 액티비티 <-> 서비스 : 서비스에서 액티비티로 결과를 리턴을 받을 때 쓰임 ; HTTP 통신과 유사한 개념

    Boolean TAG_MODE =false,DETAIL_MODE = true,GET_DATA=false,END_OF_CHAT = true,img_mode = false,finish_mode=false;

    ArrayList<Chat> chat_list = new ArrayList<>();
    ArrayList<New_article> pros = new ArrayList<>();
    ArrayList<New_article> cons = new ArrayList<>();


    LinearLayout tag_layout;
    TextView Tv_subject,Tv_description,Tv_tag_nickname,pros_no_article,cons_no_article;
    ImageView btn_img_attach,Img_attach_img;
    EditText Ev_message_content;
    Button btn_send,btn_back,btn_tag_close,btn_show_detail,btn_image_attach_close,btn_send_img,btn_attach_pros,btn_attach_cons;
    String TAG = "Debate_room",room_idx,side,tag_user_idx,tag_user_nickname,tag_chat_idx=null,user_idx,user_nickname,user_id;
    Date time = new Date();
    ScrollView layout_debate_info;
    LinearLayout message_layout,layout_img_attach;
    Uri img_uri;
    SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");

    // 사진 전송 부분
    private int GALLEY_CODE = 10;
    private String imageUrl="";
    private ImageView ivProfile;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mClient","onResume:Debate_room");
        Intent intent = new Intent(this, RemoteService.class); // 바인드를 위한 intent
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE); // 여기서 액티비티와 서비스를 바인드 해줌

        if(!GET_DATA) request_chat_list(Integer.valueOf(room_idx));
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(!img_mode || finish_mode) {
            Log.d("mClient", "onPause:Debate_room");disconnect_service();
            unbindService(mConnection);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debate_room);

        SharedPreferences sharedPreferences;
        sharedPreferences = this.getSharedPreferences("user_data",0);
        user_idx = sharedPreferences.getString("user_idx",null);
        user_nickname = sharedPreferences.getString("user_nickname",null);
        user_id = sharedPreferences.getString("user_id",null);


        Intent getData = getIntent();
        room_idx = getData.getStringExtra("room_idx");
        if(!TextUtils.isEmpty(getData.getStringExtra("tag_chat_idx"))){
            tag_chat_idx = getData.getStringExtra("tag_chat_idx");
            Log.d(TAG,"tag" + tag_chat_idx);
        }// tag_메세지 때문에 들어옴
        else tag_chat_idx = null;

        Tv_subject = findViewById(R.id.Tv_subject);
        Tv_tag_nickname = findViewById(R.id.Tv_tag_nickname);
        Tv_description = findViewById(R.id.Tv_description);
        btn_send = findViewById(R.id.insert_message);
        Ev_message_content = findViewById(R.id.Ev_message_content);
        btn_back = findViewById(R.id.btn_back);
        tag_layout = findViewById(R.id.tag_layout);
        btn_tag_close = findViewById(R.id.btn_tag_close);
        btn_img_attach = findViewById(R.id.btn_img_attach);
        btn_show_detail = findViewById(R.id.btn_show_detail);
        layout_debate_info = findViewById(R.id.layout_debate_info);
        message_layout = findViewById(R.id.message_layout);
        Img_attach_img = findViewById(R.id.Img_attach_img);
        btn_image_attach_close = findViewById(R.id.btn_img_attach_close);
        layout_img_attach = findViewById(R.id.layout_img_attach);
        btn_send_img = findViewById(R.id.btn_send_img);
        pros_no_article = findViewById(R.id.Tv_pros_no_aricle);
        cons_no_article = findViewById(R.id.Tv_cons_no_aricle);
        btn_attach_cons = findViewById(R.id.btn_attach_cons);
        btn_attach_pros = findViewById(R.id.btn_attach_pros);

        chat_RecyclerView = findViewById(R.id.dabate_recyclerview);
        chat_RecyclerView.setHasFixedSize(true);
        chatAdapter = new ChatAdapter(chat_list, this);
        chat_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chat_RecyclerView.setAdapter(chatAdapter);
        // 채팅 리사이클러뷰 세팅

        pros_recyclerview = findViewById(R.id.pros_recyclerview);
        pros_recyclerview.setHasFixedSize(true);
        articleAdapter_pros = new AttachArticleRecyclerAdapter_room( this,pros);
        pros_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        pros_recyclerview.setAdapter(articleAdapter_pros);
        // 찬성 기사 리사이클러뷰 세팅

        cons_recyclerview = findViewById(R.id.cons_recyclerview);
        cons_recyclerview.setHasFixedSize(true);
        articleAdapter_cons = new AttachArticleRecyclerAdapter_room( this,cons);
        cons_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        cons_recyclerview.setAdapter(articleAdapter_cons);
        // 반대 기사 리사이클러뷰 세팅

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish_mode = true;
                finish();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_msg(Ev_message_content.getText().toString());
                Ev_message_content.setText("");
            }
        });
        btn_tag_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag_layout.setVisibility(View.GONE);
                TAG_MODE = false;//  TAG_MODE OFF
                tag_user_idx = null;
                tag_user_nickname = null;
            }
        });
        btn_img_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag_layout.setVisibility(View.GONE);
                TAG_MODE = false;//  TAG_MODE OFF
                tag_user_idx = null;
                tag_user_nickname = null;
                img_mode = true;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,GALLEY_CODE);
//                Intent ImgIntent = new Intent();
//                ImgIntent.setType("image/*");
//                ImgIntent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(ImgIntent, GALLEY_CODE);
            }
        });
        btn_show_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DETAIL_MODE){
                    DETAIL_MODE = false;
                    chat_RecyclerView.setVisibility(View.GONE);
                    message_layout.setVisibility(View.GONE);
                    layout_debate_info.setVisibility(View.VISIBLE);
                    btn_show_detail.setText("내용 닫기");
                }
                else{
                    DETAIL_MODE = true;
                    chat_RecyclerView.setVisibility(View.VISIBLE);
                    message_layout.setVisibility(View.VISIBLE);
                    layout_debate_info.setVisibility(View.GONE);
                    btn_show_detail.setText("내용 보기");
                }
            }
        });
        btn_image_attach_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_img_attach.setVisibility(View.GONE);
                message_layout.setVisibility(View.VISIBLE);
            }
        });
        btn_send_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,img_uri.toString());
                send_img(); // 이미지 전송

                layout_img_attach.setVisibility(View.GONE); // 이미지 전송 레이아웃 끄고
                message_layout.setVisibility(View.VISIBLE); // 채팅 레이아웃 키고
            }
        });
        chat_RecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                 if (!chat_RecyclerView.canScrollVertically(1)) END_OF_CHAT = true;
                 else END_OF_CHAT = false;
            }
        });

        btn_attach_pros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_article();
            }
        });
        btn_attach_cons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_article();
            }
        });

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

            request_chat_list(Integer.valueOf(room_idx));
        } // 액티비티의 messenger 객체를 서비스에 전달

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceCallback = null;
        }
    };

    public void open_img(String url) {
        Intent open_img =  new Intent(Debate_room.this,Debate_chat_img.class);
        open_img.putExtra("url",url);
        startActivityForResult(open_img,0);
    }


    private class CallbackHandler extends Handler  {
        Gson gson = new Gson();
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RemoteService.MSG_GET_CHAT_LIST:
                    Bundle bundle = (Bundle) msg.obj;
                    Type type = new TypeToken<ArrayList<Chat>>() {}.getType();

                    Log.d(TAG,bundle.getString("list"));
                    ArrayList<Chat> temp_list = gson.fromJson(bundle.getString("list"), type);
                    chat_list = temp_list;

                    getRoomData(room_idx);
                    chatAdapter.set_chatlist(chat_list);
                    chatAdapter.notifyDataSetChanged();

                    if(tag_chat_idx!=null){
                        totTagMessage(tag_chat_idx);
                    }
                    else{
                        toBottom(true);
                    }
                    break;
                case RemoteService.MSG_GET_CHAT:
                    tag_chat_idx = null;
                    Bundle bundle2 = (Bundle) msg.obj;;
                    String temp_msg = bundle2.getString("chat");

                    Log.d(TAG,"Chat" + bundle2.getString("chat"));
                    Chat chat = gson.fromJson(temp_msg,Chat.class);
                    if(chat.getRoom_idx().equals(room_idx)) {
                        chat_list.add(new Chat(chat.getChat_mode(),chat.getChat_idx(),chat.getRoom_idx(), chat.getMsg(), chat.getUser_idx(), chat.getDatetime(), chat.getSide(), chat.getNickname(),chat.getTag_user_idx(),chat.getImg_href()));
                        chatAdapter.notifyItemChanged(chat_list.size() - 1);
                        toBottom(false);
                    }
                    break;
                case RemoteService.MSG_CHECK_ACTIVITY:
                    sendBackName(msg);
                    break;
            }
        }
    }

    private void getRoomData(String room_idx){
        DebateInterface debateInterface = ApiClient.getApiClient().create(DebateInterface.class);
        Call<com.example.toron.Retrofit.Class.Room_data> call = debateInterface.Select_Room_data(user_idx,room_idx);

        call.enqueue(new Callback<com.example.toron.Retrofit.Class.Room_data>() {
            @Override
            public void onResponse(Call<com.example.toron.Retrofit.Class.Room_data> call, Response<com.example.toron.Retrofit.Class.Room_data> response) {
                if(response.body()!=null && response.isSuccessful()){
                    Tv_subject.setText(response.body().getRoom_subject());
                    Tv_description.setText(response.body().getRoom_description());
                    side = response.body().getUser_maker();

                    cons = response.body().getCons();
                    pros = response.body().getPros();

                    if(cons!=null){
                        if(cons.size()>0) {
                        cons = response.body().getCons();
                        articleAdapter_cons.notifyDataSetChanged();
                        articleAdapter_cons.setList(cons);
                        }
                    }
                    else cons_no_article.setVisibility(View.VISIBLE);
                    if(pros!=null) {
                        if (pros.size() > 0) {
                            pros = response.body().getPros();
                            articleAdapter_pros.setList(pros);
                            articleAdapter_pros.notifyDataSetChanged();
                        }
                    }
                    else pros_no_article.setVisibility(View.VISIBLE);

                    if(side.equals("con")) btn_attach_cons.setVisibility(View.VISIBLE);
                    else btn_attach_pros.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<Room_data> call, Throwable t) {
                Log.d(TAG,"failed" + t.getMessage());
            }
        });
    } //  방 번호를 통해 방 정보를 가져옴

    private void sendBackName(Message message){
        Bundle data = (Bundle) message.obj;

        Bundle bundle = new Bundle();
        bundle.putString("name","room");
        bundle.putString("room_idx",room_idx);
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

    private void request_chat_list(Integer room_idx){
        Log.d(TAG,"request_chat_list" + room_idx);
        if (mServiceCallback != null) {
            // request 'add value' to service
            Message msg = Message.obtain(
                    null, RemoteService.MSG_GET_CHAT_LIST);
            msg.arg1 = room_idx;
            try {
                mServiceCallback.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Send MSG_GET_CHAT_LIST message to Service");
        }
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

    public void toBottom(boolean setting){
        chat_RecyclerView.post(new Runnable() {
            @Override
            public void run() {
                Log.d("Tobottom",setting + " " + tag_chat_idx + " " + END_OF_CHAT);
                if(tag_chat_idx!=null) totTagMessage(tag_chat_idx);
                else if(END_OF_CHAT) chat_RecyclerView.scrollToPosition(chat_RecyclerView.getAdapter().getItemCount() - 1);
                else if(setting) chat_RecyclerView.scrollToPosition(chat_RecyclerView.getAdapter().getItemCount() - 1);
            }
        });
    }
//                    chat_RecyclerView.scrollToPosition(chat_RecyclerView.getAdapter().getItemCount() - 1);

    private void totTagMessage(String tag_chat_idx){
        END_OF_CHAT = false;
        chat_RecyclerView.post(new Runnable() {
            @Override
            public void run() {
                chat_RecyclerView.scrollToPosition(chatAdapter.get_Tag_Item_Position(tag_chat_idx));
            }
        });
    }

    private void send_msg(String msg){
        String tag = null,content = null;
        if(TAG_MODE){
            content = "@"+tag_user_nickname+" "+ msg;
            tag = tag_user_idx;
        }// tag 모드 켜짐
        else{
            tag = null;
            content = msg;
        }//tag 모드 꺼짐

        String time_data = format1.format(time);
        Log.d(TAG,"time" + time_data);

        chat_list.add(new Chat("msg","",room_idx,content,user_idx,time_data,side,user_nickname,tag_user_idx,"")); //  나를 태그 할 일은 업승니까 그냥 보내자.
        chatAdapter.notifyItemChanged(chat_list.size()-1);
        //클라에 추가

        Bundle bundle = new Bundle();
        bundle.putString("room_idx",room_idx);
        bundle.putString("user_idx",user_idx);
        bundle.putString("datetime",time_data);
        bundle.putString("side",side);
        bundle.putString("tag_user_idx",tag);
        bundle.putString("msg",content);

        Log.d(TAG,"tag_user_idx" + tag_user_idx);



        if (mServiceCallback != null) {
            // request 'add value' to service
            Message message = Message.obtain(
                    null, RemoteService.MSG_SEND_MSG);
            message.obj = bundle;
            try {
                mServiceCallback.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        toBottom(true);
    }

    public void set_TagMode(String user_idx,String nickname){
        tag_layout.setVisibility(View.VISIBLE);

        tag_user_idx = user_idx; // tag_user_idx 설정
        tag_user_nickname = nickname;

        TAG_MODE = true; //  TAG_MODE ON
        Tv_tag_nickname.setText("to." + nickname);
    }

    private void send_img(){
        if(img_uri!=null) {
            try {
                Log.d(TAG,img_uri.toString());
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), getRealFile(img_uri));
                MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", user_idx,requestFile);
                Inquire_intface inquire_intface = ApiClient.getApiClient().create(Inquire_intface.class);
                Call<Image_upload> resultCall = inquire_intface.uploadChatImage(body);
                resultCall.enqueue(new Callback<Image_upload>() {
                    @Override
                    public void onResponse(Call<Image_upload> call, Response<Image_upload> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d(TAG, "success" + response.body().getResult() + response.body().getValue());
                            sendToService(img_uri,response.body().getValue());
                            img_uri = null;
                        }
                    }

                    @Override
                    public void onFailure(Call<Image_upload> call, Throwable t) {
                        Log.d(TAG, "error" + t.getMessage());
                        img_uri = null;
                    }
                });
            } catch (Exception e) {
            }
        }
    }

    private void sendToService(Uri img_uri,String image_name){ // 레트로핏으로 받아온 이미지 이름
        String tag = null,content = null;


        String time_data = format1.format(time);

        chat_list.add(new Chat("local_img","",room_idx,"",user_idx,time_data,side,user_nickname,"",String.valueOf(img_uri)));// 로컬에서는 바로 사진 띄우자
        chatAdapter.notifyItemChanged(chat_list.size()-1);
        //클라에 추가

        Bundle bundle = new Bundle();
        bundle.putString("chat_mode","img");
        bundle.putString("room_idx",room_idx);
        bundle.putString("user_idx",user_idx);
        bundle.putString("datetime",time_data);
        bundle.putString("side",side);
        bundle.putString("tag_user_idx",tag);
        bundle.putString("msg",null);
        bundle.putString("img_href",image_name);


        if (mServiceCallback != null) {
            // request 'add value' to service
            Message message = Message.obtain(
                    null, RemoteService.MSG_SEND_IMG);
            message.obj = bundle;
            try {
                mServiceCallback.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        toBottom(true);
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

    private void report_chat(String chat_idx){
        DebateInterface debateInterface = ApiClient.getApiClient().create(DebateInterface.class);
        Call<Result> call = debateInterface.insert_chat_report(chat_idx,user_idx);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.isSuccessful() && response.body()!=null){
                    if(response.body().getResult().equals("success")) {
                        if(response.body().getMessage().equals("insert_completed")) Toast.makeText(getApplicationContext(), "해당 의견을 신고 처리 하였습니다.", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(getApplicationContext(), "이미 신고한 의견입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }
    private void like_chat(String chat_idx,String like_chat_side){
        DebateInterface debateInterface = ApiClient.getApiClient().create(DebateInterface.class);
        Call<Result> call = debateInterface.insert_chat_like(chat_idx,user_idx,room_idx,like_chat_side);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.isSuccessful() && response.body()!=null){
                    if(response.body().getResult().equals("success")) {
                        Log.d(TAG,response.body().getMessage());
                        if(response.body().getMessage().equals("insert_completed")) Toast.makeText(getApplicationContext(), "해당 의견에 동의 하였습니다.", Toast.LENGTH_SHORT).show();
                        else if(response.body().getMessage().equals("select_completed")) Toast.makeText(getApplicationContext(), "이미 동의한 의견입니다.", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(getApplicationContext(), "같은 진영일 경우에만 의견 동의를 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }


    public void report_dialog(String chat_idx){
        String report_dialog_chat_idx = chat_idx;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("의견 신고").setMessage("정말로 신고하시겠습니까?\n모든 신고 내용은 저장됩니다.\n허위 신고의 경우, 불이익이 있을 수 있습니다.");

        builder.setPositiveButton("취소", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("신고", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                report_chat(report_dialog_chat_idx);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void open_dialog(String chat_idx,String like_chat_side){
        String open_dialog_chat_idx = chat_idx;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("의견 평가").setMessage("해당 의견에 동의 또는 신고 할 수 있습니다.");

        builder.setPositiveButton("취소", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("신고", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                report_dialog(open_dialog_chat_idx);
            }
        });
        builder.setNeutralButton("동의", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                like_chat(open_dialog_chat_idx,like_chat_side);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void MoveToArticle(String href) {
        Log.d(TAG,"href:" + href);
        Intent website =  new Intent(Intent.ACTION_VIEW, Uri.parse(href));
        startActivity(website);
    } // 기사로 이동




    private String getRealPathFromUri(Uri uri) {
        String[] proj= {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String url = cursor.getString(columnIndex);
        cursor.close();
        return url;
    }// uri 를 통해 절대 경로를 구하는 방법

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == GALLEY_CODE && resultCode == RESULT_OK) {
            imageUrl = getRealPathFromUri(data.getData());
            img_uri = data.getData();
            Picasso.get().load(img_uri).into(Img_attach_img);
//            try{
//                InputStream in = getContentResolver().openInputStream(data.getData());
//
//                Bitmap img = BitmapFactory.decodeStream(in);
//                Img_attach_img.setImageBitmap(img);
//                in.close();
//            }catch(Exception e)
//            {
//
//            }
            layout_img_attach.setVisibility(View.VISIBLE);
            message_layout.setVisibility(View.GONE);
            img_mode=false;
        }
        else if(requestCode == 11 && resultCode == RESULT_OK){
            String a_title = data.getStringExtra("title");
            String a_href = data.getStringExtra("href");

            Log.d(TAG,a_title + " " + a_href);
            attach_article(side,a_title,a_href);
        }
        else if(requestCode==0)   GET_DATA = true;

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void search_article(){
        Intent search_News = new Intent(Debate_room.this, Search_news.class);
        search_News.putExtra("mode","attach");
        startActivityForResult(search_News,ATTACH_MODE);
    } // 첨부를 위한 기사 찾기
    private void attach_article(String side, String title,String href) {
        DebateInterface debateInterface = ApiClient.getApiClient().create(DebateInterface.class);
        Call<Result> call = debateInterface.Insert_article_list(side,room_idx,title,href);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.isSuccessful() && response.body()!=null){
                    Log.d(TAG,response.body().getResult() + response.body().getMessage());
                    if(response.body().getResult().equals("success")){
                        if(response.body().getMessage().equals("select_completed")) Toast.makeText(getApplicationContext(),"이미 첨부된 기사입니다.", Toast.LENGTH_SHORT).show();
                        else  Toast.makeText(getApplicationContext(),"기사 첨부가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    if(side.equals("con")){
                        cons.add(new New_article(href,title));
                        articleAdapter_cons.setList(cons);
                    }
                    else{
                        pros.add(new New_article(href,title));
                        articleAdapter_pros.setList(pros);
                    }

                    articleAdapter_pros.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    } // 서버에 기사 첨부

}