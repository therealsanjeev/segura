package com.thesegura.co.seguraluggage.ManagerData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.thesegura.co.seguraluggage.R;

public class orderTracker extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_tracker);
        toolbar=findViewById(R.id.toolBarOthers);
        toolbar.setTitle("MY Orders");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
