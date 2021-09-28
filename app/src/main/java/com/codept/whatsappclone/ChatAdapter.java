package com.codept.whatsappclone;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference()
                .child("messages").child(mAuth.getCurrentUser().getUid())
                .child(arrayList.get(position).getUserID()).child("message");
        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference()
                .child("users").child(arrayList.get(position).getUserID());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    User user=snapshot.getValue(User.class);
                    assert user != null;
                    holder.username.setText(user.getUsername());
                    Picasso.get().load(user.getProfilePic()).into(holder.profilePic);
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
                        ChatModel chatModel=snapshot1.getValue(ChatModel.class);
                        assert chatModel != null;
                        holder.message.setText(chatModel.getMessage());

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





    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView username,message;
        CircleImageView profilePic;
        CardView cardView;
        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.contactUsername);
            message=itemView.findViewById(R.id.contactMessage);
            profilePic=itemView.findViewById(R.id.contactProfile);
            cardView=itemView.findViewById(R.id.contactCard);
        }
    }
}