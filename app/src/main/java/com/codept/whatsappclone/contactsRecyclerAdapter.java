package com.codept.whatsappclone;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class contactsRecyclerAdapter extends RecyclerView.Adapter<contactsRecyclerAdapter.myViewHolder> {

    Context context;
    ArrayList<User> userArrayList=new ArrayList<>();

    public contactsRecyclerAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lay_contacts, parent, false);

        return new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull contactsRecyclerAdapter.myViewHolder holder, int position) {
//        User user=userArrayList.get(position);
        holder.username.setText(userArrayList.get(position).getUsername());
        holder.status.setText(userArrayList.get(position).getStatus());
        Picasso.get().load(userArrayList.get(position).getProfilePic()).into(holder.profilePic);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,Messages.class);
                intent.putExtra("messageID",userArrayList.get(position).getUid());
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView username,status;
        CircleImageView profilePic;
        CardView cardView;
        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.contactUsername);
            status=itemView.findViewById(R.id.contactStatus);
            profilePic=itemView.findViewById(R.id.contactProfile);
            cardView=itemView.findViewById(R.id.contactCard);

        }
    }
}
