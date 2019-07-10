package com.Aadi.PP;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.Aadi.PP.Cards.arrayAdapter;
import com.Aadi.PP.Cards.cards;
import com.Aadi.PP.Matches.MatchesFragment;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import im.delight.android.location.SimpleLocation;
import in.arjsna.swipecardlib.SwipeCardView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private cards cards_data[];
    private com.Aadi.PP.Cards.arrayAdapter arrayAdapter;
    private com.Aadi.PP.Cards.arrayAdapter arrayAdapter2;


    private SimpleLocation location;
    private FirebaseAuth mAuth;
    private SharedPreferences sharePrefObje;

    private TextView mPatient;
    private String currentUId, name, profileImageUrl, userId, longitude2, latitude2, skill;
    private int returnVal, radiusint, distanceint;
    private Double lat2, radiusdouble;
    private Double long2, distance;
    private DatabaseReference usersDb;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private DatabaseReference mUserDatabase;


    ListView listView;
    List<cards> rowItems;



        @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            setTheme(R.style.AppTheme);

            sharePrefObje = getSharedPreferences("PREFERENCENAME", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharePrefObje.edit();
            editor.putBoolean("isLoginKey",true);
            editor.commit();

            dl = findViewById(R.id.activity_main);
            t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

            dl.addDrawerListener(t);
            t.syncState();
            Toolbar toolbar = findViewById(R.id.toolBar);
            setSupportActionBar(toolbar);
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

            nv = findViewById(R.id.nv);
            nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    switch(id)
                    {
                        case R.id.matches:
                            Intent intent = new Intent(MainActivity.this, MatchesFragment.class);
                            startActivity(intent);
                            finish();
                            return true;
                        case R.id.settings:
                            Intent intent2 = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intent2);
                            finish();
                            return true;
                        case R.id.browse:
                            Intent intent3 = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent3);
                            finish();
                            return true;
                        case android.R.id.home:
                            dl.openDrawer(GravityCompat.START);
                            return true;

                        case R.id.log_out:
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Logout From SportConnect")
                                    .setMessage("Would you like to Log out?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            updateUserStatus("offline");
                                            Intent inte = new Intent(MainActivity.this, ChooseLoginRegistrationActivity.class);
                                            sharePrefObje = getSharedPreferences("PREFERENCENAME", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharePrefObje.edit();
                                            editor.putBoolean("isLoginKey",false);
                                            editor.commit();
                                            FirebaseAuth.getInstance().signOut();
                                            startActivity(inte);
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();

                    }

                    return true;


                }
            });


        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

                currentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Button mQuestion = findViewById(R.id.questionsign);



        ShowHeaderInfo();
        checkUserSex();
        checkFirstOpen();

        final TextView emptyView = findViewById(R.id.empty_view);


        rowItems = new ArrayList<cards>();
        final LottieAnimationView animationView = findViewById(R.id.animation_view);

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );

            final SwipeCardView swipeCardView = findViewById(R.id.frame);


            location = new SimpleLocation(MainActivity.this);

            final double latitude1 = location.getLatitude();
            final double longitude1 = location.getLongitude();

            Map currentLocationMap = new HashMap();
            currentLocationMap.put("latitude",latitude1);
            currentLocationMap.put("longitude",longitude1);


            usersDb.child(currentUId).child("location")
                    .updateChildren(currentLocationMap);


        swipeCardView.setAdapter(arrayAdapter);




            swipeCardView.setFlingListener(new SwipeCardView.OnCardFlingListener() {



                @Override
                public void onCardExitLeft(Object dataObject) {

                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("nope").child(currentUId).setValue(true);
                Toasty.error(MainActivity.this, "Nope", Toast.LENGTH_SHORT).show();

                }


                @Override
                public void onCardExitRight(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("yeps").child(currentUId).setValue(true);
                isConnectionMatch(userId);
                Toasty.success(MainActivity.this, "Connect!", Toast.LENGTH_SHORT).show();
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


    private void ShowHeaderInfo(){
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        name = map.get("name").toString();



                        View header = nv.getHeaderView(0);
                        TextView nam = header.findViewById(R.id.nam);
                        nam.setText(name);
                    }


                    View header = nv.getHeaderView(0);
                    ImageView img = header.findViewById(R.id.img);
                    Glide.clear(img);
                    if(map.get("profileImageUrl")!=null){
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch(profileImageUrl){
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.profilepic).into(img);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileImageUrl).into(img);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.Proximity:
                Intent intent2 = new Intent(MainActivity.this, MapFragment.class);
                startActivity(intent2);
                finish();
                return true;

        }

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);

    }


    private void checkFirstOpen() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {



        } else if (savedVersionCode == DOESNT_EXIST) {


            return;

        } else if (currentVersionCode > savedVersionCode) {


            return;
        }


        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }

    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("yeps").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    new FancyAlertDialog.Builder(MainActivity.this)
                            .setTitle("New Connection!")
                            .setBackgroundColor(Color.parseColor("#6200EE"))  //Don't pass R.color.colorvalue
                            .setMessage("Head over to Connects screen and chat with user!")
                            .setNegativeBtnText("BROWSE")
                            .setPositiveBtnBackground(Color.parseColor("#6200EE"))
                            .setPositiveBtnText("MESSAGE")
                            .setNegativeBtnBackground(Color.parseColor("#009DEE"))  //Don't pass R.color.colorvalue
                            .setAnimation(Animation.POP)
                            .isCancellable(true)
                            .setIcon(R.drawable.ic_star_border_black_24dp,Icon.Visible)
                            .OnPositiveClicked(new FancyAlertDialogListener() {
                                @Override
                                public void OnClick() {
                                    Intent intent = new Intent(MainActivity.this, MatchesFragment.class);
                                    startActivity(intent);
                                    finish();
                                    return;
                                }
                            })
                            .OnNegativeClicked(new FancyAlertDialogListener() {
                                @Override
                                public void OnClick() {


                                }
                            })
                            .build();

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
    protected void onResume() {
        super.onResume();
        if (location != null) {
            location.beginUpdates();
        }
        updateUserStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();

        updateUserStatus("offline");

    }



    public void getOppositeSexUsers(){
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("sex").getValue() != null) {
                    if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentUId) && !name.equals(dataSnapshot.child("name").getValue().toString()) && !dataSnapshot.child("connections").child("yeps").hasChild(currentUId) && dataSnapshot.child("sex").getValue().toString().equals(oppositeUserSex)){
                        String profileImageUrl = "default";
                        if (!dataSnapshot.child("profileImageUrl").getValue().equals("default")) {
                            profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                        }
                        if(dataSnapshot.child("location").getValue()!=null){
                            longitude2 = dataSnapshot.child("location").child("longitude").getValue().toString();
                            latitude2 = dataSnapshot.child("location").child("latitude").getValue().toString();
                            long2 = Double.valueOf(longitude2);
                            lat2 = Double.valueOf(latitude2);

                            location = new SimpleLocation(MainActivity.this);

                            final double latitude1 = location.getLatitude();
                            final double longitude1 = location.getLongitude();

                            double startLatitude = Double.valueOf(latitude1);
                            double startLongitude = Double.valueOf(longitude1);
                            double endLatitude = lat2;
                            double endLongitude = long2;
                            distance = SimpleLocation.calculateDistance(startLatitude, startLongitude, endLatitude, endLongitude);

                        }

                        if(dataSnapshot.child("skill").getValue()!=null){
                            skill = dataSnapshot.child("skill").getValue().toString();
                        }

                        cards item = new cards(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString(), dataSnapshot.child("sports").getValue().toString(), skill, longitude2, latitude2, profileImageUrl);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}