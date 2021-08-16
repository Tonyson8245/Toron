package com.example.toron.Retrofit.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Reply_response {
    @Expose
    @SerializedName("result") String result;
    @Expose
    @SerializedName("total") String total;
    @Expose
    @SerializedName("reply")
    List<Reply> reply;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Reply> getReply() {
        return reply;
    }

    public void setReply(List<Reply> reply) {
        this.reply = reply;
    }

    public Reply_response(String result, String total, List<Reply> reply) {
        this.result = result;
        this.total = total;
        this.reply = reply;
    }
}
