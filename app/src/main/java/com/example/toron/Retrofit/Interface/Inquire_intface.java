package com.example.toron.Retrofit.Interface;

import com.example.toron.Retrofit.Class.Image_upload;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Inquire_intface {
    //사용자가 프로필 이미지를 변경했을때 해당 이미지를 서버로 전송하는 통신
    @Multipart
    @POST("api/image/upload_img.php")
    Call<Image_upload> uploadImage(@Part MultipartBody.Part File);

    @Multipart
    @POST("api/image/upload_chat_img.php")
    Call<Image_upload> uploadChatImage(@Part MultipartBody.Part File);
}
