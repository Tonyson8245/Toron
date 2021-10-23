package com.example.toron.Adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toron.Main.Mainpage;
import com.example.toron.R;
import com.example.toron.Service.Class.Room_data;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RoomRecyclerAdapter extends RecyclerView.Adapter<RoomRecyclerAdapter.ViewHolder> {

    private Mainpage mainpage;
    private Context context;
    private List<Room_data> List;
    String TAG = "RoomAdapter";

    public RoomRecyclerAdapter(Context context, java.util.List<Room_data> list) {
        this.context = context;
        this.mainpage = (Mainpage) context;
        List = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.toronitem_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(List.get(position));
    }

    public void set_RoomList(List<Room_data> list){
        this.List = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView Tv_message_num,Tv_participate_num,Tv_room_subject,Tv_room_description,Tv_nickname,Tv_Start_date,Tv_End_date;
        ImageView Img_profile_img;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd");
        String end_day,maker_idx,maker_nickname;
        String start_day;
        Button btn_enter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Tv_End_date = (TextView) itemView.findViewById(R.id.Tv_End_date);
            Tv_Start_date = (TextView) itemView.findViewById(R.id.Tv_Start_date);
            Tv_nickname = (TextView) itemView.findViewById(R.id.Tv_nickname);
            Tv_room_description = (TextView) itemView.findViewById(R.id.Tv_room_description);
            Tv_room_subject = (TextView) itemView.findViewById(R.id.Tv_room_subject);
            Tv_participate_num = (TextView) itemView.findViewById(R.id.Tv_participate_num);
            Tv_message_num = (TextView) itemView.findViewById(R.id.Tv_message_num);
            Img_profile_img = (ImageView) itemView.findViewById(R.id.Img_profile_img);
            btn_enter = (Button) itemView.findViewById(R.id.btn_enter);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
//                        mainpage.click_room();
                    }
                }
            }); // 클릭 이벤트 세부 페이지로 보내준다.

        }

        void onBind(Room_data room_data){
            try {
                Date new_date = format.parse(room_data.getStart_date());
                Calendar cal = Calendar.getInstance();
                cal.setTime(new_date);

                start_day = new_format.format(cal.getTime());
                cal.add(Calendar.DATE, +3);
                end_day = new_format.format(cal.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }


            String[] maker_data = room_data.getUser_maker().split("/",2);
            maker_idx = maker_data[0];
            maker_nickname = maker_data[1];


            Tv_End_date.setText(end_day);
            Tv_Start_date.setText(start_day);
            Tv_nickname.setText(maker_nickname);
            Tv_room_description.setText(room_data.getRoom_description());
            Tv_room_subject.setText(room_data.getRoom_subject());
            Tv_participate_num.setText(room_data.getMember_qty());
            Tv_message_num.setText(room_data.getChat_qty());

            try{
                String url = "http://49.247.195.99/storage/profile_img/"+maker_idx+".jpg";
                Picasso.get().load(url).into(Img_profile_img);
                Log.d(TAG,url);

            }catch (Exception e){
                Img_profile_img.setImageResource(R.drawable.ic_baseline_image_24);
                Img_profile_img.setPadding(20,20,20,20);
            }

            btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainpage.enter_room(room_data.getRoom_idx(),room_data.getStatus(),room_data.getRoom_subject(),room_data.getRoom_description());
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