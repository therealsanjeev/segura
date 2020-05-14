package com.thesegura.co.seguraluggage.verification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thesegura.co.seguraluggage.Dashboard;
import com.thesegura.co.seguraluggage.R;

import java.util.HashMap;
import java.util.Map;

public class profile_details extends AppCompatActivity {

    private static final String TAG ="h" ;
    EditText etName,etEmail,etPhone,etPass,etPassCon,etAddress;
    Button btnSave;
    String userID;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_details);

        etName=findViewById(R.id.etNamePro);
        etEmail=findViewById(R.id.etEmailPro);
        etPhone=findViewById(R.id.etphonePro);
        btnSave=findViewById(R.id.btnNextPro);
        etPass=findViewById(R.id.etpass);
        etPassCon=findViewById(R.id.etPassCon);
        etAddress=findViewById(R.id.etAddress);


        progressBar=findViewById(R.id.progressBarDetails);
        progressBar.setVisibility(View.INVISIBLE);

        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                //Manager DATA:
                String name=etName.getText().toString();
                String email=etEmail.getText().toString();
                String phone=etPhone.getText().toString();
                String pass=etPass.getText().toString();
                String passCon=etPassCon.getText().toString();
                String address=etAddress.getText().toString();

                if(dataValidation(phone,email,pass,passCon)){
                    saveData(name,phone,email,pass,address);
                }


            }
        });
    }
    public void saveData(String name,String phone,String email,String pass,String address){
        userID=auth.getCurrentUser().getUid();
        DocumentReference documentReference =firebaseFirestore.collection("Managers").document(userID);
        Map<String,Object> manager = new HashMap<>();
        manager.put("Name",name);
        manager.put("email",email);
        manager.put("phone",phone);
        manager.put("password",pass);
        manager.put("address",address);

        documentReference.set(manager).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d(TAG,"onSuccess : manager Created "+userID);
                Intent intent=new Intent(profile_details.this, Dashboard.class);
                startActivity(intent);
            }
        });
    }

    public boolean dataValidation(String phone,String email,String pass,String passCon){
        if(!isValidEmail(email)){
            etEmail.setError("Invalid Email");
            etPhone.requestFocus();
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        }
        if(phone.isEmpty()||phone.length()<10){
            etPhone.setError("Invalid Phone");
            etPhone.requestFocus();
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        }
        if(pass.isEmpty()||pass.length()<8){
            etPass.setError("Password should be greater then 8");
            etPhone.requestFocus();
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        }
        if(!pass.equals(passCon)){
            etPassCon.setError("Password does not match");
            etPhone.requestFocus();
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        }
        return true;
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}
