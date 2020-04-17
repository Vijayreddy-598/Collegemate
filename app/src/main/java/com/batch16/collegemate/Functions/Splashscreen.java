package com.batch16.collegemate.Functions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.batch16.collegemate.LoginActivity;
import com.batch16.collegemate.R;

public class Splashscreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in=new Intent(Splashscreen.this, LoginActivity.class);
                startActivity(in);
                finish();
            }
        },SPLASH_DISPLAY_LENGTH);
    }
}
