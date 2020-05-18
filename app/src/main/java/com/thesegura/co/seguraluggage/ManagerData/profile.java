package com.thesegura.co.seguraluggage.ManagerData;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thesegura.co.seguraluggage.R;

import java.io.ByteArrayOutputStream;

public class profile extends AppCompatActivity {

    TextView tvName,tvEmail,tvPhone,tvAddress;
    Toolbar toolbar;
    ImageView imageView;
    ImageButton imageButton;

    FirebaseAuth auth;
    FirebaseFirestore fs;
    String managerID;

    int imageCode=10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        toolbar = findViewById(R.id.toolBarOthers);
        toolbar.setTitle("My Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvName = findViewById(R.id.tvManagerNamePro);
        tvEmail = findViewById(R.id.tvManagerEmailPro);
        tvPhone = findViewById(R.id.tvManagerPhonePro);
        tvAddress=findViewById(R.id.tvManagerAddressPro);
        imageView=findViewById(R.id.profile_image);
        imageButton=findViewById(R.id.changeProfile);
//        headerName=findViewById(R.id.headerProfileName);
//        headerEmail=findViewById(R.id.headerProfileEmail);

        auth=FirebaseAuth.getInstance();
        fs=FirebaseFirestore.getInstance();
        managerID=auth.getCurrentUser().getUid();

        if(auth.getCurrentUser().getPhotoUrl()!=null){
            Glide.with(this).load(auth.getCurrentUser().getPhotoUrl()).into(imageView);
        }

        DocumentReference documentReference=fs.collection("Managers").document(managerID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshot != null;
                String name=documentSnapshot.getString("Name");
                String email=documentSnapshot.getString("email");
                String phone =documentSnapshot.getString("phone");
                String address=documentSnapshot.getString("address");
                tvName.setText(name);
                tvEmail.setText(email);
                tvPhone.setText(phone);
                tvAddress.setText(address);
//                headerName.setText(name);
//                headerEmail.setText(email);
            }
        });
    }


    public void profileImage(View view) {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null) {
            startActivityForResult(intent, imageCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==imageCode){
            switch (resultCode){
                case RESULT_OK:
                    Bitmap bitmap= (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                    uploadImageFirebase(bitmap);
            }
        }
    }

    private void uploadImageFirebase(Bitmap bitmap) {
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

            String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
            final StorageReference ref=FirebaseStorage.getInstance().getReference()
                    .child("managerProfile")
                    .child(uid+"jpeg");

            ref.putBytes(byteArrayOutputStream.toByteArray())
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            getImageURL(ref);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

    }

    private void getImageURL(StorageReference ref) {
        ref.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        setProfile(uri);
                    }
                });
    }

    private void setProfile(Uri uri) {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request=new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(profile.this,"Profile image Successfully",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(profile.this,"Profile image failed...",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}


