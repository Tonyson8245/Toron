package com.example.toron.Retrofit.Class;

public class Mypage_vote_result_list {
    String vote_idx;
    String room_idx;
    String start_datetime;
    String end_datetime;
    String room_subject;
    String room_description;

    public String getVote_idx() {
        return vote_idx;
    }

    public void setVote_idx(String vote_idx) {
        this.vote_idx = vote_idx;
    }

    public String getRoom_idx() {
        return room_idx;
    }

    public void setRoom_idx(String room_idx) {
        this.room_idx = room_idx;
    }

    public String getStart_datetime() {
        return start_datetime;
    }

    public void setStart_datetime(String start_datetime) {
        this.start_datetime = start_datetime;
    }

    public String getEnd_datetime() {
        return end_datetime;
    }

    public void setEnd_datetime(String end_datetime) {
        this.end_datetime = end_datetime;
    }

    public String getRoom_subject() {
        return room_subject;
    }

    public void setRoom_subject(String room_subject) {
        this.room_subject = room_subject;
    }

    public String getRoom_description() {
        return room_description;
    }

    public void setRoom_description(String room_description) {
        this.room_description = room_description;
    }

    public Mypage_vote_result_list(String vote_idx, String room_idx, String start_datetime, String end_datetime, String room_subject, String room_description) {
        this.vote_idx = vote_idx;
        this.room_idx = room_idx;
        this.start_datetime = start_datetime;
        this.end_datetime = end_datetime;
        this.room_subject = room_subject;
        this.room_description = room_description;
    }
}
