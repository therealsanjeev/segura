package com.thesegura.co.seguraluggage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.thesegura.co.seguraluggage.UserData.addCustomer;
import com.thesegura.co.seguraluggage.verification.login;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolBar:
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //navigationView :
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle =new ActionBarDrawerToggle(MainActivity.this, drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigationView);
        View nav_view= navigationView.inflateHeaderView(R.layout.navigation_header);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenu(menuItem);
                return false;
            }
        });
        FloatingActionButton fab_btn=findViewById(R.id.fab_btn);
        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"ADD YOUR CUSTOMER",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, addCustomer.class);
                startActivity(i);
            }
        });
    }

    private void UserMenu(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.home:
                break;
            case R.id.profile:
                break;
            case R.id.feedback:
                break;
            case R.id.aboutus:
                break;
            case R.id.privacy:
                break;
            case R.id.singOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
