package com.example.toron.Retrofit.Class;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Userinfo {

    @Expose
    @SerializedName("user_id") private String user_id;

    @Expose
    @SerializedName("user_nickname") private String user_nickname;

    @Expose
    @SerializedName("user_password") private String user_password;

    @Expose
    @SerializedName("user_phonenum") private String user_phonenum;

    @Override
    public String toString() {
        return "Userdata{" +
                "user_id='" + user_id + '\'' +
                ", user_nickname='" + user_nickname + '\'' +
                ", user_password='" + user_password + '\'' +
                ", user_phonenum='" + user_phonenum + '\'' +
                '}';
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_phonenum() {
        return user_phonenum;
    }

    public void setUser_phonenum(String user_phonenum) {
        this.user_phonenum = user_phonenum;
    }

    public Userinfo(String user_id, String user_nickname, String user_password, String user_phonenum) {
        this.user_id = user_id;
        this.user_nickname = user_nickname;
        this.user_password = user_password;
        this.user_phonenum = user_phonenum;
    }
}
