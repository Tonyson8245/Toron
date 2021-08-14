package com.example.toron.Retrofit.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Search_news_response {
    @Expose
    @SerializedName("lastBuildDate") String lastBuildDate;
    @Expose
    @SerializedName("total") String total;
    @Expose
    @SerializedName("start") String start;
    @Expose
    @SerializedName("display") String display;
    @Expose
    @SerializedName("items")
    List<Search_news_item> items;

    @Override
    public String toString() {
        return "Search_news_response{" +
                "lastBuildDate='" + lastBuildDate + '\'' +
                ", total='" + total + '\'' +
                ", start='" + start + '\'' +
                ", display='" + display + '\'' +
                ", items=" + items +
                '}';
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public List<Search_news_item> getItems() {
        return items;
    }

    public void setItems(List<Search_news_item> items) {
        this.items = items;
    }

    public Search_news_response(String lastBuildDate, String total, String start, String display, List<Search_news_item> items) {
        this.lastBuildDate = lastBuildDate;
        this.total = total;
        this.start = start;
        this.display = display;
        this.items = items;
    }
}
