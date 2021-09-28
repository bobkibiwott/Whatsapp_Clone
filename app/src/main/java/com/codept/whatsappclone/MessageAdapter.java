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

        //If I sent the message
        if(TextUtils.equals(userId,mesClass.getSender()))
        {
            holder.sMessage.setText(mesClass.getMessage());
            holder.sentCard.setVisibility(View.VISIBLE);
        }else{
            holder.rMessage.setText(mesClass.getMessage());
            holder.incomingMessageCard.setVisibility(View.VISIBLE);
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
        LinearLayout sentCard;
        RelativeLayout incomingMessageCard;
        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            sMessage=itemView.findViewById(R.id.sentMessage);
            sTime=itemView.findViewById(R.id.sentTime);
            sStatus=itemView.findViewById(R.id.sentStatusImage);
            rMessage=itemView.findViewById(R.id.incomingMessage);
            rTime=itemView.findViewById(R.id.incomingTime);
            sentCard=itemView.findViewById(R.id.sentMessageCard);
            incomingMessageCard=itemView.findViewById(R.id.incomingMessageCard);


        }
    }


}
