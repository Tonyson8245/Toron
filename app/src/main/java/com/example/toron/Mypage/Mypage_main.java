package com.example.toron.Mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toron.Class.ImageLoadTask;
import com.example.toron.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

public class Mypage_main extends AppCompatActivity {

    TextView Tv_user_nickname,Tv_profile_setting;
    ImageView img_profile;

    String user_id,user_nickname,user_idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_main);

        Tv_user_nickname = findViewById(R.id.Tv_user_nickname);
        Tv_profile_setting = findViewById(R.id.Tv_profile_setting);

        img_profile = findViewById(R.id.img_profile);

        Tv_profile_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile_setting = new Intent(Mypage_main.this,Profile_setting.class);
                startActivity(profile_setting);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_userdata();
    }


    void get_userdata(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("user_data",0);
        user_id = sharedPreferences.getString("user_id",null);
        user_idx = sharedPreferences.getString("user_idx",null);

        user_nickname = sharedPreferences.getString("user_nickname","nick");
        Tv_user_nickname.setText(user_nickname);
        String url = "http://49.247.195.99/storage/profile_img/" + user_idx + ".jpg";

        Log.d("Mypage_main",url);
        try{
            Picasso.get().invalidate(url);
//            Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(img_profile);
            Picasso.get().load(url).into(img_profile);
        }catch (Exception e){
            img_profile.setImageResource(R.mipmap.ic_launcher_round);
        }

//        String uristr = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Toron/Storage/Image/profile_img.jpg";
//        Log.d("!!!!되라ㅁㄴㅇ",uristr);
//        File files = new File(uristr);
//        if(files.exists()==true) {
//            Uri uri = Uri.parse(uristr);
//            img_profile.setImageURI(uri);
//        } else {
//            img_profile.setImageResource(R.mipmap.ic_launcher_round);
//        }
    }
}