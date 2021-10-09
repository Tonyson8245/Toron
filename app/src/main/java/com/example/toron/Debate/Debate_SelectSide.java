package com.example.toron.Debate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toron.Adapter.AttachArticleRecyclerAdapter;
import com.example.toron.Adapter.NewsRecyclerAdapter;
import com.example.toron.Adapter.ReplyRecyclerAdapter;
import com.example.toron.News.News_detail;
import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.New_article;
import com.example.toron.Retrofit.Class.Room_data;
import com.example.toron.Retrofit.Class.Yesno;
import com.example.toron.Retrofit.Interface.DebateInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Debate_SelectSide extends AppCompatActivity {

    RecyclerView cons_recyclerView,pros_recyclerView;
    private AttachArticleRecyclerAdapter consAdapter;
    private AttachArticleRecyclerAdapter prosAdapter;
    TextView Tv_subject,Tv_description,Tv_cons_text,Tv_pros_text;
    String room_idx;
    Button btn_con,btn_pro;
    String TAG = "Debate_SelectSide";
    ArrayList<New_article> cons = new ArrayList<>(); // 반대
    ArrayList<New_article> pros = new ArrayList<>(); // 찬성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debate_select_side);

        cons_recyclerView = findViewById(R.id.dabate_recyclerview_cons);
        pros_recyclerView = findViewById(R.id.dabate_recyclerview_pros);

        Tv_subject  = findViewById(R.id.Tv_subject);
        Tv_description = findViewById(R.id.Tv_description);
        Tv_cons_text = findViewById(R.id.cons_text);
        Tv_pros_text = findViewById(R.id.pros_text);
        btn_con = findViewById(R.id.btn_con);
        btn_pro = findViewById(R.id.btn_pro);

        Intent getData = getIntent();
        room_idx = getData.getStringExtra("room_idx");
        getRoomData(room_idx);


        cons_recyclerView.setHasFixedSize(true);
        consAdapter = new AttachArticleRecyclerAdapter(this, cons);
        cons_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cons_recyclerView.setAdapter(consAdapter);

        pros_recyclerView.setHasFixedSize(true);
        prosAdapter = new AttachArticleRecyclerAdapter(this, pros);
        pros_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pros_recyclerView.setAdapter(prosAdapter);

        btn_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_side("con");
            }
        });
        btn_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_side("pro");
            }
        });
    }
    private void getRoomData(String room_idx){
        DebateInterface debateInterface = ApiClient.getApiClient().create(DebateInterface.class);
        Call<Room_data> call = debateInterface.Select_Room_data(room_idx);

        call.enqueue(new Callback<Room_data>() {
            @Override
            public void onResponse(Call<Room_data> call, Response<Room_data> response) {
                if(response.body()!=null && response.isSuccessful()){
                    Tv_subject.setText(response.body().getRoom_subject());
                    Tv_description.setText(response.body().getRoom_description());

                    if(cons.size()>0) {
                        cons = response.body().getCons();
                        consAdapter.notifyDataSetChanged();
                        consAdapter.setList(cons);
                    }
                    if(pros.size()>0) {
                        pros = response.body().getPros();
                        prosAdapter.setList(pros);
                        prosAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onFailure(Call<Room_data> call, Throwable t) {
                Log.d(TAG,"failed" + t.getMessage());
            }
        });
    } //  방 번호를 통해 방 정보를 가져옴

    public void MoveToArticle(String href) {
        Intent website =  new Intent(Intent.ACTION_VIEW, Uri.parse(href));
        startActivity(website);
    } // 기사로 이동

    public void select_side(String side){
        SharedPreferences sharedPreferences;
        sharedPreferences = this.getSharedPreferences("user_data",0);
        String user_idx = sharedPreferences.getString("user_idx",null);

        DebateInterface debateInterface = ApiClient.getApiClient().create(DebateInterface.class);
        Call<Yesno> call = debateInterface.insert_Participate_list(room_idx,user_idx,side);
        call.enqueue(new Callback<Yesno>() {
            @Override
            public void onResponse(Call<Yesno> call, Response<Yesno> response) {
                Intent enter_room = new Intent(Debate_SelectSide.this, Debate_room.class);
                enter_room.putExtra("room_subject",Tv_subject.getText());
                enter_room.putExtra("room_description",Tv_description.getText());
                enter_room.putExtra("room_idx",room_idx);
                startActivity(enter_room);
                finish();
            }

            @Override
            public void onFailure(Call<Yesno> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"찬반 선택에 실패 했습니다. 다시 시도 하세요",Toast.LENGTH_SHORT).show();
            }
        });
    }// 방에 참가 시킴 :: 방 정보를 가지고 방으로 이동함
}