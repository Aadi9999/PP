package com.Aadi.PP;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.Aadi.PP.Pager.PagerActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.Aadi.PP.Cards.arrayAdapter;
import com.Aadi.PP.Cards.cards;
import com.Aadi.PP.Matches.MatchesActivity;
import com.onesignal.OneSignal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import in.arjsna.swipecardlib.SwipeCardView;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private cards cards_data[];
    private com.Aadi.PP.Cards.arrayAdapter arrayAdapter;
    private int i;


    private FirebaseAuth mAuth;

    private String currentUId;

    private DatabaseReference usersDb;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;




    ListView listView;
    List<cards> rowItems;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);




            AHBottomNavigation bottomNavigation = findViewById(R.id.bottom_navigation);

            // Managing Bottom Navigation Bar
            AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.Settings, R.drawable.settingsicon, R.color.colorPrimary);
            AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.Home, R.drawable.homeicon, R.color.colorPrimary);
            AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.Connects, R.drawable.usersicon, R.color.colorPrimary);
            AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.Logout, R.drawable.logouticon, R.color.colorPrimary);


            // Add items
            bottomNavigation.addItem(item1);
            bottomNavigation.addItem(item2);
            bottomNavigation.addItem(item3);
            bottomNavigation.addItem(item4);

            // Set background color
            bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#ffffff"));

            // Disable the translation inside the CoordinatorLayout
            bottomNavigation.setBehaviorTranslationEnabled(false);


            // Change colors
            bottomNavigation.setAccentColor(Color.parseColor("#6200EE"));


            // Force to tint the drawable (useful for font with icon for example)
            bottomNavigation.setForceTint(true);


            bottomNavigation.setTranslucentNavigationEnabled(true);

            // Manage titles
            bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);


            // Use colored navigation with circle reveal effect
            bottomNavigation.setColored(false);

            // Set current item programmatically
            bottomNavigation.setCurrentItem(1);


            // Set listeners
            bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
                @Override
                public boolean onTabSelected(int position, boolean wasSelected) {
                    switch (position) {
                        case 0:
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        finish();
                        return true;

                        case 2:
                            Intent inten = new Intent(MainActivity.this, MatchesActivity.class);
                            startActivity(inten);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                            return true;

                        case 3:

                                    new AlertDialog.Builder(MainActivity.this)
                                            .setTitle("Logout From SportConnect")
                                            .setMessage("Would you like to Log out?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    updateUserStatus("offline");
                                                    Intent inte = new Intent(MainActivity.this, ChooseLoginRegistrationActivity.class);
                                                    mAuth.signOut();
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


                    return wasSelected;
                }
            });

            usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

            Button mQuestion = findViewById(R.id.questionsign);


        checkUserSex();
            checkFirstOpen();

            final TextView emptyView = findViewById(R.id.empty_view);
            final TextView nope = findViewById(R.id.text);
            final TextView connect = findViewById(R.id.text2);

            nope.setVisibility(View.VISIBLE);
            ObjectAnimator fadein = ObjectAnimator.ofFloat(nope, "alpha", 0f, 1);
            fadein.setDuration(3000);
            fadein.start();

            connect.setVisibility(View.VISIBLE);
            ObjectAnimator fadein2 = ObjectAnimator.ofFloat(connect, "alpha", 0f, 1);
            fadein2.setDuration(3000);
            fadein2.start();


            rowItems = new ArrayList<cards>();
        final LottieAnimationView animationView = findViewById(R.id.animation_view);

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );

            SwipeCardView swipeCardView = findViewById(R.id.frame);



        swipeCardView.setAdapter(arrayAdapter);
            swipeCardView.setFlingListener(new SwipeCardView.OnCardFlingListener() {


                @Override
                public void onCardExitLeft(Object dataObject) {

                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("nope").child(currentUId).setValue(true);
                Toasty.error(MainActivity.this, "Nope", Toast.LENGTH_SHORT).show();
                    nope.setTextColor(Color.parseColor("#00B424"));
                    connect.setTextColor(Color.parseColor("#FFFFFF"));
                }


                @Override
                public void onCardExitRight(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("yeps").child(currentUId).setValue(true);
                isConnectionMatch(userId);
                Toasty.success(MainActivity.this, "Connect!", Toast.LENGTH_SHORT).show();
                    connect.setTextColor(Color.parseColor("#00B424"));
                    nope.setTextColor(Color.parseColor("#FFFFFF"));
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }


            @Override
            public void onScroll(float scrollProgressPercent) {

            }

                @Override
                public void onCardExitTop(Object dataObject) {

                }

                @Override
                public void onCardExitBottom(Object dataObject) {

                }
            });


        // Optionally add an OnItemClickListener
        mQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.warning(MainActivity.this, "Swipe right to connect!", Toast.LENGTH_SHORT).show();

                animationView.setAnimation("swipe_left.json");
                animationView.playAnimation();
                animationView.loop(false);
            }
        });

    }


    private void checkFirstOpen(){
        Boolean isFirstRun2 = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun2", true);

        if (isFirstRun2){

            new MaterialTapTargetPrompt.Builder(MainActivity.this)
                    .setTarget(R.id.text)
                    .setPrimaryText("Swipe left to not connect")
                    .show();


            new MaterialTapTargetPrompt.Builder(MainActivity.this)
                    .setTarget(R.id.questionsign)
                    .setPrimaryText("Click to learn more")
                    .show();

        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun2",
                false).apply();
    }

    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("yeps").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(MainActivity.this, "New Connection! Head over to Connects screen and chat with user!", Toast.LENGTH_LONG).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).child("ChatId").setValue(key);
                    usersDb.child(currentUId).child("connections").child("matches").child(dataSnapshot.getKey()).child("ChatId").setValue(key);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private String userSex;
    private String oppositeUserSex;
    public void checkUserSex(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("sex").getValue() != null){
                        userSex = dataSnapshot.child("sex").getValue().toString();
                        switch (userSex){
                            case "Male":
                                oppositeUserSex = "Male";
                                break;
                            case "Female":
                                oppositeUserSex = "Female";
                                break;
                        }
                        getOppositeSexUsers();
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
    protected void onStart() {
        super.onStart();

        updateUserStatus("online");
    }



    public void getOppositeSexUsers(){
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("sex").getValue() != null) {
                    if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentUId) && !dataSnapshot.child("connections").child("yeps").hasChild(currentUId) && dataSnapshot.child("sex").getValue().toString().equals(oppositeUserSex)) {
                        String profileImageUrl = "default";
                        if (!dataSnapshot.child("profileImageUrl").getValue().equals("default")) {
                            profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                        }
                        cards item = new cards(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString(), dataSnapshot.child("sports").getValue().toString(), profileImageUrl);
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}