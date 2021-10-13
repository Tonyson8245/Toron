package com.example.toron.Service.Class;

public class Chat {
    String chat_idx;
    String room_idx;
    String msg;
    String user_idx;
    String datetime;
    String side;
    String nickname;
    String tag_user_idx;

    public String getChat_idx() {
        return chat_idx;
    }

    public void setChat_idx(String chat_idx) {
        this.chat_idx = chat_idx;
    }

    public String getRoom_idx() {
        return room_idx;
    }

    public void setRoom_idx(String room_idx) {
        this.room_idx = room_idx;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUser_idx() {
        return user_idx;
    }

    public void setUser_idx(String user_idx) {
        this.user_idx = user_idx;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTag_user_idx() {
        return tag_user_idx;
    }

    public void setTag_user_idx(String tag_user_idx) {
        this.tag_user_idx = tag_user_idx;
    }

    public Chat(String chat_idx, String room_idx, String msg, String user_idx, String datetime, String side, String nickname, String tag_user_idx) {
        this.chat_idx = chat_idx;
        this.room_idx = room_idx;
        this.msg = msg;
        this.user_idx = user_idx;
        this.datetime = datetime;
        this.side = side;
        this.nickname = nickname;
        this.tag_user_idx = tag_user_idx;
    }
}
