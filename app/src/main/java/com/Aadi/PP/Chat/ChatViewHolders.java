package com.Aadi.PP.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Aadi.PP.R;


public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMessage;
    public TextView mTimestamp;
    public RelativeLayout mContainer;
    public ChatViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMessage = itemView.findViewById(R.id.message);
        mTimestamp = itemView.findViewById(R.id.timestamp);
        mContainer = itemView.findViewById(R.id.container);

    }



    @Override
    public void onClick(View view) {
    }
}
