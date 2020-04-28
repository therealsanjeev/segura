package com.thesegura.co.seguraluggage.verification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thesegura.co.seguraluggage.Dashboard;
import com.thesegura.co.seguraluggage.R;

import java.util.Objects;

public class login extends AppCompatActivity {

    private boolean backAlreadyPressed = false;

    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private EditText etEnterNumber;
    private Button btSendOtp;
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseFirestore fs;
    private int RC_SIGN_IN = 0;
    private String TAG = "Google SignIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_login);

        auth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBarLogin);
        etEnterNumber = findViewById(R.id.etEnterNumber);
        btSendOtp = findViewById(R.id.btSendOtp);
        signInButton = findViewById(R.id.sign_in_button);

        progressBar.setVisibility(View.INVISIBLE);

        btSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNo = etEnterNumber.getText().toString();
                if (phoneNo.isEmpty() || phoneNo.length() < 10) {
                    etEnterNumber.setError("Phone number is required");
                    etEnterNumber.requestFocus();
                    return;
                }
                final Intent intent = new Intent(login.this, verifyOTP.class);
                intent.putExtra("phoneNo", phoneNo);
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(intent);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, 5000);

            }
        });


        //Google Sign IN :
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        // hardcoded due to errors with google_services,json**It may not be same for your device**
        String clientID = "263793474923-opnbs0u05d01vg0chjj6fcd0pqkua1rf.apps.googleusercontent.com";

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientID)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;

                }
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            boolean newUser = true;
                            newUser = Objects.requireNonNull(task.getResult()).getAdditionalUserInfo().isNewUser();
                            // Sign in success, update UI with the signed-in user's information
                            updateUI(auth.getCurrentUser(), newUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI((GoogleSignInAccount) null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            Intent i = new Intent(login.this, Dashboard.class);
            startActivity(i);
        } else {
            Toast.makeText(login.this, "Sign In failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser user, boolean isNewUser) {
        if (user != null) {
            if (isNewUser) {
                Intent i = new Intent(login.this, profile_details.class);
                startActivity(i);
            } else {
                Intent i = new Intent(login.this, Dashboard.class);
                startActivity(i);
            }
        } else {
            Toast.makeText(login.this, "Sign In failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(intent);
            finish();
        }
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
        Toast.makeText(this, "Press once more to exit", Toast.LENGTH_SHORT).show();
        // set the variable to false if it takes more than 2sec for another back press
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backAlreadyPressed = false;
            }
        }, 2000);
    }
}
