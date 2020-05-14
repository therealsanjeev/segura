package com.thesegura.co.seguraluggage.UserData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thesegura.co.seguraluggage.Dashboard;
import com.thesegura.co.seguraluggage.R;
import com.thesegura.co.seguraluggage.verification.profile_details;
import com.thesegura.co.seguraluggage.verification.verifyOTP;

import java.util.concurrent.TimeUnit;

public class addCustomer extends AppCompatActivity {

    EditText name,number,luggages,etOTP;
    Button btnSave;
    ProgressBar progressBar;
    private static FirebaseAuth auth,UserAuthh;
    FirebaseFirestore fs;
    Toolbar toolbar;
    private String managerID;
    PhoneAuthProvider.ForceResendingToken token;
    String codeSent;
    Boolean flag=true;
    Boolean btnFlag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_customer);

        toolbar=findViewById(R.id.toolBarOthers);
        toolbar.setTitle("Add Customer");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //id's
        name=findViewById(R.id.nameCustomer);
        number=findViewById(R.id.numberCustomer);
        luggages=findViewById(R.id.luggageNo);
        btnSave=findViewById(R.id.saveData);
        progressBar=findViewById(R.id.userPro);
        etOTP=findViewById(R.id.etOtpAdd);

        auth=FirebaseAuth.getInstance();
        UserAuthh=FirebaseAuth.getInstance();
        fs=FirebaseFirestore.getInstance();
        managerID=auth.getCurrentUser().getUid();

        btnSave.setText("Send OTP");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                String nameStr =name.getText().toString();
                String numberStr=number.getText().toString();
                String luggageStr=luggages.getText().toString();
                if(nameStr.isEmpty()){
                    name.setError("Incorrect Name ");
                    return;
                }
                if(numberStr.isEmpty() || numberStr.length()<10){
                    number.setError("Incorrect Number..");
                    return;
                }
                if(luggageStr.isEmpty()){
                    luggages.setError("Wrong...");
                    return;
                }


                if(btnFlag){
                    btnSave.setText("Submit");
                    String code=etOTP.getText().toString();
                    verifyCode(code);
                    btnFlag=true;
                }else{
                    etOTP.setVisibility(View.VISIBLE);
                    sendOTP(numberStr);
                    btnFlag=false;
                }
            }
        });

    }

    private void saveData(String nameStr,String numberStr,String luggageStr) {
        CollectionReference dc=fs.collection("Managers").document(managerID).collection("Customers");
        UserData userData=new UserData(nameStr,numberStr,luggageStr);

        dc.add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressBar.setVisibility(View.INVISIBLE);
                name.getText().clear();
                number.getText().clear();
                luggages.getText().clear();
                Toast.makeText(addCustomer.this,"User Data Added Successfully !",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(addCustomer.this,"Something is Wrong? Try Again...",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void  verifyCode(String code ){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        UserAuthh.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            String nameStr =name.getText().toString();
                            String numberStr=number.getText().toString();
                            String luggageStr=luggages.getText().toString();
                            saveData(nameStr,numberStr,luggageStr);
                            Toast.makeText(addCustomer.this,"User add successfully!",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(addCustomer.this,"Error... "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendOTP(String num) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+num,
                60, TimeUnit.SECONDS,
                this,
                mCallbacks);

    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            progressBar.setVisibility(View.INVISIBLE);
            btnSave.setText("Send OTP");
            if(code!=null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            btnSave.setText("Send OTP");
            Toast.makeText(addCustomer.this,"Cannot Create Account "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            btnSave.setText("Verify OTP");
            codeSent=s;
            token=forceResendingToken;
            flag=false;
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
        }
    };
}
