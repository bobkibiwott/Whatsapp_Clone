package com.codept.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Messages extends AppCompatActivity {
    private String messageUserID;
    private EditText message;
    private TextView mUsername,mOnlineStatus;
    private CircleImageView mProfilePic;
    private CardView mSendMessageButton;
    private FirebaseAuth mAuth;
    private String userID;
    private DatabaseReference mRef,userRef;
    private HashMap messageMap=new HashMap();

    /////
    private RecyclerView messageRecycler;
    private MessageAdapter adapter;
    private ArrayList<messageClass> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        messageUserID=getIntent().getStringExtra("messageUserID");
        ///Firebase Initialisation
        mAuth=FirebaseAuth.getInstance();
        userID=mAuth.getCurrentUser().getUid();
        mRef= FirebaseDatabase.getInstance().getReference().child("messages");
        mRef.keepSynced(true);
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(messageUserID);
        userRef.keepSynced(true);


        ///initialisation
        message=findViewById(R.id.message);
        mSendMessageButton=findViewById(R.id.sendMessageButton);
        ImageView backButton=findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        mProfilePic=findViewById(R.id.messageProfilePic);
        mUsername=findViewById(R.id.messageUsername);
        mOnlineStatus=findViewById(R.id.messageOnlineStatus);

        messageRecycler=findViewById(R.id.messageRecyclerView);

        ///

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        messageRecycler.setLayoutManager(linearLayoutManager);

        adapter=new MessageAdapter(this,arrayList);



        ///profileData Inflation
        addProfileInfo();
        //getting any messages
        fetchMessages();


        mSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String theMessage=message.getText().toString();
                if(theMessage.isEmpty())
                {
                    return;
                }else {
                    messageMap.put("message",theMessage);
                    messageMap.put("sender",userID);
                    messageMap.put("receiver",messageUserID);
                    messageMap.put("sent","false");
                    messageMap.put("received","false");
                    messageMap.put("seen","false");
                    messageMap.put("timestamp", ServerValue.TIMESTAMP);
                    message.setText("");
                    sendMessageFunction();
                }
            }
        });
    }

    private void fetchMessages() {
        mRef.child(userID).child("users").child(messageUserID).child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.hasChildren())
                {
                    arrayList.clear();
                    for (DataSnapshot messageSnapShot:snapshot.getChildren())
                    {
                        messageClass messageclass=messageSnapShot.getValue(messageClass.class);
                        assert messageclass != null;
                        messageclass.setMessageID(messageSnapShot.getKey());
                        arrayList.add(messageclass);
                    }
                    messageRecycler.setAdapter(adapter);
                    int newMsgPosition = arrayList.size() - 1;
                    // Notify recycler view insert one new data.
                    adapter.notifyItemInserted(newMsgPosition);
                    // Scroll RecyclerView to the last message.
                    messageRecycler.scrollToPosition(newMsgPosition);

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                arrayList.clear();
                Toast.makeText(Messages.this, "Fetch error :" +error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void sendMessageFunction()
    {
        DatabaseReference senderRef=mRef.child(userID).child("users").child(messageUserID).child("message");
        String randomMessageKey=senderRef.push().getKey();
        HashMap CustomMap = new HashMap();
        CustomMap.put("/"+userID+"/users/"+messageUserID+"/message/"+randomMessageKey+"/",messageMap);
        if(!userID.equals(messageUserID)) {
            CustomMap.put("/"+messageUserID+"/users/"+userID+"/message/"+randomMessageKey+"/",messageMap);
        }
        mRef.updateChildren(CustomMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
//                Toast.makeText(Messages.this, "Message Sent!", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(Messages.this, "Error:>> "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void addProfileInfo()
    {

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    User userData=snapshot.getValue(User.class);
                    assert userData != null;
                    Picasso.get().load(userData.getProfilePic()).into(mProfilePic);
                    mUsername.setText(userData.getUsername());
                }
                else Toast.makeText(Messages.this, "Error: Occurred ", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                Toast.makeText(Messages.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}