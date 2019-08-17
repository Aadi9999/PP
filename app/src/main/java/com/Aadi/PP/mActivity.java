package com.Aadi.PP;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.Aadi.PP.Matches.MatchesFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import im.delight.android.location.SimpleLocation;

public class mActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private String name, currentUId, profileImageUrl;
    private DatabaseReference usersDb;
    private Toolbar toolbar;
    private SharedPreferences sharePrefObje;
    private SimpleLocation location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        location = new SimpleLocation(mActivity.this);

        final double latitude1 = location.getLatitude();
        final double longitude1 = location.getLongitude();

        Map currentLocationMap = new HashMap();
        currentLocationMap.put("latitude",latitude1);
        currentLocationMap.put("longitude",longitude1);


        usersDb.child(currentUId).child("location")
                .updateChildren(currentLocationMap);


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(this);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MapFragment()).commit();
            navigationView.setCheckedItem(R.id.browse);
        }
        ShowHeaderInfo();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.matches:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MatchesFragment()).commit();
                break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MatchesFragment()).commit();
                break;
            case R.id.browse:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MapFragment()).commit();
                break;
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;

            case R.id.log_out:
                new AlertDialog.Builder(mActivity.this)
                        .setTitle("Logout From SportConnect")
                        .setMessage("Would you like to Log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sharePrefObje = getSharedPreferences("PREFERENCENAME", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharePrefObje.edit();
                                editor.clear();
                                editor.apply();
                                updateUserStatus("offline");
                                Intent inte = new Intent(mActivity.this, PhoneoneActivity.class);
                                FirebaseAuth.getInstance().signOut();
                                startActivity(inte);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();


        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void ShowHeaderInfo(){
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        name = map.get("name").toString();


                        View header = navigationView.getHeaderView(0);
                        TextView nam = header.findViewById(R.id.nam);
                        nam.setText(name);
                    }


                    View header = navigationView.getHeaderView(0);
                    ImageView img = header.findViewById(R.id.img);
                    Glide.clear(img);
                    if(map.get("profileImageUrl")!=null){
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch(profileImageUrl){
                            case "default":
                                Glide.with(mActivity.this.getApplication()).load(R.drawable.profilepic).into(img);
                                break;
                            default:
                                Glide.with(mActivity.this.getApplication()).load(profileImageUrl).into(img);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateUserStatus(String state)
    {
        String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calForTime.getTime());

        Map currentStateMap = new HashMap();
        currentStateMap.put("time",saveCurrentTime);
        currentStateMap.put("date",saveCurrentDate);
        currentStateMap.put("type",state);


        usersDb.child(currentUId).child("userState")
                .updateChildren(currentStateMap);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
