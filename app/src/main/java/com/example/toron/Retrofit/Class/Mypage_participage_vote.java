package com.example.toron.Retrofit.Class;

public class Mypage_participage_vote {
    String vote_idx;
    String room_subject;
    String vote_datetime;
    String side;

    public String getVote_idx() {
        return vote_idx;
    }

    public void setVote_idx(String vote_idx) {
        this.vote_idx = vote_idx;
    }

    public String getRoom_subject() {
        return room_subject;
    }

    public void setRoom_subject(String room_subject) {
        this.room_subject = room_subject;
    }

    public String getVote_datetime() {
        return vote_datetime;
    }

    public void setVote_datetime(String vote_datetime) {
        this.vote_datetime = vote_datetime;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Mypage_participage_vote(String vote_idx, String room_subject, String vote_datetime, String side) {
        this.vote_idx = vote_idx;
        this.room_subject = room_subject;
        this.vote_datetime = vote_datetime;
        this.side = side;
    }
}
