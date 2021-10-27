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

import com.example.toron.Adapter.NewsRecyclerAdapter;
import com.example.toron.Adapter.Participate_vote_recyclerAdapter;
import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Result;
import com.example.toron.Retrofit.Class.Mypage_participage_vote;
import com.example.toron.Retrofit.Class.Result_mypage_vote_list;
import com.example.toron.Retrofit.Interface.MypageInterface;
import com.example.toron.Service.Class.Room_data;
import com.example.toron.Vote.Vote_history;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Participate_vote extends AppCompatActivity {

    Button btn_back;
    RecyclerView vote_list_recyclerview;
    Participate_vote_recyclerAdapter participate_vote_recyclerAdapter;
    TextView tv_no_data;
    String user_idx,TAG = "Participate_vote";
    ArrayList<Mypage_participage_vote> list = new ArrayList<>();
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participate_vote);

        btn_back = findViewById(R.id.btn_back);
        vote_list_recyclerview = findViewById(R.id.participate_vote_list);
        tv_no_data = findViewById(R.id.tv_no_data);

        SharedPreferences sharedPreferences;
        sharedPreferences = this.getSharedPreferences("user_data",0);
        user_idx = sharedPreferences.getString("user_idx",null);


        vote_list_recyclerview.setHasFixedSize(true);
        participate_vote_recyclerAdapter = new Participate_vote_recyclerAdapter(this);
        vote_list_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        vote_list_recyclerview.setAdapter(participate_vote_recyclerAdapter);

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
        get_vote_list(user_idx);
    }

    private void get_vote_list(String user_idx) {
        MypageInterface mypageInterface = ApiClient.getApiClient().create(MypageInterface.class);
        Call<Result_mypage_vote_list> call = mypageInterface.Select_vote_list(user_idx);
        call.enqueue(new Callback<Result_mypage_vote_list>() {
            @Override
            public void onResponse(Call<Result_mypage_vote_list> call, Response<Result_mypage_vote_list> response) {
                if(response.body()!=null && response.isSuccessful()){
                    Result_mypage_vote_list temp_result = response.body();
                    Log.d(TAG,response.body().toString());
                    if(temp_result.getResult().equals("success")){
                        list = temp_result.getMessage();
                        participate_vote_recyclerAdapter.setList(list);

                        vote_list_recyclerview.setVisibility(View.VISIBLE);
                        tv_no_data.setVisibility(View.GONE);
                    }
                    else if(temp_result.getResult().equals("failed")){
                        vote_list_recyclerview.setVisibility(View.GONE);
                        tv_no_data.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result_mypage_vote_list> call, Throwable t) {
                Log.d(TAG,t.getMessage());
            }
        });
    }

    public void moveTovote(String vote_idx,String side) {
        Intent vote_detail = new Intent(this, Vote_history.class);
        vote_detail.putExtra("status","finished");
        vote_detail.putExtra("side",side);
        vote_detail.putExtra("vote_idx",vote_idx);
        startActivity(vote_detail);
    }
}