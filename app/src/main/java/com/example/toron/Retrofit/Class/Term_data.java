package com.example.toron.Retrofit.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Term_data {
    @Expose
    @SerializedName("result") String result;
    @Expose
    @SerializedName("data") String data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Term_data(String result, String data) {
        this.result = result;
        this.data = data;
    }
}
