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

import com.example.toron.Adapter.Participate_debate_recyclerAdapter;
import com.example.toron.Debate.Debate_room;
import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Mypage_participate_debate;
import com.example.toron.Retrofit.Class.Result;
import com.example.toron.Retrofit.Class.Result_mypage_debate_list;
import com.example.toron.Retrofit.Interface.MypageInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Participate_debate extends AppCompatActivity {

    Button btn_back;
    RecyclerView recyclerView;
    Participate_debate_recyclerAdapter participate_debate_recyclerAdapter;
    TextView tv_no_data;
    String user_idx,TAG = "Participate_vote";
    ArrayList<Mypage_participate_debate> list = new ArrayList<>();
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participate_debate);

        btn_back = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.participate_debate_list);
        tv_no_data = findViewById(R.id.tv_no_data);

        SharedPreferences sharedPreferences;
        sharedPreferences = this.getSharedPreferences("user_data",0);
        user_idx = sharedPreferences.getString("user_idx",null);


        recyclerView.setHasFixedSize(true);
        participate_debate_recyclerAdapter = new Participate_debate_recyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(participate_debate_recyclerAdapter);

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
        get_debate_list(user_idx);
    }

    private void get_debate_list(String user_idx) {
        MypageInterface mypageInterface = ApiClient.getApiClient().create(MypageInterface.class);
        Call<Result_mypage_debate_list> call = mypageInterface.Select_debate_list(user_idx);
        call.enqueue(new Callback<Result_mypage_debate_list>() {
            @Override
            public void onResponse(Call<Result_mypage_debate_list> call, Response<Result_mypage_debate_list> response) {
                if(response.body()!=null && response.isSuccessful()){
                    Result_mypage_debate_list temp_result = response.body();

                    Log.d(TAG,response.body().toString());
                    if(temp_result.getResult().equals("success")){
                        list = temp_result.getMessage();
                        participate_debate_recyclerAdapter.setList(list);

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
            public void onFailure(Call<Result_mypage_debate_list> call, Throwable t) {

            }
        });
    }


    public void moveToDebate(String room_idx,String side,String status) {
        Intent room = new Intent(this, Debate_room.class);
        room.putExtra("status",status);
        room.putExtra("side",side);
        room.putExtra("room_idx",room_idx);
        startActivity(room);
    }
}