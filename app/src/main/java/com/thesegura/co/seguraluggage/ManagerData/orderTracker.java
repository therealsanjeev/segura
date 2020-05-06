package com.thesegura.co.seguraluggage.ManagerData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.thesegura.co.seguraluggage.R;
import com.thesegura.co.seguraluggage.UserData.UserData;

import java.util.ArrayList;
import java.util.List;

public class orderTracker extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore fs;
    RecyclerView recyclerView;
    UserDataAdapter adapter;
    List<UserData> userDataList;
    ProgressBar progressBar;
    String managerID;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_tracker);
        toolbar=findViewById(R.id.toolBarOthers);
        toolbar.setTitle("MY Orders");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar=findViewById(R.id.orderProgress);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        auth=FirebaseAuth.getInstance();
        fs=FirebaseFirestore.getInstance();
        managerID=auth.getCurrentUser().getUid();
        userDataList=new ArrayList<>();
        adapter=new UserDataAdapter(this,userDataList);
        recyclerView.setAdapter(adapter);


        fs.collection("Managers").document(managerID).collection("Customers").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list =queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot documentSnapshot:list){
                                UserData userData=documentSnapshot.toObject(UserData.class);
                                userDataList.add(userData);
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

    }
}
