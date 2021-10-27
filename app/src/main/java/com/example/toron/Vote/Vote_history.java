package com.example.toron.Vote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toron.Adapter.AttachArticleRecyclerAdapter;
import com.example.toron.Adapter.ChatAdapter;
import com.example.toron.Adapter.ChatAdapter_vote;
import com.example.toron.Adapter.Top_chat_RecyclerAdapter;
import com.example.toron.Adapter.VoteRecyclerAdapter;
import com.example.toron.Debate.Debate_chat_img;
import com.example.toron.Debate.Debate_room;
import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.New_article;
import com.example.toron.Retrofit.Class.Result;
import com.example.toron.Retrofit.Class.Vote_Top_chat;
import com.example.toron.Retrofit.Class.Vote_data;
import com.example.toron.Retrofit.Class.Vote_detail;
import com.example.toron.Retrofit.Interface.VoteInterface;
import com.example.toron.Service.Class.Chat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Vote_history extends AppCompatActivity {

    String vote_idx,TAG = "Vote_history",user_idx,side,status;
    Vote_detail vote_detail;
    TextView Tv_subject,Tv_description;
    Button btn_show_detail,btn_pro,btn_con,btn_back;
    ScrollView vote_detail_scrollview;
    RecyclerView debate_detail_recyclerview;
    Boolean detail_mode = false;
    Gson gson = new Gson();


    RecyclerView con_top_recyclerview,pro_top_recyclerview,pros_recyclerview,cons_recycler_view,debate_recyclerview; // 반대 상위 의견, 찬성 상위 의견, 찬성 기사 리스트, 반대 기사 리스트
    AttachArticleRecyclerAdapter pros_adapter,cons_adapter; //  반대 의견 어댑터, 찬성 의견 어댑터, 찬성 기사 어댑터, 반대 기사 어댑터
    Top_chat_RecyclerAdapter con_top_adapter,pro_top_adapter;
    ChatAdapter_vote chatAdapter;
    LinearLayoutCompat layout_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_history);

        Intent getData = getIntent();
        vote_idx = getData.getStringExtra("vote_idx");
        if(!TextUtils.isEmpty(getData.getStringExtra("status"))){
            status = getData.getStringExtra("status");
            side = getData.getStringExtra("side");
        }// tag_메세지 때문에 들어옴

        Tv_subject = findViewById(R.id.Tv_subject);
        Tv_description = findViewById(R.id.Tv_description);
        con_top_recyclerview = findViewById(R.id.con_top_recyclerview);
        pro_top_recyclerview = findViewById(R.id.pro_top_recyclerview);
        pros_recyclerview = findViewById(R.id.pros_recyclerview);
        cons_recycler_view = findViewById(R.id.cons_recyclerview);
        btn_show_detail = findViewById(R.id.btn_show_detail);
        vote_detail_scrollview = findViewById(R.id.layout_debate_info);
        debate_detail_recyclerview  = findViewById(R.id.debate_recyclerview);
        btn_back = findViewById(R.id.btn_back);
        btn_pro = findViewById(R.id.btn_pro);
        btn_con = findViewById(R.id.btn_con);
        layout_button = findViewById(R.id.layout_button);

        SharedPreferences sharedPreferences;
        sharedPreferences = this.getSharedPreferences("user_data",0);
        user_idx = sharedPreferences.getString("user_idx",null);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_show_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!detail_mode) {
                    vote_detail_scrollview.setVisibility(View.GONE);
                    debate_detail_recyclerview.setVisibility(View.VISIBLE);
                    detail_mode = true;
                    btn_show_detail.setText("투표 정보");
                }
                else{
                    vote_detail_scrollview.setVisibility(View.VISIBLE);
                    debate_detail_recyclerview.setVisibility(View.GONE);
                    detail_mode = false;
                    btn_show_detail.setText("토론 내용");

                }
            }
        });

        if(status==null) {
            btn_con.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select("con");
                }
            });

            btn_pro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select("pro");
                }
            });
        }
        else{
            layout_button.setVisibility(View.GONE);
            btn_con.setClickable(false);
            btn_pro.setClickable(false);
        }
    }

    private void select(String side) {
        VoteInterface voteInterface = ApiClient.getApiClient().create(VoteInterface.class);
        Call<Result> call = voteInterface.Vote(user_idx,vote_idx,side);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.body()!=null && response.isSuccessful()){
                    if(response.body().getResult().equals("success")){
                        Toast.makeText(getApplicationContext(),"투표가 완료되었습니다.",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else Log.d(TAG,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_vote_data();
    }

    private void get_vote_data(){
        VoteInterface voteInterface = ApiClient.getApiClient().create(VoteInterface.class);
        Call<Vote_detail> call = voteInterface.Select_vote_detail(vote_idx);
        call.enqueue(new Callback<Vote_detail>() {
            @Override
            public void onResponse(Call<Vote_detail> call, Response<Vote_detail> response) {
                if(response.body()!=null && response.isSuccessful()){
                    vote_detail = response.body();
                    Log.d(TAG,vote_detail.toString());

                    get_chat_list(vote_detail.getVote_data().getRoom_idx());

                    AdapterSetting(); //리사이클러뷰와 어댑터 연결 및 세팅
                    set_vote_data(vote_detail);
                }
                else Log.d(TAG,"FAILED");
            }

            @Override
            public void onFailure(Call<Vote_detail> call, Throwable t) {

            }
        });
    }

    private void set_vote_data(Vote_detail detail){
        ArrayList<New_article> pros = new ArrayList<>();
        ArrayList<New_article> cons = new ArrayList<>();
        ArrayList<Vote_Top_chat> vote_top_chat_con = new ArrayList<>();
        ArrayList<Vote_Top_chat> vote_top_chat_pro = new ArrayList<>();

        detail.getVote_top_chat().forEach(k -> {
            if(k.getChat_side().equals("con")) vote_top_chat_con.add(k);
            else vote_top_chat_pro.add(k);
        }); // 람다 식을 이용하여 해당 Arraylist 를 foreach 문으로 사용하고 값이 반대일 경우 반대 arraylist 에 넣는다.
        pros = detail.getPro();
        cons = detail.getCon();

        Tv_subject.setText(detail.getVote_data().getVote_subject());
        Tv_description.setText(detail.getVote_data().getVote_description());
        con_top_adapter.set_Newslist(vote_top_chat_con);
        pro_top_adapter.set_Newslist(vote_top_chat_pro);
        cons_adapter.set_Newslist(cons);
        pros_adapter.set_Newslist(pros);
    }
    public void MoveToArticle(String href) {
        Intent website =  new Intent(Intent.ACTION_VIEW, Uri.parse(href));
        startActivity(website);
    } // 기사로 이동

    private void AdapterSetting(){
        // 반대 의견 어댑터 세팅
        con_top_recyclerview.setHasFixedSize(true);
        con_top_adapter = new Top_chat_RecyclerAdapter(this);
        con_top_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        con_top_recyclerview.setAdapter(con_top_adapter);

        // 찬성 의견 어댑터 세팅
        pro_top_recyclerview.setHasFixedSize(true);
        pro_top_adapter = new Top_chat_RecyclerAdapter(this);
        pro_top_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        pro_top_recyclerview.setAdapter(pro_top_adapter);

        // 반대 기사 어댑터 세팅
        cons_recycler_view.setHasFixedSize(true);
        cons_adapter = new AttachArticleRecyclerAdapter(this);
        cons_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        cons_recycler_view.setAdapter(cons_adapter);

        // 찬성 기사 어댑터 세팅
        pros_recyclerview.setHasFixedSize(true);
        pros_adapter = new AttachArticleRecyclerAdapter(this);
        pros_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        pros_recyclerview.setAdapter(pros_adapter);

        // 채팅 어댑터 세팅
        debate_detail_recyclerview.setHasFixedSize(true);
        chatAdapter = new ChatAdapter_vote(this);
        debate_detail_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        debate_detail_recyclerview.setAdapter(chatAdapter);
    }


    private void get_chat_list(String room_idx){
        VoteInterface voteInterface = ApiClient.getApiClient().create(VoteInterface.class);
        Call<ArrayList<Chat>> call = voteInterface.Select_chat_list(room_idx);
        call.enqueue(new Callback<ArrayList<Chat>>() {
            @Override
            public void onResponse(Call<ArrayList<Chat>> call, Response<ArrayList<Chat>> response) {
                Log.d(TAG,"Asdasd" + response.body().toString());
                if(response.body()!=null && response.isSuccessful()){
                    Type type = new TypeToken<ArrayList<Chat>>() {}.getType();
                    ArrayList<Chat> temp_list = response.body();
                    chatAdapter.set_chatlist(temp_list);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Chat>> call, Throwable t) {
                Log.d(TAG,"FAILED" + t.getMessage());
            }
        });
    }

    public void open_img(String url) {
        Intent open_img =  new Intent(Vote_history.this, Debate_chat_img.class);
        open_img.putExtra("url",url);
        startActivityForResult(open_img,0);
    }

    public void totTagMessage(String tag_chat_idx){
        vote_detail_scrollview.setVisibility(View.GONE);
        debate_detail_recyclerview.setVisibility(View.VISIBLE);
        debate_detail_recyclerview.post(new Runnable() {
            @Override
            public void run() {
                debate_detail_recyclerview.scrollToPosition(chatAdapter.get_Tag_Item_Position(tag_chat_idx));
            }
        });
        detail_mode = !detail_mode;
    }
}

