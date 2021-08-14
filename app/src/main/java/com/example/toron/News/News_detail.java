package com.example.toron.News;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Detail_article;
import com.example.toron.Retrofit.Interface.NewsInterface;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class News_detail extends AppCompatActivity {

    String href,title,img,writing,datetime;
    ImageView Img_news;
    TextView Tv_news_script,Tv_news_title,Tv_datetime;
    Button btn_back,btn_website;
    String TAG = "!!!DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Intent getdata = getIntent();
        href = getdata.getStringExtra("href");
        title = getdata.getStringExtra("title");
        img = getdata.getStringExtra("img");
        writing = getdata.getStringExtra("writing");
        datetime = getdata.getStringExtra("datetime");

        Img_news = findViewById(R.id.Img_news);
        Tv_news_script = findViewById(R.id.TV_news_script);
        Tv_news_title = findViewById(R.id.Tv_news_title);
        Tv_datetime = findViewById(R.id.Tv_datetime);
        btn_back = findViewById(R.id.btn_back);
        btn_website = findViewById(R.id.btn_website);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent website =  new Intent(Intent.ACTION_VIEW, Uri.parse(href));
                startActivity(website);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        set_news();
    }

    private void get_news(){
        NewsInterface newsInterface = ApiClient.getApiClient().create(NewsInterface.class);
        Call<Detail_article> call = newsInterface.get_Detail_news(href);
        call.enqueue(new Callback<Detail_article>() {
            @Override
            public void onResponse(Call<Detail_article> call, Response<Detail_article> response) {
                Log.d(TAG,response.toString());
                if(response.body()!=null && response.isSuccessful()){
                    if(response.body().getResult().equals("success")){
                        String content = response.body().getText();
                        content = content.replace("<br>    ","\n");
                        content = content.replace("<br>","\n");
                        Tv_news_script.setText(content);
                    }
                    else{
                        //본문으로 연결
                        Log.d(TAG,"error");
                        Tv_news_script.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Detail_article> call, Throwable t) {

            }
        });
    }

    private void set_news(){
        get_news();
        try{
            Picasso.get().load(img).into(Img_news);
        }catch (Exception e){
            Img_news.setImageResource(R.drawable.ic_baseline_image_24);
            Img_news.setPadding(20,20,20,20);
        }
        Tv_news_title.setText(Html.fromHtml(title));
        Tv_datetime.setText(datetime);
    }
}