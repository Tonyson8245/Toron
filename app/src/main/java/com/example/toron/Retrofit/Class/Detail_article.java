package com.example.toron.Retrofit.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Detail_article {
    @Expose
    @SerializedName("result") private String result;
    @Expose
    @SerializedName("text") private String text;
    @Expose
    @SerializedName("news_idx") private String news_idx;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNews_idx() {
        return news_idx;
    }

    public void setNews_idx(String news_idx) {
        this.news_idx = news_idx;
    }

    public Detail_article(String result, String text, String news_idx) {
        this.result = result;
        this.text = text;
        this.news_idx = news_idx;
    }
}
