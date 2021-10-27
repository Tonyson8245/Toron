package com.example.toron.Retrofit.Class;

public class Mypage_participate_debate {
    String room_idx;
    String room_subject;
    String side;
    String datetime;
    String status;

    public Mypage_participate_debate(String room_idx, String room_subject, String side, String datetime, String status) {
        this.room_idx = room_idx;
        this.room_subject = room_subject;
        this.side = side;
        this.datetime = datetime;
        this.status = status;
    }

    public String getRoom_idx() {
        return room_idx;
    }

    public void setRoom_idx(String room_idx) {
        this.room_idx = room_idx;
    }

    public String getRoom_subject() {
        return room_subject;
    }

    public void setRoom_subject(String room_subject) {
        this.room_subject = room_subject;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
