package com.codept.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSetup extends AppCompatActivity {
    private EditText username;
    private Button next;
    private CircleImageView profilePic;
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri uri;
    private ProgressBar progressBar;
    private HashMap map=new HashMap();
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        username=findViewById(R.id.profileSetupUsername);
        next=findViewById(R.id.profileSetupNext);
        profilePic=findViewById(R.id.profileSetupProfilePhoto);
        progressBar=findViewById(R.id.profileSetupProgressBar);
        firebaseAuth=FirebaseAuth.getInstance();
        currentUserId=firebaseAuth.getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mUsername=username.getText().toString();
                if(mUsername.isEmpty())
                {
                    username.setError("Username required!");
                }else{
                    map.put("username",mUsername);
                    map.put("createdAt", ServerValue.TIMESTAMP);
                    map.put("status","Hey there! I am using WhatsApp.");
                    uploadData();
                }
            }
        });
    }

    private void uploadData() {

        databaseReference.updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(ProfileSetup.this, "Profile successfully updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileSetup.this,MainActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(ProfileSetup.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    // Select Image method
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            uri = data.getData();
            //
            uploadImage();

        }
    }
    private void uploadImage()
    {
        if (uri != null) {

            progressBar.setVisibility(View.VISIBLE);

            // Defining the child of storageReference
            StorageReference ref
                    = FirebaseStorage.getInstance().getReference().child("profile/" + FirebaseAuth.getInstance().getCurrentUser().getUid().toString()+".jpg");

            // adding listeners on upload
            // or failure of image
            UploadTask uploadTask;
            uploadTask=ref.putFile(uri);

            uploadTask
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(ProfileSetup.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileSetup.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        map.put("profilePic",downloadUri.toString());
                        Picasso.get().load(downloadUri).into(profilePic);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }
    private boolean isLoggedIn()
    {
        boolean bool;
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            bool=true;
            Toast.makeText(ProfileSetup.this, "is authenticated", Toast.LENGTH_SHORT).show();
        }
        else{
            bool=false;
            Toast.makeText(ProfileSetup.this, "is not authenticated", Toast.LENGTH_SHORT).show();
        }
        return bool;

    }
    private void toStartScreen()
    {
        startActivity(new Intent(ProfileSetup.this,StartScreen.class));
        finish();
    }
}