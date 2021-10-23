package com.example.toron.Retrofit.Class;

import java.util.ArrayList;

public class Vote_result {
    String result;
    ArrayList<Vote_list_data> data = new ArrayList<>();

    public Vote_result(String result, ArrayList<Vote_list_data> data) {
        this.result = result;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<Vote_list_data> getData() {
        return data;
    }

    public void setData(ArrayList<Vote_list_data> data) {
        this.data = data;
    }
}

