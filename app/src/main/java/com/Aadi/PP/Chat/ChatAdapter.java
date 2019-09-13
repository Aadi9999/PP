package com.Aadi.PP.Chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Aadi.PP.Chat.ChatObject;
import com.Aadi.PP.Chat.ChatViewHolders;
import com.Aadi.PP.Matches.MatchesViewHolders;
import com.bumptech.glide.Glide;
import com.Aadi.PP.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static com.Aadi.PP.R.drawable.rounded_rectangle_blue;


public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders>{
    private List<ChatObject> chatList;
    private Context context;
    private TextView last_msg;
    private boolean ischat;

    String theLastMessage;


    public ChatAdapter(List<ChatObject> chatList, Context context){
        this.chatList = chatList;
        this.context = context;
    }

    @Override
    public ChatViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolders rcv = new ChatViewHolders(layoutView);

        return rcv;
    }



    @Override
    public void onBindViewHolder(ChatViewHolders holder, int position) {



        holder.mMessage.setText(chatList.get(position).getMessage());
        holder.mTimestamp.setText(chatList.get(position).getTimestamp());
        if(chatList.get(position).getCurrentUser()){
            holder.mMessage.setBackgroundResource(R.drawable.rounded_rectangle_blu);
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.mMessage.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.mMessage.setLayoutParams(lp);
            RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) holder.mTimestamp.getLayoutParams();
            lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.mTimestamp.setLayoutParams(lp2);



        }else{
            holder.mMessage.setGravity(Gravity.START);
            holder.mMessage.setTextColor(Color.parseColor("#000000"));
            holder.mMessage.setBackgroundResource(R.drawable.rounded_rectangle_orange);


        }

    }



    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}