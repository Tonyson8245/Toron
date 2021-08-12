package com.example.toron.Retrofit.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Userdata_response{
    @Expose
    @SerializedName("result") private String result;

    @Expose
    @SerializedName("data") private Userinfo userinfo;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }

    public Userdata_response(String result, Userinfo userinfo) {
        this.result = result;
        this.userinfo = userinfo;
    }
}
