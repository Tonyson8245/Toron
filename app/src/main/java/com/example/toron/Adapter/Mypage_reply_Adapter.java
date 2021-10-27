package com.example.toron.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toron.Mypage.Mypage_reply_list;
import com.example.toron.Mypage.Participate_vote;
import com.example.toron.R;
import com.example.toron.Retrofit.Class.Mypage_reply;
import com.example.toron.Retrofit.Class.Mypage_reply;

import java.util.List;

public class Mypage_reply_Adapter extends RecyclerView.Adapter<Mypage_reply_Adapter.ViewHolder> {

    private Context context;
    private Mypage_reply_list mypage_reply_list;
    private List<Mypage_reply> List;

    public Mypage_reply_Adapter(Context context) {
        this.context = context;
        this.mypage_reply_list = (Mypage_reply_list) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_reply_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(List.get(position));
    }

    public void setList(List<Mypage_reply> list){
        this.List = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(List==null) return 0;
        else return List.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_reply_content,tv_datetime,tv_news_title;
        LinearLayout btn_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_reply_content = itemView.findViewById(R.id.tv_reply_content);
            tv_datetime = itemView.findViewById(R.id.tv_datetime);
            tv_news_title = itemView.findViewById(R.id.tv_news_title);
            btn_layout = itemView.findViewById(R.id.btn_layout);
        }

        void onBind(Mypage_reply mypage_reply) {
            tv_reply_content.setText(mypage_reply.getReply_content());
            tv_news_title.setText( Html.fromHtml(mypage_reply.getNews_title()).toString());
            tv_datetime.setText(mypage_reply.getReply_datetime());

            btn_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mypage_reply_list.moveToNews(mypage_reply.getNews_idx(),mypage_reply.getHref(), Html.fromHtml(mypage_reply.getNews_title()).toString(),mypage_reply.getNews_img(),mypage_reply.getNews_writing(),mypage_reply.getReply_datetime());
                }
            });
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