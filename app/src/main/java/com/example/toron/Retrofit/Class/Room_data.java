package com.example.toron.Retrofit.Class;


import java.util.ArrayList;

public class Room_data {
    String user_maker;
    String start_date;
    String room_subject;
    String room_description;
    String chat_qty;
    String member_qty;
    String category;
    ArrayList<New_article> cons;

    @Override
    public String toString() {
        return "Room_data{" +
                "user_maker='" + user_maker + '\'' +
                ", start_date='" + start_date + '\'' +
                ", room_subject='" + room_subject + '\'' +
                ", room_description='" + room_description + '\'' +
                ", chat_qty='" + chat_qty + '\'' +
                ", member_qty='" + member_qty + '\'' +
                ", category='" + category + '\'' +
                ", cons=" + cons +
                ", pros=" + pros +
                '}';
    }

    ArrayList<New_article> pros;

    public String getUser_maker() {
        return user_maker;
    }

    public void setUser_maker(String user_maker) {
        this.user_maker = user_maker;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getRoom_subject() {
        return room_subject;
    }

    public void setRoom_subject(String room_subject) {
        this.room_subject = room_subject;
    }

    public String getRoom_description() {
        return room_description;
    }

    public void setRoom_description(String room_description) {
        this.room_description = room_description;
    }

    public String getChat_qty() {
        return chat_qty;
    }

    public void setChat_qty(String chat_qty) {
        this.chat_qty = chat_qty;
    }

    public String getMember_qty() {
        return member_qty;
    }

    public void setMember_qty(String member_qty) {
        this.member_qty = member_qty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<New_article> getCons() {
        return cons;
    }

    public void setCons(ArrayList<New_article> cons) {
        this.cons = cons;
    }

    public ArrayList<New_article> getPros() {
        return pros;
    }

    public void setPros(ArrayList<New_article> pros) {
        this.pros = pros;
    }

    public Room_data(String user_maker, String start_date, String room_subject, String room_description, String chat_qty, String member_qty, String category) {
        this.user_maker = user_maker;
        this.start_date = start_date;
        this.room_subject = room_subject;
        this.room_description = room_description;
        this.chat_qty = chat_qty;
        this.member_qty = member_qty;
        this.category = category;
    }

    public Room_data(String user_maker, String start_date, String room_subject, String room_description, String chat_qty, String member_qty, String category, ArrayList<New_article> cons, ArrayList<New_article> pros) {
        this.user_maker = user_maker;
        this.start_date = start_date;
        this.room_subject = room_subject;
        this.room_description = room_description;
        this.chat_qty = chat_qty;
        this.member_qty = member_qty;
        this.category = category;
        this.cons = cons;
        this.pros = pros;
    }
}

