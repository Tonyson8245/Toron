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
    @Expose
    @SerializedName("reply_status") private String reply_status;
    @Expose
    @SerializedName("reply_idx") private String reply_idx;
    @Expose
    @SerializedName("reply_like_qty") private String reply_like_qty;
    @Expose
    @SerializedName("reply_like_user_id") private String reply_like_user_id;

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

    public String getReply_status() {
        return reply_status;
    }

    public void setReply_status(String reply_status) {
        this.reply_status = reply_status;
    }

    public String getReply_idx() {
        return reply_idx;
    }

    public void setReply_idx(String reply_idx) {
        this.reply_idx = reply_idx;
    }

    public String getReply_like_qty() {
        return reply_like_qty;
    }

    public void setReply_like_qty(String reply_like_qty) {
        this.reply_like_qty = reply_like_qty;
    }

    public String getReply_like_user_id() {
        return reply_like_user_id;
    }

    public void setReply_like_user_id(String reply_like_user_id) {
        this.reply_like_user_id = reply_like_user_id;
    }

    public Reply(String content, String datetime, String user_nickname, String user_profile_img, String reply_status, String reply_idx, String reply_like_qty, String reply_like_user_id) {
        this.content = content;
        this.datetime = datetime;
        this.user_nickname = user_nickname;
        this.user_profile_img = user_profile_img;
        this.reply_status = reply_status;
        this.reply_idx = reply_idx;
        this.reply_like_qty = reply_like_qty;
        this.reply_like_user_id = reply_like_user_id;
    }
}

