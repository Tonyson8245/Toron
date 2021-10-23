package com.example.toron.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.toron.Class.PicassoTransformation;
import com.example.toron.Debate.Debate_room;
import com.example.toron.R;
import com.example.toron.Service.Class.Chat;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
//import com.squareup.picasso.MemoryPolicy;
//import com.squareup.picasso.NetworkPolicy;
//import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_A = 1;
    public static final int VIEW_TYPE_B = 2;
    Context context;
    Debate_room debate_room;
    String nickname,user_idx,TAG = "chatAdapter";
    private ArrayList<Chat> list = new ArrayList<>();
    private HashMap<String ,Integer> chat_list = new HashMap<>();

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

    public int get_Tag_Item_Position(String tag_chat_idx){
        int position  = chat_list.get(tag_chat_idx);
        return position;
    } //tag 아이템 위치 호춤

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
        String mode = chat.getChat_mode();
        String img_href = chat.getImg_href();


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
        String imageStr = "http://49.247.195.99/storage/profile_img/" +chat.getUser_idx()+".jpg";


        if (holder instanceof AHolder) { //A가 남이다.
            //말풍선 색깔
            if (side.equals("con")) ((AHolder) holder).layout_other.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FDD7D7"))); //반대
            else ((AHolder) holder).layout_other.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D7DCFD"))); //찬성

            // 채팅일 때
            if(chat.getChat_mode().equals("msg")){
                ((AHolder) holder).content.setVisibility(View.VISIBLE);
                ((AHolder) holder).content.setText(msg); // 글씨 보이고
                ((AHolder) holder).Img_message.setVisibility(View.GONE); //그림 안보이고
            }
            else if(chat.getChat_mode().equals("local_img")){
                ((AHolder) holder).content.setVisibility(View.GONE);
                ((AHolder) holder).Img_message.setVisibility(View.VISIBLE); //그림 안보이고
                Picasso.get().load(img_href).placeholder(R.drawable.ic_baseline_image_24).into(((AHolder) holder).Img_message);
            }
            else if(chat.getChat_mode().equals("img")){
                ((AHolder) holder).content.setVisibility(View.GONE);
                ((AHolder) holder).Img_message.setVisibility(View.VISIBLE); //그림 안보이고
                String url = "http://49.247.195.99/storage/chat_img/" + img_href;
                Log.d(TAG,url);

                Picasso.get().load(url).placeholder(R.drawable.ic_baseline_image_24).into(((AHolder) holder).Img_message, new Callback() {
                    @Override
                    public void onSuccess() {
                        debate_room.toBottom(false);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

                ((AHolder) holder).Img_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        debate_room.open_img(url);
                    }
                });
            }


            //공통
            ((AHolder) holder).nickname.setText(nickname);
            ((AHolder) holder).datetime.setText(datetime);
            ((AHolder) holder).profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    debate_room.set_TagMode(chat.getUser_idx(),nickname);
                }
            }); // 태그의 경우 한번 클릭
            ((AHolder) holder).layout_other.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    debate_room.open_dialog(list.get(position).getChat_idx(),side);
                    return false;
                }
            }); // 신고, 좋아요의 경우 Long button
            // 이미지는 서버에서 가져온다

            Picasso.get().invalidate(imageStr);
//            Picasso.get().load(imageStr).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).error(R.drawable.ic_baseline_account_circle_24).into(((AHolder) holder).profile);
            Picasso.get().load(imageStr).into(((AHolder) holder).profile);
        } else {
            //말풍선 색깔
            if (side.equals("con")) ((BHolder) holder).layout_my.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FDD7D7"))); //반대
            else ((BHolder) holder).layout_my.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D7DCFD"))); //찬성

            // 채팅일 때
            if(chat.getChat_mode().equals("msg")){
                ((BHolder) holder).content.setVisibility(View.VISIBLE);
                ((BHolder) holder).content.setText(msg); // 글씨 보이고
                ((BHolder) holder).Img_message.setVisibility(View.GONE); //그림 안보이고
            }
            else if(chat.getChat_mode().equals("local_img")){
                ((BHolder) holder).content.setVisibility(View.GONE);
                ((BHolder) holder).Img_message.setImageURI(Uri.parse(img_href)); // 글씨 보이고
                Log.d(TAG,img_href);
                ((BHolder) holder).Img_message.setVisibility(View.VISIBLE); //그림 안보이고
            }
            else if(chat.getChat_mode().equals("img")){
                ((BHolder) holder).content.setVisibility(View.GONE);
                ((BHolder) holder).Img_message.setVisibility(View.VISIBLE); //그림 안보이고
                String url = "http://49.247.195.99/storage/chat_img/" + img_href;
                Log.d(TAG,url);

                Picasso.get().load(url).placeholder(R.drawable.ic_baseline_image_24).into(((BHolder) holder).Img_message, new Callback() {
                    @Override
                    public void onSuccess() {
                        debate_room.toBottom(false);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }


            //공통
            ((BHolder) holder).nickname.setText(nickname);
            ((BHolder) holder).datetime.setText(datetime);
            ((BHolder) holder).profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    debate_room.set_TagMode(chat.getUser_idx(),nickname);
                }
            }); // 태그의 경우 한번 클릭
            // 이미지는 서버에서 가져온다

            // 프로필 이미지
            Picasso.get().invalidate(imageStr);
//            Picasso.get().load(imageStr).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).error(R.drawable.ic_baseline_account_circle_24).into(((BHolder) holder).profile);
            Picasso.get().load(imageStr).into(((BHolder) holder).profile);
        }
    }

    private Chat getChat(int position) {
        return list.get(position);
    }

    public void set_chatlist(ArrayList<Chat> list){
        this.list = list;

        for(int i=0;i<list.size();i++){
            if(list.get(i).getTag_user_idx()!=null) chat_list.put(list.get(i).getChat_idx(),i);
        }
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
        ImageView Img_message;
        LinearLayout layout_other;


        public AHolder(View view) {
            super(view);
            content = (TextView) view.findViewById(R.id.Tv_message);
            nickname = (TextView) view.findViewById(R.id.Tv_user_name);
            datetime = (TextView) view.findViewById(R.id.Tv_datetime);
            profile = (ImageView) view.findViewById(R.id.Img_userprofile);
            Img_message = (ImageView) view.findViewById(R.id.Img_message);
            layout_other = (LinearLayout) view.findViewById(R.id.layout_other);
        }
    }

    public class BHolder extends RecyclerView.ViewHolder {

        TextView content;
        TextView nickname;
        ImageView profile;
        TextView datetime;
        ImageView Img_message;
        LinearLayout layout_my;


        public BHolder(View view) {
            super(view);
            layout_my = (LinearLayout) view.findViewById(R.id.layout_my);
            content = (TextView) view.findViewById(R.id.Tv_message);
            nickname = (TextView) view.findViewById(R.id.Tv_user_name);
            datetime = (TextView) view.findViewById(R.id.Tv_datetime);
            profile = (ImageView) view.findViewById(R.id.Img_userprofile);
            Img_message = (ImageView) view.findViewById(R.id.Img_message);
        }
    }
}