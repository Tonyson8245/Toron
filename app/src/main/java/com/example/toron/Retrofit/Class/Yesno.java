package com.example.toron.Retrofit.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Yesno {
    @Expose
    @SerializedName("response") String response;

    public void setResponse(String reponse) {
        this.response = reponse;
    }

    public void setYesno(boolean yesno) {
        this.yesno = yesno;
    }

    public String getResponse() {
        return response;
    }

    public boolean isYesno() {
        return yesno;
    }

    public Yesno(String reponse, boolean yesno) {
        this.response = reponse;
        this.yesno = yesno;
    }

    @Expose
    @SerializedName("result") private boolean yesno;


}