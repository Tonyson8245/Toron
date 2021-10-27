package com.example.toron.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toron.Mypage.Mypage_reply_list;
import com.example.toron.Mypage.Vote_result;
import com.example.toron.R;
import com.example.toron.Retrofit.Class.Mypage_reply;
import com.example.toron.Retrofit.Class.Mypage_vote_result_list;

import java.util.ArrayList;
import java.util.List;

public class Mypage_vote_result_Adapter extends RecyclerView.Adapter<Mypage_vote_result_Adapter.ViewHolder> {

    private Context context;
    private Vote_result vote_result;
    private ArrayList<Mypage_vote_result_list> List;

    public Mypage_vote_result_Adapter(Context context) {
        this.context = context;
        this.vote_result = (Vote_result) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_vote_result_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(List.get(position));
    }

    public void setList(ArrayList<Mypage_vote_result_list> list){
        this.List = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(List==null) return 0;
        else return List.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_start_date,tv_end_date,tv_title;
        Button btn_enter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_start_date = itemView.findViewById(R.id.tv_start_date);
            tv_end_date = itemView.findViewById(R.id.tv_end_date);
            tv_title = itemView.findViewById(R.id.tv_title);
            btn_enter = itemView.findViewById(R.id.btn_enter);
        }

        void onBind(Mypage_vote_result_list mypage_vote_result_list) {
            tv_title.setText(mypage_vote_result_list.getRoom_subject());
            tv_start_date.setText(mypage_vote_result_list.getStart_datetime());
            tv_end_date.setText(mypage_vote_result_list.getEnd_datetime());

            btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vote_result.moveToResultDetail(mypage_vote_result_list.getVote_idx(),mypage_vote_result_list.getStart_datetime(),mypage_vote_result_list.getEnd_datetime());
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