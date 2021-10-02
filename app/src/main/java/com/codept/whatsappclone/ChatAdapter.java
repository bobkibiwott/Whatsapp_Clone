package com.codept.whatsappclone;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.viewHolder> {
    Context context;
    ArrayList<ChatModel> arrayList=new ArrayList<>();

    public ChatAdapter(Context context, ArrayList<ChatModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_lay, parent, false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatAdapter.viewHolder holder, int position) {
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String userID=mAuth.getCurrentUser().getUid();
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference()
                .child("messages").child(mAuth.getCurrentUser().getUid()).child("users")
                .child(arrayList.get(position).getUserID()).child("message");
        mRef.keepSynced(true);
        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference()
                .child("users").child(arrayList.get(position).getUserID());
        userRef.keepSynced(true);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    User user=snapshot.getValue(User.class);
                    assert user != null;
                    holder.username.setText(user.getUsername());
//                    holder.
                    Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.profile)
                            .networkPolicy(NetworkPolicy.OFFLINE).into(holder.profilePic, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.profile)
                                    .into(holder.profilePic);

                        }
                    });
                }


            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        Query query=mRef.limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot snapshot1:snapshot.getChildren())
                    {

                        messageClass model=snapshot1.getValue(messageClass.class);
                        assert model != null;
                        TimeClass timeClass=new TimeClass(model.getTimestamp());
                        holder.message.setText(model.getMessage());
                        holder.cardView.setVisibility(View.VISIBLE);
                        holder.lastTime.setText(timeClass.getTime());



                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Messages.class);
                intent.putExtra("messageUserID",arrayList.get(position).getUserID());
                context.startActivity(intent);
            }
        });

        ///////

        //////____sending a delivery notice________//////


            if(!userID.equals(arrayList.get(position).getUserID()))
            {

                DatabaseReference mesRef=FirebaseDatabase.getInstance().getReference()
                        .child("messages").child(arrayList.get(position).getUserID())
                        .child("users").child(userID).child("message");
                mesRef.keepSynced(true);
                mesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            int messageCount=0;
                            for(DataSnapshot snapshot1:snapshot.getChildren()){
                                messageClass model=snapshot1.getValue(messageClass.class);
                                assert model != null;
                                model.setMessageID(snapshot1.getKey());
                                if(model.getReceived().equals("false")&&model.getReceiver().equals(userID)&&!arrayList.get(position).getUserID().isEmpty()){
                                    mesRef.child(model.getMessageID()).child("received").setValue("true");
                                }
                                if(TextUtils.equals(model.getSeen(),"false")&&TextUtils.equals(model.getReceiver(),userID))
                                {
                                    messageCount=messageCount+1;
                                    holder.message.setTextColor(context.getResources().getColor(R.color.light_green));
                                }else{
                                    holder.message.setTextColor(context.getResources().getColor(R.color.light_gray));

                                }
                                ///////counting unread messages here//////////////
                                if(messageCount>0)
                                {
                                    holder.messageCount.setText(messageCount+"");
                                    holder.messageCountCard.setVisibility(View.VISIBLE);
                                }else {
                                    holder.messageCountCard.setVisibility(View.INVISIBLE);
                                }

                                ////////adding blue button and design to the chat adapter
                                if(model.getSender().equals(userID))
                                {


                                    if(model.getSeen().equals("true"))
                                    {
                                        holder.messageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.seen_icon));

                                    }else if(model.getReceived().equals("true"))
                                    {

                                        holder.messageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.recieved_icon));

                                    }else if(model.getSent().equals("true"))
                                    {
                                        holder.messageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.sent_icon));
                                    }else{

                                        holder.messageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.not_sent_yet_icon));
                                        
                                    }
                                    holder.messageStatus.setVisibility(View.VISIBLE);
                                }else{
                                    holder.messageStatus.setVisibility(View.GONE);

                                }




                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });




        }




        //////






    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView username,message,messageCount,lastTime;
        CircleImageView profilePic;
        CardView cardView,messageCountCard;
        ImageView messageStatus;
        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.contactUsername);
            message=itemView.findViewById(R.id.contactMessage);
            profilePic=itemView.findViewById(R.id.contactProfile);
            cardView=itemView.findViewById(R.id.contactCard);
            messageStatus=itemView.findViewById(R.id.sentStatusImage);
            messageCount=itemView.findViewById(R.id.messageCount);
            messageCountCard=itemView.findViewById(R.id.messageCountCard);
            lastTime=itemView.findViewById(R.id.lastTime);
        }
    }
}
