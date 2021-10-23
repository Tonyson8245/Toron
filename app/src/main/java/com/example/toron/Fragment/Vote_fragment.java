package com.example.toron.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toron.Adapter.RoomRecyclerAdapter;
import com.example.toron.Adapter.VoteRecyclerAdapter;
import com.example.toron.Debate.Debate_make;
import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Vote_list_data;
import com.example.toron.Retrofit.Class.Vote_result;
import com.example.toron.Retrofit.Interface.VoteInterface;
import com.example.toron.Service.Class.Room_data;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Vote_fragment extends Fragment {

    private RecyclerView recyclerView;
    private VoteRecyclerAdapter voteRecyclerAdapter;
    String TAG = "Vote_fragment",user_id,user_idx,user_nickname;
    Gson gson = new Gson();
    ArrayList<Vote_list_data> list = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_vote_fragment, container, false);

        SharedPreferences sharedPreferences;
        sharedPreferences = getContext().getSharedPreferences("user_data",0);
        user_id = sharedPreferences.getString("user_id",null);
        user_idx = sharedPreferences.getString("user_idx",null);
        user_nickname = sharedPreferences.getString("user_nickname","nick");


        recyclerView = (RecyclerView) rootView.findViewById(R.id.vote_recyclerview);

        recyclerView.setHasFixedSize(true);
        voteRecyclerAdapter = new VoteRecyclerAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(voteRecyclerAdapter);

        setHasOptionsMenu(true);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(-1)) {
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        get_voteList();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void get_voteList(){
        VoteInterface voteInterface = ApiClient.getApiClient().create(VoteInterface.class);
        Call<Vote_result> call = voteInterface.Select_vote_list(user_idx);
        call.enqueue(new Callback<Vote_result>() {
            @Override
            public void onResponse(Call<Vote_result> call, Response<Vote_result> response) {
                if(response.body()!=null && response.isSuccessful()){
                    if(response.body().getResult().equals("success")){
                        Log.d(TAG, gson.toJson(response.body().getData()));

                        list = response.body().getData();

                        voteRecyclerAdapter.set_voteList(list);
                        voteRecyclerAdapter.notifyDataSetChanged();
                    }
                    else Toast.makeText(getContext(),"현재 투표가 없습니다.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Vote_result> call, Throwable t) {

            }
        });
    }


}