package com.example.toron.Mypage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.toron.Adapter.Mypage_reply_Adapter;
import com.example.toron.Adapter.Participate_debate_recyclerAdapter;
import com.example.toron.Debate.Debate_room;
import com.example.toron.News.News_detail;
import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Mypage_participate_debate;
import com.example.toron.Retrofit.Class.Mypage_reply;
import com.example.toron.Retrofit.Class.Result_mypage_debate_list;
import com.example.toron.Retrofit.Class.Result_mypage_reply;
import com.example.toron.Retrofit.Interface.MypageInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Mypage_reply_list extends AppCompatActivity {

    Button btn_back;
    RecyclerView recyclerView;
    Mypage_reply_Adapter mypage_reply_adapter;
    TextView tv_no_data;
    String user_idx,TAG = "Mypage_reply_list";
    ArrayList<Mypage_reply> list = new ArrayList<>();
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_reply);

        btn_back = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.reply_list);
        tv_no_data = findViewById(R.id.tv_no_data);

        SharedPreferences sharedPreferences;
        sharedPreferences = this.getSharedPreferences("user_data",0);
        user_idx = sharedPreferences.getString("user_idx",null);


        recyclerView.setHasFixedSize(true);
        mypage_reply_adapter = new Mypage_reply_Adapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mypage_reply_adapter);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_reply_list(user_idx);
    }

    private void get_reply_list(String user_idx) {
        MypageInterface mypageInterface =  ApiClient.getApiClient().create(MypageInterface.class);
        Call<Result_mypage_reply> call = mypageInterface.Select_reply_list(user_idx);
        call.enqueue(new Callback<Result_mypage_reply>() {
            @Override
            public void onResponse(Call<Result_mypage_reply> call, Response<Result_mypage_reply> response) {
                if(response.isSuccessful() && response.body()!=null){
                    Result_mypage_reply temp_result = response.body();
                    Log.d(TAG,response.body().toString());
                    if(temp_result.getResult().equals("success")){
                        list = temp_result.getMessage();
                        mypage_reply_adapter.setList(list);

                        recyclerView.setVisibility(View.VISIBLE);
                        tv_no_data.setVisibility(View.GONE);
                    }
                    else if(temp_result.getResult().equals("failed")){
                        recyclerView.setVisibility(View.GONE);
                        tv_no_data.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result_mypage_reply> call, Throwable t) {
                Log.d(TAG,t.getMessage());
            }
        });
    }

    public void moveToNews(String news_idx,String href,String title,String news_img,String news_writing,String news_datetime) {
        Intent news = new Intent(this, News_detail.class);
        news.putExtra("news_idx",news_idx);
        news.putExtra("href",href);
        news.putExtra("title",title);
        news.putExtra("img",news_img);
        news.putExtra("writing",news_writing);
        news.putExtra("datetime",news_datetime);
        startActivity(news);
    }
}