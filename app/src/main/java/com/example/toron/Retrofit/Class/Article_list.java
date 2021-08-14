package com.example.toron.Retrofit.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.List;

public class Article_list {
    @Expose
    @SerializedName("result") private String result;
    @Expose
    @SerializedName("list") private List<com.example.toron.Retrofit.Class.New_article> list;

    @Override
    public String toString() {
        return "Article_list{" +
                "result='" + result + '\'' +
                ", list=" + list +
                '}';
    }

    public List<New_article> getList() {
        return list;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setList(List<com.example.toron.Retrofit.Class.New_article> list) {
        this.list = list;
    }

    public Article_list(String result, List<com.example.toron.Retrofit.Class.New_article> list) {
        this.result = result;
        this.list = list;
    }
}
