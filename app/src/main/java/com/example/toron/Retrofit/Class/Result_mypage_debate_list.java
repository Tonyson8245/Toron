package com.example.toron.Retrofit.Class;

import java.util.ArrayList;

public class Result_mypage_debate_list {
   String result;
   ArrayList<Mypage_participate_debate> message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<Mypage_participate_debate> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<Mypage_participate_debate> message) {
        this.message = message;
    }

    public Result_mypage_debate_list(String result, ArrayList<Mypage_participate_debate> message) {
        this.result = result;
        this.message = message;
    }
}