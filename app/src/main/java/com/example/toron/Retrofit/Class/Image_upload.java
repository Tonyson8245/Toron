package com.example.toron.Retrofit.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image_upload {
    @Expose
    @SerializedName("result") String result;
    @Expose
    @SerializedName("value") String value;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Image_upload(String result, String value) {
        this.result = result;
        this.value = value;
    }
}