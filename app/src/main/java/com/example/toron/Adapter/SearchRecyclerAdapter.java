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

import com.example.toron.News.Search_news;
import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.New_article;
import com.example.toron.Retrofit.Class.Search_news_img;
import com.example.toron.Retrofit.Class.Search_news_item;
import com.example.toron.Retrofit.Interface.NewsInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {

    private Search_news search_news;
    private Context context;
    private List<Search_news_item> List;
    String TAG = "!!!SearchRecyclerAdapter";

    public SearchRecyclerAdapter(Context context, java.util.List<Search_news_item> list) {
        this.context = context;
        this.search_news = (Search_news) context;
        List = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_newsitem_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(List.get(position));
    }

    public void set_Search_news_item(List<Search_news_item> list){
        this.List = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView Img_news;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Img_news = (ImageView) itemView.findViewById(R.id.Img_new);
            title = (TextView) itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        search_news.click_news_detail(List.get(pos).getLink(),List.get(pos).getTitle(),List.get(pos).getImg_url());
                    }
                }
            }); // 클릭 이벤트 세부 페이지로 보내준다.
        }

        void onBind(Search_news_item item){
            try{
                if(item.getImg_url().length()>0) Picasso.get().load(item.getImg_url()).into(Img_news);
            }catch (Exception e){
                Img_news.setImageResource(R.drawable.ic_baseline_image_24);

                NewsInterface newsInterface = ApiClient.getApiClient().create(NewsInterface.class);
                Call<Search_news_img> call = newsInterface.get_Search_News_img(item.getLink());
                call.enqueue(new Callback<Search_news_img>() {
                    @Override
                    public void onResponse(Call<Search_news_img> call, Response<Search_news_img> response) {
                        Log.d(TAG, response.toString());

                        if (response.body() != null) {
                            if (response.body().getResult().equals("success")) {
                                Log.d(TAG, "asd" + item.getLink());
                                try {
                                    Picasso.get().load(response.body().getImg_url()).into(Img_news);
                                    item.setImg_url(response.body().getImg_url());
                                    }catch (Exception e){
                                    Img_news.setImageResource(R.drawable.ic_baseline_image_24);
                                    Img_news.setPadding(20, 20, 20, 20);
                                }
                            } else {
                                Log.d(TAG, "null");
                                Img_news.setImageResource(R.drawable.ic_baseline_image_24);
                                Img_news.setPadding(20, 20, 20, 20);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Search_news_img> call, Throwable t) {
                        Log.d(TAG, "FAILED:" + t.getMessage());
                    }
                });
            }
            title.setText(Html.fromHtml(item.getTitle()).toString());
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