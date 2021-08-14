package com.example.toron.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toron.Fragment.News_fragment;
import com.example.toron.Main.Mainpage;
import com.example.toron.News.News_detail;
import com.example.toron.R;
import com.example.toron.Retrofit.Class.New_article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder> {

    private Mainpage mainpage;
    private Context context;
    private List<New_article> List;

    public NewsRecyclerAdapter(Context context, java.util.List<New_article> list) {
        this.context = context;
        this.mainpage = (Mainpage) context;
        List = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newsitem_recyclerview, parent, false);
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
        ImageView Img_news;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Img_news = (ImageView) itemView.findViewById(R.id.Img_new);
            title = (TextView) itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        Log.d("!!!adapter",List.get(pos).getNews_href());
                        mainpage.click_news_detail(List.get(pos).getNews_href(),List.get(pos).getNews_title(),List.get(pos).getNews_img(),List.get(pos).getNews_writing(),List.get(pos).getNews_datetime());
                    }
                }
            }); // 클릭 이벤트 세부 페이지로 보내준다.
        }

        void onBind(New_article article){
            try{
                Picasso.get().load(article.getNews_img()).into(Img_news);
            }catch (Exception e){
                Img_news.setImageResource(R.drawable.ic_baseline_image_24);
                Img_news.setPadding(20,20,20,20);
            }
            title.setText(Html.fromHtml(article.getNews_title()).toString());
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