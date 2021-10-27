package com.example.toron.Retrofit.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Result_mypage_vote_list {
   String result;
   ArrayList<Mypage_participage_vote> message;

    @Override
    public String toString() {
        return "Result_mypage_vote_list{" +
                "result='" + result + '\'' +
                ", message=" + message +
                '}';
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<Mypage_participage_vote> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<Mypage_participage_vote> message) {
        this.message = message;
    }

    public Result_mypage_vote_list(String result, ArrayList<Mypage_participage_vote> message) {
        this.result = result;
        this.message = message;
    }
}