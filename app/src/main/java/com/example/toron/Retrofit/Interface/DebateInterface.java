package com.example.toron.Retrofit.Interface;

import com.example.toron.Retrofit.Class.Room_data;
import com.example.toron.Retrofit.Class.Room_idx;
import com.example.toron.Retrofit.Class.Userdata_response;
import com.example.toron.Retrofit.Class.Yesno;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DebateInterface {
    @GET("api/Debate/Insert_Room_list.php")
    Call<Room_idx> insert_room_list(
            @Query("room_subject") String room_subject,
            @Query("room_description") String description,
            @Query("room_category") String category,
            @Query("user_idx") String user_idx
            );

    @FormUrlEncoded
    @POST("api/Debate/Select_Room_data.php")
    Call<Room_data> Select_Room_data(
            @Field("room_idx") String room_idx);


    @GET("api/Debate/Insert_Participate_list.php")
    Call<Yesno> insert_Participate_list(
            @Query("room_idx") String room_idx,
            @Query("user_idx") String user_idx,
            @Query("side") String side
    );
}
