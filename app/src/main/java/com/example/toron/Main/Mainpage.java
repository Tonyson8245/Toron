package com.example.toron.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.toron.Fragment.Devate_fragment;
import com.example.toron.Fragment.Mypage_fragment;
import com.example.toron.Fragment.News_fragment;
import com.example.toron.Fragment.Vote_fragment;
import com.example.toron.News.News_detail;
import com.example.toron.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Mainpage extends AppCompatActivity {

    private BottomNavigationView mBottomNV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator_test);

        mBottomNV = findViewById(R.id.nav_view);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                BottomNavigate(menuItem.getItemId());


                return true;
            }
        });
        mBottomNV.setSelectedItemId(R.id.navigation_news);
    }
    private void BottomNavigate(int id) {  //BottomNavigation 페이지 변경
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            if (id == R.id.navigation_news) {
                fragment = new News_fragment();

            } else if (id == R.id.navigation_devate){

                fragment = new Devate_fragment();
            }else if (id == R.id.navigation_vote) {
                fragment = new Vote_fragment();
            }else {
                fragment = new Mypage_fragment();
            }

            fragmentTransaction.add(R.id.content_layout, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();


    }

    public void click_news_detail(String href,String title,String img,String writing,String datetime) {
        Intent news_detail = new Intent(this, News_detail.class);
        news_detail.putExtra("href",href);
        news_detail.putExtra("title",title);
        news_detail.putExtra("img",img);
        news_detail.putExtra("writing",writing);
        news_detail.putExtra("datetime",datetime);
        startActivity(news_detail);
    }
}