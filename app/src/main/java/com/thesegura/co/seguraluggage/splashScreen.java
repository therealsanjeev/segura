package com.thesegura.co.seguraluggage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.thesegura.co.seguraluggage.verification.login;

public class splashScreen extends AppCompatActivity {

    private static int time=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(splashScreen.this,login.class);
                startActivity(intent);
                finish();

            }
        },time);
    }
}
