package com.example.toron.Retrofit.Interface;

import com.example.toron.Retrofit.Class.Article_list;
import com.example.toron.Retrofit.Class.Detail_article;
import com.example.toron.Retrofit.Class.Reply_response;
import com.example.toron.Retrofit.Class.Result;
import com.example.toron.Retrofit.Class.Search_news_img;
import com.example.toron.Retrofit.Class.Search_news_response;
import com.example.toron.Retrofit.Class.Yesno;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NewsInterface {
    @GET("api/News/Get_news.php")
    Call<Article_list> get_News(
            @Query("page") String page);

    @GET("api/News/News_detail.php")
    Call<Detail_article> get_Detail_news(
            @Query("url") String url);

    @GET("api/News/Naver_news.php")
    Call<Search_news_response> get_Search_News(
            @Query("keyword") String keyword,
            @Query("start") String start);

    @GET("api/News/Get_news_img.php")
    Call<Search_news_img> get_Search_News_img(
            @Query("url") String url
    );

    @FormUrlEncoded
    @POST("api/News/Reply/Insert_reply.php")
    Call<Result> insert_reply(
            @Field("user_id") String user_id,
            @Field("content") String content,
            @Field("news_idx") String news_idx
    );

    @GET("api/News/Reply/Get_reply.php")
    Call<Reply_response> get_reply(
            @Query("news_idx") String news_idx,
            @Query("sort") String sort,
            @Query("page") String page
    );
}
