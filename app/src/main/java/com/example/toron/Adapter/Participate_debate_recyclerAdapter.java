package com.example.toron.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toron.Mypage.Participate_debate;
import com.example.toron.Mypage.Participate_vote;
import com.example.toron.R;
import com.example.toron.Retrofit.Class.Mypage_participate_debate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Participate_debate_recyclerAdapter extends RecyclerView.Adapter<Participate_debate_recyclerAdapter.ViewHolder> {

    private Context context;
    private Participate_debate participate_debate;
    private List<Mypage_participate_debate> List;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd");
    String TAG ="Participate_debate_recyclerAdapter";

    public Participate_debate_recyclerAdapter(Context context) {
        this.context = context;
        this.participate_debate = (Participate_debate) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_participate_debate_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(List.get(position));
    }

    public void setList(List<Mypage_participate_debate> list){
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
        TextView room_subject,debate_time,debate_status_proceed,debate_status_end;
        LinearLayout btn_layout;
        String start_day,end_day;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumb_down = itemView.findViewById(R.id.img_thumb_up);
            thumb_up = itemView.findViewById(R.id.img_thumb_down);
            room_subject = itemView.findViewById(R.id.title);
            debate_time = itemView.findViewById(R.id.tv_debate_end_date);
            debate_status_end = itemView.findViewById(R.id.debate_status_end);
            debate_status_proceed = itemView.findViewById(R.id.debate_status_proceed);
            btn_layout = itemView.findViewById(R.id.btn_layout);

        }

        void onBind(Mypage_participate_debate mypage_participate_debate) {
            /// 찬성 반성표시
            if(mypage_participate_debate.getSide().equals("con")){
                thumb_down.setVisibility(View.VISIBLE);
                thumb_up.setVisibility(View.GONE);
            }
            else{
                thumb_up.setVisibility(View.VISIBLE);
                thumb_down.setVisibility(View.GONE);
            }

            //토론 명 표시
            room_subject.setText(mypage_participate_debate.getRoom_subject());

            //방 상태 표시
            if(!mypage_participate_debate.getStatus().equals("debate")){
                debate_status_end.setVisibility(View.VISIBLE);
                debate_status_proceed.setVisibility(View.GONE);
            }
            else{
                debate_status_proceed.setVisibility(View.VISIBLE);
                debate_status_end.setVisibility(View.GONE);
            }

            /// 투표 종료시간은 토론 시작 후 6일 후이다. 토론 3일 + 투표 3일
            try {
                Date new_date = format.parse(mypage_participate_debate.getDatetime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(new_date);

                start_day = new_format.format(cal.getTime());
                cal.add(Calendar.DATE, +6);
                end_day = new_format.format(cal.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            debate_time.setText("~" + end_day);

            btn_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"하냐");
                    participate_debate.moveToDebate(mypage_participate_debate.getRoom_idx(),mypage_participate_debate.getSide(),mypage_participate_debate.getStatus());
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