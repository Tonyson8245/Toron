package com.example.toron.Retrofit.Class;

import java.util.ArrayList;

public class Result_mypage_vote_result_list {
   String result;
   ArrayList<Mypage_vote_result_list> message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<Mypage_vote_result_list> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<Mypage_vote_result_list> message) {
        this.message = message;
    }

    public Result_mypage_vote_result_list(String result, ArrayList<Mypage_vote_result_list> message) {
        this.result = result;
        this.message = message;
    }
}