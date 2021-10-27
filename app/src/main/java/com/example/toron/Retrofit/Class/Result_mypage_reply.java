package com.example.toron.Retrofit.Class;

import java.util.ArrayList;

public class Result_mypage_reply {
   String result;
   ArrayList<Mypage_reply> message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<Mypage_reply> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<Mypage_reply> message) {
        this.message = message;
    }

    public Result_mypage_reply(String result, ArrayList<Mypage_reply> message) {
        this.result = result;
        this.message = message;
    }
}