package com.thesegura.co.seguraluggage.UserData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.os.Bundle;

import com.thesegura.co.seguraluggage.R;

public class addCustomer extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        toolbar=findViewById(R.id.toolBarOthers);
        toolbar.setTitle("ADD USER");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
