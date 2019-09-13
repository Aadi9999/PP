package com.Aadi.PP.Matches;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.Aadi.PP.R;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolders>{
    private List<MatchesObject> matchesList;
    private Context context;




    public MatchesAdapter(List<MatchesObject> matchesList, Context context){
        this.matchesList = matchesList;
        this.context = context;
    }

    @Override
    public MatchesViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        MatchesViewHolders rcv = new MatchesViewHolders(layoutView);

        return rcv;
    }




    @Override
    public void onBindViewHolder(MatchesViewHolders holder, int position) {
        holder.mMatchId.setText(matchesList.get(position).getUserId());
            holder.mMatchName.setText(matchesList.get(position).getName());
        holder.mMatchName.setText(matchesList.get(position).getName());




        //Setting sport icon for Matches Activity
        if (matchesList.get(position).getSports().equals("Basketball")) {
            holder.mSportsicon.setImageResource(R.drawable.basketball);
        }

        if (matchesList.get(position).getSports().equals("Badminton")) {
            holder.mSportsicon.setImageResource(R.drawable.badminton);
        }

        if (matchesList.get(position).getSports().equals("Football")) {
            holder.mSportsicon.setImageResource(R.drawable.soccer);
        }

        if (matchesList.get(position).getSports().equals("Table Tennis")) {
            holder.mSportsicon.setImageResource(R.drawable.pingpong);
        }

        if (matchesList.get(position).getSports().equals("Exercising")) {
            holder.mSportsicon.setImageResource(R.drawable.gym);
        }

        if (matchesList.get(position).getSports().equals("Baseball")) {
            holder.mSportsicon.setImageResource(R.drawable.baseball);
        }

        if (matchesList.get(position).getSports().equals("Tennis")) {
            holder.mSportsicon.setImageResource(R.drawable.tennis);
        }

        if (matchesList.get(position).getSports().equals("Archery")) {
            holder.mSportsicon.setImageResource(R.drawable.archery);
        }

        if (matchesList.get(position).getSports().equals("Snooker")) {
            holder.mSportsicon.setImageResource(R.drawable.eightball);
        }

        if (matchesList.get(position).getSports().equals("Cycling")) {
            holder.mSportsicon.setImageResource(R.drawable.cycling);
        }

        if (matchesList.get(position).getSports().equals("Golf")) {
            holder.mSportsicon.setImageResource(R.drawable.golf);
        }

        if (matchesList.get(position).getSports().equals("Rugby")) {
            holder.mSportsicon.setImageResource(R.drawable.rugby);
        }

        if (matchesList.get(position).getSports().equals("Running")) {
            holder.mSportsicon.setImageResource(R.drawable.shoe);
        }

        if (matchesList.get(position).getSports().equals("American Football")) {
            holder.mSportsicon.setImageResource(R.drawable.americanfootball);
        }

        if (matchesList.get(position).getSports().equals("Volleyball")) {
            holder.mSportsicon.setImageResource(R.drawable.volleyball);
        }

        if (matchesList.get(position).getSports().equals("Cricket")) {
            holder.mSportsicon.setImageResource(R.drawable.cricket);
        }

        if (matchesList.get(position).getSports().equals("default")) {
            holder.mSportsicon.setImageResource(R.drawable.shoe);
        }
        //Setting sport icon for Matches Activity

        holder.UserLastSeen.setText(matchesList.get(position).getUserLastSeen());

        if (0 == position) {
            holder.mMatchName.setTextColor(Color.parseColor("#000000"));
        }
        if (matchesList.get(position).getUserLastSeen().equals("online")) {
            holder.UserLastSeen.setTextColor(Color.parseColor("#00B424"));
        }
        if(!matchesList.get(position).getProfileImageUrl().equals("default")){
            Glide.with(context).load(matchesList.get(position).getProfileImageUrl()).into(holder.mMatchImage);
        }
    }

    @Override
    public int getItemCount() {
        return this.matchesList.size();
    }


}
