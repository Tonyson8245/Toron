package com.example.toron.Service.Class;

public class Room {
    String title;
    String last_message;
    String room_idx;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLast_meassage() {
        return last_message;
    }

    public void setLast_meassage(String last_meassage) {
        this.last_message = last_meassage;
    }

    public String getRoom_idx() {
        return room_idx;
    }

    public void setRoom_idx(String room_idx) {
        this.room_idx = room_idx;
    }

    public Room(String title, String last_meassage, String room_idx) {
        this.title = title;
        this.last_message = last_meassage;
        this.room_idx = room_idx;
    }
}
