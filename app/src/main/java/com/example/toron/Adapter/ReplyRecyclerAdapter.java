package com.example.toron.Adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView reply_content,reply_delete,reply_update,reply_nickname,reply_datetime;
        ImageView profile_img;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_img = (ImageView) itemView.findViewById(R.id.Img_profile_img);
            reply_delete = (TextView) itemView.findViewById(R.id.Tv_reply_delete);
            reply_content = (TextView) itemView.findViewById(R.id.Tv_reply_content);
            reply_update = (TextView) itemView.findViewById(R.id.Tv_reply_update);
            reply_nickname = (TextView) itemView.findViewById(R.id.Tv_reply_nickname);
            reply_datetime = (TextView) itemView.findViewById(R.id.Tv_reply_datetime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {

                    }
                }
            }); // 클릭 이벤트 세부 페이지로 보내준다.
        }

        void onBind(Reply item){
            try {
                Picasso.get().load("http://49.247.195.99/storage/profile_img/"+item.getUser_profile_img()+".jpg").into(profile_img);
                }catch (Exception e) {
                profile_img.setImageResource(R.drawable.ic_baseline_image_24);
                profile_img.setPadding(20, 20, 20, 20);
            }

            reply_datetime.setText(item.getDatetime());
            reply_content.setText(item.getContent());
            reply_nickname.setText("<" + item.getUser_nickname() +">");
            if(!user_id.equals(item.getUser_profile_img())){
                reply_delete.setVisibility(View.GONE);
                reply_update.setVisibility(View.GONE);
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