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

import com.example.toron.Debate.Debate_SelectSide;
import com.example.toron.Main.Mainpage;
import com.example.toron.R;
import com.example.toron.Retrofit.Class.New_article;
import com.example.toron.Vote.Vote_history;

import java.util.List;

public class AttachArticleRecyclerAdapter extends RecyclerView.Adapter<AttachArticleRecyclerAdapter.ViewHolder> {

    private Vote_history vote_history;
    private Debate_SelectSide debate_selectSide;

    private Context context;
    private List<New_article> List;
    String TAG ="AttachArticleRecyclerAdapter";

    public AttachArticleRecyclerAdapter(Context context, java.util.List<New_article> list) {
        this.context = context;
        try {
           this.vote_history = (Vote_history) context;
        }catch (ClassCastException e){
           this.debate_selectSide = (Debate_SelectSide) context;
        }
        List = list;
    }

    public AttachArticleRecyclerAdapter(Context context) {
        this.context = context;
        try {
            this.vote_history = (Vote_history) context;
        }catch (ClassCastException e){
            this.debate_selectSide = (Debate_SelectSide) context;
        }
    }

    public void setList(java.util.List<New_article> list) {
        List = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attach_article_item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(List.get(position));
    }

    public void set_Newslist(List<New_article> list){
        this.List = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        String href;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);


        }

        void onBind(New_article article){
            title.setText("▶ " + Html.fromHtml(article.getNews_title()).toString());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        vote_history.MoveToArticle(article.getNews_href());
                    }catch (ClassCastException e){
                        debate_selectSide.MoveToArticle(article.getNews_href());
                    }

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