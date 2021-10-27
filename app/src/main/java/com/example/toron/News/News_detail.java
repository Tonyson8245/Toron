    package com.example.toron.News;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toron.Adapter.NewsRecyclerAdapter;
import com.example.toron.Adapter.ReplyRecyclerAdapter;
import com.example.toron.Debate.Debate_make;
import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Detail_article;
import com.example.toron.Retrofit.Class.New_article;
import com.example.toron.Retrofit.Class.Reply;
import com.example.toron.Retrofit.Class.Reply_response;
import com.example.toron.Retrofit.Class.Result;
import com.example.toron.Retrofit.Class.Yesno;
import com.example.toron.Retrofit.Interface.NewsInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class News_detail extends AppCompatActivity {

    private Spinner reply_spinner;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList;

    List<Reply> list = new ArrayList<>();
    private int page = 0;
    String href,title,img,writing,datetime,news_idx,mode="detail";
    ImageView Img_news;
    EditText Ev_reply_content;
    TextView Tv_news_script,Tv_news_title,Tv_datetime,Tv_reply_qty,no_reply;
    ScrollView scrollview;
    LinearLayout reply_layout,layout_reply_qty;
    Button btn_back,btn_website,btn_insert_reply,btn_update_reply,btn_make_dabete,btn_attach;
    String TAG = "!!!DETAIL",sort="like",total_qty,user_id,user_idx;
    InputMethodManager imm;
    private RecyclerView recyclerView;
    private ReplyRecyclerAdapter replyRecyclerAdapter;
    boolean last_reply=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("user_data",0);

        //댓글 데이터를 얻는 부분
        user_id = sharedPreferences.getString("user_id",null);
        user_idx = sharedPreferences.getString("user_idx",null);
        //쉐어드

        Intent getdata = getIntent();

        href = getdata.getStringExtra("href");
        title = getdata.getStringExtra("title");
        img = getdata.getStringExtra("img");
        writing = getdata.getStringExtra("writing");
        datetime = getdata.getStringExtra("datetime");
        news_idx = getdata.getStringExtra("news_idx");

        //인텐트

        /// 첨부 쪽에서 건너온 녀석인지 확인
        if(!TextUtils.isEmpty(getdata.getStringExtra("mode"))){
            mode = getdata.getStringExtra("mode");
        }

        Img_news = findViewById(R.id.Img_news);
        btn_attach = findViewById(R.id.btn_attach);
        Ev_reply_content = findViewById(R.id.Ev_reply_content);
        Tv_reply_qty = findViewById(R.id.reply_qty);
        Tv_news_script = findViewById(R.id.TV_news_script);
        Tv_news_title = findViewById(R.id.Tv_news_title);
        Tv_datetime = findViewById(R.id.Tv_datetime);
        btn_back = findViewById(R.id.btn_back);
        btn_website = findViewById(R.id.btn_website);
        btn_make_dabete = findViewById(R.id.btn_make_debate);
        btn_insert_reply = findViewById(R.id.insert_reply);
        reply_layout = findViewById(R.id.reply_layout);
        scrollview = findViewById(R.id.scrollview);
        btn_update_reply = findViewById(R.id.update_reply);
        layout_reply_qty = findViewById(R.id.layout_reply_qty);
        no_reply = findViewById(R.id.no_reply);

        // 바인딩

        recyclerView = (RecyclerView) findViewById(R.id.reply_recyclerview);
        recyclerView.setHasFixedSize(true);
        replyRecyclerAdapter = new ReplyRecyclerAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(replyRecyclerAdapter);

        //리사이클러뷰 세팅

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(mode.equals("attach")) btn_attach.setVisibility(View.VISIBLE);
        else btn_make_dabete.setVisibility(View.VISIBLE);

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
        btn_insert_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_reply();
                Ev_reply_content.setText("");
                imm.hideSoftInputFromWindow(Ev_reply_content.getWindowToken(), 0);
            }
        });

        btn_make_dabete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent make_debate = new Intent(News_detail.this,Debate_make.class);
                make_debate.putExtra("URL",href);
                startActivity(make_debate);
            }
        });
        btn_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra("title",title);
                result.putExtra("href",href);
                setResult(RESULT_OK,result);
                finish();
            }
        });
        scrollview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.d(TAG,String.valueOf(v.canScrollVertically(1)));
                if((!v.canScrollVertically(1))){
                    if(page>Integer.parseInt(total_qty) && last_reply){
                        Toast.makeText(getApplicationContext(),"마지막 댓글입니다.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        page+=5;
                        get_reply(news_idx,String.valueOf(page),sort,user_id);
                    }
                    last_reply = false;
                }
                else last_reply = true;
            }
        });

        spinner("recent");

        if(datetime.length()==0) Tv_datetime.setVisibility(View.GONE);
        if(title.length()==0) Tv_news_title.setVisibility(View.GONE);
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
                if(response.body()!=null && response.isSuccessful()){
                    if(response.body().getResult().equals("success")){
                        news_idx = response.body().getNews_idx();
                        if(news_idx.equals("no_news_idx")){
                            reply_layout.setVisibility(View.GONE);
                            no_reply.setVisibility(View.VISIBLE);
                            layout_reply_qty.setVisibility(View.GONE);
                        }
                        else{
                            reply_layout.setVisibility(View.VISIBLE);
                            no_reply.setVisibility(View.GONE);
                            layout_reply_qty.setVisibility(View.VISIBLE);
                        }

                        String content = response.body().getText();
                        if(content != null) {
                            if (!content.equals("")) {
                                content = content.replace("<br>    ", "\n");
                                content = content.replace("<br>", "\n");
                                Tv_news_script.setText(Html.fromHtml(content));
                            }
                        }
                        else Tv_news_script.setText("");
                    }
                    else{
                        news_idx = response.body().getNews_idx();
                        Tv_news_script.setVisibility(View.GONE);
                        if(news_idx.equals("no_news_idx")){
                            reply_layout.setVisibility(View.GONE);
                            no_reply.setVisibility(View.VISIBLE);
                            layout_reply_qty.setVisibility(View.GONE);
                        }else{
                            reply_layout.setVisibility(View.VISIBLE);
                            no_reply.setVisibility(View.GONE);
                            layout_reply_qty.setVisibility(View.VISIBLE);
                        }

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

    private void set_reply(){
        String reply_user_id;
        String reply_content;
        String reply_news_idx;

        reply_user_id = user_id;
        reply_content = Ev_reply_content.getText().toString();
        reply_news_idx = news_idx;

        NewsInterface newsInterface = ApiClient.getApiClient().create(NewsInterface.class);
        Call<Result> call = newsInterface.insert_reply(reply_user_id,reply_content,reply_news_idx);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.body()!=null && response.isSuccessful()){
                    if(response.body().getResult().equals("success")){
                        Toast.makeText(getApplicationContext(),"댓글이 입력되었습니다.",Toast.LENGTH_SHORT).show();
                        list.clear();
                        page = 0;
                        get_reply(news_idx,String.valueOf(page),sort,user_id);
                    }
                    else Toast.makeText(getApplicationContext(),"댓글 입력이 실패되었습니다.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });

    }

    private void get_reply(String news_idx,String page,String sort,String user_id){
        NewsInterface newsInterface = ApiClient.getApiClient().create(NewsInterface.class);
        Call<Reply_response> call = newsInterface.get_reply(news_idx,sort,page,user_id);

        call.enqueue(new Callback<Reply_response>() {
            @Override
            public void onResponse(Call<Reply_response> call, Response<Reply_response> response) {
                if(response.isSuccessful() && response.body()!=null){
                    if(response.body().getResult().equals("success")){
                        list.addAll(response.body().getReply());
                        total_qty = response.body().getTotal();
                        Tv_reply_qty.setText("댓글 " + total_qty);
                    }

                }
                else Log.d(TAG,"내용 없음");
                replyRecyclerAdapter.setList(list,user_id,user_idx);
            }

            @Override
            public void onFailure(Call<Reply_response> call, Throwable t) {

            }
        });
    }

    public void delete_reply(String reply_idx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("댓글 삭제").setMessage("정말로 삭제하시겠습니까.");

        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                NewsInterface newsInterface = ApiClient.getApiClient().create(NewsInterface.class);
                Call<Result> call = newsInterface.delete_reply(reply_idx);
                call.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            if(response.body().getResult().equals("success")){
                                Toast.makeText(getApplicationContext(),"삭제가 완료되었습니다.",Toast.LENGTH_SHORT).show();
                            }
                            else Log.e(TAG,"댓글 삭제 실패");
                        }
                        list.clear();
                        page = 0;
                        get_reply(news_idx,String.valueOf(page),sort,user_id);
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {

                    }
                });
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void spinner(String init){
        arrayList = new ArrayList<>();
        arrayList.add("좋아요순");
        arrayList.add("최신순");
        arrayList.add("오래된순");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        reply_spinner = (Spinner)findViewById(R.id.reply_spinner);
        reply_spinner.setAdapter(arrayAdapter);
        reply_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(arrayList.get(i).equals("최신순")) {
                    list.clear();
                    page = 0;
                    get_reply(news_idx, "0", "recent", user_id);
                    sort = "recent";
                }
                else if(arrayList.get(i).equals("오래된순")){
                    list.clear();
                    page = 0;
                    get_reply(news_idx, "0", "desc", user_id);
                    sort = "desc";
                }
                else{
                    list.clear();
                    page = 0;
                    get_reply(news_idx, "0", "like", user_id);
                    sort = "like";
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void update_reply(String reply_idx, String content) {
        Ev_reply_content.setText(content);
        btn_insert_reply.setVisibility(View.GONE);
        btn_update_reply.setVisibility(View.VISIBLE);
        btn_update_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_reply_method(reply_idx);
                btn_insert_reply.setVisibility(View.VISIBLE);
                btn_update_reply.setVisibility(View.GONE);
                Ev_reply_content.setText("");
                imm.hideSoftInputFromWindow(Ev_reply_content.getWindowToken(), 0);
            }
        });
    }

    private void update_reply_method(String reply_idx){
        String content = Ev_reply_content.getText().toString();
        NewsInterface newsInterface = ApiClient.getApiClient().create(NewsInterface.class);
        Call<Result> call = newsInterface.update_reply(reply_idx,content);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.d(TAG,response.toString());
                if(response.body()!=null & response.isSuccessful()){
                    if(response.body().getResult().equals("success")){
                        Toast.makeText(getApplicationContext(),"댓글 수정이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                list.clear();
                page = 0;
                get_reply(news_idx,String.valueOf(page),sort,user_id);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });

    }

    public void update_like(String reply_idx,String now_onoff) {
        NewsInterface newsInterface = ApiClient.getApiClient().create(NewsInterface.class);
        String onoff;
        if(now_onoff.equals("1")) onoff = "off";
        else onoff = "on";
        Call<Result> call = newsInterface.reply_like(reply_idx,user_id,onoff);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.body()!=null && response.isSuccessful()){
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }
}