package com.example.mentalhealthapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class IntroductoryActivity extends AppCompatActivity
{
    TextView start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);
        start = findViewById(R.id.start);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view)
            {
                Intent intent = new Intent(IntroductoryActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
