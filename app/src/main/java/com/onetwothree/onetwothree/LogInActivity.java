package com.onetwothree.onetwothree;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity {


    private TextView LogIn_Result_text;
    private ImageView LogIn_Result_profile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_login_activity);

        Intent intent = getIntent();

        String nickname = intent.getStringExtra("nickname");
        String photoUrl = intent.getStringExtra("photoUrl");

        LogIn_Result_text = findViewById(R.id.login_nickname);
        LogIn_Result_profile = findViewById(R.id.login_profile);

        LogIn_Result_text.setText(nickname);
        Glide.with(this)
                .load(photoUrl)
                .into(LogIn_Result_profile);


    }

}
