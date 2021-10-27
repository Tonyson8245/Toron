package com.example.toron.Retrofit.Class;

public class Mypage_reply {
    String news_title;
    String reply_idx;
    String reply_datetime;
    String reply_content;
    String news_idx;
    String href;
    String news_img;
    String news_writing;
    String news_datetime;

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getReply_idx() {
        return reply_idx;
    }

    public void setReply_idx(String reply_idx) {
        this.reply_idx = reply_idx;
    }

    public String getReply_datetime() {
        return reply_datetime;
    }

    public void setReply_datetime(String reply_datetime) {
        this.reply_datetime = reply_datetime;
    }

    public String getReply_content() {
        return reply_content;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }

    public String getNews_idx() {
        return news_idx;
    }

    public void setNews_idx(String news_idx) {
        this.news_idx = news_idx;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getNews_img() {
        return news_img;
    }

    public void setNews_img(String news_img) {
        this.news_img = news_img;
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

    public Mypage_reply(String news_title, String reply_idx, String reply_datetime, String reply_content, String news_idx, String href, String title, String news_img, String news_writing, String news_datetime) {
        this.news_title = news_title;
        this.reply_idx = reply_idx;
        this.reply_datetime = reply_datetime;
        this.reply_content = reply_content;
        this.news_idx = news_idx;
        this.href = href;
        this.news_img = news_img;
        this.news_writing = news_writing;
        this.news_datetime = news_datetime;
    }
}
