package com.example.toron.Retrofit.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Yesno {
    @Expose
    @SerializedName("response") String reponse;

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public void setYesno(boolean yesno) {
        this.yesno = yesno;
    }

    public String getReponse() {
        return reponse;
    }

    public boolean isYesno() {
        return yesno;
    }

    public Yesno(String reponse, boolean yesno) {
        this.reponse = reponse;
        this.yesno = yesno;
    }

    @Expose
    @SerializedName("result") private boolean yesno;


}