package com.example.toron.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.toron.Adapter.ChatAdapter;
import com.example.toron.Mypage.Mypage_main;
import com.example.toron.Mypage.Profile_setting;
import com.example.toron.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Mypage_fragment extends Fragment {
    private static final String TAG = "Mypage_fragment";
    TextView Tv_user_nickname,Tv_profile_setting;
    ImageView img_profile;

    String user_idx,user_id,user_nickname;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mypage_main, container, false);
        setHasOptionsMenu(true);
        Tv_user_nickname = view.findViewById(R.id.Tv_user_nickname);
        Tv_profile_setting = view.findViewById(R.id.Tv_profile_setting);

        img_profile = view.findViewById(R.id.img_profile);

        Tv_profile_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile_setting = new Intent(getActivity(), Profile_setting.class);
                startActivity(profile_setting);
            }
        });

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        get_userdata(getContext());
        super.onResume();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
    }






    void get_userdata(Context context){

        img_profile.setImageResource(R.mipmap.ic_launcher_round);

        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences("user_data",0);
        user_id = sharedPreferences.getString("user_id",null);
        user_idx = sharedPreferences.getString("user_idx",null);
        user_nickname = sharedPreferences.getString("user_nickname","nick");
        Tv_user_nickname.setText(user_nickname);
        String url = "http://49.247.195.99/storage/profile_img/" + user_idx + ".jpg";

        Log.d("123Mypage_main",url);

        Picasso.get().invalidate(url);
//        Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(img_profile);
        Picasso.get().load(url).into(img_profile);
    }
}