package com.Aadi.PP;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.Aadi.PP.Matches.MatchesFragment;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import im.delight.android.location.SimpleLocation;

public class mActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private String name, currentUId, profileImageUrl, userId;
    private DatabaseReference usersDb;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private Toolbar toolbar;
    private SharedPreferences sharePrefObje;
    private SimpleLocation location;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.navigation);

        // Create items
            AHBottomNavigationItem item1 = new AHBottomNavigationItem("Connects", R.drawable.users, R.color.whiteTextColor);
            AHBottomNavigationItem item2 = new AHBottomNavigationItem("Home", R.drawable.home, R.color.whiteTextColor);
            AHBottomNavigationItem item3 = new AHBottomNavigationItem("Settings", R.drawable.settings, R.color.whiteTextColor);

        // Add items
            bottomNavigation.addItem(item1);
            bottomNavigation.addItem(item2);
            bottomNavigation.addItem(item3);

        // Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.WHITE);
        bottomNavigation.setAccentColor(Color.parseColor("#000000"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));



        // Manage titles
            bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);


        // Set current item programmatically
            bottomNavigation.setCurrentItem(1);


        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
             if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                 Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                 if (dataSnapshot.child("New Notification").getValue() != null) {
                     if (dataSnapshot.child("New Notification").getValue().equals("true")){
                         bottomNavigation.setNotification("1", 0);

                     }
                     else {
                         bottomNavigation.setNotification("0", 0);
                     }


                 }

             }
         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
     });





        // Set listener
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                //show fragment
                if (position==0)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new MatchesFragment()).commit();

                }else  if (position==1)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new MapFragment()).commit();

                }else  if (position==2)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new SettingsFragment()).commit();

                }

                return true;
            }

        });

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



        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MapFragment()).commit();
        }


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
    protected void onResume() {
        super.onResume();

        updateUserStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();

        updateUserStatus("offline");
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode,resultCode,data);

    }


    }

