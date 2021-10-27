package com.example.toron.Retrofit.Class;

public class Vote_result_detail {
    String room_subject;
    String room_description;
    String vote_pro_qty;
    String vote_con_qty;

    public Vote_result_detail(String room_subject, String room_description, String vote_pro_qty, String vote_con_qty) {
        this.room_subject = room_subject;
        this.room_description = room_description;
        this.vote_pro_qty = vote_pro_qty;
        this.vote_con_qty = vote_con_qty;
    }

    @Override
    public String toString() {
        return "Vote_result_detail{" +
                "room_subject='" + room_subject + '\'' +
                ", room_description='" + room_description + '\'' +
                ", vote_pro_qty='" + vote_pro_qty + '\'' +
                ", vote_con_qty='" + vote_con_qty + '\'' +
                '}';
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

    public String getVote_pro_qty() {
        return vote_pro_qty;
    }

    public void setVote_pro_qty(String vote_pro_qty) {
        this.vote_pro_qty = vote_pro_qty;
    }

    public String getVote_con_qty() {
        return vote_con_qty;
    }

    public void setVote_con_qty(String vote_con_qty) {
        this.vote_con_qty = vote_con_qty;
    }
}
