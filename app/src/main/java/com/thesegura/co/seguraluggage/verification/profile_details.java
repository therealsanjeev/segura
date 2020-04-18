package com.thesegura.co.seguraluggage.verification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    EditText etName;
    EditText etEmail;
    EditText etPhone;
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

        progressBar=findViewById(R.id.progressBarDetails);
        progressBar.setVisibility(View.INVISIBLE);

        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name=etName.getText().toString();
                String email=etEmail.getText().toString();
                String phone=etPhone.getText().toString();
                userID=auth.getCurrentUser().getUid();
                DocumentReference documentReference =firebaseFirestore.collection("Managers").document(userID);
                Map<String,Object> manager = new HashMap<>();
                manager.put("Name",name);
                manager.put("email",email);
                manager.put("phone",phone);
                documentReference.set(manager).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"onSuccess : manager Created "+userID);
                        Intent intent=new Intent(profile_details.this, Dashboard.class);
                        startActivity(intent);
                    }
                });

            }
        });
    }

}
