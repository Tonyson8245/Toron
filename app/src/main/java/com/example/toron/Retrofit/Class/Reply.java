package com.example.toron.Retrofit.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reply {
    @Expose
    @SerializedName("content") private String content;
    @Expose
    @SerializedName("datetime") private String datetime;
    @Expose
    @SerializedName("user_nickname") private String user_nickname;
    @Expose
    @SerializedName("user_profile_img") private String user_profile_img;

    @Override
    public String toString() {
        return "Reply{" +
                "content='" + content + '\'' +
                ", datetime='" + datetime + '\'' +
                ", user_nickname='" + user_nickname + '\'' +
                ", user_profile_img='" + user_profile_img + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_profile_img() {
        return user_profile_img;
    }

    public void setUser_profile_img(String user_profile_img) {
        this.user_profile_img = user_profile_img;
    }

    public Reply(String content, String datetime, String user_nickname, String user_profile_img) {
        this.content = content;
        this.datetime = datetime;
        this.user_nickname = user_nickname;
        this.user_profile_img = user_profile_img;
    }
}
