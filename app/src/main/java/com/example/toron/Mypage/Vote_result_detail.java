package com.example.toron.Mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.toron.R;
import com.example.toron.Retrofit.ApiClient;
import com.example.toron.Retrofit.Class.Result_mypage_debate_list;
import com.example.toron.Retrofit.Class.Result_mypage_vote_result_detail;
import com.example.toron.Retrofit.Class.Vote_result;
import com.example.toron.Retrofit.Interface.MypageInterface;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Vote_result_detail extends AppCompatActivity {

    PieChart pieChart;
    String vote_idx,start_date,end_date,TAG="Vote_result_detail";
    TextView tv_title,tv_description,tv_start_date,tv_end_date_time;
    Button btn_back;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_result_detail);

        pieChart = (PieChart)findViewById(R.id.piechart);

        Intent getData = getIntent();
        vote_idx = getData.getStringExtra("vote_idx");
        start_date = getData.getStringExtra("start_date");
        end_date = getData.getStringExtra("end_date");

        tv_title = findViewById(R.id.tv_title);
        tv_description = findViewById(R.id.tv_description);
        tv_end_date_time = findViewById(R.id.tv_end_date);
        tv_start_date = findViewById(R.id.tv_start_date);
        btn_back = findViewById(R.id.btn_back);

        tv_end_date_time.setText(end_date);
        tv_start_date.setText(start_date);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_vote_data(vote_idx);
    }

    private void get_vote_data(String vote_idx){
        MypageInterface mypageInterface = ApiClient.getApiClient().create(MypageInterface.class);
        Call<Result_mypage_vote_result_detail> call = mypageInterface.Select_vote_result_detail(vote_idx);
        call.enqueue(new Callback<Result_mypage_vote_result_detail>() {
            @Override
            public void onResponse(Call<Result_mypage_vote_result_detail> call, Response<Result_mypage_vote_result_detail> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    Result_mypage_vote_result_detail temp_data = response.body();
                    tv_description.setText(temp_data.getMessage().getRoom_description());
                    tv_title.setText(temp_data.getMessage().getRoom_subject());


                    set_pieChart(temp_data.getMessage().getVote_con_qty(),temp_data.getMessage().getVote_pro_qty());
                }
            }

            @Override
            public void onFailure(Call<Result_mypage_vote_result_detail> call, Throwable t) {
                Log.d(TAG,t.getMessage());
            }
        });
    }


    private void set_pieChart(String con_qty,String pro_qty){
        float float_con = (float) Integer.valueOf(con_qty);
        float float_pro = (float) Integer.valueOf(pro_qty);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        yValues.add(new PieEntry(float_pro,"찬성"));
        yValues.add(new PieEntry(float_con,"반대"));


//        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues,"진영");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        int[] color = new int[2];
        color[0] = Color.rgb(44,124,185);
        color[1] = Color.rgb(227,76,57);
        dataSet.setColors(color);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.invalidate();
        pieChart.setTouchEnabled(false);
        pieChart.setData(data);
    }
}