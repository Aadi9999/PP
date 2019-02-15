package com.Aadi.PP.Cards;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Aadi.PP.MainActivity;
import com.bumptech.glide.Glide;
import com.Aadi.PP.R;

import java.util.List;

import es.dmoral.toasty.Toasty;


public class arrayAdapter extends ArrayAdapter<cards>{

    Context context;

    public arrayAdapter(NavigationView.OnNavigationItemSelectedListener context, int resourceId, List<cards> items){
        super((Context) context, resourceId, items);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        cards card_item = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        ImageView image = convertView.findViewById(R.id.image);
        TextView sports = convertView.findViewById(R.id.sports);
        ImageView sportsicon = convertView.findViewById(R.id.sporticon);
        TextView emptyView = convertView.findViewById(R.id.empty_view);


        name.setText(card_item.getName());

        
        if (card_item.getSports() == "Badminton"){
            sportsicon.setImageResource(R.drawable.badminton);
        }

        if (card_item.getSports().equals("Basketball")){
            sportsicon.setImageResource(R.drawable.basketball);
        }

        if (card_item.getSports().equals("Football")){
            sportsicon.setImageResource(R.drawable.soccer);
        }

        if (card_item.getSports().equals("Table Tennis")){
            sportsicon.setImageResource(R.drawable.pingpong);
        }

        if (card_item.getSports().equals("Exercising")){
            sportsicon.setImageResource(R.drawable.gym);
        }

        if (card_item.getSports().equals("Baseball")){
            sportsicon.setImageResource(R.drawable.baseball);
        }

        if (card_item.getSports().equals("Tennis")){
            sportsicon.setImageResource(R.drawable.tennis);
        }

        if (card_item.getSports().equals("Archery")){
            sportsicon.setImageResource(R.drawable.archery);
        }

        if (card_item.getSports().equals("Snooker")){
            sportsicon.setImageResource(R.drawable.eightball);
        }

        if (card_item.getSports().equals("Cycling")){
            sportsicon.setImageResource(R.drawable.cycling);
        }

        if (card_item.getSports().equals("Golf")){
            sportsicon.setImageResource(R.drawable.golf);
        }

        if (card_item.getSports().equals("Rugby")){
            sportsicon.setImageResource(R.drawable.rugby);
        }

        if (card_item.getSports().equals("Running")){
            sportsicon.setImageResource(R.drawable.shoe);
        }

        if (card_item.getSports().equals("American Football")){
            sportsicon.setImageResource(R.drawable.americanfootball);
        }

        if (card_item.getSports().equals("Volleyball")){
            sportsicon.setImageResource(R.drawable.volleyball);
        }

        if (card_item.getSports().equals("Cricket")){
            sportsicon.setImageResource(R.drawable.cricket);
        }

        if (card_item.getSports().equals("default")){
            sportsicon.setImageResource(R.drawable.shoe);
        }


        switch(card_item.getProfileImageUrl()){
            case "default":
                Glide.with(convertView.getContext()).load(R.drawable.profilepic2).into(image);
                break;
            default:
                Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);
                break;
        }


        return convertView;

    }

}
