package com.thesegura.co.seguraluggage;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.thesegura.co.seguraluggage.ManagerData.privacy_policy;
import com.thesegura.co.seguraluggage.ManagerData.profile;
import com.thesegura.co.seguraluggage.UserData.UserData;
import com.thesegura.co.seguraluggage.UserData.addCustomer;
import com.thesegura.co.seguraluggage.ManagerData.orderTracker;
import com.thesegura.co.seguraluggage.verification.login;
import com.thesegura.co.seguraluggage.verification.profile_details;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class Dashboard extends AppCompatActivity {

    TextView tvname,tvAmount,tvComplete;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    FirebaseAuth auth;
    FirebaseFirestore fs;
    GoogleSignInClient mGoogleSignInClient;

    private boolean backAlreadyPressed = false;
    private List<UserData> userDataList;
    private int amount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        tvname=findViewById(R.id.tvDashName);
        tvAmount=findViewById(R.id.tvAmount);
        tvComplete=findViewById(R.id.tvComplete);
        userDataList=new ArrayList<>();
        auth=FirebaseAuth.getInstance();
        fs=FirebaseFirestore.getInstance();

        managerNameShowOnDash();
//        googleProfile();

        //toolBar:
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //navigationView :
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle =new ActionBarDrawerToggle(Dashboard.this, drawerLayout,R.string.open,R.string.close);
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
                Toast.makeText(Dashboard.this,"ADD YOUR CUSTOMER",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Dashboard.this, addCustomer.class);
                startActivity(i);
            }
        });
    }


    private void googleProfile() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }
    }

    public  void managerNameShowOnDash(){

        DocumentReference documentReference=fs.collection("Managers").document(auth.getCurrentUser().getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    tvname.setText(documentSnapshot.getString("Name"));
                }else{
                    Toast.makeText(Dashboard.this,"Enter your personal details first",Toast.LENGTH_LONG).show();
                }

            }
        });
        documentReference.collection("Customers").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list =queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot documentSnapshot:list){
                                UserData userData=documentSnapshot.toObject(UserData.class);
                                userDataList.add(userData);
                            }
                            for(UserData l:userDataList){
                                amount+=Integer.parseInt(l.getUserLuggage());
                            }
//                            if(!userDataList.isEmpty()){
//                                int size=userDataList.size();
//                                tvComplete.setText(size);
//                            }

                            tvAmount.setText(amount*60+"₹");

                        }
                    }
                });

    }
    private void UserMenu(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.home:
                DrawerLayout mDrawerLayout;
                mDrawerLayout = findViewById(R.id.drawer_layout);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.profile:
                startActivity(new Intent(getApplicationContext(), profile.class));
                break;
            case R.id.orderTracks:
                startActivity(new Intent(Dashboard.this, orderTracker.class));
                break;
            case R.id.feedback:
                break;
            case R.id.aboutus:
                break;
            case R.id.privacy:
                startActivity(new Intent(getApplicationContext(), privacy_policy.class));
                break;
            case R.id.singOut:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Sign Out");
                builder.setMessage("Are you sure you want to exit? ").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getApplicationContext(), login.class));
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog =builder.create();
                alertDialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed() {

        if (backAlreadyPressed) {
            // close the application
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            super.onBackPressed();
            return;
        }
        // first back press should set the variable to true & show a Toast to press again to close application
        this.backAlreadyPressed = true;
        Toast.makeText(this,"Press once more to exit", Toast.LENGTH_SHORT).show();
        // set the variable to false if it takes more than 2sec for another back press
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backAlreadyPressed = false;
            }
        }, 2000);
    }

}
