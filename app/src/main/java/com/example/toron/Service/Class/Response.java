package com.example.toron.Service.Class;

public class Response {
    String type;
    String data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Response(String type, String data) {
        this.type = type;
        this.data = data;
    }
}
