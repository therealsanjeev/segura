package com.thesegura.co.seguraluggage.verification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ProgressBar;
import android.widget.Toast;

import com.thesegura.co.seguraluggage.R;

public class login extends AppCompatActivity {

    private boolean backAlreadyPressed = false;

    ProgressBar progressBar;
    EditText etEnterNumber;
    Button btSendOtp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar =findViewById(R.id.progressBarLogin);
        etEnterNumber=findViewById(R.id.etEnterNumber);
        btSendOtp=findViewById(R.id.btSendOtp);

        progressBar.setVisibility(View.INVISIBLE);

        btSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String phoneNo=etEnterNumber.getText().toString();
               if(phoneNo.isEmpty()||phoneNo.length()<10){
                   etEnterNumber.setError("Phone number is required");
                   etEnterNumber.requestFocus();
                   return;
               }
                progressBar.setVisibility(View.VISIBLE);
               Intent intent=new Intent(login.this,verifyOTP.class);
               intent.putExtra("phoneNo",phoneNo);
               startActivity(intent);
            }
        });

    }

    public void onBackPressed() {

        if (backAlreadyPressed) {
            // close the application
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            super.onBackPressed();
            return;
        }
        // first back press should set the variable to true & show a Toast to press again to close application
        this.backAlreadyPressed = true;
        Toast.makeText(this,"Press once more to exit", Toast.LENGTH_SHORT).show();
        // set the variable to false if it takes more than 2sec for another back press
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backAlreadyPressed = false;
            }
        }, 2000);
    }
}
