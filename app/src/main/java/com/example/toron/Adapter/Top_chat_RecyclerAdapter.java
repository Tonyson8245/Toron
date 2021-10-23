package com.example.toron.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toron.Debate.Debate_SelectSide;
import com.example.toron.R;
import com.example.toron.Retrofit.Class.Vote_Top_chat;

import java.util.List;

public class Top_chat_RecyclerAdapter extends RecyclerView.Adapter<Top_chat_RecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Vote_Top_chat> List;
    private int rank=0;

    public Top_chat_RecyclerAdapter(Context context, java.util.List<Vote_Top_chat> list) {
        this.context = context;
        List = list;
    }

    public Top_chat_RecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setList(java.util.List<Vote_Top_chat> list) {
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

    public void set_Newslist(List<Vote_Top_chat> list){
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

        void onBind(Vote_Top_chat vote_top_chat){
            rank +=1;
            title.setText(vote_top_chat.getChat_content());
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