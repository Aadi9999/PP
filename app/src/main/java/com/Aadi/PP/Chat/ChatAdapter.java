package com.Aadi.PP.Chat;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.Aadi.PP.Chat.ChatObject;
import com.Aadi.PP.Chat.ChatViewHolders;
import com.bumptech.glide.Glide;
import com.Aadi.PP.R;

import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders>{
    private List<ChatObject> chatList;
    private Context context;


    public ChatAdapter(List<ChatObject> matchesList, Context context){
        this.chatList = matchesList;
        this.context = context;
    }

    @Override
    public ChatViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolders rcv = new ChatViewHolders(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(ChatViewHolders holder, int position) {
        holder.mMessage.setText(chatList.get(position).getMessage());
        if(chatList.get(position).getCurrentUser()){
            holder.mMessage.setTextColor(Color.parseColor("#404040"));
            holder.mMessage.setBackgroundResource(R.drawable.rounded_rectangle_orange);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mMessage.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            holder.mMessage.setLayoutParams(params);

        }else{
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#2DB4C8"));
            holder.mContainer.setBackgroundResource(R.drawable.rounded_rectangle_blue);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mMessage.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            holder.mMessage.setLayoutParams(params);
        }

    }



    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}