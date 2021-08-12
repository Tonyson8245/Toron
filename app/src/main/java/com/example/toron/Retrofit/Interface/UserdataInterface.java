package com.example.toron.Retrofit.Interface;

import android.icu.lang.UScript;

import com.example.toron.Retrofit.Class.Userdata_response;
import com.example.toron.Retrofit.Class.Userinfo;
import com.example.toron.Retrofit.Class.Yesno;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserdataInterface {
    @GET("api/overlay_check.php")
    Call<Yesno> get_Useridoverlay(
            @Query("user_id") String id);

    @GET("api/overlay_check.php")
    Call<Yesno> get_Usernicknameoverlay(
            @Query("user_nickname") String nickname);

    @FormUrlEncoded
    @POST("api/join.php")
    Call<Userdata_response> insertUserdata(
            @Field("user_id") String user_id,
            @Field("user_birthday") String user_birthday,
            @Field("user_password") String user_password,
            @Field("user_nickname") String user_nickname,
            @Field("user_phonenum") String user_phonenum);

    @GET("api/find_account.php")
    Call<Userdata_response> get_Userid(
            @Query("type") String type,
            @Query("user_phonenum") String user_phonenum,
            @Query("user_birthday") String user_birthday);

    @GET("api/find_account.php")
    Call<Userdata_response> get_Userpw(
            @Query("type") String type,
            @Query("user_id") String user_id,
            @Query("user_phonenum") String user_phonenum);

    @FormUrlEncoded
    @POST("api/password_change.php")
    Call<Userdata_response> password_change(
            @Field("user_id") String user_id,
            @Field("user_password") String user_password);

    @FormUrlEncoded
    @POST("api/profile_update.php")
    Call<Userdata_response> updateUserprofile(
            @Field("user_id") String user_id,
            @Field("user_birthday") String user_birthday,
            @Field("user_nickname") String user_nickname);
}
