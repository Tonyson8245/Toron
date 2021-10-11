package com.example.toron.Service.Class;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Room_data implements Parcelable {
    String room_idx;

    protected Room_data(Parcel in) {
        room_idx = in.readString();
        user_maker = in.readString();
        start_date = in.readString();
        room_subject = in.readString();
        room_description = in.readString();
        chat_qty = in.readString();
        member_qty = in.readString();
        category = in.readString();
        status = in.readString();
    }

    public static final Creator<Room_data> CREATOR = new Creator<Room_data>() {
        @Override
        public Room_data createFromParcel(Parcel in) {
            return new Room_data(in);
        }

        @Override
        public Room_data[] newArray(int size) {
            return new Room_data[size];
        }
    };

    public String getRoom_idx() {
        return room_idx;
    }

    public void setRoom_idx(String room_idx) {
        this.room_idx = room_idx;
    }

    String user_maker;
    String start_date;
    String room_subject;
    String room_description;
    String chat_qty;
    String member_qty;
    String category;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    ArrayList<New_article> cons;
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

    public Room_data(String room_idx,String user_maker, String start_date, String room_subject, String room_description, String chat_qty, String member_qty, String category,String status) {
        this.user_maker = user_maker;
        this.start_date = start_date;
        this.room_subject = room_subject;
        this.room_description = room_description;
        this.chat_qty = chat_qty;
        this.member_qty = member_qty;
        this.category = category;
        this.status = status;
        this.room_idx = room_idx;
    }

    public Room_data(String room_idx,String user_maker, String start_date, String room_subject, String room_description, String chat_qty, String member_qty, String category, ArrayList<New_article> cons, ArrayList<New_article> pros, String status) {
        this.user_maker = user_maker;
        this.start_date = start_date;
        this.room_subject = room_subject;
        this.room_description = room_description;
        this.chat_qty = chat_qty;
        this.member_qty = member_qty;
        this.category = category;
        this.cons = cons;
        this.pros = pros;
        this.status = status;
        this.room_idx = room_idx;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(room_idx);
        dest.writeString(user_maker);
        dest.writeString(start_date);
        dest.writeString(room_subject);
        dest.writeString(room_description);
        dest.writeString(chat_qty);
        dest.writeString(member_qty);
        dest.writeString(category);
        dest.writeString(status);
    }
}
