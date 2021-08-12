package com.example.toron.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.toron.R;

public class Find_id_Activity extends AppCompatActivity {

    Button btn_login,btn_password,btn_back;
    TextView Tv_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        Intent intent = getIntent();
        String user_id = intent.getStringExtra("user_id");

        Tv_id = findViewById(R.id.Tv_id);
        Tv_id.setText(user_id);

        btn_login = findViewById(R.id.btn_login);
        btn_password = findViewById(R.id.btn_password);
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(Find_id_Activity.this,Login.class);
                startActivity(login);
                finish();
            }
        });
        btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}