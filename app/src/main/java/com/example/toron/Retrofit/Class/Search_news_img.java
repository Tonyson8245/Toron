package com.example.toron.Retrofit.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Search_news_img {
    @Expose
    @SerializedName("result") String result;
    @Expose
    @SerializedName("img_url") String img_url;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    @Override
    public String toString() {
        return "Search_news_img{" +
                "result='" + result + '\'' +
                ", img_url='" + img_url + '\'' +
                '}';
    }

    public Search_news_img(String result, String img_url) {
        this.result = result;
        this.img_url = img_url;
    }
}
