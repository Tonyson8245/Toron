package com.example.toron.Retrofit.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class New_article {
    @Expose
    @SerializedName("news_href") private String news_href;
    @Expose
    @SerializedName("news_title") private String news_title;
    @Expose
    @SerializedName("news_writing") private String news_writing;
    @Expose
    @SerializedName("news_datetime") private String news_datetime;
    @Expose
    @SerializedName("news_img") private String news_img;


    @Override
    public String toString() {
        return "New_article{" +
                "news_href='" + news_href + '\'' +
                ", news_title='" + news_title + '\'' +
                ", news_writing='" + news_writing + '\'' +
                ", news_datetime='" + news_datetime + '\'' +
                ", news_img='" + news_img + '\'' +
                '}';
    }

    public String getNews_href() {
        return news_href;
    }

    public void setNews_href(String news_href) {
        this.news_href = news_href;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_writing() {
        return news_writing;
    }

    public void setNews_writing(String news_writing) {
        this.news_writing = news_writing;
    }

    public String getNews_datetime() {
        return news_datetime;
    }

    public void setNews_datetime(String news_datetime) {
        this.news_datetime = news_datetime;
    }

    public String getNews_img() {
        return news_img;
    }

    public void setNews_img(String news_img) {
        this.news_img = news_img;
    }

    public New_article(String news_href, String news_title, String news_writing, String news_datetime, String news_img) {
        this.news_href = news_href;
        this.news_title = news_title;
        this.news_writing = news_writing;
        this.news_datetime = news_datetime;
        this.news_img = news_img;
    }
}
