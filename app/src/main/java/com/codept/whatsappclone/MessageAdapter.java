package com.codept.whatsappclone;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.myViewHolder> {

    Context context;
    ArrayList<messageClass> arrayList=new ArrayList<>();

    public MessageAdapter(Context context, ArrayList<messageClass> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_layout, parent, false);

        return new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageAdapter.myViewHolder holder, int position) {
        messageClass mesClass=arrayList.get(position);
        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        String userID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        //If I sent the message
        if(TextUtils.equals(userId,mesClass.getSender()))
        {
            holder.sMessage.setText(mesClass.getMessage());
            holder.sentCard.setVisibility(View.VISIBLE);
            if(mesClass.getSeen().equals("true"))
            {
                holder.sentStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.seen_icon));

            }else if(mesClass.getReceived().equals("true")){
                holder.sentStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.recieved_icon));

            }else if(mesClass.getSent().equals("true"))
            {
                holder.sentStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.sent_icon));
            }
        }else{
            holder.rMessage.setText(mesClass.getMessage());
            holder.incomingMessageCard.setVisibility(View.VISIBLE);
        }

        if(TextUtils.equals(userID,mesClass.getReceiver())){
        DatabaseReference messageRef= FirebaseDatabase.getInstance().getReference()
                .child("messages").child(mesClass.getSender()).child("users")
                .child(mesClass.getReceiver()).child("message").child(mesClass.getMessageID());
        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String seen=snapshot.child("seen").getValue().toString();
                    if(seen.equals("false")){
                        messageRef.child("seen").setValue("true");
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView sMessage,sTime;
        ImageView sStatus;
        TextView rMessage,rTime;
        LinearLayout sentCard,incomingMessageCard;
        ImageView sentStatus;
        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            sMessage=itemView.findViewById(R.id.sentMessage);
            sTime=itemView.findViewById(R.id.sentTime);
            sStatus=itemView.findViewById(R.id.sentStatusImage);
            rMessage=itemView.findViewById(R.id.incomingMessage);
            rTime=itemView.findViewById(R.id.incomingTime);
            sentCard=itemView.findViewById(R.id.sentMessageCard);
            incomingMessageCard=itemView.findViewById(R.id.incomingMessageCard);
            sentStatus=itemView.findViewById(R.id.sentStatusImage);


        }
    }


}
