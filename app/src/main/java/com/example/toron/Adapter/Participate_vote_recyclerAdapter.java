package com.example.toron.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toron.Mypage.Participate_vote;
import com.example.toron.R;
import com.example.toron.Retrofit.Class.Mypage_participage_vote;

import java.util.List;

public class Participate_vote_recyclerAdapter extends RecyclerView.Adapter<Participate_vote_recyclerAdapter.ViewHolder> {

    private Context context;
    private Participate_vote participate_vote;
    private List<Mypage_participage_vote> List;

    public Participate_vote_recyclerAdapter(Context context) {
        this.context = context;
        this.participate_vote = (Participate_vote) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_participate_voet_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(List.get(position));
    }

    public void setList(List<Mypage_participage_vote> list){
        this.List = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(List==null) return 0;
        else return List.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumb_up,thumb_down;
        TextView room_subject,vote_datetime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumb_down = itemView.findViewById(R.id.img_thumb_down);
            thumb_up = itemView.findViewById(R.id.img_thumb_up);
            room_subject = itemView.findViewById(R.id.title);
            vote_datetime = itemView.findViewById(R.id.tv_vote_datetime);
        }

        void onBind(Mypage_participage_vote mypage_participage_vote) {
            if (mypage_participage_vote.getSide().equals("con")) {
                thumb_up.setVisibility(View.GONE);
                thumb_down.setVisibility(View.VISIBLE);
            }
            else{
                thumb_up.setVisibility(View.VISIBLE);
                thumb_down.setVisibility(View.GONE);
            }

            room_subject.setText(mypage_participage_vote.getRoom_subject());
            vote_datetime.setText(mypage_participage_vote.getVote_datetime());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    participate_vote.moveTovote(mypage_participage_vote.getVote_idx(),mypage_participage_vote.getSide());
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