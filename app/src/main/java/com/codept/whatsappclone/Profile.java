package com.codept.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    private CircleImageView profilePic;
    private CardView editProfilePic;
    private TextView username,phoneNumber,status;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri uri;
    private LinearLayout editUsername,editStatus,editPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profilePic =findViewById(R.id.profilePic);
        editProfilePic =findViewById(R.id.profileEditProfilePic);
        username =findViewById(R.id.profileUsername);
        progressBar=findViewById(R.id.profileSetupProgressBar);
        phoneNumber =findViewById(R.id.profilePhoneNumber);
        status =findViewById(R.id.profileStatus);
        editUsername=findViewById(R.id.updateUsername);
        editPhoneNumber=findViewById(R.id.updatePhoneNumber);
        editStatus=findViewById(R.id.updateStatus);

        editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });




        Dialog dialog=new Dialog(this);


        mAuth= FirebaseAuth.getInstance();
        userRef= FirebaseDatabase.getInstance().getReference().child("users")
                .child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        userRef.keepSynced(true);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User userModel=snapshot.getValue(User.class);
                    assert userModel != null;
                    username.setText(userModel.getUsername());
                    status.setText(userModel.getStatus());
                    phoneNumber.setText(mAuth.getCurrentUser().getPhoneNumber());
                    Picasso.get().load(userModel.getProfilePic())
                            .placeholder(R.drawable.profile)
                            .networkPolicy(NetworkPolicy.OFFLINE).into(profilePic, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(userModel.getProfilePic())
                                    .placeholder(R.drawable.profile)
                                    .into(profilePic);


                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        editUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.edit_name_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Profile.this, "Failed to update profile picture" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        HashMap map=new HashMap();
                        map.put("profilePic",downloadUri.toString());
                        userRef.updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(Profile.this, "Profile photo updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {


                            }
                        });
                        Picasso.get().load(downloadUri).into(profilePic);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }

}