package com.example.toron.Service.Class;

public class Status_Foreground {
    String name;
    String room_idx=null;
    Object chat=null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom_idx() {
        return room_idx;
    }

    public void setRoom_idx(String room_idx) {
        this.room_idx = room_idx;
    }

    public Object getChat() {
        return chat;
    }

    public void setChat(Object chat) {
        this.chat = chat;
    }

    public Status_Foreground(String name, String room_idx, Object chat) {
        this.name = name;
        this.room_idx = room_idx;
        this.chat = chat;
    }
}
