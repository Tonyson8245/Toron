package com.example.toron.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toron.Fragment.Vote_fragment;
import com.example.toron.Main.Mainpage;
import com.example.toron.R;
import com.example.toron.Retrofit.Class.Vote_list_data;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VoteRecyclerAdapter extends RecyclerView.Adapter<VoteRecyclerAdapter.ViewHolder> {

    private Mainpage mainpage;
    private List<Vote_list_data> List;
    private String TAG = "VoteAdapter";

    public VoteRecyclerAdapter(Context context, java.util.List<Vote_list_data> list) {
        this.mainpage = (Mainpage) context;
        List = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(List.get(position));
    }

    public void set_voteList(List<Vote_list_data> list){
        this.List = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView Tv_subject,Tv_description,Tv_view_num,Tv_participant_num,Tv_datetime,Tv_end_datetime,Tv_pro,Tv_con,Tv_nickname;
        ImageView Img_profile;
        Button btn_enter_vote,btn_already_vote;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd");
        String end_day;
        String start_day;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Tv_subject = itemView.findViewById(R.id.Tv_vote_subject);
            Tv_description = itemView.findViewById(R.id.Tv_vote_description);
            Tv_con = itemView.findViewById(R.id.Tv_con);
            Tv_pro = itemView.findViewById(R.id.Tv_pro);
//            Tv_view_num = itemView.findViewById(R.id.Tv_view_num);
            Tv_participant_num = itemView.findViewById(R.id.Tv_participate_num);
            Tv_datetime = itemView.findViewById(R.id.Tv_Start_date);
            Tv_end_datetime = itemView.findViewById(R.id.Tv_End_date);
            Tv_nickname = itemView.findViewById(R.id.Tv_nickname);
            Img_profile = itemView.findViewById(R.id.Img_profile_img);
            btn_enter_vote = itemView.findViewById(R.id.btn_enter_vote);
            btn_already_vote = itemView.findViewById(R.id.btn_already_vote);
        }

        void onBind(Vote_list_data vote_list_data){
            String pro_qty,con_qty,url,view_num,participant_num;

            int total;
            float pro_percent,con_percent;

            if(vote_list_data.getVote_view_num()==null) vote_list_data.setVote_view_num("0");
            if(vote_list_data.getVote_participant_num()==null) vote_list_data.setVote_participant_num("0");

            Tv_subject.setText(vote_list_data.getVote_subject());
            Tv_description.setText(vote_list_data.getVote_description());
//            Tv_view_num.setText(vote_list_data.getVote_view_num());
            Tv_participant_num.setText(vote_list_data.getVote_participant_num());
            Tv_nickname.setText(vote_list_data.getVote_user_maker_nickname());
            Tv_datetime.setText(vote_list_data.getVote_datetime());

            url = "http://49.247.195.99/storage/profile_img/" +vote_list_data.getVote_user_maker_idx()+".jpg";
            Picasso.get().load(url).fit().into(Img_profile);

            try {
                Date new_date = format.parse(vote_list_data.getVote_datetime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(new_date);

                start_day = new_format.format(cal.getTime());
                cal.add(Calendar.DATE, +3);
                end_day = new_format.format(cal.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Tv_end_datetime.setText(end_day);
            Tv_datetime.setText(start_day);


            //투표 밑에 레이아웃
            if(vote_list_data.getVote_status().equals("1")){
                btn_enter_vote.setVisibility(View.GONE);
                btn_already_vote.setVisibility(View.VISIBLE);
            }// 이미 참여하였을 경우 이미 투표 하였다고 보여준다.
            else {
                btn_enter_vote.setVisibility(View.VISIBLE);
                btn_already_vote.setVisibility(View.GONE);
            }

            if(btn_enter_vote.isClickable()) {
                btn_enter_vote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (btn_enter_vote.isClickable())
                            mainpage.moveToHistory(vote_list_data.getVote_idx());
                    }
                });
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