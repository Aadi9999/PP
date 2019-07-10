package com.Aadi.PP;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Aadi.PP.Matches.MatchesFragment;
import com.Aadi.PP.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import im.delight.android.location.SimpleLocation;



public class MapFragment extends Fragment {


    private SharedPreferences sharePrefObje;
    private GoogleMap mMap, googleMap;
    private String longitude2, latitude2, uid, name, currentUId, profileImageUrl;
    private Double lat2;
    private Double long2, distance;
    private DatabaseReference usersDb;
    private Dialog myDialog;
    private Marker marker, marker2;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private SimpleLocation location;
    private NavigationView nv;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getActivity().getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        myDialog = new Dialog(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment



        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    getActivity(), R.raw.sportconnectmap));

                    if (!success) {
                        Log.e("Map", "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e("Map", "Can't find style.", e);
                }





        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();


                usersDb.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.child("location").getValue() != null) {
                            location = new SimpleLocation(getActivity());

                            String profileImageUrl = "default";
                            if (!dataSnapshot.child("profileImageUrl").getValue().equals("default")) {
                                profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                            }

                            longitude2 = dataSnapshot.child("location").child("longitude").getValue().toString();
                            latitude2 = dataSnapshot.child("location").child("latitude").getValue().toString();
                            long2 = Double.valueOf(longitude2);
                            lat2 = Double.valueOf(latitude2);

                            location = new SimpleLocation(getActivity());

                            final double latitude1 = location.getLatitude();
                            final double longitude1 = location.getLongitude();

                            double startLatitude = Double.valueOf(latitude1);
                            double startLongitude = Double.valueOf(longitude1);
                            double endLatitude = lat2;
                            double endLongitude = long2;
                            distance = SimpleLocation.calculateDistance(startLatitude, startLongitude, endLatitude, endLongitude);

                            LatLng latlng = new LatLng(lat2, long2);

                            Map<String, Integer> markers = new HashMap<String, Integer>();


                            //1: World
                            //5: Landmass/continent
                            //10: City
                            //15: Streets
                            //20: Buildings


                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat2, long2), 14.0f));

                            if (dataSnapshot.child("sports").getValue() != null) {
                                name = dataSnapshot.child("name").getValue().toString();
                                uid = dataSnapshot.getKey();

                                if (dataSnapshot.child("sports").getValue().toString() == "Badminton") {

                                    int height = 100;
                                    int width = 100;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.badminton);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                    marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .snippet(uid));

                                }

                                if (dataSnapshot.child("sports").getValue().toString().equals("Basketball")) {

                                    int height = 100;
                                    int width = 100;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.basketball);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                    marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .snippet(uid));

                                }

                                if (dataSnapshot.child("sports").getValue().toString().equals("Football")) {
                                    int height = 100;
                                    int width = 100;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.soccer);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                    marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .snippet(uid));


                                }

                                if (dataSnapshot.child("sports").getValue().toString().equals("Table Tennis")) {
                                    int height = 100;
                                    int width = 100;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.pingpong);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                    marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .snippet(uid));


                                }

                                if (dataSnapshot.child("sports").getValue().toString().equals("Exercising")) {
                                    int height = 100;
                                    int width = 100;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.gym);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                    marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .snippet(uid));


                                }

                                if (dataSnapshot.child("sports").getValue().toString().equals("Baseball")) {
                                    int height = 100;
                                    int width = 100;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.baseball);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                    marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .snippet(uid));


                                }

                                if (dataSnapshot.child("sports").getValue().toString().equals("Tennis")) {
                                    int height = 100;
                                    int width = 100;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.tennis);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                    marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .snippet(uid));

                                }

                                if (dataSnapshot.child("sports").getValue().toString().equals("Archery")) {
                                    int height = 100;
                                    int width = 100;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.archery);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                    marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .snippet(uid));


                                }

                                if (dataSnapshot.child("sports").getValue().toString().equals("Snooker")) {
                                    int height = 100;
                                    int width = 100;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.eightball);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                    marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .snippet(uid));


                                }

                                if (dataSnapshot.child("sports").getValue().toString().equals("Cycling")) {
                                    int height = 100;
                                    int width = 100;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.cycling);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                    marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .snippet(uid));


                                }

                                if (dataSnapshot.child("sports").getValue().toString().equals("Golf")) {
                                    int height = 100;
                                    int width = 100;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.golf);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                                    marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .snippet(uid));

                                }

                                if (dataSnapshot.child("sports").getValue().toString().equals("Rugby")) {
                                    int height = 100;
                                    int width = 100;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.rugby);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                    marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .snippet(uid));

                                }

                                if (dataSnapshot.child("sports").getValue().toString().equals("Running")) {
                                    int height = 100;
                                    int width = 100;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.shoe);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                    marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .snippet(uid));
                                }

                                if (dataSnapshot.child("sports").getValue().toString().equals("American Football")) {
                                    int height = 100;
                                    int width = 100;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.americanfootball);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                    marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .snippet(uid));

                                }

                                if (dataSnapshot.child("sports").getValue().toString().equals("Volleyball")) {
                                    int height = 100;
                                    int width = 100;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.volleyball);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                    marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .snippet(uid));


                                }

                                if (dataSnapshot.child("sports").getValue().toString().equals("Cricket")) {
                                    int height = 100;
                                    int width = 100;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.cricket);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                    marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .snippet(uid));

                                }

                                if (dataSnapshot.child("sports").getValue().toString().equals("default")) {
                                    int height = 100;
                                    int width = 100;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.footballer);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                    marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .snippet(uid));

                                }


                                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {

                                        marker.hideInfoWindow();
                                        Button btnFollow;
                                        TextView mName;
                                        myDialog.setContentView(R.layout.activity_popup);
                                        mName = myDialog.findViewById(R.id.name);
                                        mName.setText(marker.getTitle());
                                        btnFollow = myDialog.findViewById(R.id.btnfollow);

                                        btnFollow.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                usersDb.child(currentUId).child("connections").child("yeps").child(marker.getSnippet()).setValue(true);
                                                isConnectionMatch(marker.getSnippet());
                                                Toasty.success(getActivity(), "Connect!", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        myDialog.show();
                                    }
                                });


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


        });
                    return rootView;
            }




            private void isConnectionMatch(String userId) {
                DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("yeps").child(userId);
                currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            new FancyAlertDialog.Builder(getActivity())
                                    .setTitle("New Connection!")
                                    .setBackgroundColor(Color.parseColor("#6200EE"))  //Don't pass R.color.colorvalue
                                    .setMessage("Head over to Connects screen and chat with user!")
                                    .setNegativeBtnText("BROWSE")
                                    .setPositiveBtnBackground(Color.parseColor("#6200EE"))
                                    .setPositiveBtnText("MESSAGE")
                                    .setNegativeBtnBackground(Color.parseColor("#009DEE"))  //Don't pass R.color.colorvalue
                                    .setAnimation(Animation.POP)
                                    .isCancellable(true)
                                    .setIcon(R.drawable.ic_star_border_black_24dp, Icon.Visible)
                                    .OnPositiveClicked(new FancyAlertDialogListener() {
                                        @Override
                                        public void OnClick() {
                                            Intent intent = new Intent(getActivity(), MatchesFragment.class);
                                            startActivity(intent);
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


            public void updateUserStatus(String state) {
                String saveCurrentDate, saveCurrentTime;

                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());

                Calendar calForTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                saveCurrentTime = currentTime.format(calForTime.getTime());

                Map currentStateMap = new HashMap();
                currentStateMap.put("time", saveCurrentTime);
                currentStateMap.put("date", saveCurrentDate);
                currentStateMap.put("type", state);


                usersDb.child(currentUId).child("userState")
                        .updateChildren(currentStateMap);
            }
        }

