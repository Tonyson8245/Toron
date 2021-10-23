package com.example.toron.Retrofit.Class;

import java.util.ArrayList;

public class Vote_detail {
    Vote_data vote_data;
    ArrayList<Vote_Top_chat> vote_top_chat;
    ArrayList<New_article> pro;
    ArrayList<New_article> con;


    @Override
    public String toString() {
        return "Vote_detail{" +
                "vote_data=" + vote_data +
                ", vote_top_chat=" + vote_top_chat +
                ", pro=" + pro +
                ", con=" + con +
                '}';
    }

    public Vote_data getVote_data() {
        return vote_data;
    }

    public void setVote_data(Vote_data vote_data) {
        this.vote_data = vote_data;
    }

    public ArrayList<Vote_Top_chat> getVote_top_chat() {
        return vote_top_chat;
    }

    public void setVote_top_chat(ArrayList<Vote_Top_chat> vote_top_chat) {
        this.vote_top_chat = vote_top_chat;
    }

    public ArrayList<New_article> getPro() {
        return pro;
    }

    public void setPro(ArrayList<New_article> pro) {
        this.pro = pro;
    }

    public ArrayList<New_article> getCon() {
        return con;
    }

    public void setCon(ArrayList<New_article> con) {
        this.con = con;
    }

    public Vote_detail(Vote_data vote_data, ArrayList<Vote_Top_chat> vote_top_chat, ArrayList<New_article> pro, ArrayList<New_article> con) {
        this.vote_data = vote_data;
        this.vote_top_chat = vote_top_chat;
        this.pro = pro;
        this.con = con;
    }
}
