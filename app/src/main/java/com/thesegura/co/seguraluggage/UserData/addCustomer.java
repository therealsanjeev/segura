package com.thesegura.co.seguraluggage.UserData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thesegura.co.seguraluggage.R;

public class addCustomer extends AppCompatActivity {

    EditText name,number,luggages;
    Button btnSave;
    ProgressBar progressBar;
    FirebaseAuth auth;
    FirebaseFirestore fs;
    Toolbar toolbar;
    private String managerID;
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

        auth=FirebaseAuth.getInstance();
        fs=FirebaseFirestore.getInstance();
        managerID=auth.getCurrentUser().getUid();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                saveData(nameStr,numberStr,luggageStr);
                progressBar.setVisibility(View.VISIBLE);
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
}
