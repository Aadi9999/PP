package com.Aadi.PP.Cards;

import android.content.Context;
import android.content.SharedPreferences;

import android.text.Html;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.delight.android.location.SimpleLocation;

import static android.content.Context.MODE_PRIVATE;


public class arrayAdapter extends ArrayAdapter<cards>{


    private DatabaseReference mUserDatabase;

    private String longitude2, latitude2, orgname;
    private SimpleLocation location;
    private Double lat2;
    private Double long2;
    private String currentUId2;
    private DatabaseReference usersDb;

    Context context;
    public static final String SHARED_PREFS = "sharedPrefs";

    public arrayAdapter(NavigationView.OnNavigationItemSelectedListener context, int resourceId, List<cards> items){
        super((Context) context, resourceId, items);
    }
    public View getView(int position, View convertView, ViewGroup parent){

        cards card_item = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View convertView2 = inflater.inflate(R.layout.activity_main, null);


        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);

        }

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUId2 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId2);

        TextView mLocation = convertView.findViewById(R.id.location);
        TextView name = convertView.findViewById(R.id.name);
        TextView longitude = convertView.findViewById(R.id.location);
        ImageView image = convertView.findViewById(R.id.image);
        TextView sports = convertView.findViewById(R.id.sports);
        ImageView sportsicon = convertView.findViewById(R.id.sporticon);
        TextView emptyView = convertView.findViewById(R.id.empty_view);


        name.setText(card_item.getName());


        if (card_item.getLongitude2() != null){
            longitude2 = (card_item.getLongitude2());
            latitude2 = (card_item.getLatitude2());
            long2 = Double.valueOf(longitude2);
            lat2 = Double.valueOf(latitude2);

            location = new SimpleLocation(getContext());

            final double latitude1 = location.getLatitude();
            final double longitude1 = location.getLongitude();

            double startLatitude = Double.valueOf(latitude1);
            double startLongitude = Double.valueOf(longitude1);
            double endLatitude = lat2;
            double endLongitude = long2;
            double distance = SimpleLocation.calculateDistance(startLatitude, startLongitude, endLatitude, endLongitude);

            if (location != null) {
                mLocation.setText(Integer.toString((int) distance / 1000) + "km away");
            }

        }




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
