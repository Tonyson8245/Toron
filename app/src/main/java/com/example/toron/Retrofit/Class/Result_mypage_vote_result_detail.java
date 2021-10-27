package com.example.toron.Retrofit.Class;

import com.google.gson.JsonElement;

public class Result_mypage_vote_result_detail {
   String result;
   Vote_result_detail message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Vote_result_detail getMessage() {
        return message;
    }

    public void setMessage(Vote_result_detail message) {
        this.message = message;
    }

    public Result_mypage_vote_result_detail(String result, Vote_result_detail message) {
        this.result = result;
        this.message = message;
    }
}