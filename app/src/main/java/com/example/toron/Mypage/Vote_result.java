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

import com.example.toron.Adapter.Mypage_vote_result_Adapter;
import com.example.toron.Adapter.Participate_vote_recyclerAdapter;
import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Mypage_participage_vote;
import com.example.toron.Retrofit.Class.Mypage_vote_result_list;
import com.example.toron.Retrofit.Class.Result_mypage_vote_list;
import com.example.toron.Retrofit.Class.Result_mypage_vote_result_list;
import com.example.toron.Retrofit.Interface.MypageInterface;
import com.example.toron.Vote.Vote_history;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Vote_result extends AppCompatActivity {

    Button btn_back;
    RecyclerView recyclerView;
    Mypage_vote_result_Adapter adapter;
    TextView tv_no_data;
    String user_idx,TAG = "Vote_result";
    ArrayList<Mypage_vote_result_list> list = new ArrayList<>();
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_result);

        btn_back = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recyclerview);
        tv_no_data = findViewById(R.id.tv_no_data);

        SharedPreferences sharedPreferences;
        sharedPreferences = this.getSharedPreferences("user_data",0);
        user_idx = sharedPreferences.getString("user_idx",null);


        recyclerView.setHasFixedSize(true);
        adapter = new Mypage_vote_result_Adapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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
        get_vote_result_list(user_idx);
    }

    private void get_vote_result_list(String user_idx) {
        MypageInterface mypageInterface = ApiClient.getApiClient().create(MypageInterface.class);
        Call<Result_mypage_vote_result_list> call = mypageInterface.Select_vote_result_list(user_idx);
        call.enqueue(new Callback<Result_mypage_vote_result_list>() {
            @Override
            public void onResponse(Call<Result_mypage_vote_result_list> call, Response<Result_mypage_vote_result_list> response) {
                if(response.body()!=null && response.isSuccessful()){
                    Result_mypage_vote_result_list temp_result = response.body();
                    Log.d(TAG,response.body().toString());

                    if(temp_result.getResult().equals("success")){
                        list = temp_result.getMessage();

                        adapter.setList(list);

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
            public void onFailure(Call<Result_mypage_vote_result_list> call, Throwable t) {

            }
        });

    }

    public void moveToResultDetail(String vote_idx,String start_data,String end_date) {
        Intent result_detail = new Intent(this, Vote_result_detail.class);
        result_detail.putExtra("vote_idx",vote_idx);
        result_detail.putExtra("start_date",start_data);
        result_detail.putExtra("end_date",end_date);
        startActivity(result_detail);
    }
}