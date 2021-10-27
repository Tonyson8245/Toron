package com.example.toron.Retrofit.Interface;

import com.example.toron.Retrofit.Class.Result;
import com.example.toron.Retrofit.Class.Result_mypage_debate_list;
import com.example.toron.Retrofit.Class.Result_mypage_reply;
import com.example.toron.Retrofit.Class.Result_mypage_vote_list;
import com.example.toron.Retrofit.Class.Result_mypage_vote_result_detail;
import com.example.toron.Retrofit.Class.Result_mypage_vote_result_list;
import com.example.toron.Retrofit.Class.Vote_detail;
import com.example.toron.Retrofit.Class.Vote_result;
import com.example.toron.Retrofit.Class.Vote_result_detail;
import com.example.toron.Service.Class.Chat;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MypageInterface {
    @GET("api/Mypage/Read_user_pariticipate_vote.php")
    Call<Result_mypage_vote_list> Select_vote_list(
            @Query("user_idx") String user_idx
            );

    @GET("api/Mypage/Read_participate_debate.php")
    Call<Result_mypage_debate_list> Select_debate_list(
            @Query("user_idx") String user_idx
    );

    @GET("api/Mypage/Read_news_reply.php")
    Call<Result_mypage_reply> Select_reply_list(
            @Query("user_idx") String user_idx
    );

    @GET("api/Mypage/Read_vote_result.php")
    Call<Result_mypage_vote_result_list> Select_vote_result_list(
            @Query("user_idx") String user_idx
    );

    @GET("api/Mypage/Read_vote_result_detail1.php")
    Call<Result_mypage_vote_result_detail> Select_vote_result_detail(
            @Query("vote_idx") String vote_idx
    );

}

