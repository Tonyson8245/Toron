package com.example.toron.Retrofit.Interface;

import com.example.toron.Retrofit.Class.Result;
import com.example.toron.Retrofit.Class.Room_data;
import com.example.toron.Retrofit.Class.Room_idx;
import com.example.toron.Retrofit.Class.Vote_data;
import com.example.toron.Retrofit.Class.Vote_detail;
import com.example.toron.Retrofit.Class.Vote_list_data;
import com.example.toron.Retrofit.Class.Vote_result;
import com.example.toron.Retrofit.Class.Yesno;
import com.example.toron.Service.Class.Chat;
import com.example.toron.Service.Class.Response;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface VoteInterface {
    @GET("api/Vote/Select_vote_list.php")
    Call<Vote_result> Select_vote_list(
            @Query("user_idx") String user_idx
            );

    @GET("api/Vote/Select_vote_detatil.php")
    Call<Vote_detail> Select_vote_detail(
            @Query("vote_idx") String vote_idx
    );


    @GET("api/Vote/Select_chat_list.php")
    Call<ArrayList<Chat>> Select_chat_list(
            @Query("room_idx") String room_idx
    );

    @GET("api/Vote/Vote.php")
    Call<Result> Vote(
            @Query("user_idx") String user_idx,
            @Query("vote_idx") String vote_idx,
            @Query("side") String side
    );
}
