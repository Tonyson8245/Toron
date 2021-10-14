package com.example.toron.Service.Class;

public class Room_article_data {
    String room_idx;
    String room_subject;
    String room_description;
    String article_idx;
    String article_side;
    String article_href;
    String article_subject;
    String side;

    public String getRoom_idx() {
        return room_idx;
    }

    public void setRoom_idx(String room_idx) {
        this.room_idx = room_idx;
    }

    public String getRoom_subject() {
        return room_subject;
    }

    public void setRoom_subject(String room_subject) {
        this.room_subject = room_subject;
    }

    public String getRoom_description() {
        return room_description;
    }

    public void setRoom_description(String room_description) {
        this.room_description = room_description;
    }

    public String getArticle_idx() {
        return article_idx;
    }

    public void setArticle_idx(String article_idx) {
        this.article_idx = article_idx;
    }

    public String getArticle_side() {
        return article_side;
    }

    public void setArticle_side(String article_side) {
        this.article_side = article_side;
    }

    public String getArticle_href() {
        return article_href;
    }

    public void setArticle_href(String article_href) {
        this.article_href = article_href;
    }

    public String getArticle_subject() {
        return article_subject;
    }

    public void setArticle_subject(String article_subject) {
        this.article_subject = article_subject;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Room_article_data(String room_idx, String room_subject, String room_description, String article_idx, String article_side, String article_href, String article_subject, String side) {
        this.room_idx = room_idx;
        this.room_subject = room_subject;
        this.room_description = room_description;
        this.article_idx = article_idx;
        this.article_side = article_side;
        this.article_href = article_href;
        this.article_subject = article_subject;
        this.side = side;
    }
}
