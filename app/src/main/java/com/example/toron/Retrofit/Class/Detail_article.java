package com.example.toron.Retrofit.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Detail_article {
    @Expose
    @SerializedName("result") private String result;
    @Expose
    @SerializedName("text") private String text;

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

    public Detail_article(String result, String text) {
        this.result = result;
        this.text = text;
    }
}
