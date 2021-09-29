package com.codept.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyContacts extends AppCompatActivity {
    private RecyclerView recyclerView;
     contactsRecyclerAdapter adapter;
    private ArrayList<User> userArrayList=new ArrayList<>();
    private FirebaseAuth auth;
    private DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts);
        recyclerView=findViewById(R.id.myContactsRecyclerView);
        adapter=new contactsRecyclerAdapter(this,userArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(adapter);


        userRef=FirebaseDatabase.getInstance().getReference().child("users");
        userRef.keepSynced(true);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userArrayList.clear();
                if(snapshot.exists())
                {

                    for (DataSnapshot userSnap: snapshot.getChildren()) {

                        User user=userSnap.getValue(User.class);
                        assert user != null;
                        user.setUid(userSnap.getKey());
                        userArrayList.add(user);
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                userArrayList.clear();
                Toast.makeText(MyContacts.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}