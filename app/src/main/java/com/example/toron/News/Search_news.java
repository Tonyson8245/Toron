package com.example.toron.News;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.toron.Adapter.NewsRecyclerAdapter;
import com.example.toron.Adapter.SearchRecyclerAdapter;
import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Search_news_img;
import com.example.toron.Retrofit.Class.Search_news_item;
import com.example.toron.Retrofit.Class.Search_news_response;
import com.example.toron.Retrofit.Interface.NewsInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search_news extends AppCompatActivity {
    String TAG = "!!!Search_news";
    Button btn_back,btn_search_news;
    EditText Ev_search_news_keyword;
    String keyword,mode="search";

    List<Search_news_item> list = new ArrayList<Search_news_item>();
    int start=0;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private SearchRecyclerAdapter searchRecyclerAdapter;
    int ATTACH_MODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_news);

        /// 첨부 쪽에서 건너온 녀석인지 확인
        Intent getData = getIntent();
        if(!TextUtils.isEmpty(getData.getStringExtra("mode"))){
            mode = getData.getStringExtra("mode");
        }


        btn_search_news = findViewById(R.id.btn_search_news);
        btn_back = findViewById(R.id.btn_back);

        Ev_search_news_keyword = findViewById(R.id.Ev_search_news_keyword);

        recyclerView = (RecyclerView) findViewById(R.id.search_news_recyclerView);
        recyclerView.setHasFixedSize(true);
        searchRecyclerAdapter = new SearchRecyclerAdapter(Search_news.this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(Search_news.this));
        recyclerView.setAdapter(searchRecyclerAdapter);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_search_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.clear();
                start = 0;
                keyword = Ev_search_news_keyword.getText().toString();
                search_News(keyword,Integer.toString(start));
            }
        });
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (!recyclerView.canScrollVertically(1)) {
                    start += 10;
                    search_News(keyword,Integer.toString(start));
                }
            }
        });
    }

    void search_News(String keyword, String start){

        NewsInterface newsInterface = ApiClient.getApiClient().create(NewsInterface.class);
        Call<Search_news_response> call = newsInterface.get_Search_News(keyword,start);
        call.enqueue(new Callback<Search_news_response>() {
            @Override
            public void onResponse(Call<Search_news_response> call, Response<Search_news_response> response) {
                if(response.isSuccessful() && response.body()!=null){
                    list.addAll(response.body().getItems());
                    searchRecyclerAdapter.set_Search_news_item(list);
                }
            }

            @Override
            public void onFailure(Call<Search_news_response> call, Throwable t) {

            }
        }); // 제목 알아오기
    }

    public void click_news_detail(String news_href, String news_title,String img) {
        Intent news_detail = new Intent(Search_news.this, News_detail.class);
        news_detail.putExtra("href",news_href);
        news_detail.putExtra("title",news_title);
        news_detail.putExtra("img",img);
        news_detail.putExtra("writing","");
        news_detail.putExtra("datetime","");
        if(mode.equals("attach")){
            news_detail.putExtra("mode","attach");
            startActivityForResult(news_detail,ATTACH_MODE);
        }
        else startActivity(news_detail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode) {
                case 11: {
                    Log.d(TAG,"ㅇ닝ㄴ");
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        }
    }
}