package com.example.toron.Retrofit.Class;

public class Vote_data {
    String vote_subject;
    String vote_description;
    String room_idx;

    public String getVote_subject() {
        return vote_subject;
    }

    public void setVote_subject(String vote_subject) {
        this.vote_subject = vote_subject;
    }

    public String getVote_description() {
        return vote_description;
    }

    public void setVote_description(String vote_description) {
        this.vote_description = vote_description;
    }

    public String getRoom_idx() {
        return room_idx;
    }

    public void setRoom_idx(String room_idx) {
        this.room_idx = room_idx;
    }

    public Vote_data(String vote_subject, String vote_description, String room_idx) {
        this.vote_subject = vote_subject;
        this.vote_description = vote_description;
        this.room_idx = room_idx;
    }
}
