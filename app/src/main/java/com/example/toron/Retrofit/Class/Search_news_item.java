package com.example.toron.Retrofit.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Search_news_item {
    @Expose
    @SerializedName("title") String title;
    @Expose
    @SerializedName("originallink") String originallink;
    @Expose
    @SerializedName("link") String link;
    @Expose
    @SerializedName("description") String description;
    @Expose
    @SerializedName("pubDate") String pubDate;

    String img_url = "";

    @Override
    public String toString() {
        return "Search_news_item{" +
                "title='" + title + '\'' +
                ", originallink='" + originallink + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", img_url='" + img_url + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginallink() {
        return originallink;
    }

    public void setOriginallink(String originallink) {
        this.originallink = originallink;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public Search_news_item(String title, String originallink, String link, String description, String pubDate, String img_url) {
        this.title = title;
        this.originallink = originallink;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.img_url = img_url;
    }
}
