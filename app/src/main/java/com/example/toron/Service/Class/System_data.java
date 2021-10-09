package com.example.toron.Service.Class;

public class System_data {
    String type;
    String room_idx;
    String room_name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoom_idx() {
        return room_idx;
    }

    public void setRoom_idx(String room_idx) {
        this.room_idx = room_idx;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public System_data(String type, String room_idx, String room_name) {
        this.type = type;
        this.room_idx = room_idx;
        this.room_name = room_name;
    }

    public System_data(String type) {
        this.type = type;
    }
}
