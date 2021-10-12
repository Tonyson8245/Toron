package com.example.toron.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.toron.Debate.Debate_room;
import com.example.toron.R;
import com.example.toron.Service.Class.Chat;
import com.example.toron.Service.Class.Room_data;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_A = 1;
    public static final int VIEW_TYPE_B = 2;
    Context context;
    Debate_room debate_room;
    String nickname,user_idx,TAG = " chatAdapter";
    private ArrayList<Chat> list = new ArrayList<>();

    public ChatAdapter(ArrayList<Chat> list, Context context) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences("user_data",0);
        user_idx = sharedPreferences.getString("user_idx",null);
        this.context = context;
        this.debate_room = (Debate_room) context;
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
        String side = chat.getSide();

        String time_data = chat.getDatetime();
        Date date = null;
        try {
            SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = dateParser.parse(time_data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm");
        String datetime = dateFormatter.format(date);


        if (holder instanceof AHolder) { //A가 남이다.
            ((AHolder) holder).content.setText(msg);

            if (side.equals("con")) ((AHolder) holder).content.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FDD7D7"))); //반대
            else ((AHolder) holder).content.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D7DCFD"))); //찬성
            ((AHolder) holder).nickname.setText(nickname);
            ((AHolder) holder).datetime.setText(datetime);

            String imageStr = "http://49.247.195.99/storage/profile_img/" +chat.getUser_idx()+".jpg";

            // 다른 사람 이미지는 서버에서 가져온다
            Picasso.get()
                    .load(imageStr)
                    .error(R.drawable.ic_baseline_account_circle_24)
                    .into(((AHolder) holder).profile);

            ((AHolder) holder).content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    debate_room.set_TagMode("chat_idx",nickname,msg);
                }
            }); // 태그의 경우 한번 클릭

            ((AHolder) holder).content.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(debate_room,"long click",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }); // 신고, 좋아요의 경우 Long button

        } else {
            ((BHolder) holder).content.setText(msg);

            if (side.equals("con")) ((BHolder) holder).content.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FDD7D7"))); //반대
            else ((BHolder) holder).content.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D7DCFD"))); // 찬성
            ((BHolder) holder).nickname.setText(nickname);
            ((BHolder) holder).datetime.setText(datetime);

            // 내 이미지는 로컬에서 가져온다
            String uristr = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Toron/Storage/Image/profile_img.jpg";
            File files = new File(uristr);
            if(files.exists()==true) {
                Uri uri = Uri.parse(uristr);
                ((BHolder) holder).profile.setImageURI(uri);
            } else {
                ((BHolder) holder).profile.setImageResource(R.mipmap.ic_launcher_round);
            }
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
        TextView datetime;
        ImageView profile;


        public AHolder(View view) {
            super(view);
            content = (TextView) view.findViewById(R.id.Tv_message);
            nickname = (TextView) view.findViewById(R.id.Tv_user_name);
            datetime = (TextView) view.findViewById(R.id.Tv_datetime);
            profile = (ImageView) view.findViewById(R.id.Img_userprofile);
        }
    }

    public class BHolder extends RecyclerView.ViewHolder {

        TextView content;
        TextView nickname;
        ImageView profile;
        TextView datetime;


        public BHolder(View view) {
            super(view);
            content = (TextView) view.findViewById(R.id.Tv_message);
            nickname = (TextView) view.findViewById(R.id.Tv_user_name);
            datetime = (TextView) view.findViewById(R.id.Tv_datetime);
            profile = (ImageView) view.findViewById(R.id.Img_userprofile);
        }
    }
}