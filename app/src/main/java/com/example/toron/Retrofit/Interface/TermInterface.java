package com.example.toron.Retrofit.Interface;

import android.icu.lang.UScript;

import com.example.toron.Retrofit.Class.Term_data;
import com.example.toron.Retrofit.Class.Userdata_response;
import com.example.toron.Retrofit.Class.Userinfo;
import com.example.toron.Retrofit.Class.Yesno;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TermInterface {
    @GET("api/term.php")
    Call<Term_data> get_term(
            @Query("type") String type);

}
