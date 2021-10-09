package com.example.toron.Service.Class;

public class Request {
    String type;
    String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type='" + type + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public Request(String type, String data) {
        this.type = type;
        this.data = data;
    }
}
