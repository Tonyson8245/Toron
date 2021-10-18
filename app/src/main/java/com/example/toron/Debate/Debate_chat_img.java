package com.example.toron.Debate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.toron.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Debate_chat_img extends AppCompatActivity {

    Button btn_back;
    ImageView Img_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debate_chat_img);


        btn_back = findViewById(R.id.btn_back);
        Img_chat = findViewById(R.id.Img_chat);

        Intent getData = getIntent();
        String url =  getData.getStringExtra("url");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Picasso.get().invalidate(url);
        Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(Img_chat);
    }
}