package com.thesegura.co.seguraluggage.ManagerData;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.thesegura.co.seguraluggage.R;

public class privacy_policy extends AppCompatActivity {
    Toolbar toolbar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);

        textView = findViewById(R.id.privacyText);
        textView.setMovementMethod(new ScrollingMovementMethod());

        toolbar = findViewById(R.id.toolBarOthers);
        toolbar.setTitle("Segura Privacy Policy");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
