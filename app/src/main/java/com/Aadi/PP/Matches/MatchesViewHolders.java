package com.Aadi.PP.Matches;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.Aadi.PP.Chat.ChatActivity;
import com.Aadi.PP.R;
import com.github.siyamed.shapeimageview.CircularImageView;


public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMatchId, mMatchName, UserLastSeen, mInfo;
    public ImageView mMatchImage, mSportsicon;
    public CircularImageView circularImageView;

    public MatchesViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchId = itemView.findViewById(R.id.Matchid);
        mMatchName = itemView.findViewById(R.id.MatchName);
        UserLastSeen = itemView.findViewById(R.id.user_last_seen);
        CircularImageView circularImageView = itemView.findViewById(R.id.MatchName);
        mMatchImage = itemView.findViewById(R.id.MatchImage);
        mSportsicon = itemView.findViewById(R.id.sporticon2);



    }

    @Override
    public void onClick(View view) {


        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle b = new Bundle();
        b.putString("matchId", mMatchId.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);

    }
}
