package com.thesegura.co.seguraluggage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class profile extends AppCompatActivity {

    TextView tvName;
    TextView tvEmail;
    TextView tvPhone;

    FirebaseAuth auth;
    FirebaseFirestore fs;
    String managerID;
    public String managerName;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar=findViewById(R.id.toolBarOthers);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth=FirebaseAuth.getInstance();
        fs=FirebaseFirestore.getInstance();
        managerID=auth.getCurrentUser().getUid();

        tvName=findViewById(R.id.tvManagerNamePro);
        tvEmail=findViewById(R.id.tvManagerEmailPro);
        tvPhone=findViewById(R.id.tvManagerPhonePro);

        DocumentReference documentReference=fs.collection("Managers").document(managerID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                managerName=documentSnapshot.getString("Name");
                tvName.setText(managerName);
                Intent i =new Intent(profile.this,MainActivity.class);
                i.putExtra("name",managerName);
                tvEmail.setText(documentSnapshot.getString("email"));
                tvPhone.setText(documentSnapshot.getString("phone"));
            }
        });



    }
}
