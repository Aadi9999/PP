package com.Aadi.PP;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.Aadi.PP.Matches.MatchesFragment;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
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



        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //All location services are disabled
            Intent intent = new Intent(mActivity.this, LocationActivity.class);
            location.endUpdates();
            startActivity(intent);
            finish();
            return;
        }

        else{

            location = new SimpleLocation(mActivity.this);
            location.beginUpdates();

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





}

    public static Boolean isLocationEnabled(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
// This is new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
// This is Deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return  (mode != Settings.Secure.LOCATION_MODE_OFF);

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
        location.beginUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();

        updateUserStatus("offline");
        location.endUpdates();
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode,resultCode,data);

    }


    }

