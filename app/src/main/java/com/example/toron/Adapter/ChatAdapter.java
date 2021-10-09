package com.example.toron.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.toron.R;
import com.example.toron.Service.Class.Chat;
import com.example.toron.Service.Class.Room_data;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_A = 1;
    public static final int VIEW_TYPE_B = 2;
    String nickname,user_idx;
    private ArrayList<Chat> list = new ArrayList<>();

    public ChatAdapter(ArrayList<Chat> list, Context context) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences("user_data",0);
        user_idx = sharedPreferences.getString("user_idx",null);

        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_A) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_other, parent, false);
            return new AHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_myself , parent, false);

            return new BHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getUser_idx().equals(user_idx)) {
            return VIEW_TYPE_B;
        } else {
            return VIEW_TYPE_A;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Chat chat = list.get(position);
        String msg = chat.getMsg();
        String nickname = chat.getNickname();

        if (holder instanceof AHolder) {
            ((AHolder) holder).content.setText(msg);
            ((AHolder) holder).nickname.setText(nickname);
        } else {
            ((BHolder) holder).content.setText(msg);
            ((BHolder) holder).nickname.setText(nickname);
        }
    }

    private Chat getChat(int position) {
        return list.get(position);
    }

    public void set_chatlist(ArrayList<Chat> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AHolder extends RecyclerView.ViewHolder {

        TextView content;
        TextView nickname;
        ImageView profile;


        public AHolder(View view) {
            super(view);
            content = (TextView) view.findViewById(R.id.Tv_message);
            nickname = (TextView) view.findViewById(R.id.Tv_user_name);
            profile = (ImageView) view.findViewById(R.id.Img_userprofile);
        }
    }

    public class BHolder extends RecyclerView.ViewHolder {

        TextView content;
        TextView nickname;
        ImageView profile;


        public BHolder(View view) {
            super(view);
            content = (TextView) view.findViewById(R.id.Tv_message);
            nickname = (TextView) view.findViewById(R.id.Tv_user_name);
            profile = (ImageView) view.findViewById(R.id.Img_userprofile);
        }
    }

}