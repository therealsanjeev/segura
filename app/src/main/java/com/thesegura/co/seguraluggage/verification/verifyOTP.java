package com.thesegura.co.seguraluggage.verification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.thesegura.co.seguraluggage.MainActivity;
import com.thesegura.co.seguraluggage.R;


import java.util.concurrent.TimeUnit;

public class verifyOTP extends AppCompatActivity {

    EditText etOtp;
    Button btVerify;
    FirebaseAuth auth;
    String codeSent;
    ProgressBar progressBar;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);

        progressBar=findViewById(R.id.progressBarOTP);
        progressBar.setVisibility(View.INVISIBLE);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        etOtp=findViewById(R.id.etOtp);
        btVerify=findViewById(R.id.btVerifyNum);
        //sending otp:
        String num=getIntent().getStringExtra("phoneNo");
        sendOTP(num);

        btVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code=etOtp.getText().toString().trim();
                if(code.isEmpty() || code.length()<6){
                    etOtp.setError("Not valid !");
                    etOtp.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });

    }
    private void  verifyCode(String code ){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            if(user.getUid()!=null){
                                Intent intent=new Intent(verifyOTP.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(),"Welcome Back !",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(verifyOTP.this, profile_details.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                        } else {
                            Toast.makeText(verifyOTP.this,"Error ! "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendOTP(String num) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+num,
                60, TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);

    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if(code!=null){
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(verifyOTP.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent=s;
        }
    };
}
