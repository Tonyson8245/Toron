package com.example.toron.Adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toron.News.News_detail;
import com.example.toron.News.Search_news;
import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Reply;
import com.example.toron.Retrofit.Class.Search_news_img;
import com.example.toron.Retrofit.Class.Search_news_item;
import com.example.toron.Retrofit.Interface.NewsInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReplyRecyclerAdapter extends RecyclerView.Adapter<ReplyRecyclerAdapter.ViewHolder> {

    private News_detail news_detail;
    private Context context;
    private List<Reply> List;
    private String user_id;
    String TAG = "!!!ReplyRecyclerView";

    public ReplyRecyclerAdapter(Context context, java.util.List<Reply> list) {
        this.context = context;
        this.news_detail = (News_detail) context;
        List = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(List.get(position));
    }

    public void setList(List<Reply> list,String user_id){
        this.List = list;
        this.user_id = user_id;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView reply_content,reply_delete,reply_update,reply_nickname,reply_datetime,reply_modify,Tv_like_qty;
        ImageView profile_img,btn_like;
        LinearLayout item_layout;
        View item_line;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_img = (ImageView) itemView.findViewById(R.id.Img_profile_img);
            reply_delete = (TextView) itemView.findViewById(R.id.Tv_reply_delete);
            reply_content = (TextView) itemView.findViewById(R.id.Tv_reply_content);
            reply_update = (TextView) itemView.findViewById(R.id.Tv_reply_update);
            reply_nickname = (TextView) itemView.findViewById(R.id.Tv_reply_nickname);
            reply_datetime = (TextView) itemView.findViewById(R.id.Tv_reply_datetime);
            item_layout = (LinearLayout) itemView.findViewById(R.id.item_layout);
            item_line = (View) itemView.findViewById(R.id.item_line);
            reply_modify = (TextView) itemView.findViewById(R.id.reply_modify);
            btn_like =  (ImageView) itemView.findViewById(R.id.btn_like);
            Tv_like_qty = (TextView) itemView.findViewById(R.id.Tv_like_qty);
        }

        void onBind(Reply item){
            reply_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    news_detail.delete_reply(item.getReply_idx());
                }

            });
            reply_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    news_detail.update_reply(item.getReply_idx(),item.getContent());
                }
            });
            btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(true) { //!item.getUser_profile_img().equals(user_id)
                        news_detail.update_like(item.getReply_idx(), item.getReply_like_user_id());
                        if (item.getReply_like_user_id().equals("1")) {
                            btn_like.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                            item.setReply_like_user_id("0");
                            item.setReply_like_qty(String.valueOf(Integer.parseInt(item.getReply_like_qty()) - 1));
                        }//좋아요 -> 좋아요 취소
                        else {
                            btn_like.setImageResource(R.drawable.ic_baseline_favorite_24);
                            item.setReply_like_user_id("1");
                            item.setReply_like_qty(String.valueOf(Integer.parseInt(item.getReply_like_qty()) + 1));
                        }//좋아요 취소 -> 좋아요
                        Tv_like_qty.setText(item.getReply_like_qty());
                    }
                    else Toast.makeText(context,"본인의 댓글은 좋아요를 누를 수 없습니다.",Toast.LENGTH_SHORT).show();
                }
            });

            if(item.getReply_status().equals("-1")) {
                item_layout.setVisibility(View.GONE);

//                profile_img.setImageResource(R.drawable.ic_baseline_image_24);
//                profile_img.setPadding(20, 20, 20, 20);
//                reply_datetime.setText(item.getDatetime());
//                reply_content.setText("삭제된 댓글입니다.");
//                reply_nickname.setText("<" + item.getUser_nickname() + ">");
//                reply_delete.setVisibility(View.GONE);
//                reply_update.setVisibility(View.GONE);
//                btn_like.setVisibility(View.GONE);
//                Tv_like_qty.setVisibility(View.GONE);
//                reply_modify.setVisibility(View.GONE);
            }
            else{
                try {
                    Log.d(TAG,"http://49.247.195.99/storage/profile_img/" + item.getUser_profile_img() + ".jpg");
                    Picasso.get().load("http://49.247.195.99/storage/profile_img/" + item.getUser_profile_img() + ".jpg").into(profile_img);
                } catch (Exception e) {
                    profile_img.setImageResource(R.drawable.ic_baseline_image_24);
                    profile_img.setPadding(20, 20, 20, 20);
                }

                reply_datetime.setText(item.getDatetime());
                reply_content.setText(item.getContent());
                if (user_id.equals(item.getUser_profile_img())) {
                    reply_delete.setVisibility(View.VISIBLE);
                    reply_update.setVisibility(View.VISIBLE);
                    reply_nickname.setText("[" + item.getUser_nickname()+"]");
                }
                else{
                    reply_delete.setVisibility(View.GONE);
                    reply_update.setVisibility(View.GONE);

                    reply_nickname.setText(item.getUser_nickname());
                }
                if(item.getReply_status().equals("1")) reply_modify.setVisibility(View.VISIBLE);
                else reply_modify.setVisibility(View.GONE);

                Tv_like_qty.setText(item.getReply_like_qty());

                if(item.getReply_like_user_id().equals("1")){
                    btn_like.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
                else btn_like.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            }

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }
    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }
}