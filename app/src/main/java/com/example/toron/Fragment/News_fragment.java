package com.example.toron.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toron.Adapter.NewsRecyclerAdapter;
import com.example.toron.Retrofit.Class.Article_list;
import com.example.toron.Retrofit.Class.New_article;
import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Interface.NewsInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class News_fragment extends Fragment {
    String TAG = "!!!FRAG!NEWS";
    private Activity News_activity;

    private int page = 0;
    List<New_article> list = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private NewsRecyclerAdapter newsRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.activity_news_fragment, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.news_recyclerView);

        get_News(0);
        recyclerView.setHasFixedSize(true);
        newsRecyclerAdapter = new NewsRecyclerAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(newsRecyclerAdapter);
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (!recyclerView.canScrollVertically(1)) {
                    page++;
                    get_News(page);
                    Log.i(TAG, "End " + page);
                }
//                else if (!recyclerView.canScrollVertically(-1)) {
//                    list.clear();
//                    get_News(0);
//                    Toast.makeText(News_activity,"뉴스를 불러왔습니다.",Toast.LENGTH_SHORT).show();
//                }
                else {
                    Log.i(TAG, "idle");
                }
            }
        });
        Log.e("Frag", "MainFragment");
        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        if(context instanceof Activity){
            News_activity = (Activity) context;
            Log.d(TAG,News_activity.toString());
        }
    }

    private void get_News(int i) {
        NewsInterface newsInterface = ApiClient.getApiClient().create(NewsInterface.class);
        Call<Article_list> call = newsInterface.get_News(Integer.toString(i));
        call.enqueue(new Callback<Article_list>() {
            @Override
            public void onResponse(Call<Article_list> call, Response<Article_list> response) {
                if(response.isSuccessful() && response.body()!=null){
                    if(response.body().getResult().equals("success")){
                        list.addAll(response.body().getList());
                    }
                    else if(response.body().getResult().equals("failed")){
                        Toast.makeText(News_activity,"하단입니다.",Toast.LENGTH_SHORT).show();
                        page--;
                    }
                }
                else Log.d(TAG,"내용 없음");
                newsRecyclerAdapter.set_Newslist(list);
            }

            @Override
            public void onFailure(Call<Article_list> call, Throwable t) {
                Log.d(TAG,"실패");
            }
        });
    }

}