package com.codept.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private TextView username,status;
    private CircleImageView profilePic;
    private LinearLayout profileClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ///
        username=findViewById(R.id.settingsUsername);
        profilePic=findViewById(R.id.settingProfilePic);
        status=findViewById(R.id.settingsStatus);
        profileClick=findViewById(R.id.profileClicks);
        profileClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this,Profile.class));
            }
        });


        mAuth=FirebaseAuth.getInstance();
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        userRef.keepSynced(true);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User userModel=snapshot.getValue(User.class);

                    assert userModel != null;
                    username.setText(userModel.getUsername());
                    status.setText(userModel.getStatus());
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

    }
}