package com.example.toron.Retrofit.Interface;

import com.example.toron.Retrofit.Class.Article_list;
import com.example.toron.Retrofit.Class.Detail_article;

import retrofit2.Call;
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
}
